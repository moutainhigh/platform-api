package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface AccountSetDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * @param object
	 * @param paramaterJson
	 * @return
	 */
	String getSystemRegisterpage(String userInfo, String paramaterJson);

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	String queryAccounSetList(String userJson, String paramaterJson);
}
