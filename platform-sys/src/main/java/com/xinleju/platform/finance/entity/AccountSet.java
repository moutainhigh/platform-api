package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_ACCOUNT_SET",desc="财务系统公司")
public class AccountSet extends BaseEntity{
	
		
	@Column(value="app_code",desc="所属系统")
	private String appCode;
    
  		
	@Column(value="register_id",desc="财务注册系统id")
	private String registerId;
    
  		
	@Column(value="code",desc="账套编码")
	private String code;
    
  		
	@Column(value="name",desc="账套名称")
	private String name;
    
  		
	@Column(value="company_code",desc="财务公司编码")
	private String companyCode;
    
  		
	@Column(value="company_name",desc="财务公司名称")
	private String companyName;
    
  		
		
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
    
  		
	public String getRegisterId() {
		return registerId;
	}
	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}
    
  		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
    
  		
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
    
  		
	
}
