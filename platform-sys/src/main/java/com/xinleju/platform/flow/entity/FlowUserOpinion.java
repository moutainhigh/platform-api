package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author 
 * 
 * 
 */

@Table(value="PT_FLOW_USER_OPINION",desc="用户意见")
public class FlowUserOpinion extends BaseEntity{
	
		
	@Column(value="opinion",desc="")
	private String opinion;
    
  		
	@Column(value="sort",desc="")
	private Long sort;
    
  		
	@Column(value="is_default",desc="")
	private Boolean isDefault;
    
  		
		
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
    
	
}
