package com.xinleju.platform.sys.log.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface LogDubboDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 模糊查询-分页
	 * @return
	 */
	public String vaguePage(String userInfo, String paramater);
}
