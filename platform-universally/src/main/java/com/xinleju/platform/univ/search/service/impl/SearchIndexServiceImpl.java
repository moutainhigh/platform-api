package com.xinleju.platform.univ.search.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.AttachmentDto;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.attachment.dao.AttachmentDao;
import com.xinleju.platform.univ.search.dao.SearchCategoryDao;
import com.xinleju.platform.univ.search.dao.SearchCategoryPropertyDao;
import com.xinleju.platform.univ.search.dao.SearchIndexDao;
import com.xinleju.platform.univ.search.dto.SearchCategoryPropertyDto;
import com.xinleju.platform.univ.search.dto.SearchIndexDto;
import com.xinleju.platform.univ.search.entity.SearchCategory;
import com.xinleju.platform.univ.search.entity.SearchCategoryProperty;
import com.xinleju.platform.univ.search.entity.SearchIndex;
import com.xinleju.platform.univ.search.service.SearchIndexService;
import com.xinleju.platform.utils.ElasticSearchHelper;

/**
 * @author haoqp
 * 
 * 
 */
@Transactional
@Service
public class SearchIndexServiceImpl extends  BaseServiceImpl<String,SearchIndex> implements SearchIndexService{
	private static Logger log = Logger.getLogger(SearchIndexServiceImpl.class);

	@Autowired
	private SearchIndexDao searchIndexDao;
	
	@Autowired
	private SearchCategoryPropertyDao searchCategoryPropertyDao;

	@Autowired
	private SearchCategoryDao searchCategoryDao;
	
	@Autowired
	private AttachmentDao attachmentDao;
	

	@Transactional
	@Override
	public int save(SearchIndex object) throws Exception {
		// 添加索引
		List<SearchCategoryProperty> scpList = queryCategoryCodeList(object);
		
		String files = getSearchIndexFiles(object);
		
		resetIndexContent(object, scpList, files);
		
		log.info("------------保存索引内容--------------\n" + object);
		int count = searchIndexDao.save(object);
		ElasticSearchHelper.addIndex(object);
		return count;
	}

	/**
	 * 根据业务主键ID查询附件表得到索引对应的附件信息列表
	 * @param object
	 * @return
	 */
	private String getSearchIndexFiles(SearchIndex object) {
		// 根据业务ID查询附件表，并将附件列表信息添加到索引内容files中
		Map<String, Object> paramMap = new HashMap<>();
		List<String> businessIdList = new ArrayList<>();
		businessIdList.add(object.getEsDocId());
		paramMap.put("businessId", businessIdList);
		List<AttachmentDto> attList = attachmentDao.queryListByObject(paramMap);
		List<Map<String, String>> jsonObjList = new ArrayList<>();
		for (AttachmentDto aDto : attList) {
			Map<String, String> tmap = new HashMap<>();
			tmap.put("fullName", aDto.getFullName());
			tmap.put("path", aDto.getPath());
			jsonObjList.add(tmap);
		}
		return JSONArray.toJSONString(jsonObjList);
	}
	
	@Override
	public int saveBatch(List<SearchIndex> objectList) throws Exception {
		List<SearchCategoryProperty> scpList = queryCategoryCodeList(objectList.get(0));
		// TODO 待优化
		for (SearchIndex searchIndex : objectList) {
			String files = getSearchIndexFiles(searchIndex);
			resetIndexContent(searchIndex, scpList, files);
		}
		int count = searchIndexDao.saveBatch(objectList);
		ElasticSearchHelper.addIndexBatch(objectList);
		return count;
	}

	@Override
	public int updateBatch(List<SearchIndex> objectList) throws Exception {
		List<SearchCategoryProperty> scpList = queryCategoryCodeList(objectList.get(0));
		// TODO 待优化
		for (SearchIndex searchIndex : objectList) {
			String files = getSearchIndexFiles(searchIndex);
			resetIndexContent(searchIndex, scpList, files);
		}
		int count = searchIndexDao.updateBatch(objectList);
		ElasticSearchHelper.updateIndexBatch(objectList);
		return count;
	}

	@Override
	public int update(SearchIndex object) throws Exception {
		List<SearchCategoryProperty> scpList = queryCategoryCodeList(object);
		String files = getSearchIndexFiles(object);
		resetIndexContent(object, scpList, files);
		int count = searchIndexDao.update(object);
		// 添加索引
		ElasticSearchHelper.updateIndex(object);
		return count;
	}

	@Override
	public int deleteObjectById(String id) throws Exception {
		SearchIndex index = searchIndexDao.getObjectById(id);
		int count = searchIndexDao.deleteObjectById(id);
		ElasticSearchHelper.deleteIndex(index);
		
		return count;
	}

	@Override
	public int deleteAllSearchIndex(String esDocIndex, String esDocType, List<String> businessIds) throws Exception {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("esDocIndex", esDocIndex);
		parameters.put("esDocType", esDocType);
		parameters.put("businessIds", businessIds);
		int count = searchIndexDao.delete(SearchIndex.class.getName() + ".deleteAllSearchIndex", parameters);
		ElasticSearchHelper.deleteIndexBatch(esDocIndex, esDocType, businessIds);
		return count;
	}

	@Override
	public Page getPageFullTextQuery(Map<String, Object> parameters) throws Exception {
		// 查询全部的检索分类， 取得每个检索分类的对应的hostUrl
		List<SearchCategory> scLst = searchCategoryDao.queryList(new HashMap<String, Object>());
		Map<String, String> categoryHostUrlMap = new HashMap<>();
		for (SearchCategory sc : scLst) {
			categoryHostUrlMap.put(sc.getCode(), sc.getHostUrl());
		}
		Page page = ElasticSearchHelper.search(parameters, categoryHostUrlMap);
		return page;
	}
	
	private void resetIndexContent(SearchIndex object, List<SearchCategoryProperty> scpList, String files) throws IOException {
		// 重置SearchIndex对象中的索引内容，根据索引分类配置
		List<String> codeList = new ArrayList<String>();
		for (SearchCategoryProperty scp : scpList) {
			codeList.add(scp.getCode());
		}
		Map<String, Object> content = ElasticSearchHelper.content2IndexJsonString(object.getContent(), codeList, files);
		object.setContent(JSONObject.toJSONString(content));
		object.setId(object.getEsDocId());
	}

	private List<SearchCategoryProperty> queryCategoryCodeList(SearchIndex object) {
		Map<String, Object> map = new HashMap<>();
		// 分类编码就是索引type
		map.put("categoryCode", object.getEsDocType());
		
		// 租户ID就是索引index
		//map.put("tendId", object.getEsDocIndex());
		
		List<SearchCategoryProperty> scpList = searchCategoryPropertyDao.queryList(SearchCategoryProperty.class.getName() + ".queryListByCategoryCode", map);
		log.info("---------分类属性列表-----------\n" + scpList);
		return scpList;
	}

	@Override
	public void initIndexMapping(String tendId) throws Exception {
		Map<String, Object> tendIdMap = new HashMap<>();
		tendIdMap.put("tendId", tendId);
		List<Map<String, Object>> searchCategoryPropertyMap = searchCategoryPropertyDao.queryMapList(SearchIndex.class.getName() + ".queryListByTendId", tendIdMap);
		List<SearchCategoryPropertyDto> searchCategoryPropertyDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(searchCategoryPropertyMap), ArrayList.class, SearchCategoryPropertyDto.class);
		Map<String, List<SearchCategoryPropertyDto>> searchCategoryPropertyDtoGroup = new HashMap<>();
		for (SearchCategoryPropertyDto dto : searchCategoryPropertyDtoList) {
			if (!searchCategoryPropertyDtoGroup.containsKey(dto.getSearchIndexCategoryCode())) {
				searchCategoryPropertyDtoGroup.put(dto.getSearchIndexCategoryCode(), new ArrayList<SearchCategoryPropertyDto>());
			}
			searchCategoryPropertyDtoGroup.get(dto.getSearchIndexCategoryCode()).add(dto);
		}
		ElasticSearchHelper.initMapping(tendId, searchCategoryPropertyDtoGroup);
		SearchIndexDto searchIndex = new SearchIndexDto();
		ElasticSearchHelper.initMapping(searchIndex);
	}

	@Override
	public void updateIndexMapping(String tendId) throws Exception {
		
	}

}
