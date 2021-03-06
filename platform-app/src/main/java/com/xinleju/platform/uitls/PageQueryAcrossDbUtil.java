package com.xinleju.platform.uitls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.flow.dto.SysNoticeMsgDto;
import com.xinleju.platform.flow.dto.service.SysNoticeMsgDtoServiceCustomer;
import com.xinleju.platform.out.app.org.service.UserOutServiceCustomer;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * 分页跨租户查询消息数据工具
 * 
 * @author daoqi
 *
 */
public class PageQueryAcrossDbUtil {
	
	private static Logger log = Logger.getLogger(PageQueryAcrossDbUtil.class);
	
	private static final String TOTAL_ALLL = "totalAlll";

	public static PageBeanInfo<SysNoticeMsgDto> getMsgPage(BaseDtoServiceCustomer dubboService, 
			UserOutServiceCustomer userOutServiceCustomer, List<String> tendCodes, SecurityUserBeanInfo userInfo,
			int start, int limit, String type,String sord) {
		SysNoticeMsgDtoServiceCustomer msgDubboService = (SysNoticeMsgDtoServiceCustomer)dubboService;
		Map<String, Object> params = generateParams(type, start, limit, userInfo);
		params.put("sord",sord);
		PageBeanInfo<SysNoticeMsgDto> page = new PageBeanInfo<SysNoticeMsgDto>();
		Map<String, Integer> totalMap = getTotal(msgDubboService, tendCodes, userInfo, params);
		page.setTotal(totalMap.get(TOTAL_ALLL));
		page.setStart(start);
		page.setLimit(limit);
		
		//计算每页面数据
		List<SysNoticeMsgDto> list = new ArrayList<SysNoticeMsgDto>();
		int L1 = 0;
		int L2 = 0;
		int startIndex = 0;
		int startIndexL1 = 0;
		int startIndexL2 = 0;
		
		int endIndex = 0;
		int endIndexL1 = 0;
		int endIndexL2 = 0;
		int max = start + limit;
		for(int i=0; i<tendCodes.size(); i++) {
			String tendCode = tendCodes.get(i);
			int total = totalMap.get(tendCode);
			L1 = L2;
			L2 = L2 + total;
			if(start >= L1 && start <= L2) {
				startIndex = i;
				startIndexL1 = L1;
				startIndexL2 = L2;
			}
			
			if(max >= L1 && max <= L2) {
				endIndex = i;
				endIndexL1 = L1;
				endIndexL2 = L2;
				break;
			}
		}
		log.info("跨租户范围：" + tendCodes);
		log.info("startIndex=" + startIndex);
		log.info("startIndexL1=" + startIndexL1);
		log.info("startIndexL2=" + startIndexL2);
		log.info("endIndex=" + endIndex);
		log.info("endIndexL1=" + endIndexL1);
		log.info("endIndexL2=" + endIndexL2);
//		int betweenCount = endIndex - startIndex;
//		if(betweenCount == 0) {
//			String tendCode = tendCodes.get(endIndex);
//			userInfo.setTendCode(tendCode);
//			int start0 = start - startIndexL1;
//			params.put("start", start0);
//			list.addAll(msgDubboService.queryMsgList(JacksonUtils.toJson(userInfo), params));
//			
//		} else if(betweenCount == 1) {
//			
//			
//		} else {
//			
//		}
		
		//left
		int start0 = start - startIndexL1;
		int limit0 = limit;
		String tendCode0 = tendCodes.get(startIndex);
		userInfo.setTendCode(tendCode0);
		params.put("start", start0);
		params.put("limit", limit0);
		List<SysNoticeMsgDto> msgList = msgDubboService.queryMsgList(JacksonUtils.toJson(userInfo), params);
		if(startIndex != 0) {
			addLoginInfoParam(userOutServiceCustomer, msgList, tendCode0, userInfo);			
		}
		list.addAll(msgList);
		
		//middle
		if(endIndex - startIndex > 1) {
			for(int i=startIndex + 1; i<endIndex; i++) {
				String tendCode = tendCodes.get(i);
				userInfo.setTendCode(tendCode);
				params.put("start", 0);
				params.put("limit", limit);
				msgList = msgDubboService.queryMsgList(JacksonUtils.toJson(userInfo), params);
				addLoginInfoParam(userOutServiceCustomer, msgList, tendCode, userInfo);
				list.addAll(msgList);
			}
		}
		
		//right
		if(endIndex - startIndex >= 1) {
			int start2 = 0;
			int limit2 = max - endIndexL1;
			String tendCode2 = tendCodes.get(endIndex);
			userInfo.setTendCode(tendCode2);
			params.put("start", start2);
			params.put("limit", limit2);
			msgList = msgDubboService.queryMsgList(JacksonUtils.toJson(userInfo), params);
			addLoginInfoParam(userOutServiceCustomer, msgList, tendCode2, userInfo);
			list.addAll(msgList);
		}

		page.setList(list);
		
		return page;
	}

	private static Map<String, Integer> getTotal(SysNoticeMsgDtoServiceCustomer msgDubboService, List<String> tendCodes,
			SecurityUserBeanInfo userInfo, Map<String, Object> params) {
		Map<String, Integer> totalMap = new HashMap<String, Integer>();
		int totalAll = 0;
		for(String tendCode : tendCodes) {
			userInfo.setTendCode(tendCode);
			int total = msgDubboService.queryMsgTotal(JacksonUtils.toJson(userInfo), params);
			totalMap.put(tendCode, total);
			totalAll = totalAll + total;
		}
		totalMap.put(TOTAL_ALLL, totalAll);
		
		return totalMap;
	}

	private static Map<String, Object> generateParams(String type, int start, int limit,
			SecurityUserBeanInfo userInfo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		//转义用户名中包含单引号的数据
		String username = userInfo.getSecurityUserDto().getLoginName().replaceAll("\'","\\\\'");
		//username.replaceAll("'","\'");
		params.put("loginName", username);
		/**
		 * 调用存储过程，opType参数设置
		 */
		if("DB".equals(type)) {
			List<String> opTypeList = new ArrayList<String>();
			opTypeList.add("DB");
			params.put("opType", opTypeList);
			params.put("procOpType","DB");
			
		} else if("DY".equals(type)) {
			List<String> opTypeList = new ArrayList<String>();
			opTypeList.add("DY");
			params.put("opType", opTypeList);
			params.put("procOpType","DY");
		} else if("YB".equals(type)) {
			List<String> opTypeList = new ArrayList<String>();
			opTypeList.add("YB");
			opTypeList.add("YY");
			params.put("opType", opTypeList);
			params.put("procOpType","YB,YY");
			params.put("orderByField","deal_date");
			
		} else if("FQ".equals(type)) {
			List<String> opTypeList = new ArrayList<String>();
			opTypeList.add("FQ");
			params.put("opType", opTypeList);
			params.put("procOpType","FQ");
		}
		return params;
	}
	
	public static PageBeanInfo<SysNoticeMsgDto> getMsgMorePage(BaseDtoServiceCustomer dubboService, UserOutServiceCustomer userOutServiceCustomer, 
			List<String> tendCodes, SecurityUserBeanInfo userInfo, Map<String, Object> params) {
		SysNoticeMsgDtoServiceCustomer msgDubboService = (SysNoticeMsgDtoServiceCustomer)dubboService;
		PageBeanInfo<SysNoticeMsgDto> page = new PageBeanInfo<SysNoticeMsgDto>();
		Map<String, Integer> totalMap = getTotalMore(msgDubboService, tendCodes, userInfo, params);
		page.setTotal(totalMap.get(TOTAL_ALLL));
		
		int start = (int) params.get("start");
		int limit = (int) params.get("limit");
		page.setStart(start);
		page.setLimit(limit);
		
		//计算每页面数据
		List<SysNoticeMsgDto> list = new ArrayList<SysNoticeMsgDto>();
		int L1 = 0;
		int L2 = 0;
		int startIndex = 0;
		int startIndexL1 = 0;
		int startIndexL2 = 0;
		
		int endIndex = 0;
		int endIndexL1 = 0;
		int endIndexL2 = 0;
		int max = start + limit;
		for(int i=0; i<tendCodes.size(); i++) {
			String tendCode = tendCodes.get(i);
			int total = totalMap.get(tendCode);
			L1 = L2;
			L2 = L2 + total;
			if(start >= L1 && start <= L2) {
				startIndex = i;
				startIndexL1 = L1;
				startIndexL2 = L2;
			}
			
//			if(max >= L1 && max <= L2) {
			if(max >= L1) {
				endIndex = i;
				endIndexL1 = L1;
				endIndexL2 = L2;
//				break;
			}
		}
		
		//left
		int start0 = start - startIndexL1;
		int limit0 = limit;
		String tendCode0 = tendCodes.get(startIndex);
		userInfo.setTendCode(tendCode0);
		params.put("start", start0);
		params.put("limit", limit0);
		List<SysNoticeMsgDto> msgList = msgDubboService.queryMsgMoreList(JacksonUtils.toJson(userInfo), params);
		getBussniessLock(msgList,userInfo,msgDubboService);
		if(startIndex != 0) {
			addLoginInfoParam(userOutServiceCustomer, msgList, tendCode0, userInfo);			
		}
		list.addAll(msgList);
		
		//middle
		if(endIndex - startIndex > 1) {
			for(int i=startIndex + 1; i<endIndex; i++) {
				String tendCode = tendCodes.get(i);
				userInfo.setTendCode(tendCode);
				params.put("start", 0);
				params.put("limit", limit);
				msgList = msgDubboService.queryMsgMoreList(JacksonUtils.toJson(userInfo), params);
				getBussniessLock(msgList,userInfo,msgDubboService);
				addLoginInfoParam(userOutServiceCustomer, msgList, tendCode, userInfo);
				list.addAll(msgList);
			}
		}
		
		//right
		if(endIndex - startIndex >= 1) {
			int start2 = 0;
			int limit2 = max - endIndexL1;
			String tendCode2 = tendCodes.get(endIndex);
			userInfo.setTendCode(tendCode2);
			params.put("start", start2);
			params.put("limit", limit2);
			msgList = msgDubboService.queryMsgMoreList(JacksonUtils.toJson(userInfo), params);
			getBussniessLock(msgList,userInfo,msgDubboService);
			addLoginInfoParam(userOutServiceCustomer, msgList, tendCode2, userInfo);
			list.addAll(msgList);
		}

		page.setList(list);
		
		return page;
	}
	//查询业务对象锁 begin
	public static  void getBussniessLock(List<SysNoticeMsgDto> msgList, SecurityUserBeanInfo userInfo,SysNoticeMsgDtoServiceCustomer msgDubboService){
//		Map<String,Object> msgMaps = new HashMap<>();
		Map<String,Object> msgInsMaps = new HashMap<>();
		Map<String,Object> newMap = new HashMap<>();
		List<String> instanceIds = new ArrayList<>();
		String instanceId = null;
		String msgId = null;
		for (int i = 0; i <msgList.size() ; i++) {
			SysNoticeMsgDto msg = msgList.get(i);
			msgId = msg.getId();
//			msgMaps.put(msgId,msg);
			String paramJSON = msg.getMobibleParam();
			if(StringUtils.isNotBlank(paramJSON)){
				Map<String,Object> paramMap = JacksonUtils.fromJson(paramJSON, HashMap.class);
				if(paramMap.get("instanceId")!=null){
					instanceId = (String)paramMap.get("instanceId");
					instanceIds.add(instanceId);
					msgInsMaps.put(msgId,instanceId);
				}
			}

		}
		if(instanceIds.size()>0){
			newMap.put("instanceIds",instanceIds);
			//查询业务对象
			List<SysNoticeMsgDto> bussniessLocks = msgDubboService.getMsgBussniessObjects(JacksonUtils.toJson(userInfo), JacksonUtils.toJson(newMap));
			String instId = null;
			Boolean ifMobile = null;
			Map<String,Object> insLockMaps = new HashMap<>();
			for (int i = 0; i <bussniessLocks.size() ; i++) {
				SysNoticeMsgDto b = bussniessLocks.get(i);
				instId = b.getInstanceId();
				ifMobile = b.getIfBussnissObjectLock();
				insLockMaps.put(instId,ifMobile);
			}
			for (int i = 0; i <msgList.size() ; i++) {
				SysNoticeMsgDto msg = msgList.get(i);
				msgId = msg.getId();
				if(msgInsMaps.get(msgId)!=null){
					String instId_ = (String)msgInsMaps.get(msgId);
					if(insLockMaps.get(instId_)!=null){
						/**
						 * 控制移动端是否能打开
						 * 1.如果消息上锁的状态是不能打开，则不能打开
						 * 2.如果消息上锁的状态是可以打开，查询业务对象上锁的状态 
						 */
						if(msg.getIfBussnissObjectLock()!=null){
							if(msg.getIfBussnissObjectLock()==false){
								continue;
							}else{
								msg.setIfBussnissObjectLock(Boolean.valueOf(insLockMaps.get(instId_).toString()));
							}
						}else{
							msg.setIfBussnissObjectLock(Boolean.valueOf(insLockMaps.get(instId_).toString()));
						}
					}
				}
			}
		}
		//end
	}
	//多租户查询待办待阅数量
	public static List<SysNoticeMsgDto> getMsgMoreSumData(BaseDtoServiceCustomer dubboService, UserOutServiceCustomer userOutServiceCustomer, 
			List<String> tendCodes, SecurityUserBeanInfo userInfo, Map<String, Object> params) {
		SysNoticeMsgDtoServiceCustomer msgDubboService = (SysNoticeMsgDtoServiceCustomer)dubboService;
		SysNoticeMsgDto res = new SysNoticeMsgDto();
		for(String tendCode : tendCodes) {
			userInfo.setTendCode(tendCode);
			String dubboResultInfo=msgDubboService.queryTwoSumData(JacksonUtils.toJson(userInfo),JacksonUtils.toJson(params));
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<SysNoticeMsgDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,SysNoticeMsgDto.class);
				if(list!=null && list.size()>0 && list.get(0)!=null){
					SysNoticeMsgDto msgDto=list.get(0);
					int toDoSum=StringUtils.isBlank(res.getToDoSum())?0:Integer.valueOf(res.getToDoSum());
					int toReadSum=StringUtils.isBlank(res.getToReadSum())?0:Integer.valueOf(res.getToReadSum());
					int toDoSum_=StringUtils.isBlank(msgDto.getToDoSum())?0:Integer.valueOf(msgDto.getToDoSum());
					int toReadSum_=StringUtils.isBlank(msgDto.getToReadSum())?0:Integer.valueOf(msgDto.getToReadSum());
					res.setToDoSum((toDoSum+toDoSum_)+"");
					res.setToReadSum((toReadSum+toReadSum_)+"");
				}
			}
		}
		List<SysNoticeMsgDto> list=new ArrayList<SysNoticeMsgDto>();
		list.add(res);
		return list;
	}
	
	//多租户查询待办待阅数量
	public static SysNoticeMsgDto getMsgMoreTotalData(BaseDtoServiceCustomer dubboService, UserOutServiceCustomer userOutServiceCustomer, 
			List<String> tendCodes, SecurityUserBeanInfo userInfo, Map<String, Object> params) {
		SysNoticeMsgDtoServiceCustomer msgDubboService = (SysNoticeMsgDtoServiceCustomer)dubboService;
		SysNoticeMsgDto res = new SysNoticeMsgDto();
		for(String tendCode : tendCodes) {
			userInfo.setTendCode(tendCode);
			String dubboResultInfo=msgDubboService.queryTotalStatData(JacksonUtils.toJson(userInfo),JacksonUtils.toJson(params));
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				SysNoticeMsgDto msgDto=JacksonUtils.fromJson(resultInfo,SysNoticeMsgDto.class);
				if(msgDto!=null){
					int toDoSum=StringUtils.isBlank(res.getToDoSum())?0:Integer.valueOf(res.getToDoSum());
					int toReadSum=StringUtils.isBlank(res.getToReadSum())?0:Integer.valueOf(res.getToReadSum());
					int toReadSum24=StringUtils.isBlank(res.getToDoSum24Hours())?0:Integer.valueOf(res.getToDoSum24Hours());
					int toDoSum_=StringUtils.isBlank(msgDto.getToDoSum())?0:Integer.valueOf(msgDto.getToDoSum());
					int toReadSum_=StringUtils.isBlank(msgDto.getToReadSum())?0:Integer.valueOf(msgDto.getToReadSum());
					int toReadSum24_=StringUtils.isBlank(msgDto.getToDoSum24Hours())?0:Integer.valueOf(msgDto.getToDoSum24Hours());
					res.setToDoSum((toDoSum+toDoSum_)+"");
					res.setToReadSum((toReadSum+toReadSum_)+"");
					res.setToDoSum24Hours((toReadSum24+toReadSum24_)+"");
				}
			}
		}
		return res;
	}
	
	private static Map<String, Integer> getTotalMore(SysNoticeMsgDtoServiceCustomer msgDubboService, List<String> tendCodes,
			SecurityUserBeanInfo userInfo, Map<String, Object> params) {
		Map<String, Integer> totalMap = new HashMap<String, Integer>();
		int totalAll = 0;
		for(String tendCode : tendCodes) {
			userInfo.setTendCode(tendCode);
			int total = msgDubboService.queryMsgMoreTotal(JacksonUtils.toJson(userInfo), params);
			totalMap.put(tendCode, total);
			totalAll = totalAll + total;
		}
		totalMap.put(TOTAL_ALLL, totalAll);
		
		return totalMap;
	}
	
	/**
	 * 给从租户中查询的消息增加登陆信息
	 * 
	 * @param msgList
	 */
	private static void addLoginInfoParam(UserOutServiceCustomer userOutServiceCustomer, 
			List<SysNoticeMsgDto> msgList, String tendCode, SecurityUserBeanInfo userInfo) {
		if(CollectionUtils.isNotEmpty(msgList)) {
			String loginName = userInfo.getSecurityUserDto().getLoginName();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("loginNames", loginName);
			String result = userOutServiceCustomer.getUserByUserLoginNames(JacksonUtils.toJson(userInfo), 
					JacksonUtils.toJson(map));
			DubboServiceResultInfo serviceResultInfo = JacksonUtils.fromJson(result, DubboServiceResultInfo.class);
			String userId = null;
			if(serviceResultInfo.isSucess()) {
				List<UserDto> userList = JacksonUtils.fromJson(serviceResultInfo.getResult(), 
						ArrayList.class, UserDto.class);
				userId = userList.get(0).getId();
			}
			log.info("跨租户查询消息：用户" + loginName + "在租户" + tendCode + "中的userId=" + userId);
			
			for(SysNoticeMsgDto msg : msgList) {
				String url = msg.getUrl() + "&tendCode=" + tendCode + "&uid=" + userId;
				String m_url = msg.getMobibleUrl() + "&tendCode=" + tendCode + "&uid=" + userId;
				msg.setUrl(url);
				msg.setMobibleUrl(m_url);
				
			}
		}
	}
}
