package com.xinleju.platform.flow.operation.concurrent;

public enum ConcurrentType {
	COMPETITION("1"),	//抢占
	SERIAL("2"),		//串行
	TOGETHER("3");		//并行
	
	private String code;
	
	ConcurrentType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
