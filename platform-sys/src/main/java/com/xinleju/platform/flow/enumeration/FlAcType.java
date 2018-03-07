package com.xinleju.platform.flow.enumeration;

public enum FlAcType {
	START("开始", "Start", "start", "1"), 
	TASK("普通", "Task", "task", "2"), 
	END("结束", "End", "end", "3"), 
	JOIN("聚合网关","Join", "join", "4"), 
	FORK("条件网关", "Fork", "fork", "5"), 
	CONNECTOR("连线", "Connector", "connector", "6"),
	CC("抄送", "CC", "cc", "7");

	private String label;
	private String nodeName;
	private String nodeType;
	private String acType;

	private FlAcType(String label, String nodeName, String nodeType, String acType) {
		this.label = label;
		this.nodeName = nodeName;
		this.nodeType = nodeType;
		this.acType = acType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

}
