package com.xinleju.platform.sys.notice.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_NOTICE_PHONE_SERVER",desc="短信服务")
public class SysNoticePhoneServer extends BaseEntity{
	
		
	@Column(value="code",desc="编号")
	private String code;
    
  		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="host",desc="web地址")
	private String host;
    
  		
	@Column(value="id_number",desc="企业ID")
	private String idNumber;
    
  		
	@Column(value="username",desc="登录用户")
	private String username;
    
  		
	@Column(value="password",desc="密码")
	private String password;
    
  		
	@Column(value="is_default",desc="是否默认")
	private String isDefault;
    
	@Column(value="remark",desc="备注")
	private String remark;
  		
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
    
  		
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
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
    
  		
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	
}
