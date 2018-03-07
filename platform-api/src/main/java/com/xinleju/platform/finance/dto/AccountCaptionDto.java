package com.xinleju.platform.finance.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class AccountCaptionDto extends BaseDto{

		
	//科目编码
	private String code;
    
  		
	//科目名称
	private String name;
    
  		
	//辅助核算ids
	private String assIds;
    
  		
	//辅助核算名称
	private String assNames;
    
  		
	//会计科目对照项id及值id
	private String bizItemIds;
	//会计科目对照项id及值id
	private String prefixId;
    
  		
	//会计科目对照名称及值名称
	private String bizItemNames;
    
  		
	//财务系统公司id
	private String accountSetId;
    
  		
	//父级id
	private String parentId;
    
	//上级节点
	private Long level;
	//加载是否完成
	private Boolean loaded;
	
	private Boolean isLeaf;
	
	private Boolean expanded;	
	
	private String sort;
		
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
    
  		
	public String getAssIds() {
		return assIds;
	}
	public void setAssIds(String assIds) {
		this.assIds = assIds;
	}
    
  		
	public String getAssNames() {
		return assNames;
	}
	public void setAssNames(String assNames) {
		this.assNames = assNames;
	}
    
  		
	public String getBizItemIds() {
		return bizItemIds;
	}
	public void setBizItemIds(String bizItemIds) {
		this.bizItemIds = bizItemIds;
	}
    
  		
	public String getBizItemNames() {
		return bizItemNames;
	}
	public void setBizItemNames(String bizItemNames) {
		this.bizItemNames = bizItemNames;
	}
    
  		
	public String getAccountSetId() {
		return accountSetId;
	}
	public void setAccountSetId(String accountSetId) {
		this.accountSetId = accountSetId;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
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
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
    
  		
}
