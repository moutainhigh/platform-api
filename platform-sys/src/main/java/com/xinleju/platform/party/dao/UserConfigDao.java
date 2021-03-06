package com.xinleju.platform.party.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.party.entity.IntegrateApp;
import com.xinleju.platform.party.entity.UserConfig;
import com.xinleju.platform.sys.base.entity.SettlementTrades;

/**
 * @author admin
 *
 */

public interface UserConfigDao extends BaseDao<String, UserConfig> {

	/**
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getUserConfigPage(Map map);

	/**
	 * @param map
	 * @return
	 */
	public Integer getUserConfigCount(Map map);

	/**
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getUserConfig(Map map);

	/**
	 * @param map
	 * @return
	 */
	public String getUserConfigByAppId(Map<String, Object> map);

	/**
	 * @param map
	 * @return
	 */
	public List<UserConfig> getUserConfigListByAppId(Map<String, Object> map);
	
	

}
