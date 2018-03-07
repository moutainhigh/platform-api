package com.xinleju.platform.flow.enumeration;

/**
 * 与操作精英的角色分类一一对应
 * 
 * @author daoqi
 *
 */
public enum TaskType {

	STARTER("发起人", "1"), 	//2:发起人(GT)
	APPROVER("审批人", "2"),	//1:审批人(DL,ZB,ZC)
	ASSIST("被协办人", "3");	//3:被协办人(XB)
	private String name;
	private String value;

	private TaskType(String name, String value) {
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
