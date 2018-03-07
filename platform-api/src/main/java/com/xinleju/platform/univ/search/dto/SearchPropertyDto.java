package com.xinleju.platform.univ.search.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author haoqp
 * 
 *
 */
public class SearchPropertyDto extends BaseDto{

		
	//属性编码
	private String code;
    
  		
	//属性名称
	private String name;
    
  		
	//属性类别：0=系统;1=自定义
	private Short category;
    
  		
	//属性类型
	private String type;
	
	// 状态：0-启用;1-禁用
	private Boolean status;
    
  		
		
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
    
  		
	public Short getCategory() {
		return category;
	}
	public void setCategory(Short category) {
		this.category = category;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
    
  		
}
