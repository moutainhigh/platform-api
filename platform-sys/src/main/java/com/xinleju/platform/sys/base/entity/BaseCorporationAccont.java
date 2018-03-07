package com.xinleju.platform.sys.base.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_BASE_CORPORATION_ACCONT",desc="法人账号")
public class BaseCorporationAccont extends BaseEntity{
	
		
	@Column(value="bank_name",desc="开户银行")
	private String bankName;
    
  		
	@Column(value="bank_code",desc="银行账号")
	private String bankCode;
    
  		
	@Column(value="province_id",desc="省份")
	private String provinceId;
    
  		
	@Column(value="city_id",desc="城市")
	private String cityId;
    
  		
	@Column(value="corporation_id",desc="公司法人")
	private String corporationId;
    
  		
	@Column(value="address",desc="地址")
	private String address;
    
  		
	@Column(value="is_default",desc="是否默认")
	private String isDefault;
    
  		
	@Column(value="remark",desc="备注")
	private String remark;
    
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
    
  		
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
    
  		
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
    
  		
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
    
  		
	public String getCorporationId() {
		return corporationId;
	}
	public void setCorporationId(String corporationId) {
		this.corporationId = corporationId;
	}
    
  		
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
    
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	
}
