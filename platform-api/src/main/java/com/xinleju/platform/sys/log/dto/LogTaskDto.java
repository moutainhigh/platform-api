package com.xinleju.platform.sys.log.dto;

import com.xinleju.platform.base.dto.BaseDto;


import java.sql.Timestamp;



/**
 * @author admin
 * 
 *
 */
public class LogTaskDto extends BaseDto{

		
	//所属业务系统
	private String sysCode;
    
  		
	//自动任务编码
	private String taskCode;
    
  		
	//自动任务名称
	private String taskName;
    
  		
	//开始运行时间
	private Timestamp startTime;
    
  		
	//结束运行时间
	private Timestamp endTime;
    
  		
	//耗时
	private Long executeTime;
    
  		
	//是否成功
	private Boolean executeStatus;
    
  		
	//执行信息描述
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
