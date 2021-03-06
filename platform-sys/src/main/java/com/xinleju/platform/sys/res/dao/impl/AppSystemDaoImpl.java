package com.xinleju.platform.sys.res.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.res.dao.AppSystemDao;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.entity.AppSystem;
import com.xinleju.platform.sys.res.entity.DataCtrl;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class AppSystemDaoImpl extends BaseDaoImpl<String,AppSystem> implements AppSystemDao{

	public AppSystemDaoImpl() {
		super();
	}

	@Override
	public List<DataNodeDto> queryListDataCtrl(String paramater) {
		List<DataNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.DataCtrl.queryDataCtrlList", paramater);
		return list;
	}
    
	@Override
	public List<DataNodeDto> queryResourceListByAppId(String paramater) {
		List<DataNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Resource.queryResourceListAppId", paramater);
		return list;
	}
	 
	@Override
	public List<DataNodeDto> queryResourceListByParentId(String paramater) {
			List<DataNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Resource.queryResourceListByParentId", paramater);
			return list;
	}
	@Override
	public List<DataNodeDto> queryOperationList(String paramater) {
		List<DataNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Operation.queryOperationList", paramater);
		return list;
	}

	@Override
	public List<DataNodeDto> queryOperationListAll(String paramater) {
		List<DataNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Operation.queryOperationListAll", paramater);
		return list;
	}

	@Override
	public List<Map<String, Object>> querySystemList(String userType) {
		return getSqlSession().selectList("querySystemList", userType);
	}
	
	@Override
	public List<AppSystem> queryListByCondition(Map<String, Object> map) throws Exception{
		List<AppSystem> list= getSqlSession().selectList("com.xinleju.platform.sys.res.entity.AppSystem.queryListByCondition", map);
		return  list;
	}
	/**
	 * 校验编码重复
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getCodeCount(Map<String, Object> map) throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.AppSystem.getCodeCount", map);
	}
	
	/**
	 * 获取最大排序号
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getMaxSort(Map<String, Object> map) throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.AppSystem.getMaxSort", map);
	}
	/**
	 * 维护相关表全路径
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer updateAllPreFix(Map<String, Object> map) throws Exception{
		return getSqlSession().update("com.xinleju.platform.sys.res.entity.AppSystem.updateAllPreFix", map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.res.dao.AppSystemDao#queryListData()
	 */
	@Override
	public List<Map<String,Object>> queryListData() {
		List<Map<String,Object>> list=getSqlSession().selectList("com.xinleju.platform.sys.res.entity.AppSystem.queryListToSupplier");
	   return list;
	}
	/**
	 * 根据code获取appId
	 * add by gyh 2017-7-13
	 * @return
	 * @throws Exception
	 */
	public String selectAppIdByCode(Map<String, Object> map)throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.AppSystem.selectAppIdByCode",map);
	}
	@Override
	public List<AppSystem> queryListForSelect()throws Exception{
		return getSqlSession().selectList("com.xinleju.platform.sys.res.entity.AppSystem.queryListForSelect");
	}
}
