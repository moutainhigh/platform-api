package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_AGENT_POST",desc="代理授权范围")
public class AgentPost extends BaseEntity{
	
		
	@Column(value="post_id",desc="岗位id")
	private String postId;
    
	@Column(value="post_name",desc="岗位名称")
	private String postName;
		
	@Column(value="agent_id",desc="流程代理id")
	private String agentId;
    
  		
		
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
    
  		
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
    
  		
	
}
