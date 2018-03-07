package com.xinleju.platform.flow.model;

import java.sql.Timestamp;

/**
 * 逾期环节
 * 
 * @author daoqi
 *
 */
public class OverdueAc {

	private String instanceId;				//流程实例ID
	private String instanceName;			//流程实例名称
	private String businessId;				//业务表单ID
	private String flCode;					//流程模板编码
	private String pcUrl;					//业务表单URL
	private String acId;					//环节实例ID
	private String acName;					//环节实例名称
	private Timestamp startTime;			//环节开始时间
	private int duration;					//环节持续时间
	private int overdueTime;				//逾期时间阀值
	private String overdueHandle;			//逾期处理方式
	private String currentApproverIds;		//环节当前审批人ID
	private String currentApproverNames;	//环节当前审批人名称

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getOverdueTime() {
		return overdueTime;
	}

	public void setOverdueTime(int overdueTime) {
		this.overdueTime = overdueTime;
	}

	public String getOverdueHandle() {
		return overdueHandle;
	}

	public void setOverdueHandle(String overdueHandle) {
		this.overdueHandle = overdueHandle;
	}

	public String getCurrentApproverIds() {
		return currentApproverIds;
	}

	public void setCurrentApproverIds(String currentApproverIds) {
		this.currentApproverIds = currentApproverIds;
	}

	public String getCurrentApproverNames() {
		return currentApproverNames;
	}

	public void setCurrentApproverNames(String currentApproverNames) {
		this.currentApproverNames = currentApproverNames;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getFlCode() {
		return flCode;
	}

	public void setFlCode(String flCode) {
		this.flCode = flCode;
	}

	public String getPcUrl() {
		return pcUrl;
	}

	public void setPcUrl(String pcUrl) {
		this.pcUrl = pcUrl;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

}
