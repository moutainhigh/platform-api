package com.xinleju.platform.base.utils;

public enum PseudoDeletionSign {
	
	DELETE("删除",true),NOTDELETE("不删除",false);
	
	private String name;

	private Boolean  value;

	private PseudoDeletionSign(String name, Boolean value) {
		this.name=name;
		this.value=value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

	

}
