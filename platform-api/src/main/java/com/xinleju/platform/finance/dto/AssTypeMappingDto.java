package com.xinleju.platform.finance.dto;

public class AssTypeMappingDto{
	private String id;
	private String name;
	private String assItemCode;
	private String parentId;
	private String assIds;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAssItemCode() {
		return assItemCode;
	}
	public void setAssItemCode(String assItemCode) {
		this.assItemCode = assItemCode;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getAssIds() {
		return assIds;
	}
	public void setAssIds(String assIds) {
		this.assIds = assIds;
	}
	
}
