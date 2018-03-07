package com.xinleju.platform.flow.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.entity.InstanceAc;

/**
 * @author admin
 * 
 * 
 */

public interface InstanceAcService extends  BaseService <String,InstanceAc>{

	boolean setStatus(String currentAcId, String status);

	int update(String acId, Map<String, Object> params);

	
}
