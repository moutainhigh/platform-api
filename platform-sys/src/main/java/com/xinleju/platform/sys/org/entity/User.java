package com.xinleju.platform.sys.org.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * @author sy
 * 
 * 
 */

@Table(value="PT_SYS_ORG_USER",desc="用户表")
public class User extends BaseEntity{
	
		
	@Column(value="job_number",desc="工号")
	private String jobNumber;
    
  		
	@Column(value="real_name",desc="真实姓名")
	private String realName;
    
  		
	@Column(value="is_male",desc="性别")
	private String isMale;
    
  		
	@Column(value="login_name",desc="登录名称")
	private String loginName;
    
  		
	@Column(value="password",desc="密码")
	private String password;
    
  		
	@Column(value="contact_phone",desc="联系电话")
	private String contactPhone;
    
  		
	@Column(value="work_phone",desc="工作电话")
	private String workPhone;
    
  		
	@Column(value="type",desc="用户类型")
	private String type;
    
  		
	@Column(value="email",desc="电子邮箱")
	private String email;
    
	@Column(value="email_pwd",desc="电子邮箱密码")
	private String emailPwd;
	
  		
	@Column(value="birthday",desc="出生日期")
	private Timestamp birthday;
    
  		
	@Column(value="age",desc="年龄")
	private Integer age;
    
  		
	@Column(value="entry_date",desc="入职时间")
	private Timestamp entryDate;
    
  		
	@Column(value="leave_date",desc="离职时间")
	private Timestamp leaveDate;
    
  		
	@Column(value="mobile",desc="移动电话")
	private String mobile;
    
  		
	@Column(value="remark",desc="说明")
	private String remark;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
	@Column(value="belong_org_id",desc="所属组织")
	private String belongOrgId;

		
	@Column(value="worktime",desc="工作时间")
	private Timestamp workTime;

	@Column(value="disabletime",desc="禁用时间")
	private Timestamp disableTime;
	
	@Column(value="wechat",desc="微信账号")
	private String weChat;
	
	@Column(value="sort",desc="排序")
	private Long sort;
	
	@Column(value="disabled_id",desc="禁用人")
	private String  disabledId;
	
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
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
	public String getEmailPwd() {
		return emailPwd;
	}
	public void setEmailPwd(String emailPwd) {
		this.emailPwd = emailPwd;
	}
    
  		
	
}
