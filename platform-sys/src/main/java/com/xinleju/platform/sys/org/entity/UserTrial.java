package com.xinleju.platform.sys.org.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author luorx
 *
 */

@Table(value="PT_SYS_ORG_USER_TRIAL",desc="试用用户表")
public class UserTrial extends BaseEntity{

	@Column(value="name",desc="真实姓名")
	private String name;

	@Column(value="email",desc="电子邮箱")
	private String email;

  		
	@Column(value="mobile",desc="移动电话")
	private String mobile;
    
  		
	@Column(value="remark",desc="说明")
	private String remark;
    

	@Column(value="wechat",desc="微信账号")
	private String weChat;

	@Column(value="due_time",desc="过期时间")
	private Date dueTime;

	@Column(value="user_id",desc="用户id")
	private String userId;


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWeChat() {
		return weChat;
	}
	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDueTime() {
		return dueTime;
	}

	public void setDueTime(Date dueTime) {
		this.dueTime = dueTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
