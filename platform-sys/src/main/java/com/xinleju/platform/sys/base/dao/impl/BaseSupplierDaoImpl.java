package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.BaseSupplierDao;
import com.xinleju.platform.sys.base.dto.BaseSupplierDto;
import com.xinleju.platform.sys.base.entity.BaseSupplier;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BaseSupplierDaoImpl extends BaseDaoImpl<String,BaseSupplier> implements BaseSupplierDao{

	public BaseSupplierDaoImpl() {
		super();
	}

	@Override
	public List<Map<String, Object>> getSupplierData(Map map) {
		 return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseSupplier.getSupplierData", map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseSupplierDao#getSupplierDataCount(java.util.Map)
	 */
	@Override
	public Integer getSupplierDataCount(Map map) {
		 return  getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.BaseSupplier.getSupplierDataCount", map);
	}
	@Override
	public List<BaseSupplierDto> getSingleObject(String id) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseSupplier.getsingleObject", id);
	}

	@Override
	public List<Map<String, Object>> getSupplierByCompanyId(Map<String, Object> param) throws Exception {
		return getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseSupplier.getSupplierByCompanyId",param);
	}

	@Override
	public List<BaseSupplier> selectBeanByOption(Map<String, Object> paramMap) {
		return getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseSupplier.selectBeanByOption",paramMap);
	}
}
