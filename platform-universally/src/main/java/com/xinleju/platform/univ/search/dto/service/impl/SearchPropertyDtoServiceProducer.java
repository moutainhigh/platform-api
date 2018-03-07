package com.xinleju.platform.univ.search.dto.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.univ.search.dto.service.SearchPropertyDtoServiceCustomer;
import com.xinleju.platform.univ.search.entity.SearchProperty;
import com.xinleju.platform.univ.search.service.SearchPropertyService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author haoqp
 * 
 *
 */
 
public class SearchPropertyDtoServiceProducer implements SearchPropertyDtoServiceCustomer{
	private static Logger log = Logger.getLogger(SearchPropertyDtoServiceProducer.class);
	@Autowired
	private SearchPropertyService searchPropertyService;

	public String save(String userInfo, String saveJson) {
		
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			SearchProperty searchProperty = JacksonUtils.fromJson(saveJson, SearchProperty.class);
			
			// 编码唯一性验证
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("code", searchProperty.getCode());
			int count = searchPropertyService.getCount(paramMap);
			if (count > 0) {
				info.setSucess(false);
				info.setMsg("检索属性的编码重复，请重新输入");
			} else {
				paramMap.clear();
				paramMap.put("name", searchProperty.getName());
				count = searchPropertyService.getCount(paramMap);
				// 名称唯一性验证
				if (count > 0) {
					info.setSucess(false);
					info.setMsg("检索属性的名称重复，请重新输入");
				} else {
					searchPropertyService.save(searchProperty);
					info.setResult(JacksonUtils.toJson(searchProperty));
					info.setSucess(true);
					info.setMsg("保存检索属性对象成功!");
				}
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
	public String update(String userInfo, String updateJson) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		try {
			SearchProperty searchProperty = JacksonUtils.fromJson(updateJson, SearchProperty.class);
			
			// 编码唯一性验证
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("id", searchProperty.getId());
			paramMap.put("code", searchProperty.getCode());
			int count = searchPropertyService.getCountForUpdate(paramMap);
			if (count > 0) {
				info.setSucess(false);
				info.setMsg("检索属性的编码重复，请重新输入");
			} else {
				paramMap.remove("code");
				paramMap.put("name", searchProperty.getName());
				count = searchPropertyService.getCountForUpdate(paramMap);
				// 名称唯一性验证
				if (count > 0) {
					info.setSucess(false);
					info.setMsg("检索属性的名称重复，请重新输入");
				} else {
					int result = searchPropertyService.update(searchProperty);
					info.setResult(JacksonUtils.toJson(result));
					info.setSucess(true);
					info.setMsg("更新检索属性对象成功!");
				}
			}
			
		} catch (Exception e) {
			log.error("更新对象失败!" + e.getMessage());
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
			   SearchProperty searchProperty=JacksonUtils.fromJson(deleteJson, SearchProperty.class);
			   int result= searchPropertyService.deleteObjectById(searchProperty.getId());
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
				   int result= searchPropertyService.deleteAllObjectByIds(list);
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
			SearchProperty searchProperty=JacksonUtils.fromJson(getJson, SearchProperty.class);
			SearchProperty	result = searchPropertyService.getObjectById(searchProperty.getId());
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
				Page page=searchPropertyService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=searchPropertyService.getPage(new HashMap(), null, null);
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
				List list=searchPropertyService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=searchPropertyService.queryList(null);
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
			   SearchProperty searchProperty=JacksonUtils.fromJson(deleteJson, SearchProperty.class);
			   int result= searchPropertyService.deletePseudoObjectById(searchProperty.getId());
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
				   int result= searchPropertyService.deletePseudoAllObjectByIds(list);
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


}
