package com.xinleju.platform.finance.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.entity.AccountSet;

/**
 * @author admin
 * 
 * 
 */

public interface AccountSetService extends  BaseService <String,AccountSet>{

	/**
	 * @param map
	 * @return
	 */
	public Page getSystemRegisterpage(Map map)throws Exception;

	/**
	 * @param map
	 * @return
	 */
	public List queryAccounSetList(Map map)throws Exception;
}
