package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface VoucherTemplateTypeDtoServiceCustomer extends BaseDtoServiceCustomer{

	String saveTempAll(String userJson, String saveJson);

	String saveAllCopyTemp(String userJson, String saveJson);

}
