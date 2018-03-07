package com.xinleju.platform.sys.base.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_BASE_SUPPLIER_COMPANY",desc="供方所属公司")
public class BaseSupplierCompany extends BaseEntity{
	
		
	@Column(value="company_id",desc="公司id")
	private String companyId;
    
  		
	@Column(value="supper_id",desc="供方id")
	private String supperId;
    
  		
		
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
    
  		
	public String getSupperId() {
		return supperId;
	}
	public void setSupperId(String supperId) {
		this.supperId = supperId;
	}
    
  		
	
}
