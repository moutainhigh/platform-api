package com.xinleju.platform.finance.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class SysBizItemDto extends BaseDto{

		
	//编码
	private String code;
    
  		
	//名称
	private String name;
    
  		
	//所属系统
	private String appCode;
    
  		
	//地址接口
	private String url;
    
  	private String method;	
		
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
    
  		
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
    
  		
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
    
  		
}
