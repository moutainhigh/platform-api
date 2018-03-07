package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_INSTANCE_VARIABLE",desc="流程传递变量")
public class InstanceVariable extends BaseEntity{
	
		
	@Column(value="name",desc="变量名称")
	private String name;
    
  		
	@Column(value="val",desc="变量值")
	private String val;
    
  		
	@Column(value="fi_id",desc="流程实例id")
	private String fiId;
    
  		
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
    
  		
	public String getFiId() {
		return fiId;
	}
	public void setFiId(String fiId) {
		this.fiId = fiId;
	}
    
  		
	
}
