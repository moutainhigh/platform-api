package com.xinleju.platform.sys.res.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.entity.AppSystem;
import com.xinleju.platform.sys.res.entity.DataCtrl;

/**
 * @author admin
 *
 */

public interface AppSystemDao extends BaseDao<String, AppSystem> {
	/**
	 * 根据系统id获取数据控制对象集合
	 * @param  paramater(系统id)
	 */
	public List<DataNodeDto> queryListDataCtrl(String paramater);
	/**
	 * 根据系统id获取资源一级集合
	 * @param  paramater(系统id)
	 */
	public List<DataNodeDto> queryResourceListByAppId(String paramater);
	/**
	 * 根据系统id获取资源全部集合
	 * @param  paramater(系统id)
	 */
	public List<DataNodeDto> queryResourceListByParentId(String paramater);
	/**
	 * 根据资源id获取功能操作点一级集合
	 * @param  paramater(资源id)
	 */
	public List<DataNodeDto> queryOperationList(String paramater);
	/**
	 * 根据一级功能操作点id获取所有功能操作点集合
	 * @param  paramater(操作点id)
	 */
	public List<DataNodeDto> queryOperationListAll(String paramater);
	/**
	 * 查询系统类型根据不同的用户类型
	 * @param userType
	 * @return
	 */
	public List<Map<String, Object>> querySystemList(String userType);
	/**
	 * 根据角色id查询操作点一级
	 * @param userType
	 * @return
	 */

	
	/**
	 * 查询列表
	 * @param userType
	 * @return
	 */
	public List<AppSystem> queryListByCondition(Map<String, Object> map) throws Exception;
	
	/**
	 * 校验编码重复
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getCodeCount(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取最大排序号
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getMaxSort(Map<String, Object> map) throws Exception;
	/**
	 * 维护相关表全路径
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer updateAllPreFix(Map<String, Object> map) throws Exception;

	/**
	 * @return
	 */
	public List<Map<String, Object>> queryListData();
	/**
	 * 根据code获取appId
	 * add by gyh 2017-7-13
	 * @return
	 * @throws Exception
	 */
	public String selectAppIdByCode(Map<String, Object> map)throws Exception;
	public List<AppSystem> queryListForSelect()throws Exception;
}
