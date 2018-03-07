package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_OVERDUE_FL",desc="流程提醒例外模板")
public class OverdueFl extends BaseEntity{
	
		
	@Column(value="fl_id",desc="流程模板id")
	private String flId;
    
  		
	@Column(value="fl_name",desc="流程模板名称")
	private String flName;
    
  		
	@Column(value="overdue_id",desc="流程逾期id")
	private String overdueId;
    
  		
		
	public String getFlId() {
		return flId;
	}
	public void setFlId(String flId) {
		this.flId = flId;
	}
    
  		
	public String getFlName() {
		return flName;
	}
	public void setFlName(String flName) {
		this.flName = flName;
	}
    
  		
	public String getOverdueId() {
		return overdueId;
	}
	public void setOverdueId(String overdueId) {
		this.overdueId = overdueId;
	}
    
  		
	
}
