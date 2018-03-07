package com.xinleju.platform.sys.org.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.org.dao.StandardRoleDao;
import com.xinleju.platform.sys.org.dto.RoleNodeDto;
import com.xinleju.platform.sys.org.dto.StandardRoleDto;
import com.xinleju.platform.sys.org.entity.StandardRole;
import com.xinleju.platform.sys.org.service.StandardRoleService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class StandardRoleServiceImpl extends  BaseServiceImpl<String,StandardRole> implements StandardRoleService{
	

	@Autowired
	private StandardRoleDao standardRoleDao;
	
	/**
	 * 获取目录子节点标准角色
	 * @param catalogId
	 * @return
	 */
	@Override
	public List<RoleNodeDto> queryRoleListByCataId(String catalogId)  throws Exception{
		return standardRoleDao.queryRoleListByCataId(catalogId);
	}
	
	/**
	 * 根据组织机构获取角色
	 * @param paramater
	 * @return
	 */
	@Override
	public List<RoleNodeDto> queryRoleListByOrgId(Map<String, Object> paramater)  throws Exception{
		return standardRoleDao.queryRoleListByOrgId(paramater);
	}
	
	/**
	 * 根据用户Id查询角色
	 * @param paramater
	 * @return
	 */
	@Override
	public List<StandardRoleDto> queryRoleListByUserId(Map<String, Object> paramater)  throws Exception{
		return standardRoleDao.queryRoleListByUserId(paramater);
	}
	
	/**
	 * 根据用户Id查询通用角色
	 * @param paramater
	 * @return
	 */
	@Override
	public List<StandardRoleDto> queryCurrencyRoleListByUserId(Map<String, Object> paramater)  throws Exception{
		return standardRoleDao.queryCurrencyRoleListByUserId(paramater);
	}
	/**
	 * 根据岗位Id查询通用角色
	 * @param paramater
	 * @return
	 */
	@Override
	public List<StandardRoleDto> queryCurrencyRoleListByPostId(Map<String, Object> paramater)  throws Exception{
		return standardRoleDao.queryCurrencyRoleListByPostId(paramater);
	}
	/**
	 * 根据岗位Id查询标准岗位
	 * @param paramater
	 * @return
	 */
	@Override
	public List<StandardRoleDto> queryStandardPostListByPostId(Map<String, Object> paramater)  throws Exception{
		return standardRoleDao.queryStandardPostListByPostId(paramater);
	}
	
	@Override
	public List<Map<String,String>> queryRolesByIds(Map map)throws Exception{
		return standardRoleDao.queryRolesByIds(map);
	}
}
