package com.xinleju.platform.portal.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class ComponentCategoryDto extends BaseDto{

		
	//类别名称
	private String categoryName;
    
  		
	//类别代码
	private String categoryCode;
    
  		
	//类别描述
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
