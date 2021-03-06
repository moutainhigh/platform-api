package com.xinleju.platform.sys.org.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.org.dto.PostQueryDto;
import com.xinleju.platform.sys.org.entity.UserPostScope;

/**
 * @author admin
 * 
 * 
 */

public interface UserPostScopeService extends  BaseService <String,UserPostScope>{

	
	/**
	 * 根据用户ID查询岗位范围
	 * @param userId
	 * @return
	 */
	public List<Map<String,Object>> queryUserPostScopeList(Map<String, Object> paramater)  throws Exception;
	
}
