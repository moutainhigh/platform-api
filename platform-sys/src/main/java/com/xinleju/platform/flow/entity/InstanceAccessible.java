package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_INSTANCE_ACCESSIBLE",desc="流程可阅者")
public class InstanceAccessible extends BaseEntity{
	
		
	@Column(value="fi_id",desc="流程实例id")
	private String fiId;
    
  		
	@Column(value="type",desc="可阅者类型: 1:角色,2:岗位,3:人员")
	private String type;
    
  		
	@Column(value="accessible_id",desc="可阅者ID")
	private String accessibleId;
	
	@Column(value="accessible_name",desc="可阅者名称")
	private String accessibleName;
    
  		
		
	public String getFiId() {
		return fiId;
	}
	public void setFiId(String fiId) {
		this.fiId = fiId;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccessibleId() {
		return accessibleId;
	}
	public void setAccessibleId(String accessibleId) {
		this.accessibleId = accessibleId;
	}
	public String getAccessibleName() {
		return accessibleName;
	}
	public void setAccessibleName(String accessibleName) {
		this.accessibleName = accessibleName;
	}
	
}
