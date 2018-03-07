package com.xinleju.platform.flow.dto;

import java.sql.Timestamp;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class PassReadRecordDto extends BaseDto{

		
	//流程实例id
	private String fiId;
    
  		
	//流转人id
	private String transationUserId;
    
  		
	//流转人
	private String transationUserName;
    
  		
	//被流转人id
	private String toUserId;
    
  		
	//被流转人
	private String toUserName;
    
  		
	//流转日期
	private Timestamp transationDate;
    
  		
	//操作类型
	private String actionName;
    
	//消息id
  	private String msgId;

	//传阅取消日期
  	private Timestamp cancelPassReadTime;
  	
	//取消传阅人id
	private String cancelPassReadUserId;
  		
	//取消传阅人
	private String cancelPassReadUserName;
	public String getCancelPassReadUserId() {
		return cancelPassReadUserId;
	}
	public void setCancelPassReadUserId(String cancelPassReadUserId) {
		this.cancelPassReadUserId = cancelPassReadUserId;
	}
	public String getCancelPassReadUserName() {
		return cancelPassReadUserName;
	}
	public void setCancelPassReadUserName(String cancelPassReadUserName) {
		this.cancelPassReadUserName = cancelPassReadUserName;
	}
	public Timestamp getCancelPassReadTime() {
		return cancelPassReadTime;
	}
	public void setCancelPassReadTime(Timestamp cancelPassReadTime) {
		this.cancelPassReadTime = cancelPassReadTime;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getFiId() {
		return fiId;
	}
	public void setFiId(String fiId) {
		this.fiId = fiId;
	}
    
  		
	public String getTransationUserId() {
		return transationUserId;
	}
	public void setTransationUserId(String transationUserId) {
		this.transationUserId = transationUserId;
	}
    
  		
	public String getTransationUserName() {
		return transationUserName;
	}
	public void setTransationUserName(String transationUserName) {
		this.transationUserName = transationUserName;
	}
    
  		
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
    
  		
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public Timestamp getTransationDate() {
		return transationDate;
	}
	public void setTransationDate(Timestamp transationDate) {
		this.transationDate = transationDate;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
  		
}
