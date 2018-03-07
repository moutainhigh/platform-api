package com.xinleju.platform.sys.base.dto;

import java.sql.Timestamp;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class BaseProjectTypeDto extends BaseDto{

		
	//名称
	private String name;
    
  		
	//编号
	private String code;
    
  		
	//上级
	private String parentId;
    
  		
	//状态
	private String status;
    
  		
	//备注
	private String remark;
    
  	//排序号
	private String sort;
	
	//上级节点
	private Long level;
	//加载是否完成
	private Boolean loaded;
	
	private Boolean isLeaf;
	
	private Boolean expanded;

	private String prefixId;
		
	//禁用人
	private String disabledId;
  		
	//禁用日期"
	private Timestamp disabledDate;	
	
	private Boolean isrepat;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
/*	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}*/
	public String getDisabledId() {
		return disabledId;
	}
	public void setDisabledId(String disabledId) {
		this.disabledId = disabledId;
	}
	public Timestamp getDisabledDate() {
		return disabledDate;
	}
	public void setDisabledDate(Timestamp disabledDate) {
		this.disabledDate = disabledDate;
	}
	public Long getLevel() {
		return level;
	}
	public void setLevel(Long level) {
		this.level = level;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	public Boolean getLoaded() {
		return loaded;
	}
	public void setLoaded(Boolean loaded) {
		this.loaded = loaded;
	}

	public Boolean getIsrepat() {
		return isrepat;
	}
	public void setIsrepat(Boolean isrepat) {
		this.isrepat = true;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}

    
  		
}
