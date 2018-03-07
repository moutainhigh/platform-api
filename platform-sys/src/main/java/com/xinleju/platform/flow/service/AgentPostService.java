package com.xinleju.platform.flow.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.entity.AgentPost;

/**
 * @author admin
 * 
 * 
 */

public interface AgentPostService extends  BaseService <String,AgentPost>{

	/**
	 * 根据代理ID查询代理岗位
	 * 
	 * @param agentId
	 * @return
	 */
	List<String> queryAgentPostsBy(String agentId);
	/**
	 * 获取代理授权岗位列表
	 * @param agentId
	 * @return
	 */
	List<Map<String, Object>> queryAgentPostList(Map<String,Object> map);

	
}
