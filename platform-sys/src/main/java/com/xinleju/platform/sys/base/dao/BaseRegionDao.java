package com.xinleju.platform.sys.base.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.base.entity.BaseRegion;
import com.xinleju.platform.sys.res.dto.DataNodeDto;

/**
 * @author admin
 *
 */

public interface BaseRegionDao extends BaseDao<String, BaseRegion> {
/**
 *  根据parentCode 查询省级市
 * @param paramaterJson
 * @return
 */
/*	List<RegionDataDto> getBaseRegionData(String paramaterJson);*/

/**
 * @return
 */
List<BaseRegion> queryBaseRegionList();

/**
 * @return
 */
List getBaseRegionParentIdList();

/**
 * @param map
 * @return
 */
Integer getRepeatNameCount(Map<String, Object> map);

/**
 * @param map
 * @return
 */
Integer getRepeatCodeCount(Map<String, Object> map);

/**
 * @param param
 * @return
 */
List<BaseRegion> getBaseRegionListByParentId(Map<String, Object> param);

/**
 * @param map
 * @return
 */
List<String> queryListIdsByPrefixId(Map<String, Object> map);
	
	

}
