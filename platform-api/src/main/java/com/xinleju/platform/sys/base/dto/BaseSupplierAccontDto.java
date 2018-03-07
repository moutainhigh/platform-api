package com.xinleju.platform.sys.base.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class BaseSupplierAccontDto extends BaseDto{

		
	//供方id
	private String supplierId;
    
  		
	//开户银行
	private String bankName;
    
  		
	//银行账号
	private String bankCode;
    
  		
	//省份
	private String provinceId;
    
  		
	//城市
	private String cityId;
    
  		
	//地址
	private String address;
    
  		
	//是否默认
	private String isDefault;
    
  		
	//备注
	private String remark;
    
		
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
    
  		
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
