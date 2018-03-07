package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.BaseRegionDao;
import com.xinleju.platform.sys.base.entity.BaseRegion;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BaseRegionDaoImpl extends BaseDaoImpl<String,BaseRegion> implements BaseRegionDao{

	public BaseRegionDaoImpl() {
		super();
	}

/*	@Override
	public List<RegionDataDto> getBaseRegionData(String paramater) {
		List<RegionDataDto> list = getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseRegion.queryBaseRegionList", paramater);
		return list;
	}*/

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseRegionDao#queryBaseRegionList()
	 */
	@Override
	public List<BaseRegion> queryBaseRegionList() {
		List<BaseRegion> list = getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseRegion.queryBaseRegionList");
		return list;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseRegionDao#getBaseRegionParentIdList()
	 */
	@Override
	public List getBaseRegionParentIdList() {
		List<BaseRegion> list = getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseRegion.getBaseRegionParentIdList");
		return list;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseRegionDao#getRepeatNameCount(java.util.Map)
	 */
	@Override
	public Integer getRepeatNameCount(Map<String, Object> map) {
	 return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.BaseRegion.getRepeatNameCount",map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseRegionDao#getRepeatCodeCount(java.util.Map)
	 */
	@Override
	public Integer getRepeatCodeCount(Map<String, Object> map) {
	 return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.BaseRegion.getRepeatCodeCount",map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseRegionDao#getBaseRegionListByParentId(java.util.Map)
	 */
	@Override
	public List<BaseRegion> getBaseRegionListByParentId(
			Map<String, Object> param) {
		List<BaseRegion> list = getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseRegion.getBaseRegionListByParentId",param);
		return list;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseRegionDao#queryListIdsByPrefixId(java.util.Map)
	 */
	@Override
	public List<String> queryListIdsByPrefixId(Map<String, Object> map) {
		List<String> list = getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseRegion.queryListIdsByPrefixId",map);
		return list;
	}

	
	
}
