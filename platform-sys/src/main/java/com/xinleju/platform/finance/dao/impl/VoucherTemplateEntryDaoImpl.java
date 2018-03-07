package com.xinleju.platform.finance.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.finance.dao.VoucherTemplateEntryDao;
import com.xinleju.platform.finance.entity.VoucherTemplateEntry;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class VoucherTemplateEntryDaoImpl extends BaseDaoImpl<String,VoucherTemplateEntry> implements VoucherTemplateEntryDao{

	public VoucherTemplateEntryDaoImpl() {
		super();
	}

	@Override
	public List<Map<String, Object>> getMapListByTypeId(String paramater) {
		return  getSqlSession().selectList("com.xinleju.platform.finance.entity.VoucherTemplateEntry.queryEntryListByTypeId", paramater);
	}

	@Override
	public void deleteObjectByTempId(String id) {
		getSqlSession().selectList("com.xinleju.platform.finance.entity.VoucherTemplateEntry.deleteObjectByTempId", id);
	}
	
	@Override
	public List queryListByTemplateIds(List<String> paramList){
		return getSqlSession().selectList("com.xinleju.platform.finance.entity.VoucherTemplateEntry.queryListByTemplateIds", paramList);
	}
	
}
