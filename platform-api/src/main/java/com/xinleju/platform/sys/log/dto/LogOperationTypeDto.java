package com.xinleju.platform.sys.log.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class LogOperationTypeDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//类型名称
	private String name;
    
  		
	//描述
	private String remark;
    
  		
    
  		
	//排序
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
    
  		
}
