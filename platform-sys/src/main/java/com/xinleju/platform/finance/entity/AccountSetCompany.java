package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_ACCOUNT_SET_COMPANY",desc="对照公司")
public class AccountSetCompany extends BaseEntity{
	
		
	@Column(value="account_set_id",desc="财务系统公司id")
	private String accountSetId;
    
  		
	@Column(value="companyId",desc="公司id")
	private String companyId;
    
  		
	@Column(value="company_name",desc="公司名称")
	private String companyName;
    
  		
	@Column(value="payment_organ_id",desc="付款单位id")
	private String paymentOrganId;
    
  		
	@Column(value="payment_organ_name",desc="付款单位名称")
	private String paymentOrganName;
    
  		
	@Column(value="project_branch_id",desc="项目分期id")
	private String projectBranchId;
    
  		
	@Column(value="project_branch_name",desc="分期名称")
	private String projectBranchName;
    
  		
		
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
    
  		
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
    
  		
	public String getPaymentOrganId() {
		return paymentOrganId;
	}
	public void setPaymentOrganId(String paymentOrganId) {
		this.paymentOrganId = paymentOrganId;
	}
    
  		
	public String getPaymentOrganName() {
		return paymentOrganName;
	}
	public void setPaymentOrganName(String paymentOrganName) {
		this.paymentOrganName = paymentOrganName;
	}
    
  		
	public String getProjectBranchId() {
		return projectBranchId;
	}
	public void setProjectBranchId(String projectBranchId) {
		this.projectBranchId = projectBranchId;
	}
    
  		
	public String getProjectBranchName() {
		return projectBranchName;
	}
	public void setProjectBranchName(String projectBranchName) {
		this.projectBranchName = projectBranchName;
	}
    
  		
	
}
