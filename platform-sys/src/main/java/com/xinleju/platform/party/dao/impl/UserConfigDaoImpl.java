package com.xinleju.platform.party.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.party.dao.UserConfigDao;
import com.xinleju.platform.party.entity.UserConfig;
import com.xinleju.platform.sys.base.entity.SettlementTrades;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class UserConfigDaoImpl extends BaseDaoImpl<String,UserConfig> implements UserConfigDao{

	public UserConfigDaoImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.dao.UserConfigDao#getUserConfigPage(java.util.Map)
	 */
	@Override
	public List<Map<String,Object>> getUserConfigPage(Map map) {
		 return getSqlSession().selectList("com.xinleju.platform.party.entity.UserConfig.getUserConfigPage",map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.dao.UserConfigDao#getUserConfigCount(java.util.Map)
	 */
	@Override
	public Integer getUserConfigCount(Map map) {
		 return getSqlSession().selectOne("com.xinleju.platform.party.entity.UserConfig.getUserConfigCount",map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.dao.UserConfigDao#getUserConfig(java.lang.String)
	 */
	@Override
	public List<Map<String,Object>> getUserConfig(Map map) {
		 return getSqlSession().selectList("com.xinleju.platform.party.entity.UserConfig.getUserConfig",map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.dao.UserConfigDao#getUserConfigByAppId(java.util.Map)
	 */
	@Override
	public String getUserConfigByAppId(Map<String, Object> map) {
		 return getSqlSession().selectOne("com.xinleju.platform.party.entity.UserConfig.getUserConfigByAppId",map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.dao.UserConfigDao#getUserConfigListByAppId(java.util.Map)
	 */
	@Override
	public List<UserConfig> getUserConfigListByAppId(Map<String, Object> map) {
		 return getSqlSession().selectList("com.xinleju.platform.party.entity.UserConfig.getUserConfigListByAppId",map);
	}

	
	
}
