package com.xinleju.platform.sys.notice.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.notice.entity.SysNoticePhoneServer;

/**
 * @author admin
 *
 */

public interface SysNoticePhoneServerDao extends BaseDao<String, SysNoticePhoneServer> {

	List<Map<String, Object>> getPageData(Map<String, Object> map);

	Integer getPageDataCount(Map<String, Object> map);

	void disableAllData(Map<String, Object> map);

	SysNoticePhoneServer getObjectByDefault(Map<String, Object> map);
	
	

}
