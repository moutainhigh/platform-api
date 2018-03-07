package com.xinleju.platform.sys.res.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface AppSystemDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 根据系统id获取数据对象控制点
	 * @param userinfo(用户信息) param(系统id)
	 */
	public String getDataTree(String userinfo,String param);
	/**
	 * 根据系统id获取功能控制点
	 * @param userinfo(用户信息) param(系统id)
	 */
	public String getOperationTree(String userinfo,String param);
	/**
	 * @param object
	 * @param paramaterJson
	 * @return
	 */
	public String queryListData(String userinfo, String paramaterJson);
	/**
	 * 上移下移
	 * @param userinfo(用户信息) param 
	 * @return
	 */
	public String upOrDown(String userinfo, String paramaterJson);
	/**
	 * 查询系统下拉
	 * @param userinfo(用户信息) param 
	 * @return
	 */
	public String queryListForSelect(String userinfo, String paramaterJson);
}
