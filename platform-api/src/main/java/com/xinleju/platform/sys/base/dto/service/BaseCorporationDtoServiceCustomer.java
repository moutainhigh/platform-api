package com.xinleju.platform.sys.base.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface BaseCorporationDtoServiceCustomer extends BaseDtoServiceCustomer{

	public String getCorporationAndAccontById(String userinfo, String id);

	public String saveCorporationAndAccont(String userinfo, String id);

	public String deleteCorporationAndAccontByCorporationId(String userinfo, String id);

	public String updateCorporationAndAccont(String userinfo, String updateJson);

	public String updateStatus(String userinfo, String updateStatus);

	public String deleteAllByIds(String userinfo, String string);

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String getPaymentOrganTree(String userJson, String paramaterJson);

}
