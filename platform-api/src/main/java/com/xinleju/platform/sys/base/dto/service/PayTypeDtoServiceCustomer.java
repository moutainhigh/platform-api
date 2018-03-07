package com.xinleju.platform.sys.base.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface PayTypeDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * 查询符合条件的对象List列表 
	 * @param map
	 * @return
	 */
	public String queryAllPayType(String userInfo,String paramater)  ;
	
	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String payTypeParanetList(String userJson, String paramaterJson);

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String getTypetree(String userJson, String paramaterJson);

	/**
	 * @param userJson
	 * @param string
	 * @return
	 */
	public String deletePayType(String userJson, String id);

	/**
	 * @param userJson
	 * @param string
	 * @return
	 */
	public String updateStatus(String userJson, String string);

	/**
	 * @param userJson
	 * @param updateJson
	 * @return
	 */
	public String updatePayType(String userJson, String updateJson);

}
