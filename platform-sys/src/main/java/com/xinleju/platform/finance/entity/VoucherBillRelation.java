package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_VOUCHER_BILL_RELATION",desc="凭证业务关联表")
public class VoucherBillRelation extends BaseEntity{
	
		
	@Column(value="code",desc="业务数据编码")
	private String code;
    
  		
	@Column(value="voucher_bill_id",desc="生成凭证id")
	private String voucherBillId;
    
  		
	@Column(value="biz_id",desc="业务数据id")
	private String bizId;
    
	@Column(value="bill_name",desc="业务数据名称")
	private String billName;
	
	@Column(value="url",desc="业务数据url地址")
	private String url;
		
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getVoucherBillId() {
		return voucherBillId;
	}
	public void setVoucherBillId(String voucherBillId) {
		this.voucherBillId = voucherBillId;
	}
    
  		
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
    
  		
	
}
