package com.xinleju.platform.sys.base.dto.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dto.CustomFormDto;
import com.xinleju.platform.sys.base.dto.service.CustomFormDtoServiceCustomer;
import com.xinleju.platform.sys.base.entity.CustomForm;
import com.xinleju.platform.sys.base.entity.CustomFormGroup;
import com.xinleju.platform.sys.base.service.CustomFormGroupService;
import com.xinleju.platform.sys.base.service.CustomFormService;
import com.xinleju.platform.sys.res.entity.AppSystem;
import com.xinleju.platform.sys.res.service.AppSystemService;
import com.xinleju.platform.sys.res.service.DataItemService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class CustomFormDtoServiceProducer implements CustomFormDtoServiceCustomer{
	private static Logger log = Logger.getLogger(CustomFormDtoServiceProducer.class);
	@Autowired
	private CustomFormService customFormService;
	
	@Autowired
	private AppSystemService appSystemService;

	@Autowired
	private CustomFormGroupService customFormGroupService;
	
	@Autowired
	private DataItemService dataItemService;
	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   CustomForm customForm=JacksonUtils.fromJson(saveJson, CustomForm.class);
		   Integer maxSort=customFormService.queryMaxSort(customForm.getParentId());
		   customForm.setSort(maxSort==null?1L:Long.valueOf(maxSort+1));
		   customFormService.save(customForm);
		   info.setResult(JacksonUtils.toJson(customForm));
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
			   CustomForm customForm=JacksonUtils.fromJson(updateJson, CustomForm.class);
			   int result=   customFormService.update(customForm);
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
			   CustomForm customForm=JacksonUtils.fromJson(deleteJson, CustomForm.class);
			   int result= customFormService.deleteObjectById(customForm.getId());
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
				   int result= customFormService.deleteAllObjectByIds(list);
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
			CustomForm customForm=JacksonUtils.fromJson(getJson, CustomForm.class);
			CustomForm	result = customFormService.getObjectById(customForm.getId());
			CustomFormDto customFormDto = new CustomFormDto();
			CustomFormGroup customFormGroup=customFormGroupService.getObjectById(result.getParentId());
			CustomFormGroup customFormGroupOne=null;
			if(customFormGroup!=null){
				customFormGroupOne=customFormGroupService.getObjectById(customFormGroup.getParentId());
			}
			BeanUtils.copyProperties(result, customFormDto);
			customFormDto.setLevelOneName(customFormGroupOne!=null?customFormGroupOne.getName():"");
			info.setResult(JacksonUtils.toJson(customFormDto));
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

	private String getDataItem(List<Map<String, Object>> reList, String item) {
		String str="";
		for(int i=0;i<reList.size();i++){
			Map<String, Object> map=reList.get(i);
			if(map.get("itemCode")!=null && map.get("itemCode").equals(item)){
				str+="<option selected = 'selected' value='"+map.get("itemCode")+"'>"+map.get("itemName")+"</option>";
			}else{
				str+="<option value='"+map.get("itemCode")+"'>"+map.get("itemName")+"</option>";
			}
		}
		return str;
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
//				Page page=customFormService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				Page page=customFormService.getPageSort(map);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=customFormService.getPage(new HashMap(), null, null);
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
				List list=customFormService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=customFormService.queryList(null);
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
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		CustomFormDto customFormDto = new CustomFormDto();
		boolean flag=false;
		Integer errorLine=0;
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<String> idsList=Arrays.asList(map.get("id").toString().split(","));
				
				if(idsList!=null && idsList.size()>0){
					for(int i=0;i<idsList.size();i++){
						Integer result = customFormService.isHasInstanceByFormId(idsList.get(i));
						if(result>0){
							flag=true;
							errorLine=i+1;
							break;
						}
					}
				}
				if(flag){
					customFormDto.setValidateRow(errorLine);
					customFormDto.setHasInstance(flag);
				}else{
					customFormDto.setValidateRow(errorLine);
					customFormDto.setHasInstance(flag);
				}
				info.setResult(JacksonUtils.toJson(customFormDto));
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
	public String deletePseudoObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   CustomForm customForm=JacksonUtils.fromJson(deleteJson, CustomForm.class);
//			   int result= customFormService.deletePseudoObjectById(customForm.getId());
			   int result= customFormService.deleteCustomFormById(userInfo,customForm.getId());
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
				   if(list!=null && list.size()>0){
					   for(int i=0;i<list.size();i++){
						   customFormService.deleteCustomFormById(userInfo,list.get(i));
					   }
				   }
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
	public String validateIsExist(String userInfo, String saveJson) {
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   CustomForm customForm=JacksonUtils.fromJson(saveJson, CustomForm.class);
			   Integer isExistCode=customFormService.validateIsExist(customForm,"code");
			   Integer isExistName=customFormService.validateIsExist(customForm,"name");
			   CustomFormDto customFormExistDto = new CustomFormDto();
			   if(isExistCode !=null && isExistCode>0){
				   customFormExistDto.setCodeExist(true);
			   }
			   if(isExistName !=null && isExistName>0){
				   customFormExistDto.setNameExist(true);
			   }
			   info.setSucess(true);
			   info.setMsg("查询成功!");
			   info.setResult(JacksonUtils.toJson(customFormExistDto));
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
		return customFormService.validateBeforeSave(userInfo,saveJson);
	}

	@Override
	public String validateBeforeUpdate(String userInfo, String updateJson) {
		return customFormService.validateBeforeUpdate(userInfo,updateJson);
	}

	@Override
	public String getTemplateTree(String userInfo, String paramaterJson) {
		return customFormService.getTemplateTree(userInfo,paramaterJson);
	}

	@Override
	public String validateBeforeCopy(String userInfo, String copyJson) {
		return customFormService.validateBeforeCopy(userInfo,copyJson);
	}

	@Override
	public String getTemplateById(String userInfo, String idJson) {
		return customFormService.getTemplateById(userInfo,idJson);
	}

	@Override
	public String saveGenerateData(String userInfo, String saveJson) {
		return customFormService.saveGenerateData(userInfo,saveJson);
	}

	@Override
	public String getFormNumber(String userInfo, String saveJson) {
		return customFormService.getFormNumber(userInfo,saveJson);
	}

	@Override
	public String queryListForQuickEntry(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List list=customFormService.queryListForQuickEntry(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=customFormService.queryListForQuickEntry(null);
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
	public String updateSort(String userJson, String updateJson, Map<String, Object> map) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   CustomForm customForm=JacksonUtils.fromJson(updateJson, CustomForm.class);
			   CustomForm object= customFormService.getObjectById(customForm.getId());
			   int result=   customFormService.updateSort(object,map);
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
	public String getObjectByIdForUpdate(String userInfo, String getJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			CustomForm customForm=JacksonUtils.fromJson(getJson, CustomForm.class);
			CustomForm	result = customFormService.getObjectById(customForm.getId());
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("delflag", false);
			paramMap.put("code", "OA");
			List<AppSystem> resultList=appSystemService.queryList(paramMap);
			String dataItemStr="";
			if(resultList!=null && resultList.size()>0){
				AppSystem appSystem=resultList.get(0);
				paramMap.remove("code");
				paramMap.put("appId", appSystem.getId());
				List<Map<String, Object>> reList=dataItemService.queryDataItemAndPointList(paramMap);
				dataItemStr=this.getDataItem(reList,result.getDataItemId());
			}
			
			CustomFormDto customFormDto=new CustomFormDto();
			BeanUtils.copyProperties(result, customFormDto);
			customFormDto.setDataItem(dataItemStr);
			info.setResult(JacksonUtils.toJson(customFormDto));
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
	public String updateForPublish(String userInfo, String updateJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map=JacksonUtils.fromJson(updateJson, HashMap.class);
			Integer isTemplateChange=(Integer) map.get("isTemplateChange");
			CustomForm customForm=JacksonUtils.fromJson(updateJson, CustomForm.class);
			int result=   customFormService.updateForPublish(customForm,isTemplateChange.toString());
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
	public String saveGenerateDataEx(String userInfo, String saveJson) {
		return customFormService.saveGenerateDataEx(userInfo,saveJson);
	}

	@Override
	public String saveGenerateDataExInstance(String userInfo, String saveJson) {
		return customFormService.saveGenerateDataExInstance(userInfo,saveJson);
	}

}
