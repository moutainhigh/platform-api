package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.entity.CalendarDetail;

/**
 * @author admin
 *
 */

public interface CalendarDetailDao extends BaseDao<String, CalendarDetail> {

	void deleteByParamMap(CalendarDetail detail);

	void deleteDataForInitAction(Map<String, Integer> map);

	void setYearAsHolidayByParamMap(Map<String, Object> detailMap);

	void updateWorkDayByParamMap(Map<String, Object> detailMap);

	void updateDetailInfo(Map<String, Object> detailMap);
	//查询所有日期及其是否为工作日
	public  List<Map<String, String>> selectAllDays() ;
}
