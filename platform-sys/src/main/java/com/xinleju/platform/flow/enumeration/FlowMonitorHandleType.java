package com.xinleju.platform.flow.enumeration;

/**
 * 流程监控点处理方式
 * 1、提醒2、挂起、3待办
 * @author daoqi
 *
 */
public enum FlowMonitorHandleType {

	SEND_TOREAD("发待阅", "1"), 
	FLOW_HANGUP("流程挂起", "2"), 
	SEND_TODO("发待办", "3");

	private String name;
	private String value;

	private FlowMonitorHandleType(String name, String value) {
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
