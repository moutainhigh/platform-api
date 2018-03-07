package com.xinleju.platform.flow.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.OperationTypeDao;
import com.xinleju.platform.flow.entity.OperationType;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class OperationTypeDaoImpl extends BaseDaoImpl<String,OperationType> implements OperationTypeDao{

	public OperationTypeDaoImpl() {
		super();
	}

	
	
}
