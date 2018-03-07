package com.xinleju.platform.flow.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.MonitorPointDao;
import com.xinleju.platform.flow.entity.MonitorPoint;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class MonitorPointDaoImpl extends BaseDaoImpl<String, MonitorPoint> implements MonitorPointDao{

	public MonitorPointDaoImpl() {
		super();
	}

	
	
}