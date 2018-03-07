package com.xinleju.platform.sys.res.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.dto.DataPointDto;
import com.xinleju.platform.sys.res.entity.DataItem;

/**
 * @author admin
 *
 */

public interface DataItemDao extends BaseDao<String, DataItem> {
	/**
	 * 根据数据对象控制项id获取所有数据项控制点集合
	 * @param  paramater(数据对象控制项id)
	 */
	List<DataPointDto> querylistDataPoint(String id);
	
	/**
	 * 校验某系统下的作用域编码是否已存在
	 * @param map 系统ID和作用域编码
	 * @return
	 * @throws Exception
	 */
	public Integer checkAppIdAndItemCode(Map<String,Object> map)throws Exception;
	/**
	 * 查询作用域业务对象和控制点
	 * @param map 查询条件
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryDataItemAndPointList(Map<String,Object> map)throws Exception;
	/**
	 * 查询作用域业务对象
	 * @param map 查询条件
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryDataItem(Map<String,Object> map)throws Exception;
}
