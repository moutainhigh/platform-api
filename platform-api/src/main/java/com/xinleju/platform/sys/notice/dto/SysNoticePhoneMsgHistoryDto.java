package com.xinleju.platform.sys.notice.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class SysNoticePhoneMsgHistoryDto extends BaseDto{

		
	//接收方手机号
	private String phones;
    
  		
	//接收方信息
	private String msg;
    
  		
	//状态
	private String status;
    
  		
	//发送次数
	private String num;
    
  		
	//服务器Id
	private String phonelServerId;
	
	
    
	//备注
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
    
  		
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
    
  		
	public String getPhonelServerId() {
		return phonelServerId;
	}
	public void setPhonelServerId(String phonelServerId) {
		this.phonelServerId = phonelServerId;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
  		
}
