package com.xinleju.platform.sys.org.dto;

import com.xinleju.platform.base.dto.BaseDto;


import java.sql.Timestamp;



/**
 * @author sy
 * 
 *
 */
public class UserDto extends BaseDto{

		
	//工号
	private String jobNumber;
    
  		
	//真实姓名
	private String realName;
    
  		
	//性别
	private String isMale;
    
  		
	//登录名称
	private String loginName;
    
  		
	//密码
	private String password;
    
  		
	//联系电话
	private String contactPhone;
    
  		
	//工作电话
	private String workPhone;
    
  		
	//用户类型
	private String type;
    
  		
	//电子邮箱
	private String email;
	//电子邮箱密码
	private String emailPwd;
  		
	//出生日期
	private Timestamp birthday;
    
  		
	//年龄
	private Integer age;
    
  		
	//入职时间
	private Timestamp entryDate;
    
  		
	//离职时间
	private Timestamp leaveDate;
    
  		
	//移动电话
	private String mobile;
    
  		
	//说明
	private String remark;
    
  		
	//状态
	private String status;
    
  	//所属公司
	private String belongOrgId;
	
	//所属公司
	private String belongOrgName;
	
	//所属全路径
	private String prefixName;
	
	//工作时间
	private Timestamp workTime;
	
	//禁用时间
	private Timestamp disableTime;
	
	//微信账号
	private String weChat;
	
	//排序
	private Long sort;
	
	//禁用人
	private String  disabledId;	

	//用户岗位名称
	private String postName;

	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
    
  		
	public String getEmailPwd() {
		return emailPwd;
	}
	public void setEmailPwd(String emailPwd) {
		this.emailPwd = emailPwd;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
    
  		
	public String getIsMale() {
		return isMale;
	}
	public void setIsMale(String isMale) {
		this.isMale = isMale;
	}
    
  		
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
    
  		
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
  		
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
    
  		
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    
  		
	public Timestamp getBirthday() {
		return birthday;
	}
	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}
    
  		
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
    
  		
	public Timestamp getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Timestamp entryDate) {
		this.entryDate = entryDate;
	}
    
  		
	public Timestamp getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Timestamp leaveDate) {
		this.leaveDate = leaveDate;
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
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getBelongOrgId() {
		return belongOrgId;
	}
	public void setBelongOrgId(String belongOrgId) {
		this.belongOrgId = belongOrgId;
	}
	public Timestamp getWorkTime() {
		return workTime;
	}
	public void setWorkTime(Timestamp workTime) {
		this.workTime = workTime;
	}
	public Timestamp getDisableTime() {
		return disableTime;
	}
	public void setDisableTime(Timestamp disableTime) {
		this.disableTime = disableTime;
	}
	public String getWeChat() {
		return weChat;
	}
	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getDisabledId() {
		return disabledId;
	}
	public void setDisabledId(String disabledId) {
		this.disabledId = disabledId;
	}
	public String getBelongOrgName() {
		return belongOrgName;
	}
	public void setBelongOrgName(String belongOrgName) {
		this.belongOrgName = belongOrgName;
	}
	public String getPrefixName() {
		return prefixName;
	}
	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}
}
