package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface BusinessObjectDtoServiceCustomer extends BaseDtoServiceCustomer{

	String updateStatus(String userJson, String string);

	String saveMasterTable(String userJson, String saveJson);

}
