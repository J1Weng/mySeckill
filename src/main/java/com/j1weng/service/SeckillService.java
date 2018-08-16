package com.j1weng.service;

import java.util.List;

import com.j1weng.dto.Exposer;
import com.j1weng.dto.SeckillExecution;
import com.j1weng.entity.Seckill;
import com.j1weng.exception.RepeatKillException;
import com.j1weng.exception.SeckillCloseException;
import com.j1weng.exception.SeckillException;

public interface SeckillService {
    /**
     * 查询全部的秒杀记录
     * @return
     */
	List<Seckill> getSeckillList();
	
	/**
     *查询单个秒杀记录
     * @param seckillId
     * @return
     */
	Seckill getById(Long seckillId);
	 
	//再往下，是我们最重要的行为的一些接口
    
	
	/**
     * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
     * @param seckillId
     */
	Exposer expostSeckillUrl(Long seckillId);
	
	/**
     * 执行秒杀操作，有可能失败，有可能成功，所以要抛出我们允许的异常
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
	SeckillExecution executeSeckill(Long seckillId,Long userPhone,String md5) throws 
	SeckillException,RepeatKillException,SeckillCloseException; 

	/**
     * 执行秒杀操作by存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
	
	SeckillExecution executeSeckillProcedure(Long seckillId,Long userPhone,String md5) ; 

}
