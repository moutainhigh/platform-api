package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.MobileFormDao;
import com.xinleju.platform.flow.dto.MobileFormDto;
import com.xinleju.platform.flow.entity.MobileForm;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class MobileFormDaoImpl extends BaseDaoImpl<String,MobileForm> implements MobileFormDao{

	public MobileFormDaoImpl() {
		super();
	}

	@Override
	public List<MobileFormDto> queryMobileFormBy(String flCode, String businessId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("flCode", flCode);
		params.put("businessId", businessId);
		return getSqlSession().selectList(MobileForm.class.getName() + ".queryMobileFormBy", params);
	}

	@Override
	public String getBusObjName(Map<String, Object> params) {
		return getSqlSession().selectOne(MobileForm.class.getName() + ".getBusObjName", params);
	}

	@Override
	public void delMobileFormByBusinessId(String businessId) {
		getSqlSession().update(MobileForm.class.getName() + ".delMobileFormByBusinessId", businessId);
	}
}
