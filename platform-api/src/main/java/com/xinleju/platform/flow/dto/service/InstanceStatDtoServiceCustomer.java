package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface InstanceStatDtoServiceCustomer extends BaseDtoServiceCustomer{

	String statUseTimes(String userJson, String paramaterJson);

	String statInstanceEffiency(String userJson, String paramaterJson);

	String statOperateTimes(String userJson, String paramaterJson);

	String statTaskLength(String userJson, String paramaterJson);

	String detailInstanceEfficiencyList(String userJson, String paramaterJson);

	String detailOperateTimesList(String userJson, String paramaterJson);

	String detailTaskLengthList(String userJson, String paramaterJson);

	String statInstanceEfficiencyMxPage(String userJson, String paramaterJson);

}
