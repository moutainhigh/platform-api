package com.xinleju.platform.sys.log.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.log.entity.LogDubbo;
import com.xinleju.platform.sys.log.entity.LogOperation;

/**
 * @author admin
 *
 */

public interface LogDubboDao extends BaseDao<String, LogDubbo> {
	
	/**
	 * 分页数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<LogDubbo> getPageData(Map<String, Object> map);
	/**
	 * 总条数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer getPageDataCount(Map<String, Object> map);

}
