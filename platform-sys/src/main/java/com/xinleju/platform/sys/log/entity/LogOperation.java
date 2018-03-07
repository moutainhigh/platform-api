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

@Table(value="PT_SYS_LOG_OPERATION",desc="操作日志")
public class LogOperation extends BaseEntity{
	
		
	@Column(value="login_name",desc="登录名")
	private String loginName;
    
  		
	@Column(value="name",desc="姓名")
	private String name;
    
  		
	@Column(value="com_id",desc="所在公司id")
	private String comId;
    
  		
	@Column(value="com_name",desc="所在公司名称")
	private String comName;
    
  		
	@Column(value="login_ip",desc="登录ip")
	private String loginIp;
    
  		
	@Column(value="login_browser",desc="登录的浏览器")
	private String loginBrowser;
    
  		
	@Column(value="operation_time",desc="操作时间")
	private Timestamp operationTime;
    
  		
	@Column(value="operation_type_id",desc="操作类型ID")
	private String operationTypeId;
    
  		
	@Column(value="sys_code",desc="操作业务系统code")//换成id
	private String sysCode;
    
  		
	@Column(value="node",desc="操作节点")
	private String node;
    
  		
	@Column(value="note",desc="操作记录内容")
	private String note;
    
  		
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getComId() {
		return comId;
	}
	public void setComId(String comId) {
		this.comId = comId;
	}
    
  		
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
    
  		
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
    
  		
	public String getLoginBrowser() {
		return loginBrowser;
	}
	public void setLoginBrowser(String loginBrowser) {
		this.loginBrowser = loginBrowser;
	}
    
  		
	public Timestamp getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(Timestamp operationTime) {
		this.operationTime = operationTime;
	}
    
  		
	public String getOperationTypeId() {
		return operationTypeId;
	}
	public void setOperationTypeId(String operationTypeId) {
		this.operationTypeId = operationTypeId;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
    
  		
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
    
  		
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
    
  		
	
}
