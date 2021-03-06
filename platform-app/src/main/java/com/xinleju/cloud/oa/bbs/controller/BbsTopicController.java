package com.xinleju.cloud.oa.bbs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinleju.cloud.oa.bbs.dto.BbsForumDto;
import com.xinleju.cloud.oa.bbs.dto.service.BbsForumDtoServiceCustomer;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.uitls.LoginUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.bbs.dto.BbsTopicDto;
import com.xinleju.cloud.oa.bbs.dto.service.BbsTopicDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;


/**
 * 主题（帖子）控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/oa/bbs/topic")
public class BbsTopicController {

	private static Logger log = Logger.getLogger(BbsTopicController.class);
	
	@Autowired
	private BbsTopicDtoServiceCustomer bbsTopicDtoServiceCustomer;
	@Autowired
	private BbsForumDtoServiceCustomer bbsForumDtoServiceCustomer;
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
			String dubboResultInfo=bbsTopicDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsTopicDto bbsTopicDto=JacksonUtils.fromJson(resultInfo, BbsTopicDto.class);
				result.setResult(bbsTopicDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg());
				result.setCode (dubboServiceResultInfo.getCode ());
			}
		} catch (Exception e) {
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	
	/**
	  * @Description:查询用户信息
	  * @author:zhangfangzhi
	  * @date 2017年6月23日 下午2:15:26
	  * @version V1.0
	 */
	@RequestMapping(value="/getUserInfo",method=RequestMethod.GET)
	public @ResponseBody MessageResult getUserInfo(){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			result.setResult(userBeanInfo.getSecurityUserDto());
			result.setSuccess(MessageInfo.GETSUCCESS.isResult());
			result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		} catch (Exception e) {
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	
	/**
	 * 返回分页对象
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
		    String dubboResultInfo=bbsTopicDtoServiceCustomer.getPage(getUserJson(), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg());
				result.setCode (dubboServiceResultInfo.getCode ());
			}
		} catch (Exception e) {
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	/**
	 * 返回分页对象
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/mine/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult minePage(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer.getMinePage(getUserJson(), paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg());
				result.setCode (dubboServiceResultInfo.getCode ());
			}
		} catch (Exception e) {
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	/**
	 * 返回符合条件的列表
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer.queryList(getUserJson(), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<BbsTopicDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,BbsTopicDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg());
		    }
			
		} catch (Exception e) {
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
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
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody BbsTopicDto t){
		MessageResult result=new MessageResult();
		try {
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=bbsTopicDtoServiceCustomer.save(getUserJson(), saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsTopicDto BbsTopicDto=JacksonUtils.fromJson(resultInfo, BbsTopicDto.class);
				result.setResult(BbsTopicDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(dubboServiceResultInfo.isSucess ());
				result.setMsg(dubboServiceResultInfo.getMsg ());
				result.setCode (dubboServiceResultInfo.getCode ());
		    }
		} catch (Exception e) {
			try {
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			} catch (JsonProcessingException e1) {
			}
			
		}
		return result;
	}
	
	/**
	  * @Description:帖子收藏
	  * @author:zhangfangzhi
	  * @date 2017年6月23日 上午8:55:13
	  * @version V1.0
	 */
	@RequestMapping(value="/addFavorite/{ids}",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult addFavorite(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer.addFavorite(getUserJson(), "{\"id\":\""+ids+"\"}");
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsTopicDto BbsTopicDto=JacksonUtils.fromJson(resultInfo, BbsTopicDto.class);
				result.setResult(BbsTopicDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg());
		    }
		} catch (Exception e) {
			try {
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(ids);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			} catch (JsonProcessingException e1) {
			}
			
		}
		return result;
	}
	
	/**
	 * 删除实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer.deleteObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsTopicDto BbsTopicDto=JacksonUtils.fromJson(resultInfo, BbsTopicDto.class);
				result.setResult(BbsTopicDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg());
			}
		} catch (Exception e) {
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		
		return result;
	}
	
	
	/**
	 * 删除实体对象
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer.deleteAllObjectByIds(getUserJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsTopicDto BbsTopicDto=JacksonUtils.fromJson(resultInfo, BbsTopicDto.class);
				result.setResult(BbsTopicDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg());
			}
		} catch (Exception e) {
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		
		return result;
	}
	
	/**
	 * 修改修改实体对象
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		BbsTopicDto BbsTopicDto=null;
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer.getObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=bbsTopicDtoServiceCustomer.update(getUserJson(), updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setSuccess(updateDubboServiceResultInfo.isSucess ());
					 result.setMsg(updateDubboServiceResultInfo.getMsg ());
					 result.setCode (updateDubboServiceResultInfo.getCode ());
				 }
			}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		} catch (Exception e) {
			try{
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(BbsTopicDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			}catch (JsonProcessingException e1) {
			}
			
		}
		return result;
	}

	/**
	 * 伪删除实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer.deletePseudoObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsTopicDto BbsTopicDto=JacksonUtils.fromJson(resultInfo, BbsTopicDto.class);
				result.setResult(BbsTopicDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg());
			}
		} catch (Exception e) {
		    log.error("调用deletePseudo方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		
		return result;
	}
	
	
	/**
	 * 伪删除实体对象
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer.deletePseudoAllObjectByIds(getUserJson(), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				BbsTopicDto bbsTopicDto=JacksonUtils.fromJson(resultInfo, BbsTopicDto.class);
				result.setResult(bbsTopicDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg());
			}
		} catch (Exception e) {
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		
		return result;
	}

	/**
	 *  计算帖子点击量
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/hit/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult clickNum(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
				String updateDubboResultInfo=bbsTopicDtoServiceCustomer.updateClickNum(getUserJson(),id);
				DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
			    result.setSuccess (updateDubboServiceResultInfo.isSucess ());
			    result.setMsg (updateDubboServiceResultInfo.getMsg ());
			    result.setResult (updateDubboServiceResultInfo.getResult ());
		} catch (Exception e) {
				log.error("调用click方法:  【参数"+id);
				result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}

	/**
	 * 返回主题明细分页对象
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detail/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult detailPage(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer.getDetailPage(getUserJson(), paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg());
			}
		} catch (Exception e) {
			log.error("调用page方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}

	/**
	 * 修改修改实体对象
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/updateStatus/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult updateStatus(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer .getObjectById(getUserJson(), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String updateJson= JacksonUtils.toJson(map);
				String updateDubboResultInfo=bbsTopicDtoServiceCustomer.updateStatus(getUserJson(), updateJson);
				DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
			    result.setCode (updateDubboServiceResultInfo.getCode ());
				result.setSuccess (updateDubboServiceResultInfo.isSucess ());
				result.setResult (updateDubboServiceResultInfo.getResult ());
				result.setMsg (updateDubboServiceResultInfo.getMsg ());
			}else{
				result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				result.setMsg("不存在更新的对象");
				result.setCode (ErrorInfoCode.NULL_ERROR.getValue ());
			}
		} catch (Exception e) {
				result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());

		}
		return result;
	}
	private String getUserJson() {
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();

		String userJson = JacksonUtils.toJson(userBeanInfo);
		return userJson;
	}


	/**
	 * 首页portal综合信息论坛模块列表
	 * @return
	 */
	@RequestMapping(value = "/getBbsForumForPortal",method = RequestMethod.GET)
	@ResponseBody
	public MessageResult getBbsForumForPortal() {
		MessageResult result=new MessageResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("portalPermission","1");
		map.put("sidx","sortNum");
		map.put("sord","asc");
		map.put("delflag",false);
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			String dubboResultInfo=bbsForumDtoServiceCustomer.queryList(getUserJson(), paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<BbsForumDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,BbsForumDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}

		} catch (Exception e) {

			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}

	/**
	 * 首页portal综合信息论坛模块列表
	 * @return
	 */
	@RequestMapping(value = "/getBbsForumItemForPortal/{forumId}",method = RequestMethod.GET)
	@ResponseBody
	public MessageResult getBbsForumItemForPortal(@PathVariable("forumId") String forumId) {
		MessageResult result=new MessageResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("forumId",forumId);
		map.put("status","PUBLISHED");
		map.put("screen",false);
		map.put("start",0);
		map.put("limit",10);
		map.put("delflag",false);
		map.put("sortFields","{\"stick\":\"desc\",\"sortNum\": \"asc\",\"createDate\":\"desc\"}");
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			String dubboResultInfo=bbsTopicDtoServiceCustomer.getPage(getUserJson(), paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg());
				result.setCode (dubboServiceResultInfo.getCode ());
			}
		} catch (Exception e) {
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}


}
