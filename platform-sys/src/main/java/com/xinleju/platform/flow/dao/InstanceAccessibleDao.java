package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.dto.InstanceAccessibleDto;
import com.xinleju.platform.flow.entity.InstanceAccessible;

/**
 * @author admin
 *
 */

public interface InstanceAccessibleDao extends BaseDao<String, InstanceAccessible> {

	/**
	 * 删除指定流程实例的全部可阅人
	 * 
	 * @param instanceId
	 */
	void deleteByInstanceId(String instanceId);

	void deleteReaderDataByParamMap(Map<String, Object> paramMap);

	List<InstanceAccessibleDto> queryInstanceReaderList(Map<String, Object> map);
	
}
