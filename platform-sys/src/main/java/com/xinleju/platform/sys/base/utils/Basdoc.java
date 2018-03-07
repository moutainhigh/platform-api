package com.xinleju.platform.sys.base.utils;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlType(name = "basdoc", propOrder = {})
public class Basdoc {
	@XmlAttribute
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@XmlElement(name="basdoc_head")
	private BasdocHead basdochead;
	
	public BasdocHead getBasdochead() {
		return basdochead;
	}
	public void setBasdochead(BasdocHead basdochead) {
		this.basdochead = basdochead;
	}
}
