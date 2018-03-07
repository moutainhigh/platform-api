package com.xinleju.platform.sys.res.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.res.dao.DataItemDao;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.dto.DataPointDto;
import com.xinleju.platform.sys.res.entity.DataItem;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class DataItemDaoImpl extends BaseDaoImpl<String,DataItem> implements DataItemDao{

	public DataItemDaoImpl() {
		super();
	}

	@Override
	public List<DataPointDto> querylistDataPoint(String paramater) {
		List<DataPointDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.DataPoint.queryDataPointList", paramater);
		return list;
	}
	
	/**
	 * 校验某系统下的作用域编码是否已存在
	 * @param map 系统ID和作用域编码
	 * @return
	 * @throws Exception
	 */
	@Override
	public Integer checkAppIdAndItemCode(Map<String,Object> map)throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.DataItem.checkAppIdAndItemCode", map);
	}
	/**
	 * 查询作用域业务对象和控制点
	 * @param map 查询条件
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> queryDataItemAndPointList(Map<String, Object> map) throws Exception {
		return getSqlSession().selectList("com.xinleju.platform.sys.res.entity.DataItem.selectDataItemAndPointList", map);
	}
	/**
	 * 查询作用域业务对象
	 * @param map 查询条件
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> queryDataItem(Map<String, Object> map) throws Exception {
		return getSqlSession().selectList("com.xinleju.platform.sys.res.entity.DataItem.selectDataItem", map);
	}
}
