package com.xinleju.platform.sys.res.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class DataPermissionDto extends BaseDto{

		
	//角色id
	private String roleId;
    
  		
	//控制点id
	private String dataPointId;
    
  		
		
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
    
  		
	public String getDataPointId() {
		return dataPointId;
	}
	public void setDataPointId(String dataPointId) {
		this.dataPointId = dataPointId;
	}
    
  		
}
