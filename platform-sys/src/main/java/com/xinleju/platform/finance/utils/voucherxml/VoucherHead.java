package com.xinleju.platform.finance.utils.voucherxml;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "voucher_head", propOrder = {})
public class VoucherHead {
@XmlElement	
private String company;
@XmlElement	
private String reveiver;
@XmlElement(name="voucher_type")
private String voucherType;
@XmlElement(name="fiscal_year")
private String fiscalYear;
@XmlElement(name="accounting_period")
private String accountingPeriod;
@XmlElement(name="voucher_id")
private String voucherId;
@XmlElement(name="attachment_number")
private String attachmentNumber;
@XmlElement
private String prepareddate;
@XmlElement
private String enter;
@XmlElement
private String cashier;
@XmlElement
private String signature;
@XmlElement
private String checker;
@XmlElement(name="posting_date")
private String postingDate;
@XmlElement(name="posting_person")
private String postingPerson;
@XmlElement(name="voucher_making_system")
private String voucherMakingSystem;
@XmlElement
private String memo1; 
@XmlElement
private String memo2;
@XmlElement
private String reserve1;
@XmlElement
private String reserve2;
public String getCompany() {
	return company;
}
public void setCompany(String company) {
	this.company = company;
}

public String getReveiver() {
	return reveiver;
}
public void setReveiver(String reveiver) {
	this.reveiver = reveiver;
}
public String getVoucherType() {
	return voucherType;
}
public void setVoucherType(String voucherType) {
	this.voucherType = voucherType;
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
public String getVoucherId() {
	return voucherId;
}
public void setVoucherId(String voucherId) {
	this.voucherId = voucherId;
}
public String getAttachmentNumber() {
	return attachmentNumber;
}
public void setAttachmentNumber(String attachmentNumber) {
	this.attachmentNumber = attachmentNumber;
}
public String getPrepareddate() {
	return prepareddate;
}
public void setPrepareddate(String prepareddate) {
	this.prepareddate = prepareddate;
}
public String getEnter() {
	return enter;
}
public void setEnter(String enter) {
	this.enter = enter;
}
public String getCashier() {
	return cashier;
}
public void setCashier(String cashier) {
	this.cashier = cashier;
}
public String getSignature() {
	return signature;
}
public void setSignature(String signature) {
	this.signature = signature;
}
public String getChecker() {
	return checker;
}
public void setChecker(String checker) {
	this.checker = checker;
}
public String getPostingDate() {
	return postingDate;
}
public void setPostingDate(String postingDate) {
	this.postingDate = postingDate;
}
public String getPostingPerson() {
	return postingPerson;
}
public void setPostingPerson(String postingPerson) {
	this.postingPerson = postingPerson;
}
public String getVoucherMakingSystem() {
	return voucherMakingSystem;
}
public void setVoucherMakingSystem(String voucherMakingSystem) {
	this.voucherMakingSystem = voucherMakingSystem;
}
public String getMemo1() {
	return memo1;
}
public void setMemo1(String memo1) {
	this.memo1 = memo1;
}
public String getMemo2() {
	return memo2;
}
public void setMemo2(String memo2) {
	this.memo2 = memo2;
}
public String getReserve1() {
	return reserve1;
}
public void setReserve1(String reserve1) {
	this.reserve1 = reserve1;
}
public String getReserve2() {
	return reserve2;
}
public void setReserve2(String reserve2) {
	this.reserve2 = reserve2;
}


}
