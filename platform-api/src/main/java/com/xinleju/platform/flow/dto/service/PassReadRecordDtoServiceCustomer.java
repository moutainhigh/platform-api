package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface PassReadRecordDtoServiceCustomer extends BaseDtoServiceCustomer{

	String queryPassReadList(String userInfo, String instanceId, String requestSource);

}
