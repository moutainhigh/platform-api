package com.xinleju.platform.sys.base.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_BASE_CUSTOM_FORM_GROUP",desc="自定义表单分类")
public class CustomFormGroup extends BaseEntity{
	
		
	@Column(value="code",desc="编码")
	private String code;
    
  		
	@Column(value="name",desc="名称")
	private String name;
	
	@Column(value="parent_id",desc="父id")
	private String parentId;
    
	@Column(value="sort",desc="序号")
	private Long sort;
    
  		
	@Column(value="description",desc="说明")
	private String description;
    
	@Column(value="resource_id",desc="资源id")
	private String resourceId;
		
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
    
	
}
