package com.xinleju.platform.sys.num.dto.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.sys.num.dto.service.FormVariableDtoServiceCustomer;
import com.xinleju.platform.sys.num.entity.FormVariable;
import com.xinleju.platform.sys.num.service.FormVariableService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class FormVariableDtoServiceProducer implements FormVariableDtoServiceCustomer{
	private static Logger log = Logger.getLogger(FormVariableDtoServiceProducer.class);
	@Autowired
	private FormVariableService formVariableService;

	public String save(String userInfo, String saveJson){
		
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		Timestamp timestamp = Timestamp.valueOf(dateFormater.format(date));
		
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   //获取用户对象
		   SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
		   FormVariable formVariable=JacksonUtils.fromJson(saveJson, FormVariable.class);
		   //用户信息赋值
		   formVariable.setCreateDate(timestamp);
		   formVariable.setTendId(user.getTendId());
		   formVariable.setCreatePersonId(user.getSecurityUserDto().getId());
		   formVariable.setCreatePersonName(user.getSecurityUserDto().getLoginName());
		   formVariable.setCreateCompanyId(user.getSecurityUserDto().getBelongOrgId());
		   formVariable.setCreateCompanyName(user.getSecurityUserDto().getBelongOrgName());
		   formVariable.setCreateOrgId(user.getSecurityUserDto().getBelongOrgId());
		   formVariable.setCreateOrgName(user.getSecurityUserDto().getBelongOrgName());
		   
		   formVariableService.save(formVariable);
		   info.setResult(JacksonUtils.toJson(formVariable));
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
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		Timestamp timestamp = Timestamp.valueOf(dateFormater.format(date));
		
	    DubboServiceResultInfo info=new DubboServiceResultInfo();
	    try {
	    	//获取用户对象
		   SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
		   FormVariable formVariable=JacksonUtils.fromJson(updateJson, FormVariable.class);
		   
		   formVariable.setUpdateDate(timestamp);
		   formVariable.setUpdatePersonId(user.getSecurityUserDto().getId());
		   formVariable.setUpdatePersonName(user.getSecurityUserDto().getLoginName());
		   int result=   formVariableService.update(formVariable);
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
			   FormVariable formVariable=JacksonUtils.fromJson(deleteJson, FormVariable.class);
			   int result= formVariableService.deleteObjectById(formVariable.getId());
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
				   @SuppressWarnings("unchecked")
				   Map<String,Object> map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= formVariableService.deleteAllObjectByIds(list);
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
			FormVariable formVariable=JacksonUtils.fromJson(getJson, FormVariable.class);
			FormVariable	result = formVariableService.getObjectById(formVariable.getId());
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
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				@SuppressWarnings("unchecked")
				Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=formVariableService.getFormVariableByPage(map);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=formVariableService.getPage(new HashMap<String,Object>(), null, null);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
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
				@SuppressWarnings("unchecked")
				Map<String,Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<FormVariable> list=formVariableService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List<FormVariable> list=formVariableService.queryList(null);
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
			   FormVariable formVariable=JacksonUtils.fromJson(deleteJson, FormVariable.class);
			   int result= formVariableService.deletePseudoObjectById(formVariable.getId());
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
				   @SuppressWarnings("unchecked")
				   Map<String,Object> map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= formVariableService.deletePseudoAllObjectByIds(list);
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
	public String saveAllFormVariable(String userJson, String saveJson) {
	    DubboServiceResultInfo info=new DubboServiceResultInfo();
	    try {
	    	@SuppressWarnings("unchecked")
			List<Map<String,Object>> formVariableList = JacksonUtils.fromJson(saveJson,List.class);
		    formVariableService.saveAllFormVariable(formVariableList);
		    info.setResult(JacksonUtils.toJson(saveJson));
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


}
