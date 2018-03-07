package com.xinleju.platform.flow.enumeration;

public enum AutoPassType {

	AUTO_PASS("自动跳过", 1), 
	NORMAL("参与流程竞争跳过", 0), 
	NOT_PASS("强制不跳过", -1);

	private String name;
	private Integer value;

	private AutoPassType(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
