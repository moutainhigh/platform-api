package com.xinleju.platform.sys.log.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.log.dao.LogOperationTypeDao;
import com.xinleju.platform.sys.log.entity.LogOperationType;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class LogOperationTypeDaoImpl extends BaseDaoImpl<String,LogOperationType> implements LogOperationTypeDao{

	public LogOperationTypeDaoImpl() {
		super();
	}

	
	
}
