package com.xinleju.platform.flow.enumeration;

public enum OverdueAcHandle {

	NOTICE_APPROVER("通知当前审批人", "0"), 
	BACKTO_STARTER("打回到人", "1"), 
	AUTO_PASS("自动通过", "2");

	private String name;
	private String value;

	private OverdueAcHandle(String name, String value) {
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
