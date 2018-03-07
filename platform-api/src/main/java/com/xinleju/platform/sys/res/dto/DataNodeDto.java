package com.xinleju.platform.sys.res.dto;

import java.util.List;


public class DataNodeDto {
 private String id;
 private String name;
 
 private String type;
 private String code;
 
 private String appId;
 
 private String resourceId;
 
 private String openmode;
 private String resourceurl;
	//手机地址
	private String mobileUrl;


 
 private String level;
 
	private Boolean loaded;
	
	private Boolean isLeaf;
	
	private Boolean expanded;
		
	private Long lft;
	
	
	private Long rgt; 
 
 //是否分配权限  0是分配 1是未分配
 private Boolean isValid;
 //排序
 private Long sort;
 private String parentId;
 //子节点对象
 private List<DataNodeDto> children;

	public String getMobileUrl() {
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	public DataNodeDto() {
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
public String getParentId() {
	return parentId;
}
public void setParentId(String parentId) {
	this.parentId = parentId;
}
public List<DataNodeDto> getChildren() {
	return children;
}
public void setChildren(List<DataNodeDto> children) {
	this.children = children;
}
public Long getSort() {
	return sort;
}
public void setSort(Long sort) {
	this.sort = sort;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
public Boolean getIsValid() {
	return isValid;
}
public void setIsValid(Boolean isValid) {
	this.isValid = isValid;
}
public String getOpenmode() {
	return openmode;
}
public void setOpenmode(String openmode) {
	this.openmode = openmode;
}
public String getResourceurl() {
	return resourceurl;
}
public void setResourceurl(String resourceurl) {
	this.resourceurl = resourceurl;
}
public String getLevel() {
	return level;
}
public void setLevel(String level) {
	this.level = level;
}
public Boolean getLoaded() {
	return loaded;
}
public void setLoaded(Boolean loaded) {
	this.loaded = loaded;
}
public Boolean getIsLeaf() {
	return isLeaf;
}
public void setIsLeaf(Boolean isLeaf) {
	this.isLeaf = isLeaf;
}
public Boolean getExpanded() {
	return expanded;
}
public void setExpanded(Boolean expanded) {
	this.expanded = expanded;
}
public Long getLft() {
	return lft;
}
public void setLft(Long lft) {
	this.lft = lft;
}
public Long getRgt() {
	return rgt;
}
public void setRgt(Long rgt) {
	this.rgt = rgt;
}
public String getAppId() {
	return appId;
}
public void setAppId(String appId) {
	this.appId = appId;
}
public String getResourceId() {
	return resourceId;
}
public void setResourceId(String resourceId) {
	this.resourceId = resourceId;
}

 
}
