package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.CalendarBasicDao;
import com.xinleju.platform.flow.entity.CalendarBasic;
import com.xinleju.platform.flow.entity.CalendarDetail;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class CalendarBasicDaoImpl extends BaseDaoImpl<String,CalendarBasic> implements CalendarBasicDao{

	public CalendarBasicDaoImpl() {
		super();
	}

	@Override
	public void deleteByParamMap(CalendarBasic calendarBasic) {
		String method = CalendarBasic.class.getName() + ".deleteByParamMap";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("year", calendarBasic.getYear());
		getSqlSession().update(method, map);
	}

	@Override
	public void deleteDataForInitAction(Map<String, Integer> map) {
		String method = CalendarBasic.class.getName() + ".deleteDataForInitAction";
		getSqlSession().delete(method, map);
	}

	@Override
	public void upateBasicInfoByParamMap(Map<String, Object> map) {
		String method = CalendarBasic.class.getName() + ".upateBasicInfoByParamMap";
		getSqlSession().update(method, map);
	}
}
