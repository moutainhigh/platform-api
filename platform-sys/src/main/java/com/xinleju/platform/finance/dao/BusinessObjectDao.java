package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.BusinessObject;

/**
 * @author admin
 *
 */

public interface BusinessObjectDao extends BaseDao<String, BusinessObject> {

	List<Map<String, Object>> getPageData(Map<String, Object> map);

	Integer getPageDataCount(Map<String, Object> map);
	
	Integer getCountByCode(Map<String, Object> map);
	

}
