package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class MobileFormDto extends BaseDto{

		
	//流程实例ID
	private String instanceId;
    
  		
	//模板ID
	private String flId;
    
  		
	//业务对象ID
	private String businessObjectId;
    
  		
	//业务ID
	private String businessId;
    
  		
	//表单字段名称
	private String name;
    
  		
	//表单字段值
	private String value;
    
	private int sort;
  		
		
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
    
  		
	public String getFlId() {
		return flId;
	}
	public void setFlId(String flId) {
		this.flId = flId;
	}
    
  		
	public String getBusinessObjectId() {
		return businessObjectId;
	}
	public void setBusinessObjectId(String businessObjectId) {
		this.businessObjectId = businessObjectId;
	}
    
  		
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
    
  		
}
