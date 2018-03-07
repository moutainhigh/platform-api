package com.xinleju.platform.flow.dto;

import java.util.ArrayList;
import java.util.List;

public class BatchModifyReaderDto {
	
	private String instanceIdText;//流程实例Id的逗号拼接字符串
	private String operateType;//操作类型 add/reset/delete
	private List<AddReaderDto> readerList = new ArrayList<AddReaderDto>();
	
	private String flowIdText;//流程实例Id的逗号拼接字符串  partcipantList flowIdText
	private List<ParticipantDto> partcipantList = new ArrayList<ParticipantDto>();
	
	private String synInstance;//是否同步更新流程实例 1是  0否
	public String getSynInstance() {
		return synInstance;
	}
	public void setSynInstance(String synInstance) {
		this.synInstance = synInstance;
	}
	public String getInstanceIdText() {
		return instanceIdText;
	}
	public void setInstanceIdText(String instanceIdText) {
		this.instanceIdText = instanceIdText;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public List<AddReaderDto> getReaderList() {
		return readerList;
	}
	public void setReaderList(List<AddReaderDto> readerList) {
		this.readerList = readerList;
	}
	public String getFlowIdText() {
		return flowIdText;
	}
	public void setFlowIdText(String flowIdText) {
		this.flowIdText = flowIdText;
	}
	public List<ParticipantDto> getPartcipantList() {
		return partcipantList;
	}
	public void setPartcipantList(List<ParticipantDto> partcipantList) {
		this.partcipantList = partcipantList;
	}

}
