package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.entity.MonitorSetting;
import com.xinleju.platform.flow.model.FlowMonitorBean;

/**
 * @author admin
 *
 */

public interface MonitorSettingDao extends BaseDao<String, MonitorSetting> {

	int deleteAllFlowBySettingId(String setttingId);

	int deleteAllMonitorBySettingId(String setttingId);

	int deleteAllMonitoredBySettingId(String setttingId);

	int deleteAllMonitorPoint(String setttingId);

	List<Map<String, String>> queryMonitorList(String settingId);

	List<Map<String, String>> queryMonitoredList(String settingId);

	List<Map<String, String>> queryFlowList(String settingId);

	List<Map<String, String>> queryPointList(String settingId);

	List<FlowMonitorBean> queryMonitorByUser(Map<String, Object> params);

	List<FlowMonitorBean> queryMonitorByFlow(Map<String, Object> params);

	List<FlowMonitorBean> queryMonitorWhenException(Map<String, Object> params);
	
	

}
