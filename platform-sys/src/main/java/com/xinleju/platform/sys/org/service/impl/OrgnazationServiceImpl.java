package com.xinleju.platform.sys.org.service.impl;

import java.util.*;

import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;
import com.xinleju.platform.flow.utils.StringUtil;
import com.xinleju.platform.sys.org.dto.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.out.app.org.entity.UserAuthDataOrgList;
import com.xinleju.platform.sys.org.dao.OrgnazationDao;
import com.xinleju.platform.sys.org.dao.PostDao;
import com.xinleju.platform.sys.org.dao.StandardRoleDao;
import com.xinleju.platform.sys.org.dao.UserDao;
import com.xinleju.platform.sys.org.entity.Orgnazation;
import com.xinleju.platform.sys.org.entity.Post;
import com.xinleju.platform.sys.org.entity.User;
import com.xinleju.platform.sys.org.service.OrgnazationService;
import com.xinleju.platform.sys.org.service.PostService;
import com.xinleju.platform.sys.org.service.StandardRoleService;
import com.xinleju.platform.sys.org.service.UserService;
import com.xinleju.platform.sys.res.dao.AppSystemDao;
import com.xinleju.platform.sys.res.dao.FuncPermissionDao;
import com.xinleju.platform.sys.res.dto.ResourceDto;
import com.xinleju.platform.sys.res.service.FuncPermissionService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.tools.data.JacksonUtils;
import sun.java2d.opengl.OGLRenderQueue;

/**
 * @author admin
 * 
 * 
 */

@Service
public class OrgnazationServiceImpl extends  BaseServiceImpl<String,Orgnazation> implements OrgnazationService{
	private static Logger log = Logger.getLogger(OrgnazationServiceImpl.class);

	@Autowired
	private OrgnazationDao orgnazationDao;
	@Autowired
	private StandardRoleService standardRoleService;
	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;
	@Autowired
	private FuncPermissionService funcPermissionService;
	@Autowired
	private FuncPermissionDao funcPermissionDao;
	@Autowired
	private PostDao postDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private StandardRoleDao standardRoleDao;
	@Autowired
	private AppSystemDao appSystemDao;

	@Override
	public List<OrgnazationNodeDto> queryOrgListRoot(String rootId) throws Exception {
		return orgnazationDao.queryOrgListRoot(rootId);

	}

	@Override
	public List<Orgnazation> queryOrgListRootReturnOrg(String rootId) throws Exception {
		return orgnazationDao.queryOrgListRootReturnOrg(rootId);

	}

	@Override
	public List<Orgnazation> queryOrgListRootReturnOrg(String rootId,Long sort) throws Exception {
		return orgnazationDao.queryOrgListRootReturnOrg(rootId,sort);

	}

	@Override
	public List<OrgnazationNodeDto> queryOrgList(String parentId) throws Exception {
		return orgnazationDao.queryOrgList(parentId);

	}

	@Override
	public List<OrgnazationNodeDto> queryAllOrgList(Map<String,Object> map) throws Exception {
		return orgnazationDao.queryAllOrgList(map);

	}

	@Override
	public List<Orgnazation> queryAllOrgListReturnOrg() throws Exception {
		return orgnazationDao.queryAllOrgListReturnOrg();

	}

	@Override
	public List<Orgnazation> queryOrgListReturnOrg(String parentId) throws Exception {
		return orgnazationDao.queryOrgListReturnOrg(parentId);

	}

	@Override
	public List<Orgnazation> queryOrgListReturnOrg(String parentId,Long sort) throws Exception {
		return orgnazationDao.queryOrgListReturnOrg(parentId, sort);

	}

	@Override
	public List<Map<String, Object>> queryListCompany(Map map)throws Exception {
		return orgnazationDao.queryListCompany(map);

	}

	@Override
	public List<Map<String,String>> queryOrgsByIds(Map map)throws Exception{
		return orgnazationDao.queryOrgsByIds(map);
	}

	@Override
	public Integer updatePrefix(Map map)throws Exception{
		return orgnazationDao.updatePrefix(map);
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Integer updateNew(String updateJson)throws Exception{
		/*updateJson=	updateJson.replaceAll("\\\\", "\\\\\\\\");
		updateJson=	updateJson.replaceAll("\\'", "\\\\\\\\\'");*/
		Orgnazation orgnazation=JacksonUtils.fromJson(updateJson, Orgnazation.class);
		Orgnazation orgnazationold = this.getObjectById(orgnazation.getId());
		//校验编码重复
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("code", orgnazation.getCode());
		param.put("parentId", orgnazation.getParentId());
		param.put("id", orgnazation.getId());
		int c=checkCode(param);
		if(c>0){
			throw new InvalidCustomException("同级编码已存在，不可重复");
		}
		//如果状态没有改变不更改下级
		if(!orgnazation.getStatus().equals(orgnazationold.getStatus())){
			Map<String,Object> map=new HashMap<String,Object>();
			if(orgnazation.getStatus().equals("0")){//禁用组织，并将其下级组织禁用
				String prefixId=orgnazationold.getPrefixId();
				map.put("orgId", prefixId);
				this.lockOrg(map);
			}else if(orgnazation.getStatus().equals("1")){//启用组织，并将其上级组织启用
				String prefixId=orgnazationold.getPrefixId();
				String orgIds[]=prefixId.split("/");
				map.put("orgIds", orgIds);
				this.unLockOrg(map);
			}
		}
		String prefixName=orgnazation.getPrefixName();
		prefixName=prefixName.replaceAll("\\\\\\\\", "\\\\");
		prefixName=prefixName.replaceAll("\\\\'", "'");
		//如果全路径有变化，先更改全路径
		if(!orgnazationold.getPrefixId().equals(orgnazation.getPrefixId()) || !orgnazationold.getPrefixName().equals(prefixName)){
			Map<String,String> map = new HashMap<String,String>();
			map.put("prefixIdold", orgnazationold.getPrefixId());
			map.put("prefixId", orgnazation.getPrefixId());
			map.put("prefixNameold", orgnazationold.getPrefixName());
			map.put("prefixName", prefixName);
			this.updatePrefix(map);
		}

		if(null == orgnazation.getSort()){
//			Map<String,Object> map=new HashMap<String,Object>();
//			map.put("tableName", "pt_sys_org_orgnazation");
//			Long maxSort=appSystemDao.getMaxSort(map)+1L;//排序号自动加1
			orgnazation.setSort(99L);
		}
		int result=   this.update(orgnazation);
		return result;
	}

	/**
	 * 禁用组织
	 * @param paramater
	 * @return
	 */
	@Override
	public Integer lockOrg(Map map)throws Exception{
		return orgnazationDao.lockOrg(map);
	}

	/**
	 * 启用组织
	 * @param map
	 * @return
	 */
	@Override
	public Integer unLockOrg(Map map)throws Exception{
		//启用组织，并将其上级组织启用
		String id=(String)map.get("orgId");
		Orgnazation org = getObjectById(id);
		String prefixId=org.getPrefixId();
		String upIds[]=prefixId.split("/");
		Set<String>  orgIds = new HashSet<>() ;
		orgIds.addAll(Arrays.asList(upIds));
		if(map.get("ifUnLockDown") != null && map.get("ifUnLockDown").toString().equals("1")){
			//同时启用下级
			map.put("orgId", id);
			List<Orgnazation>  downOrgs = orgnazationDao.selectSunOrgByOrgId(map);
			for (int i = 0; i < downOrgs.size(); i++) {
				orgIds.add(downOrgs.get(i).getId());
			}
		}
		map.put("orgIds", orgIds);
		return orgnazationDao.unLockOrg(map);
	}

	/**
	 * 根据组织机构Id查询以上级别的组织机构（当前用户）
	 * @param paramater
	 * @return
	 */
	@Override
	public List<OrgnazationDto> queryAuthOrgListByOrgId(Map<String, Object> paramater)  throws Exception{
		return orgnazationDao.queryAuthOrgListByOrgId(paramater);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.org.service.OrgnazationService#getCompanyOrgId()
	 */
	@Override
	public List<String> getCompanyOrgId() throws Exception {
		return orgnazationDao.getCompanyOrgId();
	}

	/**
	 * 查询组织详情
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public OrgnazationDto getOrgById(Map<String, Object> paramater)throws Exception{
		return orgnazationDao.getOrgById(paramater);
	}

	/**
	 * 根据用户id，获取其角色/岗位/组织/菜单  等信息
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public AuthenticationDto getUserRPOMInfoByUserId(Map<String, Object> param)throws Exception{

		AuthenticationDto authenticationDto =new AuthenticationDto(); 
		if(param.containsKey("searchType") && param.get("searchType")!=null && StringUtils.isNotBlank(param.get("searchType").toString())){
			String searchType=param.get("searchType").toString();
			User user=null;
			if(param.containsKey("userId") && param.get("userId")!=null && StringUtils.isNotBlank(param.get("userId").toString())){
				user= userService.getObjectById(param.get("userId").toString());
			}

			//获取用户的角色
			List<StandardRoleDto> standardRoleDtoList =  standardRoleService.queryRoleListByUserId(param);
			if(searchType.equals("Role")||searchType.equals("All")){
				authenticationDto.setStandardRoleDtoList(standardRoleDtoList);
			}

			//获取用户的岗位
			if(searchType.equals("Post")||searchType.equals("All")){
				List<PostDto> postDtoList = postService.queryAuthPostListByUserId(param);
				authenticationDto.setPostDtoList(postDtoList);
			}

			//获取用户的组织信息
			if(searchType.equals("Org")||searchType.equals("All")){
				//若传递组织id
				if (param.containsKey("post_orgId") && param.get("post_orgId")!=null && StringUtils.isNotBlank(param.get("post_orgId").toString())) {
					param.put("orgId", param.get("post_orgId").toString());
				}else{
					//获取用户主岗对应组织，若无，使用用户所属组织
					String postOrg=postService.getDefaultPostOrg(param);
					if (StringUtils.isNotBlank(postOrg)) {
						param.put("orgId", postOrg);
					}else {
						param.put("orgId", user.getBelongOrgId());
					}
				}
				getOrgInfo(authenticationDto,param);
			}

			//获取用户的授权信息
			if(searchType.equals("Menu")||searchType.equals("All")){
				List<String> list = new ArrayList<String>();
				/*if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
				for(StandardRoleDto srDto:standardRoleDtoList){
					list.add(srDto.getId());
				}
				map.put("roleIds", list);
				//获取用户授权的菜单
				List<ResourceDto> resourceDtoList =  resourceService.queryAuthMenu(map);
				authenticationDto.setResourceDtoList(resourceDtoList);
				}*/

				if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
					for(StandardRoleDto srDto:standardRoleDtoList){
						list.add(srDto.getId());
					}
				}
				param.put("roleIds", list);
				//获取用户的菜单清单（已授权的未授权的）
				List<ResourceDto> resourceDtoList =  funcPermissionService.queryAuthorizationAllList(param);
				authenticationDto.setResourceDtoList(resourceDtoList);
			}
		}

		return authenticationDto;
	}
	//TODO 获取组织相关信息
	public AuthenticationDto getOrgInfo(AuthenticationDto authenticationDto,Map<String,Object> param) throws Exception {
		//获取当前用户的组织机构
		List<OrgnazationDto> orgnazationDtoList = queryAuthOrgListByOrgId(param);

		if(null != orgnazationDtoList && orgnazationDtoList.size()>0){

			//直属组织
			OrgnazationDto orgnazationDto= orgnazationDtoList.get(orgnazationDtoList.size()-1);
			//顶级组织
			OrgnazationDto topOrgnazationDto= orgnazationDtoList.get(0);
			//公司
			List<OrgnazationDto> companyList = new ArrayList<OrgnazationDto>();
			//部门
			List<OrgnazationDto> deptList = new ArrayList<OrgnazationDto>();
			for(OrgnazationDto orgDto:orgnazationDtoList){
				if(orgDto.getType().equals("company") || orgDto.getType().equals("zb")){
					companyList.add(orgDto);
				}
				if(orgDto.getType().equals("dept")){
					deptList.add(orgDto);
				}
			}
			//设置当前用户所在组织的类型
			authenticationDto.setOrganizationType(orgnazationDto.getType());
			//设置当前用户所在的组织，分期、项目、部门、顶级部门、公司、顶级公司
			if(orgnazationDto.getType().equals("branch")){
				//设置分期
				authenticationDto.setBranchDto(orgnazationDto);
				//设置项目
				authenticationDto.setGroupDto(orgnazationDtoList.get(orgnazationDtoList.size()-2));
				//设置直属公司
				authenticationDto.setDirectCompanyDto(companyList.get(companyList.size()-1));
				//设置顶级公司
				authenticationDto.setTopCompanyDto(companyList.get(0));
				//顶级公司也可以如下设置
				//authenticationDto.setTopCompanyDto(topOrgnazationDto);
			}else if(orgnazationDto.getType().equals("group")){
				//设置项目
				authenticationDto.setGroupDto(orgnazationDto);
				//设置直属公司
				authenticationDto.setDirectCompanyDto(companyList.get(companyList.size()-1));
				//设置顶级公司
				authenticationDto.setTopCompanyDto(companyList.get(0));
			}else if(orgnazationDto.getType().equals("dept")){
				//设置直属部门
				authenticationDto.setDirectDeptDto(orgnazationDto);
				//设置顶级部门
				authenticationDto.setTopDeptDto(deptList.get(0));
				//设置直属公司
				authenticationDto.setDirectCompanyDto(companyList.get(companyList.size()-1));
				//设置顶级公司
				authenticationDto.setTopCompanyDto(companyList.get(0));
			}else if(orgnazationDto.getType().equals("company")){
				//设置直属公司
				authenticationDto.setDirectCompanyDto(orgnazationDto);
				//设置顶级公司
				authenticationDto.setTopCompanyDto(topOrgnazationDto);
			}else if(orgnazationDto.getType().equals("zb")){
				//设置直属公司
				authenticationDto.setDirectCompanyDto(orgnazationDto);
				//设置顶级公司
				authenticationDto.setTopCompanyDto(topOrgnazationDto);
			}
		}
		return authenticationDto;
	}
	/**
	 * 查询用户所有组织信息：所属组织U岗位组织
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<OrgnazationDto> getUserAllOrgs(Map<String, Object> paramater) throws Exception {
		return orgnazationDao.selectUserAllOrgs(paramater);
	}
	/**
	 * 查询部门 或项目分期 （包含集团和公司）
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String,Object>> getDeptOrBranch(Map<String, Object> paramater) throws Exception {
		List<Map<String,Object>> res=orgnazationDao.getDeptOrBranch(paramater);
		String prefixId=null;
		Integer level=null;
		int maxLevel=0;
		//id-->map
		Map<String, Object> idMap=new HashMap<String, Object>();
		for (int i = 0; i < res.size(); i++) {
			Map<String,Object> map=res.get(i);
			/*if(!map.containsKey("prefixId")||map.get("prefixId")==null||StringUtils.isBlank(map.get("prefixId").toString())){
				continue;
			}*/
			prefixId=map.get("prefix_id").toString();
			level=prefixId.split("/").length;
			map.put("level",level);
			if(level>maxLevel){
				maxLevel=level;
			}
			map.put("expanded", false);
			map.put("loaded", true);
			idMap.put(map.get("id").toString(), map);
		}
		//重新排序，讲子节点插入到父节点下一条
		List<Map<String,Object>> sortList=new LinkedList<Map<String,Object>>();
		sortList.addAll(res);
		//父节点下有几条子节点
		Map<String, Integer> sCount=new HashMap<String, Integer>();
		for (int m = 2; m < maxLevel+1; m++) {
			for (int i = 0; i < res.size(); i++) {
				//需要重新排序的map
				Map<String,Object> smap=res.get(i);
				//父节点id
				if(smap.get("pId")!=null){
					String fId=smap.get("pId").toString();
					if(Integer.valueOf(smap.get("level").toString())==m){
						//父节点map
						Map<String,Object> fmap=(Map)idMap.get(fId);
						Map<String,Object> _map=null;
						//父节点位置
						int fIndex=sortList.indexOf(fmap);
						//我的位置
						int sIndex=sortList.indexOf(smap);
						if(fIndex!=-1&&sIndex!=-1){
							int sC=sCount.get(fId)==null?0:sCount.get(fId);//我的父节点现在的子节点数量
							//重新排序：按顺序依次插到父节点后面
							if(fIndex<sIndex){//父节点在前
								_map=sortList.get((sIndex));
								sortList.add((fIndex+sC+1), _map);
								sC++;
								sCount.put(fId, sC);//父节点的子节点数量加1
								sortList.remove((sIndex+1));//删除原先的位置
							}else if(fIndex>sIndex){//父节点在后
								fIndex++;
								_map=sortList.get((sIndex));
								sortList.add((fIndex+sC), _map);
								sC++;
								sCount.put(fId, sC);//父节点的子节点数量加1
								sortList.remove((sIndex));
							}
						}
					}
				}
			}
		}
		
		return sortList; 
//		return res; 
	}

	@Override
	public List<OrgnazationDto> getAllOrgListDto(Map<String, Object> paramater) throws Exception {
		return orgnazationDao.getAllOrgListDto(paramater);
	}
	
	@Override
	public List<OrgnazationDto> getOrgsByIds(Map<String, Object> paramater) throws Exception {
		return orgnazationDao.getOrgsByIds(paramater);
	}
	/**
	 * 获取指定公司的下指定组织（公司，部门，项目，分期）
	 * @return
	 */
	@Override
	public Map<String,Object> getChildOrgList(Map<String, Object> param) throws Exception {
		String[] companyIds=param.get("companyIds").toString().split(",");
		Boolean isLeaf=(Boolean)param.get("isLeaf");
		String ids=null;
		List<OrgnazationDto> list=null;
		Map<String,Object> res=new HashMap<String,Object>();
		if(isLeaf!=null&&isLeaf){//所有下级指定组织
			for (int i = 0; i < companyIds.length; i++) {
				param.put("parentId", companyIds[i]);
				ids=orgnazationDao.selectChildIds(param);
				ids=ids.replace(companyIds[i], "");
				if(StringUtils.isNotBlank(ids)){
					param.put("parentId", null);
					param.put("ids", ids.split(","));
					list=orgnazationDao.selectChildOrgList(param);
				}else{
					list=new ArrayList<OrgnazationDto>();
				}
				res.put(companyIds[i],list);
			}
		}else{//所有直接指定组织
			for (int i = 0; i < companyIds.length; i++) {
				param.put("parentId", companyIds[i]);
				list=orgnazationDao.selectChildOrgList(param);
				res.put(companyIds[i],list);
			}
		}
		return res;
	}
	/**
	 * 获取指定公司下的分期
	 * @return
	 */
	@Override
	public Map<String,Object> getChildBranchList(Map<String, Object> param) throws Exception {
		String[] companyIds=param.get("companyIds").toString().split(",");
		List<OrgnazationDto> list=null;
		Map<String,Object> res=new HashMap<String,Object>();
		for (int i = 0; i < companyIds.length; i++) {
			param.put("parentId", companyIds[i]);
			list=orgnazationDao.selectChildBranchList(param);
			res.put(companyIds[i],list);
		}
		return res;
	}
	/**
	 * 获取用户岗位对应公司
	 * paramJson:{userIds:"指用户Ids"}
	 * @return
	 */
	@Override
	public Map<String,Object> getUserPostRelationOrgList(Map<String, Object> param) throws Exception {
		String[] userIds=param.get("userIds").toString().split(",");
		List<OrgnazationDto> list=null;
		List<String> ids=null;
		Map<String,Object> res=new HashMap<String,Object>();
		for (int i = 0; i < userIds.length; i++) {
			param.put("userId", userIds[i]);
			ids=orgnazationDao.selectUserPostOrgIds(param);
			param.put("parentId", null);
			param.put("ids", ids);
			if(ids!=null&&ids.size()>0){
				list=orgnazationDao.selectChildOrgList(param);
				res.put(userIds[i],list);
			}else{
				res.put(userIds[i],new ArrayList<>());
			}
		}
		return res;
	}

	/**
	 * 获取用户直属组织(集团/公司/部门/项目/分期)
	 * @param paramJson:{userIds:"指用户Ids"}
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getUserOrgnazationList(Map<String, Object> param)throws Exception{
		String[] userIds=param.get("userIds").toString().split(",");
		Map<String,Object> res=new HashMap<String,Object>();
		param.put("ids", userIds);
		String orgId=null;
		List<Map<String,Object>> list=orgnazationDao.selectUserOrgnazationList(param);
		for (int i = 0; i < list.size();i++) {
			Map<String,Object> org=list.get(i);
			orgId=org.get("uId").toString();
			org.remove("uId");
			res.put(orgId, org);
		}
		return res;
	}

	/**
	 * 获取指定岗位的组织信息
	 * @param postIds:"指用户Ids" 
	 * @throws Exception
	 */
	public Map<String,Object> getOrgRelationByPostId(Map<String, Object> param)throws Exception{
		String[] postIds=param.get("postIds").toString().split(",");
		Map<String,Object> res=new HashMap<String,Object>();
		String orgId=null;
		for (int i = 0; i < postIds.length;i++) {
			Post post=postDao.getObjectById(postIds[i]);
			param.put("orgId", post.getRefId());
			AuthenticationDto auth=new AuthenticationDto();
			getOrgInfo(auth, param);
			res.put(postIds[i],auth);
		}
		return res;
	}

	/**
	 * 根据组织id查询组织dto：公司/部门/项目/分期
	 * @param userJson
	 * @param paramJson:{orgIds:"组织ids"}
	 */
	public Map<String,Object> getOrgDtoByOrgIds(Map<String, Object> param)throws Exception{
		String[] orgIds=param.get("orgIds").toString().split(",");
		Map<String,Object> res=new HashMap<String,Object>();
		param.put("ids",orgIds);
		List<OrgnazationDto> list=orgnazationDao.selectOrgDtoByOrgIds(param);
		for (int i = 0; i < list.size(); i++) {
			res.put(list.get(i).getId(), list.get(i));
		}
		return res;
	}

	/**
	 * 获取所有组织机构
	 * @param userJson
	 */
	public List<OrgnazationDto> getAllOrgList(Map<String, Object> param)throws Exception{
		return orgnazationDao.selectOrgDtoByOrgIds(param);
	}
	/**
	 * 获取用户主岗组织信息
	 * @param userJson
	 */
	@Override
	public Map<String, Object> getMainOrgRelationByUserId(Map<String, Object> param) throws Exception {
		String[] userIds=param.get("userIds").toString().split(",");
		Map<String,Object> res=new HashMap<String,Object>();
		for (int i = 0; i < userIds.length; i++) {
			param.put("userId",userIds[i]);
			//获取用户主岗对应组织
			String postOrg=postService.getDefaultPostOrg(param);
			if (StringUtils.isNotBlank(postOrg)) {
				param.put("orgId", postOrg);
				AuthenticationDto authenticationDto=new AuthenticationDto();
				getOrgInfo(authenticationDto,param);
				res.put(userIds[i], authenticationDto);
			}else {
				res.put(userIds[i], null);
			}
		}
		return res;
	}

	/**
	 * 获取上级组织信息
	 * @param userJson
	 */
	@Override
	public Map<String,Object> getParentOrgByOrgId(Map<String, Object> param)throws Exception{
		String[] orgIds=param.get("orgIds").toString().split(",");
		Map<String,Object> res=new HashMap<String,Object>();
		param.put("ids", orgIds);
		List<Map<String,Object>> list=orgnazationDao.selectParentOrgByOrgId(param);
		for (int i = 0; i < list.size(); i++) {
			Map<String,Object> map=list.get(i);
			OrgnazationDto dto=new OrgnazationDto();
			BeanUtils.populate(dto, map);
			res.put(map.get("key").toString(),dto );
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.org.service.OrgnazationService#queryListCompanyAndZb()
	 */
	@Override
	public List<Orgnazation> queryListCompanyAndZb() throws Exception {
		return orgnazationDao.queryListCompanyAndZb();
	}
	/**
	 * 获取用户授权公司、部门
	 * @param param
	 * @return
	 */
	@Override
	public Map<String,UserAuthDataOrgList> getUserDataAuthCoAndDeptList(Map<String, Object> param) throws Exception {
		String userIds[]=param.get("userIds").toString().split(",");
		String appId=appSystemDao.selectAppIdByCode(param);
		if(appId==null||StringUtils.isBlank(appId)){
			throw new InvalidCustomException("系统不存在");
		}
		String itemCode=(String)param.get("itemCode");
		if(itemCode==null ||StringUtils.isBlank(itemCode)){
			itemCode="dept";
		}
		Map<String,UserAuthDataOrgList> res=new HashMap<String, UserAuthDataOrgList>();
		for (int i = 0; i < userIds.length; i++) {
			UserAuthDataOrgList auth=getAuthOrgIds(userIds[i], appId,1,itemCode,param);
			res.put(userIds[i], auth);
		}
		return res;
	}
	/**
	 * 获取用户授权组织
	 * @param param
	 * @return
	 */
	@Override
	public Map<String,UserAuthDataOrgList> getUserDataAuthGroupAndBranchList(Map<String, Object> param) throws Exception {
		String userIds[]=param.get("userIds").toString().split(",");
		String appId=appSystemDao.selectAppIdByCode(param);
		if(appId==null||StringUtils.isBlank(appId)){
			throw new InvalidCustomException("系统不存在");
		}
		String itemCode=(String)param.get("itemCode");
		if(itemCode==null ||StringUtils.isBlank(itemCode)){
			itemCode="group";
		}
		Map<String,UserAuthDataOrgList> res=new HashMap<String, UserAuthDataOrgList>();
		for (int i = 0; i < userIds.length; i++) {
			UserAuthDataOrgList auth=getAuthOrgIds(userIds[i], appId,2,itemCode,param);
			res.put(userIds[i], auth);
		}
		return res;
	}
	/**
	 * 获取授权公司/部门ids
	 * @param isLeaf:true(如果true,数据授权所有公司，如果是false，数据授权直属公司
	 * @param appId:系统id
	 * @param qType:1公司、项目 ，2项目、分期
	 * @return
	 * @throws Exception 
	 */
	public UserAuthDataOrgList getAuthOrgIds(String userId,String appId,int qType,String itemCode,Map<String, Object> map) throws Exception{
		try {
			UserAuthDataOrgList auth=new UserAuthDataOrgList();
			List<String> authIds=new LinkedList<String>();
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("appId", appId);
			param.put("itemCode", itemCode);
			/*//查询用户所有角色
			List<StandardRoleDto> rList=standardRoleDao.queryRoleListByUserId(param);*/
			//查询用户四种授权id：用户，标准岗位，岗位，通用角色
			List<String> roleIds=funcPermissionDao.selectAuthTypeId(param);
			/*
			for (int i = 0; i < rList.size(); i++) {
				roleIds.add(rList.get(i).getId());
			}*/
			param.put("roleIds", roleIds);
			if(roleIds==null||roleIds.size()==0){
				return auth;
			}
			//查询所有角色授权控制点
			String authDdataPoint=funcPermissionDao.selectUserRoleAuthDataCode(param);
			if (authDdataPoint==null||StringUtils.isBlank(authDdataPoint)) {
				return auth;
				/*//未授权时默认本部门
				authDdataPoint="thisDept";*/
			}
			//根据授权情况获取授权公司
			//全公司
			if(authDdataPoint.contains("allOrg")){
				if(qType==1){
					//获取授权公司、部门
					param.put("orgType"," in ('company','zb','dept')"); 
				}else{
					//获取授权项目、分期
//					param.put("orgType"," in ('group','branch')"); 
					param.put("orgType"," !='dept'"); 
				}
				if(map.get("keyWord") != null){
					param.put("keyWord", map.get("keyWord"));
				}
				List<OrgnazationDto> list=orgnazationDao.selectOrgDtoByOrgIds(param);
				for (OrgnazationDto dto:list) {
					if (dto.getType().equals("zb")||dto.getType().equals("company")) {
						auth.getCompanyList().add(dto);
					}else if(dto.getType().equals("dept")){
						auth.getDeptList().add(dto);
					}else if(dto.getType().equals("group")){
						auth.getGroupList().add(dto);
					}else if(dto.getType().equals("branch")){
						auth.getBranchList().add(dto);
					}
				}
			}else {
				//查询用户所有组织id=用户所属组织+用户岗位组织
				List<OrgnazationDto> userOrgs=orgnazationDao.selectUserAllOrgs(param);
				if (qType==1) {
					if (authDdataPoint.contains("thisCompany")) {
						//所有本公司id
						String[] orgArr=new String[userOrgs.size()];
						for (int i = 0; i < userOrgs.size(); i++) {
							param.put("orgId", userOrgs.get(i).getId());
							param.put("topType", "  in ('zb','company')");//上级公司
							//上级组织结构
							List<OrgnazationDto> upOrgs=orgnazationDao.queryAuthOrgListByOrgId(param);
							if (upOrgs!=null&&upOrgs.size()>0) {
								String orgId=upOrgs.get(upOrgs.size()-1).getId();//本公司(直属公司)
								orgArr[i]=orgId;
								String[] upids=upOrgs.get(upOrgs.size()-1).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}else {
								orgArr[i]=userOrgs.get(i).getId();
								String[] upids=userOrgs.get(i).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}
						}
						param.put("orgArr", orgArr);
						param.put("orgType"," in ('company','zb','dept')"); 
						//授权的公司和部门
						List<String> upOrgs=orgnazationDao.getAuthOrgIds(param);
						if (upOrgs!=null&&upOrgs.size()>0) {
							authIds.addAll(upOrgs);
						}
					}else if(authDdataPoint.contains("topDept")){
						//所有顶级部门id
						String[] orgArr=new String[userOrgs.size()];
						for (int i = 0; i < userOrgs.size(); i++) {
							param.put("orgId", userOrgs.get(i).getId());
							param.put("topType", " ='dept'");//顶级部门
							//上级组织结构
							List<OrgnazationDto> upOrgs=orgnazationDao.queryAuthOrgListByOrgId(param);
							if (upOrgs!=null&&upOrgs.size()>0) {
								String orgId=upOrgs.get(0).getId();//顶级部门
								orgArr[i]=orgId;
								String[] upids=upOrgs.get(0).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}else {
								orgArr[i]=userOrgs.get(i).getId();
								String[] upids=userOrgs.get(i).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}
						}
						param.put("orgArr", orgArr);
						param.put("orgType"," in ('company','zb','dept')"); 
						//授权的公司和部门
						List<String> upOrgs=orgnazationDao.getAuthOrgIds(param);
						if (upOrgs!=null&&upOrgs.size()>0) {
							authIds.addAll(upOrgs);
						}
					}else if (authDdataPoint.contains("thisDept")) {
						//所有本部门id
						String[] orgArr=new String[userOrgs.size()];
						for (int i = 0; i < userOrgs.size(); i++) {
							param.put("orgId", userOrgs.get(i).getId());
							param.put("topType", " ='dept'");//上级部门
							//上级组织结构
							List<OrgnazationDto> upOrgs=orgnazationDao.queryAuthOrgListByOrgId(param);
							if (upOrgs!=null&&upOrgs.size()>0) {
								String orgId=upOrgs.get(upOrgs.size()-1).getId();//本部门
								orgArr[i]=orgId;
								String[] upids=upOrgs.get(upOrgs.size()-1).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}else {
								orgArr[i]=userOrgs.get(i).getId();
								String[] upids=userOrgs.get(i).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}
						}
						param.put("orgArr", orgArr);
						param.put("orgType"," in ('company','zb','dept')"); 
						//授权的公司和部门
						List<String> upOrgs=orgnazationDao.getAuthOrgIds(param);
						if (upOrgs!=null&&upOrgs.size()>0) {
							authIds.addAll(upOrgs);
						}
					}else if (authDdataPoint.contains("thisMyself")) {
						//如果配置的有仅本人，看看是否有指定组织，如果没有指定组织，直接返回空，和不配置任何数据权限一致
						if (authDdataPoint.contains("designOrg")) {
							//有指定组织，这里不做任何处理，后面有合并指定的处理
						}else{
							//只有仅本人，返回空，和不配置任何数据权限一致
							return auth;
						}
					}
				}else{
					//TODO
					if (authDdataPoint.contains("thisCompany")) {
						//所有本公司id
						String[] orgArr=new String[userOrgs.size()];
						for (int i = 0; i < userOrgs.size(); i++) {
							param.put("orgId", userOrgs.get(i).getId());
							param.put("topType", "  in ('zb','company')");//上级公司
							//上级组织结构
							List<OrgnazationDto> upOrgs=orgnazationDao.queryAuthOrgListByOrgId(param);
							if (upOrgs!=null&&upOrgs.size()>0) {
								String orgId=upOrgs.get(upOrgs.size()-1).getId();//本公司(直属公司)
								orgArr[i]=orgId;
								String[] upids=upOrgs.get(upOrgs.size()-1).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}else {
								orgArr[i]=userOrgs.get(i).getId();
								String[] upids=userOrgs.get(i).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}
						}
						param.put("orgArr", orgArr);
//						param.put("orgType"," in ('group','branch')");
						param.put("orgType"," !='dept'"); 
						//授权的项目和分期
						List<String> upOrgs=orgnazationDao.getAuthOrgIds(param);
						if (upOrgs!=null&&upOrgs.size()>0) {
							authIds.addAll(upOrgs);
						}
					}else if(authDdataPoint.contains("thisGroup")){
						//所有本项目id
						String[] orgArr=new String[userOrgs.size()];
						for (int i = 0; i < userOrgs.size(); i++) {
							param.put("orgId", userOrgs.get(i).getId());
							param.put("topType", " ='group'");//直属项目
							//上级组织结构
							List<OrgnazationDto> upOrgs=orgnazationDao.queryAuthOrgListByOrgId(param);
							if (upOrgs!=null&&upOrgs.size()>0) {
								String orgId=upOrgs.get(0).getId();//本项目
								orgArr[i]=orgId;
								String[] upids=userOrgs.get(i).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}else {
								orgArr[i]=userOrgs.get(i).getId();
								String[] upids=userOrgs.get(i).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}
						}
						param.put("orgArr", orgArr);
//						param.put("orgType"," in ('group','branch')"); 
						param.put("orgType"," !='dept'"); 
						//授权的项目和分期
						List<String> upOrgs=orgnazationDao.getAuthOrgIds(param);
						if (upOrgs!=null&&upOrgs.size()>0) {
							authIds.addAll(upOrgs);
						}
					}else if (authDdataPoint.contains("thisBranch")) {
						//所有本分期id
						String[] orgArr=new String[userOrgs.size()];
						for (int i = 0; i < userOrgs.size(); i++) {
							param.put("orgId", userOrgs.get(i).getId());
							param.put("topType", " ='branch'");//上级分期
							//上级组织结构
							List<OrgnazationDto> upOrgs=orgnazationDao.queryAuthOrgListByOrgId(param);
							if (upOrgs!=null&&upOrgs.size()>0) {
								String orgId=upOrgs.get(upOrgs.size()-1).getId();//本分期
								orgArr[i]=orgId;
								String[] upids=userOrgs.get(i).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}else {
								orgArr[i]=userOrgs.get(i).getId();
								String[] upids=userOrgs.get(i).getPrefixId().split("/");
								authIds.addAll(Arrays.asList(upids));
							}
						}
						param.put("orgArr", orgArr);
//						param.put("orgType"," in ('group','branch')"); 
						param.put("orgType"," !='dept'"); 
						//授权的项目和分期
						List<String> upOrgs=orgnazationDao.getAuthOrgIds(param);
						if (upOrgs!=null&&upOrgs.size()>0) {
							authIds.addAll(upOrgs);
						}
					}else if (authDdataPoint.contains("thisMyself")) {
						//如果配置的有仅本人，看看是否有指定组织，如果没有指定组织，直接返回空，和不配置任何数据权限一致
						if (authDdataPoint.contains("designOrg")) {
							//有指定组织，这里不做任何处理，后面有合并指定的处理
						}else{
							//只有仅本人，返回空，和不配置任何数据权限一致
							return auth;
						}
					}
				}

				if (authDdataPoint.contains("designOrg")) {
					//获取指定授权的授权值
					List<String> upOrgs=funcPermissionDao.selectAuthValIds(param);
					if (upOrgs!=null&&upOrgs.size()>0) {
						authIds.addAll(upOrgs);
					}
				}
				//去除重复公司id
				Set<String> setIds = new HashSet<String>(authIds); 
				Object[] ids=setIds.toArray();
				param.put("ids", ids);
				if(map.get("keyWord") != null){
					param.put("keyWord", map.get("keyWord"));
				}
				List<OrgnazationDto> list=orgnazationDao.selectOrgDtoByOrgIds(param);
				for (OrgnazationDto dto:list) {
					if (dto.getType().equals("zb")||dto.getType().equals("company")) {
						auth.getCompanyList().add(dto);
					}else if(dto.getType().equals("dept")){
						auth.getDeptList().add(dto);
					}else if(dto.getType().equals("group")){
						auth.getGroupList().add(dto);
					}else if(dto.getType().equals("branch")){
						auth.getBranchList().add(dto);
					}
				}
			}
			return auth;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 公司岗：公司
	 * 部门岗：公司+部门
	 * 项目岗：公司+项目
	 * 分期岗：公司+项目+分期
	 */
	@Override
	public List<OrgnazationDto> getOrgsByPostId(Map<String, Object> param) throws Exception {
		String postId=param.get("postId").toString();
		Post post=postService.getObjectById(postId);
		String orgId=post.getRefId();
		param.put("orgId", orgId);
		
		List<OrgnazationDto> res=new ArrayList<OrgnazationDto>();
				
		//上级组织结构
		List<OrgnazationDto> upOrgs=orgnazationDao.queryAuthOrgListByOrgId(param);
		List<OrgnazationDto> cos=new ArrayList<OrgnazationDto>();
		List<OrgnazationDto> depts=new ArrayList<OrgnazationDto>();
		List<OrgnazationDto> groups=new ArrayList<OrgnazationDto>();
		List<OrgnazationDto> branchs=new ArrayList<OrgnazationDto>();
		for (OrgnazationDto dto:upOrgs) {
			if ("zb".equals(dto.getType())||"company".equals(dto.getType())) {
				cos.add(dto);
			}else if("dept".equals(dto.getType())){
				depts.add(dto);
			}else if("group".equals(dto.getType())){
				groups.add(dto);
			}else if("branch".equals(dto.getType())){
				branchs.add(dto);
			}
		}
		if (cos.size()>0) {
			res.add(cos.get(cos.size()-1));
		}
		if (depts.size()>0) {
			res.add(depts.get(depts.size()-1));
		}
		if (groups.size()>0) {
			res.add(groups.get(groups.size()-1));
		}
		if (branchs.size()>0) {
			res.add(branchs.get(branchs.size()-1));
		}
		
		return res;
	
	}
	//复制粘贴组织结构
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Map<String, Orgnazation> saveCopyAndPasteOrg(Map<String, Object> param) throws Exception {
		
		try {
			String copyId=param.get("copyId").toString();//复制组织id
			String pasteId=null;//粘贴组织id
			String pastePrefixId="";//粘贴组织全路径
			String pastePrefixName="";
			if(param.containsKey("pasteId")&&param.get("pasteId")!=null&&StringUtils.isNotBlank(param.get("pasteId").toString())){
				pasteId=param.get("pasteId").toString();
				Orgnazation pasteOrg=orgnazationDao.getObjectById(pasteId);//目标组织
				pastePrefixId=pasteOrg.getPrefixId()+"/";
				pastePrefixName=pasteOrg.getPrefixName()+"/";
			}
			
			param.put("orgId", copyId);
			List<Orgnazation> copyOrgs=orgnazationDao.selectSunOrgByOrgId(param);//要复制的组织结构
			//老id与新组织映射关系，通过老id找到新组织
			Map<String, Orgnazation> oldNewMap=new HashMap<String, Orgnazation>();
			
			//复制出的组织结构
			List<Orgnazation> newOrgs=new ArrayList<Orgnazation>();
			String newId=null;//新id
			Random random=new Random();
			Orgnazation parentOrg=null;
			String oldId=null;
			int end=0;
			String code=null;
			for (int i = 0; i < copyOrgs.size(); i++) {
				Orgnazation oldOrg=copyOrgs.get(i);
				Orgnazation newOrg=copyOrgs.get(i);
				newId=IDGenerator.getUUID();
				oldId=oldOrg.getId();
				if (i==0) {
					newOrg.setId(newId);
					end=oldOrg.getCode().indexOf("_copy")==-1 ? oldOrg.getCode().length():oldOrg.getCode().indexOf("_copy");
					code=oldOrg.getCode().substring(0, end) +"_copy_"+random.nextInt();
					if(code.length()>220){
						throw new  Exception("编码长度超过限制");
					}
					newOrg.setCode(code);
					newOrg.setParentId(pasteId);
					newOrg.setPrefixId(pastePrefixId+oldOrg.getId());
					newOrg.setPrefixName(pastePrefixName+oldOrg.getName());
				}else{
					newOrg.setId(newId);
					/***********************下级节点的code不用加copy***************************/
					/*end=oldOrg.getCode().indexOf("_copy")==-1 ? oldOrg.getCode().length():oldOrg.getCode().indexOf("_copy");
					code=oldOrg.getCode().substring(0, end) +"_copy_"+random.nextInt();
					newOrg.setCode(code);
					if(code.length()>220){
						throw new  Exception("编码长度超过限制");
					}*/
					parentOrg=oldNewMap.get(oldOrg.getParentId());
					newOrg.setParentId(parentOrg.getId());
					newOrg.setPrefixId(parentOrg.getPrefixId()+"/"+oldOrg.getId());
					newOrg.setPrefixName(parentOrg.getPrefixName()+"/"+oldOrg.getName());
				}
				oldNewMap.put(oldId, newOrg);
				newOrgs.add(newOrg);
			}
			//保存
			orgnazationDao.saveBatch(newOrgs);
			return oldNewMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/**
	 * 删除符合条件（没有被引用）的组织及其下级
	 */
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Integer deleteOrgAllSon(Orgnazation orgnazation) throws Exception {
		//查询组织及其下级是否存在岗位，如果有不让删除
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orgId", orgnazation.getId());
		map.put("delflag", "0");
		Integer pcount = postDao.selectSonRefCount(map);
		//查询组织及其下级下是否存在用户，如果有不让删除
		Integer ucount = userDao.selectSonRefCount(map);
		if(null!=pcount && pcount>0){
			throw new InvalidCustomException("该组织或其下级下存在岗位，不可删除");
		}
		if(null!=ucount && ucount>0){
			throw new InvalidCustomException("该组织或其下级下存在用户，不可删除");
		}
		List<Orgnazation> list=orgnazationDao.selectSunOrgByOrgId(map);
		List<String> ids=new ArrayList<String>();
		for (Orgnazation org : list) {
			ids.add(org.getId());
		}
		return deleteAllObjectByIds(ids);
	}
	//获取公司下级组织
	@Override
	public List<Map<String, Object>> getSubOrgByComId(Map<String, Object> param) throws Exception {
		return orgnazationDao.getSubOrgByComId(param);
	}
	//获取公司下级分期
	@Override
	public List<Map<String, Object>> getSubBranchByComId(Map<String, Object> param) throws Exception {
		return orgnazationDao.getSubBranchByComId(param);
	}
	//获取公司下级用户
	@Override
	public List<Map<String, Object>> getSubUserByComId(Map<String, Object> param) throws Exception {
		return orgnazationDao.getSubUserByComId(param);
	}
	/**
	 * 根据用户获取其所有组织的顶级部门和顶级公司
	 * add by gyh 2017-7-14
	 * @param param：用户id
	 */
	@Override
	public List<Map<String,Object>> getTopDeptAnaTopComByUser(Map<String, Object> param)throws Exception{
		List<Map<String,Object>> res=new ArrayList<>();
		//查询用户所有组织id=用户所属组织+用户岗位组织
		List<OrgnazationDto> userOrgs=orgnazationDao.selectUserAllOrgs(param);
		List<String> orgIds=new ArrayList<>();
		if(userOrgs==null||userOrgs.size()==0){
			return res;
		}
		for (OrgnazationDto orgnazationDto : userOrgs) {
			orgIds.add(orgnazationDto.getId());
		}
		param.put("orgIds", orgIds);
		List<Map<String, Object>> tops=orgnazationDao.selectTopDeptAnaTopCom(param);
		for (int i=0;i<tops.size()-1;i++) {
			Map<String, Object> map=new HashMap<String, Object>();
			Map<String, Object> topComp=tops.get(i);
			i++;
			Map<String, Object> topDept=tops.get(i);
			
			map.put("topDeptId", topDept.get("id"));
			map.put("topDeptName", topDept.get("name"));
			map.put("topDeptAllName", topDept.get("prefix_name"));
			map.put("topCompId", topComp.get("id"));
			map.put("topCompName", topComp.get("name"));
			res.add(map);
		}
		return res;
	}
	/**
	 * 根据用户获取其所有组织的直属部门和直属公司
	 * add by gyh 2017-8-4
	 * @param param：用户id
	 */
	@Override
	public List<Map<String,Object>> getDirectDeptAnaDirectComByUser(Map<String, Object> param)throws Exception{
		List<Map<String,Object>> res=new ArrayList<>();
		//查询用户所有组织id=用户所属组织+用户岗位组织
		List<OrgnazationDto> userOrgs=orgnazationDao.selectUserAllOrgs(param);
		List<String> orgIds=new ArrayList<>();
		if(userOrgs==null||userOrgs.size()==0){
			return res;
		}
		for (OrgnazationDto orgnazationDto : userOrgs) {
			orgIds.add(orgnazationDto.getId());
		}
		param.put("orgIds", orgIds);
		param.put("orderBy", "desc");
		List<Map<String, Object>> orgs=orgnazationDao.selectDirectDeptAnaTopCom(param);
		Set<String> sidSet=new HashSet<>();
		Map<String, Integer> sidMap=new HashMap<>();
		Map<String, Map<String, Object>> sidMap_=new HashMap<>();
		String sid=null;
		String type=null;
		for (int i=0;i<orgs.size();i++) {
			Map<String, Object> org = orgs.get(i);
			sid = org.get("Sid").toString();
			type = org.get("type").toString();
			if(sidSet.add(sid)){//第一次，说明是直属部门
				if("dept".equals(type) && sidMap.get(sid)==null){
					Map<String, Object> map = new HashMap<String, Object>();
					sidMap.put(sid, 1);
					sidMap_.put(sid, map);
					map.put("directDeptId", org.get("id"));
					map.put("directDeptName", org.get("name"));
					map.put("directDeptAllName", org.get("prefix_name"));
//					res.add(map);
				}
			}else{//已存在，寻找第一个公司，即直属公司
				if("company".equals(type) && 1 == sidMap.get(sid)){
					Map<String, Object> map = sidMap_.get(sid);
					map.put("directCompId", org.get("id"));
					map.put("directCompName", org.get("name"));
					sidMap.put(sid, 2);
				}
			}
		}
		Set<String> temp = new HashSet<String>();
		for (int i = 0; i < orgIds.size(); i++) {
			String oid = orgIds.get(i);
			if(temp.add(oid)){//去重
				if(sidMap_.containsKey(oid)){
					res.add(sidMap_.get(oid));
				}
			}
		}
/*		for (int i=0;i<tops.size()-1;i++) {
			Map<String, Object> map=new HashMap<String, Object>();
			Map<String, Object> topComp=tops.get(i);
			i++;
			Map<String, Object> topDept=tops.get(i);
			
			map.put("directDeptId", topDept.get("id"));
			map.put("directDeptName", topDept.get("name"));
			map.put("directDeptAllName", topDept.get("prefix_name"));
			map.put("directCompId", topComp.get("id"));
			map.put("directCompName", topComp.get("name"));
			res.add(map);
		}
*/		return res;
	}

	/**
	 * 查找用户直属部门和直属公司
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String,Object> queryUserDirectDeptAndDirectCom(Map<String, Object> param)throws Exception{
		if(param.get("userIds") == null){
			throw new InvalidCustomException("userIds不可为空");
		}
		Map<String,Object> res = new HashMap<>();
		List<Map<String, Object>> orgs = orgnazationDao.selectUserOrgAndUp(param);
		String userId = null;
		String type = null;
		for (int i = 0; i < orgs.size(); i++) {
			Map<String, Object> org = orgs.get(i);
			userId = org.get("userId").toString();
			type = org.get("type").toString();
			Map<String, Object> direct = (Map<String, Object>) res.get(userId);
			if(direct == null){
				direct = new HashMap<>();
			}
			if("dept".equals(type)){
				direct.put("deptId",org.get("id"));
				direct.put("deptName",org.get("prefixName"));
			}else{
				direct.put("comId",org.get("id"));
				direct.put("comName",org.get("prefixName"));
			}
			res.put(userId,direct);
		}
		return res;
	}
	/**
	 * 读excel并插入db，校验数据的合法性，插入数据库，并返回结果
	 * add by gyh 2018-1-12
	 * @param list:读取excel组织数据
	 */
	@Override
	public Map<String,Object> readExcelAndInsert(List<OrgnazationExcelDto> list, String parentId)throws Exception{
		Long t1 = System.currentTimeMillis();
		Orgnazation parentOrg = orgnazationDao.getObjectById(parentId);
		Long t2 = System.currentTimeMillis();
		log.info("getObjectById...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(t2-t1));
		if(parentOrg == null){
			return null;
		}
		//过滤掉不合法的数据：code或name为空、code重复、code父节点不存在
		//合法数据
		List<OrgnazationExcelDto> rightList = new ArrayList<>(list.size());
		//非法数据：编码为空或不合法，或编码重复
		List<OrgnazationExcelDto> wrongCodeList = new ArrayList<>();
		//非法数据：名称为空，或字段长度不合法
		List<OrgnazationExcelDto> wrongNameList = new ArrayList<>();
		//非法数据：父节点不存在
		List<OrgnazationExcelDto> wrongParentList = new ArrayList<>();
		//合法数据 code<->org 映射
		Map<String,OrgnazationExcelDto> rightMap = new HashMap<>();
		String code = null;
		String pCode = null;
		for (int i = 0; i < list.size(); i++) {
			OrgnazationExcelDto org = list.get(i);
			if(org == null){
				continue;
			}
			//编码为空
			if (StringUtils.isBlank(org.getCode())){
				wrongCodeList.add(org);
				continue;
			}
			//名称为空
			if (StringUtils.isBlank(org.getName())){
				wrongNameList.add(org);
				continue;
			}
			//长度校验
			if(org.getName().length() > 64){
				wrongNameList.add(org);
				continue;
			}
			code = org.getCode();
			//长度校验
			if(code.length() > 64){
				wrongCodeList.add(org);
				continue;
			}
			//编码不合法
			if(code.equals(".")){
				wrongCodeList.add(org);
				continue;
			}
			//编码重复
			if(rightMap.containsKey(code)){
				wrongCodeList.add(org);
				continue;
			}
			//校验fullName长度
			if(StringUtils.isBlank(org.getFullName()) && org.getFullName().length() > 100){
				wrongNameList.add(org);
				continue;
			}
			//校验remark长度
			if(StringUtils.isBlank(org.getRemark()) && org.getRemark().length() > 2000){
				wrongNameList.add(org);
				continue;
			}
			//编码父节点不存在
			/*if(code.indexOf(".") > 0 ){
				pCode = code.substring(0,code.lastIndexOf("."));
				if(!rightMap.containsKey(pCode)){
					wrongParentList.add(org);
					continue;
				}
			}*/
			rightMap.put(code,org);
			rightList.add(org);
		}
		Long t3 = System.currentTimeMillis();
		log.info("check wrong data...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(t3-t2));
		//查询数据库中已存在的code，导入excel编码全局不可重复
		Map<String,Object> dbParam = new HashMap<>();
		dbParam.put("codes",rightMap.keySet());
		List<String> dbCodes = new ArrayList<>();
		if(rightMap.size()>0){
			dbCodes = orgnazationDao.selectDbCodes(dbParam);
		}
		Long t4 = System.currentTimeMillis();
		log.info("selectDbCodes...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(t4-t3));
		for (int i = 0; i < dbCodes.size(); i++) {
			if(rightMap.containsKey(dbCodes.get(i))){
				rightMap.remove(dbCodes.get(i));
			}
		}
		Long t5 = System.currentTimeMillis();
		log.info("remove dbCode...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(t5-t4));
		String id = null;
		String prefixId = parentOrg.getPrefixId();
		String prefixName = parentOrg.getPrefixName();
		String parentId_ = null;
		String prefixId_ = null;
		String prefixName_ = null;
		OrgnazationExcelDto pOrg = null;
		LinkedList<OrgnazationExcelDto> temp = new LinkedList<>();
		for (int i = 0; i < rightList.size(); i++) {
			OrgnazationExcelDto org = rightList.get(i);
			code = org.getCode();
			if(dbCodes.contains(code)){
				wrongCodeList.add(org);
				continue;
			}
			//编码父节点不存在
			if(code.indexOf(".") > 0 ){
				pCode = code.substring(0,code.lastIndexOf("."));
				if(!rightMap.containsKey(pCode) || rightMap.get(pCode) == null){
					/*wrongParentList.add(org);
					continue;*/
					id = IDGenerator.getUUID();
					parentId_ = parentId;
					prefixId_ = prefixId + "/" + id;
					prefixName_ = prefixName + "/" + org.getName();
				}else{
					id = IDGenerator.getUUID();
					pOrg = rightMap.get(pCode);
					//父节点id还未赋值，放到temp集合中，等待赋值
					if(pOrg.getParentId() == null){
						temp.add(org);
						parentId_ = null;
						prefixId_ = null;
						prefixName_ = null;
					}else{
						parentId_ = pOrg.getId();
						prefixId_ = pOrg.getPrefixId() + "/" + id;
						prefixName_ = pOrg.getPrefixName() + "/" + org.getName();
					}
				}
			}else{
				id = IDGenerator.getUUID();
				parentId_ = parentId;
				prefixId_ = prefixId + "/" + id;
				prefixName_ = prefixName + "/" + org.getName();
			}
			org.setId(id);
			org.setParentId(parentId_);
			org.setPrefixId(prefixId_);
			org.setPrefixName(prefixName_);
			org.setSort(0L+i);
			org.setType("dept");
		}
		Long t6 = System.currentTimeMillis();
		log.info("remove dbCode sons ...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(t6-t5));
		while (temp.size()>0){
			OrgnazationExcelDto tempOrg = temp.getFirst();
			code = tempOrg.getCode();
			pCode = code.substring(0,code.lastIndexOf("."));
			pOrg = rightMap.get(pCode);
			//父节点id还未赋值，放到temp末尾
			if(pOrg.getParentId() == null){
				temp.remove(tempOrg);
				temp.add(tempOrg);
			}else{
				parentId_ = pOrg.getId();
				prefixId_ = pOrg.getPrefixId() + "/" + id;
				prefixName_ = pOrg.getPrefixName() + "/" + tempOrg.getName();
				tempOrg.setParentId(parentId_);
				tempOrg.setPrefixId(prefixId_);
				tempOrg.setPrefixName(prefixName_);
				temp.remove(tempOrg);
			}
		}
		Long t7 = System.currentTimeMillis();
		log.info("query temp parent ...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(t7-t6));
		//去除所有不合法数据
		rightList.removeAll(wrongCodeList);
		rightList.removeAll(wrongParentList);
		Integer insertCount = 0;
		if(rightList.size() > 0){
			SecurityUserDto loginUser = LoginUtils.getSecurityUserBeanInfo().getSecurityUserDto();
			dbParam.put("orgs",rightList);
			dbParam.put("rootId",parentOrg.getRootId());
			dbParam.put("loginUserId",loginUser.getId());
			dbParam.put("loginUserName",loginUser.getRealName());
			dbParam.put("createDate",new Date());
			insertCount = orgnazationDao.insertExcelData(dbParam);
			Long t8 = System.currentTimeMillis();
			log.info("insert data ...>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+(t8-t7));
		}
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("insertCount",insertCount);
		if(insertCount<=200){
			resMap.put("right",rightList);
		}
		resMap.put("wrongCode",wrongCodeList);
		resMap.put("wrongName",wrongNameList);
		resMap.put("wrongParent",wrongParentList);
		return resMap;
	}
	@Override
	public int checkCode(Map<String, Object> param)throws Exception{
		return orgnazationDao.checkCode(param);
	}
}

