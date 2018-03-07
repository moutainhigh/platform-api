package com.xinleju.platform.sys.org.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_ORG_COMPANY",desc="公司扩展")
public class Company extends BaseEntity{
	
		
	@Column(value="ref_id",desc="公司id")
	private String refId;
    
  		
	@Column(value="area_id",desc="地区")
	private String areaId;
    
  		
	@Column(value="address",desc="公司地址")
	private String address;
    
  		
	@Column(value="web_url",desc="网址")
	private String webUrl;
    
  		
	@Column(value="fax",desc="传真")
	private String fax;
    
  		
	@Column(value="legal",desc="法人代表")
	private String legal;
    
  		
		
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
    
  		
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
    
  		
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
    
  		
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
    
  		
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
    
  		
	public String getLegal() {
		return legal;
	}
	public void setLegal(String legal) {
		this.legal = legal;
	}
    
  		
	
}
