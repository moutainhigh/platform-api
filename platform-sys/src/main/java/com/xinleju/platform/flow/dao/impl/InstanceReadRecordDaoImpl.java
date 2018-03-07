package com.xinleju.platform.flow.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.InstanceReadRecordDao;
import com.xinleju.platform.flow.entity.InstanceReadRecord;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class InstanceReadRecordDaoImpl extends BaseDaoImpl<String,InstanceReadRecord> implements InstanceReadRecordDao{

	public InstanceReadRecordDaoImpl() {
		super();
	}

	
	
}
