package com.xinleju.platform.sys.res.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class FuncPermissionDto extends BaseDto{

		
	//角色id
	private String roleId;
    
  	//操作点id
	private String operationId;
		
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
    
  		
}
