package com.xinleju.platform.sys.res.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.res.dao.ResourceDao;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.dto.ResourceDto;
import com.xinleju.platform.sys.res.entity.Resource;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class ResourceDaoImpl extends BaseDaoImpl<String,Resource> implements ResourceDao{

	public ResourceDaoImpl() {
		super();
	}

	
	@Override
	public List<DataNodeDto> queryResourceListByAppId(Map<String, Object> paramater) {
		String appId = "";
		if (paramater != null) {
			Set<String> set = paramater.keySet();
			for (String key : set) {
				if(key.equals("appId")){
					appId = (String) paramater.get(key);
				}
				
			}
		}
		List<DataNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Resource.queryResourceListAppId", appId);
		return list;
	}
	 
	@Override
	public List<DataNodeDto> queryResourceListByParentId(String paramater) {
		List<DataNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Resource.queryResourceListByParentId", paramater);
		return list;
	}
	
	@Override
	public List<ResourceDto> queryListByCondition(Map<String, Object> map) throws Exception{
		return getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Resource.queryListByCondition", map);
	}
	/**
	 * 校验编码重复
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getCodeCount(Map<String, Object> map) throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.Resource.getCodeCount", map);
	}
	/**
	 * 查询所有父节点
	 * @param map appId系统ID
	 * @return
	 * @throws Exception
	 */
	public List<String> selectAllParentId(Map<String, Object> map) throws Exception{
		return getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Resource.selectAllParentId", map);
	}
	
	/**
	 * 判断菜单是否存在下级菜单或按钮
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer selectSonNum(Map<String,Object> map) throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.Resource.selectSonNum", map);
	}
	
	/**
	 * 禁用菜单
	 * @param paramater
	 * @return
	 */
	public Integer lockMenu(Map map)throws Exception{
		return getSqlSession().update("com.xinleju.platform.sys.res.entity.Resource.lockMenu", map);
	}
	
	/**
	 * 启用菜单
	 * @param paramater
	 * @return
	 */
	public Integer unLockMenu(Map map)throws Exception{
		return getSqlSession().update("com.xinleju.platform.sys.res.entity.Resource.unLockMenu", map);
	}
	
	/**
	 * 获取用户的菜单
	 * @param paramater
	 * @return
	 */
	public List<ResourceDto> queryAuthMenu(Map map)throws Exception{
		return getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Resource.queryAuthMenu", map);
	}
}
