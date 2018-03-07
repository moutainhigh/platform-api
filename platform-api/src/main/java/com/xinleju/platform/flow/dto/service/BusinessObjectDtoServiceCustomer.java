package com.xinleju.platform.flow.dto.service;

import java.util.Map;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface BusinessObjectDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 获取业务对象树
	 * 
	 * @param parentId
	 * @return
	 */
	public String getTree(String userInfo, String paramater);

	public String seachKeyword(String userinfo, String paramaterJson);

	public String saveObjectAndVariableData(String userinfo, String saveJson);

	public String queryListByCondition(String userJson, String paramaterJson);

	public String getTreeBySystemApp(String userJson, String paramaterJson);

	public String queryCountLikePrefixMap(String userJson, String paramaterJson);

	/**
	 * 根据应用系统去获取业务对象分类树（但不含业务对象本身）
	 */
	public String getCategoryTreeBySystemApp(String userJson, String paramaterJson);

	public String queryRelatedCountByPrefixMap(String userJson, String paramaterJson);

	public String copyAndSaveBusiObjectData(String userJson, String paramaterJson);

	public String saveBusinessObject(String userJson, String saveJson);

	public String updateSort(String userJson, String string, Map<String, Object> map);

}
