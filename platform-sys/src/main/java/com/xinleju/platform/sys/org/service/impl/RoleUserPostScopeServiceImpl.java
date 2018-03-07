package com.xinleju.platform.sys.org.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.sys.org.dao.RoleUserPostScopeDao;
import com.xinleju.platform.sys.org.entity.RoleUserPostScope;
import com.xinleju.platform.sys.org.service.RoleUserPostScopeService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class RoleUserPostScopeServiceImpl extends  BaseServiceImpl<String,RoleUserPostScope> implements RoleUserPostScopeService{
	

	@Autowired
	private RoleUserPostScopeDao roleUserPostScopeDao;
	
	/**
	 * 批量保存引用
	 * @return
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Integer saveBatchRoleUserPostScope(Map<String, Object> param)  throws Exception{
		List<Map<String, Object>> list=(List<Map<String, Object>>)param.get("list");
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> m=list.get(i);
			if(!m.containsKey("id")||m.get("id")==null||StringUtils.isBlank(m.get("id").toString())){
				m.put("id", IDGenerator.getUUID());
			}
		}
		//删除之前的
		roleUserPostScopeDao.deleteByRefId(param);
		//批量保存
		return roleUserPostScopeDao.insertRoleUserPostScopeBatch(param);
	}
	/**
	 * 查询管辖范围列表
	 * @return
	 */
	public List<Map<String,Object>> queryScopeByRefId(Map<String, Object> param)  throws Exception{
		return roleUserPostScopeDao.queryScopeByRefId(param);
	}
}
