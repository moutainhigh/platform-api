package com.xinleju.platform.sys.notice.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface SysNoticePhoneServerDtoServiceCustomer extends BaseDtoServiceCustomer{

	String vaguePage(String userJson, String paramaterJson);

	String setDefault(String userJson, String string);

}