package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_VOUCHER_TEMPLATE_TYPE",desc="凭证模板业务类型")
public class VoucherTemplateType extends BaseEntity{
	
		
	@Column(value="account_set_id",desc="财务系统公司id")
	private String accountSetId;
    
  		
	@Column(value="company_id",desc="公司id")
	private String companyId;
    
  		
	@Column(value="name",desc="业务类型名称")
	private String name;
    
  		
	@Column(value="parent_id",desc="父级id")
	private String parentId;
    
  		
		
	public String getAccountSetId() {
		return accountSetId;
	}
	public void setAccountSetId(String accountSetId) {
		this.accountSetId = accountSetId;
	}
    
  		
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
  		
	
}
