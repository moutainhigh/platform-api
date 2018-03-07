package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_PARTICIPANT",desc="流程参与者")
public class Participant extends BaseEntity{
	
		
	@Column(value="type",desc="环节参与者类型: 1:审批,2:抄送,3:模板可阅")
	private String type;
    
  		
	@Column(value="fl_id",desc="流程模板Id")
	private String flId;
    
  		
	@Column(value="ac_id",desc="环节id")
	private String acId;
    
  		
	@Column(value="participant_id",desc="组织机构id")
	private String participantId;

	@Column(value="participant_name",desc="组织机构名称")
	private String participantName;

  		
	@Column(value="participant_type",desc="组织机构类型: 1:人员,2: 岗位,3:角色,4:相对参与人")
	private String participantType;
    
  		
	@Column(value="participant_scope",desc="角色参与者计算范围: 11:指定人员，12:表单人员 ;21:指定岗位，22:表单岗位 ;31:指定角色（逻辑表示），311:集团，312：本公司，313：本部部门，314：本项目，315:本分期 ，316:指定机构  ，317:表单机构41：发起人直接领导，42：发起人顶级部门领导，43：上一环节审批人直接领导，44：上一环节审批人顶级部门领导")
	private String participantScope;
    
	@Column(value="param_value",desc="备用参数值: 只在participant_scope为316或317时指定组织或表单组织时使用")
	private String paramValue;//只在participant_scope为316或317时指定组织或表单组织时使用
	
	@Column(value="sort",desc="序号")
	private Long sort;
    
  		
		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getFlId() {
		return flId;
	}
	public void setFlId(String flId) {
		this.flId = flId;
	}
    
  		
	public String getAcId() {
		return acId;
	}
	public void setAcId(String acId) {
		this.acId = acId;
	}
    
  		
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
    
  		
	public String getParticipantType() {
		return participantType;
	}
	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}
    
  		
	public String getParticipantScope() {
		return participantScope;
	}
	public void setParticipantScope(String participantScope) {
		this.participantScope = participantScope;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
}
