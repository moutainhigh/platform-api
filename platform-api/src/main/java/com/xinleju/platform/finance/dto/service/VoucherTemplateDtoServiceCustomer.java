package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface VoucherTemplateDtoServiceCustomer extends BaseDtoServiceCustomer{
	public String saveVoucher(String userJson, String paramaterJson,String excelCompanyId,String companyId);
	public String queryListByTypeIds(String userJson, String paramater);
}
