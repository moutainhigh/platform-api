package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface BusinessFieldDtoServiceCustomer extends BaseDtoServiceCustomer{

	String getListByObjId(String userJson, String paramaterJson);

	String queryTreeList(String userJson, String paramaterJson);

}
