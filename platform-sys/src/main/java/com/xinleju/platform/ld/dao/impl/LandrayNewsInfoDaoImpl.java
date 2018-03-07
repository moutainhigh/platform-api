package com.xinleju.platform.ld.dao.impl;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.ld.dao.LandrayNewsInfoDao;
import com.xinleju.platform.ld.entity.LandrayNewsInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class LandrayNewsInfoDaoImpl extends BaseDaoImpl<String,LandrayNewsInfo> implements LandrayNewsInfoDao {

	public LandrayNewsInfoDaoImpl() {
		super();
	}


	@Override
	public List<Map<String, Object>> getBillData(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.ld.entity.LandrayNewsInfo.getNewsData", map);
	}

	@Override
	public Integer getBillDataCount(Map map) {
		return  getSqlSession().selectOne("com.xinleju.platform.ld.entity.LandrayNewsInfo.getNewsDataCount", map);
	}

	@Override
	public List<LandrayNewsInfo> portalList(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.ld.entity.LandrayNewsInfo.getPortalNewsData", map);
	}
}
