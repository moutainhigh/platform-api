package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class ApproveOperationDto extends BaseDto{

		
	//审批类型id
	private String approveId;
    
  		
	//审批操作id
	private String operationId;
    
  		
	//审批角色: 1,发起人,2,审批人,3 被协办人
	private String approveRole;
    
  		
	//显示名称: 操作类型的显示名称
	private String showName;
	
	//英文名称
	private String eName;
    
  		
	//默认意见
	private String defaultNote;
    
  		
	//是否需要填写意见
	private Boolean noteType;
    
  		
	//序号
	private Long sort;
	
	private String typeCode;//审批类型码
	private String operationCode;//操作码
	private String operationName;//操作名称
		
	public String getApproveId() {
		return approveId;
	}
	public void setApproveId(String approveId) {
		this.approveId = approveId;
	}
    
  		
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
    
  		
	public String getApproveRole() {
		return approveRole;
	}
	public void setApproveRole(String approveRole) {
		this.approveRole = approveRole;
	}
    
  		
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
    
  		
	public String getDefaultNote() {
		return defaultNote;
	}
	public void setDefaultNote(String defaultNote) {
		this.defaultNote = defaultNote;
	}
    
  		
	public Boolean getNoteType() {
		return noteType;
	}
	public void setNoteType(Boolean noteType) {
		this.noteType = noteType;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String geteName() {
		return eName;
	}
	public void seteName(String eName) {
		this.eName = eName;
	}
    
  		
}
