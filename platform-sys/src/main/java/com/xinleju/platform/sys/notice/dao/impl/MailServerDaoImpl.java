package com.xinleju.platform.sys.notice.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.notice.dao.MailServerDao;
import com.xinleju.platform.sys.notice.entity.MailServer;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class MailServerDaoImpl extends BaseDaoImpl<String,MailServer> implements MailServerDao{

	public MailServerDaoImpl() {
		super();
	}

	
	/**
	 * 查询默认邮件服务器
	 * @return
	 */
	public MailServer getDefualt()throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.notice.entity.MailServer.getDefualt");
	}
	
	/**
	 * 校验code是否重复
	 * @param paramater
	 * @return
	 */
	public Integer checkCode(Map map)throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.notice.entity.MailServer.checkCode",map);
	}
	/**
	 * 校验code是否重复
	 * @param paramater
	 * @return
	 */
	public Integer unDefaultServer(Map map)throws Exception{
		return getSqlSession().update("com.xinleju.platform.sys.notice.entity.MailServer.unDefaultServer",map);
	}
	
	/**
	 * 模糊查询列表
	 * @param paramater
	 * @return
	 */
	public List<MailServer> queryListLike(Map map)throws Exception{
		return getSqlSession().selectList("com.xinleju.platform.sys.notice.entity.MailServer.queryListLike",map);
	}
}
