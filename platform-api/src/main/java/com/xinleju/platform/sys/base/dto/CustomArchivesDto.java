package com.xinleju.platform.sys.base.dto;

import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class CustomArchivesDto extends BaseDto{

		
	//类型编码
	private String code;
    
  		
	//类型名称
	private String name;
    
  		
	//类型
	private String showType;
    
  		
	//是否预置
	private String isDefault;
	
	//序号
	private Long sort;
	
	//状态
	private String status;
	
	//保存子项
	private List<CustomArchivesDto> customArchivesList;
	
	//添加修改标识
	private String saveOrUpdate;
    
  		
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
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public List<CustomArchivesDto> getCustomArchivesList() {
		return customArchivesList;
	}
	public void setCustomArchivesList(List<CustomArchivesDto> customArchivesList) {
		this.customArchivesList = customArchivesList;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getSaveOrUpdate() {
		return saveOrUpdate;
	}
	public void setSaveOrUpdate(String saveOrUpdate) {
		this.saveOrUpdate = saveOrUpdate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
}
