package com.xinleju.platform.sys.org.dto;

/**
 * @author sy
 * 
 *
 */
public class SyncUserArchive{

		
	//主键
	private String id;
  		
	//真实姓名
	private String realName;
  		
	//登录名称
	private String loginName;
	
	//电子邮箱
	private String email;
	
	//移动电话
	private String mobile;
	
  	//所属公司
	private String belongOrgId;
	
	//状态
	private String status;
	//是否删除
	private Boolean delflag;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getBelongOrgId() {
		return belongOrgId;
	}

	public void setBelongOrgId(String belongOrgId) {
		this.belongOrgId = belongOrgId;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getDelflag() {
		return delflag;
	}

	public void setDelflag(Boolean delflag) {
		this.delflag = delflag;
	}
	
}
