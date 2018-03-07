package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dto.SysNoticeMsgDto;
import com.xinleju.platform.flow.dto.SysNoticeMsgStatDto;
import com.xinleju.platform.flow.entity.SysNoticeMsg;

/**
 * @author admin
 *
 */

public interface SysNoticeMsgDao extends BaseDao<String, SysNoticeMsg> {

	List<SysNoticeMsgDto> queryTwoSumData(Map<String, String> paramMap);

	List<SysNoticeMsg> queryHaveDoneList(Map<String, String> paramMap);

	List<SysNoticeMsgDto> queryDBDYList(Map<String, String> paramMap);

	int updateStatusOfNoticeMsg(Map<String, Object> paramMap);


	int setMessageOpened(String messageId);

	int deleteOpTypeDataByParamMap(Map<String, Object> paramMap);

	Page pageQueryByParamMap(Map<String, Object> map, Integer start, Integer limit);

	int updateStatusOfNoticeMsgByCurrentUser(Map<String, Object> paramMap);

	List<SysNoticeMsgDto> queryMsgDto24Hours(Map<String, String> paramMap);

	SysNoticeMsgStatDto queryFirstTypeStatData(Map<String, String> map);

	//查询所有用户的待办/待阅的数据汇总, 入参是opType='DB'/'DY', 返回值是userId和toDoSum的List
	List<SysNoticeMsgDto> queryAllUserToDoList(Map<String, Object> queryMap);
	
	List<SysNoticeMsgDto> doQueryByParamMap(Map<String, Object> paramMap, Integer start, Integer limit);

	/**
	 * 根据opType分页查询消息列表，主要用于首页代办列表
	 * @param parameters
	 * @return
	 */
	public List<SysNoticeMsgDto> queryFlowMsgListByPage(Map<String,Object> parameters);

	/**
	 * 根据opType分页查询消息列表总记录，主要用于首页代办列表
	 * @param parameters
	 * @return
	 */
	public int queryFlowMsgListByPageCount(Map<String,Object> parameters);

	//List<SysNoticeMsg> searchDataByKeyword(Map<String, String> paramMap);
	List<SysNoticeMsgDto> searchDataByKeywordPageParam(Map<String, Object> map);

	Integer searchDataCountByKeywordPageParam(Map<String, Object> map);

	int deleteMsgOfAdminBy(String instanceId);

    List<SysNoticeMsgDto> queryNoticeMsg(Map map);

	int updateMsg(Map<String, Object> params);

	List<SysNoticeMsgDto> statisticsNoticeMsg(Map map);

	List<Map> selectProcessInfo(Map map);

	List<Map> selectOrgInfo(Map map);

	int updateStatusOfNoticeMsgByBatch(Map<String, Object> paramMap);

	SysNoticeMsg getLanuchAssist(Map<String, Object> paramMap);
	
	void assistMessageUpdate(Map<String, Object> paramMap);
	
	List<SysNoticeMsgDto>  getMsgBussniessObjects(Map<String, Object>  map);
	void delMsgAndRecord(Map<String, Object> paramMap);

	SysNoticeMsg getMsgByGroupId(Map<String, Object> paramMap);
}
