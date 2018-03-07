package com.xinleju.platform.sys.base.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class PayTypeDto extends BaseDto{

		
	//付款款项类型名称
	private String name;
    
  		
	//付款款项类型编码
	private String code;
    
  		
	//状态
	private String status;
    
  		
	//父级id
	private String parentId;
    
  		
	//说明
	private String remark;
	
	//排序
	private String sort;
    
  		
	//拼接id
	private String prefixId;
    
	//上级节点
	private Long level;
	//加载是否完成
	private Boolean loaded;
	
	private Boolean isLeaf;
	
	private Boolean expanded;	
	
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
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public Long getLevel() {
		return level;
	}
	public void setLevel(Long level) {
		this.level = level;
	}
	public Boolean getLoaded() {
		return loaded;
	}
	public void setLoaded(Boolean loaded) {
		this.loaded = loaded;
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
    
  		
}
