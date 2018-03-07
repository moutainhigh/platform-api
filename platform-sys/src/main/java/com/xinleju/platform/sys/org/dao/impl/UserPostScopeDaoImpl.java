package com.xinleju.platform.sys.org.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.UserPostScopeDao;
import com.xinleju.platform.sys.org.entity.UserPostScope;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class UserPostScopeDaoImpl extends BaseDaoImpl<String,UserPostScope> implements UserPostScopeDao{

	public UserPostScopeDaoImpl() {
		super();
	}

	
	@Override
	public List<Map<String,Object>> queryUserPostScopeList(Map<String, Object> paramater)  throws Exception{
		String userId = "";
		if (paramater != null) {
			Set<String> set = paramater.keySet();
			for (String key : set) {
				if(key.equals("userId")){
					userId = (String) paramater.get(key);
				}
				
			}
		}
		List<Map<String,Object>> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.UserPostScope.queryUserPostScopeList", userId);
		return list;
	}
}
