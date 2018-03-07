package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_APPROVE_OPERATION",desc="审批操作关联表")
public class ApproveOperation extends BaseEntity{
	
		
	@Column(value="approve_id",desc="审批类型id")
	private String approveId;
    
  		
	@Column(value="operation_id",desc="审批操作id")
	private String operationId;
    
  		
	@Column(value="approve_role",desc="审批角色: 1,发起人,2,审批人,3 被协办人")
	private String approveRole;
    
  		
	@Column(value="show_name",desc="显示名称: 操作类型的显示名称")
	private String showName;
    
  		
	@Column(value="default_note",desc="默认意见")
	private String defaultNote;
    
  		
	@Column(value="note_type",desc="是否需要填写意见")
	private Boolean noteType;
    
  		
	@Column(value="sort",desc="序号")
	private Long sort;
    
  		
		
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
    
  		
	
}
