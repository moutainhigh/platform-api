package com.xinleju.platform.flow.model;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 流程实例流转处理模型：审批人单元
 * 
 * 1、对应数据库中的工作组表
 * 2、一个环节模型对应一组审批人单元，有先后顺序
 * 3、审批人单元中含有岗位信息
 * 
 * @author daoqi
 *
 */
public class ApproverUnit {
	private String id;					//主键
	private String acPostId;			//所属岗位ID
	private String approverId;			//审批人ID
	private String approverName;		//审批人名称
	private int approverSeq;			//同岗位中审批人序号
	
	private Integer autoPass;			//是否自动跳过	1:自动跳过 0：流程状态（参与流程中的抢占跳过）-1：强制不跳过,
										//代理强制不跳过	
	
	private String status;				//审批人收到待办前监控挂起，此时没有任务,只有标在人员上
	
	//附属的任务信息
	private TaskUnit task;
	
	@JsonIgnore
	private PostUnit owner;
	
	private boolean change = false;
	
	private int dbAction = 0;			//1新增2删除 TODO zhangdaoqiang 与change合并
	
	private String proxy;				//代理人
	private String proxyed;				//被代理人
	private String proxyType;			//代理类型

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAcPostId() {
		return acPostId;
	}

	public void setAcPostId(String acPostId) {
		this.acPostId = acPostId;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		
		if(StringUtils.isNotEmpty(approverName) 
				&& approverName.contains("'")
				&& !approverName.contains("\\")) {
			approverName = approverName.replaceAll("\'", "\\\\'");
		}
		
		this.approverName = approverName;
	}

	public int getApproverSeq() {
		return approverSeq;
	}

	public void setApproverSeq(int approverSeq) {
		this.approverSeq = approverSeq;
		this.setChange(true);
	}

	public TaskUnit getTask() {
		return task;
	}

	public void setTask(TaskUnit task) {
		this.task = task;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public int getDbAction() {
		return dbAction;
	}

	public void setDbAction(int dbAction) {
		this.dbAction = dbAction;
	}

	public PostUnit getOwner() {
		return owner;
	}

	public void setOwner(PostUnit owner) {
		this.owner = owner;
	}

	public Integer getAutoPass() {
		return autoPass;
	}

	public void setAutoPass(Integer autoPass) {
		this.autoPass = autoPass;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
		this.setChange(true);
	}

	public String getProxyType() {
		return proxyType;
	}

	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}

	public String getProxyed() {
		return proxyed;
	}

	public void setProxyed(String proxyed) {
		this.proxyed = proxyed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
