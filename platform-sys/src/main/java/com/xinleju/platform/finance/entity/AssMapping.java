package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_ASS_MAPPING",desc="辅助核算明细")
public class AssMapping extends BaseEntity{
	@Column(value="ass_mapping_id",desc="辅助核算id")
	private String assMappingId;
    
	@Column(value="ass_item_code",desc="核算代码")
	private String assItemCode;
    
	@Column(value="ass_item_name",desc="核算名称")
	private String assItemName;
    
	@Column(value="object_item_code",desc="业务对象代码")
	private String objectItemCode;
    
	@Column(value="object_item_name",desc="业务对象名称")
	private String objectItemName;
    
	@Column(value="object_id",desc="业务对象id")
	private String objectId;
	
	@Column(value="send_status",desc="推送状态")
	private String sendStatus;
	
	@Column(value="errmsg",desc="推送信息")
	private String errmsg;
	
	@Column(value="send_date",desc="推送时间")
	private String sendDate;

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
