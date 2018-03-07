package com.xinleju.platform.flow.dto;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.xinleju.platform.base.dto.BaseDto;



/**
 * @author admin
 * 
 *
 */
public class InstanceTaskDto extends BaseDto{

		
	//任务参与者
	private String participantName;
    
  		
	//任务完成时间
	private Timestamp endDate;
    
  		
	//工作组id
	private String groupId;
    
  		
	//操作名称
	private String operationName;
    
  		
	//操作类型: DH:打回,TG :通过,(GLY_SJ:审结,GLY_TG:跳过),SYS_TG:系统,JS:接受,BJS:不接受,XB:协办,ZB:转办,GT:沟通,WYY无异议,HF:回复
	private String operationCode;
    
  		
	//意见
	private String userNote;
    
  		
	//关联任务id
	private String relationTaskId;
    
  		
	//关联任务分配人id
	private String relationParticipantId;
    
  		
	//关联任务分配人
	private String relationParticipant;
    
  		
	//是否有效
	private Boolean disable;
    
  		
	//无效方式: 1:人工删除完成,2:被动完成
	private String disableType;
    
  		
	//任务参与者id
	private String participantId;
    
  		
	//来源: 1:加签 ,2:正常 ,3:代理 ,4:监控 ,5:替换 
	private String source;
    
  		
	//来源id
	private String sourceId;
    
  		
	//任务类型: 1:审批人(DL,ZB,ZC),2:发起人(GT),3:被协办人(XB)
	private String type;
    
  		
	//状态: 1:运行,2:完成
	private String status;
    
  		
	//任务审批者
	private String approverName;
    
  		
	//任务审批者id
	private String approverId;
    
  		
	//任务激活时间
	private Timestamp activateDate;
    
  		
	//传输数据用的  instanceId
	private String instanceId;//实例ID
	private String currentPersonNameText;//当前审批人的名称文本
	private String currentPersonIdText;//当前审批人的ID文本
	
	/**
	 * 任务消息是否被打开过：1：打开过0：未打开过
	 */
	private int isOpen;
	
	private String approveType;
	
	private String msgId;
	

	//协办任务开始时间
	private Timestamp xbStartDate;
		
	public Timestamp getXbStartDate() {
		return xbStartDate;
	}
	public void setXbStartDate(Timestamp xbStartDate) {
		this.xbStartDate = xbStartDate;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
    
  		
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
    
  		
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
    
  		
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
    
  		
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
    
  		
	public String getUserNote() {
		return userNote;
	}
	public void setUserNote(String userNote) {
		this.userNote = userNote;
	}
    
  		
	public String getRelationTaskId() {
		return relationTaskId;
	}
	public void setRelationTaskId(String relationTaskId) {
		this.relationTaskId = relationTaskId;
	}
    
  		
	public String getRelationParticipantId() {
		return relationParticipantId;
	}
	public void setRelationParticipantId(String relationParticipantId) {
		this.relationParticipantId = relationParticipantId;
	}
    
  		
	public String getRelationParticipant() {
		return relationParticipant;
	}
	public void setRelationParticipant(String relationParticipant) {
		this.relationParticipant = relationParticipant;
	}
    
  		
	public Boolean getDisable() {
		return disable;
	}
	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
    
  		
	public String getDisableType() {
		return disableType;
	}
	public void setDisableType(String disableType) {
		this.disableType = disableType;
	}
    
  		
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
    
  		
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
    
  		
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public String getApproverName() {
		return approverName;
	}
	public void setApproverName(String approverName) {
		
		if(StringUtils.isNotEmpty(approverName) && approverName.contains("'")) {
			approverName = approverName.replaceAll("\'", "\\\\'");
		}
		
		this.approverName = approverName;
	}
    
  		
	public String getApproverId() {
		return approverId;
	}
	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}
    
  		
	public Timestamp getActivateDate() {
		return activateDate;
	}
	public void setActivateDate(Timestamp activateDate) {
		this.activateDate = activateDate;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getCurrentPersonNameText() {
		return currentPersonNameText;
	}
	public void setCurrentPersonNameText(String currentPersonNameText) {
		this.currentPersonNameText = currentPersonNameText;
	}
	public String getCurrentPersonIdText() {
		return currentPersonIdText;
	}
	public void setCurrentPersonIdText(String currentPersonIdText) {
		this.currentPersonIdText = currentPersonIdText;
	}
	public int getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}
	public String getApproveType() {
		return approveType;
	}
	public void setApproveType(String approveType) {
		this.approveType = approveType;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
