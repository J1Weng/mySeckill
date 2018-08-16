package com.j1weng.dto;

import com.j1weng.entity.SuccessKilled;
import com.j1weng.enums.SeckillStateEnum;

public class SeckillExecution {
	
	private Long seckillId;
	private int state;
	private String stateInfo;
	
	//当秒杀成功时，需要传递秒杀成功的对象回去
	private SuccessKilled successKilled;
	
	//秒杀成功返回所有信息
	public SeckillExecution(Long seckillId,SeckillStateEnum seckillStateEnum,SuccessKilled successKilled) {
		this.seckillId = seckillId;
		this.state = seckillStateEnum.getState();
		this.stateInfo = seckillStateEnum.getInfo();
		this.successKilled = successKilled;
	}
	//秒杀失败
	public SeckillExecution(Long seckillId,SeckillStateEnum seckillStateEnum) {
		this.seckillId = seckillId;
		this.state = seckillStateEnum.getState();
		this.stateInfo = seckillStateEnum.getInfo();
	}
	public Long getSeckillId() {
		return seckillId;
	}
	public void setSeckillId(Long seckillId) {
		this.seckillId = seckillId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}
	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}
	 @Override
	public String toString() {
	     return "SeckillExecution{" +
	                "seckillId=" + seckillId +
	                ", state=" + state +
	                ", stateInfo='" + stateInfo + '\'' +
	                ", successKilled=" + successKilled +
	                '}';
	 }

	

}
