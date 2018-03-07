package com.xinleju.platform.sys.notice.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.notice.entity.SysNoticePhoneMsg;

/**
 * @author admin
 * 
 * 
 */

public interface SysNoticePhoneMsgService extends  BaseService <String,SysNoticePhoneMsg>{

	Page queryVaguePage(Map<String, Object> map)throws Exception;

	String saveSendMsg(Map<String, Object> map)throws Exception;

	String saveSendMsgTest(Map<String, Object> map)throws Exception ;

	String updateAgainSendMsg(Map<String, Object> map)throws Exception ;
	
}
