package com.xinleju.platform.univ.search.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author haoqp
 * 
 * 
 */

@Table(value="PT_UNIV_SEARCH_PROPERTY",desc="属性表")
public class SearchProperty extends BaseEntity{
	
		
	@Column(value="code",desc="属性编码")
	private String code;
    
  		
	@Column(value="name",desc="属性名称")
	private String name;
    
  		
	@Column(value="category",desc="属性类别：0=系统;1=自定义")
	private Short category;
    
  		
	@Column(value="type",desc="属性类型")
	private String type;

	@Column(value="status",desc="状态：0-启用;1-禁用")
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
