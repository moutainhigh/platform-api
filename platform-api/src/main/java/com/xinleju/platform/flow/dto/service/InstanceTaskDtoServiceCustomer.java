package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;

public interface InstanceTaskDtoServiceCustomer extends BaseDtoServiceCustomer{

	String queryCurrentPersonList(String userInfo, String paramaterJson);

	String queryTaskIdByInstanceId(String userInfo, String paramaterJson);

}
