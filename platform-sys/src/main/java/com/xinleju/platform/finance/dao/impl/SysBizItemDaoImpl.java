package com.xinleju.platform.finance.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.finance.dao.SysBizItemDao;
import com.xinleju.platform.finance.entity.SysBizItem;
import com.xinleju.platform.finance.entity.SysRegister;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class SysBizItemDaoImpl extends BaseDaoImpl<String,SysBizItem> implements SysBizItemDao{

	public SysBizItemDaoImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.dao.SysBizItemDao#getSysBizItempageList(java.util.Map)
	 */
	@Override
	public List<SysBizItem> getSysBizItempageList(Map map) {
		  return getSqlSession().selectList("com.xinleju.platform.finance.entity.SysBizItem.getSysBizItempageList", map);

	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.dao.SysBizItemDao#getSysBizItempageListCount(java.util.Map)
	 */
	@Override
	public Integer getSysBizItempageListCount(Map map) {
		  return getSqlSession().selectOne("com.xinleju.platform.finance.entity.SysBizItem.getSysBizItempageListCount", map);

	}

	
	
}
