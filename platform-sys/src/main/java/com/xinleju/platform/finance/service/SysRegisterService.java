package com.xinleju.platform.finance.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.entity.SysRegister;

/**
 * @author admin
 * 
 * 
 */

public interface SysRegisterService extends  BaseService <String,SysRegister>{

	/**
	 * @param map
	 * @return
	 */
	public Page getSystemRegisterpage(Map map)throws Exception;

	/**
	 * @param sysRegisterDtoBean
	 * @return
	 */
	public int updateStatus(SysRegister sysRegisterDtoBean);

	
}
