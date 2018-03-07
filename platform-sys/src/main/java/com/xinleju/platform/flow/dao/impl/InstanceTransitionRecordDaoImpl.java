package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.InstanceTransitionRecordDao;
import com.xinleju.platform.flow.dto.InstanceTransitionRecordDto;
import com.xinleju.platform.flow.entity.InstanceTransitionRecord;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class InstanceTransitionRecordDaoImpl extends BaseDaoImpl<String,InstanceTransitionRecord> implements InstanceTransitionRecordDao{

	public InstanceTransitionRecordDaoImpl() {
		super();
	}

	@Override
	public List<InstanceTransitionRecordDto> queryTransferList(String instanceId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("instanceId", instanceId);
		return getSqlSession().selectList(InstanceTransitionRecord.class.getName() + ".queryTransferList", params);
	}

	
	
}
