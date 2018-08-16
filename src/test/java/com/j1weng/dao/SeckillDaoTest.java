package com.j1weng.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.j1weng.base.BaseTest;
import com.j1weng.entity.Seckill;

public class SeckillDaoTest extends BaseTest{
	@Autowired
	private SeckillDao seckillDao;
	
	@Test
	public void queryById() throws Exception{
		long seckillId=1000;
		Seckill seckill=seckillDao.queryById(seckillId);
		System.out.print(seckill.getName());
		System.out.print(seckill);
	}
	@Test
	public void queryAll()throws Exception{
		List<Seckill> seckills=seckillDao.queryAll(0,10);
		for(Seckill seckill:seckills) {
			System.out.println(seckill);
		}
	}
	@Test
	public void reduceNumber() throws Exception{
		Long seckillId=1000l;
		Date date=new Date();
		int updateCount=seckillDao.reduceNumber(seckillId, date);
		System.out.println(updateCount);
	}

}
