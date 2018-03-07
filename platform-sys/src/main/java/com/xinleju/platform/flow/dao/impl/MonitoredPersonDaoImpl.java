package com.xinleju.platform.flow.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.MonitoredPersonDao;
import com.xinleju.platform.flow.entity.MonitoredPerson;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class MonitoredPersonDaoImpl extends BaseDaoImpl<String,MonitoredPerson> implements MonitoredPersonDao{

	public MonitoredPersonDaoImpl() {
		super();
	}

	
	
}