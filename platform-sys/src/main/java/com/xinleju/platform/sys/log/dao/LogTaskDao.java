package com.xinleju.platform.sys.log.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.log.entity.LogDubbo;
import com.xinleju.platform.sys.log.entity.LogTask;

/**
 * @author admin
 *
 */

public interface LogTaskDao extends BaseDao<String, LogTask> {
	
	/**
	 * 分页数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<LogTask> getPageData(Map<String, Object> map);
	/**
	 * 总条数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer getPageDataCount(Map<String, Object> map);


}
