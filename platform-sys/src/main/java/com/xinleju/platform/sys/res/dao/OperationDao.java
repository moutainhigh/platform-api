package com.xinleju.platform.sys.res.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.dto.OperationDto;
import com.xinleju.platform.sys.res.entity.Operation;

/**
 * @author admin
 *
 */

public interface OperationDao extends BaseDao<String, Operation> {
	
	/**
	 * 根据资源id获取功能操作点(查询菜单下的一级按钮)
	 * @param map(资源ID)
	 * @return
	 */
	public List<DataNodeDto> queryOperationListRootByResourceId(Map<String, Object> map) throws Exception;
	
	/**
	 * 根据资源id获取所有按钮
	 * @param map(资源ID)
	 * @return
	 */
	public List<DataNodeDto> queryOperationListAllByResourceId(Map<String, Object> map) throws Exception;

	/**
	 * 校验编码重复
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getCodeCount(Map<String, Object> map) throws Exception;
	/**
	 * 查询列表
	 * @param userType
	 * @return
	 */
	public List<OperationDto> queryListByCondition(Map<String, Object> map) throws Exception;
	/**
	 * 判断菜单是否存在下级按钮
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer selectSonNum(Map<String,Object> map) throws Exception;
}
