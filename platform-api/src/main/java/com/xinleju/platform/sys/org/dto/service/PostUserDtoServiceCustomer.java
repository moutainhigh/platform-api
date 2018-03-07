package com.xinleju.platform.sys.org.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface PostUserDtoServiceCustomer extends BaseDtoServiceCustomer{

	
	/**
	 * 批量保存对象
	 * @param objectList
	 */
	public String deleteAllObjectByUserOrPostIds(String userInfo,String  deleteJsonList) ;
	/**
	 * 批量保存post_user和role_user
	 * @param objectList
	 */
	public String savePostUserAndRoleUser(String userInfo,String jsonList) ;
	
	/**
	 * 设置主岗
	 * @param objectList
	 */
	public String setDefaultPost(String userInfo,String dataJson) ;
}
