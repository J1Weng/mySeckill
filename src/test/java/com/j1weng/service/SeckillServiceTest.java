package com.j1weng.service;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.j1weng.base.BaseTest;
import com.j1weng.dto.Exposer;
import com.j1weng.dto.SeckillExecution;
import com.j1weng.entity.Seckill;
import com.j1weng.exception.RepeatKillException;
import com.j1weng.exception.SeckillCloseException;

public class SeckillServiceTest extends BaseTest {

	private final Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;
	
	//@Test
	public void getSeckillList() throws Exception{
		List<Seckill> seckills=seckillService.getSeckillList();
		System.out.println(seckills);
	}
	//@Test
	public void getById() throws Exception{
		Long seckillId=1000l;
		Seckill seckill=seckillService.getById(seckillId);
		System.out.println(seckill);
	}
	//@Test
	public void testSeckillLogic() throws Exception{
		Long seckillId=1000l;
		Long userPhone=12345678910l;
		
		Exposer exposer=seckillService.expostSeckillUrl(seckillId);
		if(exposer.isExposed()) {
			System.out.println(exposer);
			String md5=exposer.getMd5();
			try {
				SeckillExecution seckillExecution=seckillService.executeSeckill(seckillId, userPhone, md5);
				System.out.print(seckillExecution);
			}catch(RepeatKillException e) {
				e.printStackTrace();
			}catch(SeckillCloseException e) {
				e.printStackTrace();
			}
		}else {
			//秒杀未开启
			System.out.print(exposer);
		}
	}
	@Test
	public void executeSeckillProcedure() {
		long seckillId=1001;
		long phone=18158501113l;
		Exposer exposer=seckillService.expostSeckillUrl(seckillId);
		if(exposer.isExposed()) {
			String md5=exposer.getMd5();
			SeckillExecution execution=seckillService.executeSeckillProcedure(seckillId, phone, md5);
			logger.info(execution.getStateInfo());
		}
	}
	
	
}
