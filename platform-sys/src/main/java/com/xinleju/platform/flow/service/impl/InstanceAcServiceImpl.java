package com.xinleju.platform.flow.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.InstanceAcDao;
import com.xinleju.platform.flow.entity.InstanceAc;
import com.xinleju.platform.flow.service.InstanceAcService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class InstanceAcServiceImpl extends  BaseServiceImpl<String,InstanceAc> implements InstanceAcService{
	

	@Autowired
	private InstanceAcDao instanceAcDao;

	@Override
	public boolean setStatus(String currentAcId, String status) {
		return instanceAcDao.setStatus(currentAcId, status);
	}

	@Override
	public int update(String acId, Map<String, Object> params) {
		return instanceAcDao.update(acId, params);
	}
	

}
