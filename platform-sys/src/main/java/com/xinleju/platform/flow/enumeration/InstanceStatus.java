package com.xinleju.platform.flow.enumeration;

/**
 * 1:运行中2:正常完成3:撤回4:打回7:作废9:挂起
 * @author daoqi
 *
 */
public enum InstanceStatus {

	RUNNING("运行中", "1"), 
	FINISHED("正常完成", "2"), 
	WITHDRAW("撤回", "3"),
	REJECT("打回", "4"),
	CANCEL("作废", "7"),
	HANGUP("挂起", "9");

	private String name;
	private String value;

	private InstanceStatus(String name, String value) {
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
