package com.xinleju.platform.out.app.old.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.xinleju.erp.flow.flowutils.bean.DebugInfo;
import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.service.api.extend.OrgnService;
import com.xinleju.erp.flow.service.api.extend.dto.CompanyDTO;
import com.xinleju.erp.flow.service.api.extend.dto.DeptDTO;
import com.xinleju.erp.flow.service.api.extend.dto.OrgnDTO;
import com.xinleju.erp.flow.service.api.extend.dto.PartyEntityDTO;
import com.xinleju.erp.flow.service.api.extend.dto.PostDTO;
import com.xinleju.erp.flow.service.api.extend.dto.RoleDTO;
import com.xinleju.erp.flow.service.api.extend.dto.ScopeDTO;

public class OrgnServiceImpl implements OrgnService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public FlowResult<List<OrgnDTO>> getOrgnListAll() {
		/*String sql="SELECT o.id,o.id refId,o.`name`,if(IFNULL(o.parent_id,'')='',0,o.parent_id) parentId,o.type,o.type nodetype,o.`code`,o.`status`,FALSE isLeaf from pt_sys_org_orgnazation o"
				+" WHERE o.delflag = 0 and o.`status`=1 and EXISTS (SELECT 1 from pt_sys_org_orgnazation t where t.parent_id=o.id)"
				+" UNION "
				+" SELECT o.id,o.id refId,o.`name`,if(IFNULL(o.parent_id,'')='',0,o.parent_id) parentId,o.type,o.type nodetype,o.`code`,o.`status`,TRUE isLeaf from pt_sys_org_orgnazation o"
				+" WHERE o.delflag = 0 and o.`status`=1 and NOT EXISTS (SELECT 1 from pt_sys_org_orgnazation t where t.parent_id=o.id)";*/
		String sql1="SELECT t.id,t.`name`,if(IFNULL(t.parent_id,'')='',0,t.parent_id) parentId,t.id prefixId,'cata' type,'cata' nodetype,'' postId,1 sort  from pt_sys_org_root t"
				+" UNION"
				+" SELECT o.id,o.`name`,if(IFNULL(o.parent_id ,'')='',o.root_id,o.parent_id) parentId,o.prefix_id prefixId,o.type,o.type nodeType,'' postId,o.sort from pt_sys_org_orgnazation o"  
				+" WHERE o.delflag=0 and o.`status`=1  "
				+" 	and not EXISTS (SELECT 1 from pt_sys_org_orgnazation p WHERE (p.delflag=1 or p.`status`=0) and o.prefix_id LIKE CONCAT('',p.prefix_id,'%'))"
				+ " order by  case when sort is null then 1 else 0 end,  sort ,LENGTH(prefixId)";
		String sql2=" SELECT p.id,r.`name`,p.ref_id parentId,CONCAT(o.prefix_id,'/',p.id) prefixId,'post' type,'post' nodetype,p.id postId,null sort  from pt_sys_org_post p  "
				+" 	LEFT JOIN pt_sys_org_orgnazation o on p.ref_id=o.id"
				+" 	LEFT JOIN pt_sys_org_standard_role r on r.id=p.role_id"
				+" 	WHERE  r.delflag=0 and r.`status`=1 and o.delflag=0 and o.`status`=1 and p.delflag=0"
				+" 	and not EXISTS (SELECT 1 from pt_sys_org_orgnazation p WHERE (p.delflag=1 or p.`status`=0) and o.prefix_id LIKE CONCAT('',p.prefix_id,'%'))";
		String sql3=" SELECT u.id,u.real_name name,p.id parentId,CONCAT(o.prefix_id,'/',p.id,'/',u.id) prefixId,'user' type,'user' nodetype,p.id postId ,u.sort from pt_sys_org_post_user pu " 
				+" 	LEFT JOIN pt_sys_org_post p on pu.post_id=p.id"
				+" 	LEFT JOIN pt_sys_org_orgnazation o on p.ref_id=o.id"
				+" 	LEFT JOIN pt_sys_org_user u on pu.user_id=u.id"
				+" 	LEFT JOIN pt_sys_org_standard_role r on r.id=p.role_id"
				+" 	WHERE  r.delflag=0 and r.`status`=1 and u.delflag=0 and u.`status`=1 and o.delflag=0 and o.`status`=1 and p.delflag=0"
				+" 	and not EXISTS (SELECT 1 from pt_sys_org_orgnazation p WHERE (p.delflag=1 or p.`status`=0) and o.prefix_id LIKE CONCAT('',p.prefix_id,'%'))";
		RowMapper<OrgnDTO> rowMapper=new BeanPropertyRowMapper<OrgnDTO>(OrgnDTO.class);
		List<OrgnDTO> list=new ArrayList<>();
		List<OrgnDTO> list1= jdbcTemplate.query(sql1,rowMapper);
		List<OrgnDTO> list2= jdbcTemplate.query(sql2,rowMapper);
		List<OrgnDTO> list3= jdbcTemplate.query(sql3,rowMapper);
		list.addAll(list1);
		list.addAll(list2);
		list.addAll(list3);
		FlowResult<List<OrgnDTO>> result=new FlowResult<>();
		result.setResult(list);
		return result;
	}

	@Override
	public FlowResult<OrgnDTO> getOrgnById(Long id) {

		return null;
	}

	@Override
	public FlowResult<OrgnDTO> getParentOrgnByChildId(Long childId) {

		return null;
	}
	/**
	 * 查询组织机构根节点信息
	 * @return
	 */
	@Override
	public FlowResult<OrgnDTO> getOrgnRoot() {
		String sql="SELECT o.id,o.id refId,o.`name` from pt_sys_org_root o WHERE o.delflag=0 and o.`status`=1";
		RowMapper<OrgnDTO> rowMapper=new BeanPropertyRowMapper<OrgnDTO>(OrgnDTO.class);
		List<OrgnDTO> list= jdbcTemplate.query(sql,rowMapper);
		FlowResult<OrgnDTO> result=new FlowResult<OrgnDTO>();
		if (list!=null&&list.size()>0) {
			result.setResult(list.get(0));
		}else {
			result.setResult(null);
		}
		return result;
	}
	/**
	 * 获取孩子组织机构列表
	 * 
	 * @param parentId
	 *            父ID
	 * @param includeTypes
	 *           包含节点类型（company:公司  department:部门  group:团队 ）
	 * @return
	 */
	@Override
	public FlowResult<List<OrgnDTO>> getOrgnListSub(String parentId, String[] includeTypes) {
		FlowResult<List<OrgnDTO>> result=new FlowResult<List<OrgnDTO>>();
		try {
			String sql="SELECT o.id,o.id refId,o.`name`,o.parent_id parentId,o.type,o.`code`,o.`status`,COUNT(s.id) subCount from pt_sys_org_orgnazation o "
						 +" LEFT JOIN pt_sys_org_orgnazation t on o.parent_id=t.id"
						 +" LEFT JOIN pt_sys_org_orgnazation s on s.parent_id=o.id and s.delflag = 0 and s.`status`=1 "
						 +"  WHERE o.delflag = 0 and o.`status`=1 and o.type in ('%s')  "
						 +"  and t.id=?"
						 +" GROUP BY o.id "
						 +"  union "
						 +" SELECT o.id,o.id refId,o.`name`,o.parent_id parentId,o.type,o.`code`,o.`status`,COUNT(s.id) subCount from pt_sys_org_orgnazation o  "
						 +" LEFT JOIN pt_sys_org_orgnazation s on s.parent_id=o.id and s.delflag = 0 and s.`status`=1 "
						 +"  WHERE o.delflag = 0 and o.`status`=1 and o.type in ('%s') "
						 +"  and o.root_id=? and IFNULL(o.parent_id ,'')=''"
						 +" GROUP BY o.id ";
			if (includeTypes!=null&&includeTypes.length>0) {
				String type=StringUtils.join(includeTypes,"','");
				type=type.replaceAll("department", "dept");
				type=type.replaceAll("company", "company','zb");
				sql=String.format(sql, type,type);
				RowMapper<OrgnDTO> rowMapper=new BeanPropertyRowMapper<OrgnDTO>(OrgnDTO.class);
				List<OrgnDTO> list= jdbcTemplate.query(sql,rowMapper,parentId,parentId);
				result.setResult(list);
			}else {
				result.setResult(null);
				result.setSuccess(false);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListAll() {
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId from pt_sys_org_orgnazation o  "
				+" where o.delflag=0  and o.`status`=1 and o.type in ('company','zb')";
		RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
		List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper);
		FlowResult<List<CompanyDTO>> result=new FlowResult<List<CompanyDTO>>();
		result.setResult(list);
		return result;
	}
	/**
	 * 根据ID查询公司
	 * @param companyId
	 * @return
	 */
	@Override
	public FlowResult<CompanyDTO> getCompanyById(String companyId) {
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId from pt_sys_org_orgnazation o  "
				+" where o.delflag=0 and o.`status`=1 and o.id =? and o.type in ('zb','company')";
		FlowResult<CompanyDTO> result=new FlowResult<>();
		RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
		List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper,companyId);
		if (list!=null&&list.size()>0) {
			result.setResult(list.get(0));
		}else {
			result.setResult(null);
		}
		return result;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByIds(String[] companyIds) {
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId from pt_sys_org_orgnazation o  "
				+" where o.delflag=0 and o.`status`=1 and o.type in ('zb','company') and o.id in ";
		FlowResult<List<CompanyDTO>> result=new FlowResult<>();
		if(companyIds!=null&&companyIds.length>0){
			String ids=StringUtils.join(companyIds,"','");
			sql=sql+"('"+ids+"')";
			RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
			List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper);
			result.setResult(list);
		}else {
			result.setResult(null);
			result.setSuccess(false);
		}
		return result;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListLeaf() {

		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListHaveProject() {

		return null;
	}

	@Override
	public FlowResult<CompanyDTO> getCompanyByDeptId(String deptId) {
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId FROM pt_sys_org_orgnazation t,pt_sys_org_orgnazation o"
					+" WHERE t.id=? and t.parent_id=o.id and o.type in  ('zb','company')";
		FlowResult<CompanyDTO> result=new FlowResult<>();
		RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
		List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper,deptId);
		if (list!=null&&list.size()>0) {
			result.setResult(list.get(0));
		}else {
			result.setResult(null);
		}
		return result;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByDeptIds(String[] deptIds) {

		return null;
	}
	/**
	 * 获取用户公司
	 * @param userId
	 * @return
	 */
	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByUserId(String userId) {
		FlowResult<List<CompanyDTO>> result=new FlowResult<List<CompanyDTO>>();
		List<CompanyDTO> listCompany=new ArrayList<CompanyDTO>();
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId from pt_sys_org_orgnazation o "
				+" LEFT JOIN pt_sys_org_user u on u.belong_org_id =o.id "
				+" where u.id=? and o.delflag=0 and o.`status`=1 and o.type in ('zb','company')"
				+" UNION"
				+" SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId from pt_sys_org_orgnazation o"
				+" LEFT JOIN pt_sys_org_post p on p.ref_id =o.id " 
				+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
				+" where o.delflag=0 and p.delflag=0 and o.`status`=1 and pu.user_id=? and o.type in ('zb','company')";
		RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
		List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper,userId,userId);
		if(list.isEmpty()||list.size()==0){
			String sqlDept="SELECT o.id,o.`code`,o.`name`,o.`status`,o.prefix_id parent_id from pt_sys_org_orgnazation o "
					+" LEFT JOIN pt_sys_org_user u on u.belong_org_id =o.id "
					+" where u.id=? and o.delflag=0 and o.`status`=1 and o.type in ('dept','company')"
					+" UNION"
					+" SELECT o.id,o.`code`,o.`name`,o.`status`,o.prefix_id parent_id from pt_sys_org_orgnazation o"
					+" LEFT JOIN pt_sys_org_post p on p.ref_id =o.id " 
					+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
					+" where o.delflag=0 and p.delflag=0 and o.`status`=1 and pu.user_id=? and o.type in ('dept','company')";
			List<CompanyDTO> listDept= jdbcTemplate.query(sqlDept,rowMapper,userId,userId);
		    if(!listDept.isEmpty()&&listDept.size()>0){
		    	for (CompanyDTO companyDTO : listDept) {
		    		List<CompanyDTO> listCompanyDTOs=new ArrayList<CompanyDTO>();
					String ss="'"+companyDTO.getParentId().replaceAll("/", "','")+"'".replaceAll("\\s*", "");
					String sqlString="SELECT o.id, o.`code`, o.`name`, o.`status`, o.parent_id parentId, o.prefix_id, o.prefix_name, o.type "
							+ "FROM pt_sys_org_orgnazation o where id in("+ss+")";
					List<Map<String, Object>> orgIdList= jdbcTemplate.queryForList(sqlString);
					for (Map<String, Object> map : orgIdList) {
						CompanyDTO  company=new CompanyDTO();
						if(listCompanyDTOs.size()>0&&(map.get("type").equals("zb")||map.get("type").equals("company"))){
							listCompanyDTOs.clear();
						}
						if(map.get("type").equals("zb")||map.get("type").equals("company")){
							company.setId(map.get("id").toString());
							company.setCode(map.get("code").toString());
							company.setName(map.get("name").toString());
							company.setStatus(Integer.parseInt(map.get("status").toString()));
							company.setParentId(map.get("parentId").toString());
							listCompanyDTOs.add(company);
						}else {
							continue;
						}
					}
				    listCompany.add(listCompanyDTOs.get(0));
				}
		    	
		    }
		}else {
			listCompany=list;
		}
		//去除重复数据
		List<CompanyDTO> companyDTOs=removeRepeatById(listCompany);
		result.setResult(companyDTOs);
		return result;
	}
	/**
	 * 去重复对象
	 * @param companys
	 * @return
	 */
	public List<CompanyDTO> removeRepeatById(List<CompanyDTO> companys) {
		Set<CompanyDTO> companySet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
		companySet.addAll(companys);
		return new ArrayList<>(companySet);
	}
	/**
	 * 获取用户主公司
	 * @param userId
	 * @return
	 */
	@Override
	public FlowResult<CompanyDTO> getCompanyMainByUserId(String userId) {
		String sql="SELECT o.id,o.`code`,o.`name`,o.`status` from pt_sys_org_post_user pu "
				+" LEFT JOIN pt_sys_org_post p on p.id=pu.post_id"
				+" LEFT JOIN pt_sys_org_orgnazation o on o.id=p.ref_id"
				+" where pu.user_id=? and o.delflag=0 and o.`status`=1 and p.delflag=0   and pu.is_default=1 ";
		RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
		List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper,userId);
		FlowResult<CompanyDTO> result=new FlowResult<CompanyDTO>();
		if(list!=null&&list.size()>0){
			String upOrgSql="SELECT top.id,top.`code`,top.`name`,top.`status`"
					+" from pt_sys_org_orgnazation t,pt_sys_org_orgnazation top "
					+" where  t.id =? and top.type in ('zb','company') "
					+" and t.prefix_id like concat(top.prefix_id,'%%') "
					+" ORDER BY LENGTH(top.prefix_id)";
			//组织结构
			List<CompanyDTO> mainCom=jdbcTemplate.query(upOrgSql,rowMapper,list.get(0).getId());
			if (mainCom!=null&&mainCom.size()>0) {
				result.setResult(mainCom.get(mainCom.size()-1));
			}
		}else {
			result.setResult(null);
		}
		return result;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByUserIds(String[] userIds) {

		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByUserLoginName(String userLoginName) {

		return null;
	}

	@Override
	public FlowResult<CompanyDTO> getCompanyMainByUserLoginName(String userLoginName) {
		FlowResult<CompanyDTO> result=new FlowResult<>();
		DebugInfo info=new DebugInfo();
		//判断用户id
		String userSql="SELECT u.id from pt_sys_org_user u WHERE u.login_name=? and u.delflag=0 and u.`status`=1";
		String uId= jdbcTemplate.queryForObject(userSql,String.class,userLoginName);
		if (uId==null||StringUtils.isBlank(uId)) {
			result.setSuccess(false);
			info.setCompleteAt("该用户不存在或被禁用");
			result.setDebugInfo(info);
			return result;
		}
		result=getCompanyMainByUserId(uId);
		return result;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByUserLoginNames(String[] userLoginNames) {

		return null;
	}

	/**
	 * 根据公司ID获取公司下部门
	 * @param companyId
	 * @return
	 */
	@Override
	public FlowResult<List<DeptDTO>> getDeptListByCompanyId(String companyId) {
		String sql="SELECT o.id,o.`code`,o.`name`,o.`name` shortName,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId "
				+" from pt_sys_org_orgnazation o where o.delflag=0 and o.`status`=1 and o.type='dept' and o.parent_id=? ORDER BY o.sort";
		RowMapper<DeptDTO> rowMapper=new BeanPropertyRowMapper<DeptDTO>(DeptDTO.class);
		List<DeptDTO> list= jdbcTemplate.query(sql,rowMapper,companyId);
		FlowResult<List<DeptDTO>> result=new FlowResult<List<DeptDTO>>();
		result.setResult(list);
		return result;
	}

	/**
	 * 取部门DTO
	 */
	@Override
	public FlowResult<DeptDTO> getDeptById(String deptId) {
		String sql="SELECT o.id,o.`code`,o.`name`,o.`name` shortName,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId from pt_sys_org_orgnazation o"
				+ " where o.delflag=0 and o.`status`=1 and o.type='dept' and o.id=?";
		RowMapper<DeptDTO> rowMapper=new BeanPropertyRowMapper<DeptDTO>(DeptDTO.class);
		List<DeptDTO> list= jdbcTemplate.query(sql,rowMapper,deptId);
		FlowResult<DeptDTO> result=new FlowResult<DeptDTO>();
		if(list!=null&&list.size()>0){
			result.setResult(list.get(0));
		}else {
			result.setResult(null);
		}
		return result;
	}
	/**
	 * 根据部门ID获取部门
	 * @param deptIds
	 * @return
	 */
	@Override
	public FlowResult<List<DeptDTO>> getDeptListByIds(String[] deptIds) {
		FlowResult<List<DeptDTO>> result=new FlowResult<>();
		String sql="SELECT o.id,o.`code`,o.`name`,o.`name` shortName,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId from pt_sys_org_orgnazation o "
				+" where o.type='dept' and id in('%s')";
		if (deptIds!=null&&deptIds.length>0) {
			String ids=StringUtils.join(deptIds, "','");
			sql=String.format(sql, ids);
			RowMapper<DeptDTO> rowMapper=new BeanPropertyRowMapper<DeptDTO>(DeptDTO.class);
			List<DeptDTO> list= jdbcTemplate.query(sql,rowMapper);
			result.setResult(list);
		}else {
			result.setSuccess(false);
			result.setResult(null);
		}
		return result;
	}

	@Override
	public FlowResult<DeptDTO> getDeptByPostId(Long postId) {

		return null;
	}

	@Override
	public FlowResult<List<DeptDTO>> getDeptListByPostIds(String[] postIds) {

		return null;
	}
	/**
	 * 根据用户ID获取用户部门
	 * @param userId
	 * @return
	 */
	@Override
	public FlowResult<List<DeptDTO>> getDeptListByUserId(String userId) {
		FlowResult<List<DeptDTO>> result=new FlowResult<>();
		List<DeptDTO> listDeptDTOs=new ArrayList<DeptDTO>();
		String sql="SELECT o.id,o.`code`,o.`name`,o.`name` shortName,o.leader_id deptRoleId,o.prefix_id upDeptRoleId from pt_sys_org_orgnazation o "
				+" LEFT JOIN pt_sys_org_user u on u.belong_org_id =o.id"
				+" where u.id=? and o.delflag=0 and o.`status`=1 and o.type in ('dept')"
				+" UNION"
				+" SELECT o.id,o.`code`,o.`name`,o.`name` shortName,o.leader_id deptRoleId,o.prefix_id upDeptRoleId from pt_sys_org_orgnazation o"
				+" LEFT JOIN pt_sys_org_post p on p.ref_id =o.id "
				+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
				+" where o.delflag=0 and p.delflag=0 and o.`status`=1 and pu.user_id=? and o.type in ('dept')";
		RowMapper<DeptDTO> rowMapper=new BeanPropertyRowMapper<DeptDTO>(DeptDTO.class);
		List<DeptDTO> list= jdbcTemplate.query(sql,rowMapper,userId,userId);
		if (!list.isEmpty() && list.size() > 0) {
			for (DeptDTO deptDTO : list) {
				List<DeptDTO> listDeptDTOs2 = new ArrayList<DeptDTO>();
				String ss = "'" + deptDTO.getUpDeptRoleId().replaceAll("/", "','") + "'".replaceAll("\\s*", "");
				String sqlString = "select  o.id, o.`code`, o.`name`, o.`name` shortName, o.leader_id deptRoleId, o.up_leader_id upDeptRoleId,o.type from pt_sys_org_orgnazation o where o.id in(" + ss + ")";
				List<Map<String, Object>> orgIdList = jdbcTemplate.queryForList(sqlString);
				for (Map<String, Object> map : orgIdList) {
					DeptDTO dept = new DeptDTO();
					if (map.get("type").equals("dept")) {
						dept.setId(map.get("id").toString());
						dept.setCode(map.get("code").toString());
						dept.setName(map.get("name").toString());
						dept.setShortName(map.get("shortName").toString());
						dept.setDeptRoleId(map.get("deptRoleId") != null ? map.get("deptRoleId").toString() : "");
						dept.setUpDeptRoleId(map.get("upDeptRoleId") != null ? map.get("upDeptRoleId").toString() : "");
						listDeptDTOs2.add(dept);
						break;
					}
				}
				listDeptDTOs.add(listDeptDTOs2.get(0));
			}
		}
		//过滤重复数据
		Set<DeptDTO> deptDTOs = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
		deptDTOs.addAll(listDeptDTOs);
		result.setResult( new ArrayList<>(deptDTOs));
		return result;
	}
	/**
	 * 根据用户ID获取用户部门
	 * @param userId
	 * @return
	 */
	@Override
	public FlowResult<List<PartyEntityDTO>> getDeptListByUserIdNew(String userId) {
		String sql="SELECT o.id,o.id refId,o.parent_id refParentId,o.`name`,o.prefix_id prefix,o.prefix_name namePrefix from pt_sys_org_orgnazation o" 
				+" LEFT JOIN pt_sys_org_post p on p.ref_id=o.id"
				+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
				+" where o.delflag=0 and o.`status`=1 and pu.delflag=0 and o.type='dept' and pu.user_id=?";	
		RowMapper<PartyEntityDTO> rowMapper=new BeanPropertyRowMapper<PartyEntityDTO>(PartyEntityDTO.class);
		List<PartyEntityDTO> list= jdbcTemplate.query(sql,rowMapper,userId);
		FlowResult<List<PartyEntityDTO>> result=new FlowResult<>();
		result.setResult(list);
		return result;
	}

	/**
	 * 根据公司Id,用户ID获取用户部门
	 * @param userId
	 * @return
	 */
	@Override
	public FlowResult<List<DeptDTO>> getDeptListByUserId(String companyId, String userId) {
		FlowResult<List<DeptDTO>> result=new FlowResult<>();
		List<DeptDTO> listDeptDTOs =new ArrayList<DeptDTO>();
		String orgIdSql="SELECT u.belong_org_id from pt_sys_org_user u WHERE u.id=?"
						+" UNION "
						+" SELECT p.ref_id from pt_sys_org_post_user pu"
						+" LEFT JOIN pt_sys_org_post p on pu.post_id=p.id"
						+" WHERE pu.user_id=? and p.delflag=0 ";
		List<String> orgIdList=jdbcTemplate.queryForList(orgIdSql,String.class,userId,userId);
		if (orgIdList==null || orgIdList.size()<=0) {
			result.setResult(null);
			return result;
		}else{
			String sql="SELECT o.id,o.`code`,o.`name`,o.`name` shortName,o.leader_id deptRoleId,o.prefix_id upDeptRoleId from pt_sys_org_orgnazation  o,pt_sys_org_orgnazation t "
						+" where  t.id =? and o.type='dept' and o.id in ('%s')"
						+" 	and o.prefix_id like concat(t.prefix_id,'%%') "
						+" 	ORDER BY LENGTH(o.prefix_id)  ";
			String orgIds=StringUtils.join(orgIdList,"','");
			sql=String.format(sql, orgIds);
			RowMapper<DeptDTO> rowMapper=new BeanPropertyRowMapper<DeptDTO>(DeptDTO.class);
			List<DeptDTO> list= jdbcTemplate.query(sql,rowMapper,companyId);
			if (!list.isEmpty() && list.size() > 0) {
				for (DeptDTO deptDTO : list) {
					List<DeptDTO> listDeptDTOs2 = new ArrayList<DeptDTO>();
					String ss = "'" + deptDTO.getUpDeptRoleId().replaceAll("/", "','") + "'".replaceAll("\\s*", "");
					String sqlString = "select  o.id, o.`code`, o.`name`, o.`name` shortName, o.leader_id deptRoleId, o.up_leader_id upDeptRoleId,o.type from pt_sys_org_orgnazation o where o.id in(" + ss + ")";
					List<Map<String, Object>> depList = jdbcTemplate.queryForList(sqlString);
					for (Map<String, Object> map : depList) {
						DeptDTO dept = new DeptDTO();
						if (map.get("type").equals("dept")) {
							dept.setId(map.get("id").toString());
							dept.setCode(map.get("code").toString());
							dept.setName(map.get("name").toString());
							dept.setShortName(map.get("shortName").toString());
							dept.setDeptRoleId(map.get("deptRoleId") != null ? map.get("deptRoleId").toString() : "");
							dept.setUpDeptRoleId(map.get("upDeptRoleId") != null ? map.get("upDeptRoleId").toString() : "");
							listDeptDTOs2.add(dept);
							break;
						}
					}
					listDeptDTOs.add(listDeptDTOs2.get(0));
				}
			}
			//过滤重复数据
			Set<DeptDTO> deptDTOs = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
			deptDTOs.addAll(listDeptDTOs);
			result.setResult( new ArrayList<>(deptDTOs));
			result.setResult(list);
			return result;
		}
	}
	
	
	/**
	 * 查询用户的本部门（去掉直属组织，只查岗位对应的，不往上也不往下找）（可以根据公司进行过滤）
	 * @param companyId
	 * @param userLoginName
	 * @return
	 */
	@Override
	public FlowResult<List<DeptDTO>> getThisDeptList(String companyId, String userLoginName) {
		FlowResult<List<DeptDTO>> result=new FlowResult<>();
		DebugInfo info=new DebugInfo();
		//判断用户id
		String userSql="SELECT u.id from pt_sys_org_user u WHERE u.login_name=? and u.delflag=0 and u.`status`=1";
		String userId= jdbcTemplate.queryForObject(userSql,String.class,userLoginName);
		if (userId==null||StringUtils.isBlank(userId)) {
			result.setSuccess(false);
			info.setCompleteAt("该用户不存在或被禁用");
			result.setDebugInfo(info);
			return result;
		}
//		String orgIdSql=" SELECT DISTINCT p.ref_id from pt_sys_org_post_user pu"
//						+" LEFT JOIN pt_sys_org_post p on pu.post_id=p.id"
//						+" WHERE pu.user_id=? and p.delflag=0 ORDER BY pu.is_default desc";
//		List<String> orgIdList=jdbcTemplate.queryForList(orgIdSql,String.class,userId);
//		if (orgIdList==null || orgIdList.size()<=0) {
//			result.setResult(null);
//			return result;
//		}else{
//			String sql ="";
//			if (companyId==null || companyId.equals("")) {
//				sql="SELECT DISTINCT o.id,o.`code`,o.`name`,o.`name` shortName,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId from pt_sys_org_orgnazation  o,pt_sys_org_orgnazation t "
//						+" where o.type='dept' and o.id in ('%s')"
//						+" 	and o.prefix_id like concat(t.prefix_id,'%%') ";
//			}else{
//				sql="SELECT DISTINCT o.id,o.`code`,o.`name`,o.`name` shortName,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId from pt_sys_org_orgnazation  o,pt_sys_org_orgnazation t "
//						+" where o.type='dept' and o.id in ('%s')"
//						+" 	and o.prefix_id like concat(t.prefix_id,'%%') and  t.id='"+companyId+"'  ";
//			}
//			 
//			String orgIds=StringUtils.join(orgIdList,"','");
//			sql=String.format(sql, orgIds);
			
			
			String sql =" SELECT DISTINCT o.id,o.`code`,o.`name`,o.`name` shortName,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId from pt_sys_org_orgnazation  o "
							+" right JOIN (SELECT DISTINCT p.ref_id,pu.is_default from pt_sys_org_post_user pu "
							+" LEFT JOIN pt_sys_org_post p on pu.post_id=p.id "
							+" WHERE pu.user_id='"+userId+"' and p.delflag=0 ) tt on o.id = tt.ref_id  "
							+" where o.type='dept'  "
							+" and o.prefix_id like concat('%%','"+companyId+"','%%') "
							+" ORDER BY tt.is_default desc  ";
			
			
			RowMapper<DeptDTO> rowMapper=new BeanPropertyRowMapper<DeptDTO>(DeptDTO.class);
			List<DeptDTO> list= jdbcTemplate.query(sql,rowMapper);
			result.setResult(list);
			return result;
	}
	
	/**
	 * 查询用户的本公司（去掉直属组织，只查岗位对应的，不往上也不往下找）
	 * @param userLoginName
	 * @return
	 */
	@Override
	public FlowResult<List<CompanyDTO>> getThisCompanyList(String userLoginName) {
		FlowResult<List<CompanyDTO>> result=new FlowResult<>();
		DebugInfo info=new DebugInfo();
		//判断用户id
		String userSql="SELECT u.id from pt_sys_org_user u WHERE u.login_name=? and u.delflag=0 and u.`status`=1";
		String userId= jdbcTemplate.queryForObject(userSql,String.class,userLoginName);
		if (userId==null||StringUtils.isBlank(userId)) {
			result.setSuccess(false);
			info.setCompleteAt("该用户不存在或被禁用");
			result.setDebugInfo(info);
			return result;
		}
		String orgIdSql=" SELECT DISTINCT p.ref_id from pt_sys_org_post_user pu"
						+" LEFT JOIN pt_sys_org_post p on pu.post_id=p.id"
						+" WHERE pu.user_id=? and p.delflag=0 ORDER BY pu.is_default desc";
		List<String> orgIdList=jdbcTemplate.queryForList(orgIdSql,String.class,userId);
		List<String> comIds =  new ArrayList<String>();
		if (orgIdList==null || orgIdList.size()<=0) {
			result.setResult(null);
			return result;
		}else{
			List<CompanyDTO> listresult= new ArrayList<CompanyDTO>();
			for(String s:orgIdList){
				String sql=" SELECT DISTINCT o.id,o.`code`,o.`name`,o.`name` shortName,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId from pt_sys_org_orgnazation  o,pt_sys_org_orgnazation t " 
				+" where t.id = '"+s+"' and o.type = 'company' and o.delflag = 0 and o.`status` = 1 "
				 +" 	and t.prefix_id like concat(o.prefix_id,'%%') ORDER BY LENGTH(o.prefix_id) desc ";
				RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
				List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper);
				if (list==null || list.size()<=0) {
				}else{
					boolean isexist = false;
					for(CompanyDTO c:listresult){
						if(c.getId().equals(list.get(0).getId())){
							isexist = true;
							break;
						}
					}
					if(!isexist){
						listresult.add(list.get(0));
					}
				}
			}
//			String sqlresult="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId  from pt_sys_org_orgnazation o "
//					+" where o.delflag=0 and o.`status`=1 and o.id in ('%s')";
//			String orgIds=StringUtils.join(comIds,"','");
//			sqlresult=String.format(sqlresult, orgIds);
//				RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
//				List<CompanyDTO> list= jdbcTemplate.query(sqlresult,rowMapper);
				result.setResult(listresult);
			return result;
		}
	}
	
	/**
	 * 根据用户id获取用户主部门
	 * @param userId
	 * @return
	 */
	@Override
	public FlowResult<DeptDTO> getDeptMainByUserId(String userId) {
		String sql="SELECT o.id,o.`code`,o.`name`,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId from pt_sys_org_post_user pu "
				+" LEFT JOIN pt_sys_org_post p on p.id=pu.post_id"
				+" LEFT JOIN pt_sys_org_orgnazation o on o.id=p.ref_id"
				+" where pu.user_id=? and pu.is_default=1 and o.type='dept' and o.delflag=0 and o.`status`=1 and p.delflag=0  ";
		RowMapper<DeptDTO> rowMapper=new BeanPropertyRowMapper<DeptDTO>(DeptDTO.class);
		List<DeptDTO> list= jdbcTemplate.query(sql,rowMapper,userId);
		FlowResult<DeptDTO> result=new FlowResult<>();
		if(list!=null&&list.size()>0){
			result.setResult(list.get(0));
			String upOrgSql="SELECT top.id,top.`code`,top.`name`,top.`name` shortName,top.`status`"
					+" from pt_sys_org_orgnazation t,pt_sys_org_orgnazation top "
					+" where  t.id =? and top.type ='dept' "
					+" and t.prefix_id like concat(top.prefix_id,'%%') "
					+" ORDER BY LENGTH(top.prefix_id)";
			//组织结构
			List<DeptDTO> mainCom=jdbcTemplate.query(upOrgSql,rowMapper,list.get(0).getId());
			if (mainCom!=null&&mainCom.size()>0) {
				result.setResult(mainCom.get(0));
			}
		}else {
			result.setResult(null);
		}
		return result;
	}

	@Override
	public FlowResult<DeptDTO> getUpDeptMainByUserId(Long userId) {

		return null;
	}
	/**
	 * 根据用户ID获取用户主部门
	 * Map key 为用户登录名
	 * @param userIds
	 * @return
	 */
	@Override
	public FlowResult<Map<String, DeptDTO>> getDeptMainByUserIds(String[] userIds) {
		FlowResult<Map<String, DeptDTO>> result=new FlowResult<>();
		String sql="SELECT u.login_name loginName,o.id,o.`code`,o.`name`,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId from pt_sys_org_user u "
				+" LEFT JOIN pt_sys_org_post_user pu on pu.user_id=u.id and pu.is_default=1 "
				+" LEFT JOIN pt_sys_org_post p on p.id=pu.post_id and p.delflag=0"
				+" LEFT JOIN pt_sys_org_orgnazation o on o.id=p.ref_id and o.type='dept' and o.delflag=0  and o.`status`=1"
				+" where pu.user_id in ('%s') ";
		if(userIds!=null&&userIds.length>0){
			String ids=StringUtils.join(userIds, "','");
			sql=String.format(sql, ids);
			List<Map<String, Object>> list= jdbcTemplate.queryForList(sql);
			if (list!=null&&list.size()>0) {
				Map<String, DeptDTO> res=new HashMap<String, DeptDTO>();
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map=list.get(i);
					DeptDTO dto=null;
					String key=map.get("loginName").toString();
					if(map.containsKey("id")&&map.get("id")!=null){
						try {
							dto = new DeptDTO();
							BeanUtils.populate(dto, map);
						} catch (IllegalAccessException | InvocationTargetException e) {
						}
					}
					res.put(key,dto);
				}
				result.setResult(res);
			}else {
				result.setResult(null);
			}
		}else {
			result.setResult(null);
			result.setSuccess(false);
		}

		return result;
	}

	@Override
	public FlowResult<List<DeptDTO>> getDeptListByUserIds(String[] userIds) {

		return null;
	}

	@Override
	public FlowResult<List<PostDTO>> getPostListByCompanyId(Long companyId) {

		return null;
	}

	@Override
	public FlowResult<List<PostDTO>> getPostListByCompanyIdAndStandardRoleId(String companyId, String standardRoleId) {
		String sql="SELECT p.id,r.`code`,r.`name`,p.type,p.role_id parentId,p.leader_id leaderRoleId,r.prefix_name upPath FROM pt_sys_org_post p  "
					+" LEFT JOIN pt_sys_org_standard_role r  on p.role_id=r.id "
					+" LEFT JOIN pt_sys_org_orgnazation s on s.id=p.ref_id"
					+" LEFT JOIN pt_sys_org_orgnazation f on s.prefix_id like CONCAT(f.prefix_id,'%')"
					+" WHERE p.delflag=0   and f.id=? and r.id=?";
		RowMapper<PostDTO> rowMapper=new BeanPropertyRowMapper<PostDTO>(PostDTO.class);
		List<PostDTO> list= jdbcTemplate.query(sql,rowMapper,companyId,standardRoleId);
		FlowResult<List<PostDTO>> result=new FlowResult<>();
		result.setResult(list);
		return result;
	}

	@Override
	public FlowResult<List<PostDTO>> getPostListByDeptIdAndStandardRoleId(Long companyId, Long standardRoleId) {

		return null;
	}

	@Override
	public FlowResult<List<PostDTO>> getPostListByStandardRoleId(Long standardRoleId) {

		return null;
	}
	/**
	 * 根据岗位ID获取岗位
	 * @param postId
	 * @return
	 */
	@Override
	public FlowResult<PostDTO> getPostById(String postId) {
		String sql="SELECT p.id,r.`code`,r.`name`,p.type,p.role_id parentId,p.leader_id leaderRoleId,r.prefix_name upPath FROM pt_sys_org_post p " 
				+" LEFT JOIN pt_sys_org_standard_role r  on p.role_id=r.id "
				+" WHERE p.delflag=0   and p.id=?";
		RowMapper<PostDTO> rowMapper=new BeanPropertyRowMapper<PostDTO>(PostDTO.class);
		List<PostDTO> list= jdbcTemplate.query(sql,rowMapper,postId);
		FlowResult<PostDTO> result=new FlowResult<>();
		if (list!=null&&list.size()>0) {
			result.setResult(list.get(0));
		}else {
			result.setResult(null);
		};
		return result;
	}

	@Override
	public FlowResult<List<PostDTO>> getPostListByIds(String[] postIds) {

		return null;
	}
	/**
	 * 根据部门ID获取岗位列表
	 * @param deptId
	 * @return
	 */
	@Override
	public FlowResult<List<PostDTO>> getPostListByDeptId(String deptId) {
		String sql="SELECT p.id,r.`code`,r.`name`,p.type,p.role_id parentId,p.leader_id leaderRoleId,r.prefix_name upPath FROM   pt_sys_org_post p " 
				+" LEFT JOIN pt_sys_org_standard_role r  on p.role_id=r.id "
				+" WHERE p.delflag=0  and p.ref_id=?";
		RowMapper<PostDTO> rowMapper=new BeanPropertyRowMapper<PostDTO>(PostDTO.class);
		List<PostDTO> list= jdbcTemplate.query(sql,rowMapper,deptId);
		FlowResult<List<PostDTO>> result=new FlowResult<>();
		result.setResult(list);
		return result;
	}
	/**
	 * 根据部门ID获取岗位列表
	 * @param deptId
	 * @return
	 */
	@Override
	public FlowResult<List<PostDTO>> getPostListByPeId(String peId) {
		String sql="SELECT p.id,r.`code`,r.`name`,p.type,p.role_id parentId,p.leader_id leaderRoleId,r.prefix_name upPath FROM   pt_sys_org_post p " 
				+" LEFT JOIN pt_sys_org_standard_role r  on p.role_id=r.id "
				+" WHERE p.delflag=0  and p.ref_id=?";
		RowMapper<PostDTO> rowMapper=new BeanPropertyRowMapper<PostDTO>(PostDTO.class);
		List<PostDTO> list= jdbcTemplate.query(sql,rowMapper,peId);
		FlowResult<List<PostDTO>> result=new FlowResult<>();
		result.setResult(list);
		return result;
	}

	@Override
	public FlowResult<List<PostDTO>> getPostListByUserLoginName(String userLoginName) {

		return null;
	}
	/**
	 * 根据用户ID获取用户岗位列表
	 * @param userId
	 * @return
	 */
	@Override
	public FlowResult<List<PostDTO>> getPostListByUserId(String userId) {
		String sql="SELECT p.id,r.`code`,r.`name`,p.type,p.role_id parentId,p.leader_id leaderRoleId,r.prefix_name upPath FROM pt_sys_org_post_user pu"
				+" LEFT JOIN pt_sys_org_post p on p.id=pu.post_id"
				+" LEFT JOIN pt_sys_org_standard_role r  on p.role_id=r.id "
				+" WHERE pu.user_id = ? AND pu.delflag = 0 and p.delflag=0  ";
		RowMapper<PostDTO> rowMapper=new BeanPropertyRowMapper<PostDTO>(PostDTO.class);
		List<PostDTO> list= jdbcTemplate.query(sql,rowMapper,userId);
		FlowResult<List<PostDTO>> result=new FlowResult<>();
		result.setResult(list);
		return result;
	}

	@Override
	public List<PartyEntityDTO> getParyEntityByStPidIsStrc(String partyStructTypeId, String parentEntityId,
			String isStruct) {

		return null;
	}

	@Override
	public FlowResult<List<OrgnDTO>> getOrgnListAll(String moduleCode, String authUserLoginName, Long ctrId,
			Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<OrgnDTO> getOrgnRoot(String moduleCode, String authUserLoginName, Long ctrId, Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<List<OrgnDTO>> getOrgnListSub(Long parentId, String[] includeTypes, String moduleCode,
			String authUserLoginName, Long ctrId, Long fieldId) {

		return null;
	}

	/**
	 * 获取授权公司列表
	 * moduleCode:系统编码 （PT）
	 * authUserLoginName：登录用户用户名 (haha)
	 * ctrId：无用
	 * fieldId：业务对象编码 (dept)
	 */
	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListAll(String moduleCode, String authUserLoginName, String ctrId,String fieldId) {
		FlowResult<List> result= getAuthOrgIds(moduleCode, authUserLoginName, 1, null);
		FlowResult<List<CompanyDTO>> res=new FlowResult<>();
		res.setDebugInfo(result.getDebugInfo());
		List<CompanyDTO> list=result.getResult();
		res.setResult(list);
		res.setSuccess(result.isSuccess());
		return res;
	}

	/**
	 * @param moduleCode 系统编码
	 * @param authUserLoginName 登录用户用户名
	 * @param authType 返回内容：1公司，2部门
	 * @return
	 */
	public FlowResult<List> getAuthOrgIds(String moduleCode,String authUserLoginName,int authType,String companyId) {
		FlowResult<List> result=new FlowResult<>();
		DebugInfo info=new DebugInfo();
		//判断用户id
		String userSql="SELECT u.id from pt_sys_org_user u WHERE u.login_name=? and u.delflag=0 and u.`status`=1";
		String uId= jdbcTemplate.queryForObject(userSql,String.class,authUserLoginName);
		if (uId==null||StringUtils.isBlank(uId)) {
			result.setSuccess(false);
			info.setCompleteAt("该用户不存在或被禁用");
			result.setDebugInfo(info);
			return result;
		}
		//判断系统id
		String appSql="SELECT a.id from pt_sys_res_app a WHERE a.`code`=? and a.delflag=0 and a.`status`=1";
		String appId= jdbcTemplate.queryForObject(appSql,String.class,moduleCode);
		if (appId==null||StringUtils.isBlank(appId)) {
			result.setSuccess(false);
			info.setCompleteAt("该系统不存在或被禁用");
			result.setDebugInfo(info);
			return result;
		}
		/*//查询用户所有角色
		String uRoleSql="SELECT GROUP_CONCAT(\"'\",t.id,\"'\") from ("
				+" SELECT r.id FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu"
				+" where r.id = p.role_id and r.delflag = 0 and r.status = 1 "
				+" and  p.id = pu.post_id and pu.user_id = ?"
				+" and pu.delflag = 0 and p.delflag = 0 and p.status = 1"
				+" UNION"
				+" SELECT r.id FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru "
				+" where r.id = ru.role_id and ru.user_id =? and r.delflag = 0 and r.status = 1"
				+" ) t";selectAuthTypeId
		String uRoles= jdbcTemplate.queryForObject(uRoleSql,String.class,uId,uId);*/
		String uRoles= selectAuthTypeId(uId);
		//查询所有角色授权控制点
		String dataPointSql="SELECT GROUP_CONCAT(po.`code`) from pt_sys_res_data_permission p" 
				+" LEFT JOIN pt_sys_res_data_point po on po.id=data_point_id"
				+" LEFT JOIN pt_sys_res_data_item i on i.id=po.item_id"
				+" WHERE p.role_id in (%s)"
				+" and i.item_code='dept' and i.app_id=? and po.delflag=0 and i.delflag=0";
		dataPointSql=String.format(dataPointSql, uRoles);
		String authDdataPoint= jdbcTemplate.queryForObject(dataPointSql,String.class,appId);
		if(authDdataPoint==null||StringUtils.isBlank(authDdataPoint)){
			result.setSuccess(false);
			info.setCompleteAt("没有数据授权信息");
			result.setDebugInfo(info);
			return result;
		}
		//根据授权情况获取授权公司
		if(authDdataPoint.contains("allOrg")){
			//全公司
			//获取授权公司
			String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId  from pt_sys_org_orgnazation o "
					+" where o.delflag=0 and o.`status`=1 and o.type %s";
			if (authType==1) {//公司
				sql=String.format(sql, " in ('company','zb')");
				RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
				List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper);
				result.setResult(list);
			}else{
				sql=String.format(sql, " ='dept'");
				if (companyId!=null&&StringUtils.isNotBlank(companyId)) {
					sql="SELECT DISTINCT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId  from pt_sys_org_orgnazation o "
						+" LEFT JOIN pt_sys_org_orgnazation t on o.prefix_id LIKE CONCAT('',t.prefix_id,'%%')"
						+" where o.delflag=0 and o.`status`=1 and o.type ='dept' and t.id='"+companyId+"'"+" ORDER BY o.sort ";
				}
				RowMapper<DeptDTO> rowMapper=new BeanPropertyRowMapper<DeptDTO>(DeptDTO.class);
				List<DeptDTO> list= jdbcTemplate.query(sql,rowMapper);
				result.setResult(list);
			}
				
		}else {
			//所有授权公司id
			List<String> ids=new ArrayList<>();
			if (authDdataPoint.contains("thisCompany")||authDdataPoint.contains("topDept")||authDdataPoint.contains("thisDept")) {
				//查询用户所有组织id=用户所属组织+用户岗位组织
				String uOrgSql="SELECT u.belong_org_id id,o.prefix_id from pt_sys_org_user u "
						+" LEFT JOIN pt_sys_org_orgnazation o on u.belong_org_id =o.id "
						+" WHERE u.id=? UNION"
						+" SELECT p.ref_id id,o.prefix_id from pt_sys_org_post_user pu "
						+" LEFT JOIN pt_sys_org_post p on pu.post_id=p.id"
						+" LEFT JOIN pt_sys_org_orgnazation o on p.ref_id =o.id "
						+" WHERE pu.user_id=? and p.delflag=0 ";
				List<Map<String,Object>> uOrg= jdbcTemplate.queryForList(uOrgSql,uId,uId);
				//获取用户组织对应的上级组织id
				String upOrgSql="SELECT top.id topId,top.prefix_id"
						+" from pt_sys_org_orgnazation t,pt_sys_org_orgnazation top "
						+" where  t.id =? and top.type %s "
						+" and t.prefix_id like concat(top.prefix_id,'%%') "
						+" ORDER BY LENGTH(top.prefix_id)";
				//查询组织所有下级公司id（即授权公司）
				String allDownSql="SELECT o.id from pt_sys_org_orgnazation o"
						+" LEFT JOIN pt_sys_org_orgnazation t on o.prefix_id LIKE CONCAT('',t.prefix_id,'%%')"
						+" where t.id in ('%s') and o.delflag=0 and o.`status`=1 and o.type %s ";
				if (authDdataPoint.contains("thisCompany")) {
					//用户所有本公司
					String[] orgArr=new String[uOrg.size()];
					upOrgSql=String.format(upOrgSql, " in ('zb','company')");
					for (int i = 0; i < uOrg.size(); i++) {
						//组织结构
						List<Map<String,Object>> upOrgs=jdbcTemplate.queryForList(upOrgSql,uOrg.get(i).get("id"));
						if (upOrgs!=null&&upOrgs.size()>0) {
							String orgId=upOrgs.get(upOrgs.size()-1).get("topId").toString();//本公司
							orgArr[i]=orgId;
							String[] upids=upOrgs.get(upOrgs.size()-1).get("prefix_id").toString().split("/");
							ids.addAll(Arrays.asList(upids));
						}else {
							orgArr[i]=uOrg.get(i).get("id").toString();
							String[] upids=uOrg.get(i).get("prefix_id").toString().split("/");
							ids.addAll(Arrays.asList(upids));
						}
					}
					String tid= StringUtils.join(orgArr,"','");
					if (authType==1) {//公司
						allDownSql=String.format(allDownSql,tid," in ('company','zb')");
					}else{
						allDownSql=String.format(allDownSql,tid," ='dept'");
					}
					List<String> upOrgs=jdbcTemplate.queryForList(allDownSql,String.class);
					if (upOrgs!=null&&upOrgs.size()>0) {
						ids.addAll(upOrgs);
					}
				}else if(authDdataPoint.contains("topDept")){
					//用户所有顶级部门
					String[] orgArr=new String[uOrg.size()];
					upOrgSql=String.format(upOrgSql, " ='dept'");
					for (int i = 0; i < uOrg.size(); i++) {
						//组织结构
						List<Map<String,Object>> upOrgs=jdbcTemplate.queryForList(upOrgSql,uOrg.get(i).get("id"));
						if (upOrgs!=null&&upOrgs.size()>0) {
							String orgId=upOrgs.get(0).get("topId").toString();//顶级部门
							orgArr[i]=orgId;
							String[] upids=upOrgs.get(0).get("prefix_id").toString().split("/");
							ids.addAll(Arrays.asList(upids));
						}else {
							orgArr[i]=uOrg.get(i).get("id").toString();
							String[] upids=uOrg.get(i).get("prefix_id").toString().split("/");
							ids.addAll(Arrays.asList(upids));
						}
					}
					String tid= StringUtils.join(orgArr,"','");
					if (authType==1) {//公司
						allDownSql=String.format(allDownSql,tid," in ('company','zb')");
					}else{
						allDownSql=String.format(allDownSql,tid," ='dept'");
					}
					List<String> upOrgs=jdbcTemplate.queryForList(allDownSql,String.class);
					if (upOrgs!=null&&upOrgs.size()>0) {
						ids.addAll(upOrgs);
					}
				}else if (authDdataPoint.contains("thisDept")) {
					//用户所有本部门
					String[] orgArr=new String[uOrg.size()];
					upOrgSql=String.format(upOrgSql, " ='dept'");
					for (int i = 0; i < uOrg.size(); i++) {
						//组织结构
						List<Map<String,Object>> upOrgs=jdbcTemplate.queryForList(upOrgSql,uOrg.get(i).get("id"));
						if (upOrgs!=null&&upOrgs.size()>0) {
							String orgId=upOrgs.get(upOrgs.size()-1).get("topId").toString();//本部门
							orgArr[i]=orgId;
							String[] upids=upOrgs.get(upOrgs.size()-1).get("prefix_id").toString().split("/");
							ids.addAll(Arrays.asList(upids));
						}else {
							orgArr[i]=uOrg.get(i).get("id").toString();
							String[] upids=uOrg.get(i).get("prefix_id").toString().split("/");
							ids.addAll(Arrays.asList(upids));
						}
					}
					String tid= StringUtils.join(orgArr,"','");
					if (authType==1) {//公司
						allDownSql=String.format(allDownSql,tid," in ('company','zb')");
					}else{
						allDownSql=String.format(allDownSql,tid," ='dept'");
					}
					List<String> upOrgs=jdbcTemplate.queryForList(allDownSql,String.class);
					if (upOrgs!=null&&upOrgs.size()>0) {
						ids.addAll(upOrgs);
					}
				}
			}
			if (authDdataPoint.contains("designOrg")) {
				//获取指定授权的授权值
				String dataPointIdsSql="SELECT v.val from pt_sys_res_data_permission p "
						+" LEFT JOIN pt_sys_res_data_point po on po.id=p.data_point_id"
						+" LEFT JOIN pt_sys_res_data_item i on i.id=po.item_id"
						+" LEFT JOIN  pt_sys_res_data_point_permission_val v on p.id=v.data_permission_id"
						+" WHERE p.role_id in (%s)"
						+" and i.item_code='dept' and i.app_id=? and po.type=2 and po.delflag=0 and i.delflag=0 ";
				dataPointIdsSql=String.format(dataPointIdsSql, uRoles);
				List<String> upOrgs=jdbcTemplate.queryForList(dataPointIdsSql,String.class,appId);
				if (upOrgs!=null&&upOrgs.size()>0) {
					ids.addAll(upOrgs);
				}
			}
			//去除重复公司id
			Set<String> setIds = new HashSet<String>(ids); 
			//获取授权公司
			String sql="SELECT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId  from pt_sys_org_orgnazation o "
					+" where o.delflag=0 and o.`status`=1 and o.type %s and o.id in ('%s')";
			Object[] allIds=  setIds.toArray();
			String allId=StringUtils.join(allIds, "','");
			if (authType==1) {//公司
				sql=String.format(sql," in ('company','zb')",allId);
				RowMapper<CompanyDTO> rowMapper=new BeanPropertyRowMapper<CompanyDTO>(CompanyDTO.class);
				List<CompanyDTO> list= jdbcTemplate.query(sql,rowMapper);
				result.setResult(list);
			}else{
				sql=String.format(sql," ='dept'",allId);
				if (companyId!=null&&StringUtils.isNotBlank(companyId)) {
					sql="SELECT DISTINCT o.id,o.`code`,o.`name`,o.`status`,o.parent_id parentId,o.leader_id deptRoleId,o.up_leader_id upDeptRoleId  from pt_sys_org_orgnazation o "
						+" LEFT JOIN pt_sys_org_orgnazation t on o.prefix_id LIKE CONCAT('',t.prefix_id,'%%')"
						+" where o.delflag=0 and o.`status`=1 and o.type ='dept' and o.id in ('%s') and t.id='"+companyId+"'";
					sql=String.format(sql,allId);
				}
				RowMapper<DeptDTO> rowMapper=new BeanPropertyRowMapper<DeptDTO>(DeptDTO.class);
				List<DeptDTO> list= jdbcTemplate.query(sql,rowMapper);
				result.setResult(list);
			}
			
		}
		return result;
	}
	public String selectAuthTypeId(String uId) {
		String sql=" SELECT u.id from pt_sys_org_user u WHERE u.id=?"
						+" UNION "
						+" SELECT t.id from pt_sys_org_standard_role t "
						+" LEFT JOIN pt_sys_org_post p on p.role_id=t.id"
						+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
						+" WHERE pu.user_id=? and t.delflag=0 and t.`status`=1"
						+" UNION "
						+" SELECT p.id from pt_sys_org_post p "
						+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
						+" WHERE pu.user_id=? and p.delflag=0"
						+" UNION"
						+" SELECT r.id from pt_sys_org_standard_role r"
						+" LEFT JOIN pt_sys_org_role_user ru on ru.role_id=r.id "
						+" WHERE ru.user_id=? and r.delflag=0 and r.`status`=1 "
						+" UNION"
						+" SELECT r.id from pt_sys_org_standard_role r"
						+" LEFT JOIN pt_sys_org_role_user ru on ru.role_id=r.id"
						+" LEFT JOIN pt_sys_org_post p on p.id=ru.post_id AND IFNULL(ru.user_id,'')='' "
						+" LEFT JOIN pt_sys_org_post_user pu on pu.post_id=p.id"
						+" WHERE pu.user_id=? and p.delflag=0";
//		String uRoles= jdbcTemplate.queryForObject(sql,String.class,uId,uId,uId,uId,uId);
		List<String> upOrgs=jdbcTemplate.queryForList(sql,String.class,uId,uId,uId,uId,uId);
		String uRoles="";
		if(!upOrgs.isEmpty()&&upOrgs.size()>0){
			for (String ids : upOrgs) {
				uRoles+="'"+ids+"',";
			}
		}
		if(uRoles.length()>0){
			uRoles=uRoles.substring(0,uRoles.length()-1);
		}
		return uRoles;
	}

	@Override
	public FlowResult<CompanyDTO> getCompanyById(Long companyId, String moduleCode, String authUserLoginName,
			Long ctrId, Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByIds(String[] companyIds, String moduleCode,
			String authUserLoginName, Long ctrId, Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListLeaf(String moduleCode, String authUserLoginName, Long ctrId,
			Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListHaveProject(String moduleCode, String authUserLoginName,
			Long ctrId, Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<CompanyDTO> getCompanyByDeptId(Long deptId, String moduleCode, String authUserLoginName,
			Long ctrId, Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByDeptIds(String[] deptIds, String moduleCode,
			String authUserLoginName, Long ctrId, Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByUserId(String userId, String moduleCode, String authUserLoginName,
			String ctrId, String fieldId) {
		FlowResult<List> result= getAuthOrgIds(moduleCode, authUserLoginName, 1, null);
		FlowResult<List<CompanyDTO>> res=new FlowResult<>();
		res.setDebugInfo(result.getDebugInfo());
		List<CompanyDTO> list=result.getResult();
		res.setResult(list);
		res.setSuccess(result.isSuccess());
		return res;
	}

	@Override
	public FlowResult<CompanyDTO> getCompanyMainByUserId(Long userId, String moduleCode, String authUserLoginName,
			Long ctrId, Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByUserIds(String[] userIds, String moduleCode,
			String authUserLoginName, Long ctrId, Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByUserLoginName(String userLoginName, String moduleCode,
			String authUserLoginName, String ctrId, String fieldId) {
		FlowResult<List> result= getAuthOrgIds(moduleCode, authUserLoginName, 1, null);
		FlowResult<List<CompanyDTO>> res=new FlowResult<>();
		res.setDebugInfo(result.getDebugInfo());
		List<CompanyDTO> list=result.getResult();
		res.setResult(list);
		res.setSuccess(result.isSuccess());
		return res;
	}

	@Override
	public FlowResult<CompanyDTO> getCompanyMainByUserLoginName(String userLoginName, String moduleCode,
			String authUserLoginName, Long ctrId, Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<List<CompanyDTO>> getCompanyListByUserLoginNames(String[] userLoginNames, String moduleCode,
			String authUserLoginName, Long ctrId, Long fieldId) {

		return null;
	}

	/**
	 * 获取公司下有权限的部门
	 * companyId 公司id 可以为空
	 * moduleCode 系统编码
	 * authUserLoginName 用户名
	 */
	@Override
	public FlowResult<List<DeptDTO>> getAllDept(String companyId, String moduleCode, String authUserLoginName, String ctrId,String fieldId) {

		FlowResult<List> result= getAuthOrgIds(moduleCode, authUserLoginName, 2, companyId);
		FlowResult<List<DeptDTO>> res=new FlowResult<>();
		res.setDebugInfo(result.getDebugInfo());
		List<DeptDTO> list=result.getResult();
		res.setSuccess(result.isSuccess());
		res.setResult(list);
		return res;
	}

	@Override
	public FlowResult<List<OrgnDTO>> getAuthDeptByCompany(Long companyId, String moduleCode, String authUserLoginName,
			Long ctrId, Long fieldId) {

		return null;
	}

	@Override
	public FlowResult<List<OrgnDTO>> getOrgnListNotProjectAndBranch() {
		String sql="SELECT o.id,o.id refId,o.`name`,if(IFNULL(o.parent_id,'')='',0,o.parent_id) parentId,o.type,o.type nodetype,o.`code`,o.`status`,FALSE isLeaf from pt_sys_org_orgnazation o"
				+" WHERE o.delflag = 0 and o.`status`=1 and o.type in('company','zb','dept') and EXISTS (SELECT 1 from pt_sys_org_orgnazation t where t.parent_id=o.id)"
				+" UNION "
				+" SELECT o.id,o.id refId,o.`name`,if(IFNULL(o.parent_id,'')='',0,o.parent_id) parentId,o.type,o.type nodetype,o.`code`,o.`status`,TRUE isLeaf from pt_sys_org_orgnazation o"
				+" WHERE o.delflag = 0 and o.`status`=1 and o.type in('company','zb','dept') and NOT EXISTS (SELECT 1 from pt_sys_org_orgnazation t where t.parent_id=o.id)";
		RowMapper<OrgnDTO> rowMapper=new BeanPropertyRowMapper<OrgnDTO>(OrgnDTO.class);
		List<OrgnDTO> list= jdbcTemplate.query(sql,rowMapper);
		FlowResult<List<OrgnDTO>> result=new FlowResult<>();
		result.setResult(list);
		return result;
	}

	@Override
	public FlowResult<List<OrgnDTO>> getOrgnByRefId(Long refId, String[] includeTypes) {

		return null;
	}

	/**
	 * 查询上级部门
	 * 
	 * @param includeTypes
	 *           包含节点类型（company:公司  department:部门  group:团队 ）
	 * @return
	 */
	@Override
	public FlowResult<OrgnDTO> getParentOrgnByChildId(String refId, String[] includeTypes) {
		FlowResult<OrgnDTO> result=new FlowResult<OrgnDTO>();
		try {
			String sql="SELECT o.id,o.id refId,o.`name`,o.parent_id parentId,o.type,o.`code`,o.`status` from pt_sys_org_orgnazation o"
					+" WHERE o.delflag = 0 and o.type in ('%s') and o.parent_id='%s'";
			if (includeTypes!=null&&includeTypes.length>0) {
				String type=StringUtils.join(includeTypes,"','");
				type=type.replaceAll("department", "dept");
				type=type.replaceAll("company", "company','zb");
				sql=String.format(sql, type,refId);
				RowMapper<OrgnDTO> rowMapper=new BeanPropertyRowMapper<OrgnDTO>(OrgnDTO.class);
				List<OrgnDTO> list= jdbcTemplate.query(sql,rowMapper);
				if (list!=null&&list.size()>0) {
					result.setResult(list.get(0));
				}else {
					result.setResult(null);
				}
			}else {
				result.setResult(null);
				result.setSuccess(false);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public FlowResult<List<ScopeDTO>> getAuthScope(String moduleCode, String authUserLoginName, String ctrId,
			String fieldId) {
		List<ScopeDTO> listResult = new ArrayList<ScopeDTO>();
		List<Map<String,Object>> roleList = null;
		List<Map<String,Object>> resultList = null;
		FlowResult result=new FlowResult();
		try {
			String queryRoleSql = "SELECT "
					+" r.id AS id, "
					+" r.NAME AS NAME, "
					+" r.CODE AS CODE  "
					+" FROM pt_sys_org_standard_role r ,pt_sys_org_post p ,pt_sys_org_post_user pu ,pt_sys_org_user u  "
					+" where r.id = p.role_id and r.delflag = 0 and r.status = 1  "
					+" and  p.id = pu.post_id and pu.user_id = u.id and u.login_name = '"+authUserLoginName+"'  "
					+" and pu.delflag = 0 and p.delflag = 0 and p.status = 1 "
					+" UNION "
					+" SELECT "
					+" r.id AS id, "
					+" r.NAME AS NAME, "
					+" r.CODE AS CODE "
					+" FROM pt_sys_org_standard_role r ,pt_sys_org_role_user ru ,pt_sys_org_user u  "
					+" where r.id = ru.role_id and ru.user_id = u.id and u.login_name = '"+authUserLoginName+"' and r.delflag = 0 and r.status = 1 ";
			roleList = jdbcTemplate.queryForList(queryRoleSql);
			
			List<String> roleListString = null;
			String roleString = "";
			if(null != roleList && roleList.size()>0){
				for(Map map:roleList){
					roleString += "'"+(String)map.get("id")+"',";
				}
			}
			
			if(roleString.length()>0){
				roleString = roleString.substring(0,roleString.length()-1);
			}else{
				result.setSuccess(false);
				result.setResult(listResult);
				return result;
			}
			String queryResultSql = " select t.id,t.item_id itemId,app.id appId,app.`code` appCode,app.`name` appName ,di.item_code itemCode,di.item_name itemName,t.code ,t.name,tt.role_id ,GROUP_CONCAT(ttt.val) val from pt_sys_res_data_point t   "
					+" LEFT JOIN pt_sys_res_data_permission tt on t.id = tt.data_point_id   "
					+" LEFT JOIN pt_sys_res_data_point_permission_val ttt on ttt.data_permission_id = tt.id   "
					+" LEFT JOIN pt_sys_res_data_item di on t.item_id = di.id   "
					+" LEFT JOIN pt_sys_res_app app on di.app_id = app.id   "
					+" where tt.role_id in ("+roleString+")  ";
			if(null != moduleCode && !"".equals(moduleCode)){
				queryResultSql = queryResultSql + " AND app.code = '"+moduleCode+"' ";
			}
			
			if(null != fieldId && !"".equals(fieldId)){
				queryResultSql = queryResultSql + " and t.item_id = '"+fieldId+"' ";
			}
			
			queryResultSql = queryResultSql + " GROUP BY t.id,tt.role_id ";
			resultList = jdbcTemplate.queryForList(queryResultSql);
			
			for(Map map2:resultList){
					ScopeDTO scopeDTO = new ScopeDTO();
					scopeDTO.setId((String)map2.get("id"));
					scopeDTO.setCode((String)map2.get("code"));
					scopeDTO.setName((String)map2.get("name"));
					scopeDTO.setValue((String)map2.get("val"));
					listResult.add(scopeDTO);
			}
			result.setSuccess(true);
			result.setResult(listResult);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(e.getMessage());
		}
		
		return result;
	}

	@Override
	public List<RoleDTO> getPostsByCurrentUser(String loginName) {

		return null;
	}
}
