package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface VoucherFormSettingDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * @param object
	 * @param paramaterJson
	 * @return
	 */
	String getFormSettingPage(String userInfo, String paramaterJson);

}
