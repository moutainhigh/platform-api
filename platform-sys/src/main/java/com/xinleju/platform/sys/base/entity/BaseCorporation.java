package com.xinleju.platform.sys.base.entity;

import java.sql.Timestamp;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_BASE_CORPORATION",desc="公司法人")
public class BaseCorporation extends BaseEntity{
	
		
	@Column(value="name",desc="法人名称")
	private String name;
    
  		
	@Column(value="code",desc="法人编码")
	private String code;
    
  		
	@Column(value="company_id",desc="所属公司")
	private String companyId;
    
	
	@Column(value="company_name",desc="所属公司")
	private String companyName;
	
  		
	@Column(value="province_id",desc="省份")
	private String provinceId;
    
  		
	@Column(value="city_id",desc="城市")
	private String cityId;
    
  		
	@Column(value="representative",desc="法人代表")
	private String representative;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
	@Column(value="remark",desc="备注")
	private String remark;
    
	@Column(value="disabled_id",desc="禁用人")
	private String disabledId;
  		
	@Column(value="disabled_date",desc="禁用日期")
	private Timestamp disabledDate;		
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
    
  		
	public String getRepresentative() {
		return representative;
	}
	public void setRepresentative(String representative) {
		this.representative = representative;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDisabledId() {
		return disabledId;
	}
	public void setDisabledId(String disabledId) {
		this.disabledId = disabledId;
	}
	public Timestamp getDisabledDate() {
		return disabledDate;
	}
	public void setDisabledDate(Timestamp disabledDate) {
		this.disabledDate = disabledDate;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
    
  		
	
}
