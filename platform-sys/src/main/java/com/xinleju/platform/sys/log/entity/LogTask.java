package com.xinleju.platform.sys.log.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_LOG_TASK",desc="自动任务日志")
public class LogTask extends BaseEntity{
	
		
	@Column(value="sys_code",desc="所属业务系统")
	private String sysCode;
    
  		
	@Column(value="task_code",desc="自动任务编码")
	private String taskCode;
    
  		
	@Column(value="task_name",desc="自动任务名称")
	private String taskName;
    
  		
	@Column(value="start_time",desc="开始运行时间")
	private Timestamp startTime;
    
  		
	@Column(value="end_time",desc="结束运行时间")
	private Timestamp endTime;
    
  		
	@Column(value="execute_time",desc="耗时")
	private Long executeTime;
    
  		
	@Column(value="execute_status",desc="是否成功")
	private Boolean executeStatus;
    
  		
	@Column(value="run_info",desc="执行信息描述")
	private String runInfo;
    
  		
		
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
    
  		
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
    
  		
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
    
  		
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
    
  		
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
    
  		
	public Long getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(Long executeTime) {
		this.executeTime = executeTime;
	}
    
  		
	public Boolean getExecuteStatus() {
		return executeStatus;
	}
	public void setExecuteStatus(Boolean executeStatus) {
		this.executeStatus = executeStatus;
	}
    
  		
	public String getRunInfo() {
		return runInfo;
	}
	public void setRunInfo(String runInfo) {
		this.runInfo = runInfo;
	}
    
  		
	
}