package com.xinleju.platform.sys.res.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xinleju.platform.sys.org.dao.OrgnazationDao;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.sys.org.dto.PostDto;
import com.xinleju.platform.sys.org.dto.StandardRoleDto;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.service.PostService;
import com.xinleju.platform.sys.org.service.StandardRoleService;
import com.xinleju.platform.sys.org.service.UserService;
import com.xinleju.platform.sys.res.dao.FuncPermissionDao;
import com.xinleju.platform.sys.res.dto.ResourceDto;
import com.xinleju.platform.sys.res.entity.FuncPermission;
import com.xinleju.platform.sys.res.service.AppSystemService;
import com.xinleju.platform.sys.res.service.FuncPermissionService;
import com.xinleju.platform.sys.res.utils.UserType;

/**
 * @author admin
 * 
 * 
 */

@Service
public class FuncPermissionServiceImpl extends  BaseServiceImpl<String,FuncPermission> implements FuncPermissionService{
	

	@Autowired
	private FuncPermissionDao funcPermissionDao;
	@Autowired
	private OrgnazationDao orgnazationDao;
	@Autowired
	private AppSystemService appSystemService;
	@Autowired
	private StandardRoleService standardRoleService;
	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;
	
	@Override
	public List<Map<String,Object>> querySystemListByRole(Map<String,Object> map) throws Exception {
	   String userType = (String)map.get("type");
	   String roleId = (String)map.get("roleId");
		if(UserType.systemUser.getCode().equals(userType)){//超级管理员 查询全部
			return appSystemService.querySystemList(userType);
		}else if(UserType.adminUser.getCode().equals(userType)){//管理员查询 应用的业务系统
			return appSystemService.querySystemList(userType);
		}else if(UserType.user.getCode().equals(userType)){//普通员工
			return funcPermissionDao.querySystemListByRole(roleId);
		}else{
			return null;
		}
	}
	
	@Override
	public List<Map<String,Object>> queryAuthorizationListByAppIds(Map<String,Object> map) throws Exception {
		String appIds = (String)map.get("appIds");
		if("".equals(appIds)){
			return null;
		}else{
			String[] appIdsList = appIds.split(",");
			Map<String,Object> mapCon = new HashMap<String,Object>();
			mapCon.put("ids", appIdsList);
			List<Map<String,Object>> list= funcPermissionDao.queryAuthorizationListByAppIdsPermission(mapCon);
			List<Map<String,Object>> listreturn= new ArrayList<Map<String,Object>>();
			for(int i=0;i<list.size();i++){
				Map<String,Object> mapObject = list.get(i);
				String prefixSort = (String)mapObject.get("prefix_sort");
				String[] prefixSorts = prefixSort.split("-");
				mapObject.put("level", prefixSorts.length);
				mapObject.put("expanded", false);
				mapObject.put("loaded", true);
				if(null == mapObject.get("parentId") || "".equals(mapObject.get("parentId").toString())){
					mapObject.put("parentId", mapObject.get("pId"));
				}
				listreturn.add(mapObject);
			}
			return listreturn;
		}
		
	}
	
	@Override
	public List<ResourceDto> queryAuthorizationAllList(Map<String,Object> map) throws Exception {
		Set<String> roleIds= (Set<String>)map.get("roleIds");
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String loginName = userBeanInfo.getSecurityUserDto().getLoginName();
		String tendId = userBeanInfo.getTendId();
		String cookies = userBeanInfo.getCookies();
		String sessionId="";
		if(StringUtils.isNotBlank(cookies)){
			String cookiess [] = cookies.split(";");
			for(String s:cookiess){
				if(s.split("=")[0].equals("DTL_SESSION_ID")){
					sessionId = s.split("=")[1];
				}
			}
		}
		
		
		Map<String,Object> mapCon = new HashMap<String,Object>();
		List<String> list_con = new ArrayList<String>();
		list_con.add("");
		if(null == roleIds || roleIds.size()<1){
			mapCon.put("roleIds", list_con);
		}else{
			mapCon.put("roleIds", roleIds);
		}
		Long d1=System.currentTimeMillis();
		//获取已授权的所有prefixId
//		List<Map<String,Object>> listAuthData= funcPermissionDao.queryAuthDataByappIdsAndroleIds(mapCon);
		List<String> listAuthData= funcPermissionDao.queryAuthFunIds(mapCon);
		//所有有权限的resourceId和appId
		Set<String> authSet=new HashSet<>();
		String[] arr=null;
		for (String prefix : listAuthData) {
			if(prefix!=null&&StringUtils.isNotBlank(prefix)){
				arr=prefix.split("/");
				for (String funid : arr) {
					authSet.add(funid);
				}
			}
		}
		/*Long d2=System.currentTimeMillis();
		System.out.println("1----->"+(d2-d1));*/
		//获取所有注册资源数据
//		List<Map<String,Object>> list= funcPermissionDao.queryAuthFuns(mapCon);
		List<Map<String,Object>> list1= funcPermissionDao.queryApps(mapCon);
		List<Map<String,Object>> list2= funcPermissionDao.queryFuns(mapCon);
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list.addAll(list1);
		list.addAll(list2);
		/*Long d3=System.currentTimeMillis();
		System.out.println("2----->"+(d3-d2));*/
		
		List<ResourceDto> listreturn= new ArrayList<ResourceDto>();
		String prefixSort = null;
		String prefixIdAll =null;
		String[] prefixSorts=null;
		String id=null;
		for(int i=0;i<list.size();i++){
			Map<String,Object> mapObject = list.get(i);
			ResourceDto resourceDto = new ResourceDto();
			prefixSort=(String)mapObject.get("prefix_sort");
			prefixIdAll= (String)mapObject.get("prefix_id");
			prefixSorts = prefixSort.split("-");
			id=(String)mapObject.get("id");
			resourceDto.setId(id);
			if(null == mapObject.get("parentId") || "".equals(mapObject.get("parentId").toString())){
				resourceDto.setParentId((String)mapObject.get("pId"));
			}else{
				resourceDto.setParentId((String)mapObject.get("parentId"));
			}
			resourceDto.setName((String)mapObject.get("name"));
			resourceDto.setCode((String)mapObject.get("code"));
			
			if(null != mapObject.get("isextsys")){
				if("1".equals((String)mapObject.get("isextsys"))){
					String url = (String)mapObject.get("url");
					url = url.replace("#[userName]", loginName);
					url = url.replace("#[tendId]", tendId);
					url = url.replace("#[sessionId]", sessionId);
					resourceDto.setResourceUrl(url);
				}else{
					resourceDto.setResourceUrl((String)mapObject.get("url"));
				}
			}else{
				resourceDto.setResourceUrl((String)mapObject.get("url"));
			}
			
			
			resourceDto.setType((String)mapObject.get("type"));
			if(null!=mapObject.get("icon")&&(!"".equals(mapObject.get("icon")))){
				resourceDto.setIcon(new String((byte [])mapObject.get("icon")));
			}else{
				resourceDto.setIcon("");
			}
			if(null != mapObject.get("openmode")){
				resourceDto.setOpenmode((String)mapObject.get("openmode"));
			}else{
				resourceDto.setOpenmode("1");
			}
			resourceDto.setPrefixId(prefixIdAll);
			resourceDto.setPrefixSort(prefixSort);
			resourceDto.setPrefixName((String)mapObject.get("prefix_name"));
			resourceDto.setAppId((String)mapObject.get("app_id"));
			resourceDto.setLevel(new Long(prefixSorts.length));
			resourceDto.setExpanded(true);
			resourceDto.setLoaded(true);
			resourceDto.setIsAuth("0");
			
			//循环已授权的数据，如果在已授权中，更改授权标识为1
			/*for(int y=0;y<listAuthData.size();y++){
				Map<String,Object> mapAuth = listAuthData.get(y);
				String prefixIdOperation = (String)mapAuth.get("prefix_id");
				if(prefixIdOperation.indexOf(prefixIdAll)>=0){
					resourceDto.setIsAuth("1");
				}
			}*/
			if(authSet.contains(id)){
				resourceDto.setIsAuth("1");
			}
			listreturn.add(resourceDto);
		}
//		Long d4=System.currentTimeMillis();
//		System.out.println("3----->"+(d4-d3));
		return listreturn;
	}
	
	/**
	 * 根据用户Id和系统Id获取有权限的菜单
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ResourceDto> getFuncMenuAuthByUserIdAndAppId(Map<String,Object> map) throws Exception {
		
		String userId = (String)map.get("userId");
		String appId = (String)map.get("appId");
		
		//获取用户的角色
		List<StandardRoleDto> standardRoleDtoList =  standardRoleService.queryRoleListByUserId(map);
		
		List<String> roleIds = new ArrayList<String>();
		List<String> appIds = new ArrayList<String>();
		appIds.add(appId);
		if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
			for(StandardRoleDto srDto:standardRoleDtoList){
				roleIds.add(srDto.getId());
			}
		}
		Map<String,Object> mapCon = new HashMap<String,Object>();
		List<String> list_con = new ArrayList<String>();
		list_con.add("");
		if(null == roleIds || roleIds.size()<1){
			mapCon.put("roleIds", list_con);
		}else{
			mapCon.put("roleIds", roleIds);
		}
		mapCon.put("appIds", appIds);
		//查询所有注册资源数据时，根据系统Id进行过滤
		mapCon.put("ids", appIds);
		//获取已授权
		List<Map<String,Object>> listAuthData= funcPermissionDao.queryAuthDataByappIdsAndroleIds(mapCon);
		
		//获取所有注册资源数据（根据appId查询出来的）
		List<Map<String,Object>> list= funcPermissionDao.queryAuthorizationListByAppIds(mapCon);
		
		List<ResourceDto> listreturn= new ArrayList<ResourceDto>();
		//循环所有注册资源数据（根据appId查询出来的）
		for(int i=0;i<list.size();i++){
			Map<String,Object> mapObject = list.get(i);
			
			//只要菜单项
			if("RESOURCE".equals((String)mapObject.get("type"))){
				ResourceDto resourceDto = new ResourceDto();
				String prefixSort = (String)mapObject.get("prefix_sort");
				String prefixIdAll = (String)mapObject.get("prefix_id");
				String[] prefixSorts = prefixSort.split("-");
				
				resourceDto.setId((String)mapObject.get("id"));
				if(null == mapObject.get("parentId") || "".equals(mapObject.get("parentId").toString())){
					resourceDto.setParentId((String)mapObject.get("pId"));
				}else{
					resourceDto.setParentId((String)mapObject.get("parentId"));
				}
				resourceDto.setName((String)mapObject.get("name"));
				resourceDto.setCode((String)mapObject.get("code"));
				resourceDto.setResourceUrl((String)mapObject.get("url"));
				resourceDto.setType((String)mapObject.get("type"));
				if(null != mapObject.get("icon")){
					resourceDto.setIcon(new String((byte [])mapObject.get("icon")));
				}else{
					resourceDto.setIcon("");
				}
				if(null != mapObject.get("openmode")){
					resourceDto.setOpenmode((String)mapObject.get("openmode"));
				}else{
					resourceDto.setOpenmode("1");
				}
				resourceDto.setPrefixId(prefixIdAll);
				resourceDto.setPrefixSort(prefixSort);
				resourceDto.setPrefixName((String)mapObject.get("prefix_name"));
				resourceDto.setAppId((String)mapObject.get("app_id"));
				resourceDto.setLevel(new Long(prefixSorts.length));
				resourceDto.setExpanded(true);
				resourceDto.setLoaded(true);
				resourceDto.setIsAuth("0");
				//循环已授权的数据，如果在已授权中，放入返回数据中
				for(int y=0;y<listAuthData.size();y++){
					Map<String,Object> mapAuth = listAuthData.get(y);
					String prefixIdOperation = (String)mapAuth.get("prefix_id");
					if(prefixIdOperation.indexOf(prefixIdAll)>=0){
						resourceDto.setIsAuth("1");
						listreturn.add(resourceDto);
					}
				}
			}
		}
		return listreturn;
	}
	
	/**
	 * 根据用户Id和系统Id和菜单Id获取有权限的按钮
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ResourceDto> getFuncButtonAuthByUserIdAndAppIdAndMenuId(Map<String,Object> map) throws Exception {
		
		String userId = (String)map.get("userId");
		String appId = (String)map.get("appId");
		String menuId = (String)map.get("menuId");
		
		//获取用户的角色
		List<StandardRoleDto> standardRoleDtoList =  standardRoleService.queryRoleListByUserId(map);
		
		List<String> roleIds = new ArrayList<String>();
		List<String> appIds = new ArrayList<String>();
		appIds.add(appId);
		if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
			for(StandardRoleDto srDto:standardRoleDtoList){
				roleIds.add(srDto.getId());
			}
		}
		Map<String,Object> mapCon = new HashMap<String,Object>();
		List<String> list_con = new ArrayList<String>();
		list_con.add("");
		if(null == roleIds || roleIds.size()<1){
			mapCon.put("roleIds", list_con);
		}else{
			mapCon.put("roleIds", roleIds);
		}
		mapCon.put("appIds", appIds);
		//查询所有注册资源数据时，根据系统Id进行过滤
		mapCon.put("ids", appIds);
		//获取已授权
		List<Map<String,Object>> listAuthData= funcPermissionDao.queryAuthDataByappIdsAndroleIds(mapCon);
		
		//获取所有注册资源数据（根据appId查询出来的）
		List<Map<String,Object>> list= funcPermissionDao.queryAuthorizationListByAppIds(mapCon);
		
		List<ResourceDto> listreturn= new ArrayList<ResourceDto>();
		//循环所有注册资源数据（根据appId查询出来的）
		for(int i=0;i<list.size();i++){
			Map<String,Object> mapObject = list.get(i);
			
			//只要按钮项
			if("OPERATION".equals((String)mapObject.get("type"))){
				ResourceDto resourceDto = new ResourceDto();
				
				//如果菜单Id不为空，并且菜单Id不等于当前按钮的菜单Id ，不往下进行，继续下一条数据
				if(menuId!=null && !menuId.equals("") && !menuId.equals((String)mapObject.get("pId"))){
					continue;
				}
				
				String prefixSort = (String)mapObject.get("prefix_sort");
				String prefixIdAll = (String)mapObject.get("prefix_id");
				String[] prefixSorts = prefixSort.split("-");
				
				resourceDto.setId((String)mapObject.get("id"));
				if(null == mapObject.get("parentId") || "".equals(mapObject.get("parentId").toString())){
					resourceDto.setParentId((String)mapObject.get("pId"));
				}else{
					resourceDto.setParentId((String)mapObject.get("parentId"));
				}
				resourceDto.setName((String)mapObject.get("name"));
				resourceDto.setCode((String)mapObject.get("code"));
				resourceDto.setResourceUrl((String)mapObject.get("url"));
				resourceDto.setType((String)mapObject.get("type"));
				if(null != mapObject.get("icon")){
					resourceDto.setIcon(new String((byte [])mapObject.get("icon")));
				}else{
					resourceDto.setIcon("");
				}
				if(null != mapObject.get("openmode")){
					resourceDto.setOpenmode((String)mapObject.get("openmode"));
				}else{
					resourceDto.setOpenmode("1");
				}
				resourceDto.setPrefixId(prefixIdAll);
				resourceDto.setPrefixSort(prefixSort);
				resourceDto.setPrefixName((String)mapObject.get("prefix_name"));
				resourceDto.setAppId((String)mapObject.get("app_id"));
				resourceDto.setLevel(new Long(prefixSorts.length));
				resourceDto.setExpanded(true);
				resourceDto.setLoaded(true);
				resourceDto.setIsAuth("0");
				//循环已授权的数据，如果在已授权中，放入返回数据中
				for(int y=0;y<listAuthData.size();y++){
					Map<String,Object> mapAuth = listAuthData.get(y);
					String prefixIdOperation = (String)mapAuth.get("prefix_id");
					if(prefixIdOperation.indexOf(prefixIdAll)>=0){
						resourceDto.setIsAuth("1");
						listreturn.add(resourceDto);
					}
				}
			}
		}
		return listreturn;
	}
	
	
	
	@Override
	public List<Map<String,Object>> queryAuthorizationListAllRoles() throws Exception {
			List<Map<String,Object>> list= funcPermissionDao.queryAuthorizationListAllRoles();
			List<Map<String,Object>> listreturn= new ArrayList<Map<String,Object>>();
			for(int i=0;i<list.size();i++){
				Map<String,Object> mapObject = list.get(i);
				String prefixSort = (String)mapObject.get("prefix_sort");
				String[] prefixSorts = prefixSort.split("-");
				mapObject.put("level", prefixSorts.length);
				mapObject.put("expanded", true);
				mapObject.put("loaded", true);
				listreturn.add(mapObject);
			}
			return listreturn;
		
	}
	
	@Override
	public List<Map<String,Object>> queryAuthorizationListAllCurrencyRoles() throws Exception {
			List<Map<String,Object>> list= funcPermissionDao.queryAuthorizationListAllCurrencyRoles();
			List<Map<String,Object>> listreturn= new ArrayList<Map<String,Object>>();
			for(int i=0;i<list.size();i++){
				Map<String,Object> mapObject = list.get(i);
				String prefixSort = (String)mapObject.get("prefix_sort");
				String[] prefixSorts = prefixSort.split("-");
				mapObject.put("level", prefixSorts.length);
				mapObject.put("expanded", true);
				mapObject.put("loaded", true);
				listreturn.add(mapObject);
			}
			return listreturn;
		
	}
	
	@Override
	public List<Map<String,Object>> queryAuthorizationListAllPost() throws Exception {
			List<Map<String,Object>> list= funcPermissionDao.queryAuthorizationListAllPost();
			List<Map<String,Object>> listreturn= new ArrayList<Map<String,Object>>();
			for(int i=0;i<list.size();i++){
				Map<String,Object> mapObject = list.get(i);
				String prefixSort = (String)mapObject.get("prefix_id");
				String[] prefixSorts = prefixSort.split("/");
				mapObject.put("level", prefixSorts.length);
				mapObject.put("expanded", true);
				mapObject.put("loaded", true);
				listreturn.add(mapObject);
			}
			return listreturn;
		
	}
	
	@Override
	public List<Map<String,Object>> queryAuthorizationListAllUser(Map<String,Object> map) throws Exception {
			List<Map<String,Object>> list= funcPermissionDao.queryAuthorizationListAllUser(map);
			List<Map<String,Object>> listreturn= new ArrayList<Map<String,Object>>();
			for(int i=0;i<list.size();i++){
				Map<String,Object> mapObject = list.get(i);
				String prefixSort = (String)mapObject.get("prefix_id");
				String[] prefixSorts = prefixSort.split("/");
				mapObject.put("level", prefixSorts.length);
				mapObject.put("expanded", true);
				mapObject.put("loaded", true);
				listreturn.add(mapObject);
			}
			return listreturn;
		
	}
	
	@Override
	public List<Map<String,Object>> queryAuthDataByappIdsAndroleIds(Map<String,Object> map) throws Exception {
		String appIds = (String)map.get("appIds");
		String roleIds = (String)map.get("roleIds");
		if("".equals(appIds)){
			return null;
		}else{
			String[] appIdsList = appIds.split(",");
			String[] roleIdsList = roleIds.split(",");
			Map<String,Object> mapCon = new HashMap<String,Object>();
			mapCon.put("appIds", appIdsList);
			mapCon.put("roleIds", roleIdsList);
			List<Map<String,Object>> list= funcPermissionDao.queryAuthDataByappIdsAndroleIds(mapCon);
			return list;
		}
		
	}
	@Override
	public List<Map<String,Object>> queryAuthDataByobjectIds(Map<String,Object> map) throws Exception {
		String objectids = (String)map.get("objectids");
		if("".equals(objectids)){
			return null;
		}else{
//			String[] appIdsList = appIds.split(",");
			String[] objectidsList = objectids.split(",");
			Map<String,Object> mapCon = new HashMap<String,Object>();
//			mapCon.put("appIds", appIdsList);
			mapCon.put("objectIds", objectidsList);
			List<Map<String,Object>> list= funcPermissionDao.queryAuthDataByobjectIds(mapCon);
			return list;
		}
		
	}
	@Override
	public List<Map<String,Object>> queryAuthDataByobjectIdsReturnId(Map<String,Object> map) throws Exception {
		List<Map<String,Object>> list= funcPermissionDao.queryAuthDataByobjectIdsReturnId(map);
		return list;
	}
	
	@Override
	public List<Map<String,Object>> queryAuthDataByOperationIds(Map<String,Object> map) throws Exception {
		String operationIds = (String)map.get("operationIds");
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		if("".equals(operationIds)){
			return null;
		}else{
			String[] operationIdsList = operationIds.split(",");
			Map<String,Object> mapCon = new HashMap<String,Object>();
			mapCon.put("operationIds", operationIdsList);
			//查询标准岗位和通用角色
			if(null == map.get("type") || "role".equals((String)map.get("type")) || "".equals((String)map.get("type"))){
				 list= funcPermissionDao.queryAuthDataByOperationIds(mapCon);
			}else if("post".equals((String)map.get("type"))){
				//查询岗位
				list= funcPermissionDao.queryAuthDataPostByOperationIds(mapCon);
			}else if("user".equals((String)map.get("type"))){
				//查询人员
				list= funcPermissionDao.queryAuthDataUserByOperationIds(mapCon);
			}else if("org".equals((String)map.get("type"))){
				//查询组织
				list= funcPermissionDao.queryAuthDataOrgByOperationIds(mapCon);
			}
			
			return list;
		}
		
	}
	
	@Override
	public List<Map<String,Object>> getFuncButtonAuthByUserLoginNameAndAppCodeAndMenuCode(Map<String,Object> map) throws Exception {
		String userLoginName = (String)map.get("userLoginName");
		String appCode = (String)map.get("appCode");
		String menuCode = (String)map.get("menuCode");
		UserDto userDto = userService.selectUserInfoById(map);
		
		Map<String,Object> mapcon = new HashMap<String,Object>();
		mapcon.put("userId", userDto.getId());
		map.put("userId", userDto.getId());
		//获取用户的角色
		List<StandardRoleDto> standardRoleDtoList =  standardRoleService.queryRoleListByUserId(mapcon);
		
		//获取用户的通用角色
		List<StandardRoleDto> currencyRoleDtoList =  standardRoleService.queryCurrencyRoleListByUserId(mapcon);
		
		//获取用户的岗位
		List<PostDto> postDtoList = postService.queryAuthPostListByUserId(mapcon);
		//用户的ID，用户标准岗位ID，用户角色ID，用户的岗位ID，用户的所有组织id及上级组织id
		Set<String> list = new HashSet<>();
		//用户的标准岗位ID
		if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
			for(StandardRoleDto srDto:standardRoleDtoList){
				list.add(srDto.getId());
			}
		}
		//用户的通用角色ID
		if(null != currencyRoleDtoList && currencyRoleDtoList.size()>0){
			for(StandardRoleDto srDto:currencyRoleDtoList){
				list.add(srDto.getId());
			}
		}
		//用户的岗位ID
		if(null != postDtoList && postDtoList.size()>0){
			for(PostDto srDto:postDtoList){
				list.add(srDto.getId());
			}
		}
		//用户的组织ID
		List<OrgnazationDto> userOrgs=orgnazationDao.selectUserAllOrgs(map);
		Set<String> orgIds = new HashSet<>();
		if(CollectionUtils.isNotEmpty(userOrgs)) {
			for (int i = 0; i < userOrgs.size(); i++) {
				orgIds.add(userOrgs.get(i).getId());
			}
		}
		map.put("orgIds",orgIds);
		List<Map<String,Object>> orgs = orgnazationDao.selectAllUpOrgs(map);
		if(CollectionUtils.isNotEmpty(orgs)){
			for(Map<String,Object> org : orgs){
				list.add((String)org.get("parentId"));
			}
		}
		//当前用户的ID
		list.add(userDto.getId());
		
		//设置当前授权对象的所有ID
		map.put("ids", list);
		List<Map<String,Object>> listReturn= funcPermissionDao.getFuncButtonAuthByUserLoginNameAndAppCodeAndMenuCode(map);
		return listReturn;
	}
	
	@Override
	public List<Map<String,Object>> getFuncMenuAuthByUserLoginNameAndAppCode(Map<String,Object> map) throws Exception {
		String userLoginName = (String)map.get("userLoginName");
		String appCode = (String)map.get("appCode");
		UserDto userDto = userService.selectUserInfoById(map);
		
		Map<String,Object> mapcon = new HashMap<String,Object>();
		mapcon.put("userId", userDto.getId());
		map.put("userId", userDto.getId());
		//获取用户的角色
		List<StandardRoleDto> standardRoleDtoList =  standardRoleService.queryRoleListByUserId(mapcon);
		
		//获取用户的通用角色
		List<StandardRoleDto> currencyRoleDtoList =  standardRoleService.queryCurrencyRoleListByUserId(mapcon);
		
		//获取用户的岗位
		List<PostDto> postDtoList = postService.queryAuthPostListByUserId(mapcon);
		//用户的ID，用户标准岗位ID，用户角色ID，用户的岗位ID，用户的所有组织id及上级组织id
		Set<String> list = new HashSet<>();
		//用户的标准岗位ID
		if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
			for(StandardRoleDto srDto:standardRoleDtoList){
				list.add(srDto.getId());
			}
		}
		//用户的通用角色ID
		if(null != currencyRoleDtoList && currencyRoleDtoList.size()>0){
			for(StandardRoleDto srDto:currencyRoleDtoList){
				list.add(srDto.getId());
			}
		}
		//用户的岗位ID
		if(null != postDtoList && postDtoList.size()>0){
			for(PostDto srDto:postDtoList){
				list.add(srDto.getId());
			}
		}
		//用户的组织ID
		List<OrgnazationDto> userOrgs=orgnazationDao.selectUserAllOrgs(map);
		Set<String> orgIds = new HashSet<>();
		if(CollectionUtils.isNotEmpty(userOrgs)) {
			for (int i = 0; i < userOrgs.size(); i++) {
				orgIds.add(userOrgs.get(i).getId());
			}
		}
		map.put("orgIds",orgIds);
		List<Map<String,Object>> orgs = orgnazationDao.selectAllUpOrgs(map);
		if(CollectionUtils.isNotEmpty(orgs)){
			for(Map<String,Object> org : orgs){
				list.add((String)org.get("parentId"));
			}
		}
		//当前用户的ID
		list.add(userDto.getId());
		
		//设置当前授权对象的所有ID
		map.put("ids", list);
		List<Map<String,Object>> listReturn= funcPermissionDao.getFuncMenuAuthByUserLoginNameAndAppCode(map);
		return listReturn;
	}


	@Override
	public List<Map<String,Object>> queryUsersByMenuId(Map<String, Object> map) throws Exception {
		return funcPermissionDao.queryUsersByMenuId(map);
	}

	@Override
	public List<Map<String, Object>> queryAuthorizationListByAppId(Map map) throws Exception {
		String appIds = (String)map.get("appId");
		String roleIds = (String)map.get("roleId");
		if("".equals(appIds)||"".equals(roleIds)){
			return null;
		}else{
			Map<String,Object> mapCon = new HashMap<String,Object>();
			String[] appIdsList = appIds.split(",");
			String[] roleIdsList = roleIds.split(",");
			mapCon.put("roleIds", roleIdsList);
			mapCon.put("appIds", appIdsList);

			Long d1=System.currentTimeMillis();
			//获取已授权的所有prefixId
			List<String> listAuthData= funcPermissionDao.queryAuthPrefixIds(mapCon);
			//所有有权限的resourceId和appId
			Set<String> authSet=new HashSet<>();
			String[] arr=null;
			for (String prefix : listAuthData) {
				if(prefix!=null&&StringUtils.isNotBlank(prefix)){
					arr=prefix.split("/");
					for (String funid : arr) {
						authSet.add(funid);
					}
				}
			}

			mapCon.clear();
			mapCon.put("ids", appIdsList);
			List<Map<String,Object>> list= funcPermissionDao.queryAuthorizationListByAppIdsPermission(mapCon);
			List<Map<String,Object>> listreturn= new ArrayList<Map<String,Object>>();
			for(int i=0;i<list.size();i++){
				Map<String,Object> mapObject = list.get(i);
				if(authSet.contains(mapObject.get("id").toString())){
					String prefixSort = (String)mapObject.get("prefix_sort");
					String[] prefixSorts = prefixSort.split("-");
					mapObject.put("level", prefixSorts.length);
					mapObject.put("expanded", false);
					mapObject.put("loaded", true);
					if(null == mapObject.get("parentId") || "".equals(mapObject.get("parentId").toString())){
						mapObject.put("parentId", mapObject.get("pId"));
					}
					listreturn.add(mapObject);
				}
			}
			return listreturn;
		}
	}
}
