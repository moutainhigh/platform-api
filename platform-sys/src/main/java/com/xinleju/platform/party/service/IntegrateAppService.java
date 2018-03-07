package com.xinleju.platform.party.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.party.entity.IntegrateApp;

/**
 * @author admin
 * 
 * 
 */

public interface IntegrateAppService extends  BaseService <String,IntegrateApp>{

	/**
	 * @param map
	 * @return
	 */
	public Page getIntegratePage(Map map)throws Exception;

	/**
	 * @param integrateApp
	 */
	public int saveIntegrateApp(IntegrateApp integrateApp);

	/**
	 * @param integrateApp
	 * @return
	 */
	public int updateIntegrateApp(IntegrateApp integrateApp);

	
}
