package com.xinleju.platform.finance.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.finance.dto.CashFlowItemDto;
import com.xinleju.platform.finance.entity.CashFlowItem;

/**
 * @author admin
 * 
 * 
 */

public interface CashFlowItemService extends  BaseService <String,CashFlowItem>{

	List<CashFlowItemDto> queryTreeList(Map<String, Object> map)throws Exception ;

	CashFlowItem queryBudgetcapByBudget(Map<String, Object> map)throws Exception ;

	List<CashFlowItem> queryCashFlowItemList(Map<String, Object> map)throws Exception ;
}
