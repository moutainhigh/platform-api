package com.xinleju.platform.portal.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.portal.entity.ComponentCategory;

/**
 * @author admin
 * 
 * 
 */

public interface ComponentCategoryService extends  BaseService <String,ComponentCategory>{
  
	public ComponentCategory getComponentCategoryBySerialNo(
			ComponentCategory componentCategory);

	
}
