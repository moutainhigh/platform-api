package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface FlDtoServiceCustomer extends BaseDtoServiceCustomer {

	/**
	 * 根据条件获取流程模板列表
	 * 
	 * @param userInfo
	 * @param paramater
	 * @return
	 */
	public String queryFlList(String userInfo, String paramater);
	/**
	 * 根据条件分组获取流程模板列表
	 * 
	 * @param userInfo
	 * @param paramater
	 * @return
	 */
	public String queryFlByGroupList(String userInfo, String paramater);
	
	/**
	 * 把整个FLDto的大数据全部保存
	 * 
	 * @param userInfo
	 * @param paramater
	 * @return
	 */
	public String saveAll(String userInfo, String paramater);

	/**
	 * 根据ID查询流程模板及相关属性
	 * 
	 * @param userInfo
	 * @param paramater
	 * @return
	 */
	public String getAll(String userInfo, String paramater);
	
	/**
	 * 流程发起
	 * 
	 * @param userInfo
	 * @param parameter：flCode:流程模板code，businessId:业务单据ID,businessObjectCode:业务对象code
	 * @return
	 */
	public String start(String userInfo, String parameter);

	/**
	 * 流程仿真
	 * @param userInfo
	 * @param parameter
	 * @return
	 */
	public String emulation(String userInfo,String parameter);

	public String queryListByApprover(String userJson, String paramaterJson);
	
	public String queryListByParticipant(String userJson, String paramaterJson);

	public String queryViewList(String userJson, String paramaterJson);

	public String queryFlowBusiObjectList(String userJson, String paramaterJson);
	public String getFlowRetractForInstance(String userJson, String paramaterJson);

	/**
	 * 根据业务对象编码查找对应的默认流程模板
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String queryDefaultFl(String userJson, String paramaterJson);
	
	/**
	 * 根据流程模板编号字符串来逻辑删除对应的流程模板的所有版本
	 * @param codeText
	 * @return
	 */
	public String deleteFlowsByCodeText(String userJson, String string);

	/**
	 * 设置默认模板
	 * @param userJson
	 * @param parameterJson
	 * @return
	 */
	public String setDefaultFl(String userJson,String parameterJson);
	 /**  批量修改模板
	 * @param userJson
	 * @param parameterJson
	 * @return
	 */
	public String updateFlowsByids(String userJson,String parameterJson);
}
