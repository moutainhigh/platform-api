package com.xinleju.platform.sys.num.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.sys.num.dto.BillDto;
import com.xinleju.platform.sys.num.dto.service.BillDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 编号规则类别控制层
 * @author ly
 *
 */
@Controller
@RequestMapping("/sys/num/bill")
public class BillController { 

	private static Logger log = Logger.getLogger(BillController.class);
	
	@Autowired
	private BillDtoServiceCustomer billDtoServiceCustomer;
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=billDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BillDto billDto=JacksonUtils.fromJson(resultInfo, BillDto.class);
				result.setResult(billDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setCode(dubboServiceResultInfo.getResult());
			    result.setSuccess(dubboServiceResultInfo.isSucess());
			    result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
		    String dubboResultInfo=billDtoServiceCustomer.getPage(userJson, paramaterJson);
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=billDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<BillDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,BillDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}


	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody BillDto t){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=billDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BillDto billDto=JacksonUtils.fromJson(resultInfo, BillDto.class);
				result.setResult(billDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			} catch (JsonProcessingException e1) {
				//e1.printStackTrace();
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=billDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BillDto billDto=JacksonUtils.fromJson(resultInfo, BillDto.class);
				result.setResult(billDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=billDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BillDto billDto=JacksonUtils.fromJson(resultInfo, BillDto.class);
				result.setResult(billDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
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
		BillDto billDto=null;
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		Timestamp timestamp = Timestamp.valueOf(dateFormater.format(date));
		map.put("updateDate",timestamp);
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=billDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 @SuppressWarnings("unchecked")
				Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=billDtoServiceCustomer.update(userJson, updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setCode(updateDubboServiceResultInfo.getResult());
				     result.setSuccess(updateDubboServiceResultInfo.isSucess());
				     result.setMsg(updateDubboServiceResultInfo.getMsg());
				 }
			}else{
				 result.setCode(ErrorInfoCode.NULL_ERROR.getValue());
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		}catch (Exception e) {
			try{
			 ////e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(billDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			 result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			}catch (JsonProcessingException e1) {
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=billDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BillDto billDto=JacksonUtils.fromJson(resultInfo, BillDto.class);
				result.setResult(billDto);
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
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=billDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BillDto billDto=JacksonUtils.fromJson(resultInfo, BillDto.class);
				result.setResult(billDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
				result.setCode(dubboServiceResultInfo.getResult());
			    result.setSuccess(dubboServiceResultInfo.isSucess());
			    result.setMsg(dubboServiceResultInfo.getMsg());
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数ids"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	/**
	 * 对业务对象设置启用 禁用
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/updateStatus/{id}",method=RequestMethod.PUT)
	public @ResponseBody MessageResult updateStatus(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=billDtoServiceCustomer.updateStatus(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				int i=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Integer.class);
				result.setResult(i);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
				result.setCode(dubboServiceResultInfo.getResult());
			    result.setSuccess(dubboServiceResultInfo.isSucess());
			    result.setMsg(dubboServiceResultInfo.getMsg());
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用updateStatus方法:  【参数id"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/saveBillAndRuler",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult saveBillAndRuler(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String saveJson= JacksonUtils.toJson(map);
			String dubboResultInfo=billDtoServiceCustomer.saveBillAndRuler(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BillDto billDto=JacksonUtils.fromJson(resultInfo, BillDto.class);
				result.setResult(billDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getExceptionMsg());
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(map);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			} catch (JsonProcessingException e1) {
				//e1.printStackTrace();
			}
		}
		return result;
	}
	@RequestMapping(value="/getLoginUserID",method=RequestMethod.GET)
	public @ResponseBody MessageResult getLoginUserID(){
		//获取用户对象
		SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
		MessageResult result=new MessageResult();
		String loginName = user.getSecurityUserDto().getLoginName();
		result.setResult(loginName==null?user.getSecurityUserDto().getId():loginName);
		result.setSuccess(MessageInfo.GETSUCCESS.isResult());
		result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		return result;
	}
}
