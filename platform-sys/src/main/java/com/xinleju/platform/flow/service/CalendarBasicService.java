package com.xinleju.platform.flow.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.entity.CalendarBasic;

/**
 * @author admin
 * 
 * 
 */

public interface CalendarBasicService extends  BaseService <String,CalendarBasic>{

	void deleteDataForInitAction(Map<String, Integer> map);

	void saveAndUpdateWorkDay(CalendarBasic calendarBasic);

	
}
