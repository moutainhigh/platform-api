package com.xinleju.platform.sys.base.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.base.entity.BaseCorporation;

/**
 * @author admin
 *
 */

public interface BaseCorporationDao extends BaseDao<String, BaseCorporation> {

	public  List<Map<String,Object>> getBaseCorporationList(Map map);

	/**
	 * @param map
	 * @return
	 */
	public Integer getBaseCorporationCount(Map map);
	
	

}
