package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class MonitorFlDto extends BaseDto{

		
	//流程模板id
	private String flId;
    
  		
	//监控设置id
	private String monitorSettingId;
    
  		
	//流程模板名称
	private String flName;
	
	private String busiObjectName;
	private String code;
		
	public String getFlId() {
		return flId;
	}
	public void setFlId(String flId) {
		this.flId = flId;
	}
    
  		
	public String getMonitorSettingId() {
		return monitorSettingId;
	}
	public void setMonitorSettingId(String monitorSettingId) {
		this.monitorSettingId = monitorSettingId;
	}
    
  		
	public String getFlName() {
		return flName;
	}
	public void setFlName(String flName) {
		this.flName = flName;
	}
	public String getBusiObjectName() {
		return busiObjectName;
	}
	public void setBusiObjectName(String busiObjectName) {
		this.busiObjectName = busiObjectName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
}
