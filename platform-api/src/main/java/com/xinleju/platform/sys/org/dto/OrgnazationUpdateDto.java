package com.xinleju.platform.sys.org.dto;


public class OrgnazationUpdateDto {
	
	 //主键
	  private String type ;
	  //名称
	  private OrgnazationTreeNodeDto target;
	  //类型
	  private OrgnazationTreeNodeDto source;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public OrgnazationTreeNodeDto getTarget() {
		return target;
	}
	public void setTarget(OrgnazationTreeNodeDto target) {
		this.target = target;
	}
	public OrgnazationTreeNodeDto getSource() {
		return source;
	}
	public void setSource(OrgnazationTreeNodeDto source) {
		this.source = source;
	}
	  
	  
}
