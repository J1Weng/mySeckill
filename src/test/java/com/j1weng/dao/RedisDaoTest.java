package com.j1weng.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.j1weng.base.BaseTest;
import com.j1weng.dao.cache.RedisDao;
import com.j1weng.entity.Seckill;

public class RedisDaoTest extends BaseTest {
	private long id=1001;
	@Autowired
	private RedisDao redisDao;
	@Autowired
	private SeckillDao seckillDao;
	@Test
	public void testSeckill() throws Exception{
		Seckill seckill=redisDao.getSeckill(id);
		if(seckill==null) {
			seckill=seckillDao.queryById(id);
			if(seckill!=null) {
				String result=redisDao.putSeckill(seckill);
				System.out.print(result);
				seckill=redisDao.getSeckill(id);
				System.out.print(seckill);
			}
		}
	}
	

}
