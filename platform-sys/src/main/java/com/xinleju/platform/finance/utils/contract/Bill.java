package com.xinleju.platform.finance.utils.contract;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "bill", propOrder = {})
public class Bill {
	@XmlAttribute
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@XmlElement(name="billhead")
	private BillHead billhead;
	/**
	 * @return the billhead
	 */
	public BillHead getBillhead() {
		return billhead;
	}
	/**
	 * @param billhead the billhead to set
	 */
	public void setBillhead(BillHead billhead) {
		this.billhead = billhead;
	}
	
}
