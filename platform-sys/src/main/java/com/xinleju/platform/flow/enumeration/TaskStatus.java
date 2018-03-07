package com.xinleju.platform.flow.enumeration;

public enum TaskStatus {

	NOT_RUNNING("未运行", "1"), 
	RUNNING("运行中", "2"), 
	FINISHED("完成", "3"),
	SKIP("跳过", "4"),		//竞争时跳过
	WITHDRAW("流程撤回", "5");	//流程撤回

	private String name;
	private String value;

	private TaskStatus(String name, String value) {
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
