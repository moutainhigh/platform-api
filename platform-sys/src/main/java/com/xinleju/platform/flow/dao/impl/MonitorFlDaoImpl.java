package com.xinleju.platform.flow.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.MonitorFlDao;
import com.xinleju.platform.flow.dto.MonitorFlDto;
import com.xinleju.platform.flow.entity.MonitorFl;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class MonitorFlDaoImpl extends BaseDaoImpl<String,MonitorFl> implements MonitorFlDao{

	public MonitorFlDaoImpl() {
		super();
	}

	@Override
	public List<MonitorFlDto> queryMonitorFlowList(Map<String,String> paramMap) {
		String method = MonitorFl.class.getName()+".queryMonitorFlowList";
		return this.getSqlSession().selectList(method, paramMap);
	}

	
	
}
