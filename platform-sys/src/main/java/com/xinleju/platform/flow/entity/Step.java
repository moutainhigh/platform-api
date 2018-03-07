package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_STEP",desc="流程模板环节连线")
public class Step extends BaseEntity{
	
		
	@Column(value="code",desc="编号")
	private String code;
    
  		
	@Column(value="condition_expression",desc="条件表达式")
	private String conditionExpression;


	@Column(value = "condition_translation",desc = "条件表达式翻译(中文对照)")
	private String conditionTranslation;

  		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="fl_id",desc="流程模板Id")
	private String flId;
    
  		
	@Column(value="source_id",desc="源节点")
	private String sourceId;
    
  		
	@Column(value="target_id",desc="目标结点")
	private String targetId;
    
  		
	@Column(value="start_x",desc="开始点x坐标")
	private Long startX;
    
  		
	@Column(value="start_y",desc="开始点y坐标")
	private Long startY;
    
  		
	@Column(value="target_x",desc="目标点x坐标")
	private Long targetX;
    
  		
	@Column(value="target_y",desc="目标点y坐标")
	private Long targetY;
	
	@Column(value="node_id",desc="流程设计图中环节连线ID")
	private String nodeId;
    
  		
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getConditionExpression() {
		return conditionExpression;
	}
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}


	public String getConditionTranslation() {
		return conditionTranslation;
	}
	public void setConditionTranslation(String conditionTranslation) {
		this.conditionTranslation = conditionTranslation;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getFlId() {
		return flId;
	}
	public void setFlId(String flId) {
		this.flId = flId;
	}
    
  		
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
    
  		
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
    
  		
	public Long getStartX() {
		return startX;
	}
	public void setStartX(Long startX) {
		this.startX = startX;
	}
    
  		
	public Long getStartY() {
		return startY;
	}
	public void setStartY(Long startY) {
		this.startY = startY;
	}
    
  		
	public Long getTargetX() {
		return targetX;
	}
	public void setTargetX(Long targetX) {
		this.targetX = targetX;
	}
    
  		
	public Long getTargetY() {
		return targetY;
	}
	public void setTargetY(Long targetY) {
		this.targetY = targetY;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
    
}
