package com.xinleju.platform.flow.dto;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;



/**
 * @author admin
 * 
 *
 */
public class AgentDto extends BaseDto{

		
	//名称
	private String name;
    
  		
	//状态
	private String status;//1-启用  0-禁用
    
  		
	//代理类型
	private String proxyType;
    
  		
	//授权人
	private String authorizer;
    
  		
	//授权人id
	private String authorizerId;
    
  		
	//被授权人
	private String authorized;
    
  		
	//被授权人id
	private String authorizedId;
    
  		
	//授权开始日期
	private Timestamp startDate;
    
  		
	//授权结束日期
	private Timestamp endDate;
    
  		
	//备注
	private String remark;
    
	//授权的岗位范围1-全部 岗位  2-指定岗位
	private String postScope;
	//实例的流程范围1-全部模板   2-指定模板	
	private String flowScope;
	
	private List<AgentFlDto> flowList = new ArrayList<AgentFlDto>();
	private List<AgentPostDto> postList = new ArrayList<AgentPostDto>();
	
	//以下是查询数据返回时需要用到
	private String agentId;
	private String flId;
	private String instanceName;
	private String startUserName;
	private String busiObjectName;
	private String currentApprovers;
	
	private String pcUrl, flCode;
	private String businessId, businessObjectId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public String getProxyType() {
		return proxyType;
	}
	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}
    
  		
	public String getAuthorizer() {
		return authorizer;
	}
	public void setAuthorizer(String authorizer) {
		this.authorizer = authorizer;
	}
    
  		
	public String getAuthorizerId() {
		return authorizerId;
	}
	public void setAuthorizerId(String authorizerId) {
		this.authorizerId = authorizerId;
	}
    
  		
	public String getAuthorized() {
		return authorized;
	}
	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}
    
  		
	public String getAuthorizedId() {
		return authorizedId;
	}
	public void setAuthorizedId(String authorizedId) {
		this.authorizedId = authorizedId;
	}
    
  		
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
    
  		
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPostScope() {
		return postScope;
	}
	public void setPostScope(String postScope) {
		this.postScope = postScope;
	}
	public String getFlowScope() {
		return flowScope;
	}
	public void setFlowScope(String flowScope) {
		this.flowScope = flowScope;
	}
	public List<AgentFlDto> getFlowList() {
		return flowList;
	}
	public void setFlowList(List<AgentFlDto> flowList) {
		this.flowList = flowList;
	}
	public List<AgentPostDto> getPostList() {
		return postList;
	}
	public void setPostList(List<AgentPostDto> postList) {
		this.postList = postList;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getFlId() {
		return flId;
	}
	public void setFlId(String flId) {
		this.flId = flId;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getStartUserName() {
		return startUserName;
	}
	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}
	public String getBusiObjectName() {
		return busiObjectName;
	}
	public void setBusiObjectName(String busiObjectName) {
		this.busiObjectName = busiObjectName;
	}
	public String getCurrentApprovers() {
		return currentApprovers;
	}
	public void setCurrentApprovers(String currentApprovers) {
		this.currentApprovers = currentApprovers;
	}
	@Override
	public String toString() {
		return name;
	}
	public String getPcUrl() {
		return pcUrl;
	}
	public void setPcUrl(String pcUrl) {
		this.pcUrl = pcUrl;
	}
	public String getFlCode() {
		return flCode;
	}
	public void setFlCode(String flCode) {
		this.flCode = flCode;
	}
	public String getBusinessObjectId() {
		return businessObjectId;
	}
	public void setBusinessObjectId(String businessObjectId) {
		this.businessObjectId = businessObjectId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
}
