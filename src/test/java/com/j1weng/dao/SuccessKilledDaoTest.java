package com.j1weng.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.j1weng.base.BaseTest;
import com.j1weng.entity.SuccessKilled;

public class SuccessKilledDaoTest extends BaseTest {

	@Autowired
	public SuccessKilledDao successKilledDao;
	
	@Test
	public void insertSuccessKilld() throws Exception{
		
		Long seckillId=1000L;
		Long userPhone=12345678910L;
		int insertCount=successKilledDao.insertSuccessKilled(seckillId, userPhone);
		System.out.println("insertCount="+insertCount);
	}
	@Test
	public void queryByIdWithSeckill() throws Exception{
		Long seckillId=1000L;
		Long userPhone=12345678910L;
		SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
		System.out.println(successKilled);
		System.out.println(successKilled.getSeckill());
	}
	
}
