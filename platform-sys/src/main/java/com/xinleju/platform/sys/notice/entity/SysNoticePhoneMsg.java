package com.xinleju.platform.sys.notice.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_NOTICE_PHONE_MSG",desc="手机信息表")
public class SysNoticePhoneMsg extends BaseEntity{
	
		
	@Column(value="phones",desc="接收方手机号")
	private String phones;
    
  		
	@Column(value="msg",desc="接收方信息")
	private String msg;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
	@Column(value="num",desc="发送次数")
	private Integer num;
    
  		
	@Column(value="phonel_server_id",desc="服务器Id")
	private String phonelServerId;
    
  		
	@Column(value="template",desc="消息模板")
	private String template;
    
  		
	@Column(value="is_template",desc="是否需要模板发送")
	private String isTemplate;
    
	@Column(value="remark",desc="备注")
	private String remark;
		
	public String getPhones() {
		return phones;
	}
	public void setPhones(String phones) {
		this.phones = phones;
	}
    
  		
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
    
  		
	public String getPhonelServerId() {
		return phonelServerId;
	}
	public void setPhonelServerId(String phonelServerId) {
		this.phonelServerId = phonelServerId;
	}
    
  		
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
    
  		
	public String getIsTemplate() {
		return isTemplate;
	}
	public void setIsTemplate(String isTemplate) {
		this.isTemplate = isTemplate;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	
}
