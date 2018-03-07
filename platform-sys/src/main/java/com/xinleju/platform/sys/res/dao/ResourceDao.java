package com.xinleju.platform.sys.res.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.dto.ResourceDto;
import com.xinleju.platform.sys.res.entity.Resource;

/**
 * @author admin
 *
 */

public interface ResourceDao extends BaseDao<String, Resource> {
	
	/**
	 * 根据系统id获取资源一级集合
	 * @param  paramater(系统id)
	 */
	public List<DataNodeDto> queryResourceListByAppId(Map<String, Object> paramater);
	/**
	 * 根据系统id获取资源全部集合
	 * @param  paramater(系统id)
	 */
	public List<DataNodeDto> queryResourceListByParentId(String paramater);
	
	/**
	 * 查询列表
	 * @param userType
	 * @return
	 */
	public List<ResourceDto> queryListByCondition(Map<String, Object> map) throws Exception;
	/**
	 * 校验编码重复
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getCodeCount(Map<String, Object> map) throws Exception;
	/**
	 * 查询所有父节点
	 * @param map appId系统ID
	 * @return
	 * @throws Exception
	 */
	public List<String> selectAllParentId(Map<String, Object> map) throws Exception;
	/**
	 * 判断菜单是否存在下级菜单或按钮
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer selectSonNum(Map<String,Object> map) throws Exception;
	/**
	 * 禁用菜单
	 * @param paramater
	 * @return
	 */
	public Integer lockMenu(Map map)throws Exception;
	
	/**
	 * 启用菜单
	 * @param paramater
	 * @return
	 */
	public Integer unLockMenu(Map map)throws Exception;
	
	/**
	 * 获取用户的菜单
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<ResourceDto> queryAuthMenu(Map<String,Object> map) throws Exception;
}
