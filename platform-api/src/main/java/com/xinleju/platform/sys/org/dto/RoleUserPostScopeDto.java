package com.xinleju.platform.sys.org.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class RoleUserPostScopeDto extends BaseDto{

		
	//管辖类型
	private String type;
    
  		
	//管辖id
	private String refId;
    
  		
	//引用对象关系id
	private String roleUserId;
    
  		
		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
    
  		
	public String getRoleUserId() {
		return roleUserId;
	}
	public void setRoleUserId(String roleUserId) {
		this.roleUserId = roleUserId;
	}
    
  		
}
