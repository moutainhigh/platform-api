package com.xinleju.platform.sys.res.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.entity.AppSystem;

/**
 * @author admin
 * 
 * 
 */

/**
 * @author win7
 *
 */
public interface AppSystemService extends  BaseService <String,AppSystem>{

/**
 * 根据系统id获取数据控制对象集合
 * @param  paramater(系统id)
 */
public	List<DataNodeDto> queryListDataCtrl(String paramater) throws Exception;
/**
 * 根据系统id获取资源一级集合
 * @param  paramater(系统id)
 */
public List<DataNodeDto> queryResourceListByAppId(String paramater)throws Exception;
/**
 * 根据资源id获取功能操作点一级集合
 * @param  paramater(资源id)
 */
public List<DataNodeDto> queryOperationListByResourceId(String id)throws Exception;
/**
 * 根据一级功能操作点id获取所有功能操作点集合
 * @param  paramater(操作点id)
 */
public List<DataNodeDto> queryOperationListAll(String id)throws Exception;
/**
 * 根据系统id获取全部集合
 * @param  paramater(系统id)
 */
public List<DataNodeDto> queryResourceListByParentId(String id)throws Exception;
/**
 * 根据用户类型获取系统名称 id
 * @param  paramater(usertype)
 */

public List<Map<String, Object>> querySystemList(String userType)throws Exception;

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
public List<Map<String,Object>> queryListData()throws Exception;
/**
 * @return
 */
public Integer upOrDown(Map<String,Object> map)throws Exception;
/**
 * @return
 */
public int updateApp(AppSystem appSystem)throws Exception;
public List<AppSystem> queryListForSelect()throws Exception;

}
