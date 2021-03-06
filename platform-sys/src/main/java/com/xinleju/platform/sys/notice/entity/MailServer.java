package com.xinleju.platform.sys.notice.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_NOTICE_MAIL_SERVER",desc="邮件服务设置")
public class MailServer extends BaseEntity{
	
		
	@Column(value="host",desc="smtp服务器地址")
	private String host;
    
  		
	@Column(value="display_name",desc="邮件显示名称")
	private String displayName;
    
  		
	@Column(value="username",desc="发件人真实的账户名")
	private String username;
    
  		
	@Column(value="password",desc="邮件发送者密码")
	private String password;
    
  		
	@Column(value="is_default",desc="是否默认")
	private Boolean isDefault;
    
  		
	@Column(value="name",desc="服务器名称")
	private String name;
    
  		
	@Column(value="code",desc="服务器code")
	private String code;
	@Column(value="remark",desc="备注")
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
