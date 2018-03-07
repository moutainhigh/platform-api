package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.InstanceAcDao;
import com.xinleju.platform.flow.entity.Instance;
import com.xinleju.platform.flow.entity.InstanceAc;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class InstanceAcDaoImpl extends BaseDaoImpl<String,InstanceAc> implements InstanceAcDao{

	public InstanceAcDaoImpl() {
		super();
	}

	@Override
	public boolean setStatus(String currentAcId, String status) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("currentAcId", currentAcId);
		params.put("status", status);
		getSqlSession().update(InstanceAc.class.getName() + ".setStatus", params);
		return true;
	}

	//校验code重复
	@Override
	public Integer checkCode(Map<String, String> paramMap) throws Exception{
		return getSqlSession().selectOne(InstanceAc.class.getName() + ".checkCode",paramMap);
	}
	
}
