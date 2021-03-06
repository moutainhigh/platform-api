package com.xinleju.platform.flow.dto;

import java.sql.Timestamp;

import com.xinleju.platform.base.dto.BaseDto;



/**
 * @author admin
 * 
 *
 */
public class InstanceOperateLogDto extends BaseDto{

		
	//流程实例id
	private String instanceId;
    
  		
	//备注说明
	private String remark;
    
  		
	//环节Id
	private String acId;
    
  		
	//分组Id
	private String groupId;
    
  		
	//任务Id
	private String taskId;
    
  		
	//操作类型
	private String operateType;
    
  		
	//操作人员的ID组合
	private String operatorIds;
	
	//任务人的任务岗位所属的公司ID
	private String companyId;
	
	//任务人的任务岗位所属的部门ID
	private String deptId;
	
	//任务人的任务岗位所属的项目ID
	private String projectId;
	
	//任务人的任务岗位所属的分期ID
	private String branchId;
	
	
    
  		
	//操作时间
	private Timestamp operateTime;
    
  		
	//操作内容
	private String operateContent;
    
  		
	//删除时间
	private Timestamp deleteTime;
    
  		
		
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	public String getAcId() {
		return acId;
	}
	public void setAcId(String acId) {
		this.acId = acId;
	}
    
  		
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
    
  		
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
    
  		
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
    
  		
	public String getOperatorIds() {
		return operatorIds;
	}
	public void setOperatorIds(String operatorIds) {
		this.operatorIds = operatorIds;
	}
    
  		
	public Timestamp getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
    
  		
	public String getOperateContent() {
		return operateContent;
	}
	public void setOperateContent(String operateContent) {
		this.operateContent = operateContent;
	}
    
  		
	public Timestamp getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Timestamp deleteTime) {
		this.deleteTime = deleteTime;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
    
  		
}
