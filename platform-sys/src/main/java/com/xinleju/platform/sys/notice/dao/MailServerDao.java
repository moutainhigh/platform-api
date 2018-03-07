package com.xinleju.platform.sys.notice.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.notice.entity.MailMsg;
import com.xinleju.platform.sys.notice.entity.MailServer;

/**
 * @author admin
 *
 */

public interface MailServerDao extends BaseDao<String, MailServer> {
	
	/**
	 * 查询默认邮件服务器
	 * @return
	 */
	public MailServer getDefualt()  throws Exception;
	/**
	 * 校验code是否重复
	 * @param paramater
	 * @return
	 */
	public Integer checkCode(Map map)throws Exception;
	/**
	 * 校验code是否重复
	 */
	public Integer unDefaultServer(Map map)throws Exception;
	
	/**
	 * 模糊查询列表
	 * @param paramater
	 * @return
	 */
	public List<MailServer> queryListLike(Map map)throws Exception;
}
