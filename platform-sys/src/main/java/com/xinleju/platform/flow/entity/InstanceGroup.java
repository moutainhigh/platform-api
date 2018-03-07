package com.xinleju.platform.flow.entity;

import java.sql.Timestamp;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_INSTANCE_GROUP",desc="流程工作组")
public class InstanceGroup extends BaseEntity{
	
		
	@Column(value="ac_id",desc="流程环节id")
	private String acId;
	
	@Column(value="ac_post_id",desc="对应岗位表ID")
	private String acPostId;
    
  		
	@Column(value="activate_date",desc="岗位激活时间")
	private Timestamp activateDate;
    
  		
	@Column(value="end_date",desc="岗位完成时间")
	private Timestamp endDate;
    
  		
	@Column(value="status",desc="状态: 1:未运行,2:运行中 ,3:完成")
	private String status;
    
  		
	@Column(value="disable",desc="是否有效")
	private Boolean disable;
    
  		
	@Column(value="disable_type",desc="无效方式: 1:人工删除完成,2:被动完成")
	private String disableType;
    
  		
	@Column(value="post_name",desc="岗位名称")
	private String postName;
    
  		
	@Column(value="parse_type",desc="岗位解析类型: 1:角色,2:岗位,3:人员")
	private String parseType;
    
  		
	@Column(value="post_id",desc="岗位id")
	private String postId;
    
  		
	@Column(value="participant_name",desc="任务参与者")
	private String participantName;
    
  		
	@Column(value="participant_id",desc="任务参与者id")
	private String participantId;
    
  		
	@Column(value="px",desc="排序")
	private Long px;
    
  		
	@Column(value="source",desc="来源: 1:模板,2:加签")
	private String source;
    
  		
	@Column(value="source_id",desc="来源id")
	private String sourceId;
    
	//审批人是否自动通过（流程启动时检查相同审批人并设置）
	//1:自动跳过 0：流程状态（参与流程中的抢占跳过）-1：强制不跳过
	@Column(value="auto_pass",desc="是否自动通过")
  	private Integer autoPass = 0;
	
	@Column(value="proxy",desc="代理人所在Group的ID")
	private String proxy;
	
	@Column(value="proxyed",desc="被代理人所在Group的ID")
	private String proxyed;
	
	@Column(value="proxy_type",desc="代理类型")
	private String proxyType;
		
	public String getAcId() {
		return acId;
	}
	public void setAcId(String acId) {
		this.acId = acId;
	}
    
  		
	public Timestamp getActivateDate() {
		return activateDate;
	}
	public void setActivateDate(Timestamp activateDate) {
		this.activateDate = activateDate;
	}
    
  		
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public Boolean getDisable() {
		return disable;
	}
	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
    
  		
	public String getDisableType() {
		return disableType;
	}
	public void setDisableType(String disableType) {
		this.disableType = disableType;
	}
    
  		
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
    
  		
	public String getParseType() {
		return parseType;
	}
	public void setParseType(String parseType) {
		this.parseType = parseType;
	}
    
  		
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
    
  		
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
    
  		
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
    
  		
	public Long getPx() {
		return px;
	}
	public void setPx(Long px) {
		this.px = px;
	}
    
  		
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
    
  		
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getAcPostId() {
		return acPostId;
	}
	public void setAcPostId(String acPostId) {
		this.acPostId = acPostId;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public String getProxyed() {
		return proxyed;
	}
	public void setProxyed(String proxyed) {
		this.proxyed = proxyed;
	}
	public String getProxyType() {
		return proxyType;
	}
	public void setProxyType(String proxyType) {
		this.proxyType = proxyType;
	}
	public Integer getAutoPass() {
		return autoPass;
	}
	public void setAutoPass(Integer autoPass) {
		this.autoPass = autoPass;
	}
}
