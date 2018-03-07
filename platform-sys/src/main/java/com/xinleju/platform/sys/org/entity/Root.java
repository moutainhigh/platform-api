package com.xinleju.platform.sys.org.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_ORG_ROOT",desc="根目录")
public class Root extends BaseEntity{
	
		
	@Column(value="name",desc="名称")
	private String name;
  		
	@Column(value="parent_id",desc="上级")
	private String parentId;
    
  		
	@Column(value="sort",desc="排序")
	private Long sort;
    
  		
	@Column(value="icon",desc="图标")
	private String icon;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
		
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
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
    
  		
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	
}
