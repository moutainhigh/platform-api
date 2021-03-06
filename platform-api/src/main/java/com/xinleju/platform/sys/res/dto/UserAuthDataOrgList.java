package com.xinleju.platform.sys.res.dto;

import com.xinleju.platform.sys.org.dto.OrgnazationDto;

import java.util.ArrayList;
import java.util.List;

public class UserAuthDataOrgList {
	//授权的公司列表
	private List<OrgnazationDto> companyList;
	//授权的部门列表
	private List<OrgnazationDto> deptList;
	//授权的项目列表
	private List<OrgnazationDto> groupList;
	//授权的分期列表
	private List<OrgnazationDto> branchList;

	public UserAuthDataOrgList() {
		companyList=new ArrayList<OrgnazationDto>();
		deptList=new ArrayList<OrgnazationDto>();
		groupList=new ArrayList<OrgnazationDto>();
		branchList=new ArrayList<OrgnazationDto>();
	}

	public List<OrgnazationDto> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<OrgnazationDto> groupList) {
		this.groupList = groupList;
	}

	public List<OrgnazationDto> getBranchList() {
		return branchList;
	}

	public void setBranchList(List<OrgnazationDto> branchList) {
		this.branchList = branchList;
	}

	public List<OrgnazationDto> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(List<OrgnazationDto> companyList) {
		this.companyList = companyList;
	}
	public List<OrgnazationDto> getDeptList() {
		return deptList;
	}
	public void setDeptList(List<OrgnazationDto> deptList) {
		this.deptList = deptList;
	}


}
