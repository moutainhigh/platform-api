package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dao.BaseCorporationDao;
import com.xinleju.platform.sys.base.entity.BaseCorporation;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BaseCorporationDaoImpl extends BaseDaoImpl<String,BaseCorporation> implements BaseCorporationDao{

	public BaseCorporationDaoImpl() {
		super();
	}

	@Override
	public List<Map<String,Object>> getBaseCorporationList(Map map) {
	  return   getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseCorporation.getBaseCorporationList", map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseCorporationDao#getAllList(java.util.Map)
	 */
	@Override
	public Integer getBaseCorporationCount(Map map) {
		  return   getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.BaseCorporation.getBaseCorporationCount", map);
	}

	
	
}
