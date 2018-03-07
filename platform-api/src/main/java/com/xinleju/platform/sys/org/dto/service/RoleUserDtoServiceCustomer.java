package com.xinleju.platform.sys.org.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface RoleUserDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 批量保存roleUser
	 * @param parentId
	 * @return
	 */
	public String saveBatchRoleUser(String userInfo, String paramater);
	/**
	 * 批量保存roleUserPost
	 * @param parentId
	 * @return
	 */
	public String saveBatchRoleUserPost(String userInfo, String paramater);
	/**
	 * 获取通用角色引用对象列表
	 * @return
	 */
	public String queryRoleRefListByRoleId(String userInfo, String paramater);
	/**
	 * 查询用户岗位组织树
	 * @return
	 */
	public String selectUserPostTree(String userInfo, String paramater);
	/**
	 * 查询用户组织树
	 * @return
	 */
	public String selectUserOrgTree(String userInfo, String paramater);

}
