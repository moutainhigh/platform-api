package com.xinleju.platform.party.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface UserConfigDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String getUserConfigPage(String userJson, String paramaterJson);

	/**
	 * @param userJson
	 * @param string
	 * @return
	 */
	public String getUserConfig(String userJson, String string);

	/**
	 * @param userJson
	 * @param id
	 * @return
	 */
	public String updateStatus(String userJson, String id);

	/**
	 * @param userJson
	 * @param updateJson
	 * @return
	 */
	public String updateUserConfigBatch(String userJson, String updateJson);

}
