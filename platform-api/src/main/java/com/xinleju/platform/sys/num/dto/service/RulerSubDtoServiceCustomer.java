package com.xinleju.platform.sys.num.dto.service;

import java.util.Map;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface RulerSubDtoServiceCustomer extends BaseDtoServiceCustomer{

	public String getBeanIdByBillId(String userinfo, Map<String, Object> map);

	/**
	 * @param userinfo
	 * @param map
	 * @return
	 */

	//public String saveAndRulerData(String userinfo, String updateJson);

	public String getBillNumber(String userinfo, Map<String, Object> map);
}
