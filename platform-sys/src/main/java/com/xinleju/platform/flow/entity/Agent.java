package com.xinleju.platform.flow.entity;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_AGENT",desc="流程代理")
public class Agent extends BaseEntity{
	
		
	@Column(value="name",desc="名称", fuzzyQuery=true)
	private String name;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
	@Column(value="proxy_type",desc="代理类型")
	private String proxyType;
    
  		
	@Column(value="authorizer",desc="授权人")
	private String authorizer;
    
  		
	@Column(value="authorizer_id",desc="授权人id")
	private String authorizerId;
    
  		
	@Column(value="authorized",desc="被授权人")
	private String authorized;
    
  		
	@Column(value="authorized_id",desc="被授权人id")
	private String authorizedId;
    
  		
	@Column(value="start_date",desc="授权开始日期")
	private Timestamp startDate;
    
  		
	@Column(value="end_date",desc="授权结束日期")
	private Timestamp endDate;
    
  		
	@Column(value="remark",desc="备注")
	private String remark;
    
	//授权的岗位范围1-全部岗位    2-指定岗位
	@Column(value="post_scope",desc="授权的岗位范围1-全部岗位  2-指定岗位")
	private String postScope;
	//实例的岗位范围1-全部实例     2-指定实例	
	@Column(value="flow_scope",desc="授权的岗位范围1-全部实例     2-指定实例")
	private String flowScope;
		
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
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
  		
}
