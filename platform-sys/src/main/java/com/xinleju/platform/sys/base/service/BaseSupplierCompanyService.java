package com.xinleju.platform.sys.base.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.base.entity.BaseSupplierCompany;

/**
 * @author admin
 * 
 * 
 */

public interface BaseSupplierCompanyService extends  BaseService <String,BaseSupplierCompany>{

	public List queryCompanyList(Map map);

	/**
	 * @param id
	 * @return
	 */
	public List<String> getObjectBySupplierId(String id)throws Exception;
	
	public List<String> getIdsBySupplierId(String id)throws Exception;

	
}
