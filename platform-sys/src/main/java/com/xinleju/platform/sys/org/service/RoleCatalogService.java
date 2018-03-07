package com.xinleju.platform.sys.org.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.org.dto.RoleCatalogDto;
import com.xinleju.platform.sys.org.dto.RoleNodeDto;
import com.xinleju.platform.sys.org.entity.Orgnazation;
import com.xinleju.platform.sys.org.entity.RoleCatalog;

/**
 * @author admin
 * 
 * 
 */

public interface RoleCatalogService extends  BaseService <String,RoleCatalog>{

	/**
	 * 获取目录子节点目录
	 * @param parentId
	 * @return
	 */
	public List<RoleNodeDto> queryRoleCatalogList(String parentId)  throws Exception;
	/**
	 * 获取根目录
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<RoleNodeDto> queryRoleCatalogRoot(Map<String,Object> map)  throws Exception;
	
	/**
	 * 维护角色分类以及角色全路径
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer updateCataAndRoleAllPreFix(Map<String, Object> map) throws Exception;
	
	/**
	 * 禁用角色
	 * @param paramater
	 * @return
	 */
	public Integer lockRole(Map map)throws Exception;
	
	/**
	 * 启用角色
	 * @param paramater
	 * @return
	 */
	public Integer unLockRole(Map map)throws Exception;
	//删除符合条件（没有被引用）的角色目录及其下级
	public Integer deleteOrgAllSon(RoleCatalogDto roleCatalogDto)throws Exception;
	
	public Integer updateNew(RoleCatalog catalogDto) throws Exception;
	/**
	 * 校验名字是否重复
	 * @param paramater
	 * @return
	 */
	public Integer checkName(Map map)throws Exception;
		
}
