package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_ASS_TYPE",desc="辅助核算类型")
public class AssType extends BaseEntity{
	
		
	@Column(value="biz_object_name",desc="业务对象名称")
	private String bizObjectName;
    
  		
	@Column(value="biz_object_id",desc="业务对象id")
	private String bizObjectId;
    
  		
	@Column(value="account_set_id",desc="账套id")
	private String accountSetId;
    
  		
	@Column(value="ass_name",desc="辅助核算名称")
	private String assName;
    
  		
	@Column(value="type",desc="类型")
	private String type;
    
  		
	@Column(value="synchro",desc="是否同步")
	private String synchro;
    
  		
	@Column(value="is_direct_code",desc="传输类型")
	private String isDirectCode;
    
  		
	@Column(value="company_id",desc="公司id")
	private String companyId;
    
  		
		
	public String getBizObjectName() {
		return bizObjectName;
	}
	public void setBizObjectName(String bizObjectName) {
		this.bizObjectName = bizObjectName;
	}
    
  		
	public String getBizObjectId() {
		return bizObjectId;
	}
	public void setBizObjectId(String bizObjectId) {
		this.bizObjectId = bizObjectId;
	}
    
  		
	public String getAccountSetId() {
		return accountSetId;
	}
	public void setAccountSetId(String accountSetId) {
		this.accountSetId = accountSetId;
	}
    
  		
	public String getAssName() {
		return assName;
	}
	public void setAssName(String assName) {
		this.assName = assName;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getSynchro() {
		return synchro;
	}
	public void setSynchro(String synchro) {
		this.synchro = synchro;
	}
    
  		
	public String getIsDirectCode() {
		return isDirectCode;
	}
	public void setIsDirectCode(String isDirectCode) {
		this.isDirectCode = isDirectCode;
	}
    
  		
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
    
  		
	
}
