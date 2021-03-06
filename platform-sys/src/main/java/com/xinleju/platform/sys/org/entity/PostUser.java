package com.xinleju.platform.sys.org.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_ORG_POST_USER",desc="用户岗位关联表")
public class PostUser extends BaseEntity{
	
		
	@Column(value="user_id",desc="用户")
	private String userId;
    
  		
	@Column(value="post_id",desc="岗位")
	private String postId;
    
  		
	@Column(value="sort",desc="排序")
	private Long sort;
    
	@Column(value="is_default",desc="租户")
	private Boolean isDefault;
    	
		
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    
  		
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
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
