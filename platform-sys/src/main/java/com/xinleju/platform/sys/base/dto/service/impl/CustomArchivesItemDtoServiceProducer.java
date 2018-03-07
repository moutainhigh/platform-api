package com.xinleju.platform.sys.base.dto.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dto.CustomArchivesItemDto;
import com.xinleju.platform.sys.base.dto.service.CustomArchivesItemDtoServiceCustomer;
import com.xinleju.platform.sys.base.entity.CustomArchivesItem;
import com.xinleju.platform.sys.base.service.CustomArchivesItemService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class CustomArchivesItemDtoServiceProducer implements CustomArchivesItemDtoServiceCustomer{
	private static Logger log = Logger.getLogger(CustomArchivesItemDtoServiceProducer.class);
	@Autowired
	private CustomArchivesItemService customArchivesItemService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   CustomArchivesItem customArchivesItem=JacksonUtils.fromJson(saveJson, CustomArchivesItem.class);
		   customArchivesItemService.save(customArchivesItem);
		   info.setResult(JacksonUtils.toJson(customArchivesItem));
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
		// TODO Auto-generated method stub
		return null;
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
			   CustomArchivesItem customArchivesItem=JacksonUtils.fromJson(updateJson, CustomArchivesItem.class);
			   int result=   customArchivesItemService.update(customArchivesItem);
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
			   CustomArchivesItem customArchivesItem=JacksonUtils.fromJson(deleteJson, CustomArchivesItem.class);
			   int result= customArchivesItemService.deleteObjectById(customArchivesItem.getId());
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
				   int result= customArchivesItemService.deleteAllObjectByIds(list);
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
			CustomArchivesItem customArchivesItem=JacksonUtils.fromJson(getJson, CustomArchivesItem.class);
			CustomArchivesItem	result = customArchivesItemService.getObjectById(customArchivesItem.getId());
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
//				Page page=customArchivesItemService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				Page page=customArchivesItemService.getPageSort(map);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=customArchivesItemService.getPage(new HashMap(), null, null);
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
//				List list=customArchivesItemService.queryList(map);
				List list=customArchivesItemService.queryListSort(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=customArchivesItemService.queryList(null);
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
			   CustomArchivesItem customArchivesItem=JacksonUtils.fromJson(deleteJson, CustomArchivesItem.class);
			   int result= customArchivesItemService.deletePseudoObjectById(customArchivesItem.getId());
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
				   int result= customArchivesItemService.deletePseudoAllObjectByIds(list);
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
	public Integer getCurrentMaxSortByMainId(String mainId) {
		return customArchivesItemService.getCurrentMaxSortByMainId(mainId);
	}

	@Override
	public String saveList(String userInfo, String saveJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			info=customArchivesItemService.saveList(userInfo,saveJson);
		} catch (Exception e) {
			log.error("批量操作失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("批量操作失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String validateIsExist(Object object, String saveJson) {
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   CustomArchivesItem customArchivesItem=JacksonUtils.fromJson(saveJson, CustomArchivesItem.class);
			   Integer isExistCode=customArchivesItemService.validateIsExist(customArchivesItem,"code");
			   Integer isExistName=customArchivesItemService.validateIsExist(customArchivesItem,"name");
			   CustomArchivesItemDto customArchivesItemDto = new CustomArchivesItemDto();
			   if(isExistCode !=null && isExistCode>0){
				   customArchivesItemDto.setCodeExist(true);
			   }
			   if(isExistName !=null && isExistName>0){
				   customArchivesItemDto.setNameExist(true);
			   }
			   info.setSucess(true);
			   info.setMsg("查询成功!");
			   info.setResult(JacksonUtils.toJson(customArchivesItemDto));
			} catch (Exception e) {
			 log.error("查询失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("查询失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
		}

	@Override
	public String validateBeforeSave(String userInfo, String saveJson) {
		return customArchivesItemService.validateBeforeSave(userInfo,saveJson);
	}

	@Override
	public String validateBeforeUpdate(String userInfo, String updateJson) {
		return customArchivesItemService.validateBeforeUpdate(userInfo,updateJson);
	}

}
