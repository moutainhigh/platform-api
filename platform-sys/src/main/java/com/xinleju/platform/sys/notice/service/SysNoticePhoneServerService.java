package com.xinleju.platform.sys.notice.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.notice.entity.SysNoticePhoneServer;

/**
 * @author admin
 * 
 * 
 */

public interface SysNoticePhoneServerService extends  BaseService <String,SysNoticePhoneServer>{

	int updateSetDefault(SysNoticePhoneServer serverBean)throws Exception;

	Page queryVaguePage(Map<String, Object> map)throws Exception;

	void updateDisableAllData()throws Exception;

	
}
