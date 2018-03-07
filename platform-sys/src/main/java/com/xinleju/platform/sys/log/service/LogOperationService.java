package com.xinleju.platform.sys.log.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.log.entity.LogOperation;

/**
 * @author admin
 * 
 * 
 */

public interface LogOperationService extends  BaseService <String,LogOperation>{
	/**
	 * 模糊查询-分页
	 * @return
	 */
	public Page vaguePage(Map<String, Object> map)throws Exception;
	
}
