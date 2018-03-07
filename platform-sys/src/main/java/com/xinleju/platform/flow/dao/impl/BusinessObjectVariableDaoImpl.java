package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.BusinessObjectVariableDao;
import com.xinleju.platform.flow.dto.BusinessObjectVariableDto;
import com.xinleju.platform.flow.entity.BusinessObject;
import com.xinleju.platform.flow.entity.BusinessObjectVariable;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BusinessObjectVariableDaoImpl extends BaseDaoImpl<String,BusinessObjectVariable> implements BusinessObjectVariableDao{

	public BusinessObjectVariableDaoImpl() {
		super();
	}

	@Override
	public List<BusinessObjectVariable> queryBusiVariableListByTemlateId(String businessObjectId) {
		String selectorText = "com.xinleju.platform.flow.entity.BusinessObjectVariable.queryBusiVariableListByTemlateId";
		return getSqlSession().selectList(selectorText, businessObjectId);//flId
	}

	@Override
	public List<BusinessObjectVariable> queryVariableUsedInExpressionBy(String businessObjectId) {
		return getSqlSession().selectList(BusinessObjectVariable.class.getName() + ".queryVariableUsedInExpressionBy", businessObjectId);
	}

	@Override
	public int deleteVariableListByObjectId(String businessObjectId) {
		return getSqlSession().update(BusinessObjectVariable.class.getName() + ".deleteVariableListByObjectId", businessObjectId);
		
	}

	@Override
	public List<BusinessObjectVariableDto> queryListByCondition(Map paramMap) {
		return getSqlSession().selectList(BusinessObjectVariable.class.getName()+".queryListByCondition", paramMap);
	}

	@Override
	public List<String> selectAllParentId(Map paramMap) {
		return getSqlSession().selectList(BusinessObjectVariable.class.getName()+".selectAllParentId", paramMap);
	}

	@Override
	public List<BusinessObjectVariableDto> getVariableTreeByBusiObject(Map<String, String> paramMap) {
		return getSqlSession().selectList(BusinessObjectVariable.class.getName()+".getVariableTreeByBusiObject", paramMap);
	}

	@Override
	public int updateObjectPrefixIdByParamMap(Map<String, String> paramMap) {
		String method = BusinessObjectVariable.class.getName()+".updateObjectPrefixIdByParamMap";
		return getSqlSession().update(method, paramMap);
	}

	@Override
	public Integer queryRelatedCountByPrefixMap(Map paramMap) {
		//先查询出相关业务变量的ID,  
		String method1 = BusinessObjectVariable.class.getName()+".queryRelatedVariableIdsByPrefixMap";
		List<String> variableIdList = getSqlSession().selectList(method1, paramMap);
		//System.out.println("variableIdList="+variableIdList);
		if(variableIdList!=null && variableIdList.size()>0){
			//System.out.println("---- variableIdList.size="+variableIdList.size());
			String fullText = "";
			for(String itemText : variableIdList){
				fullText += itemText + ",";
			}
			fullText = fullText.substring(0, fullText.length()-1);
			//然后把这些ID组合成数组,再做一次查询
			//System.out.println("---- fullText="+fullText);
			Map<String,Object> paramMap2 = new HashMap<String,Object>();
			paramMap2.put("variableIds", fullText.split(","));
			paramMap2.put("businessObjectId", paramMap.get("businessObjectId"));
			String method = BusinessObjectVariable.class.getName()+".queryRelatedCountByPrefixMap";
			//System.out.println("---- method="+method);
			return getSqlSession().selectOne(method, paramMap2);
		}
		return 0;
		
	}

	@Override
	public Integer deleteObjectAndChileren(Map<String, String> paramMap) {
		String method = BusinessObjectVariable.class.getName()+".deleteObjectAndChileren";
		return getSqlSession().update(method, paramMap);
	}

	@Override
	public List<BusinessObjectVariableDto> queryListByParamMap(Map<String, Object> paramMap) {
		String method = BusinessObjectVariable.class.getName()+".queryListByParamMap";
		return getSqlSession().selectList(method, paramMap);
	}

	@Override
	public List<BusinessObjectVariable> queryBusinessObjectVariableListByParam(Map<String, Object> paramMap) {
		String method = BusinessObjectVariable.class.getName()+".queryBusinessObjectVariableListByParam";
		return getSqlSession().selectList(method, paramMap);
	}

	@Override
	public void updateAllNodes(Map<String, Object> paramMap) {
		String method = BusinessObjectVariable.class.getName()+".updateAllNodes";
		getSqlSession().update(method, paramMap);
	}

	@Override
	public void updateAllNodesSortAndPrefix(Map<String, Object> updateMap) {
		String method = BusinessObjectVariable.class.getName()+".updateAllNodesSortAndPrefix";
		getSqlSession().update(method, updateMap);
		
	}

	@Override
	public List<BusinessObjectVariable> queryToFixDataList() {
		String method = BusinessObjectVariable.class.getName()+".queryToFixDataList";
		return getSqlSession().selectList(method);
	}

	@Override
	public void doFixDataUpdate(Map<String, Object> updateMap) {
		String method = BusinessObjectVariable.class.getName()+".doFixDataUpdate";
		getSqlSession().update(method, updateMap);
	}
}
