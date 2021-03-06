package com.xinleju.platform.sys.num.dto.service;

import java.util.Map;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface RulerDtoServiceCustomer extends BaseDtoServiceCustomer{

	public String getRuleListByBillId(String userinfo, String paramaterJson);

	public int getRulerSortNum(String userinfo, Map<String,Object> paramaterJson);

	public String updateSort(String userinfo, String string, Map<String, Object> map);


}
