package com.xinleju.platform.flow.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.dto.MonitorFlDto;
import com.xinleju.platform.flow.entity.MonitorFl;

/**
 * @author admin
 * 
 * 
 */

public interface MonitorFlService extends  BaseService <String,MonitorFl>{

	List<MonitorFlDto> queryMonitorFlowList(Map<String,String> map);

	
}
