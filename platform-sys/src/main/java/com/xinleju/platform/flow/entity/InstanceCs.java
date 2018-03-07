package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_INSTANCE_CS",desc="流程环节抄送")
public class InstanceCs extends BaseEntity{
	
		
	@Column(value="parse_type",desc="岗位解析类型: 1:角色,2:岗位,3:人员")
	private String parseType;
    
  		
	@Column(value="post_name",desc="岗位名称")
	private String postName;
    
  		
	@Column(value="post_id",desc="岗位id")
	private String postId;
    
  		
	@Column(value="participant_name",desc="任务参与者")
	private String participantName;
    
  		
	@Column(value="participant_id",desc="任务参与者id")
	private String participantId;
    
  		
	@Column(value="fi_id",desc="实例id")
	private String fiId;
    
  		
	@Column(value="ac_id",desc="环节id")
	private String acId;
    
  		
		
	public String getParseType() {
		return parseType;
	}
	public void setParseType(String parseType) {
		this.parseType = parseType;
	}
    
  		
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
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
    
  		
	public String getFiId() {
		return fiId;
	}
	public void setFiId(String fiId) {
		this.fiId = fiId;
	}
    
  		
	public String getAcId() {
		return acId;
	}
	public void setAcId(String acId) {
		this.acId = acId;
	}
    
  		
	
}
