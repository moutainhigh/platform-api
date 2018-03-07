package com.xinleju.platform.sys.org.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_ORG_ORGNAZATION",desc="组织结构")
public class Orgnazation extends BaseEntity{
	
		
	@Column(value="name",desc="名称")
	private String name;
	
	@Column(value="code",desc="组织机构代码")
	private String code;
    
  		
	@Column(value="full_name",desc="全称")
	private String fullName;
    
  		
	@Column(value="leader_id",desc="领导岗位")
	private String leaderId;
    @Column(value="leader_type",desc="领导类型")
	private String leaderType;

  		
	@Column(value="up_leader_id",desc="上级领导岗位")
	private String upLeaderId;
    
  		
	@Column(value="parent_id",desc="上级")
	private String parentId;
  		
	@Column(value="type",desc="类型")
	private String type;
    
  		
	@Column(value="root_id",desc="目录id")
	private String rootId;
    
  		
	@Column(value="sort",desc="排序")
	private Long sort;
    
  		
	@Column(value="icon",desc="图标")
	private String icon;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
	@Column(value="remark",desc="说明")
	private String remark;
	
	@Column(value="prefix_id",desc="全路径Id")
	private String prefixId;
	
	@Column(value="prefix_name",desc="全路径名称")
	private String prefixName;

	public String getLeaderType() {
		return leaderType;
	}

	public void setLeaderType(String leaderType) {
		this.leaderType = leaderType;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
    
  		
	public String getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
    
  		
	public String getUpLeaderId() {
		return upLeaderId;
	}
	public void setUpLeaderId(String upLeaderId) {
		this.upLeaderId = upLeaderId;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getRootId() {
		return rootId;
	}
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
    
  		
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public String getPrefixName() {
		return prefixName;
	}
	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}
    
  		
	
}
