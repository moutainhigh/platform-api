package com.xinleju.platform.flow.dto;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 审批信息列表  
 * 
 * @author daoqi
 *
 */
public class ApprovalList {
	
	//1、人员（工作组）
	private String groupKey;			//主键
	private String acPostId;			//所属岗位ID
	private String approverId;			//审批人ID
	private String approverName;		//审批人名称
	private int approverSeq;			//同岗位中审批人序号
	private int autoPass;				//是否自动通过
	private String proxy;				//代理人所在GROUP的ID
	private String proxyed;				//被代理人所在GROUP的ID
	private String proxyType;			//代理类型
	private String approverStatus;		//审批人状态
	
	//附属的任务信息
	private String taskId;				//任务ID
	private String msgId;				//任务对应的消息ID
	private String taskType;			//任务类型: 1:审批人(DL,ZB,ZC),2:发起人(GT),3:被协办人(XB)
	private String taskStatus;			//任务状态
	private Timestamp taskStartTime;
	private Timestamp taskEndTime;
	private String taskResult;			//审批结果
	private String taskResultName;		//审批结果文字
	private String taskComments;		//审批意见
	private String fromId;				//任务分配人ID
	private String fromName;			//任务分配人名称
	
	//2、岗位
	private String postId;				//岗位ID
	private String postName;			//岗位名称
	private String postType;			//岗位解析类型: 1:角色,2:岗位,3:人员
	private int postSeq;
	private String postStatus;
	private Timestamp postStartTime;
	private Timestamp postEndTime;
	private int leftPerson;				//岗位中剩余待处理人的数量
	
	//实例环节
	private String acId;				//环节实例ID
	private String acCode;				//环节实例code
	private String acName;				//环节实例名称
	private String acType;				//环节类型
	private String acStatus;			//环节状态
	private Date acStartTime;			//激活时间
	private Date acEndTime;				//完成时间
	private int acPx;					//环节顺序
	private int returnPx;				//打回环节id
	private int leftPost;				//1环节中剩余责任岗位数：环节对应的初始岗位数据 2join节点时记录已到达分支数，判断是否往下走
	private String ccIds;				//环节抄送人ID集合，以逗号分隔
	private String ccNames;				//环节抄送人Name集合，以逗号分隔
	private String nextAcIds;			//下一环节ACID，当是分支环节时以逗号分隔多个分支的ID
	private String preAcIds;			//上一环节ACID，当是聚合环节时以逗号分隔多个来源的ID
	private boolean setApproverWhenStart;	//发起人指定审批人
	private String fromReturn;			//此节点是否来产生于打回
	private String approverNull;		//审批人为空策略
	private String postNull;			//岗位为空能否提交策略
	
	private String approvalTypeId;		//审批类型ID
	private String approvalType;		//审批类型
	private boolean isAddLabel;			//是否加签
	private boolean isStart;			//审批人由发起人指定时为空是否能发起
	private String multiPost;			//多岗策略
	private String multiPerson;			//同岗多人
	private String source;				//来源：1：模板，2加签 ，3 前置代理  4后置代理
	private String instanceId;			//实例ID
	private String instanceName;		//实例名称
	private String instanceStatus;		//实例状态
	private String appId;				//应用ID
	private String flId;				//模板ID
	private String flCode;				//模板编码
	private String pcUrl;				//流程内嵌表单URL
	private String businessId;			//业务数据ID
	
	//页面操作变化
	private int changeType = 0;		//0：不变；1：新增；2：删除；3：修改
	
	public int getReturnPx() {
		return returnPx;
	}

	public void setReturnPx(int returnPx) {
		this.returnPx = returnPx;
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
		this.approverName = approverName;
	}

	public int getApproverSeq() {
		return approverSeq;
	}

	public void setApproverSeq(int approverSeq) {
		this.approverSeq = approverSeq;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Timestamp getTaskStartTime() {
		return taskStartTime;
	}

	public void setTaskStartTime(Timestamp taskStartTime) {
		this.taskStartTime = taskStartTime;
	}

	public Timestamp getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(Timestamp taskEndTime) {
		this.taskEndTime = taskEndTime;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public int getPostSeq() {
		return postSeq;
	}

	public void setPostSeq(int postSeq) {
		this.postSeq = postSeq;
	}

	public String getPostStatus() {
		return postStatus;
	}

	public void setPostStatus(String postStatus) {
		this.postStatus = postStatus;
	}

	public Timestamp getPostStartTime() {
		return postStartTime;
	}

	public void setPostStartTime(Timestamp postStartTime) {
		this.postStartTime = postStartTime;
	}

	public Timestamp getPostEndTime() {
		return postEndTime;
	}

	public void setPostEndTime(Timestamp postEndTime) {
		this.postEndTime = postEndTime;
	}

	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public String getAcCode() {
		return acCode;
	}

	public void setAcCode(String acCode) {
		this.acCode = acCode;
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}

	public String getAcStatus() {
		return acStatus;
	}

	public void setAcStatus(String acStatus) {
		this.acStatus = acStatus;
	}

	public Date getActivateTime() {
		return acStartTime;
	}

	public void setActivateTime(Date activateTime) {
		this.acStartTime = activateTime;
	}

	public Date getFinishTime() {
		return acEndTime;
	}

	public void setFinishTime(Date finishTime) {
		this.acEndTime = finishTime;
	}

	public String getApprovalTypeId() {
		return approvalTypeId;
	}

	public void setApprovalTypeId(String approvalTypeId) {
		this.approvalTypeId = approvalTypeId;
	}

	public String getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

	public boolean isAddLabel() {
		return isAddLabel;
	}

	public void setAddLabel(boolean isAddLabel) {
		this.isAddLabel = isAddLabel;
	}

	public String getMultiPost() {
		return multiPost;
	}

	public void setMultiPost(String multiPost) {
		this.multiPost = multiPost;
	}

	public String getMultiPerson() {
		return multiPerson;
	}

	public void setMultiPerson(String multiPerson) {
		this.multiPerson = multiPerson;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getAcPx() {
		return acPx;
	}

	public void setAcPx(int acPx) {
		this.acPx = acPx;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getTaskResult() {
		return taskResult;
	}

	public void setTaskResult(String taskResult) {
		this.taskResult = taskResult;
	}

	public String getTaskResultName() {
		return taskResultName;
	}

	public void setTaskResultName(String taskResultName) {
		this.taskResultName = taskResultName;
	}

	public String getTaskComments() {
		return taskComments;
	}

	public void setTaskComments(String taskComments) {
		this.taskComments = taskComments;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public Date getAcStartTime() {
		return acStartTime;
	}

	public void setAcStartTime(Date acStartTime) {
		this.acStartTime = acStartTime;
	}

	public Date getAcEndTime() {
		return acEndTime;
	}

	public void setAcEndTime(Date acEndTime) {
		this.acEndTime = acEndTime;
	}

	public int getLeftPerson() {
		return leftPerson;
	}

	public void setLeftPerson(int leftPerson) {
		this.leftPerson = leftPerson;
	}

	public int getLeftPost() {
		return leftPost;
	}

	public void setLeftPost(int leftPost) {
		this.leftPost = leftPost;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public int getAutoPass() {
		return autoPass;
	}

	public void setAutoPass(int autoPass) {
		this.autoPass = autoPass;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getFlId() {
		return flId;
	}

	public void setFlId(String flId) {
		this.flId = flId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getCcIds() {
		return ccIds;
	}

	public void setCcIds(String ccIds) {
		this.ccIds = ccIds;
	}

	public String getCcNames() {
		return ccNames;
	}

	public void setCcNames(String ccNames) {
		this.ccNames = ccNames;
	}

	public int getChangeType() {
		return changeType;
	}

	public void setChangeType(int changeType) {
		this.changeType = changeType;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getProxyed() {
		return proxyed;
	}

	public void setProxyed(String proxyed) {
		this.proxyed = proxyed;
	}

	public String getProxyType() {
		return proxyType;
	}

	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}

	public String getNextAcIds() {
		return nextAcIds;
	}

	public void setNextAcIds(String nextAcIds) {
		this.nextAcIds = nextAcIds;
	}

	public String getPreAcIds() {
		return preAcIds;
	}

	public void setPreAcIds(String preAcIds) {
		this.preAcIds = preAcIds;
	}

	public String getPcUrl() {
		return pcUrl;
	}

	public void setPcUrl(String pcUrl) {
		this.pcUrl = pcUrl;
	}

	public String getApproverStatus() {
		return approverStatus;
	}

	public void setApproverStatus(String approverStatus) {
		this.approverStatus = approverStatus;
	}

	public boolean isSetApproverWhenStart() {
		return setApproverWhenStart;
	}

	public void setSetApproverWhenStart(boolean setApproverWhenStart) {
		this.setApproverWhenStart = setApproverWhenStart;
	}

	public String getFlCode() {
		return flCode;
	}

	public void setFlCode(String flCode) {
		this.flCode = flCode;
	}

	public String getFromReturn() {
		return fromReturn;
	}

	public void setFromReturn(String fromReturn) {
		this.fromReturn = fromReturn;
	}

	public boolean isIsStart() {
		return isStart;
	}

	public void setIsStart(boolean isStart) {
		this.isStart = isStart;
	}

	public String getInstanceStatus() {
		return instanceStatus;
	}

	public void setInstanceStatus(String instanceStatus) {
		this.instanceStatus = instanceStatus;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getPostNull() {
		return postNull;
	}

	public void setPostNull(String postNull) {
		this.postNull = postNull;
	}

	public String getApproverNull() {
		return approverNull;
	}

	public void setApproverNull(String approverNull) {
		this.approverNull = approverNull;
	}
}
