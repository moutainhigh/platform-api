package com.xinleju.platform.flow.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.dto.AgentFlDto;
import com.xinleju.platform.flow.entity.AgentFl;

/**
 * @author admin
 * 
 * 
 */

public interface AgentFlService extends  BaseService <String,AgentFl>{

	List<AgentFlDto> queryAgentFlowList(Map<String, String> map);

	/**
	 * 根据代理ID查询代理模板
	 * 
	 * @param agentId
	 * @return
	 */
	List<String> queryAgentFlsBy(String agentId);

}