package com.xinleju.platform.sys.base.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface BaseRegionDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 三级联动查询省 市 县数据
	 * @param t
	 * @return
	 */
	public String getBaseRegionData(String userinfo, String paramaterJson);

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String getTypetree(String userJson, String paramaterJson);

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String getSelectTree(String userJson, String paramaterJson);

	public String updateSort(String userJson, String paramaterJson);

}
