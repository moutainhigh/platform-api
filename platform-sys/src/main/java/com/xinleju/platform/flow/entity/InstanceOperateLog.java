package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_INSTANCE_OPERATE_LOG",desc="流程操作日志")
public class InstanceOperateLog extends BaseEntity{
	
		
	@Column(value="instance_id",desc="流程实例id")
	private String instanceId;
    
	
	@Column(value="remark",desc="备注说明")
	private String remark;
    
  		
	@Column(value="ac_id",desc="环节Id")
	private String acId;
    
	
	@Column(value="group_id",desc="分组Id")
	private String groupId;
    
  		
	@Column(value="task_id",desc="任务Id")
	private String taskId;
    
  		
	@Column(value="operate_type",desc="操作类型")
	private String operateType;
    
  		
	@Column(value="operator_ids",desc="操作人员的ID组合")
	private String operatorIds;
    
	@Column(value="company_id",desc="任务人的任务岗位所属的公司ID")
	private String companyId;
	
	@Column(value="dept_id",desc="任务人的任务岗位所属的部门ID")
	private String deptId;
	
	@Column(value="project_id",desc="任务人的任务岗位所属的项目ID")
	private String projectId;
	
	@Column(value="branch_id",desc="任务人的任务岗位所属的分期ID")
	private String branchId;
	
  		
	@Column(value="operate_time",desc="操作时间")
	private Timestamp operateTime;
    
	@Column(value="operate_content",desc="操作内容")
	private String operateContent;
    
  		
	@Column(value="delete_time",desc="删除时间")
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
