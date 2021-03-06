package com.xinleju.platform.finance.dto;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * @author admin
 * 
 *
 */
public class VoucherBillDto extends BaseDto{
		
	//财务系统公司id
	private String accountSetId;
  		
	//制单人
	private String enterName;
    
	//制单人编码
	private String enterCode;
  		
	//制单人id
	private String enterId;
    
  		
	//制单日期
	private String enterDate;
    
  		
	//输出日期
	private String exportDate;
    
  		
	//凭证字
	private String word;
    
  		
	//不完整凭证原因
	private String notFullError;
    
  		
	//输出状态
	private String sendStatus;
    
  		
	//失败原因
	private String errorCause;
    
  		
	//凭证号
	private String voucherNo;
    
  		
	//会计年度
	private String fiscalYear;
    
  		
	//会计期间/月份
	private String accountingPeriod;
    
  		
	//附单据数
	private String attachmentNumber;
    
  		
	//模板业务id
	private String templateId;
    
  		
	//模板业务类型id
	private String templateTypeId;
	
	//模板业务父类型id
	private String templateParentTypeId;
    
  		
	//账套的公司编码
	private String companyCode;
    
  		
	//贷方金额合计
	private String creditAmount;
    
  		
	//借方金额合计
	private String debitAmount;
    
  	//表单类型
	private String billType;
	
  	private String status; //1：完整凭证 2：不完整凭证
		
	public String getTemplateParentTypeId() {
		return templateParentTypeId;
	}
	public void setTemplateParentTypeId(String templateParentTypeId) {
		this.templateParentTypeId = templateParentTypeId;
	}
	public String getAccountSetId() {
		return accountSetId;
	}
	public void setAccountSetId(String accountSetId) {
		this.accountSetId = accountSetId;
	}
  		
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getEnterName() {
		return enterName;
	}
	public void setEnterName(String enterName) {
		this.enterName = enterName;
	}
  		
	public String getEnterCode() {
		return enterCode;
	}
	public void setEnterCode(String enterCode) {
		this.enterCode = enterCode;
	}
	public String getEnterId() {
		return enterId;
	}
	public void setEnterId(String enterId) {
		this.enterId = enterId;
	}
    
  		
	public String getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(String enterDate) {
		this.enterDate = enterDate;
	}
    
  		
	public String getExportDate() {
		return exportDate;
	}
	public void setExportDate(String exportDate) {
		this.exportDate = exportDate;
	}
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
    
  		
	public String getNotFullError() {
		return notFullError;
	}
	public void setNotFullError(String notFullError) {
		this.notFullError = notFullError;
	}
    
  		
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
    
  		
	public String getErrorCause() {
		return errorCause;
	}
	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
    
  		
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
    
  		
	public String getFiscalYear() {
		return fiscalYear;
	}
	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}
    
  		
	public String getAccountingPeriod() {
		return accountingPeriod;
	}
	public void setAccountingPeriod(String accountingPeriod) {
		this.accountingPeriod = accountingPeriod;
	}
    
  		
	public String getAttachmentNumber() {
		return attachmentNumber;
	}
	public void setAttachmentNumber(String attachmentNumber) {
		this.attachmentNumber = attachmentNumber;
	}
    
  		
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
    
  		
	public String getTemplateTypeId() {
		return templateTypeId;
	}
	public void setTemplateTypeId(String templateTypeId) {
		this.templateTypeId = templateTypeId;
	}
    
  		
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
    
  		
	public String getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}
    
  		
	public String getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(String debitAmount) {
		this.debitAmount = debitAmount;
	}
    
  		
}
