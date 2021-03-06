package com.xinleju.platform.flow.enumeration;

/**
 * 流程查询来源
 * 
 * @author daoqi
 *
 */
public enum InstanceQuerySource {

	TODO("待办", "DB"), 
	TOREAD("待阅", "DY"),
	START("发起", "start"),
	SEARCH("检索", "search");

	private String name;
	private String value;

	private InstanceQuerySource(String name, String value) {
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
