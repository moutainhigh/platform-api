package com.xinleju.platform.univ.task.dao;

import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.univ.task.entity.TaskInfo;

/**
 * @author admin
 *
 */

public interface TaskInfoDao extends BaseDao<String, TaskInfo> {
	
	String getDataBaseKey(String sql, Map<String, Object> params);
	

}