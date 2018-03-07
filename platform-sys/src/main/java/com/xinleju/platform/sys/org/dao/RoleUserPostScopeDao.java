package com.xinleju.platform.sys.org.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.org.entity.RoleUserPostScope;

/**
 * @author admin
 *
 */

public interface RoleUserPostScopeDao extends BaseDao<String, RoleUserPostScope> {
	
	/**
	 * 删除之前引用
	 * @return
	 */
	public Integer deleteByRefId(Map<String, Object> paramater)  throws Exception;
	/**
	 * 批量保存引用
	 * @return
	 */
	public Integer insertRoleUserPostScopeBatch(Map<String, Object> paramater)  throws Exception;
	/**
	 * 查询管辖范围列表
	 * @return
	 */
	public List<Map<String,Object>> queryScopeByRefId(Map<String, Object> paramater)  throws Exception;

}
