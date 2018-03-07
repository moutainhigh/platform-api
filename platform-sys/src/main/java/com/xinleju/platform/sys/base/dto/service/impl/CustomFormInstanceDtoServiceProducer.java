package com.xinleju.platform.sys.base.dto.service.impl;

import java.util.ArrayList;
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
import com.xinleju.platform.sys.base.dto.CustomFormInstanceDto;
import com.xinleju.platform.sys.base.dto.service.CustomFormInstanceDtoServiceCustomer;
import com.xinleju.platform.sys.base.entity.CommonDraft;
import com.xinleju.platform.sys.base.entity.CustomFormInstance;
import com.xinleju.platform.sys.base.service.CommonDraftService;
import com.xinleju.platform.sys.base.service.CustomFormInstanceService;
import com.xinleju.platform.sys.res.entity.AppSystem;
import com.xinleju.platform.sys.res.service.AppSystemService;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.utils.Common;

/**
 * @author admin
 * 
 *
 */
 
public class CustomFormInstanceDtoServiceProducer implements CustomFormInstanceDtoServiceCustomer{
	private static Logger log = Logger.getLogger(CustomFormInstanceDtoServiceProducer.class);
	@Autowired
	private CustomFormInstanceService customFormInstanceService;
	
	@Autowired
	private AppSystemService appSystemService;

	@Autowired
	private CommonDraftService commonDraftService;
	
	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   CustomFormInstance customFormInstance=JacksonUtils.fromJson(saveJson, CustomFormInstance.class);
		   customFormInstanceService.saveInstance(customFormInstance);
		   info.setResult(JacksonUtils.toJson(customFormInstance));
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

	private Integer getNumber(String formNumber) {
		formNumber=formNumber.substring(formNumber.lastIndexOf("-")+1);
		return Integer.valueOf(formNumber);
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
			   CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(updateJson, CustomFormInstanceDto.class);
			   int result = customFormInstanceService.updateInstance(customFormInstanceDto);
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
			   CustomFormInstance customFormInstance=JacksonUtils.fromJson(deleteJson, CustomFormInstance.class);
			   int result= customFormInstanceService.deleteObjectById(customFormInstance.getId());
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
				   int result= customFormInstanceService.deleteAllObjectByIds(list);
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
			CustomFormInstance customFormInstance=JacksonUtils.fromJson(getJson, CustomFormInstance.class);
			CustomFormInstance	result = customFormInstanceService.getObjectById(customFormInstance.getId());
			CustomFormInstanceDto customFormInstanceDto=new CustomFormInstanceDto();
			BeanUtils.copyProperties(result, customFormInstanceDto);
			//查询系统appid
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("delflag", false);
			map.put("code", "OA");
			List<AppSystem> appList=appSystemService.queryList(map);
			String code=customFormInstanceService.isCustomformInstance(customFormInstance.getId());
			customFormInstanceDto.setDocumentType(code);
			String appId=(appList!=null && appList.size()>0)?appList.get(0).getId():null;
			customFormInstanceDto.setAppId(appId);
			info.setResult(JacksonUtils.toJson(customFormInstanceDto));
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
//				Page page=customFormInstanceService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				Page page=customFormInstanceService.getPageForForm(userInfo,map);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=customFormInstanceService.getPage(new HashMap(), null, null);
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
				List list=customFormInstanceService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=customFormInstanceService.queryList(null);
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
			   CustomFormInstance customFormInstance=JacksonUtils.fromJson(deleteJson, CustomFormInstance.class);
			   int result= customFormInstanceService.deletePseudoObjectById(customFormInstance.getId());
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
				   int result= customFormInstanceService.deleteByIds(list);
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
	public String getInstanceByFormId(String userInfo, String jsonStr) {
		return customFormInstanceService.getInstanceByFormId(userInfo,jsonStr);
	}

	@Override
	public String getVariableById(String userInfo, String idJson) {
		return customFormInstanceService.getVariableById(userInfo,idJson);
	}

	@Override
	public String updateStatus(String userInfo, String updateJson) {
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   CustomFormInstance customFormInstance=new CustomFormInstance();
		   CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(updateJson, CustomFormInstanceDto.class);
		   try {
			   CustomFormInstance customFormInstanceData=customFormInstanceService.getObjectById(customFormInstanceDto.getBusinessId());
			   String status=customFormInstanceDto.getStatus();
			   switch (status){
	               case "1":
	            	   customFormInstance.setStatus(Common.SH_SHZ_STATE);
	                   break;
	               case "2":
	            	   customFormInstance.setStatus(Common.SH_SHWC_STATE);
	            	   break;
	               case "3":
	               case "4":
	               case "7":
	            	   customFormInstance.setStatus(Common.SH_WSH_STATE);
	            	   break;
	               case "9":
	            	   customFormInstance.setStatus(Common.SH_SHZ_STATE);
	            	   break;
               }
			   customFormInstance.setId(customFormInstanceDto.getBusinessId());
			   customFormInstance.setInstanceId(customFormInstanceDto.getInstanceId());
			   
			   if(!customFormInstanceData.getStatus().equals(customFormInstance.getStatus())){
				   String statusText=getStatusText(customFormInstance.getStatus());
				   String pcValueJson=toUpdateFormValueJson(customFormInstanceData.getFormValueJson(),statusText);
				   String mobileValueJson=toUpdateMobileValueJson(customFormInstanceData.getFormMobileValueJson(),statusText);
				   customFormInstance.setFormValueJson(pcValueJson);
				   customFormInstance.setFormMobileValueJson(mobileValueJson);
			   }
			   
			   int result=customFormInstanceService.updateStatus(customFormInstance);
			   if(result>0){
				   if("0".equals(customFormInstance.getStatus())){
					   //打回等情况恢复草稿
					   CommonDraft commonDraft = commonDraftService.getObjectById(customFormInstance.getId());
					   if(commonDraft!=null){
						   commonDraft.setDelflag(false);
						   commonDraftService.update(commonDraft);
					   }
				   }else if("1".equals(customFormInstance.getStatus())){
					   commonDraftService.deletePseudoObjectById(customFormInstance.getId());
				   }
			   }
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("更新状态成功!");
			} catch (Exception e) {
				log.error("流程调用更新状态失败!"+"参数："+customFormInstanceDto.getStatus()+","+customFormInstanceDto.getBusinessId()+","+customFormInstanceDto.getInstanceId()+"异常信息："+e);
				info.setSucess(false);
				info.setMsg("更新状态失败!");
				info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	private String toUpdateMobileValueJson(String formMobileValueJson, String statusText) {
		boolean flag=false;
		List<Map<String, Object>> mobileFormValueList=JacksonUtils.fromJson(formMobileValueJson, ArrayList.class);
		if(mobileFormValueList!=null && mobileFormValueList.size()>0){
			for(int i=0;i<mobileFormValueList.size();i++){
				Map<String, Object> reMap=mobileFormValueList.get(i);
				if(reMap!=null && reMap.get("type")!=null){
					String type=(String) reMap.get("type");
					if("Status".equals(type)){
						reMap.put("value", statusText);
						flag=true;
						break;
					}
				}
			}
			if(flag){
				return JacksonUtils.toJson(mobileFormValueList);
			}else{
				return formMobileValueJson;
			}
		}else{
			return formMobileValueJson;
		}
	}

	private String toUpdateFormValueJson(String formValueJson, String statusText) {
		Map<String,Map<String,Object>> resultFormJsonMap = JacksonUtils.fromJson(formValueJson, HashMap.class);
		boolean flag=false;
		if(resultFormJsonMap!=null && resultFormJsonMap.size()>0){
			for (Map<String,Object> reMap : resultFormJsonMap.values()) {  
				String cmpName=(String) reMap.get("cmpName");		
				if("Status".equals(cmpName)){
					reMap.put("cmpValue", statusText);
					reMap.put("cmpValueShowName", statusText);
					flag=true;
					break;
				}
			}
			if(flag){
				return JacksonUtils.toJson(resultFormJsonMap);
			}else{
				return formValueJson;
			}
		}else{
			return formValueJson;
		}
	}

	private String getStatusText(String status) {
		if(status!=null && !"".equals(status)){
			if(Common.SH_WSH_STATE.equals(status)){
				return Common.SH_WSH_STATE_TEXT;
			}else if(Common.SH_SHZ_STATE.equals(status)){
				return Common.SH_SHZ_STATE_TEXT;
			}else if(Common.SH_SHWC_STATE.equals(status)){
				return Common.SH_SHWC_STATE_TEXT;
			}
		}
		return "";
	}

	@Override
	public String updateStatusForCustomForm(String userInfo, String updateJson) {
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   int result=customFormInstanceService.updateStatusForCustomForm(updateJson);
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
	public String getFundPage(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=customFormInstanceService.getFundPageForForm(userInfo,map);
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
	public String importFundPayment(String json, String dataParam) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map=JacksonUtils.fromJson(dataParam, HashMap.class);
			List<String> list=Arrays.asList(map.get("id").toString().split(","));
			info=customFormInstanceService.importFundPayment(list);
		} catch (Exception e) {
			log.error("保存对象失败!"+e.getMessage());
			info.setSucess(false);
		 	info.setMsg("保存对象失败!");
		 	info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String synFund(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				customFormInstanceService.synFund(map);
				info.setResult(null);
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPageCustom(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=customFormInstanceService.getPageForForm(userInfo,map);
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
	public String getMyFormPage(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=customFormInstanceService.getMyFormPage(userInfo,map);
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
}
