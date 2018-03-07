package com.xinleju.platform.univ.search.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.univ.search.entity.SearchIndex;

/**
 * @author haoqp
 * 
 * 
 */

public interface SearchIndexService extends  BaseService <String,SearchIndex>{
	
	/**
	 * 批量删除索引
	 * @param esDocIndex ES 索引
	 * @param esDocType ES 类型
	 * @param businessIds 业务主键列表
	 * @return
	 * @throws Exception
	 */
	int deleteAllSearchIndex(String esDocIndex, String esDocType, List<String> businessIds) throws Exception;
	
	/**
	 * 全文检索
	 * 
	 * @param map
	 * @param integer
	 * @param integer2
	 * @return
	 * @throws Exception 
	 */
	Page getPageFullTextQuery(Map<String, Object> parameters) throws Exception;
	
	void initIndexMapping(String tendId) throws Exception;
	
	void updateIndexMapping(String tendId) throws Exception;
}
