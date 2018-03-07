package com.xinleju.platform.sys.org.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.org.dto.service.UserPostScopeDtoServiceCustomer;
import com.xinleju.platform.sys.org.entity.PostUser;
import com.xinleju.platform.sys.org.entity.UserPostScope;
import com.xinleju.platform.sys.org.service.PostUserService;
import com.xinleju.platform.sys.org.service.UserPostScopeService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class UserPostScopeDtoServiceProducer implements UserPostScopeDtoServiceCustomer{
	private static Logger log = Logger.getLogger(UserPostScopeDtoServiceProducer.class);
	@Autowired
	private UserPostScopeService userPostScopeService;
	
	@Autowired
	private PostUserService postUserService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   UserPostScope userPostScope=JacksonUtils.fromJson(saveJson, UserPostScope.class);
		   userPostScopeService.save(userPostScope);
		   info.setResult(JacksonUtils.toJson(userPostScope));
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
	public String saveBatch(String userInfo, String saveJsonList)
			 {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(saveJsonList)) {
				Map map = JacksonUtils.fromJson(saveJsonList, HashMap.class);
				Map<String,Object> paramater = new HashMap<String,Object>();
				String userId = (String)map.get("userId");
				String postId = (String)map.get("postId");
				String uuids = (String)map.get("uuids");
				String refId = (String)map.get("refId");
				
				//获取岗位和用户关系ID
				paramater.put("userId", userId);
				paramater.put("postId", postId);
				List<PostUser> listPostUser = postUserService.queryList(paramater);
				String postUserId = "";
				if(null!= listPostUser && listPostUser.size()>0){
					postUserId = listPostUser.get(0).getId();
				}
				
				//删除原有的管辖范围
				Map<String,Object> paramaterScope = new HashMap<String,Object>();
				paramaterScope.put("postUserId", postUserId);
				List<UserPostScope> listUserPostScope = userPostScopeService.queryList(paramaterScope);
				List<String> list = new ArrayList<String>();
				for(UserPostScope ups:listUserPostScope){
					list.add(ups.getId());
				}
				if(list.size()>0){
					int result= userPostScopeService.deleteAllObjectByIds(list);
				}
				
				//截取uuid和组织机构ID
				String [] refIdsList = refId.split(",");
				String [] uuidsList = uuids.split(",");
				UserPostScope userPostScope = new UserPostScope();
				for(int i =0;i<refIdsList.length;i++){
					userPostScope.setId(IDGenerator.getUUID());
					userPostScope.setRefId(refIdsList[i]);
					userPostScope.setPostUserId(postUserId);
					userPostScopeService.save(userPostScope);
				}
				
				info.setResult("保存成功");
				info.setSucess(true);
				info.setMsg("保存成功!");
			} else {
				info.setResult("保存失败");
				info.setSucess(true);
				info.setMsg("保存失败");
			}
		} catch (Exception e) {
			log.error("保存失败!" + e.getMessage());
			info.setResult("保存失败");
			info.setSucess(false);
			info.setMsg("保存失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
		// TODO Auto-generated method stub
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
			   UserPostScope userPostScope=JacksonUtils.fromJson(updateJson, UserPostScope.class);
			   int result=   userPostScopeService.update(userPostScope);
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
			   UserPostScope userPostScope=JacksonUtils.fromJson(deleteJson, UserPostScope.class);
			   int result= userPostScopeService.deleteObjectById(userPostScope.getId());
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
				   int result= userPostScopeService.deleteAllObjectByIds(list);
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
			UserPostScope userPostScope=JacksonUtils.fromJson(getJson, UserPostScope.class);
			UserPostScope	result = userPostScopeService.getObjectById(userPostScope.getId());
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
				Page page=userPostScopeService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=userPostScopeService.getPage(new HashMap(), null, null);
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
				List list=userPostScopeService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=userPostScopeService.queryList(null);
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
}
