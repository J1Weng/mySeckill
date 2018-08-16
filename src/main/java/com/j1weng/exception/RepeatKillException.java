package com.j1weng.exception;

public class RepeatKillException extends SeckillException {
private static final long serialVersionUID = 1L;

	public RepeatKillException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	 public RepeatKillException(String message, Throwable cause) {
	        super(message, cause);
	    }

	
}
