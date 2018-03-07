package com.xinleju.platform.flow.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dto.InstanceDto;
import com.xinleju.platform.flow.dto.InstanceStatDto;
import com.xinleju.platform.flow.entity.InstanceStat;

/**
 * @author admin
 * 
 * 
 */

public interface InstanceStatService extends  BaseService <String,InstanceStat>{

	List<InstanceStatDto> statUseTimes(Map<String, Object> map);

	List<InstanceStatDto> statInstanceEffiency(Map<String, Object>  map);

	List<InstanceStatDto> statOperateTimes(Map<String, Object>  map);

	List<InstanceStatDto> statTaskLength(Map<String, Object>  map);

	List<InstanceStatDto> detailTaskLengthList(Map map);

	List<InstanceDto> detailOperateTimesList(Map map);

	List<InstanceStatDto> detailInstanceEfficiencyList(Map map);

	List<InstanceStatDto> statTaskLengthByNoticeMsg(Map map);

	List<InstanceStatDto> detailStatTaskLengthByNoticeMsg(Map map);

	Page getPageForForm(String userJson, Map map);
}
