package com.xinleju.platform.sys.org.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.org.dto.RoleUserDto;
import com.xinleju.platform.sys.org.entity.RoleUser;

/**
 * @author admin
 *
 */

public interface RoleUserDao extends BaseDao<String, RoleUser> {

	/**
	 * 根据roleid删除
	 * @param parentId
	 * @return
	 */
	public Integer deleteByRoleId(Map<String, Object> paramater)  throws Exception;
	/**
	 * 批量保存
	 * @param parentId
	 * @return
	 */
	public Integer insertRoleUserBatch(Map<String, Object> paramater)  throws Exception;

	/**
	 * 获取通用角色引用对象列表
	 * @return
	 */
	public List<RoleUserDto> queryRoleRefListByRoleId(Map<String, Object> paramater)  throws Exception;
	/**
	 * 获取通用角色引用对象列表：岗位
	 * @return
	 */
	public List<RoleUserDto> queryRoleRefPostListByRoleId(Map<String, Object> paramater)  throws Exception;
	/**
	 * 获取通用角色引用对象列表：人员
	 * @return
	 */
	public List<RoleUserDto> queryRoleRefUserListByRoleId(Map<String, Object> paramater)  throws Exception;
	/**
	 * 查询用户岗位组织树
	 * @return
	 */
	public List<Map<String, Object>> selectUserPostTree(Map<String, Object> paramater)  throws Exception;
	//查询组织树
	public List<Map<String, Object>> selectTreeOrg(Map<String, Object> paramater)  throws Exception;
	//查询岗位（挂在组织下的）
	public List<Map<String, Object>> selectTreePost(Map<String, Object> paramater)  throws Exception;
	//查询用户（挂在岗位下的）
	public List<Map<String, Object>> selectTreePostUser(Map<String, Object> paramater)  throws Exception;
	//查询用户（挂在组织下的）
	public List<Map<String, Object>> selectTreeOrgUser(Map<String, Object> paramater)  throws Exception;
	//查询用户（挂在组织下和岗位组织下）
	public List<Map<String, Object>> selectPostOrgAndBelongOrgUser(Map<String, Object> paramater)  throws Exception;
	/**
	 * 查询用户组织树
	 * @return
	 */
	public List<Map<String, Object>> selectUserOrgTree(Map<String, Object> paramater)  throws Exception;

	//查询目录下级是被引用
	public Integer selectSonRefCount(Map map)throws Exception;
}
