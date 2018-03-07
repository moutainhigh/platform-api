package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author admin
 * 
 * 
 */
@Table(value = "PT_FLOW_MONITOR_PERSON", desc = "监控人")
public class MonitorPerson extends BaseEntity {

	@Column(value = "monitor_name", desc = "监控人")
	private String monitorName;

	@Column(value = "monitor_id", desc = "监控人id")
	private String monitorId;

	@Column(value = "monitor_type", desc = "监控人类型")
	private String monitorType;

	@Column(value = "monitor_setting_id", desc = "监控设置id")
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
