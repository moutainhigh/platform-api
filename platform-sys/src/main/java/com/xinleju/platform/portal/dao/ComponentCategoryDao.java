package com.xinleju.platform.portal.dao;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.portal.entity.ComponentCategory;

/**
 * @author admin
 *
 */

public interface ComponentCategoryDao extends BaseDao<String, ComponentCategory> {

	public ComponentCategory getComponentCategoryBySerialNo(
			ComponentCategory componentCategory);
	
	

}
