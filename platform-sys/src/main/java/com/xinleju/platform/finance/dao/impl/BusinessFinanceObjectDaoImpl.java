package com.xinleju.platform.finance.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.finance.dao.BusinessObjectDao;
import com.xinleju.platform.finance.entity.BusinessObject;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BusinessFinanceObjectDaoImpl extends BaseDaoImpl<String,BusinessObject> implements BusinessObjectDao{

	public BusinessFinanceObjectDaoImpl() {
		super();
	}

	@Override
	public List<Map<String, Object>> getPageData(Map<String, Object> map) {
		return  getSqlSession().selectList("com.xinleju.platform.finance.entity.BusinessObject.getPageData", map);
	}

	@Override
	public Integer getPageDataCount(Map<String, Object> map) {
		return  getSqlSession().selectOne("com.xinleju.platform.finance.entity.BusinessObject.getPageDataCount", map);
	}

	@Override
	public Integer getCountByCode(Map<String, Object> map) {
		return  getSqlSession().selectOne("com.xinleju.platform.finance.entity.BusinessObject.getCountByCode", map);
	}

	
	
}
