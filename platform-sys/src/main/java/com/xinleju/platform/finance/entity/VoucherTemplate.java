package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_VOUCHER_TEMPLATE",desc="凭证模板")
public class VoucherTemplate extends BaseEntity{
	
		
	@Column(value="type_id",desc="凭证模板业务类型id")
	private String typeId;
    
  		
	@Column(value="biz_object_id",desc="业务对象id")
	private String bizObjectId;
    
  		
	@Column(value="biz_object_name",desc="业务对象名称")
	private String bizObjectName;
    
  		
	@Column(value="filter",desc="筛选条件")
	private String filter;
    
  		
	@Column(value="word",desc="凭证字")
	private String word;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
	@Column(value="remark",desc="业务类型说明")
	private String remark;
    
	@Column(value="name",desc="模板名（同模板类型名）")
	private String name;

	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
    
  		
	public String getBizObjectId() {
		return bizObjectId;
	}
	public void setBizObjectId(String bizObjectId) {
		this.bizObjectId = bizObjectId;
	}
    
  		
	public String getBizObjectName() {
		return bizObjectName;
	}
	public void setBizObjectName(String bizObjectName) {
		this.bizObjectName = bizObjectName;
	}
    
  		
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
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
  		
	
}
