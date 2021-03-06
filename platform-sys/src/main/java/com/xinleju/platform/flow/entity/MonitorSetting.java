package com.xinleju.platform.flow.entity;

import java.sql.Timestamp;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author admin
 * 
 * 
 */
@Table(value = "PT_FLOW_MONITOR_SETTING", desc = "流程监控设置")
public class MonitorSetting extends BaseEntity {

	@Column(value = "name", desc = "监控名称", fuzzyQuery = true)
	private String name;

	@Column(value = "code", desc = "监控编号", fuzzyQuery = true)
	private String code;

	@Column(value = "start_date", desc = "监控开始日期")
	private Timestamp startDate;

	@Column(value = "end_date", desc = "监控结束日期")
	private Timestamp endDate;

	@Column(value = "status", desc = "状态")
	private String status;

	@Column(value = "type", desc = "监控类型：1人员2模板3异常")
	private String type;

	// 备注
	@Column(value = "remark", desc = "备注")
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
