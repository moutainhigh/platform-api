package com.xinleju.platform.sys.base.dto;

import java.util.List;

public class CustomFormNodeDto {

	// 主键
	private String id;
	// 名称
	private String name;
	// 类型
	private String type;
	// 排序
	private Long sort;
	// 父节点ID
	private String parentId;
	// 目录ID
	private String rootId;
	// 子节点对象
	private List<CustomFormNodeDto> children;
	
	private String pId;

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<CustomFormNodeDto> getChildren() {
		return children;
	}

	public void setChildren(List<CustomFormNodeDto> children) {
		this.children = children;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

}
