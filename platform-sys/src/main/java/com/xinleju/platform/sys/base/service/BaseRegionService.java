package com.xinleju.platform.sys.base.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.base.dto.BaseRegionDto;
import com.xinleju.platform.sys.base.entity.BaseRegion;
import com.xinleju.platform.sys.res.dto.DataNodeDto;

/**
 * @author admin
 * 
 * 
 */

public interface BaseRegionService extends  BaseService <String,BaseRegion>{
	/**
	 * 根据parentCode 查询省级市
	 * @param paramaterJson
	 * @return
	 */
	List<BaseRegion> getBaseRegionData()throws Exception;

	/**
	 * @return
	 */
	List<BaseRegionDto> getTypetree()throws Exception;

	/**
	 * @param baseRegion
	 */
	int saveBaseRegion(BaseRegion baseRegion)throws Exception;

	/**
	 * @return
	 */
	List<BaseRegion> queryBaseRegionList()throws Exception;

	/**
	 * @param baseRegion
	 * @return
	 */
	int updateBaseRegion(BaseRegion baseRegion)throws Exception;

	/**
	 * @param id
	 * @return
	 */
	int deletePseudo(BaseRegion baseRegion)throws Exception;

	int updateSort(Map map);

	
}
