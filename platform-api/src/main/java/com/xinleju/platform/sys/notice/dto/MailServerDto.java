package com.xinleju.platform.sys.notice.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class MailServerDto extends BaseDto{

		
	//smtp服务器地址
	private String host;
    
  		
	//邮件显示名称
	private String displayName;
    
  		
	//发件人真实的账户名
	private String username;
    
  		
	//邮件发送者密码
	private String password;
    
  		
	//是否默认
	private Boolean isDefault;
    
  		
	//服务器名称
	private String name;
    
  		
	//服务器code
	private String code;
	//备注
	private String remark;
    
  		
		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
    
  		
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
    
  		
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
    
  		
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
  		
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
}
