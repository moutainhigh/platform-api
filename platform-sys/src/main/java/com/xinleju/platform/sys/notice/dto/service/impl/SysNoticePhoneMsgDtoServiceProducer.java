package com.xinleju.platform.sys.notice.dto.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.notice.dto.service.SysNoticePhoneMsgDtoServiceCustomer;
import com.xinleju.platform.sys.notice.entity.SysNoticePhoneMsg;
import com.xinleju.platform.sys.notice.service.SysNoticePhoneMsgService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class SysNoticePhoneMsgDtoServiceProducer implements SysNoticePhoneMsgDtoServiceCustomer{
	private static Logger log = Logger.getLogger(SysNoticePhoneMsgDtoServiceProducer.class);
	@Autowired
	private SysNoticePhoneMsgService sysNoticePhoneService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   SysNoticePhoneMsg sysNoticePhone=JacksonUtils.fromJson(saveJson, SysNoticePhoneMsg.class);
		   sysNoticePhoneService.save(sysNoticePhone);
		   info.setResult(JacksonUtils.toJson(sysNoticePhone));
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
			   SysNoticePhoneMsg sysNoticePhone=JacksonUtils.fromJson(updateJson, SysNoticePhoneMsg.class);
			   int result=   sysNoticePhoneService.update(sysNoticePhone);
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
			   SysNoticePhoneMsg sysNoticePhone=JacksonUtils.fromJson(deleteJson, SysNoticePhoneMsg.class);
			   int result= sysNoticePhoneService.deleteObjectById(sysNoticePhone.getId());
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
				Map<String, Object> map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= sysNoticePhoneService.deleteAllObjectByIds(list);
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
			SysNoticePhoneMsg sysNoticePhone=JacksonUtils.fromJson(getJson, SysNoticePhoneMsg.class);
			SysNoticePhoneMsg	result = sysNoticePhoneService.getObjectById(sysNoticePhone.getId());
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
				@SuppressWarnings("unchecked")
				Map<String, Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=sysNoticePhoneService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=sysNoticePhoneService.getPage(new HashMap<String, Object>(), null, null);
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

	@SuppressWarnings("unchecked")
	@Override
	public String queryList(String userInfo, String paramater){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map<String, Object> map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<SysNoticePhoneMsg> list=sysNoticePhoneService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List<SysNoticePhoneMsg> list=sysNoticePhoneService.queryList(null);
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
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   SysNoticePhoneMsg sysNoticePhone=JacksonUtils.fromJson(deleteJson, SysNoticePhoneMsg.class);
			   int result= sysNoticePhoneService.deletePseudoObjectById(sysNoticePhone.getId());
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
				   Map<String, Object> map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= sysNoticePhoneService.deletePseudoAllObjectByIds(list);
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
	public String vaguePage(String userJson, String paramaterJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramaterJson)){
				@SuppressWarnings("unchecked")
				Map<String,Object> map=JacksonUtils.fromJson(paramaterJson, HashMap.class);
				Page page=sysNoticePhoneService.queryVaguePage(map);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=sysNoticePhoneService.getPage(new HashMap<String,Object>(), null, null);
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
	public String sendMsg(String userJson, String paramaterJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramaterJson)){
				@SuppressWarnings("unchecked")
				Map<String,Object> map=JacksonUtils.fromJson(paramaterJson, HashMap.class);
				String str=sysNoticePhoneService.saveSendMsg(map);
				info.setResult(JacksonUtils.toJson(str));
			    info.setSucess(true);
			    info.setMsg("发送短信成功!");
			}else{
				log.error("发送短信失败!参数为空。");
				info.setSucess(false);
				info.setMsg("发送短信失败!参数为空。");
				info.setExceptionMsg("paramaterJson为空。");
			}
		} catch (Exception e) {
			 log.error("发送短信失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("发送短信失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String sendMsgTest(String userJson, String paramaterJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramaterJson)){
				@SuppressWarnings("unchecked")
				Map<String,Object> map=JacksonUtils.fromJson(paramaterJson, HashMap.class);
				String str=sysNoticePhoneService.saveSendMsgTest(map);
				info.setResult(JacksonUtils.toJson(str));
			    info.setSucess(true);
			    info.setMsg("发送短信成功!");
			}else{
				log.error("发送短信失败!参数为空。");
				info.setSucess(false);
				info.setMsg("发送短信失败!参数为空。");
				info.setExceptionMsg("paramaterJson为空。");
			}
		} catch (Exception e) {
			 log.error("发送短信失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("发送短信失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String againSendMsg(String userJson, String paramaterJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramaterJson)){
				@SuppressWarnings("unchecked")
				Map<String,Object> map=JacksonUtils.fromJson(paramaterJson, HashMap.class);
				String str=sysNoticePhoneService.updateAgainSendMsg(map);
				info.setResult(JacksonUtils.toJson(str));
			    info.setSucess(true);
			    info.setMsg("发送短信成功!");
			}else{
				log.error("发送短信失败!参数为空。");
				info.setSucess(false);
				info.setMsg("发送短信失败!参数为空。");
				info.setExceptionMsg("paramaterJson为空。");
			}
		} catch (Exception e) {
			 log.error("发送短信失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("发送短信失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

}
