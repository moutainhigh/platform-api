package com.xinleju.platform.univ.search.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.univ.search.dao.SearchCategoryPropertyDao;
import com.xinleju.platform.univ.search.entity.SearchCategoryProperty;
import com.xinleju.platform.univ.search.service.SearchCategoryPropertyService;

/**
 * @author haoqp
 * 
 * 
 */

@Transactional
@Service
public class SearchCategoryPropertyServiceImpl extends  BaseServiceImpl<String,SearchCategoryProperty> implements SearchCategoryPropertyService{
	

	@Autowired
	private SearchCategoryPropertyDao searchCategoryPropertyDao;
	

}
