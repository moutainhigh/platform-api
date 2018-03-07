package com.xinleju.platform.sys.res.dto.service.impl;

import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;

import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.sys.org.dao.OrgnazationDao;
import com.xinleju.platform.sys.org.dao.UserDao;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.entity.Post;
import com.xinleju.platform.sys.res.dao.FuncPermissionDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.sys.org.dto.PostDto;
import com.xinleju.platform.sys.org.dto.StandardRoleDto;
import com.xinleju.platform.sys.org.service.PostService;
import com.xinleju.platform.sys.org.service.StandardRoleService;
import com.xinleju.platform.sys.res.dto.service.FuncPermissionDtoServiceCustomer;
import com.xinleju.platform.sys.res.entity.FuncPermission;
import com.xinleju.platform.sys.res.service.FuncPermissionService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author admin
 * 
 *
 */
 
public class FuncPermissionDtoServiceProducer implements FuncPermissionDtoServiceCustomer{
	private static Logger log = Logger.getLogger(FuncPermissionDtoServiceProducer.class);
	@Autowired
	private FuncPermissionService funcPermissionService;
	@Autowired
	private FuncPermissionDao funcPermissionDao;
	
	@Autowired
	private StandardRoleService standardRoleService;
	
	@Autowired
	private PostService postService;
	@Autowired
	private OrgnazationDao orgnazationDao;
	@Autowired
	private UserDao userDao;
	private ExecutorService threadPool = ThreadPoolUtil.getInstance() ;

	@Value("#{configuration['ImAuthUserUrl']?:''}")
	private String ImAuthUserUrl;

	private static final String APPCODE_LLOA = "LLOA";
	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   FuncPermission funcPermission=JacksonUtils.fromJson(saveJson, FuncPermission.class);
		   funcPermissionService.save(funcPermission);
		   info.setResult(JacksonUtils.toJson(funcPermission));
		   info.setSucess(true);
		   info.setMsg("保存对象成功!");
		} catch (Exception e) {
		 log.error("保存对象失败!"+e.getMessage());
		 info.setSucess(false);
		 info.setMsg("保存对象失败!");
		 info.setExceptionMsg(e.getMessage());
		}
	   return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBatch(String userInfo, String saveJsonList){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			if (StringUtils.isNotBlank(saveJsonList)) {
				Map map = JacksonUtils.fromJson(saveJsonList, HashMap.class);
				String objectType = (String)map.get("objectType");
				List<String> roleIds = new ArrayList<>();
				Map mapAuthData  = (HashMap) map.get("saveData");
				//此处查询所有的会有8万多调数据，后面再这个里面循环找会很慢
//				Map<String,Object> mapCon = new HashMap<String,Object>();
//				mapCon.put("sidx","id");
//				mapCon.put("sord","desc");
//				List<FuncPermission> listFuncPermission = funcPermissionService
//						.queryList(mapCon);
				Map<String,List<String>> delMap = new HashMap<>();
				List<String> delIds = null;
				List<FuncPermission> saveObjs = new ArrayList<>();
				if (mapAuthData != null) {
					List<String> listFuncPermissionIds = new ArrayList<String>();
					Set<String> set = mapAuthData.keySet();
					for (String key : set) {
						String[] keyIds = key.split("#");
						String[] operationIds = keyIds[0].split("/");
						String operationId = operationIds[operationIds.length-1];
						String roleId = keyIds[1];
						String appId = keyIds[4];
						String resourceId = keyIds[5];
						if(!roleId.equals("roleId")){
							roleIds.add(roleId);
							if("1".equals(mapAuthData.get(key))){
								FuncPermission funcPermission = new FuncPermission();
								funcPermission.setId(IDGenerator.getUUID());
								funcPermission.setOperationId(operationId);
								funcPermission.setRoleId(roleId);
								funcPermission.setAppId(appId);
								funcPermission.setResourceId(resourceId);
								saveObjs.add(funcPermission);
//								funcPermissionService.save(funcPermission);
							}else{
								Map<String,Object> mapCon = new HashMap<String,Object>();
								/*mapCon.put("sidx","id");
								mapCon.put("sord","desc");*/
								mapCon.put("roleId",roleId);
								mapCon.put("operationId",operationId);
								delIds = delMap.get(roleId);
								if(delIds == null){
									delIds = new ArrayList<>();
								}
								delIds.add(operationId);
								delMap.put(roleId,delIds);
								/*List<FuncPermission> listFuncPermission = funcPermissionService
										.queryList(mapCon);
							 	for(FuncPermission f :listFuncPermission){
									listFuncPermissionIds.add(f.getId());
								}*/
							}
						}
						
					}
					Map<String,Object> mapParam = new HashMap<String,Object>();
					if(saveObjs.size()>0){
						SecurityUserDto user = userBeanInfo.getSecurityUserDto();
						mapParam.put("saveObjs",saveObjs);
						mapParam.put("tendId",userBeanInfo.getTendId());
						mapParam.put("createDate",new Date());
						mapParam.put("createPersonId",user.getId());
						mapParam.put("createPersonName",user.getRealName());
						funcPermissionDao.savebatchAuth(mapParam);
					}
					if(delMap.size() > 0){
						for (Map.Entry entry:delMap.entrySet()){
							mapParam.put("roleId",entry.getKey());
							mapParam.put("operations",entry.getValue());
							List<String> listFuncPermission = funcPermissionDao.selectFuncIdByRole(mapParam);
							listFuncPermissionIds.addAll(listFuncPermission);
						}
					}
					if (listFuncPermissionIds.size() > 0) {
						funcPermissionService.deleteAllObjectByIds(listFuncPermissionIds);
					}
				}
				if(roleIds.size()>0){
					String tendId = userBeanInfo.getTendId();
					noticeThirdUserAuth(objectType,roleIds,tendId);
				}
				info.setResult("保存成功");
				info.setSucess(true);
				info.setMsg("保存对象成功!");
			} else {
				info.setResult("参数为空");
				info.setSucess(false);
				info.setMsg("保存对象失败!");
			}
		} catch (Exception e) {
			log.error("保存对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("保存对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	//通知IM系统哪些人员的权限有变动
	public void noticeThirdUserAuth(String objectType,List<String> roleIds,String tendId){
		List<String> userIds = null;
		Map<String, Object> param = new HashMap<>();
		try {
			param.put("roleIds", roleIds);
			switch (objectType) {
				//标准岗位
				case "standardPost":
					userIds = userDao.getUsersFromStandardPost(param);
					break;
				//角色
				case "role":
					userIds = userDao.getUsersFromRole(param);
					break;
				//岗位
				case "post":
					userIds = userDao.getUsersFromPost(param);
					break;
				//人员
				case "user":
					userIds = roleIds;
					break;
				//组织
				case "org":
					userIds = userDao.getUsersFromOrg(param);
					break;
				default:
					break;
			}
		} catch (Exception e) {
			log.error("查询授权人员出错：" + e.getMessage());
			e.printStackTrace();
		}
		if(CollectionUtils.isNotEmpty(userIds)){
			param.put("personIds",userIds);
			param.put("tendId",tendId);
			param.remove("roleIds");
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try{
						//通知IM系统哪些人员的权限有变动
						String sendUrl = ImAuthUserUrl;
						log.info("通知IM系统哪些人员的权限有变动---sendUrl=" + sendUrl);
						CloseableHttpClient httpClient = HttpClientBuilder.create().build();
						HttpPost httpPost = new HttpPost(sendUrl);
						httpPost.addHeader("token", "xyre");
						httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
						String json = JacksonUtils.toJson(param);
//						json = URLEncoder.encode(json, "utf-8");
						log.info("通知IM系统哪些人员的权限有变动---json=" + json);
						String imresult = null;
						StringEntity se = new StringEntity(json);
						se.setContentType(CONTENT_TYPE_TEXT_JSON);
						se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
						httpPost.setEntity(se);
						HttpResponse res = httpClient.execute(httpPost);
						if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							imresult = EntityUtils.toString(res.getEntity()); // 返回json格式字符串
							log.debug("通知IM系统哪些人员的权限有变动>>> imresult=" + imresult);
						}
						log.info("通知IM系统哪些人员的权限有变动---result>>> StatusCode=" + res.getStatusLine().getStatusCode());

					} catch (Exception e) {
						log.error("通知IM系统哪些人员的权限有变动,出错：" + e.getMessage());
						e.printStackTrace();
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
			});
		}
	}

	@Override
	public String saveBatchFunToRole(String userInfo, String saveJsonList){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(saveJsonList)) {
				Map map = JacksonUtils.fromJson(saveJsonList, HashMap.class);
				Map mapAuthData  = (HashMap) map.get("saveData");
				String objectType = (String)map.get("objectType");
				List<String> roleIds_ = new ArrayList<>();
				//此处查询所有的会有8万多调数据，后面再这个里面循环找会很慢
//				Map<String,Object> mapCon = new HashMap<String,Object>();
//				mapCon.put("sidx","id");
//				mapCon.put("sord","desc");
//				List<FuncPermission> listFuncPermission = funcPermissionService
//						.queryList(mapCon);
				if (mapAuthData != null) {
					List<String> listFuncPermissionIds = new ArrayList<String>();
					Set<String> set = mapAuthData.keySet();
					for (String key : set) {
						String[] keyIds = key.split("#");
						String[] roleIds = keyIds[0].split("/");
						String roleId = roleIds[roleIds.length-1];
						String operationId = keyIds[1];
						String appId = keyIds[4];
						String resourceId = keyIds[5];
						if(!operationId.equals("operationId")){
							roleIds_.add(roleId);
							if("1".equals(mapAuthData.get(key))){
								FuncPermission funcPermission = new FuncPermission();
								funcPermission.setId(IDGenerator.getUUID());
								funcPermission.setOperationId(operationId);
								funcPermission.setRoleId(roleId);
								funcPermission.setAppId(appId);
								funcPermission.setResourceId(resourceId);
								funcPermissionService.save(funcPermission);
							}else{
								Map<String,Object> mapCon = new HashMap<String,Object>();
								mapCon.put("sidx","id");
								mapCon.put("sord","desc");
								mapCon.put("roleId",roleId);
								mapCon.put("operationId",operationId);
								List<FuncPermission> listFuncPermission = funcPermissionService
										.queryList(mapCon);
								for(FuncPermission f :listFuncPermission){
									listFuncPermissionIds.add(f.getId());
								}
//								for(FuncPermission f :listFuncPermission){
//									if(f.getRoleId().equals(roleId) && f.getOperationId().equals(operationId)){
//										listFuncPermissionIds.add(f.getId());
//									}
//								}
							}
						}
						
					}
					if (listFuncPermissionIds.size() > 0) {
						funcPermissionService.deleteAllObjectByIds(listFuncPermissionIds);
					}
				}
				if(roleIds_.size()>0){
					String tendId = LoginUtils.getSecurityUserBeanInfo().getTendId();
					noticeThirdUserAuth(objectType,roleIds_,tendId);
				}
				info.setResult("保存成功");
				info.setSucess(true);
				info.setMsg("保存对象成功!");
			} else {
				info.setResult("参数为空");
				info.setSucess(false);
				info.setMsg("保存对象失败!");
			}
		} catch (Exception e) {
			log.error("保存对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("保存对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	@Override
	public String saveBatchFunImport(String userInfo, String saveJsonList){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(saveJsonList)) {
				Map map = JacksonUtils.fromJson(saveJsonList, HashMap.class);
				
				if(null == map.get("importObjectId") || "".equals((String)map.get("importObjectId"))){
					throw new InvalidCustomException("需要更改的对象为空");
				}
				
				if(null == map.get("objectids") || "".equals((String)map.get("objectids"))){
					throw new InvalidCustomException("需要引入的对象为空");
				}
				
				String objectids = (String)map.get("objectids");
				String importObjectId = (String)map.get("importObjectId");
				String importType = (String)map.get("importType");
				
				List<Map<String,Object>> resultList = funcPermissionService.queryAuthDataByobjectIds(map);
				
				String[] importObjectIdList = importObjectId.split(",");
				
				for(String imObjectId :importObjectIdList){
					Map<String,Object> mapCon = new HashMap<String,Object>();
					mapCon.put("imObjectId", imObjectId);
					List<Map<String,Object>> imResultList = funcPermissionService.queryAuthDataByobjectIdsReturnId(mapCon);
					
					//覆盖
					if(importType.equals("1")){
						//删除原有的
						List<String> listFuncPermissionDelIds = new ArrayList<String>();
						for(Map<String,Object> m :imResultList){
							listFuncPermissionDelIds.add((String)m.get("id"));
						}
						if (listFuncPermissionDelIds.size() > 0) {
							funcPermissionService.deleteAllObjectByIds(listFuncPermissionDelIds);
						}
						//添加新的
						for(Map<String,Object> m :resultList){
							FuncPermission funcPermission = new FuncPermission();
							funcPermission.setId(IDGenerator.getUUID());
							funcPermission.setOperationId((String)m.get("operation_id"));
							funcPermission.setRoleId(imObjectId);
							funcPermission.setAppId((String)m.get("app_id"));
							funcPermission.setResourceId((String)m.get("resource_id"));
							funcPermissionService.save(funcPermission);
						}
						
					}else if(importType.equals("0")){
						//添加
						for(Map<String,Object> m :resultList){
							boolean isexist = false;
							//循环已有的
							for(Map<String,Object> mOld :imResultList){
								if(((String)m.get("operation_id")).equals((String)mOld.get("operation_id"))){
									isexist = true;
									break;
								}
							}
							if(!isexist){
								FuncPermission funcPermission = new FuncPermission();
								funcPermission.setId(IDGenerator.getUUID());
								funcPermission.setOperationId((String)m.get("operation_id"));
								funcPermission.setRoleId(imObjectId);
								funcPermission.setAppId((String)m.get("app_id"));
								funcPermission.setResourceId((String)m.get("resource_id"));
								funcPermissionService.save(funcPermission);
							}
						}
					}
				}
				
				info.setResult("保存成功");
				info.setSucess(true);
				info.setMsg("保存对象成功!");
			} else {
				info.setResult("参数为空");
				info.setSucess(false);
				info.setMsg("保存对象失败!");
			}
		} catch (Exception e) {
			log.error("保存对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("保存对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	@Override
	public String queryAuthDataByappIdsAndroleIds(String userInfo, String paramer){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramer)) {
				Map map = JacksonUtils.fromJson(paramer, HashMap.class);
				List<Map<String,Object>> resultList = funcPermissionService.queryAuthDataByappIdsAndroleIds(map);
				info.setResult(JacksonUtils.toJson(resultList));
				info.setSucess(true);
				info.setMsg("保存对象成功!");
			} else {
				info.setResult("参数为空");
				info.setSucess(false);
				info.setMsg("保存对象失败!");
			}
		} catch (Exception e) {
			log.error("查询对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("查询对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	@Override
	public String queryListByObjectType(String userInfo, String paramer){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramer)) {
				List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
				Map<String,Object> resultMap = new LinkedHashMap<String,Object>();
				Map map = JacksonUtils.fromJson(paramer, HashMap.class);
				//对象类型
				String objTypeShow = (String)map.get("objTypeShow");
				//对象ID
				String id = (String)map.get("id");
				if(objTypeShow.equals("standardPost")){
					resultMap.put(id, "标准岗位自有权限");
				}else if(objTypeShow.equals("role")){
					resultMap.put(id, "角色自有权限");
				}else if(objTypeShow.equals("post")){
					Map<String,Object> mapc = new HashMap<String,Object>();
					mapc.put("postId", id);
					//根据岗位获取通用角色
					List<StandardRoleDto> currencyRoleDtoList =  standardRoleService.queryCurrencyRoleListByPostId(mapc);
					//根据岗位获取标准岗位
					List<StandardRoleDto> standardPostDtoList =  standardRoleService.queryStandardPostListByPostId(mapc);
					//岗位对应的标准岗位ID
					if(null != standardPostDtoList && standardPostDtoList.size()>0){
						for(StandardRoleDto srDto:standardPostDtoList){
//							resultMap.put(srDto.getId(), "继承标准岗位【"+srDto.getName()+"】权限");
							resultMap.put(srDto.getId(), "继承标准岗位【"+srDto.getPrefixName()+"】权限");
						}
					}
					//岗位对应的通用角色ID
					if(null != currencyRoleDtoList && currencyRoleDtoList.size()>0){
						for(StandardRoleDto srDto:currencyRoleDtoList){
//							resultMap.put(srDto.getId(), "继承角色【"+srDto.getName()+"】权限");
							resultMap.put(srDto.getId(), "继承角色【"+srDto.getPrefixName()+"】权限");
						}
					}
					//岗位对应的组织
					Post post = postService.getObjectById(id);
					String orgIds[] = new String[]{post.getRefId()};
					mapc.put("orgIds",orgIds);
					List<Map<String,Object>> orgs = orgnazationDao.selectAllUpOrgs(mapc);
					if(CollectionUtils.isNotEmpty(orgs)){
						for(Map<String,Object> org : orgs){
							resultMap.put( (String)org.get("parentId"), "继承组织【"+ org.get("prefixName") +"】权限");
						}
					}
					resultMap.put(id, "岗位自有权限");
				}else if(objTypeShow.equals("user")){
					Map<String,Object> mapc = new HashMap<String,Object>();
					mapc.put("userId", id);
					//获取用户的角色
					List<StandardRoleDto> standardRoleDtoList =  standardRoleService.queryRoleListByUserId(mapc);
					
					//获取用户的通用角色
					List<StandardRoleDto> currencyRoleDtoList =  standardRoleService.queryCurrencyRoleListByUserId(mapc);
					
					//获取用户的岗位
					List<PostDto> postDtoList = postService.queryAuthPostListByUserId(mapc);

					//用户的标准岗位ID
					if(null != standardRoleDtoList && standardRoleDtoList.size()>0){
						for(StandardRoleDto srDto:standardRoleDtoList){
//							resultMap.put(srDto.getId(), "继承标准岗位【"+srDto.getName()+"】权限");
							resultMap.put(srDto.getId(), "继承标准岗位【"+srDto.getPrefixName()+"】权限");
						}
					}
					
					//用户的岗位ID
					if(null != postDtoList && postDtoList.size()>0){
						for(PostDto srDto:postDtoList){
//							resultMap.put(srDto.getId(), "继承岗位【"+srDto.getName()+"】权限");
							resultMap.put(srDto.getId(), "继承岗位【"+srDto.getOrgPrefixName()+"/"+srDto.getName()+"】权限");
						}
					}
					
					//用户的通用角色ID
					if(null != currencyRoleDtoList && currencyRoleDtoList.size()>0){
						for(StandardRoleDto srDto:currencyRoleDtoList){
//							resultMap.put(srDto.getId(), "继承角色【"+srDto.getName()+"】权限");
							resultMap.put(srDto.getId(), "继承角色【"+srDto.getPrefixName()+"】权限");
						}
					}
					mapc.put("userId",id);
					//用户对应的所有组织
					List<OrgnazationDto> userOrgs=orgnazationDao.selectUserAllOrgs(mapc);
					Set<String> orgIds = new HashSet<>();
					for (int i = 0; i <userOrgs.size() ; i++) {
						orgIds.add(userOrgs.get(i).getId());
					}
					mapc.put("orgIds",orgIds);
					List<Map<String,Object>> orgs = orgnazationDao.selectAllUpOrgs(mapc);
					if(CollectionUtils.isNotEmpty(orgs)){
						for(Map<String,Object> org : orgs){
							resultMap.put( (String)org.get("parentId"), "继承组织【"+ org.get("prefixName") +"】权限");
						}
					}
					resultMap.put(id, "人员自有权限");
				}else if(objTypeShow.equals("org")){
					/*Map<String,Object> mapc = new HashMap<String,Object>();
					String orgIds[] = new String[]{id};
					mapc.put("orgIds",orgIds);
					List<Map<String,Object>> orgs = orgnazationDao.selectAllUpOrgs(mapc);
					if(CollectionUtils.isNotEmpty(orgs)){
						for(Map<String,Object> org : orgs){
							resultMap.put( (String)org.get("parentId"), "继承组织【"+ org.get("prefixName") +"】权限");
						}
					}*/
					resultMap.put(id, "组织自有权限");
				}

				resultList.add(resultMap);
//				List<Map<String,Object>> resultList = funcPermissionService.queryAuthDataByappIdsAndroleIds(map);
				info.setResult(JacksonUtils.toJson(resultMap));
				info.setSucess(true);
				info.setMsg("保存对象成功!");
			} else {
				info.setResult("参数为空");
				info.setSucess(false);
				info.setMsg("保存对象失败!");
			}
		} catch (Exception e) {
			log.error("查询对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("查询对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	

	@Override
	public String updateBatch(String userInfo, String updateJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String userInfo, String updateJson)  {
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   FuncPermission funcPermission=JacksonUtils.fromJson(updateJson, FuncPermission.class);
			   int result=   funcPermissionService.update(funcPermission);
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("更新对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   FuncPermission funcPermission=JacksonUtils.fromJson(deleteJson, FuncPermission.class);
			   int result= funcPermissionService.deleteObjectById(funcPermission.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= funcPermissionService.deleteAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除对象成功!");
				}
			} catch (Exception e) {
			 log.error("删除对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String getObjectById(String userInfo, String getJson)
	 {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			FuncPermission funcPermission=JacksonUtils.fromJson(getJson, FuncPermission.class);
			FuncPermission	result = funcPermissionService.getObjectById(funcPermission.getId());
			info.setResult(JacksonUtils.toJson(result));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=funcPermissionService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=funcPermissionService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取分页对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取分页对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryList(String userInfo, String paramater){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List list=funcPermissionService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=funcPermissionService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getCount(String userInfo, String paramater)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo,
			String deleteJsonList) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String querySystemListByRole(String userinfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> list=funcPermissionService.querySystemListByRole(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=funcPermissionService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	@Override
	public String queryAuthorizationListByAppIds(String userinfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> list=funcPermissionService.queryAuthorizationListByAppIds(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=funcPermissionService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	@Override
	public String queryAuthorizationListAllRoles(String userinfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
//				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> list=funcPermissionService.queryAuthorizationListAllRoles();
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=funcPermissionService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	@Override
	public String queryAuthorizationListAllCurrencyRoles(String userinfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
//				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> list=funcPermissionService.queryAuthorizationListAllCurrencyRoles();
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=funcPermissionService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	@Override
	public String queryAuthorizationListAllPost(String userinfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
//				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> list=funcPermissionService.queryAuthorizationListAllPost();
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=funcPermissionService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	@Override
	public String queryAuthorizationListAllUser(String userinfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> list=funcPermissionService.queryAuthorizationListAllUser(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=funcPermissionService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	@Override
	public String queryAuthDataByOperationIds(String userInfo, String paramer){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(paramer)) {
				Map map = JacksonUtils.fromJson(paramer, HashMap.class);
				List<Map<String,Object>> resultList = funcPermissionService.queryAuthDataByOperationIds(map);
				info.setResult(JacksonUtils.toJson(resultList));
				info.setSucess(true);
				info.setMsg("保存对象成功!");
			} else {
				info.setResult("参数为空");
				info.setSucess(false);
				info.setMsg("保存对象失败!");
			}
		} catch (Exception e) {
			log.error("查询对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("查询对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryUsersByMenuId(String userInfo, String menuIdmap) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map=JacksonUtils.fromJson(menuIdmap, HashMap.class);
			List list=funcPermissionService.queryUsersByMenuId(map);
			info.setResult(JacksonUtils.toJson(list));
			info.setSucess(true);
			info.setMsg("获取用户ID集合成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取用户ID集合失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取用户ID集合失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryAuthorizationListByAppId(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<Map<String,Object>> list=funcPermissionService.queryAuthorizationListByAppId(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=funcPermissionService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
}
