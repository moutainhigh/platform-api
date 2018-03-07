package com.xinleju.platform.sys.res.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.res.dao.DataPointDao;
import com.xinleju.platform.sys.res.entity.DataPoint;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class DataPointDaoImpl extends BaseDaoImpl<String,DataPoint> implements DataPointDao{

	public DataPointDaoImpl() {
		super();
	}
	// 校验itemId+code是否重复 
	@Override
	public Integer checkItemIdAndPointCode(Map<String, Object> param) throws Exception {
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.DataPoint.checkItemIdAndPointCode", param);
	}

	//通过条件查询控制点列表
	@Override
	public List<DataPoint> queryDataPointByPram(Map<String, Object> param) {
		return getSqlSession().selectList("com.xinleju.platform.sys.res.entity.DataPoint.selectDataPointByPram", param);
	}

	
	/**
	 * 根据IDs逻辑删除控制点
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@Override
	public Integer deleteByIds(String[] ids) throws Exception {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("ids", ids);
		return getSqlSession().update("com.xinleju.platform.sys.res.entity.DataPoint.deleteByIds", map);
	}
	/**
	 * 通过条件查询控制点及授权情况
	 * @param param
	 * @return
	 */
	@Override
	public Map<String, Object> queryDataPointSelByPram(Map<String, Object> param) throws Exception {
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.DataPoint.selectDataPointSelByPram", param);
	}
	
	// 引用类型数量 
	@Override
	public Integer checkYyTypeCount(Map<String, Object> param) throws Exception {
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.DataPoint.checkYyTypeCount", param);
	}

	/**
	 * 根据数据权限项编码获取数据权限授权点列表
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> queryDataPointListByDataItem(Map<String, Object> paramMap) throws Exception {
		return getSqlSession().selectList("com.xinleju.platform.sys.res.entity.DataPoint.queryDataPointListByDataItem", paramMap);
	}
	@Override
	public Integer savePointSort(Map<String,Object> param)  throws Exception {
		return getSqlSession().insert("com.xinleju.platform.sys.res.entity.DataPoint.savePointSort", param);
	}
}
