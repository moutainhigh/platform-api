package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class MonitorPersonDto extends BaseDto{

		
	//监控人
	private String monitorName;
    
  		
	//监控人id
	private String monitorId;
    
  		
	//监控人类型(1-人员 2-岗位 3-角色)
	private String monitorType;
    
  		
	//监控设置id
	private String monitorSettingId;
    
  		
		
	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
    
  		
	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
    
  		
	public String getMonitorType() {
		return monitorType;
	}
	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}
    
  		
	public String getMonitorSettingId() {
		return monitorSettingId;
	}
	public void setMonitorSettingId(String monitorSettingId) {
		this.monitorSettingId = monitorSettingId;
	}
    
  		
}
