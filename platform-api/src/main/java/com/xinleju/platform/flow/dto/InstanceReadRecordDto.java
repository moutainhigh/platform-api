package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;


import java.sql.Timestamp;



/**
 * @author admin
 * 
 *
 */
public class InstanceReadRecordDto extends BaseDto{

		
	//流程实例id
	private String fiId;
    
  		
	//阅读者Id
	private String userId;
    
  		
	//阅读者名称
	private String userName;
	//阅读者岗位
	private String postName;
  		
	//来源
	private String source;
    
  		
	//来源IP
	private String ip;
    
  		
	//阅读时间
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
