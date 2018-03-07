package com.xinleju.platform.sys.base.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_BASE_CUSTOM_ARCHIVES_ITEM",desc="自定义档案子表")
public class CustomArchivesItem extends BaseEntity{
	
		
	@Column(value="code",desc="编码")
	private String code;
    
  		
	@Column(value="name",desc="名称")
	private String name;
	
	@Column(value="description",desc="说明")
	private String description;
    
	@Column(value="status",desc="状态")
	private String status;
  		
	@Column(value="parent_id",desc="父节点ID")
	private String parentId;
	
	@Column(value="main_id",desc="主表ID")
	private String mainId;
	
	@Column(value="sort",desc="序号")
	private Long sort;
    
  		
		
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
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
