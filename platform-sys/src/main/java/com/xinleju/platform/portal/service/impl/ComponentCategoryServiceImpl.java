package com.xinleju.platform.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.portal.dao.ComponentCategoryDao;
import com.xinleju.platform.portal.entity.ComponentCategory;
import com.xinleju.platform.portal.service.ComponentCategoryService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class ComponentCategoryServiceImpl extends  BaseServiceImpl<String,ComponentCategory> implements ComponentCategoryService{
	

	@Autowired
	private ComponentCategoryDao componentCategoryDao;

	@Override
	public ComponentCategory getComponentCategoryBySerialNo(
			ComponentCategory componentCategory) {
		
		return componentCategoryDao.getComponentCategoryBySerialNo(componentCategory);
	}

}
