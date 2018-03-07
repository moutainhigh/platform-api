package com.xinleju.platform.sys.org.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class PostDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//类型
	private String type;
    
  		
	//关联主键
	private String refId;
    
  		
	//角色id
	private String roleId;
    
	//排序
	private Long sort;
    
  		
	//图标
	private String incon;
    
  		
	//状态
	private String status;
    
  		
	//领导岗位
	private String leaderId;
	
	//名称
	private String name;
	//是否主岗
	private String isDefault;
	//岗位 组织全路径
	private String orgPrefixId;
	//岗位 组织全路径
	private String orgPrefixName;
    
  		
		
	public String getOrgPrefixId() {
		return orgPrefixId;
	}
	public void setOrgPrefixId(String orgPrefixId) {
		this.orgPrefixId = orgPrefixId;
	}
	public String getOrgPrefixName() {
		return orgPrefixName;
	}
	public void setOrgPrefixName(String orgPrefixName) {
		this.orgPrefixName = orgPrefixName;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
    
  		
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
    
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
    
  		
	public String getIncon() {
		return incon;
	}
	public void setIncon(String incon) {
		this.incon = incon;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public String getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
}
