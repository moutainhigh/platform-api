package com.xinleju.platform.finance.utils.contract;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(name = "ufinterface", propOrder = {})
public class Ufinterface {
	@XmlAttribute
	private String account;
	@XmlAttribute
	private String billtype;
	@XmlAttribute
	private String filename;
	@XmlAttribute
	private String isexchange;
	@XmlAttribute
	private String proc;
	@XmlAttribute
	private String receiver;
	@XmlAttribute
	private String replace;
	@XmlAttribute
	private String roottag;
	@XmlAttribute
	private String sender;
	@XmlAttribute
	private String subbilltype;
	@XmlElement
	private Bill bill;
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the billtype
	 */
	public String getBilltype() {
		return billtype;
	}
	/**
	 * @param billtype the billtype to set
	 */
	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the isexchange
	 */
	public String getIsexchange() {
		return isexchange;
	}
	/**
	 * @param isexchange the isexchange to set
	 */
	public void setIsexchange(String isexchange) {
		this.isexchange = isexchange;
	}
	/**
	 * @return the proc
	 */
	public String getProc() {
		return proc;
	}
	/**
	 * @param proc the proc to set
	 */
	public void setProc(String proc) {
		this.proc = proc;
	}
	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	/**
	 * @return the replace
	 */
	public String getReplace() {
		return replace;
	}
	/**
	 * @param replace the replace to set
	 */
	public void setReplace(String replace) {
		this.replace = replace;
	}
	/**
	 * @return the roottag
	 */
	public String getRoottag() {
		return roottag;
	}
	/**
	 * @param roottag the roottag to set
	 */
	public void setRoottag(String roottag) {
		this.roottag = roottag;
	}
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * @return the subbilltype
	 */
	public String getSubbilltype() {
		return subbilltype;
	}
	/**
	 * @param subbilltype the subbilltype to set
	 */
	public void setSubbilltype(String subbilltype) {
		this.subbilltype = subbilltype;
	}
	/**
	 * @return the bill
	 */
	public Bill getBill() {
		return bill;
	}
	/**
	 * @param bill the bill to set
	 */
	public void setBill(Bill bill) {
		this.bill = bill;
	}
	
}
