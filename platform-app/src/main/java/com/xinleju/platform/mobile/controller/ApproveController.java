package com.xinleju.platform.mobile.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.flow.dto.MobileApproveDto;
import com.xinleju.platform.flow.dto.SysNoticeMsgDto;
import com.xinleju.platform.flow.dto.service.SysNoticeMsgDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 移动端的消息列表和详情的控制层
 * @author zhengjiajie
 *
 */
@Controller
@RequestMapping("/mobile/approve")
public class ApproveController {

	private static Logger log = Logger.getLogger(ApproveController.class);
	
	@Autowired
	private SysNoticeMsgDtoServiceCustomer sysNoticeMsgDtoServiceCustomer;
	
	/**
	 * 根据Id获取业务对象
	 * 
	 * @param id  业务对象主键
	 * 
	 * @return     业务对象
	 */
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult get(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	/**
	 * 返回分页对象
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
		    String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.getPage(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用page方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				//String loginName = userBeanInfo.getSecurityUserDto().getLoginName();
				map.put("userId", userId);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryHaveDoneList >>>userId="+userId);
			}else{
				System.out.println("\n queryList >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}
			
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysNoticeMsgDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysNoticeMsgDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 修改消息的状态
	 * @param paramater [newStatus, oldStatus,  id,  businessId、extendInfo]
	 * @return
	 */
	@RequestMapping(value="/updateStatusOfNoticeMsg",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult updateStatusOfNoticeMsg(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
		
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.updateStatusOfNoticeMsg(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 设置待办消息为打开过状态
	 * 
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/setMessageOpened",method={RequestMethod.GET}, consumes="application/json")
	public @ResponseBody MessageResult setMessageOpened(String messageId){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
		
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.setMessageOpened(userJson, messageId);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用setMessageOpened方法:  【参数"+messageId+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryDBDYList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryDBDYList(@RequestBody Map<String,Object> map){
		long time1 = System.currentTimeMillis();
		System.out.println("\n Controller queryDBDYList >> time1="+time1);
		
		
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				//String loginName = userBeanInfo.getSecurityUserDto().getLoginName();
				map.put("userId", userId);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryHaveDoneList >>>userId="+userId);
			}else{
				System.out.println("\n queryHaveDoneList >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}
			
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.queryDBDYList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysNoticeMsgDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysNoticeMsgDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		long time2 = System.currentTimeMillis();
		System.out.println("\n Controller queryDBDYList >> time2="+time2 );
		System.out.println("\n Controller queryDBDYList >> time2-time1="+(time2-time1) );
		return result;
	}
	
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryHaveDoneList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryHaveDoneList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				//String loginName = userBeanInfo.getSecurityUserDto().getLoginName();
				map.put("userId", userId);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryHaveDoneList >>>userId="+userId);
			}else{
				System.out.println("\n queryHaveDoneList >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}
			
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.queryHaveDoneList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysNoticeMsgDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysNoticeMsgDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	
	/**
	 * 返回待办和待阅的汇总数据
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryTwoSumData",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryStatiscData(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
		
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				//String loginName = userBeanInfo.getSecurityUserDto().getLoginName();
				map.put("userId", userId);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryHaveDoneList >>>userId="+userId);
			}else{
				System.out.println("\n queryStatiscData >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}
			
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.queryTwoSumData(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysNoticeMsgDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysNoticeMsgDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}


	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody SysNoticeMsgDto t){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				////e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 修改修改实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		SysNoticeMsgDto msgDto=null;
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=sysNoticeMsgDtoServiceCustomer.update(null, updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
					 result.setMsg(updateDubboServiceResultInfo.getMsg()+"【"+updateDubboServiceResultInfo.getExceptionMsg()+"】");
				 }
			}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		} catch (Exception e) {
			try{
			 ////e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(msgDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				////e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	

	/**
	 * 伪删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用deletePseudo方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 伪删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo, SysNoticeMsgDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 根据 businessId、userId删除对应的opType、
	 * @param paramater [ opType,  businessId、userId]
	 * @return
	 */
	@RequestMapping(value="/deleteOpTypeDataByParamMap",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult deleteOpTypeDataByParamMap(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
		
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.deleteOpTypeDataByParamMap(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				String datetext = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
				result.setMsg(datetext);
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 返回分页对象
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/pageQueryByParamMap",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult pageQueryByParamMap(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				map.put("userId", userId);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n pageQueryByParamMap >>>userId="+userId);
			}else{
				System.out.println("\n pageQueryByParamMap >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}
			
		    String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.pageQueryByParamMap(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用page方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 根据Id获取业务对象
	 * 
	 * @param id  业务对象主键
	 * 
	 * @return     业务对象
	 */
	/*@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult get(@PathVariable("id")  String id){*/
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryMobileApprove",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryMobileApprove(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		System.out.println("\n queryMobileApprove >>>paramaterJson="+paramaterJson);
		try {
			//当前登录用户
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			if(userBeanInfo!=null && userBeanInfo.getSecurityUserDto()!=null){
				String userId = userBeanInfo.getSecurityUserDto().getId();
				map.put("userId", userId);
				paramaterJson = JacksonUtils.toJson(map);
				System.out.println("\n queryMobileApprove 001 >>>userId="+userId);
				System.out.println("\n queryMobileApprove 002 >>>paramaterJson="+paramaterJson);
				
			}else{
				System.out.println("\n queryMobileApprove >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}
			System.out.println("\n\n queryMobileApprove()>> userJson="+userJson);
			String dubboResultInfo = sysNoticeMsgDtoServiceCustomer.queryMobileApproveByParamMap(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				MobileApproveDto msgDto=JacksonUtils.fromJson(resultInfo, MobileApproveDto.class);
				result.setResult(msgDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	
	@RequestMapping(value="/uploadFile", method = RequestMethod.POST)
	public @ResponseBody MessageResult uploadFile(MultipartHttpServletRequest request) {
		System.out.println("\n\n ========================================================================"
				+ "\n001 uploadFile() is called...");
		MessageResult result=new MessageResult();
		Iterator<String> itr = request.getFileNames();
		try {
			while (itr.hasNext()) {
				final MultipartFile uploadFile = request.getFile(itr.next());
				String uploadFileName = uploadFile.getOriginalFilename();// 文件名
				System.out.println("002 uploadFileName="+uploadFileName);
			}
			System.out.println("\n\n ========================================================================");
		} catch (Exception e) {
				////e.printStackTrace();
				log.error("调用upload方法: 【" + e.getMessage() + "】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}
}
