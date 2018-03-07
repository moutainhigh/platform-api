package com.xinleju.platform.sys.res.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class DataPointDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//名称
	private String name;
  		
	//类型
	private String type;
    
  		
	//模型
	private String model;
    
  		
	//连接
	private String url;
    
  		
	//数据控制对象id
	private String itemId;
    
  		
	//图标
	private String icon;
    
  		
	//排序
	private Long sort;
    

	//业务对象ID
	private String busObjId;
	
	//说明
	private String remark;
	
	
		
	public String getBusObjId() {
		return busObjId;
	}
	public void setBusObjId(String busObjId) {
		this.busObjId = busObjId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		

  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
    
  		
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    

  		
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
    
  		
}
