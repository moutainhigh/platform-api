package com.xinleju.platform.sys.base.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dto.BaseProjectTypeDto;
import com.xinleju.platform.sys.base.dto.service.BaseProjectTypeDtoServiceCustomer;
import com.xinleju.platform.sys.base.entity.BaseProjectType;
import com.xinleju.platform.sys.base.service.BaseProjectTypeService;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.utils.ResourceType;
import com.xinleju.platform.tools.data.JacksonUtils;
import org.springframework.beans.factory.support.ManagedProperties;

/**
 * @author admin
 * 
 *
 */
 
public class BaseProjectTypeDtoServiceProducer implements BaseProjectTypeDtoServiceCustomer{
	private static Logger log = Logger.getLogger(BaseProjectTypeDtoServiceProducer.class);
	@Autowired
	private BaseProjectTypeService baseProjectTypeService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   BaseProjectType baseProjectType=JacksonUtils.fromJson(saveJson, BaseProjectType.class);
		   int result = baseProjectTypeService.saveBaseProjectType(baseProjectType);
		   info.setResult(JacksonUtils.toJson(baseProjectType));
		   if(result==5){
			   info.setSucess(false);
			   info.setMsg("保存对象失败，数据库已存在"); 
		   }else{
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
			   BaseProjectType baseProjectType=JacksonUtils.fromJson(updateJson, BaseProjectType.class);
			   int result=   baseProjectTypeService.updateProjectType(baseProjectType);
			   info.setResult(JacksonUtils.toJson(result));
			   if(result==5){
				   info.setSucess(false);
				   info.setMsg("更新对象失败，数据库已存在"); 
			   }else{
				   info.setSucess(true);
				   info.setMsg("更新对象成功!");
			   }
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
			   BaseProjectType baseProjectType=JacksonUtils.fromJson(deleteJson, BaseProjectType.class);
			   int result= baseProjectTypeService.deleteObjectById(baseProjectType.getId());
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
				   int result= baseProjectTypeService.deleteAllObjectByIds(list);
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
			BaseProjectType baseProjectType=JacksonUtils.fromJson(getJson, BaseProjectType.class);
			BaseProjectType	result = baseProjectTypeService.getObjectById(baseProjectType.getId());
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
				Page page=baseProjectTypeService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=baseProjectTypeService.getPage(new HashMap(), null, null);
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
				List list=baseProjectTypeService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=baseProjectTypeService.queryList(null);
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
	public String getTypetree(String userinfo, String paramaterJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
        Map<String,Object> map = JacksonUtils.fromJson(paramaterJson, HashMap.class);
     /*   String status = (String) map.get("status");*/
        List<BaseProjectTypeDto> list=baseProjectTypeService.queryBaseProjecType(map);
     	info.setResult(JacksonUtils.toJson(list));
	    info.setSucess(true);
	    info.setMsg("获取树对象成功!");
		System.out.println(JacksonUtils.toJson(list));
	}catch(Exception e){
		log.error("获取树对象失败!"+e.getMessage());
		 info.setSucess(false);
		 info.setMsg("获取树对象失败!");
		 info.setExceptionMsg(e.getMessage());
	}
	return JacksonUtils.toJson(info);
	}


	@Override
	public String updateSort(String userinfo, String updateJson,
			Map<String, Object> map) {
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseProjectType baseProjectType=JacksonUtils.fromJson(updateJson, BaseProjectType.class);
			   BaseProjectType object= baseProjectTypeService.getObjectById(baseProjectType.getId());
			   int result=   baseProjectTypeService.updateSort(object,map);
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
	public String updateName(String userinfo, String updateJson,
			Map<String, Object> map) {
		  DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseProjectType baseProjectType=JacksonUtils.fromJson(updateJson, BaseProjectType.class);
			   BaseProjectType object= baseProjectTypeService.getObjectById(baseProjectType.getId());
			   int result=   baseProjectTypeService.updateName(object,map);
			   info.setResult(JacksonUtils.toJson(result));
			   if(result==5){
				   info.setSucess(false);
				   info.setMsg("保存对象失败，数据库已存在"); 
			   }else{
				   info.setSucess(true);
				   info.setMsg("保存对象成功!");
			   }
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String updateStatus(String userinfo, String updateJson) {
		  DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseProjectType baseProjectType=JacksonUtils.fromJson(updateJson, BaseProjectType.class);
			   Map<String,Object> paramMap = JacksonUtils.fromJson (updateJson, Map.class);
			   BaseProjectType object= baseProjectTypeService.getObjectById(baseProjectType.getId());
			   int result=   baseProjectTypeService.updateStatus(object,paramMap);
			   info.setMsg("更新对象成功");
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}


	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson) {
		  DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseProjectType baseProjectType=JacksonUtils.fromJson(deleteJson, BaseProjectType.class);
			   BaseProjectType object= baseProjectTypeService.getObjectById(baseProjectType.getId());
			   int result=   baseProjectTypeService.deletePseudoProjectTypeById(baseProjectType.getId());
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
	public String deletePseudoAllObjectByIds(String userInfo,
			String deleteJsonList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String,Object>> getLeafBaseProjectType(String userInfo) {
		List<Map<String,Object>> list=null;
		try {
			list = baseProjectTypeService.getLeafBaseProjectType();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String json = JacksonUtils.toJson(list);
		List<Map<String,Object>> resultList=JacksonUtils.fromJson(json, ArrayList.class,Map.class);
		return resultList;
	}
}
