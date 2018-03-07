package com.xinleju.platform.sys.res.dto;

import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class DataItemDto extends BaseDto{

		
	//作用域编号
	private String ctrlCode;
    
  		
	//作用域名称
	private String ctrlName;
	
	//控制对象编号
	private String itemCode;
	
	
	//控制对象名称
	private String itemName;
    
	
	//系统id
	private String appId;
  		
	//数据控制对象id
	private String ctrlId;
    
  		
	//图标
	private String icon;
    
  		
	//类型
	private String type;
    
  		
	//排序
	private Long sort;
	
	//备注
	private String remark;
    
  	
	//数据项控制点
	private List<DataPointDto> list;
		
	
	public String getCtrlCode() {
		return ctrlCode;
	}
	public void setCtrlCode(String ctrlCode) {
		this.ctrlCode = ctrlCode;
	}
	public String getCtrlName() {
		return ctrlName;
	}
	public void setCtrlName(String ctrlName) {
		this.ctrlName = ctrlName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCtrlId() {
		return ctrlId;
	}
	public void setCtrlId(String ctrlId) {
		this.ctrlId = ctrlId;
	}
    
  		
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public List<DataPointDto> getList() {
		return list;
	}
	public void setList(List<DataPointDto> list) {
		this.list = list;
	}
    
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
