package com.xinleju.platform.flow.entity;

import java.sql.Timestamp;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_INSTANCE_READ_RECORD",desc="流程阅读记录")
public class InstanceReadRecord extends BaseEntity{
	
		
	@Column(value="fi_id",desc="流程实例id")
	private String fiId;
    
  		
	@Column(value="user_id",desc="阅读者Id")
	private String userId;
    
  		
	@Column(value="user_name",desc="阅读者名称")
	private String userName;
	
	@Column(value="post_name",desc="阅读者岗位")
	private String postName;
	
  		
	@Column(value="source",desc="来源")
	private String source;
    
  		
	@Column(value="ip",desc="来源IP")
	private String ip;
    
  		
	@Column(value="read_date",desc="阅读时间")
	private Timestamp readDate;
    
  		
		
	public String getFiId() {
		return fiId;
	}
	public void setFiId(String fiId) {
		this.fiId = fiId;
	}
    
  		
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    
  		
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
    
  		
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
    
  		
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
    
  		
	public Timestamp getReadDate() {
		return readDate;
	}
	public void setReadDate(Timestamp readDate) {
		this.readDate = readDate;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
    
  		
	
}
