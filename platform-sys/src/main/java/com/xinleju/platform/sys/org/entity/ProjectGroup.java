package com.xinleju.platform.sys.org.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_ORG_PROJECT_GROUP",desc="项目团队扩展")
public class ProjectGroup extends BaseEntity{
	
		
	@Column(value="ref_id",desc="项目id")
	private String refId;
    
  		
		
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
    
  		
	
}
