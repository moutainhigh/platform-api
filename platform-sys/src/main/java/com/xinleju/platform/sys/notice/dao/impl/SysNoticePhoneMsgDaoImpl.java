package com.xinleju.platform.sys.notice.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.notice.dao.SysNoticePhoneMsgDao;
import com.xinleju.platform.sys.notice.entity.SysNoticePhoneMsg;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class SysNoticePhoneMsgDaoImpl extends BaseDaoImpl<String,SysNoticePhoneMsg> implements SysNoticePhoneMsgDao{

	public SysNoticePhoneMsgDaoImpl() {
		super();
	}

	@Override
	public List<Map<String, Object>> getPageData(Map<String, Object> map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.notice.entity.SysNoticePhoneMsg.getPageData", map);
	}

	@Override
	public Integer getPageDataCount(Map<String, Object> map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.notice.entity.SysNoticePhoneMsg.getPageDataCount", map);
	}

	@Override
	public Integer updateStatus(Map<String, Object> map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.notice.entity.SysNoticePhoneMsg.updateStatus", map);
	}
	
	
}
