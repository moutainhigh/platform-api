package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.StepDao;
import com.xinleju.platform.flow.entity.Step;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class StepDaoImpl extends BaseDaoImpl<String,Step> implements StepDao{

	public StepDaoImpl() {
		super();
	}

	@Override
	public List<String> bizVarBeUsedInFlow(String businessObjectId, String varCode) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessObjectId", businessObjectId);
		params.put("varCode", varCode);
		return getSqlSession().selectList(Step.class.getName() + ".bizVarBeUsedInFlow", params);
	}

	@Override
	public List<Step> queryStepsBy(String flId) {
		return getSqlSession().selectList(Step.class.getName() + ".queryStepsByFlId", flId);
	}

	
	
}
