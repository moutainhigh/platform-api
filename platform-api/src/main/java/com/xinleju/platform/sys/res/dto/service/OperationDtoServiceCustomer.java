package com.xinleju.platform.sys.res.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface OperationDtoServiceCustomer extends BaseDtoServiceCustomer{

	public String getOperationTreeByResourceId(String userInfo, String paramater);
}
