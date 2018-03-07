package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.dto.ApprovalStatDto;
import com.xinleju.platform.flow.dto.InstanceDto;
import com.xinleju.platform.flow.dto.InstanceStatDto;
import com.xinleju.platform.flow.entity.InstanceStat;

/**
 * @author admin
 *
 */

public interface InstanceStatDao extends BaseDao<String, InstanceStat> {

	List<InstanceStatDto> statUseTimes(Map<String, Object> map);

	List<InstanceStatDto> statInstanceEffiency(Map<String, Object> map);

	List<InstanceStatDto> statOperateTimes(Map<String, Object> map);

	List<InstanceStatDto> statTaskLength(Map<String, Object> map);

	InstanceStatDto queryHolidaySumAndMinMAxDate(String startDate, String endDate);

	List<InstanceStatDto> detailTaskLengthList(Map map);

	List<InstanceDto> detailOperateTimesList(Map map);

	List<InstanceStatDto> detailInstanceEfficiencyList(Map map);

	String queryDayTypeByGivenDay(String endDate);

	List<ApprovalStatDto> getPageSort(Map map);

	Integer getPageSortCount(Map map);
	
	

}
