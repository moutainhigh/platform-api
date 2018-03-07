package com.xinleju.platform.univ.search.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.univ.search.dao.SearchCategoryPropertyDao;
import com.xinleju.platform.univ.search.entity.SearchCategoryProperty;

/**
 * @author haoqp
 * 
 * 
 */

@Repository
public class SearchCategoryPropertyDaoImpl extends BaseDaoImpl<String,SearchCategoryProperty> implements SearchCategoryPropertyDao{

	public SearchCategoryPropertyDaoImpl() {
		super();
	}

	@Override
	public int deleteAllObjectByCategoryIds(List<String> ids) throws DataAccessException {
		Map<String, List<String>> param = new HashMap<>();
		param.put("ids", ids);
		return getSqlSession().delete(SearchCategoryProperty.class.getName() + ".deleteByCategoryIds", param);
	}

	@Override
	public List<Map<String, Object>> queryMapList(String statementName, Map<String, Object> parameters)
			throws DataAccessException {
		return getSqlSession().selectList(statementName, parameters);
	}

	
	
}
