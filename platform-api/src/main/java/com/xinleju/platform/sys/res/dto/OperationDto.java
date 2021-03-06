package com.xinleju.platform.sys.res.dto;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * @author admin
 * 
 * 
 */
public class OperationDto extends BaseDto {

	// 编号
	private String code;

	// 名称
	private String name;

	// 上级
	private String parentId;

	// 图标
	private String icon;
	
	//系统
	private String appId;

	// 资源
	private String resourceId;

	// 地址
	private String url;

	// 排序
	private Long sort;

	// 按钮类别
	private String type;

	// 是否有效
	private String isValid;
	
	//说明
	private String remark;
	
	//系统名称
	private String appName;
	
	//资源名称
	private String resourceName;
	
	//上级按钮名称
	private String parentName;
	
	//全路径Id
	private String prefixId;
	//全路径排序
	private String prefixSort;
	//全路径名称
	private String prefixName;
	
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public String getPrefixSort() {
		return prefixSort;
	}
	public void setPrefixSort(String prefixSort) {
		this.prefixSort = prefixSort;
	}
	public String getPrefixName() {
		return prefixName;
	}
	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}
