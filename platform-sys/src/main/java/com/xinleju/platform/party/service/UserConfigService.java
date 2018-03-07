package com.xinleju.platform.party.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.party.entity.UserConfig;

/**
 * @author admin
 * 
 * 
 */

public interface UserConfigService extends  BaseService <String,UserConfig>{

	/**
	 * @param map
	 * @return
	 */
	public Page getUserConfigPage(Map map)throws Exception;

	/**
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getUserConfig(String id)throws Exception;

	/**
	 * @param userConfig
	 * @return
	 */
	public int updateStatus(UserConfig userConfig)throws Exception;

	/**
	 * @param userConfig
	 * @return 
	 */
	public int saveUserConfig(UserConfig userConfig)throws Exception;

	/**
	 * @param userConfig
	 * @return
	 */
	public int updateUserConfig(UserConfig userConfig)throws Exception;

	/**
	 * @param userConfigList
	 */
	public void saveBatchList(List<UserConfig> userConfigList)throws Exception;

	/**
	 * @param userConfig
	 * @return
	 */
	public void updateUserConfigBatch(UserConfig userConfig)throws Exception;

	
}
