package com.xinleju.platform.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.InstanceVariableDao;
import com.xinleju.platform.flow.entity.InstanceVariable;
import com.xinleju.platform.flow.service.InstanceVariableService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class InstanceVariableServiceImpl extends  BaseServiceImpl<String,InstanceVariable> implements InstanceVariableService{
	

	@Autowired
	private InstanceVariableDao instanceVariableDao;
	

}
