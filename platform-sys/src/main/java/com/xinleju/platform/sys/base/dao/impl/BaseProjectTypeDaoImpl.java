package com.xinleju.platform.sys.base.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.BaseProjectTypeDao;
import com.xinleju.platform.sys.base.entity.BaseProjectType;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BaseProjectTypeDaoImpl extends BaseDaoImpl<String,BaseProjectType> implements BaseProjectTypeDao{

	public BaseProjectTypeDaoImpl() {
		super();
	}

/*	@Override
	public List<TypeNodeDto> queryPostTypeList(Map<String,Object> paramater) {
		List<TypeNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseProjectType.queryTypeNodeList", paramater);
		return list; 
	}*/

	@Override
	public BaseProjectType getBaseProjectTypeBySort(Map<String,Object> map) {
		BaseProjectType baseProjectType=getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.BaseProjectType.getBaseProjectBySort", map);
		return baseProjectType;
	}

	@Override
	public List<BaseProjectType> getBaseProjectList(Map<String, Object> param) {
		List<BaseProjectType> list=getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseProjectType.getBaseProjectBySort", param);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseProjectTypeDao#getRepeatObject(java.util.Map)
	 */
	@Override
	public List<BaseProjectType> getRepeatObject(Map<String, Object> map) {
		List<BaseProjectType> list=getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseProjectType.getRepeatObject", map);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseProjectTypeDao#getBaseProjectListToView(java.util.Map)
	 */
	@Override
	public List<BaseProjectType> getBaseProjectListToView(
			Map<String, Object> map) {
		return	getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseProjectType.getBaseProjectListToView", map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseProjectTypeDao#getBaseProjectParentIdMap()
	 */
	@Override
	public List<String> getBaseProjectParentIdList() {
		
		return	getSqlSession().selectList
				("com.xinleju.platform.sys.base.entity.BaseProjectType.getBaseProjectParentIdList");
				
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseProjectTypeDao#getBaseProjectTypeAllNodes(java.lang.String)
	 */
	@Override
	public void updateAllNodes(Map<String,Object> map) {
		getSqlSession().update("com.xinleju.platform.sys.base.entity.BaseProjectType.updateAllNodes", map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseProjectTypeDao#getDeleteBaseProjectList(java.lang.String)
	 */
	@Override
	public List<String> getDeleteBaseProjectList(Map<String,Object> map) {
		return	getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseProjectType.getDeleteBaseProjectList",map);
	}

	@Override
	public List<Map<String,Object>> getAllProductType() {
		return	getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseProjectType.getAllProductType");
	}

	@Override
	public List<BaseProjectType> queryListByIds(Set setIds) {
		Map map=new HashMap();
		map.put("setIds",setIds);
		return	getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseProjectType.queryListByIds",map);
	}

	@Override
	public List<Map<String, Object>> getLeafBaseProjectType() {
		return	getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseProjectType.getLeafBaseProjectType");

	}
	
}
