package com.xinleju.platform.sys.res.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface ResourceDtoServiceCustomer extends BaseDtoServiceCustomer{
	
	public String getResourceTree(String userInfo, String paramater);
	public String getResourceTreeAll(String userInfo, String paramater);
	public String getOperationTreeByAppId(String userInfo, String paramater);
	public String getfuncPermissionButtonJqgridTreeByAppid(String userInfo, String paramater);
	/**
	 * 上移下移
	 * @param userinfo(用户信息) param 
	 * @return
	 */
	public String upOrDown(String userinfo, String paramaterJson);
}
