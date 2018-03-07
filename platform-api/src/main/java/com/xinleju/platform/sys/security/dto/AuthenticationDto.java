package com.xinleju.platform.sys.security.dto;

import java.util.List;

import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.dto.PostDto;
import com.xinleju.platform.sys.org.dto.StandardRoleDto;
import com.xinleju.platform.sys.res.dto.ResourceDto;





/**
 * @author admin
 * 
 *
 */
public class AuthenticationDto{

		
	/**
	 * 当前用户的标准岗位
	 * 
	 */
	private List<StandardRoleDto> standardRoleDtoList;
	
	/**
	 * 当前用户的通用角色
	 * 
	 */
	private List<StandardRoleDto> currencyRoleDtoList;
	
	/**
	 * 当前用户的岗位
	 * 
	 */
	List<PostDto> postDtoList;
	
	/**
	 * 当前用户的菜单
	 * 
	 */
	List<ResourceDto> resourceDtoList;
	
	/**
	 * 当前用户所在组织的类型
	 * 
	 */
	private String organizationType;
	
	/**
	 * 当前用户的一级公司
	 * 
	 */
	private OrgnazationDto topCompanyDto;
	
	/**
	 * 当前用户的一级部门
	 * 
	 */
	private OrgnazationDto topDeptDto;
	
	/**
	 * 当前用户的直属公司
	 * 
	 */
	private OrgnazationDto directCompanyDto;
	
	/**
	 * 当前用户的直属部门
	 * 
	 */
	private OrgnazationDto directDeptDto;
	
	/**
	 * 当前用户的项目
	 * 
	 */
	private OrgnazationDto groupDto;
	
	/**
	 * 当前用户的分期
	 * 
	 */
	private OrgnazationDto branchDto;
	
	
	
	


	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public OrgnazationDto getTopCompanyDto() {
		return topCompanyDto;
	}

	public void setTopCompanyDto(OrgnazationDto topCompanyDto) {
		this.topCompanyDto = topCompanyDto;
	}

	public OrgnazationDto getTopDeptDto() {
		return topDeptDto;
	}

	public void setTopDeptDto(OrgnazationDto topDeptDto) {
		this.topDeptDto = topDeptDto;
	}

	public OrgnazationDto getDirectCompanyDto() {
		return directCompanyDto;
	}

	public void setDirectCompanyDto(OrgnazationDto directCompanyDto) {
		this.directCompanyDto = directCompanyDto;
	}

	public OrgnazationDto getDirectDeptDto() {
		return directDeptDto;
	}

	public void setDirectDeptDto(OrgnazationDto directDeptDto) {
		this.directDeptDto = directDeptDto;
	}

	public OrgnazationDto getGroupDto() {
		return groupDto;
	}

	public void setGroupDto(OrgnazationDto groupDto) {
		this.groupDto = groupDto;
	}

	public OrgnazationDto getBranchDto() {
		return branchDto;
	}

	public void setBranchDto(OrgnazationDto branchDto) {
		this.branchDto = branchDto;
	}

	public List<StandardRoleDto> getStandardRoleDtoList() {
		return standardRoleDtoList;
	}

	public void setStandardRoleDtoList(List<StandardRoleDto> standardRoleDtoList) {
		this.standardRoleDtoList = standardRoleDtoList;
	}

	public List<PostDto> getPostDtoList() {
		return postDtoList;
	}

	public void setPostDtoList(List<PostDto> postDtoList) {
		this.postDtoList = postDtoList;
	}

	public List<ResourceDto> getResourceDtoList() {
		return resourceDtoList;
	}

	public void setResourceDtoList(List<ResourceDto> resourceDtoList) {
		this.resourceDtoList = resourceDtoList;
	}

	public List<StandardRoleDto> getCurrencyRoleDtoList() {
		return currencyRoleDtoList;
	}

	public void setCurrencyRoleDtoList(List<StandardRoleDto> currencyRoleDtoList) {
		this.currencyRoleDtoList = currencyRoleDtoList;
	}

	
  		
}