package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class InstanceVariableDto extends BaseDto{

		
	//变量名称
	private String name;
    
  		
	//变量值
	private String val;
    
  		
	//流程实例id
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
