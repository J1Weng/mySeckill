package com.j1weng.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.j1weng.dao.SeckillDao;
import com.j1weng.dao.SuccessKilledDao;
import com.j1weng.dao.cache.RedisDao;
import com.j1weng.dto.Exposer;
import com.j1weng.dto.SeckillExecution;
import com.j1weng.entity.Seckill;
import com.j1weng.entity.SuccessKilled;
import com.j1weng.enums.SeckillStateEnum;
import com.j1weng.exception.RepeatKillException;
import com.j1weng.exception.SeckillCloseException;
import com.j1weng.exception.SeckillException;
import com.j1weng.service.SeckillService;


@Service
public class SeckillServiceImpl implements SeckillService {
	//日志对象
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	//加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出我们的md5值，值任意给，越复杂越好
	private final String salt="qwerasdf'1'";
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	@Autowired
	private RedisDao redisDao;
	
	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(Long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer expostSeckillUrl(Long seckillId) {
		//优化点：缓存优化,一致性维护：建立在超时的基础上
		//访问redis
		Seckill seckill=redisDao.getSeckill(seckillId);
		if(seckill==null) {
			seckill=seckillDao.queryById(seckillId);
			if(seckill==null) {//说明查不到这个秒杀产品的记录

				return new Exposer(false,seckillId);
			}else {
				redisDao.putSeckill(seckill);				
			}
		}
		
		
		Date startTime=seckill.getStartTime();
		Date endTime=seckill.getEndTime();
		Date nowTime=new Date();
		//若当前时间不在秒杀时间范围内，秒杀不开启
		if(startTime.getTime()>nowTime.getTime()||endTime.getTime()<nowTime.getTime()) {
			return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
		}
		//秒杀开启
		//获得加密码md5
		String md5=getMD5(seckillId);
		//开启秒杀，返回加密码md5，秒杀商品id
		return new Exposer(true,md5,seckillId);
	}
	//md5密码生成器
	private String getMD5(long seckillId) {
		String base=seckillId+"/"+salt;
		String md5=DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	
	
	//秒杀是否成功，成功:减库存，增加明细；失败:抛出异常，事务回滚
	@Override
	@Transactional
	public SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		if(md5==null||!md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");//秒杀数据被重写了
		}
		//否则，执行秒杀逻辑：减库存+增加购买明细
		Date nowTime=new Date();
		try {
			int updateCount=seckillDao.reduceNumber(seckillId, nowTime);
			if(updateCount<=0) {
				//没有更新库存记录，说明秒杀结束
				throw new SeckillCloseException("seckill was closed");
			}else {
				//看是否该明细被重复插入，即用户是否重复秒杀
				int insertCount=successKilledDao.insertSuccessKilled(seckillId, userPhone);
				if(insertCount<=0) {
					throw new RepeatKillException("seckill repeated");
				}else{
				//秒杀成功,得到成功插入的明细记录,并返回成功秒杀的信息
				SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,successKilled);
			}
		}
	}catch(SeckillCloseException e1) {
		throw e1;
	}catch(RepeatKillException e2) {
		throw e2;
	}catch(Exception e) {
		logger.error(e.getMessage());
		throw new SeckillException("seckill inner error :"+e.getMessage());
	}
	}
	@Override
	public SeckillExecution executeSeckillProcedure(Long seckillId,Long userPhone,String md5) {
		if(md5==null || !md5.equals(getMD5(seckillId))) {
			return new SeckillExecution(seckillId,SeckillStateEnum.DATE_REWRITE);
		}
		Date killTime=new Date();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		//执行存储过程后，result被赋值；
		try {
			seckillDao.killByProcedure(map);
			//获取resul
			int result=MapUtils.getInteger(map,"result",-2);
			if(result==1) {
				SuccessKilled sk=successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,sk);
			}else {
				return new SeckillExecution(seckillId,SeckillStateEnum.stateOf(result));
				
			}
		}catch(Exception e) {
			logger.error(e.getMessage(),e);
			return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);

		}
				
	}
}

