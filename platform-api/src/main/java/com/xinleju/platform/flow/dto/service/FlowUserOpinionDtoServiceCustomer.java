package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface FlowUserOpinionDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * 用户操作常用意见后，批量保存（增删改）
	 * @param userInfo
	 * @param saveJson
	 * @return
	 */
	String saveUserOpinIons(String userInfo,String saveJson) ;
	
	/**
	 * 查询用户自定义意见
	 * @param userInfo
	 * @param saveJson
	 * @return
	 */
	String queryUserOpinion(String userInfo,String param) ;
}
