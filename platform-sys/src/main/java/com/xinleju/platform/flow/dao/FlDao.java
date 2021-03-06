package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dto.FlDto;
import com.xinleju.platform.flow.entity.Fl;

/**
 * @author admin
 *
 */

public interface FlDao extends BaseDao<String, Fl> {

	/**
	 * 根据业条件获取流程模板列表
	 * 
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public Page queryFlList(Map<String, Object> paramater) throws Exception;
	public Page queryFlListNew(Map<String, Object> paramater) throws Exception;
	
	public Page queryFlByGroupList(Map<String, Object> paramater) throws Exception;
	
	/**
	 * 删除流程模板基本信息及相关信息
	 * 
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public int deleteFl(String paramater) throws Exception;

	/**
	 * 查询指定业务对象下面的默认流程模板
	 * 
	 * @param businessCode
	 * @return
	 */
	public Fl queryDefaultFlow(String businessCode);

	/**
	 * 根据流程模板编码查询启用发布的流程模板
	 * 
	 * @param flCode
	 * @return
	 */
	public Fl queryReadyFlowBy(String flCode);

	public List<FlDto> queryListByApprover(Map<String, String> paramMap);
	
	
	public List<FlDto> queryListByParticipant(Map<String, String> paramMap);

	public List<FlDto> queryViewList(Map<String, String> paraMap);

	public List<FlDto> queryFlowBusiObjectList(Map<String, String> paramMap);
	public Map<String, String> getFlowRetractForInstance(Map<String, String> paramMap);

	/**
	 * 根据用户ID、用户岗位ID、用户角色ID查询用户所在的模板
	 * 
	 * @param params
	 * @return
	 */
	public List<String> queryFlListBy(List<String> params);
	
	public int deleteFlowsByCodeText(String[] codes);

	/**
	 * 根据业务对象编码查询流程模板列表
	 *
	 * @param businessCode
	 * @return
	 */
	public List<Map<String, String>> queryFlowTemplateBy(String businessCode);
	/**
	 * 查询用户拥有审核权限的流程模板
	 * @param paraMap
	 * @return
	 */
	public List<FlDto> queryUserFlowBusiObjectList(Map<String, String> paraMap);
	public Integer updateFlowsByids(Map<String, Object> paraMap);

	//获取生效版本
	public List<FlDto> getPublishFls(Map<String, Object> paraMap);
	
	/**
	 	* @Description:查询指定流程模板的所有版本
	  * @author:zhangfangzhi
	  * @date 2018年1月31日 下午2:16:34
	  * @version V1.0
	 */
	public List<String> queryAllFlByCode(Map<String, Object> paramMap);
	
	/**
	  * @Description:根据可阅人id查询所有对应的流程模板
	  * @author:zhangfangzhi
	  * @date 2018年2月1日 下午4:09:53
	  * @version V1.0
	 */
	public List<String> queryUpdateFlDataList(Map<String, Object> params);


}
