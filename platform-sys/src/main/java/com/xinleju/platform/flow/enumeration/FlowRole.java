package com.xinleju.platform.flow.enumeration;

/**
 * 流程角色
 * 
 * @author daoqi
 *
 */
public enum FlowRole {

	STARTER("发起人", "1"),
	APPROVER_BEFORE("审批人-审批前", "2"),
	APPROVER_AFTER("审批人-审批后", "3"),
	ADMIN("管理员", "4"),	
	OTHER("其他人", "5");	
	
	private String name;
	private String value;

	private FlowRole(String name, String value) {
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
