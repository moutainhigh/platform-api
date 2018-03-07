package com.xinleju.platform.univ.search.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.univ.search.entity.SearchCategory;
import com.xinleju.platform.univ.search.entity.SearchCategoryProperty;

/**
 * @author haoqp
 * 
 * 
 */

public interface SearchCategoryService extends  BaseService <String,SearchCategory>{

	/**
	 * 新增检索分类
	 * @param searchCategory 检索分类
	 * @param searchCategoryPropertyList 检索分类属性列表
	 * @return
	 * @throws Exception
	 */
	int save(SearchCategory searchCategory, List<SearchCategoryProperty> searchCategoryPropertyList) throws Exception;
	
	/**
	 * 更新检索分类
	 * @param searchCategory 检索分类
	 * @param searchCategoryPropertyList 检索分类属性列表
	 * @return
	 * @throws Exception
	 */
	int update(SearchCategory searchCategory, List<SearchCategoryProperty> searchCategoryPropertyList) throws Exception;

	/**
	 * 查询存在记录条数，用于更新时验证编码名称重复
	 * 
	 * @param paramMap
	 * @return
	 */
	int getCountForUpdate(Map<String, Object> paramMap);

	/**
	 * 启用检索分类，建立索引映射
	 * 
	 * @param searchCategory
	 * @return
	 * @throws Exception 
	 */
	int updateStatus(SearchCategory searchCategory) throws Exception;
	
}
