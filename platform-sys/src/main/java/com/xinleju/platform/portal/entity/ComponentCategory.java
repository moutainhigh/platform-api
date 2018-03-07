package com.xinleju.platform.portal.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_PORTAL_COMPONENT_CATEGORY",desc="组件类别表")
public class ComponentCategory extends BaseEntity{
	
		
	@Column(value="category_name",desc="类别名称")
	private String categoryName;
    
  		
	@Column(value="category_code",desc="类别代码")
	private String categoryCode;
    
  		
	@Column(value="category_desc",desc="类别描述")
	private String categoryDesc;
    
  		
		
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    
  		
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
    
  		
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
    
  		
	
}
