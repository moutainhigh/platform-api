package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface InstanceAccessibleDtoServiceCustomer extends BaseDtoServiceCustomer{

	String addResetReaderFormData(String userInfo, String saveJson);

	String deleteReaderByFormData(String userInfo, String saveJson);

	String queryInstanceReaderList(String userInfo, String paramaterJson);

}
