package com.xinleju.platform.flow.dto;

import java.io.Serializable;

/**
 * 流程审批查询参数
 * 
 * @author daoqi
 *
 */
public class FlowQueryBean implements Serializable {

	private static final long serialVersionUID = -8323521112562011314L;

	/**
	 * 业务表单ID(必选项)
	 */
	private String businessId;

	/**
	 * 流程模板编码
	 */
	private String flCode;
	
	/**
	 * 业务系统编码
	 */
	private String appId;
	
	/**
	 * 当前用户
	 */
	private String userId;
	
	/**
	 * 租户编码：API请求时使用
	 */
	private String tendCode;
	
	//以下为内部参数
	
	/**
	 * 流程实例ID
	 */
	private String instanceId;
	
	/**
	 * 任务id
	 */
	private String taskId;
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getFlCode() {
		return flCode;
	}

	public void setFlCode(String flCode) {
		this.flCode = flCode;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getTendCode() {
		return tendCode;
	}

	public void setTendCode(String tendCode) {
		this.tendCode = tendCode;
	}
}
