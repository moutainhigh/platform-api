package com.xinleju.platform.flow.dto;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 审批结果DTO
 * 
 * @author admin
 */
public class ApprovalSubmitDto {

	private String instanceId;
	private String taskId;
	private String msgId;			//待办消息ID
	private String operationType;
	private String operationName;
	private String userNote;
	private String userOpinionId;//用户自定义意见id
	
	//转办
	private String transferId;		//转办人ID
	private String transferName;	//转办人名称
	
	//协办
	//[{"id":"zhangsan", "name":"张三"},{...}]
	private String assisters;
	private String assistersName;
	
	//打回
	private String returnApprover;	//格式为：环节ID.审批人ID
	private String returnApproverName;
	private String approveRepeat;
	
	//撤回任务操作时待撤回的任务
//	private List<InstanceTaskDto> toWithdrawTasks;
	
	//校稿环节时通知业务需要的categoryId
	private String approvalTypeId;	//在后台确定当前位置时赋值
	private String categoryId;
	
	//打回前流程实例ID
	private String instanceIdBeforeReturn;

	//当前提交人，用于定时任务执行逾期扫描时使用
	private String currentSubmitUserId;

	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getUserNote() {
		return userNote;
	}
	public void setUserNote(String userNote) {
		this.userNote = userNote;
	}
	
	public String getUserOpinionId() {
		return userOpinionId;
	}
	public void setUserOpinionId(String userOpinionId) {
		this.userOpinionId = userOpinionId;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	public String getTransferName() {
		return transferName;
	}
	public void setTransferName(String transferName) {
		this.transferName = transferName;
	}
	public String getAssisters() {
		return assisters;
	}
	public void setAssisters(String assisters) {
		this.assisters = assisters;
	}
	public String getAssistersName() {
		return assistersName;
	}
	public void setAssistersName(String assistersName) {
		this.assistersName = assistersName;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getReturnApprover() {
		return returnApprover;
	}
	public void setReturnApprover(String returnApprover) {
		this.returnApprover = returnApprover;
	}
	public String getApproveRepeat() {
		return approveRepeat;
	}
	public void setApproveRepeat(String approveRepeat) {
		this.approveRepeat = approveRepeat;
	}
//	public List<InstanceTaskDto> getToWithdrawTasks() {
//		return toWithdrawTasks;
//	}
//	public void setToWithdrawTasks(List<InstanceTaskDto> toWithdrawTasks) {
//		this.toWithdrawTasks = toWithdrawTasks;
//	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getApprovalTypeId() {
		return approvalTypeId;
	}
	public void setApprovalTypeId(String approvalTypeId) {
		this.approvalTypeId = approvalTypeId;
	}
	public String getReturnApproverName() {
		return returnApproverName;
	}
	public void setReturnApproverName(String returnApproverName) {
		this.returnApproverName = returnApproverName;
	}
	public String getInstanceIdBeforeReturn() {
		return instanceIdBeforeReturn;
	}
	public void setInstanceIdBeforeReturn(String instanceIdBeforeReturn) {
		this.instanceIdBeforeReturn = instanceIdBeforeReturn;
	}

	public String getCurrentSubmitUserId() {
		return currentSubmitUserId;
	}

	public void setCurrentSubmitUserId(String currentSubmitUserId) {
		this.currentSubmitUserId = currentSubmitUserId;
	}
}
