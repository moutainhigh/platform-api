package com.xinleju.platform.sys.res.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_RES_DATA_POINT",desc="数据项控制点")
public class DataPoint extends BaseEntity{
	
		
	@Column(value="code",desc="编号")
	private String code;
    
  		
	@Column(value="name",desc="名称")
	private String name;
  		
	@Column(value="type",desc="类型")
	private String type;
    
  		
	@Column(value="model",desc="模型")
	private String model;
    
  		
	@Column(value="url",desc="连接")
	private String url;
    
  		
	@Column(value="item_id",desc="数据控制对象id")
	private String itemId;
    
  		
	@Column(value="icon",desc="图标")
	private String icon;
    
  		
	@Column(value="sort",desc="排序")
	private Long sort;
    
	@Column(value="bus_obj_id",desc="业务对象ID")
	private String busObjId;
	
	@Column(value="remark",desc="说明")
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
    
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
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
    
  		
	
}
