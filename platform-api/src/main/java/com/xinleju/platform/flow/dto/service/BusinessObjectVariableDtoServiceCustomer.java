package com.xinleju.platform.flow.dto.service;

import java.util.Map;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface BusinessObjectVariableDtoServiceCustomer extends BaseDtoServiceCustomer{

	String queryBusiVariableListByTemlateId(String userInfo, String paramaterJson);

	/**
	 * 查询指定业务对象ID下的可用于条件分支的变量
	 * @param businessObjectId：业务对象ID
	 * @return
	 */
	String queryVariableUsedInExpressionBy(String userInfo, String businessObjectId);

	String queryListByCondition(String userJson, String paramaterJson);

	String getVariableTreeByBusiObject(String userJson, String paramaterJson);

	String queryRelatedCountByPrefixMap(String userJson, String paramaterJson);

	String queryListByParamMap(String userInfo, String paramaterJson);

	String updateSort(String userJson, String string, Map<String, Object> map);

	String fixVariableData(String userInfo, String saveJson);

}
