package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;
import com.xinleju.platform.flow.dto.SysNoticeMsgDto;

import java.util.List;
import java.util.Map;

public interface SysNoticeMsgDtoServiceCustomer extends BaseDtoServiceCustomer {

	String queryTwoSumData(String userInfo, String paramaterJson);

	String queryHaveDoneList(String userInfo, String paramaterJson);

	String queryDBDYList(String userJson, String paramaterJson);

	String updateStatusOfNoticeMsg(String userJson, String paramaterJson);

	String searchDataByKeyword(String userJson, String paramaterJson);

	/**
	 * 将指定消息设置为已打开过状态
	 * 
	 * @param userJson
	 * @param messageId
	 * @return
	 */
	String setMessageOpened(String userJson, String messageId);

	String deleteOpTypeDataByParamMap(String userJson, String paramaterJson);

	String pageQueryByParamMap(String userJson, String paramaterJson);

	String queryMobileApproveByParamMap(String userJson, String paramaterJson);

	String updateStatusOfNoticeMsgByCurrentUser(String userJson, String paramaterJson);

	String queryTotalStatData(String userJson, String paramaterJson);

	String queryFirstTypeStatData(String userJson, String paramaterJson);

	//给微信平台发送消息
	String sendWeixinMsg(String userJson, String updateJson);

	public String queryMsgListByPage(String userJson,String parameters);

	public String queryPersonalMsgForPortal(String userJson,String parameters);
	
	public String queryPersonalMsgForPortal2(String userJson, Map<String, Object> params);
	
	public String getLoginNameByVX(String userJson,String parameters);

	//跨租户查询消息
	int queryMsgTotal(String userInfo, Map<String, Object> params);

	List<SysNoticeMsgDto> queryMsgList(String userInfo, Map<String, Object> params);

	int queryMsgMoreTotal(String userInfo, Map<String, Object> params);

	List<SysNoticeMsgDto> queryMsgMoreList(String userInfo, Map<String, Object> params);

	//对外查询消息
	String queryNoticeMsg(String userInfo,String params);
	//定时重发失败的推送消息
	String resendNoticeMsg(String userInfo,String params);

	String updateStatusOfNoticeMsgBatch(String userJson, String paramaterJson);
	List<SysNoticeMsgDto> getMsgBussniessObjects(String userJson, String paramater) ;
	String deletePseudoBatchAndRecord(String userJson, String string);

}
