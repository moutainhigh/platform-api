package com.xinleju.platform.finance.utils.voucherxml;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)    
@XmlType(name = "cashflowcase", propOrder = {})
public class Cashflowcase {
@XmlElement
private String	money;
@XmlElement
private String	moneyass;
@XmlElement
private String 	moneymain;
@XmlElement
private String	pk_cashflow;
public String getMoney() {
	return money;
}
public void setMoney(String money) {
	this.money = money;
}
public String getMoneyass() {
	return moneyass;
}
public void setMoneyass(String moneyass) {
	this.moneyass = moneyass;
}
public String getMoneymain() {
	return moneymain;
}
public void setMoneymain(String moneymain) {
	this.moneymain = moneymain;
}
public String getPk_cashflow() {
	return pk_cashflow;
}
public void setPk_cashflow(String pk_cashflow) {
	this.pk_cashflow = pk_cashflow;
}



}
