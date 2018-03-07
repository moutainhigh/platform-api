package com.xinleju.platform.flow.controller;

import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xinleju.platform.finance.utils.excel.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.erp.flow.service.api.extend.FlowService;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.flow.dto.ApprovalList;
import com.xinleju.platform.flow.dto.ApprovalListDto;
import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.dto.FlowApproveViewBean;
import com.xinleju.platform.flow.dto.FlowQueryBean;
import com.xinleju.platform.flow.dto.InstanceAcDto;
import com.xinleju.platform.flow.dto.InstanceDto;
import com.xinleju.platform.flow.dto.InstanceTransitionRecordDto;
import com.xinleju.platform.flow.dto.UserDto;
import com.xinleju.platform.flow.dto.service.InstanceDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 流程实例控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/flow/instance")
public class InstanceController {

	private static Logger log = Logger.getLogger(InstanceController.class);

	@Autowired
	private InstanceDtoServiceCustomer instanceDtoServiceCustomer;

	@Value("#{configuration['ImGroupLineUrl']?:''}")
	private String ImGroupLineUrl;
	@Value("#{configuration['ImSingleLineUrl']?:''}")
	private String ImSingleLineUrl;

	private static final String APPCODE_LLOA = "LLOA";
	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

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
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo=instanceDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceDto instanceDto=JacksonUtils.fromJson(resultInfo, InstanceDto.class);
				result.setResult(instanceDto);
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
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo=instanceDtoServiceCustomer.getPage(userInfo, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				PageBeanInfo<?> pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
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
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo=instanceDtoServiceCustomer.queryList(userInfo, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<InstanceDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceDto.class);
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
	 * 返回符合条件的个人查询列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/personalQueryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult personalQueryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			if(securityUserBeanInfo!=null && securityUserBeanInfo.getSecurityUserDto()!=null){
				String userId = securityUserBeanInfo.getSecurityUserDto().getId();
				map.put("userId", userId);
				String loginName = securityUserBeanInfo.getSecurityUserDto().getLoginName();
				map.put("loginName", loginName);
				paramaterJson = JacksonUtils.toJson(map);
			}else{
				System.out.println("\n personalQueryList >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}

			String dubboResultInfo=instanceDtoServiceCustomer.personalQueryList(userInfo, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				//List<InstanceDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceDto.class);
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
	public @ResponseBody MessageResult save(@RequestBody InstanceDto t){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=instanceDtoServiceCustomer.save(userInfo, saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceDto instanceDto=JacksonUtils.fromJson(resultInfo, InstanceDto.class);
				result.setResult(instanceDto);
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
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo=instanceDtoServiceCustomer.deleteObjectById(userInfo, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceDto instanceDto=JacksonUtils.fromJson(resultInfo, InstanceDto.class);
				result.setResult(instanceDto);
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
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo=instanceDtoServiceCustomer.deleteAllObjectByIds(userInfo, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceDto instanceDto=JacksonUtils.fromJson(resultInfo, InstanceDto.class);
				result.setResult(instanceDto);
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
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		InstanceDto instanceDto=null;
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo=instanceDtoServiceCustomer.getObjectById(userInfo, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,Object> oldMap = JacksonUtils.fromJson(resultInfo, HashMap.class);
				oldMap.putAll(map);
				String updateJson= JacksonUtils.toJson(oldMap);
				String updateDubboResultInfo=instanceDtoServiceCustomer.update(userInfo, updateJson);
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
				String  paramJson = mapper.writeValueAsString(instanceDto);
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
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo=instanceDtoServiceCustomer.deletePseudoObjectById(userInfo, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceDto instanceDto=JacksonUtils.fromJson(resultInfo, InstanceDto.class);
				result.setResult(instanceDto);
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
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo=instanceDtoServiceCustomer.deletePseudoAllObjectByIds(userInfo, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceDto instanceDto=JacksonUtils.fromJson(resultInfo, InstanceDto.class);
				result.setResult(instanceDto);
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
	 * 流程审批页面数据查询
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryApprovalList", method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryApprovalList(@RequestBody Map<String,String> params){
		MessageResult result = new MessageResult();
		String instanceId = params.get("instanceId");
		String requestSource = params.get("requestSource");
		if(StringUtils.isEmpty(instanceId) || StringUtils.isEmpty(requestSource)) {
			result.setSuccess(false);
			result.setMsg("参数为空！");
			return result;
		}

		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			if(securityUserBeanInfo == null) {
				result.setSuccess(false);
				result.setMsg("非法用户请求！");
				return result;
			}

			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.queryApprovalList(userInfo, instanceId, requestSource, "0");

			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), List.class, ApprovalList.class));
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用queryApprovalData方法:instanceId=" + instanceId, e);
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 获取节点审批类型
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryApprovalTypeId", method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryApprovalTypeId(@RequestBody Map<String,String> params){
		MessageResult result = new MessageResult();

		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			if(securityUserBeanInfo == null) {
				result.setSuccess(false);
				result.setMsg("非法用户请求！");
				return result;
			}

			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.queryApprovalTypeId(userInfo, JacksonUtils.toJson(params));

			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用queryApprovalTypeId方法:params=" +  JacksonUtils.toJson(params), e);
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 管理员调整环节页面数据查询
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryApprovalListAdmin", method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryApprovalListAdmin(@RequestBody Map<String,String> params){
		MessageResult result = new MessageResult();
		String instanceId = params.get("instanceId");
		String requestSource = params.get("requestSource");
		if(StringUtils.isEmpty(instanceId) || StringUtils.isEmpty(requestSource)) {
			result.setSuccess(false);
			result.setMsg("参数为空！");
			return result;
		}

		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			if(securityUserBeanInfo == null) {
				result.setSuccess(false);
				result.setMsg("非法用户请求！");
				return result;
			}

			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.queryApprovalList(userInfo, instanceId, requestSource, "1");

			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), List.class, ApprovalList.class));
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用queryApprovalData方法:instanceId=" + instanceId, e);
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 业务系统查询流程审批记录，flow_view页面用
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryApprovalListExternal", method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryApprovalListExternal(@RequestBody Map<String,String> params){
		MessageResult result = new MessageResult();

		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		if(securityUserBeanInfo == null) {
			result.setSuccess(false);
			result.setMsg("非法用户请求！");
			return result;
		}

		String flCode = params.get("flCode");
		String businessId = params.get("businessId");
		String appId = params.get("appId");
		String userId = params.get("userId");
		if(StringUtils.isEmpty(businessId)) {
			result.setSuccess(false);
			result.setMsg("businessId参数为空！");
			return result;
		}

		if(StringUtils.isEmpty(userId)) {
			userId = securityUserBeanInfo.getSecurityUserDto().getId();
		}

		try {
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.queryApprovalListExternal(userInfo, flCode, businessId, appId, userId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), ApprovalListDto.class));
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用queryApprovalListExternal方法错误", e);
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 查询流程流转记录
	 * 现在查询的是审批记录中的完成条目） TODO zhangdaoqiang
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryTransferList", method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryTransferList(@RequestBody Map<String,String> params){
		MessageResult result = new MessageResult();
		String instanceId = params.get("instanceId");
		String requestSource = params.get("requestSource");
		if(StringUtils.isEmpty(instanceId)) {
			result.setSuccess(false);
			result.setMsg("参数为空！");
			return result;
		}

		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			if(securityUserBeanInfo == null) {
				result.setSuccess(false);
				result.setMsg("非法用户请求！");
				return result;
			}
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo = instanceDtoServiceCustomer.queryTransferList(userInfo, instanceId, requestSource);

			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), List.class, InstanceTransitionRecordDto.class));
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用queryApprovalData方法:instanceId=" + instanceId, e);
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 查询下一步
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryNext", method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryNext(@RequestBody Map<String,String> params){
		MessageResult result = new MessageResult();
		String instanceId = params.get("instanceId");
		String taskId = params.get("taskId");
		if(StringUtils.isEmpty(instanceId) || StringUtils.isEmpty(taskId)) {
			result.setSuccess(false);
			result.setMsg("参数为空！");
			return result;
		}

		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			if(securityUserBeanInfo == null) {
				result.setSuccess(false);
				result.setMsg("非法用户请求！");
				return result;
			}
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo = instanceDtoServiceCustomer.queryNext(userInfo, instanceId, taskId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), List.class, String.class));
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用queryApprovalData方法:instanceId=" + instanceId, e);
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 查询已审批人
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/queryApproverDone", method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryApproverBeReturn(@RequestBody Map<String,String> params){
		MessageResult result = new MessageResult();
		String instanceId = params.get("instanceId");
		String taskId = params.get("taskId");
		if(StringUtils.isEmpty(instanceId)) {
			result.setSuccess(false);
			result.setMsg("参数为空！");
			return result;
		}

		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			if(securityUserBeanInfo == null) {
				result.setSuccess(false);
				result.setMsg("非法用户请求！");
				return result;
			}
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo = instanceDtoServiceCustomer.queryApproverBeReturn(userInfo, instanceId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), List.class, Map.class));
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用queryApprovalData方法:instanceId=" + instanceId, e);
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【" + e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 流程审批处理
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/approval", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult approval(@RequestBody ApprovalSubmitDto approvalDto, HttpServletRequest request){
		MessageResult result=new MessageResult();
		String approvalParams= JacksonUtils.toJson(approvalDto);
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();

			//保存baseUrl add by zhangdaoqiag
			if(StringUtils.isEmpty(securityUserBeanInfo.getBaseUrl())) {
				String baseUrl = request.getScheme() + "://" + request.getServerName() + ":"
						+ request.getServerPort() + request.getContextPath();
				securityUserBeanInfo.setBaseUrl(baseUrl);
			}

			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.approval(userInfo, approvalParams);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用queryList方法:  【参数" + approvalParams+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 发起人撤回流程
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/withDrawFlow/{instanceId}", method = RequestMethod.GET, consumes="application/json")
	public @ResponseBody MessageResult withDrawFlow(@PathVariable("instanceId")  String instanceId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.withDrawFlow(userInfo, instanceId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用withDrawFlow方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 审批人撤回任务
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/withDrawTask", method = RequestMethod.GET, consumes="application/json")
	public @ResponseBody MessageResult withDrawTask(String instanceId, String taskId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.withDrawTask(userInfo, instanceId, taskId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用isApproved方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 审批人撤回任务
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getRelateInstances", method = RequestMethod.GET, consumes="application/json")
	public @ResponseBody MessageResult getRelateInstances(String instanceId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.getRelateInstances(userInfo, instanceId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			List<InstanceDto> instances = JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), List.class, InstanceDto.class);
			result.setResult(instances);
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用getRelateInstances方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 管理员功能：审结流程
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/finishApproval/{instanceId}", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult finishApproval(@PathVariable("instanceId")  String instanceId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.finishApproval(userInfo, instanceId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用finishApproval方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 管理员功能：跳过当前审批人
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/skipCurrentApprover/{instanceId}", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult skipCurrentApprover(@PathVariable("instanceId")  String instanceId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.skipCurrentApprover(userInfo, instanceId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用skipCurrentApprover方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 管理员功能：挂起的流程放行
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/letItGo/{instanceId}", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult letItGo(@PathVariable("instanceId")  String instanceId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.flowRestart(userInfo, instanceId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用letItGo方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 管理员功能：流程作废
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/cancelInstance/{instanceId}", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult cancelInstance(@PathVariable("instanceId")  String instanceId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.cancelInstance(userInfo, instanceId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用cancelInstance方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 管理员功能：传阅
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/passAndRead/{instanceId}", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult passAndRead(@PathVariable("instanceId")  String instanceId,
												   @RequestBody List<UserDto> users, HttpServletRequest request){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(users);

			//保存baseUrl add by zhangdaoqiag
			if(StringUtils.isEmpty(securityUserBeanInfo.getBaseUrl())) {
				String baseUrl = request.getScheme() + "://" + request.getServerName() + ":"
						+ request.getServerPort() + request.getContextPath();
				securityUserBeanInfo.setBaseUrl(baseUrl);
			}
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo = instanceDtoServiceCustomer.passAndRead(userInfo, instanceId, userJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用passAndRead方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}
	
	/**
	  * @Description:取消传阅
	  * @author:zhangfangzhi
	  * @date 2018年2月3日 上午10:16:33
	  * @version V1.0
	 */
	@RequestMapping(value="/cancelPassAndRead/{id}", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult cancelPassAndRead(@PathVariable("id")  String id,HttpServletRequest request){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			if(StringUtils.isEmpty(securityUserBeanInfo.getBaseUrl())) {
				String baseUrl = request.getScheme() + "://" + request.getServerName() + ":"
						+ request.getServerPort() + request.getContextPath();
				securityUserBeanInfo.setBaseUrl(baseUrl);
			}
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo = instanceDtoServiceCustomer.cancelPassAndRead(userInfo,id);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用cancelPassAndRead方法:  【参数" + id+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 发起人功能：催办
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/remind/{instanceId}", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult remind(@PathVariable("instanceId")  String instanceId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.remind(userInfo, instanceId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用remind方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 流程收藏
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/collection/{instanceId}", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult collection(@PathVariable("instanceId")  String instanceId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.collection(userInfo, instanceId);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用collection方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 管理员功能：修改可阅人
	 *
	 * @param type:add/delete/reset
	 * @return
	 */
	@RequestMapping(value="/updateReader/{type}/{instanceId}", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult updateReader(@PathVariable("type")  String type,
													@PathVariable("instanceId")  String instanceId, @RequestBody List<UserDto> users){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String userJson = JacksonUtils.toJson(users);
			String dubboResultInfo = instanceDtoServiceCustomer.updateReader(userInfo, type, instanceId, userJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用updateReader方法:  【参数" + instanceId+"】======" + "【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 管理员功能：调整环节
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/adjustAc", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult adjustAc(@RequestBody List<ApprovalList> approvalList){
		MessageResult result = new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.adjustAc(userInfo, JacksonUtils.toJson(approvalList));
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用adjustAc方法:【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}
	/**
	 * 管理员功能：调整环节 -> 构造环节
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/createAc", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult createAc(@RequestBody InstanceAcDto instanceAcDto){
		MessageResult result = new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.createAc(userInfo, JacksonUtils.toJson(instanceAcDto));
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);

			if(dubboServiceResultInfo.isSucess()){
				result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setResult(dubboServiceResultInfo.getResult());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			log.error("调用createAc方法:【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 管理员功能：修改审批人
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/updateApprover", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult updateApprover(@RequestBody List<ApprovalList> approvalList){
		MessageResult result = new MessageResult();

		if(CollectionUtils.isEmpty(approvalList)) {
			result.setSuccess(false);
			result.setMsg("请求参数为空！");
			return result;
		}

		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String dubboResultInfo = instanceDtoServiceCustomer.updateApprover(userInfo, JacksonUtils.toJson(approvalList));
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用updateApprover方法:【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 管理员功能：修改审批意见
	 *
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/updateApprovalComments", method = RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult updateApprovalComments(@RequestBody List<ApprovalList> approvalList){
		MessageResult result = new MessageResult();
		System.out.println("\n\n updateApprovalComments approvalList.size="+approvalList.size());
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String paramDataJson = JacksonUtils.toJson(approvalList);
			System.out.println("updateApprovalComments paramDataJson="+paramDataJson);

			String dubboResultInfo = instanceDtoServiceCustomer.updateApprovalComments(userInfo, paramDataJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			result.setSuccess(dubboServiceResultInfo.isSucess());
			result.setResult(dubboServiceResultInfo.getResult());
			result.setMsg(dubboServiceResultInfo.getMsg());

		} catch (Exception e) {
			log.error("调用updateApprovalComments方法:【" + e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg() + "【"+e.getMessage() + "】");
		}
		return result;
	}

	/**
	 * 根据提交的实例化参数, 实现模板和环节参与人的相关数据的拷贝和保存
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/saveAllInstanceData",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult saveAllInstanceData(@RequestBody InstanceDto t){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			String saveJson= JacksonUtils.toJson(t);
			log.info("===1、流程发起，保存实例数据");
			log.info("===1.1、保存数据开始：实例数据=" + saveJson + ", 当前用户=" + userInfo);
			HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String hostHeader = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			System.out.println("----------------hostHeader hostHeader="+hostHeader);
			String dubboResultInfo=instanceDtoServiceCustomer.saveAllInstanceData(userInfo, saveJson, hostHeader);
			System.out.println("----------------saveAllInstanceData   dubboResultInfo="+dubboResultInfo);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,Object> res=JacksonUtils.fromJson(resultInfo, HashMap.class);
				result.setResult(res);
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
	 * 根据审批人查询对应的信息信息
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryListByApprover",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryListByApprover(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo=instanceDtoServiceCustomer.queryListByApprover(userInfo, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				//List<InstanceDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceDto.class);
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
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 根据审批人查询对应的信息信息
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryInstanceBy",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryInstanceBy(@RequestBody Map<String, Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
			if(securityUserBeanInfo!=null && securityUserBeanInfo.getSecurityUserDto()!=null){
				String userId = securityUserBeanInfo.getSecurityUserDto().getId();
				map.put("userId", userId);
				paramaterJson = JacksonUtils.toJson(map);
			}else{
				System.out.println("\n queryInstanceBy >>> userBeanInfo==null 或 userBeanInfo.getSecurityUserDto()==null");
			}

			String dubboResultInfo=instanceDtoServiceCustomer.queryInstanceBy(userInfo, paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				//List<InstanceDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,InstanceDto.class);
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
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	@RequestMapping(value="/flowView",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult flowView(@RequestBody Map<String, String> params,HttpServletRequest req){
		MessageResult result = new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		if(securityUserBeanInfo == null) {
			result.setSuccess(false);
			result.setCode("relogin");
			result.setMsg("请重新登陆！");
			return result;
		}

		//验证流程查看权限
		String instanceId = params.get("instanceId");
		String sourceInstanceId = params.get("sourceInstanceId");
		String attachmentUrlId = params.get("attachmentUrl_id");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("instanceId",instanceId);
		paramMap.put("sourceInstanceId",sourceInstanceId);
		paramMap.put("attachmentUrl_id",attachmentUrlId);
		String dubboResultInfo1=instanceDtoServiceCustomer.validateInstanceDataAuth(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(paramMap));
		DubboServiceResultInfo dubboServiceResultInfo1= JacksonUtils.fromJson(dubboResultInfo1, DubboServiceResultInfo.class);
		if(dubboServiceResultInfo1.isSucess()){
			String resultInfo= dubboServiceResultInfo1.getResult();
			Boolean instanceAccess = JacksonUtils.fromJson(resultInfo, Boolean.class);
			if(!instanceAccess){
				result.setSuccess(false);
				result.setCode("NO_AUTH");
				result.setMsg("没有流程查看权限！");
				return result;
			}
		}



		String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
		String dubboResultInfo = instanceDtoServiceCustomer.flowView(userInfo, JacksonUtils.toJson(params));
		DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		result.setSuccess(dubboServiceResultInfo.isSucess());
		Map<String,Object> resultMap = JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Map.class);
		resultMap.put("currentSessionId",req.getSession().getId());
		resultMap.put("loginUserTendId",securityUserBeanInfo.getTendId());
		result.setResult(resultMap);
		result.setMsg(dubboServiceResultInfo.getMsg());
		result.setCode(dubboServiceResultInfo.getCode());
		return result;
	}

	@RequestMapping(value="/validateInstanceDataAuth/{instanceId}",method=RequestMethod.GET)
	public @ResponseBody MessageResult validateInstanceDataAuth(@PathVariable("instanceId")  String instanceId, @RequestParam(value = "sourceInstanceId",required = false) String sourceInstanceId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("instanceId",instanceId);
			paramMap.put("sourceInstanceId",sourceInstanceId);
			String dubboResultInfo=instanceDtoServiceCustomer.validateInstanceDataAuth(userInfo, JacksonUtils.toJson(paramMap));
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Boolean instanceAccess = JacksonUtils.fromJson(resultInfo, Boolean.class);
				result.setResult(instanceAccess);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用validateInstanceDataAuth方法:  【参数"+instanceId+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 获取流程实例流转图数据
	 *
	 * @param instanceId  流程实例ID
	 *
	 * @return
	 */
	@RequestMapping(value="/getInstanceGraph/{instanceId}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getInstanceGraph(@PathVariable("instanceId")  String instanceId){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String dubboResultInfo=instanceDtoServiceCustomer.getInstanceGraph(userInfo, "{\"instanceId\":\""+instanceId+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				InstanceDto instanceDto=JacksonUtils.fromJson(resultInfo, InstanceDto.class);
				result.setResult(instanceDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用getInstanceGraph方法:  【参数"+instanceId+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	@RequestMapping(value="/imOnLine",method=RequestMethod.POST)
	public @ResponseBody MessageResult imOnLine(@RequestBody Map<String, Object> params){
		MessageResult result=new MessageResult();
		if(params.get("type")==null || StringUtil.isBlank(params.get("type").toString())){
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("参数错误");
			return result;
		}
		try {
			/*SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);*/
			String type = params.get("type").toString();
			String sendUrl = type.equals("group")?ImGroupLineUrl:ImSingleLineUrl;
			log.info("im聊天消息---sendUrl="+sendUrl);
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(sendUrl);
			httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			String json = JacksonUtils.toJson(params);
			json = URLEncoder.encode(json, "utf-8");
			log.info("im聊天消息---json="+json);
			String imresult = null;
			try {
				StringEntity se = new StringEntity(json);
				se.setContentType(CONTENT_TYPE_TEXT_JSON);
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
				httpPost.setEntity(se);
				HttpResponse res = httpClient.execute(httpPost);
				if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					imresult = EntityUtils.toString(res.getEntity()); // 返回json格式字符串
					log.debug("imresult>>> imresult="+imresult);
					Map<String,Object> imRes = JacksonUtils.fromJson(imresult,HashMap.class);
					if(imRes.get("stateCode")!=null && Integer.valueOf(imRes.get("stateCode").toString())==0){
						result.setSuccess(true);
						result.setCode(imRes.get("stateCode").toString());
					}else{
						result.setSuccess(false);
					}
					if(imRes.get("msg")!=null ){
						result.setMsg(imRes.get("msg").toString());
					}
				}else{
					imresult = "发起im聊天失败";
					result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg(imresult);
				}
				log.info("im聊天消息---result>>> StatusCode="+res.getStatusLine().getStatusCode());
			} catch(Exception e) {
				imresult = "发起im聊天失败";
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
				result.setMsg(imresult);
			}

		} catch (Exception e) {
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	@RequestMapping(value="/saveModifyInstance",method= RequestMethod.POST, consumes="application/json")
	public @ResponseBody
	MessageResult saveModifyInstance(@RequestBody Map<String,Object> modifyInstanceDataMap){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String saveJson= JacksonUtils.toJson(modifyInstanceDataMap);
			String dubboResultInfo = instanceDtoServiceCustomer.saveModifyInstance(userInfo, saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,Object> instanceMap = JacksonUtils.fromJson(resultInfo, Map.class);
				result.setResult(instanceMap);
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
				String  paramJson = mapper.writeValueAsString(modifyInstanceDataMap);
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
	@RequestMapping(value="/getUserPostNew",method= RequestMethod.POST, consumes="application/json")
	public @ResponseBody
	MessageResult getUserPostNew(@RequestBody Map<String,Object> param){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

			String saveJson= JacksonUtils.toJson(param);
			String dubboResultInfo = instanceDtoServiceCustomer.getUserPostNew(userInfo, saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,Object> instanceMap = JacksonUtils.fromJson(resultInfo, Map.class);
				result.setResult(instanceMap);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			ObjectMapper mapper = new ObjectMapper();
			log.error("调用save方法:  【参数"+param+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.SAVEERROR.isResult());
			result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");

		}
		return result;
	}
}
