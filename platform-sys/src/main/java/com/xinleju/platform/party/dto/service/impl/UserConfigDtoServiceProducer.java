package com.xinleju.platform.party.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.party.dto.service.UserConfigDtoServiceCustomer;
import com.xinleju.platform.party.entity.UserConfig;
import com.xinleju.platform.party.service.UserConfigService;
import com.xinleju.platform.sys.base.entity.SettlementTrades;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class UserConfigDtoServiceProducer implements UserConfigDtoServiceCustomer{
	private static Logger log = Logger.getLogger(UserConfigDtoServiceProducer.class);
	@Autowired
	private UserConfigService userConfigService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   UserConfig userConfig=JacksonUtils.fromJson(saveJson, UserConfig.class);
		   int i = userConfigService.saveUserConfig(userConfig);
		   if(i==5){
			   info.setSucess(false);
			   info.setMsg("应用名称下已存在该用户!");
			   
		   }else{
			   info.setResult(JacksonUtils.toJson(userConfig));
			   info.setSucess(true);
			   info.setMsg("保存对象成功!");
		   }
		} catch (Exception e) {
		 log.error("保存对象失败!"+e.getMessage());
		 info.setSucess(false);
		 info.setMsg("保存对象失败!");
		 info.setExceptionMsg(e.getMessage());
		}
	   return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBatch(String userInfo, String saveJson)
			 {
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			  List<UserConfig> userConfigList=JacksonUtils.fromJson(saveJson,ArrayList.class,UserConfig.class);
			   userConfigService.saveBatchList(userConfigList);
				   info.setResult(JacksonUtils.toJson(userConfigList));
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
			   UserConfig userConfig=JacksonUtils.fromJson(updateJson, UserConfig.class);
			   int i = userConfigService.updateUserConfig(userConfig);
				   info.setResult(JacksonUtils.toJson(i));
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
			   UserConfig userConfig=JacksonUtils.fromJson(deleteJson, UserConfig.class);
			   int result= userConfigService.deleteObjectById(userConfig.getId());
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
				   int result= userConfigService.deleteAllObjectByIds(list);
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
			UserConfig userConfig=JacksonUtils.fromJson(getJson, UserConfig.class);
			UserConfig	result = userConfigService.getObjectById(userConfig.getId());
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
				Page page=userConfigService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=userConfigService.getPage(new HashMap(), null, null);
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
				List list=userConfigService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=userConfigService.queryList(null);
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
	public String deletePseudoObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   UserConfig userConfig=JacksonUtils.fromJson(deleteJson, UserConfig.class);
			   int result= userConfigService.deletePseudoObjectById(userConfig.getId());
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
	public String deletePseudoAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= userConfigService.deletePseudoAllObjectByIds(list);
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

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.dto.service.UserConfigDtoServiceCustomer#getUserConfigPage(java.lang.String, java.lang.String)
	 */
	@Override
	public String getUserConfigPage(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=userConfigService.getUserConfigPage(map);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=userConfigService.getUserConfigPage(new HashMap());
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

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.dto.service.UserConfigDtoServiceCustomer#getUserConfig(java.lang.String, java.lang.String)
	 */
	@Override
	public String getUserConfig(String userJson, String getJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			UserConfig userConfig=JacksonUtils.fromJson(getJson, UserConfig.class);
			List<Map<String,Object>>result = userConfigService.getUserConfig(userConfig.getId());
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

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.dto.service.UserConfigDtoServiceCustomer#updateStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public String updateStatus(String userJson, String id) {
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			  UserConfig userConfig = userConfigService.getObjectById(id);
			   int result=   userConfigService.updateStatus(userConfig);
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("更新状态成功!");
			} catch (Exception e) {
			 log.error("更新状态失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("更新状态失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.dto.service.UserConfigDtoServiceCustomer#updateUserConfigBatch(java.lang.String, java.lang.String)
	 */
	@Override
	public String updateUserConfigBatch(String userJson, String updateJson) {
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   UserConfig userConfig=JacksonUtils.fromJson(updateJson, UserConfig.class);
			    userConfigService.updateUserConfigBatch(userConfig);
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


}
