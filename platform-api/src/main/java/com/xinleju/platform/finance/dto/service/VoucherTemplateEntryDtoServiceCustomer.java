package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface VoucherTemplateEntryDtoServiceCustomer extends BaseDtoServiceCustomer{
	public String queryListByTemplateIds(String userJson, String paramater);
}
