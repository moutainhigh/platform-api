package com.xinleju.platform.univ.search.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.univ.search.dao.SearchPropertyDao;
import com.xinleju.platform.univ.search.entity.SearchProperty;
import com.xinleju.platform.univ.search.service.SearchPropertyService;

/**
 * @author haoqp
 * 
 * 
 */
@Transactional
@Service
public class SearchPropertyServiceImpl extends  BaseServiceImpl<String,SearchProperty> implements SearchPropertyService{
	

	@Autowired
	private SearchPropertyDao searchPropertyDao;

	@Override
	public int getCountForUpdate(Map<String, Object> paramMap) {
		int count = searchPropertyDao.getCount(SearchProperty.class.getName() + ".getCountForUpdate", paramMap);
		return count;
	}
	

}
