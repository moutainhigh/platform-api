package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class StepDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//条件表达式
	private String conditionExpression;

	//条件表达式翻译(中文对照)
	private String conditionTranslation;
  		
	//名称
	private String name;
    
  		
	//流程模板Id
	private String flId;
    
  		
	//源节点
	private String sourceId;
    
  		
	//目标结点
	private String targetId;
    
  		
	//开始点x坐标
	private Long startX;
    
  		
	//开始点y坐标
	private Long startY;
    
  		
	//目标点x坐标
	private Long targetX;
    
  		
	//目标点y坐标
	private Long targetY;
	
	//流程设计图中环节连线ID
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
