package com.xinleju.platform.sys.res.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_RES_DATA_POINT_PERMISSION_VAL",desc="数据点角色授权值")
public class DataPointPermissionVal extends BaseEntity{
	
		
	@Column(value="data_permission_id",desc="数据授权id")
	private String dataPermissionId;
    
  		
	@Column(value="val",desc="值")
	private String val;
    
  		
		
	public String getDataPermissionId() {
		return dataPermissionId;
	}
	public void setDataPermissionId(String dataPermissionId) {
		this.dataPermissionId = dataPermissionId;
	}
    
  		
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
    
  		
	
}
