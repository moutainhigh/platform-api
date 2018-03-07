package com.xinleju.platform.flow.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.InstanceCollectionDao;
import com.xinleju.platform.flow.entity.InstanceCollection;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class InstanceCollectionDaoImpl extends BaseDaoImpl<String, InstanceCollection> implements InstanceCollectionDao{

	public InstanceCollectionDaoImpl() {
		super();
	}

	
	
}
