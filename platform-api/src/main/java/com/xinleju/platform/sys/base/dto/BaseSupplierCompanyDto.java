package com.xinleju.platform.sys.base.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class BaseSupplierCompanyDto extends BaseDto{

		
	//公司id
	private String companyId;
    
  		
	//供方id
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
