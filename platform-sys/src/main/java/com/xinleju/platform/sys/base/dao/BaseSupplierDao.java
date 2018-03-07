package com.xinleju.platform.sys.base.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.base.dto.BaseSupplierDto;
import com.xinleju.platform.sys.base.entity.BaseSupplier;

/**
 * @author admin
 *
 */

public interface BaseSupplierDao extends BaseDao<String, BaseSupplier> {

	public List<Map<String, Object>> getSupplierData(Map map);
	
	public Integer getSupplierDataCount(Map map);

	/**
	 * @param id
	 * @return
	 */
	public List<BaseSupplierDto> getSingleObject(String id);
	
	/**
	 * 根据公司Id获取供方档案
	 * @param companyId
	 * @return
	 */
	public List<Map<String,Object>> getSupplierByCompanyId(Map<String, Object> param)throws Exception;

    List<BaseSupplier> selectBeanByOption(Map<String, Object> paramMap);
}
