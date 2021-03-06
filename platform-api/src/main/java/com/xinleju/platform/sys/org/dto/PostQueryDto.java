package com.xinleju.platform.sys.org.dto;

/**
 * @author admin
 * 
 *
 */
public class PostQueryDto {

	// 主键
	private String id;

	// 编号
	private String code;

	// 名称
	private String name;
	
	// 类型
	private String type;

	// 排序
	private Long sort;
	
	//角色Id
	private String roleId;
	
	//组织机构Id
	private String refId;
	
	//组织机构名称
	private String refName;
	
	//管辖范围
	private String orgIds;
	
	//管辖范围名称
	private String orgNames;
	
	//组织机构全路径
	private String prefixName;
	
	//角色全路径
	private String rolePrefixName;
	
	//领导岗位
	private String leaderId;
	
	//领导岗位名称
	private String leaderName;
	//领导岗位所属组织
	private String leaderPrefixName;
	
	
	

	public String getLeaderPrefixName() {
		return leaderPrefixName;
	}

	public void setLeaderPrefixName(String leaderPrefixName) {
		this.leaderPrefixName = leaderPrefixName;
	}

	public String getRolePrefixName() {
		return rolePrefixName;
	}

	public void setRolePrefixName(String rolePrefixName) {
		this.rolePrefixName = rolePrefixName;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	public String getOrgNames() {
		return orgNames;
	}

	public void setOrgNames(String orgNames) {
		this.orgNames = orgNames;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public String getPrefixName() {
		return prefixName;
	}

	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}
	
}
