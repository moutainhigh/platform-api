package com.xinleju.platform.sys.org.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class RoleUserDto extends BaseDto{

		
	// 用户id 
	private String userId;
	//目标全路径:引用用户或岗位全路径
	private String targetName;
    
	//目标类型 
	private String targetType;
	//岗位id
	private String postId;
	
  		
	//角色
	private String roleId;
    
  		
	//排序
	private Long sort;
    
    
  		
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
    
  		
  		
}
