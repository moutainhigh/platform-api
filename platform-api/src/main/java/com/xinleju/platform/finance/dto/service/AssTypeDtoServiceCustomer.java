package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface AssTypeDtoServiceCustomer extends BaseDtoServiceCustomer{


	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String queryAssType(String userJson, String paramaterJson);


	public String getMapListByCompanyId(String userinfo, String paramaterJson);
	
	public String queryTree(String userJson, String paramaterJson);

}
