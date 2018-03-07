package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface AccountCaptionDtoServiceCustomer extends BaseDtoServiceCustomer{


	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	String getAccountCaptionTree(String userJson, String paramaterJson);

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	String queryCaptionList(String userJson, String paramaterJson);

}
