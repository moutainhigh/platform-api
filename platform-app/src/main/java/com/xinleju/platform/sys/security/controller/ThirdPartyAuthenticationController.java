package com.xinleju.platform.sys.security.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.flow.dto.SysNoticeMsgUserConfigDto;
import com.xinleju.platform.flow.dto.service.SysNoticeMsgUserConfigDtoServiceCustomer;
import com.xinleju.platform.uitls.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xinleju.platform.flow.dto.SysNoticeMsgDto;
import com.xinleju.platform.flow.dto.service.SysNoticeMsgDtoServiceCustomer;
import com.xinleju.platform.out.app.org.service.UserOutServiceCustomer;
import com.xinleju.platform.party.dto.IntegrateAppDto;
import com.xinleju.platform.party.dto.UserConfigDto;
import com.xinleju.platform.party.dto.service.IntegrateAppDtoServiceCustomer;
import com.xinleju.platform.party.dto.service.UserConfigDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.sys.security.dto.service.AuthenticationDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * 第三方认证登录
 * 
 * @author hao
 *
 */
@Controller
@RequestMapping("/sys/thirdPartyAuthentication")
public class ThirdPartyAuthenticationController {
	private  Logger log = Logger.getLogger(ThirdPartyAuthenticationController.class);

	@Autowired
	private AuthenticationDtoServiceCustomer authenticationDtoServiceCustomer;

	@Autowired
	private UserConfigDtoServiceCustomer userConfigDtoServiceCustomer;
	
	@Autowired
	private IntegrateAppDtoServiceCustomer integrateAppDtoServiceCustomer;
	
	@Autowired
	private UserDtoServiceCustomer userDtoServiceCustomer;
	
	@Autowired
	protected RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private SysNoticeMsgDtoServiceCustomer sysNoticeMsgDtoServiceCustomer;

	@Autowired
	private UserOutServiceCustomer userOutServiceCustomer;


	/**
	 * 同步IM人员密码接口
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/syncUser",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody Map<String, Object> syncUser(@RequestBody Map<String,Object> map,HttpServletRequest request){

//		Map map= JacksonUtils.fromJson(pJson, Map.class);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//白名单拦截ip , add by gyh 20180130
		boolean checkIp = WhiteIpUtils.checkHttpMethod();
		if(!checkIp){
			returnMap.put("success",false);
			returnMap.put("msg", String.format(ErrorInfoCode.WRONG_WHITE_IP.getName(),WhiteIpUtils.getIpAddress(request)));
			return returnMap;
		}
	/*	String userId=(String) map.get("userId");
		String password=(String) map.get("password");
		String domain = (String) map.get("domain");*/
//		String userId=(String) map.get("userId");
//		String password=(String) map.get("password");
//		String domain = (String) map.get("domain");
//		String mobile = (String) map.get("mobile");
//		String isMale = (String) map.get("isMale");
//		String email = (String) map.get("email");
//		String plaintextPassword = (String) map.get("plaintextPassword");
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo=new SecurityUserBeanInfo();
		SecurityUserDto securityUserDto = new SecurityUserDto();
		securityUserDto.setId("imid");
		securityUserDto.setRealName("IM系统更改人员密码");
		securityUserBeanInfo.setSecurityUserDto(securityUserDto);
		try {
			Date t0 = new Date();
			System.out.println ("切换库开始："+t0.getTime());
			String dubboResultInfo=authenticationDtoServiceCustomer.getDomain(null, paramaterJson);
			Date t1 = new Date();
			System.out.println("切换库结束："+t1.getTime()+"用时：：：：：：：：："+(t1.getTime()-t0.getTime()));
			 DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
		    	String Info= dubboServiceResultInfo.getResult();
		    	Map demain=JacksonUtils.fromJson(Info, HashMap.class);
		    	if(demain!=null){
			    	securityUserBeanInfo.setTendId(demain.get("tendId").toString());
			    	securityUserBeanInfo.setTendCode(demain.get("tendCode").toString());
		    	}
		    }else{
		    	returnMap.put("success",dubboServiceResultInfo.isSucess());
		    	returnMap.put("msg",dubboServiceResultInfo.getMsg());
				return returnMap;
		    }
		} catch (Exception e1) {
			returnMap.put("success",false);
	    	returnMap.put("msg",e1.getMessage());
			return returnMap;
		}
		
		try {
			String dubboResultInfo=userDtoServiceCustomer.updateIMuser(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				returnMap.put("success",true);
			}else{
				returnMap.put("success",false);
		    	returnMap.put("msg",dubboServiceResultInfo.getExceptionMsg());
			}
		} catch (Exception e) {
			returnMap.put("success",false);
	    	returnMap.put("msg",e.getMessage());
		}
		return  returnMap;
	}
	
	
	
	/**
	 * 第三方认证，获取访问token 改成accessToken2
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/accessToken",method={RequestMethod.GET}, consumes="application/json")
	public @ResponseBody Map<String, Object> accessToken(HttpServletRequest request){

		long t1 = System.currentTimeMillis ();
		log.info (new Date()+"进入获取token方法开始计算耗时(accessToken接口方法)"+t1);
		Map<String, Object> result = new HashMap<>();
		/*
		 *  根据用户名认证并获取对应的权限数据放到session中 
		 */

		// 构建用户安全信息
		SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
		SecurityUserBeanRelationInfo securityUserBeanRelationInfo = new SecurityUserBeanRelationInfo();
		securityUserBeanInfo.setSecurityUserDto(new SecurityUserDto());
		// 获取第三方请求用户名
		String userInfo = request.getParameter("userInfo");
		String loginName = null,
		domainName = null;
		String paramaterJson = null;
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotEmpty(userInfo) && userInfo.indexOf('@') > 0) {
			loginName = userInfo.substring(0, userInfo.indexOf('@'));
			domainName = userInfo.substring( userInfo.indexOf('@') + 1);
			map.put("domain", domainName);
			map.put("loginName", loginName);
			paramaterJson = JacksonUtils.toJson(map);
			try {
				// 根据域名查询租户信息
				long au1 = System.currentTimeMillis ();
				String dubboResultInfo = authenticationDtoServiceCustomer.getDomain(null, paramaterJson);
				long au2 = System.currentTimeMillis ();
				log.info ("根据域名查询租户信息authenticationDtoServiceCustomer.getDomain(accessToken接口方法)方法耗时"+(au2-au1));
				DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				if (dubboServiceResultInfo.isSucess()) {
					String Info = dubboServiceResultInfo.getResult();
					@SuppressWarnings("rawtypes")
					Map demain = JacksonUtils.fromJson(Info, HashMap.class);
					if (demain != null) {
						securityUserBeanInfo.setTendId(demain.get("tendId").toString());
						securityUserBeanInfo.setTendCode(demain.get("tendCode").toString());
					}
				}
			} catch (Exception e) {
				// TODO 租户信息查询失败，返回告知
				log.info ("查询用户信息发生错误");
				result.put("success", false);
				result.put("message", "查询用户信息发生错误");
				return result;
			}
			
		} else {
			loginName = userInfo;
			String tendId = request.getParameter("tendId");
			String tendCode = request.getParameter("tendCode");
			if (StringUtils.isNotBlank(tendId) && StringUtils.isNotBlank(tendCode)) {
				securityUserBeanInfo.setTendId(tendId);
				securityUserBeanInfo.setTendCode(tendCode);
				map.put("loginName", userInfo);
				paramaterJson = JacksonUtils.toJson(map);
			} else {
				// TODO 用户参数信息不合法
				log.info ("请求参数信息不合法，用户信息应为：loginName@tend"+" === "+userInfo);
				result.put("success", false);
				result.put("message", "请求参数信息不合法，用户信息应为：loginName@tend");
				return result;
			}
		}
		try {
			String dubboResultInfo = null;
			DubboServiceResultInfo dubboServiceResultInfo = null;
			
			// 根据请求参数appId和appSecret匹配第三方应用信息
			String appId = request.getParameter("appId");
			String appSecret = request.getParameter("appSecret");
			String userInfoJson = JacksonUtils.toJson(securityUserBeanInfo);
			if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(appSecret)) {
				Map integrateAppDto = new HashMap ();
				integrateAppDto.put("appSecret",appSecret);
				integrateAppDto.put("code",appId);
				long queryListT0=System.currentTimeMillis ();
				dubboResultInfo = integrateAppDtoServiceCustomer.queryList(userInfoJson, JacksonUtils.toJson(integrateAppDto));
				long queryListT1=System.currentTimeMillis ();
				log.info (new Date()+"integrateAppDtoServiceCustomer.queryList(accessToken接口方法)耗时"+(queryListT1-queryListT0));
				dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				if (dubboServiceResultInfo.isSucess()) {
					String resultInfo = dubboServiceResultInfo.getResult();
					List<IntegrateAppDto> appList = JacksonUtils.fromJson(resultInfo, ArrayList.class, IntegrateAppDto.class);
					if (appList==null||appList.isEmpty ()) {
						// TODO 没有正确匹配出应用信息
						log.info ("查询应用信息发生错误");
						result.put("success", false);
						result.put("message", "查询应用信息发生错误");
						return result;
					}
				} else {
					// TODO 应用信息查询失败
					log.info ("查询应用信息发生错误");
					result.put("success", false);
					result.put("message", "查询应用信息发生错误");
					return result;
				}
			} else {
				// TODO 应用参数信息不合法
				log.info ("应用参数信息不合法");
				result.put("success", false);
				result.put("message", "应用参数信息不合法");
				return result;
			}
			// 根据用户信息从DB中查询token
			Map userDto = new HashMap ();
			userDto.put ("loginName",loginName);
			userDto.put ("delflag",false);
			long userQueryListT0 = System.currentTimeMillis ();
			dubboResultInfo = userDtoServiceCustomer.queryList(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(userDto));
			long userQueryListT1 = System.currentTimeMillis ();
			log.info (new Date()+"userDtoServiceCustomer.queryList(accessToken接口方法)耗时"+(userQueryListT1-userQueryListT0)+"毫秒");
			dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if (dubboServiceResultInfo.isSucess()) {
				String resultInfo = dubboServiceResultInfo.getResult();
				List<UserDto> userList = JacksonUtils.fromJson(resultInfo, ArrayList.class, UserDto.class);
				if (null != userList && userList.size() == 1) {
					UserDto user = userList.get(0);
					// 查询第三方用户配置信息
					Map userConfigDto = new HashMap ();
					userConfigDto.put ("userId",user.getId());
					userConfigDto.put("delflag",false);
					long userConfigDtoT0 = System.currentTimeMillis ();
					dubboResultInfo = userConfigDtoServiceCustomer.queryList(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(userConfigDto));
					long userConfigDtoT1 = System.currentTimeMillis ();
					log.info (new Date()+"userConfigDtoServiceCustomer.queryList(accessToken接口方法)耗时"+(userConfigDtoT1-userConfigDtoT0)+"毫秒");
					dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
					if (dubboServiceResultInfo.isSucess()) {
						resultInfo = dubboServiceResultInfo.getResult();
						List<UserConfigDto> userConfigList = JacksonUtils.fromJson(resultInfo, ArrayList.class, UserConfigDto.class);
						if (null != userConfigList && userConfigList.size() == 1) {
							UserConfigDto uc = userConfigList.get(0);
							//判断token失效时间是否小于当前时间
							//如果token失效时间小于当前时间则更新token开始时间及失效时间
							if(System.currentTimeMillis()>uc.getEndTime().getTime()){
								uc.setStartTime(new Date());
								Calendar cal = Calendar.getInstance();
								cal.add(Calendar.YEAR,1);
								uc.setEndTime(cal.getTime());
								userConfigDtoServiceCustomer.update(JacksonUtils.toJson(securityUserBeanInfo),JacksonUtils.toJson(uc));
							}
							// 匹配出token
							result.put("token", uc.getToken());
							result.put("token_end_date", uc.getEndTime());

							long u3 = System.currentTimeMillis ();
						} else {
							// TODO 没有正确查询匹配出第三方用户配置信息
//							result.put("success", false);
//							result.put("message", "没有相应的用户信息");
//							return result;
							// 分配token
							Calendar cal = Calendar.getInstance();
							UserConfigDto ucd = new UserConfigDto();
							ucd.setAppId(appId);
							ucd.setDelflag(false);
							ucd.setStartTime(cal.getTime());
							ucd.setId(IDGenerator.getUUID());
							cal.add(Calendar.YEAR, 1);
							ucd.setEndTime(cal.getTime());
							ucd.setStatus("1");
							ucd.setTendId(securityUserBeanInfo.getTendId());
							ucd.setToken(IDGenerator.getUUID());
							ucd.setUserId(user.getId());
							ucd.setCreatePersonName("程序产生");
							userConfigDtoServiceCustomer.save(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(ucd));
							long tt = System.currentTimeMillis () ;
							result.put("token", ucd.getToken());
							result.put("token_end_date", ucd.getEndTime());
						}
					} else {
						// TODO 第三方用户配置信息查询失败
						log.info("查询用户配置信息失败");
						result.put("success", false);
						result.put("message", "查询用户配置信息失败");
						return result;
					}
				} else {
					// TODO 没有正确查询出匹配的用户信息
					log.info("查询用户信息失败");
					result.put("success", false);
					result.put("message", "查询用户信息失败");
					return result;
				}
			} else {
				// TODO 用户信息查询失败
				log.info ("查询用户配置信息失败");
				result.put("success", false);
				result.put("message", "查询用户配置信息失败");
				return result;
			}
			long preCheckT0 = System.currentTimeMillis ();
			dubboResultInfo = authenticationDtoServiceCustomer.preCheck(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
			long preCheckT1 = System.currentTimeMillis ();
			log.info (new Date()+"authenticationDtoServiceCustomer.preCheck(accessToken接口方法)耗时："+(preCheckT1-preCheckT0)+"毫秒"+" 参数"+paramaterJson);
		    dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
		    	// 获取用户角色
				String resultInfo = dubboServiceResultInfo.getResult();
				SecurityUserDto securityUserDto = JacksonUtils.fromJson(resultInfo, SecurityUserDto.class);
				
				// 将cookie信息写入用户安全信息中
				StringBuilder sb = new StringBuilder();
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
					}
					securityUserBeanInfo.setCookies(sb.toString());
				}
				
				securityUserBeanInfo.setSecurityUserDto(securityUserDto);
				
				// 获取用户相关授权信息
				long t2 = System.currentTimeMillis ();
				String authenticationInfodubboResultInfo = authenticationDtoServiceCustomer.getUserAuthenticationInfo(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(securityUserDto));
				long t3 = System.currentTimeMillis ();
				log.info (new Date()+"执行用户数据权限和菜单查询authenticationDtoServiceCustomer.getUserAuthenticationInfo(accessToken接口方法)耗时："+(t3-t2)+"毫秒");
				DubboServiceResultInfo authenticationInfodubboServiceResultInfo = JacksonUtils.fromJson(authenticationInfodubboResultInfo, DubboServiceResultInfo.class);
				if (authenticationInfodubboServiceResultInfo.isSucess()) {
					String authenticationInforesultInfo = authenticationInfodubboServiceResultInfo.getResult();
					AuthenticationDto authenticationDto = JacksonUtils.fromJson(authenticationInforesultInfo, AuthenticationDto.class);
					//获取用户标准岗位
					List<SecurityStandardRoleDto> securityStandardRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getStandardRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
					securityUserBeanInfo.setSecurityStandardRoleDtoList(securityStandardRoleDtoList);
					//当前用户的菜单清单（未授权和已授权的）
					List<SecurityResourceDto> SecurityResourceDtoList = JacksonUtils.fromJson (JacksonUtils.toJson (authenticationDto.getResourceDtoList ()), ArrayList.class, SecurityResourceDto.class);
					securityUserBeanRelationInfo.setResourceDtoList (SecurityResourceDtoList);
					//获取用户通用角色
					List<SecurityStandardRoleDto> securityCurrencyRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getCurrencyRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
					securityUserBeanInfo.setSecurityCurrencyRoleDtoList(securityCurrencyRoleDtoList);
					//获取用户岗位
					List<SecurityPostDto> securityPostDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getPostDtoList()),ArrayList.class,SecurityPostDto.class);
					securityUserBeanInfo.setSecurityPostDtoList(securityPostDtoList);
					//当前用户所在组织的类型
					String securityOrganizationType = authenticationDto.getOrganizationType();
					securityUserBeanInfo.setSecurityOrganizationType(securityOrganizationType);
					//当前用户的一级公司
					SecurityOrganizationDto securityTopCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopCompanyDto()),SecurityOrganizationDto.class);
					securityUserBeanInfo.setSecurityTopCompanyDto(securityTopCompanyDto);
					//当前用户的直属公司
					SecurityOrganizationDto securityDirectCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectCompanyDto()),SecurityOrganizationDto.class);
					securityUserBeanInfo.setSecurityDirectCompanyDto(securityDirectCompanyDto);
					//当前用户的一级部门
					SecurityOrganizationDto securityTopDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopDeptDto()),SecurityOrganizationDto.class);
					securityUserBeanInfo.setSecurityTopDeptDto(securityTopDeptDto);
					//当前用户的直属部门
					SecurityOrganizationDto securityDirectDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectDeptDto()),SecurityOrganizationDto.class);
					securityUserBeanInfo.setSecurityDirectDeptDto(securityDirectDeptDto);
					//当前用户的项目
					SecurityOrganizationDto securityGroupDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getGroupDto()),SecurityOrganizationDto.class);
					securityUserBeanInfo.setSecurityGroupDto(securityGroupDto);
					//当前用户的分期
					SecurityOrganizationDto securityBranchDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getBranchDto()),SecurityOrganizationDto.class);
					securityUserBeanInfo.setSecurityBranchDto(securityBranchDto);
				}
				
				long endTime = ((Date)result.get("token_end_date")).getTime();
                long redisSetT0 = System.currentTimeMillis ();
				redisTemplate.opsForValue().set(SecurityUserBeanInfo.TOKEN_TEND_USER + result.get("token"), JacksonUtils.toJson(securityUserBeanInfo), endTime, TimeUnit.MILLISECONDS);
				long redisSetT1 = System.currentTimeMillis ();
				log.info (new Date()+"redis数据set(accessToken接口方法)耗时"+(redisSetT1-redisSetT0)+"毫秒");
				redisTemplate.opsForValue().set(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + result.get("token"), JacksonUtils.toJson(securityUserBeanRelationInfo), endTime, TimeUnit.MILLISECONDS);
				HttpSession session = request.getSession();
				session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
			    session.setAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU,securityUserBeanRelationInfo);
				long finalTime = System.currentTimeMillis ();
				log.info(new Date()+"所有方法耗时："+ (finalTime-t1)+"毫秒");
			    result.put("DTL_SESSION_ID", session.getId());
	    	}
	    } catch (Exception e) {
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.put("success", false);
			result.put("message", "获取访问token发生异常");
			result.put("token", null);
			result.put("token_end_date", null);
			return result;
		}

		result.put("success", true);
        long endTime = System.currentTimeMillis ();
		log.info (new Date()+"经过整个accessToken接口(accessToken接口方法)总耗时"+(endTime-t1)+"毫秒");
		return result;
	}
	
	/**
	 * 第三方应用通过该请求访问资源
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/redirect")
	public void redirect(HttpServletRequest request, HttpServletResponse response){
        //这句话的意思，是让浏览器用utf8来解析返回的数据
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        //这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
        response.setCharacterEncoding("UTF-8");
		long startTime = System.currentTimeMillis ();
		// 验证token有效性
		String token = request.getParameter("token");

		Boolean hasToken = redisTemplate.hasKey(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
		
		try {
			if (hasToken == true) {
//				String loginInfoStr =jedisCluster.get(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
//				String menuInfoStr = jedisCluster.get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token);
				String loginInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
			//	String menuInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token);
				SecurityUserBeanInfo securityUserBeanInfo = JacksonUtils.fromJson(loginInfoStr, SecurityUserBeanInfo.class);
			//	SecurityUserBeanRelationInfo securityUserBeanRelationInfo = JacksonUtils.fromJson(menuInfoStr, SecurityUserBeanRelationInfo.class);
				
				HttpSession session = request.getSession();
				session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
			//    session.setAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU,securityUserBeanRelationInfo);


			    // 将cookie信息写入用户安全信息中
				StringBuilder sb = new StringBuilder();
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
					}
					securityUserBeanInfo.setCookies(sb.toString());
				}
				
				// 用户是否禁用
				
				// ip是否禁用
				
				// token是否在有效期内
				
				
				// token有效，跳转url
				String redirectUri = request.getParameter("redirectUri");
				redirectUri = new String(Base64.decodeBase64(redirectUri));
				
				//request.getRequestDispatcher(redirectUri).forward(request, response);http://10.17.3.87:100
//				String serverAddr = "http://10.17.3.72:8080";
//				String serverAddr = "http://10.17.3.87:100";
                long endTime = System.currentTimeMillis ();
                log.info ("redirect"+redirectUri+"耗时:"+(endTime - startTime)+"  毫秒");

                if(redirectUri.indexOf ("http://moa.xyre.com")>-1||redirectUri.indexOf("http://lderp.xyre.com")>-1){
                    String[] arrs = redirectUri.split ("&");
                    StringBuffer sp = new StringBuffer ();
                    for(int i=0;i<arrs.length;i++){
                        String  arr = arrs[i];
                        if(i>0){
                            sp.append ("&");
                        }
                        if(arr.indexOf ("UserId=")>-1){
                            String[] ar =  arr.split ("=");
                            arr = ar[0]+"="+ URLEncoder.encode (ar[1],"utf-8");
                        }
                        sp.append (arr);
                    }
                    redirectUri = sp.toString ();
                    log.info ("经过URLEncoder.encode 处理LLOA跳转用的redirectUri="+redirectUri);
                }

				if(redirectUri.indexOf("tend_code")>-1&&redirectUri.indexOf("/platform-app/flow/runtime/approve/flow.html")>-1){
					//外部系统cc桌面端消息跳转处理
					redirectUri = redirectUri.substring(redirectUri.lastIndexOf("http://"));
				}
				response.sendRedirect(redirectUri);
				
			} else {
				//response.getWriter().write("{\"success\":false, \"message\":\"没有权限\"}");
				response.sendRedirect(request.getContextPath()+"/mobile/error.html");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 提供im接口
	 * @return
	 */
	@RequestMapping(value="/setQRByUser",method=RequestMethod.POST)
	public @ResponseBody MessageResult setQRByUser(@RequestBody Map<String,Object> map,HttpServletRequest request, HttpServletResponse response){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user= LoginUtils.getSecurityUserBeanInfo();
			//获取token
			String token =map.get("token").toString();
			// 验证token有效性
			Boolean hasToken = redisTemplate.hasKey(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
			if (hasToken == true) {
				//获取二维码
				String qrCodeInfo = map.get("qrCodeInfo").toString();
				if(null!=qrCodeInfo&&!"".equals(qrCodeInfo)&&qrCodeInfo.indexOf("k=")>0){
					String key = qrCodeInfo.substring(qrCodeInfo.indexOf("k=")+2);
					// 验证二维码有效性
					Boolean hasQR = redisTemplate.hasKey(key);
					if(hasQR == true){
						//获取token对应人员中权限菜单
						String menuInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token);
						SecurityUserBeanRelationInfo securityUserBeanRelationInfo = JacksonUtils.fromJson(menuInfoStr, SecurityUserBeanRelationInfo.class);
						List<SecurityResourceDto>  list = securityUserBeanRelationInfo.getResourceDtoList();
						Boolean bl = false;
						for(SecurityResourceDto srDto:list){
							if(srDto.getIsAuth().equals("1") && srDto.getType().equals("APPSystem") ){
								//匹配经营分析系统 --- 1.绑定唯一二维码标识到用户  2.生成二维码
								if("BI".equals(srDto.getCode())){
									bl = true;
									break;
								}
							}
						}
						if(bl){
							String value = redisTemplate.opsForValue().get(key);
							Map hashMap = JacksonUtils.fromJson(value,HashMap.class);
							hashMap.put("token",token);
							redisTemplate.opsForValue().set(key,JacksonUtils.toJson(hashMap));
							result.setSuccess(MessageInfo.GETSUCCESS.isResult());
							result.setMsg("请求成功！");
						}else{
							result.setSuccess(MessageInfo.GETERROR.isResult());
							result.setMsg("无权限访问！");
						}
					}else{//二维码失效
						result.setSuccess(MessageInfo.GETERROR.isResult());
						result.setMsg("二维码失效！");
					}
				}else{
					result.setSuccess(MessageInfo.GETERROR.isResult());
					result.setMsg("请检查二维码格式是否正确！");
				}
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg("token失效！");
			}
		} catch (Exception e) {
			log.error("调用setQRByUser方法: ======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg("系统繁忙，请稍后重试！");
		}
		return result;
	}
	
	/**
	 * 第三方应用通过该请求访问资源
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getData")
	public @ResponseBody Map<String, Object> getData(HttpServletRequest request){
		long t1 = System.currentTimeMillis ();
		log.info (new Date()+"进入(getData接口)方法开始计时"+t1);
		// 验证token有效性
		String token = request.getParameter("token");
		log.info(new Date()+"进入(getData接口)获取请求token");
//		Boolean hasToken = jedisCluster.exists (SecurityUserBeanInfo.TOKEN_TEND_USER + token);

		Boolean hasToken = redisTemplate.hasKey(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if (hasToken == true) {
//			String loginInfoStr = jedisCluster.get(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
//			String menuInfoStr = jedisCluster.get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token);

			String loginInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
//			String menuInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token);
			SecurityUserBeanInfo securityUserBeanInfo = JacksonUtils.fromJson(loginInfoStr, SecurityUserBeanInfo.class);
//			SecurityUserBeanRelationInfo securityUserBeanRelationInfo = JacksonUtils.fromJson(menuInfoStr, SecurityUserBeanRelationInfo.class);
			
			HttpSession session = request.getSession();
			session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
	//	    session.setAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU,securityUserBeanRelationInfo);
		 // 将cookie信息写入用户安全信息中
			StringBuilder sb = new StringBuilder();
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
				}
				securityUserBeanInfo.setCookies(sb.toString());
			}
			
			// 用户是否禁用
			
			// ip是否禁用
			
			// token是否在有效期内
			
			
			// token有效，跳转url
		    String method = request.getParameter("method");
		    if (StringUtils.isNotBlank(method)) {
		    	
		    	try {
		    		long t2 = System.currentTimeMillis ();
		    		String result=null;
		    		log.info (new Date()+"sysNoticeMsgDtoServiceCustomer(getData接口)反射调用接口方法{"+method+"}开始"+loginInfoStr);
		    		if("queryTotalStatData".equals(method)){
		    			List<String> lines = FileUtils.readLines("/msg-sync-path.properties");
		    			List<String> syncTendCodes = getSyncTendCode(lines, securityUserBeanInfo);
		    			String loginName = securityUserBeanInfo.getSecurityUserDto().getLoginName();
		    			Map<String,Object> map=new HashMap<String,Object>();
						map.put("loginName", loginName);
		    			SysNoticeMsgDto msgDto  = PageQueryAcrossDbUtil.getMsgMoreTotalData(sysNoticeMsgDtoServiceCustomer, 
		    					userOutServiceCustomer, syncTendCodes, securityUserBeanInfo, map);
		    			Map<String,Object> res=new HashMap<String,Object>();
		    			res.put("sucess", true);
		    			res.put("result", msgDto);
		    			result=JacksonUtils.toJson(res);
		    		}else {
						
		    			Method call = sysNoticeMsgDtoServiceCustomer.getClass().getDeclaredMethod(method, String.class, String.class);
		    			result = (String) call.invoke(sysNoticeMsgDtoServiceCustomer, loginInfoStr, "{}");
					}
					long t3 = System.currentTimeMillis ();
					log.info (new Date()+"sysNoticeMsgDtoServiceCustomer(getData接口)反射调用接口方法{"+method+"}结束耗时"+(t3-t2));
//					String result = sysNoticeMsgDtoServiceCustomer.queryTotalStatData(loginInfoStr, "{}");
			    	Map<String, Object> resultMap = JacksonUtils.fromJson(result, HashMap.class);
			    	Boolean success = (Boolean) resultMap.get("sucess");
			    	if (success == true) {
			    		returnMap.put("success", true);
			    		returnMap.put("message", "获取数据成功");
			    		returnMap.put("result", resultMap.get("result"));
			    	} else {
			    		returnMap.put("success", false);
			    		returnMap.put("message", "获取数据失败");
			    	}
			    	
		    	} catch (NoSuchMethodException e) {
		    		returnMap.put("success", false);
				    returnMap.put("message", "没有此方法：" + method);
				} catch (Exception e) {
					e.printStackTrace();
					returnMap.put("success", false);
				    returnMap.put("message", "调用方法发生错误：" + method);
				}
		    	
		    }
			
		} else {
			returnMap.put("success", false);
		    returnMap.put("message", "没有权限");
		}
		long t4 = System.currentTimeMillis ();
		log.info (new Date()+"调用(getData接口)结束总耗时"+(t4-t1));
		return returnMap;
		
	}
	private List<String> getSyncTendCode(List<String> lines, SecurityUserBeanInfo userInfo) {
		List<String> syncTendCode = new ArrayList<String>();
		syncTendCode.add(userInfo.getTendCode());
		if(CollectionUtils.isEmpty(lines)) {
			return syncTendCode;
		}
		
		for(String line : lines) {
			String[] mapArray = line.split(":");
			if(mapArray == null || mapArray.length != 3) {
				log.info("租户间消息同步配置错误：" + line);
				continue;
				
			} else {
				if(userInfo.getTendCode().equals(mapArray[0])
						&& mapArray[2].contains(userInfo.getSecurityUserDto().getLoginName())) {
					syncTendCode.add(mapArray[1]);
				}
			}
		}
		
		return syncTendCode;
	}
	/**
	 * 获取json格式数据
	 * 		token：第三方获取数据的用户token
	 * 		forwardUri：响应数据所在url
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getCommonData")
	public void getCommonData(HttpServletRequest request,HttpServletResponse response){
		long t1 = System.currentTimeMillis ();
		log.info (new Date()+"进入(getCommonData接口)方法开始计时"+t1);

		String forwardUri = request.getParameter("forwardUri");
		if(forwardUri==null){
			forwardUri = "/flow/sysNoticeMsg/getEmailUnreadCount";
		}else{
			forwardUri = new String(Base64.decodeBase64(forwardUri));
		}

		// 验证token有效性
		String token = request.getParameter("token");
		log.info(new Date()+"进入(getCommonData接口)获取请求token");
//		Boolean hasToken = jedisCluster.exists (SecurityUserBeanInfo.TOKEN_TEND_USER + token);

		Boolean hasToken = redisTemplate.hasKey(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if (hasToken == true) {
//			String loginInfoStr = jedisCluster.get(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
//			String menuInfoStr = jedisCluster.get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token);

			String loginInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
//			String menuInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token);
			SecurityUserBeanInfo securityUserBeanInfo = JacksonUtils.fromJson(loginInfoStr, SecurityUserBeanInfo.class);
//			SecurityUserBeanRelationInfo securityUserBeanRelationInfo = JacksonUtils.fromJson(menuInfoStr, SecurityUserBeanRelationInfo.class);

			HttpSession session = request.getSession();
			session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
			//	    session.setAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU,securityUserBeanRelationInfo);
			// 将cookie信息写入用户安全信息中
			StringBuilder sb = new StringBuilder();
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
				}
				securityUserBeanInfo.setCookies(sb.toString());
			}
			try {
				request.getRequestDispatcher(forwardUri).forward(request,response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			returnMap.put("success", false);
			returnMap.put("msg", "没有权限");
			PrintWriter out = null;
			try {
				out = response.getWriter();
				out.append(JacksonUtils.toJson(returnMap));
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(out!=null){
					out.close();
				}
				long t4 = System.currentTimeMillis ();
				log.info (new Date()+"调用(getData接口)结束总耗时"+(t4-t1));
			}
		}
	}


	/**
	 * 根据微信获取用户登录号
	 * @param parameters
	 * @return
	 */
	@RequestMapping("/getLoginNameByVX")
	public @ResponseBody Object getLoginNameByVX(@RequestBody Map<String, Object> parameters){
		MessageResult result=new MessageResult();
		try {
			String dubboResultInfo=sysNoticeMsgDtoServiceCustomer.getLoginNameByVX(null, JacksonUtils.toJson(parameters));
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String, Object> res=JacksonUtils.fromJson(resultInfo, HashMap.class);
				result.setResult(res);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用getLoginNameByVX方法: ======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	/**
	 * 第三方应用通过该请求访问资源
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/wxLoginByToken")
	public @ResponseBody Object wxLoginByToken(HttpServletRequest request, HttpServletResponse response){
		MessageResult result=new MessageResult();
		// 验证token有效性
		String token = request.getParameter("token");
//		Boolean hasToken = jedisCluster.exists (SecurityUserBeanInfo.TOKEN_TEND_USER + token);

		Boolean hasToken = redisTemplate.hasKey(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
		
		if (hasToken == true) {
//			String loginInfoStr = jedisCluster.get(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
//			String menuInfoStr = jedisCluster.get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token);

			String loginInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanInfo.TOKEN_TEND_USER + token);
//			String menuInfoStr = redisTemplate.opsForValue().get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token);
			SecurityUserBeanInfo securityUserBeanInfo = JacksonUtils.fromJson(loginInfoStr, SecurityUserBeanInfo.class);
//			SecurityUserBeanRelationInfo securityUserBeanRelationInfo = JacksonUtils.fromJson(menuInfoStr, SecurityUserBeanRelationInfo.class);
			
			HttpSession session = request.getSession();
			session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
	//	    session.setAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU,securityUserBeanRelationInfo);
		 // 将cookie信息写入用户安全信息中
			StringBuilder sb = new StringBuilder();
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
				}
				securityUserBeanInfo.setCookies(sb.toString());
			}
		    result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}
		return result;
	}
	
	@RequestMapping(value = "/setLoginInfo", method={RequestMethod.GET})
	public @ResponseBody Object setLoginInfo(HttpServletRequest request, HttpServletResponse response){
		MessageResult result=new MessageResult();
		String tendCode = request.getParameter("tendCode");
		String userId = request.getParameter("userId");
		SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
		securityUserBeanInfo.setTendCode(tendCode);

//		String loginName = null, 
//		domainName = null;
//		String paramaterJson = null;
//		Map<String, Object> map = new HashMap<>();
//		if (StringUtils.isNotEmpty(fullLoginName) && fullLoginName.indexOf('@') > 0) {
//			loginName = fullLoginName.substring(0, fullLoginName.indexOf('@'));
//			domainName = fullLoginName.substring( fullLoginName.indexOf('@') + 1);
//			map.put("domain", domainName);
//			map.put("loginName", loginName);
//			
//			paramaterJson = JacksonUtils.toJson(map);
//			try {
//				// 根据域名查询租户信息
//				String dubboResultInfo = authenticationDtoServiceCustomer.getDomain(null, paramaterJson);
//				DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
//				if (dubboServiceResultInfo.isSucess()) {
//					String Info = dubboServiceResultInfo.getResult();
//					@SuppressWarnings("rawtypes")
//					Map demain = JacksonUtils.fromJson(Info, HashMap.class);
//					if (demain != null) {
//						securityUserBeanInfo.setTendId(demain.get("tendId").toString());
//						securityUserBeanInfo.setTendCode(demain.get("tendCode").toString());
//					}
//				}
//			} catch (Exception e) {
//				// TODO 租户信息查询失败，返回告知
//				log.info ("查询用户信息发生错误");
//				result.setMsg("查询用户信息发生错误");
//				return result;
//			}
//			
//		}
		
		SecurityUserDto securityUserDto = new SecurityUserDto();
		securityUserDto.setId(userId);
		securityUserDto.setRealName("");
		securityUserBeanInfo.setSecurityUserDto(securityUserDto);
		
		HttpSession session = request.getSession();
		session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
		
		SecurityUserBeanRelationInfo relationInfo = new SecurityUserBeanRelationInfo();
		session.setAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU, relationInfo);
		// 将cookie信息写入用户安全信息中
		StringBuilder sb = new StringBuilder();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
			}
			securityUserBeanInfo.setCookies(sb.toString());
		}
		result.setSuccess(true);
		return result;
	}

	/**
	 * 第三方认证，登陆平台首页
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/login")
	public void login(HttpServletRequest request, HttpServletResponse response){

		Map<String, Object> result = new HashMap<>();
	    try {
			//这句话的意思，是让浏览器用utf8来解析返回的数据
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			//这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859
			response.setCharacterEncoding("UTF-8");
			PrintWriter pw = response.getWriter ();
		/*
		 *  根据用户名认证并获取对应的权限数据放到session中
		 */

			// 构建用户安全信息
			SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo ();
			SecurityUserBeanRelationInfo securityUserBeanRelationInfo = new SecurityUserBeanRelationInfo ();
			securityUserBeanInfo.setSecurityUserDto (new SecurityUserDto ());

			// 获取第三方请求用户名
			String userInfo = request.getParameter ("userInfo");
			if(userInfo!=null&&userInfo.indexOf ("@wuye")>-1){
				userInfo = userInfo.replace ("@wuye","@xy");
			}
			String loginName = null,
					domainName = null;
			String paramaterJson = null;
			Map<String, Object> map = new HashMap<> ();
			if (StringUtils.isNotEmpty (userInfo) && userInfo.indexOf ('@') > 0) {
				loginName = userInfo.substring (0, userInfo.indexOf ('@'));
				domainName = userInfo.substring (userInfo.indexOf ('@') + 1);
				map.put ("domain", domainName);
				map.put ("loginName", loginName);

				paramaterJson = JacksonUtils.toJson (map);
				try {
					// 根据域名查询租户信息
					String dubboResultInfo = authenticationDtoServiceCustomer.getDomain (null, paramaterJson);
					DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson (dubboResultInfo, DubboServiceResultInfo.class);
					if (dubboServiceResultInfo.isSucess ()) {
						String Info = dubboServiceResultInfo.getResult ();
						@SuppressWarnings("rawtypes")
						Map demain = JacksonUtils.fromJson (Info, HashMap.class);
						if (demain != null) {
							securityUserBeanInfo.setTendId (demain.get ("tendId").toString ());
							securityUserBeanInfo.setTendCode (demain.get ("tendCode").toString ());
						}
					}
				} catch (Exception e) {
					// TODO 租户信息查询失败，返回告知
					log.info ("查询用户信息发生错误");
					result.put ("success", false);
					result.put ("message", "查询用户信息发生错误");

					pw.write ("<script language='javascript'>alert('用户权限不足，请联系管理员！');window.history.back(-1); </script>");
					pw.flush ();
					pw.close ();
				}

			} else {
				loginName = userInfo;
				String tendId = request.getParameter ("tendId");
				String tendCode = request.getParameter ("tendCode");
				if (StringUtils.isNotBlank (tendId) && StringUtils.isNotBlank (tendCode)) {
					securityUserBeanInfo.setTendId (tendId);
					securityUserBeanInfo.setTendCode (tendCode);
					map.put ("loginName", userInfo);
					paramaterJson = JacksonUtils.toJson (map);
				} else {
					// TODO 用户参数信息不合法
					log.info ("请求参数信息不合法，用户信息应为：loginName@tend" + " === " + userInfo);
					result.put ("success", false);
					result.put ("message", "请求参数信息不合法，用户信息应为：loginName@tend");
					pw.write ("<script language='javascript'>alert('"+userInfo+"请求参数信息不合法，用户信息应为：loginName@tend！');window.history.back(-1); </script>");
					pw.flush ();
					pw.close ();
				}
			}

			try {
				String dubboResultInfo = null;
				DubboServiceResultInfo dubboServiceResultInfo = null;

				// 根据请求参数appId和appSecret匹配第三方应用信息
				IntegrateAppDto integrateAppDto = new IntegrateAppDto ();
				String appId = request.getParameter ("appId");
				String appSecret = request.getParameter ("appSecret");
				String userInfoJson = JacksonUtils.toJson (securityUserBeanInfo);
				if (StringUtils.isNotBlank (appId) && StringUtils.isNotBlank (appSecret)) {
					integrateAppDto.setSecret (appSecret);
					integrateAppDto.setCode (appId);
					dubboResultInfo = integrateAppDtoServiceCustomer.queryList (userInfoJson, JacksonUtils.toJson (integrateAppDto));
					dubboServiceResultInfo = JacksonUtils.fromJson (dubboResultInfo, DubboServiceResultInfo.class);
					if (dubboServiceResultInfo.isSucess ()) {
						String resultInfo = dubboServiceResultInfo.getResult ();
						List<IntegrateAppDto> appList = JacksonUtils.fromJson (resultInfo, ArrayList.class, IntegrateAppDto.class);
						if (null != appList && appList.size () == 1) {

						} else {
							// TODO 没有正确匹配出应用信息
							log.info ("查询应用信息发生错误");
							result.put ("success", false);
							result.put ("message", "查询应用信息发生错误");
							pw.write ("<script language='javascript'>alert('查询应用信息发生错误!');window.history.back(-1); </script>");
							pw.flush ();
							pw.close ();
						}
					} else {
						// TODO 应用信息查询失败
						log.info ("查询应用信息发生错误");
						result.put ("success", false);
						result.put ("message", "查询应用信息发生错误");
						pw.write ("<script language='javascript'>alert('查询应用信息发生错误!');window.history.back(-1); </script>");
						pw.flush ();
						pw.close ();
					}
				} else {
					// TODO 应用参数信息不合法
					log.info ("应用参数信息不合法");
					result.put ("success", false);
					result.put ("message", "应用参数信息不合法");
					pw.write ("<script language='javascript'>alert('应用参数信息不合法!');window.history.back(-1); </script>");
					pw.flush ();
					pw.close ();
				}

				// 根据用户信息从DB中查询token
				UserDto userDto = new UserDto ();
				userDto.setLoginName (loginName);
				userDto.setDelflag (false);
				dubboResultInfo = userDtoServiceCustomer.queryList (JacksonUtils.toJson (securityUserBeanInfo), JacksonUtils.toJson (userDto));
				dubboServiceResultInfo = JacksonUtils.fromJson (dubboResultInfo, DubboServiceResultInfo.class);
				if (dubboServiceResultInfo.isSucess ()) {
					String resultInfo = dubboServiceResultInfo.getResult ();
					List<UserDto> userList = JacksonUtils.fromJson (resultInfo, ArrayList.class, UserDto.class);
					if (null != userList && userList.size () == 1) {
						UserDto user = userList.get (0);

						// 查询第三方用户配置信息
						UserConfigDto userConfigDto = new UserConfigDto ();
						userConfigDto.setUserId (user.getId ());
						userConfigDto.setDelflag (false);
						dubboResultInfo = userConfigDtoServiceCustomer.queryList (JacksonUtils.toJson (securityUserBeanInfo), JacksonUtils.toJson (userConfigDto));
						dubboServiceResultInfo = JacksonUtils.fromJson (dubboResultInfo, DubboServiceResultInfo.class);

						if (dubboServiceResultInfo.isSucess ()) {
							resultInfo = dubboServiceResultInfo.getResult ();
							List<UserConfigDto> userConfigList = JacksonUtils.fromJson (resultInfo, ArrayList.class, UserConfigDto.class);
							if (null != userConfigList && userConfigList.size () == 1) {
								UserConfigDto uc = userConfigList.get (0);

								// 匹配出token
								result.put ("token", uc.getToken ());
								result.put ("token_end_date", uc.getEndTime ());
							} else {
								// TODO 没有正确查询匹配出第三方用户配置信息
//							result.put("success", false);
//							result.put("message", "没有相应的用户信息");
//							return result;
								// 分配token
								Calendar cal = Calendar.getInstance ();
								UserConfigDto ucd = new UserConfigDto ();
								ucd.setAppId (appId);
								ucd.setDelflag (false);
								ucd.setStartTime (cal.getTime ());
								ucd.setId (IDGenerator.getUUID ());
								cal.add (Calendar.YEAR, 1);
								ucd.setEndTime (cal.getTime ());
								ucd.setStatus ("1");
								ucd.setTendId (securityUserBeanInfo.getTendId ());
								ucd.setToken (IDGenerator.getUUID ());
								ucd.setUserId (user.getId ());
								ucd.setCreatePersonName ("程序产生");
								userConfigDtoServiceCustomer.save (JacksonUtils.toJson (securityUserBeanInfo), JacksonUtils.toJson (ucd));
								result.put ("token", ucd.getToken ());
								result.put ("token_end_date", ucd.getEndTime ());
							}
						} else {
							// TODO 第三方用户配置信息查询失败
							log.info ("查询用户配置信息失败");
							result.put ("success", false);
							result.put ("message", "查询用户配置信息失败");
							pw.write ("<script language='javascript'>alert('用户权限不足，请联系管理员！');window.history.back(-1); </script>");
							pw.flush ();
							pw.close ();
						}
					} else {
						// TODO 没有正确查询出匹配的用户信息
						log.info ("查询用户信息失败");
						result.put ("success", false);
						result.put ("message", "查询用户信息失败");
						pw.write ("<script language='javascript'>alert('用户权限不足，请联系管理员！');window.history.back(-1); </script>");
						pw.flush ();
						pw.close ();
					}
				} else {
					// TODO 用户信息查询失败
					log.info ("查询用户配置信息失败");
					result.put ("success", false);
					result.put ("message", "查询用户配置信息失败");
					pw.write ("<script language='javascript'>alert('用户权限不足，请联系管理员！');window.history.back(-1); </script>");
					pw.flush ();
					pw.close ();
				}

				dubboResultInfo = authenticationDtoServiceCustomer.preCheck (JacksonUtils.toJson (securityUserBeanInfo), paramaterJson);
				dubboServiceResultInfo = JacksonUtils.fromJson (dubboResultInfo, DubboServiceResultInfo.class);
				if (dubboServiceResultInfo.isSucess ()) {
					// 获取用户角色
					String resultInfo = dubboServiceResultInfo.getResult ();
					SecurityUserDto securityUserDto = JacksonUtils.fromJson (resultInfo, SecurityUserDto.class);

					// 将cookie信息写入用户安全信息中
					StringBuilder sb = new StringBuilder ();
					Cookie[] cookies = request.getCookies ();
					log.info ("请求 httpPost cookies:"+JacksonUtils.toJson (request.getCookies ()));
					if (cookies != null) {
						for (Cookie cookie : cookies) {
							if(Objects.equals (cookie.getName (),"DTL_SESSION_ID")){
								sb.append (cookie.getName () + "=" + cookie.getValue () + ";");
								break;
							}
						}
						if(sb.toString ().indexOf ("DTL_SESSION_ID")==-1){
							log.info ("请求 httpPost cookies 为空，设置request.getSession ().getId ()："+request.getSession ().getId ());
							sb.append ("DTL_SESSION_ID="+request.getSession ().getId ());
						}
						securityUserBeanInfo.setCookies (sb.toString ());
					}else{
						securityUserBeanInfo.setCookies ("DTL_SESSION_ID="+request.getSession ().getId ()+";");
					}
					log.info ("设置securityUserBeanInfo.setCookies后的 httpPost cookies:"+JacksonUtils.toJson (securityUserBeanInfo.getCookies ()));
					securityUserBeanInfo.setSecurityUserDto (securityUserDto);
                    Date t0 = new Date();
                    System.out.println("获取认证信息总时间开始："+t0.getTime());
					// 获取用户相关授权信息
					String authenticationInfodubboResultInfo = authenticationDtoServiceCustomer.getUserAuthenticationInfo (JacksonUtils.toJson (securityUserBeanInfo), JacksonUtils.toJson (securityUserDto));
                    Date t1 = new Date();
                    System.out.println("获取认证信息总时间结束："+t1.getTime()+"用时：：：：：：：：："+(t1.getTime()-t0.getTime()));
                    DubboServiceResultInfo authenticationInfodubboServiceResultInfo = JacksonUtils.fromJson (authenticationInfodubboResultInfo, DubboServiceResultInfo.class);
					if (authenticationInfodubboServiceResultInfo.isSucess ()) {
						String authenticationInforesultInfo = authenticationInfodubboServiceResultInfo.getResult ();
						AuthenticationDto authenticationDto = JacksonUtils.fromJson (authenticationInforesultInfo, AuthenticationDto.class);
						//获取用户标准岗位
						List<SecurityStandardRoleDto> securityStandardRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getStandardRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
						securityUserBeanInfo.setSecurityStandardRoleDtoList(securityStandardRoleDtoList);

						//获取用户通用角色
						List<SecurityStandardRoleDto> securityCurrencyRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getCurrencyRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
						securityUserBeanInfo.setSecurityCurrencyRoleDtoList(securityCurrencyRoleDtoList);
						//获取用户岗位
						List<SecurityPostDto> securityPostDtoList = JacksonUtils.fromJson (JacksonUtils.toJson (authenticationDto.getPostDtoList ()), ArrayList.class, SecurityPostDto.class);
						securityUserBeanInfo.setSecurityPostDtoList (securityPostDtoList);
						//当前用户的菜单清单（未授权和已授权的）
						List<SecurityResourceDto> SecurityResourceDtoList = JacksonUtils.fromJson (JacksonUtils.toJson (authenticationDto.getResourceDtoList ()), ArrayList.class, SecurityResourceDto.class);
						securityUserBeanRelationInfo.setResourceDtoList (SecurityResourceDtoList);
						//当前用户所在组织的类型
						String securityOrganizationType = authenticationDto.getOrganizationType ();
						securityUserBeanInfo.setSecurityOrganizationType (securityOrganizationType);
						//当前用户的一级公司
						SecurityOrganizationDto securityTopCompanyDto = JacksonUtils.fromJson (JacksonUtils.toJson (authenticationDto.getTopCompanyDto ()), SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityTopCompanyDto (securityTopCompanyDto);
						//当前用户的直属公司
						SecurityOrganizationDto securityDirectCompanyDto = JacksonUtils.fromJson (JacksonUtils.toJson (authenticationDto.getDirectCompanyDto ()), SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityDirectCompanyDto (securityDirectCompanyDto);
						//当前用户的一级部门
						SecurityOrganizationDto securityTopDeptDto = JacksonUtils.fromJson (JacksonUtils.toJson (authenticationDto.getTopDeptDto ()), SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityTopDeptDto (securityTopDeptDto);
						//当前用户的直属部门
						SecurityOrganizationDto securityDirectDeptDto = JacksonUtils.fromJson (JacksonUtils.toJson (authenticationDto.getDirectDeptDto ()), SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityDirectDeptDto (securityDirectDeptDto);
						//当前用户的项目
						SecurityOrganizationDto securityGroupDto = JacksonUtils.fromJson (JacksonUtils.toJson (authenticationDto.getGroupDto ()), SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityGroupDto (securityGroupDto);
						//当前用户的分期
						SecurityOrganizationDto securityBranchDto = JacksonUtils.fromJson (JacksonUtils.toJson (authenticationDto.getBranchDto ()), SecurityOrganizationDto.class);
						securityUserBeanInfo.setSecurityBranchDto (securityBranchDto);

					}

					long endTime = ((Date) result.get ("token_end_date")).getTime ();
					redisTemplate.opsForValue ().set (SecurityUserBeanInfo.TOKEN_TEND_USER + result.get ("token"), JacksonUtils.toJson (securityUserBeanInfo), endTime, TimeUnit.MILLISECONDS);
					redisTemplate.opsForValue ().set (SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + result.get ("token"), JacksonUtils.toJson (securityUserBeanRelationInfo), endTime, TimeUnit.MILLISECONDS);
					HttpSession session = request.getSession ();
					session.setAttribute (SecurityUserBeanInfo.TOKEN_TEND_USER, securityUserBeanInfo);
					session.setAttribute (SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU, securityUserBeanRelationInfo);
					result.put ("DTL_SESSION_ID", session.getId ());
					response.sendRedirect ("/platform-app/index.html?_t=" + new Date ().getTime ());
				}
			} catch (Exception e) {
				log.error ("调用queryList方法:  【参数" + paramaterJson + "】======" + "【" + e.getMessage () + "】");
				result.put ("success", false);
				result.put ("message", "获取访问token发生异常");
				result.put ("token", null);
				result.put ("token_end_date", null);
				pw.write ("<script language='javascript'>alert('获取访问token发生异常!');window.history.back(-1); </script>");
				pw.flush ();
				pw.close ();
			}

		}catch(Exception e1){

		}

	}



	/**
	 * 第三方认证，获取访问token2--luorongxin 2017-09-08 替换成accessToken
	 *
	 * @param request
	 * @return
	 */
	/*@RequestMapping(value="/accessToken2",method={RequestMethod.GET}, consumes="application/json")
	public @ResponseBody Map<String, Object> accessToken2(HttpServletRequest request){
//		File f = null;
		*//*if(OS.isLinux){
			 f=new File("/tmp/accessToken2log.txt");
		}else{
			 f=new File("C:\\accessToken2log.txt");
		}*//*
		long t1 = System.currentTimeMillis ();
		log.info (new Date()+"进入获取accessToken2方法(accessToken2接口方法)开始计算耗时"+t1);
//		FileWriter fw=null;
//		PrintWriter out = null;
		*//*try{
			if(!f.exists()){
				f.createNewFile();
			}
			fw=new FileWriter(f.getAbsoluteFile(),true);  //true表示可以追加新内容
			//fw=new FileWriter(f.getAbsoluteFile()); //表示不追加
			out = new PrintWriter(fw);
			//out.println("进入获取token方法。。。。。。。。。。。。开始计算耗时");*//*
			Map<String, Object> result = new HashMap<>();

		*//*
		 *  根据用户名认证并获取对应的权限数据放到session中
		 *//*

			// 构建用户安全信息
			SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo();
			securityUserBeanInfo.setSecurityUserDto(new SecurityUserDto());
			// 获取第三方请求用户名
			String userInfo = request.getParameter("userInfo");
			String loginName = null;
			String domainName = null;
			String paramaterJson = null;
			Map<String, Object> map = new HashMap<>();
			if (StringUtils.isNotEmpty(userInfo) && userInfo.indexOf('@') > -1) {
				loginName = userInfo.substring(0, userInfo.indexOf('@'));
				domainName = userInfo.substring( userInfo.indexOf('@') + 1);
				map.put("domain", domainName);
				map.put("loginName", loginName);
				paramaterJson = JacksonUtils.toJson(map);
				try {
					// 根据域名查询租户信息
					long au1 = System.currentTimeMillis ();
					String dubboResultInfo = authenticationDtoServiceCustomer.getDomain(null, paramaterJson);
					long au2 = System.currentTimeMillis ();
					log.info (new Date()+"根据域名查询租户信息authenticationDtoServiceCustomer.getDomain(accessToken2接口方法)方法耗时"+(au2-au1));
//					//out.println("根据域名查询租户信息..........authenticationDtoServiceCustomer.getDomain.........方法耗时"+(au2-au1)+" 参数 "+paramaterJson);
					DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
					if (dubboServiceResultInfo.isSucess()) {
						String Info = dubboServiceResultInfo.getResult();
						@SuppressWarnings("rawtypes")
						Map demain = JacksonUtils.fromJson(Info, HashMap.class);
						if (demain != null) {
							securityUserBeanInfo.setTendId(demain.get("tendId").toString());
							securityUserBeanInfo.setTendCode(demain.get("tendCode").toString());
						}
					}
				} catch (Exception e) {
					// TODO 租户信息查询失败，返回告知
					log.info ("查询用户信息发生错误");
					result.put("success", false);
					result.put("message", "查询用户信息发生错误");
					return result;
				}

			} else {
				loginName = userInfo;
				String tendId = request.getParameter("tendId");
				String tendCode = request.getParameter("tendCode");
				if (StringUtils.isNotBlank(tendId) && StringUtils.isNotBlank(tendCode)) {
					securityUserBeanInfo.setTendId(tendId);
					securityUserBeanInfo.setTendCode(tendCode);
					map.put("loginName", userInfo);
					paramaterJson = JacksonUtils.toJson(map);
				} else {
					// TODO 用户参数信息不合法
					log.info ("请求参数信息不合法，用户信息应为：loginName@tend"+" === "+userInfo);
					result.put("success", false);
					result.put("message", "请求参数信息不合法，用户信息应为：loginName@tend");
					return result;
				}
			}
			try {
				// 根据请求参数appId和appSecret匹配第三方应用信息
				String appId = request.getParameter("appId");
				String appSecret = request.getParameter("appSecret");
				String userInfoJson = JacksonUtils.toJson(securityUserBeanInfo);
				if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(appSecret)) {
					Map param = new HashMap ();
					param.put ("code",appId);
					param.put ("appSecret",appSecret);
					param.put ("loginName",loginName);
					param.put ("domain",domainName);
					long check0 = System.currentTimeMillis ();
					String resultInfo = integrateAppDtoServiceCustomer.thirdPartyCheck (userInfoJson,JacksonUtils.toJson (param));
					long check1 = System.currentTimeMillis ();
					log.info (new Date()+"integrateAppDtoServiceCustomer.thirdPartyCheck(accessToken2接口方法) 方法耗时："+(check1-check0));
					//out.println ("integrateAppDtoServiceCustomer.thirdPartyCheck 方法耗时："+(check1-check0));
					DubboServiceResultInfo thirdPartCheckResultInfo = JacksonUtils.fromJson (resultInfo,DubboServiceResultInfo.class);
					if(thirdPartCheckResultInfo.isSucess()){
						// 获取用户角色
						String thirdPartyResultInfo = thirdPartCheckResultInfo.getResult();
						Map thirdPartyResultMap = JacksonUtils.fromJson(thirdPartyResultInfo, Map.class);
						String token = String.valueOf (thirdPartyResultMap.get ("token"));
						String token_end_date = String.valueOf (thirdPartyResultMap.get ("token_end_date"));
						result.put ("token",token);
						result.put ("token_end_date",token_end_date);
						SecurityUserDto securityUserDto = JacksonUtils.fromJson (JacksonUtils.toJson (thirdPartyResultMap.get ("user")),SecurityUserDto.class);
						// 将cookie信息写入用户安全信息中
						StringBuilder sb = new StringBuilder();
						Cookie[] cookies = request.getCookies();
						if (cookies != null) {
							for (Cookie cookie : cookies) {
								sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
							}
							securityUserBeanInfo.setCookies(sb.toString());
						}
						securityUserBeanInfo.setSecurityUserDto(securityUserDto);

						// 获取用户相关授权信息
						long t2 = System.currentTimeMillis ();
						String authenticationInfodubboResultInfo = authenticationDtoServiceCustomer.getUserAuthenticationInfoWithoutResource(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(securityUserDto));
						log.info (authenticationInfodubboResultInfo);
						long t3 = System.currentTimeMillis ();
						log.info (new Date()+"执行用户数据权限和菜单查询-authenticationDtoServiceCustomer.getUserAuthenticationInfo方法(accessToken2接口方法)耗时："+(t3-t2));
						//out.println("执行用户数据权限和菜单查询---------------------authenticationDtoServiceCustomer.getUserAuthenticationInfo（）方法结束----------------方法耗时："+(t3-t2)+" ");
						DubboServiceResultInfo authenticationInfodubboServiceResultInfo = JacksonUtils.fromJson(authenticationInfodubboResultInfo, DubboServiceResultInfo.class);
						if (authenticationInfodubboServiceResultInfo.isSucess()) {
							String authenticationInforesultInfo = authenticationInfodubboServiceResultInfo.getResult();
							AuthenticationDto authenticationDto = JacksonUtils.fromJson(authenticationInforesultInfo, AuthenticationDto.class);
							//获取用户标准岗位
							List<SecurityStandardRoleDto> securityStandardRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getStandardRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
							securityUserBeanInfo.setSecurityStandardRoleDtoList(securityStandardRoleDtoList);

							//获取用户通用角色
							List<SecurityStandardRoleDto> securityCurrencyRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getCurrencyRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
							securityUserBeanInfo.setSecurityCurrencyRoleDtoList(securityCurrencyRoleDtoList);
							//获取用户岗位
							List<SecurityPostDto> securityPostDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getPostDtoList()),ArrayList.class,SecurityPostDto.class);
							securityUserBeanInfo.setSecurityPostDtoList(securityPostDtoList);
							//当前用户所在组织的类型
							String securityOrganizationType = authenticationDto.getOrganizationType();
							securityUserBeanInfo.setSecurityOrganizationType(securityOrganizationType);
							//当前用户的一级公司
							SecurityOrganizationDto securityTopCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopCompanyDto()),SecurityOrganizationDto.class);
							securityUserBeanInfo.setSecurityTopCompanyDto(securityTopCompanyDto);
							//当前用户的直属公司
							SecurityOrganizationDto securityDirectCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectCompanyDto()),SecurityOrganizationDto.class);
							securityUserBeanInfo.setSecurityDirectCompanyDto(securityDirectCompanyDto);
							//当前用户的一级部门
							SecurityOrganizationDto securityTopDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopDeptDto()),SecurityOrganizationDto.class);
							securityUserBeanInfo.setSecurityTopDeptDto(securityTopDeptDto);
							//当前用户的直属部门
							SecurityOrganizationDto securityDirectDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectDeptDto()),SecurityOrganizationDto.class);
							securityUserBeanInfo.setSecurityDirectDeptDto(securityDirectDeptDto);
							//当前用户的项目
							SecurityOrganizationDto securityGroupDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getGroupDto()),SecurityOrganizationDto.class);
							securityUserBeanInfo.setSecurityGroupDto(securityGroupDto);
							//当前用户的分期
							SecurityOrganizationDto securityBranchDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getBranchDto()),SecurityOrganizationDto.class);
							securityUserBeanInfo.setSecurityBranchDto(securityBranchDto);
						}

						long endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse (token_end_date).getTime();
						long redisSetT0 = System.currentTimeMillis ();
						redisTemplate.opsForValue().set(SecurityUserBeanInfo.TOKEN_TEND_USER + token, JacksonUtils.toJson(securityUserBeanInfo), endTime, TimeUnit.MILLISECONDS);
						long redisSetT1 = System.currentTimeMillis ();
						log.info (new Date()+"redis数据set(accessToken2接口方法)耗时"+(redisSetT1-redisSetT0));


						HttpSession session = request.getSession();
						session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
						long finalTime = System.currentTimeMillis ();
						log.info(new Date()+"总耗时方法(accessToken2接口方法)耗时："+ (finalTime-t1));
						//out.println ("session。。。。。。 session.setAttribute(。。。。。。。。。。方法耗时"+(t8-t7)+" ");
						//out.println ("总耗时方法耗时："+ (t8-t1));
						result.put("DTL_SESSION_ID", session.getId());
					}else{
						result.put ("success",false);
						result.put("message",thirdPartCheckResultInfo.getMsg () );
						result.put("token", null);
						result.put("token_end_date", null);
						return result;
					}
				}

			} catch (Exception e) {
				log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
				result.put("success", false);
				result.put("message", "获取访问token发生异常");
				result.put("token", null);
				result.put("token_end_date", null);
				return result;
			}
			result.put("success", true);
			long totalTime = System.currentTimeMillis ();
			log.info (new Date()+"经过整个accessToken2接口(accessToken2接口方法)总耗时"+(totalTime-t1));
			log.info (new Date()+" 离开accessToken2(accessToken2接口方法)接口");
			return result;
	}*/

	/**
	 * 验证用户登陆状态
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkLogin")
	public @ResponseBody MessageResult checkLogin(HttpServletRequest request, HttpServletResponse response) {
		MessageResult result = new MessageResult();
		//跨租户消息问题，暂时注掉
		String tendCode = request.getParameter("tendCode");

		try {
			HttpSession session = request.getSession();
			SecurityUserBeanInfo securityUserBeanInfo = (SecurityUserBeanInfo) session.getAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER);
			SecurityUserBeanRelationInfo securityUserBeanRelationInfo = (SecurityUserBeanRelationInfo) session.getAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU);

			if (securityUserBeanInfo != null) {
				result.setSuccess(true);
			} else {
				if(tendCode!=null){
					Session springSession = MultiSessionUrlEncode.getSessionIdByAlias(request,tendCode);
					SecurityUserBeanInfo securityUserBeanInfo1 = springSession.getAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER);
					if(securityUserBeanInfo1==null){
						String loginName = request.getParameter("loginName");
						securityUserBeanInfo1 = setUserBeanInfo(request,loginName,tendCode);
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("tendCode",tendCode);
						result.setResult(map);
					}
					result.setSuccess(true);
				}else{
					result.setSuccess(false);
				}
			}
		} catch (Exception e) {
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 消息推送
	 * @param
     * @return
     */
	@RequestMapping(value="/postNoticeMsg", method={RequestMethod.POST},consumes="application/json")
	public @ResponseBody MessageResult postNoticeMsg(HttpServletRequest request,@RequestBody Map<String,Object> param) {
		MessageResult result = new MessageResult ();
		try {
            SecurityUserBeanInfo userBeanInfo = new SecurityUserBeanInfo ();
            String tendCode = (String)param.get ("tendCode");
            String loginName = (String)param.get ("loginName");
			userBeanInfo.setTendCode (tendCode);
			Map paramaterMap = new HashMap ();
			paramaterMap.put ("loginName",loginName);
			userBeanInfo = 	this.setUserBeanInfo (request,loginName,tendCode);
			String paramJson = JacksonUtils.toJson (param.get("param"));
			Map pramJsonMap = JacksonUtils.fromJson (paramJson,Map.class);
			String receiverLoginName = String.valueOf (pramJsonMap.get ("loginName"));
			if(userBeanInfo.getSecurityUserDto()==null){
				userBeanInfo = this.setUserBeanInfo(request,receiverLoginName,tendCode);
			}
			if(userBeanInfo.getSecurityUserDto ()!=null){
				// 获取用户角色
				Map getLoginNameMap = new HashMap ();
				getLoginNameMap.put ("loginNames",receiverLoginName);
				String usersResult = userOutServiceCustomer.getUserByUserLoginNames (JacksonUtils.toJson (userBeanInfo),JacksonUtils.toJson (getLoginNameMap));
				DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson (usersResult,DubboServiceResultInfo.class);
				List<UserDto> userDtos = null;
				if(dubboServiceResultInfo.isSucess ()){
					 userDtos = JacksonUtils.fromJson (dubboServiceResultInfo.getResult (),List.class,UserDto.class);
				}
				if(userDtos!=null&&!userDtos.isEmpty ()){
					pramJsonMap.put ("tendId",userBeanInfo.getTendId ());
					pramJsonMap.put("userId",userDtos.get (0).getId ());
					paramJson = JacksonUtils.toJson (pramJsonMap);
				}else{
					result.setSuccess (false);
					result.setMsg ("消息推送失败！,获取接收人信息失败！"+loginName);
					return result;
				}
			}else{
				result.setSuccess (false);
				result.setMsg ("消息推送失败！,获取发送人信息失败！"+loginName);
				return result;
			}
			String resultJson = sysNoticeMsgDtoServiceCustomer.save (JacksonUtils.toJson (userBeanInfo),paramJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson (resultJson,DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess ()){
				result.setSuccess (true);
				result.setMsg (dubboServiceResultInfo.getMsg ());
				result.setResult (dubboServiceResultInfo.getResult ());
			}else{
				result.setSuccess (false);
				result.setMsg (dubboServiceResultInfo.getMsg ());
			}
		}catch (Exception e){
			result.setSuccess (false);
			result.setMsg ("消息推送失败！");
		}
		return result;
	}

	/**
	 * 消息状态修改
	 * @param
	 * @return
	 */
	@RequestMapping(value="/updateNoticeMsg", method={RequestMethod.POST},consumes="application/json")
	public @ResponseBody MessageResult updateNoticeMsg(HttpServletRequest request,@RequestBody Map<String,Object> param) {
		MessageResult result = new MessageResult ();
		try {
            SecurityUserBeanInfo userBeanInfo = new SecurityUserBeanInfo ();
            String loginName = (String)param.get ("loginName");
			String tendCode = (String)param.get ("tendCode");
			String paramJson = JacksonUtils.toJson (param.get("param"));
            userBeanInfo = this.setUserBeanInfo (request,loginName,tendCode);
			String resultJson = sysNoticeMsgDtoServiceCustomer.updateStatusOfNoticeMsg (JacksonUtils.toJson (userBeanInfo),paramJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson (resultJson,DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess ()){
				result.setSuccess (true);
				result.setMsg (dubboServiceResultInfo.getMsg ());
				result.setResult (dubboServiceResultInfo.getResult ());
			}else{
				result.setSuccess (false);
				result.setMsg (dubboServiceResultInfo.getMsg ());
			}
		}catch (Exception e){
			result.setSuccess (false);
			result.setMsg ("消息状态修改失败！");
		}
		return result;
	}

	/**
	 * 消息信息查询
	 * @param
	 * @return
	 */
	@RequestMapping(value="/queryNoticeMsg", method={RequestMethod.POST},consumes="application/json")
	public @ResponseBody MessageResult queryNoticeMsg(@RequestBody Map<String,Object> param) {
		MessageResult result = new MessageResult ();
		try {
			String tendCode = (String)param.get ("tendCode");
			SecurityUserBeanInfo userBeanInfo = new SecurityUserBeanInfo ();
			userBeanInfo.setTendCode (tendCode);
			String paramJson = JacksonUtils.toJson (param.get("param"));
			String resultJson = sysNoticeMsgDtoServiceCustomer.queryNoticeMsg (JacksonUtils.toJson (userBeanInfo),paramJson);
			DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson (resultJson,DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess ()){
				result.setSuccess (dubboServiceResultInfo.isSucess ());
				result.setMsg (dubboServiceResultInfo.getMsg ());
				result.setResult (dubboServiceResultInfo.getResult ());
			}else{
				result.setSuccess (dubboServiceResultInfo.isSucess ());
				result.setMsg (dubboServiceResultInfo.getMsg ());
			}
		}catch (Exception e){
			e.printStackTrace ();
			result.setSuccess (false);
			result.setMsg ("消息查询失败！");
		}
		return result;
	}

	private SecurityUserBeanInfo setUserBeanInfo(HttpServletRequest request,String loginName,String tendCode)throws Exception{
        SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo ();
        securityUserBeanInfo.setTendCode (tendCode);
        String dubboResultInfo = null;
        DubboServiceResultInfo dubboServiceResultInfo = new DubboServiceResultInfo ();
        Map paramater = new HashMap ();
        paramater.put ("loginName",loginName);
        dubboResultInfo = authenticationDtoServiceCustomer.preCheck(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson (paramater));
        dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
        if(dubboServiceResultInfo.isSucess()){
            // 获取用户角色
            String resultInfo = dubboServiceResultInfo.getResult();
			UserDto user = JacksonUtils.fromJson(resultInfo, UserDto.class);
			SecurityUserDto securityUserDto = JacksonUtils.fromJson (resultInfo,SecurityUserDto.class);
            HttpSession session = request.getSession ();
            securityUserBeanInfo.setCookies("DTL_SESSION_ID="+session.getId ());
            securityUserBeanInfo.setSecurityUserDto(securityUserDto);
			securityUserBeanInfo.setTendId (user.getTendId ());
            // 获取用户相关授权信息
            long t2 = System.currentTimeMillis ();
            String authenticationInfodubboResultInfo = authenticationDtoServiceCustomer.getUserAuthenticationInfoWithoutResource(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(securityUserDto));
            long t3 = System.currentTimeMillis ();
            log.info (new Date()+"执行用户数据权限和菜单查询authenticationDtoServiceCustomer.getUserAuthenticationInfo(accessToken接口方法)耗时："+(t3-t2)+"毫秒");
            DubboServiceResultInfo authenticationInfodubboServiceResultInfo = JacksonUtils.fromJson(authenticationInfodubboResultInfo, DubboServiceResultInfo.class);
            if (authenticationInfodubboServiceResultInfo.isSucess()) {
                String authenticationInforesultInfo = authenticationInfodubboServiceResultInfo.getResult();
                AuthenticationDto authenticationDto = JacksonUtils.fromJson(authenticationInforesultInfo, AuthenticationDto.class);
                //获取用户标准岗位
                List<SecurityStandardRoleDto> securityStandardRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getStandardRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
                securityUserBeanInfo.setSecurityStandardRoleDtoList(securityStandardRoleDtoList);

                //获取用户通用角色
                List<SecurityStandardRoleDto> securityCurrencyRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getCurrencyRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
                securityUserBeanInfo.setSecurityCurrencyRoleDtoList(securityCurrencyRoleDtoList);
                //获取用户岗位
                List<SecurityPostDto> securityPostDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getPostDtoList()),ArrayList.class,SecurityPostDto.class);
                securityUserBeanInfo.setSecurityPostDtoList(securityPostDtoList);
                //当前用户所在组织的类型
                String securityOrganizationType = authenticationDto.getOrganizationType();
                securityUserBeanInfo.setSecurityOrganizationType(securityOrganizationType);
                //当前用户的一级公司
                SecurityOrganizationDto securityTopCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopCompanyDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityTopCompanyDto(securityTopCompanyDto);
                //当前用户的直属公司
                SecurityOrganizationDto securityDirectCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectCompanyDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityDirectCompanyDto(securityDirectCompanyDto);
                //当前用户的一级部门
                SecurityOrganizationDto securityTopDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopDeptDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityTopDeptDto(securityTopDeptDto);
                //当前用户的直属部门
                SecurityOrganizationDto securityDirectDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectDeptDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityDirectDeptDto(securityDirectDeptDto);
                //当前用户的项目
                SecurityOrganizationDto securityGroupDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getGroupDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityGroupDto(securityGroupDto);
                //当前用户的分期
                SecurityOrganizationDto securityBranchDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getBranchDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityBranchDto(securityBranchDto);
            }
            session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
    }
        return securityUserBeanInfo;
	}

    private SecurityUserBeanInfo setUserBeanInfo(Session session,String loginName,String tendCode)throws Exception{
        SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo ();
        securityUserBeanInfo.setTendCode (tendCode);
        String dubboResultInfo = null;
        DubboServiceResultInfo dubboServiceResultInfo = new DubboServiceResultInfo ();
        Map paramater = new HashMap ();
        paramater.put ("loginName",loginName);
        dubboResultInfo = authenticationDtoServiceCustomer.preCheck(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson (paramater));
        dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
        if(dubboServiceResultInfo.isSucess()){
            // 获取用户角色
            String resultInfo = dubboServiceResultInfo.getResult();
            UserDto user = JacksonUtils.fromJson(resultInfo, UserDto.class);
            SecurityUserDto securityUserDto = JacksonUtils.fromJson (resultInfo,SecurityUserDto.class);
            //HttpSession session = request.getSession ();
            securityUserBeanInfo.setCookies("DTL_SESSION_ID="+session.getId ());
            securityUserBeanInfo.setSecurityUserDto(securityUserDto);
            securityUserBeanInfo.setTendId (user.getTendId ());
            // 获取用户相关授权信息
            long t2 = System.currentTimeMillis ();
            String authenticationInfodubboResultInfo = authenticationDtoServiceCustomer.getUserAuthenticationInfoWithoutResource(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(securityUserDto));
            long t3 = System.currentTimeMillis ();
            log.info (new Date()+"执行用户数据权限和菜单查询authenticationDtoServiceCustomer.getUserAuthenticationInfo(accessToken接口方法)耗时："+(t3-t2)+"毫秒");
            DubboServiceResultInfo authenticationInfodubboServiceResultInfo = JacksonUtils.fromJson(authenticationInfodubboResultInfo, DubboServiceResultInfo.class);
            if (authenticationInfodubboServiceResultInfo.isSucess()) {
                String authenticationInforesultInfo = authenticationInfodubboServiceResultInfo.getResult();
                AuthenticationDto authenticationDto = JacksonUtils.fromJson(authenticationInforesultInfo, AuthenticationDto.class);
                //获取用户标准岗位
                List<SecurityStandardRoleDto> securityStandardRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getStandardRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
                securityUserBeanInfo.setSecurityStandardRoleDtoList(securityStandardRoleDtoList);

                //获取用户通用角色
                List<SecurityStandardRoleDto> securityCurrencyRoleDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getCurrencyRoleDtoList()),ArrayList.class,SecurityStandardRoleDto.class);
                securityUserBeanInfo.setSecurityCurrencyRoleDtoList(securityCurrencyRoleDtoList);
                //获取用户岗位
                List<SecurityPostDto> securityPostDtoList = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getPostDtoList()),ArrayList.class,SecurityPostDto.class);
                securityUserBeanInfo.setSecurityPostDtoList(securityPostDtoList);
                //当前用户所在组织的类型
                String securityOrganizationType = authenticationDto.getOrganizationType();
                securityUserBeanInfo.setSecurityOrganizationType(securityOrganizationType);
                //当前用户的一级公司
                SecurityOrganizationDto securityTopCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopCompanyDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityTopCompanyDto(securityTopCompanyDto);
                //当前用户的直属公司
                SecurityOrganizationDto securityDirectCompanyDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectCompanyDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityDirectCompanyDto(securityDirectCompanyDto);
                //当前用户的一级部门
                SecurityOrganizationDto securityTopDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getTopDeptDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityTopDeptDto(securityTopDeptDto);
                //当前用户的直属部门
                SecurityOrganizationDto securityDirectDeptDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getDirectDeptDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityDirectDeptDto(securityDirectDeptDto);
                //当前用户的项目
                SecurityOrganizationDto securityGroupDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getGroupDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityGroupDto(securityGroupDto);
                //当前用户的分期
                SecurityOrganizationDto securityBranchDto = JacksonUtils.fromJson(JacksonUtils.toJson(authenticationDto.getBranchDto()),SecurityOrganizationDto.class);
                securityUserBeanInfo.setSecurityBranchDto(securityBranchDto);
            }
            session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER,securityUserBeanInfo);
        }
        return securityUserBeanInfo;
    }


	/**
	 * 内部調用登陸
	 * @param
	 * @return
	 */
	@RequestMapping(value="/innerLogin", method={RequestMethod.POST},consumes="application/json")
	public @ResponseBody MessageResult innerLogin(HttpServletRequest request,@RequestBody Map<String,Object> param) {
		MessageResult result = new MessageResult ();
		try {

			String userInfoJson = (String)param.get ("userInfoJson");
			log.info ("模拟用户内部登录，用户信息："+userInfoJson);
			SecurityUserBeanInfo userBeanInfo = JacksonUtils.fromJson (userInfoJson,SecurityUserBeanInfo.class);
			HttpSession session = request.getSession ();
			userBeanInfo.setCookies ("DTL_SESSION_ID="+session.getId ());
			session.setAttribute (SecurityUserBeanInfo.TOKEN_TEND_USER,userBeanInfo);
			result.setSuccess (true);
			result.setMsg ("登录成功！");
			result.setResult (userBeanInfo.getCookies ());
			return result;
		}catch (Exception e){
			e.printStackTrace ();
			result.setSuccess (false);
			result.setMsg ("登录失败！");
		}
		return result;
	}
}

