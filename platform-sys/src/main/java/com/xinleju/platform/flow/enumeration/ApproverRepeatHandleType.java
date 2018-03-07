package com.xinleju.platform.flow.enumeration;

public enum ApproverRepeatHandleType {

	NOT_SKIP("不跳过", "1"), 
	FIRST("前置审批", "2"), 
	LAST("后置审批", "3");

	private String name;
	private String value;

	private ApproverRepeatHandleType(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
