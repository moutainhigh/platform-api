package com.xinleju.platform.sys.org.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.org.entity.UserPostScope;

/**
 * @author admin
 *
 */

public interface UserPostScopeDao extends BaseDao<String, UserPostScope> {
	
	
	/**
	 * 根据用户ID查询岗位范围
	 * @param userId
	 * @return
	 */
	public List<Map<String,Object>> queryUserPostScopeList(Map<String, Object> paramater)  throws Exception;
}
