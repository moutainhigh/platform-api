package com.xinleju.platform.sys.sync.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.sync.entity.SyncData;

/**
 * @author admin
 * 
 * 
 */

public interface SyncDataService extends  BaseService <String,SyncData>{
	Page getBeanPage(Map<String, Object> map)throws Exception;
	
	MessageResult syncData(String id)throws Exception;
	
	MessageResult syncDataOne(String id,String tendCode)throws Exception;
}
