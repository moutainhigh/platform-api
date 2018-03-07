package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_VOUCHER_FORM_SETTING",desc="业务凭证表单地址设置")
public class VoucherFormSetting extends BaseEntity{
	
		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="biz_object_id",desc="业务对象id")
	private String bizObjectId;
    
  		
	@Column(value="url_type",desc="url类型值")
	private String urlType;
    
  		
	@Column(value="url",desc="打开单据地址url")
	private String url;
    
  		
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getBizObjectId() {
		return bizObjectId;
	}
	public void setBizObjectId(String bizObjectId) {
		this.bizObjectId = bizObjectId;
	}
    
  		
	public String getUrlType() {
		return urlType;
	}
	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}
    
  		
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    
  		
	
}
