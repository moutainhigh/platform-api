package com.xinleju.platform.finance.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class AssMappingDto extends BaseDto{

		
	//辅助核算id
	private String assMappingId;
    
  		
	//核算代码
	private String assItemCode;
    
  		
	//核算名称
	private String assItemName;
    
  		
	//业务对象代码
	private String objectItemCode;
    
  		
	//业务对象名称
	private String objectItemName;
    
  		
	//业务对象id
	private String objectId;
    
	//业务对象id
	private String dataType;

	//账套id
	private String accountSetId;
	
	//推送状态
	private String sendStatus;
	
	//推送信息
	private String errmsg;
	
	//推送时间
	private String sendDate;

	public String getAccountSetId() {
		return accountSetId;
	}
	public void setAccountSetId(String accountSetId) {
		this.accountSetId = accountSetId;
	}


	public String getAssMappingId() {
		return assMappingId;
	}
	public void setAssMappingId(String assMappingId) {
		this.assMappingId = assMappingId;
	}
    
  		
	public String getAssItemCode() {
		return assItemCode;
	}
	public void setAssItemCode(String assItemCode) {
		this.assItemCode = assItemCode;
	}
    
  		
	public String getAssItemName() {
		return assItemName;
	}
	public void setAssItemName(String assItemName) {
		this.assItemName = assItemName;
	}
    
  		
	public String getObjectItemCode() {
		return objectItemCode;
	}
	public void setObjectItemCode(String objectItemCode) {
		this.objectItemCode = objectItemCode;
	}
    
  		
	public String getObjectItemName() {
		return objectItemName;
	}
	public void setObjectItemName(String objectItemName) {
		this.objectItemName = objectItemName;
	}
    
  		
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
}
