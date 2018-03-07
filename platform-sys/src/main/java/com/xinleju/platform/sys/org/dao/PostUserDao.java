package com.xinleju.platform.sys.org.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.org.entity.PostUser;
import com.xinleju.platform.sys.org.vo.CommonRolePostUserVo;
import com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo;

/**
 * @author admin
 *
 */

public interface PostUserDao extends BaseDao<String, PostUser> {
	
	/**
	 * 批量保存post_user和role_user
	 * @param objectList
	 */
	public Integer savePostUserAndRoleUser(List<Map<String,Object>> list)throws Exception;
	/**
	 * 移除岗位角色
	 * @param userInfo
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Integer delPostUserAndRoleUser(String userId) throws Exception;
	/**
	 * 设置主岗
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer setDefaultPost(Map<String,Object> map) throws Exception; 
	
	
	
   /**
    *根据分期id获取对应的项项目Id
	 * @param branchId
	 * @return
	 * @throws Exception
	 */
	public String  getProjectIdByProjectBranchId(String branchId) throws Exception;
	
	

	/**
	 * 获取流程公司级岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowCompanyOrgnazationPostUserVos(Map<String,Object> map);
	/**
	 * 获取流程公司级岗位--下级公司下的
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowCompanyOrgnazationPostUserVos_2(Map<String,Object> map);
	
	/**
	 * 查询上级公司id 
	 * @param map
	 * @return
	 */
	public List<String> getUpCompanyIds(Map<String,Object> map);
	
	
	
	/**
	 * 
	 * 公司上找在进行下找
	 * 获取流程公司级岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowCompanyUpOrgnazationPostUserVos(Map<String,Object> map);

	/**
	 * 获取流程公司级岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowJTOrgnazationPostUserVos(Map<String,Object> map);
	
	/**
	 * 获取流程公司级岗位(岗位下没有人)
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowJTOrgnazationPostVos(Map<String,Object> map);
	/**
	 * 获取流程项目项目岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowPjectOrgnazationPostUserVos(Map<String,Object> map);
	
	/**
	 * 获取流程部门岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowDeptOrgnazationPostUserVos(Map<String,Object> map);
	/**
	 * 经办部门上级级部门岗位--取最靠近经办部门的部门岗
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowOrgnazationPostUserVos_up(Map<String,Object> map);
	
	
	/**
	 * 获取直接组织岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowDirectOrgnazationPostUserVos(Map<String,Object> map);
	
	/**
	 * 获取符合的通用角色
	 * @param map
	 * @return
	 */
	public List<CommonRolePostUserVo> getFlowCommonRolePostUserVos(Map<String,Object> map);
	
	
	/**
	 * 获取符合岗位人员
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowCommonRolePostUser_PostVos(Map<String,Object> map);
	
	
	/**
	 * 获取符合的人员岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowCommonRolePostUser_UserVos(Map<String,Object> map);
	
	/**
	 * 
	 * 获取默认用户岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowUserDefualtOrgnazationPostUserVos(Map<String,Object> map);


	/**
	 *
	 * 获取默认用户岗位By username
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowUserDefualtOrgnazationPostUserVosByUsername(Map<String,Object> map);
	
	/**
	 * 
	 * 获取用户岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowUserOrgnazationPostUserVos(Map<String,Object> map);
	
	/**
	 * 
	 * 获取默认用户岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowUserDefualtOrgnazationUserVos(Map<String,Object> map);
	
	
	/**
	 * 
	 * 获取岗位用户
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowPostUserOrgnazationPostUserVos(Map<String,Object> map);
	
	

	/**
	 * 
	 * 获取发起用户领导岗位
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowStartUserOrgnazationPostUserVos(Map<String,Object> map);
	
	/**
	 * 
	 * 获取发起人上级部门领导 
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getFlowStartUserDeptOrgnazationPostUserVos(Map<String,Object> map);
	public List<OrgnazationPostUserVo> getleaderPost(Map<String,Object> map);
	
	
	/**
	 * 
	 * 获取发起人一级部门领导 
	 * @param map
	 * @return
	 */
	public String getFlowStartUserDeptOrgnazationLeaderId(Map<String,Object> map);
	//是否存在主岗
	public Integer selectDefault(Map<String,Object> map);
	
	public List<OrgnazationPostUserVo> getLeaderPostOnOrgWhenLeaderOnPostIsNull(Map<String, Object> paramMap);
	
	/**
	 * 经办部门及其下级的用户岗的领导岗和经办部门及其下级的领导岗
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getDeptDownUserPostLeaderAndDeptDownLeader(Map<String,Object> map);
	/**
	 * 经办部门及其下级的用户岗的领导岗和经办部门及其下级的领导岗
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> getDeftPostLeaderOrDeftPostOrgLeaderOrUserOrgLeader(Map<String,Object> map);
	/**
	 * 指定人员改为从组织人员树选择，不含岗位，目前将用户组织id当作post返回
	 * @param map
	 * @return
	 */
	public List<OrgnazationPostUserVo> selectOrgUserById(Map<String,Object> map);
	/**
	 *  查找直属公司id
	 * @param map
	 * @return
	 */
	public String selectDriectComId(Map<String,Object> map);
	/**
	 * 在直属公司下的所有部门中查找（不包括子公司）
	 * @param map
	 * @return
	 */
	public OrgnazationPostUserVo selectDownDeptUserPost(Map<String,Object> map);
	/**
	 * 查询用户主岗
	 * @param map
	 * @return
	 */
	public OrgnazationPostUserVo selectUserMainPost(Map<String,Object> map);	/**
	 * 计算经办部门下的岗位，即当前经办部门下是否有岗位，如果没有，再找上级部门
	 * @param map
	 * @return
	 */
	public OrgnazationPostUserVo selectUpDeptUserPost(Map<String,Object> map);
}
