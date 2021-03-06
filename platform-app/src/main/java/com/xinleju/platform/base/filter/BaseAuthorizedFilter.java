package com.xinleju.platform.base.filter;

import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.sys.security.dto.service.AuthenticationDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.MultiSessionUrlEncode;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


/**
 * @author yzp
 * 权限认证，管理
 * 
 *
 */
public class BaseAuthorizedFilter implements Filter {
    private static Logger log = Logger.getLogger(BaseAuthorizedFilter.class);
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	private List<String> noAuthorizedUrls=new ArrayList<String>();
	private String loginUrl="";
	
     protected RedisTemplate<String, String> redisTemplate;

	private  AuthenticationDtoServiceCustomer authenticationDtoServiceCustomer;
	protected RedisOperationsSessionRepository sessionRepository;


//	@Autowired
//	JedisCluster jedisCluster;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chian) throws IOException, ServletException {


		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();

		// 为了防止ftp协议
		if (session == null) {
			chian.doFilter(request, response);
		}
		
		// 请求地址
		String url = httpRequest.getRequestURI() ; //参数  ;

		long doFilter01 = 0L;
		if(url.indexOf ("/sys/thirdPartyAuthentication/accessToken")>-1){
			doFilter01 = System.currentTimeMillis ();
			log.info (new Date()+" "+url+"进入过滤器doFilter(accessToken接口方法)----"+doFilter01);
		}

		/*外部系统待办穿透*/
		if(url.indexOf("flow/runtime/approve/flow.html")>-1||url.indexOf("mobile/approve/approve_detail.html")>-1){
			String tendCode = httpRequest.getParameter("tend_code");
			String loginName = httpRequest.getParameter("loginName");
			if(StringUtils.isNotBlank(tendCode)&&StringUtils.isNotBlank(loginName)){
					this.setUserBeanInfo(httpRequest,loginName,tendCode);
			}
		}
		//处理流程审批页面的分流：微信、Android、iphone
		if(url.indexOf("flowhub") != -1) {
			String pcUrl = httpRequest.getParameter("pcUrl");
			String mobileUrl = httpRequest.getParameter("mobileUrl");
			String requestHeader = httpRequest.getHeader("user-agent");
			log.info("处理流程审批页面的分流: pcUrl=" + pcUrl + ", mobileUrl=" + mobileUrl + ", source=" + requestHeader);
			if(isMobileDevice(requestHeader)) {
				httpResponse.sendRedirect(mobileUrl);
				
			} else {
				httpResponse.sendRedirect(pcUrl);
			}
			return ;
		}
		
		// 第三方请求，构建session
		if (url.indexOf("/sys/thirdPartyAuthentication/redirect") != -1) {
			String token = httpRequest.getParameter("token");
			session.setAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER, JacksonUtils.fromJson(redisTemplate.opsForValue().get(SecurityUserBeanInfo.TOKEN_TEND_USER + token), SecurityUserBeanInfo.class));
			session.setAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU, JacksonUtils.fromJson(redisTemplate.opsForValue().get(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token), SecurityUserBeanRelationInfo.class));
		}

		SecurityUserBeanInfo securityUserBeanInfo = (SecurityUserBeanInfo) session.getAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER);
		SecurityUserBeanRelationInfo securityUserBeanRelationInfo = (SecurityUserBeanRelationInfo) session.getAttribute(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU);
		//招采调用
		if(url.indexOf("/sys/org/user/queryUserAndPostsByUname")>-1 && StringUtils.isNotBlank(httpRequest.getParameter("sessionId"))){
			String sessionId = httpRequest.getParameter("sessionId");
			ExpiringSession session_ = sessionRepository.getSession(sessionId );
			securityUserBeanInfo = (SecurityUserBeanInfo) session_.getAttribute(SecurityUserBeanInfo.TOKEN_TEND_USER);
		}
		log.info("url=" + url + "当前用户信息：" + securityUserBeanInfo);
		
		if (url.indexOf(".js") > 0 || url.indexOf(".css") > 0 || url.indexOf(".image") > 0 || url.indexOf(".png") > 0) {
			chian.doFilter(request, response);
		}
		
		PathMatcher matcher = new AntPathMatcher();
		// **/sys/authentication/login ///platform-app/sys/authentication/login
		boolean flag = false;
		for (String noAuthorizedUrl : noAuthorizedUrls) {
			flag = matcher.match(noAuthorizedUrl.trim(), url.trim());
			if (flag) {
				break;
			}

		}
		if (flag) {
			if(url.indexOf("/sys/thirdPartyAuthentication/accessToken")>-1){
				long entryTime = System.currentTimeMillis ();
				log.info (new Date()+"filter进入(accessToken接口方法)方法---"+(entryTime-doFilter01));
			}
			chian.doFilter(request, response);
		} else {
			boolean logFlag = false;
			String note = null;
			String urlName = null;
			String sysCode=null;
			if (securityUserBeanInfo != null) {
				// chian.doFilter(request, response);
				// TODO 校验权限
				// 已注册的url清单
				if(StringUtils.isNotBlank(httpRequest.getQueryString())){
					url = httpRequest.getRequestURI()  + "?" + (httpRequest.getQueryString()); //参数  ;
				}
				if(securityUserBeanRelationInfo==null){
					chian.doFilter(request, response);
					return;
				}
				//访问url
				List<SecurityResourceDto> list = securityUserBeanRelationInfo.getResourceDtoList();
				boolean isUrl = false;// 是否注册
				if(list!=null){
					for (SecurityResourceDto srDto : list) {
						// 已注册url，需要校验是否授权
						if (StringUtils.isNotBlank(srDto.getResourceUrl()) && matcher.match("/**/" + srDto.getResourceUrl().trim() + "**/**", url.trim())) {
							isUrl = true;
							if (srDto.getIsAuth().equals("1")) {
								// 已授权
								chian.doFilter(request, response);
								note = "访问成功!";
								sysCode=srDto.getAppId();
								logFlag = true;
							} else {
								// 未授权
								Date d = new Date();
								//add by dgh on 09/29 跨租户消息问题，暂时注释掉
								String redirectUrl = "nopower.html";
								redirectUrl = MultiSessionUrlEncode.encodeUrl(httpRequest,redirectUrl);
								httpResponse.sendRedirect(redirectUrl);
								//httpResponse.sendRedirect(httpRequest.getContextPath() + "/nopower.html?time=" + d.getTime());
								note = "访问失败，未授权!";
								sysCode=srDto.getAppId();
								logFlag = true;
							}
							urlName = srDto.getPrefixName();
							break;
						}
						continue;
					}
				}
				if (!isUrl) {
					chian.doFilter(request, response);
				}
			} else {
				Date d = new Date();
				//add by dgh on 09/29 跨租户消息问题，暂时注释掉
				String redirectUrl = loginUrl;
				redirectUrl = MultiSessionUrlEncode.encodeUrl(httpRequest,redirectUrl);
				httpResponse.sendRedirect(redirectUrl);
				///httpResponse.sendRedirect(httpRequest.getContextPath() + "/" + loginUrl + "?time=" + d.getTime());

				note = "访问失败，未登录!";
				logFlag = true;
				// return ;
			}
			if (logFlag) {
				// 记录访问日志
				try {

					Map<String, Object> param = new HashMap<String, Object>();
					param.put("loginIp", (getIpAddress((HttpServletRequest) request)));
//					param.put("loginIp", ((HttpServletRequest) request).getRemoteAddr());
					param.put("loginBrowser", ((HttpServletRequest) request).getHeader("User-Agent"));
					if (urlName != null) {
						param.put("node", urlName);
					} else {
						param.put("node", url);
					}
					param.put("note", note);
					param.put("sysCode", sysCode);
					AuthenticationDtoServiceCustomer authenticationDtoServiceCustomer = (AuthenticationDtoServiceCustomer) SpringContextUtil.getBean("authenticationDtoServiceCustomer");
					authenticationDtoServiceCustomer.menuLog(JacksonUtils.toJson(securityUserBeanInfo), JacksonUtils.toJson(param));
				} catch (Exception e) {
					//// e.printStackTrace();
				}
			}

		}

	}
	/** 
	 * 获取对象的IP地址等信息 
	 * @author X-rapido 
	 * 
	 */  
    public  String getIpAddress(HttpServletRequest request) {  
        String ip = request.getHeader("x-forwarded-for");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_REAL_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
        	ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
	      
	private boolean isMobileDevice(String requestHeader) {
		// TODO Auto-generated method stub
        String[] deviceArray = new String[]{"Android","mac os","windows phone", "iPhone"};
        if(requestHeader == null)
            return false;
        requestHeader = requestHeader.toLowerCase();
        for(int i=0;i<deviceArray.length;i++){
            if(requestHeader.indexOf(deviceArray[i])>0){
                return true;
            }
        }
		return false;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		String noAuthorized=config.getInitParameter("noAuthorized");
		loginUrl=config.getInitParameter("loginUrl");
		if(StringUtils.isNotBlank(noAuthorized)){
			String[] noAuthorizeds=noAuthorized.split(",");
			noAuthorizedUrls=Arrays.asList(noAuthorizeds);
			
		}
		redisTemplate = (RedisTemplate<String, String>) SpringContextUtil.getBean("redisTemplate");
		sessionRepository = (RedisOperationsSessionRepository) SpringContextUtil.getBean("sessionRepository");
		authenticationDtoServiceCustomer = (AuthenticationDtoServiceCustomer)SpringContextUtil.getBean("authenticationDtoServiceCustomer");
	}
	
	
	public static void main(String[] args){
		  PathMatcher matcher = new AntPathMatcher();
		 //	 [/**/login.html, /**/sys/authentication/login, /**/sys/authentication/logout]
		//  [ERROR][2017-08-11 15:12:42,349][BaseAuthorizedFilter:122] 地址请求拦截 ***/platform-app/finance/financeSystem/financesystem_list.html======================###/platform-app/finance/financeSystem/financesystem_list.html?_t=1502435561244
		   boolean result1 =matcher.match("/platform-app/finance/financeSystem/financesystem_list.html**/**","/platform-app/finance/financeSystem/bb.html/11/11/11?_t=1502435561244");
		   System.out.println(result1);
	}
	private SecurityUserBeanInfo setUserBeanInfo(HttpServletRequest request,String loginName,String tendCode){
		SecurityUserBeanInfo securityUserBeanInfo = new SecurityUserBeanInfo ();
		try{
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
			}catch (Exception e){

			}
		return securityUserBeanInfo;

	}

}
