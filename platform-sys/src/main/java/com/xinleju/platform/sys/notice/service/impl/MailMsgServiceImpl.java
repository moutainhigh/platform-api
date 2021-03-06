package com.xinleju.platform.sys.notice.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.notice.dao.MailMsgDao;
import com.xinleju.platform.sys.notice.dao.MailServerDao;
import com.xinleju.platform.sys.notice.entity.MailMsg;
import com.xinleju.platform.sys.notice.entity.MailServer;
import com.xinleju.platform.sys.notice.service.MailMsgService;
import com.xinleju.platform.sys.notice.util.EmailManager;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;

/**
 * @author admin
 * 
 * 
 */

@Service
public class MailMsgServiceImpl extends  BaseServiceImpl<String,MailMsg> implements MailMsgService{
	

	@Autowired
	private MailMsgDao mailMsgDao;
	@Autowired
	private MailServerDao mailServerDao;
	
	/**
	 * 发送邮件并修改消息状态
	 * @return
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Map<String,Object> sendMailAndUpdateStatus(MailMsg mailMsg,String serverId)  throws Exception{
		MailServer server=null;
		//如果没有要使用的服务器，查询默认邮件服务器
		if(serverId==null){
			//获取默认邮件服务器
			server=mailServerDao.getDefualt();
		}else{
			server=mailServerDao.getObjectById(serverId);
		}
		if(server==null){
			throw new InvalidCustomException("没有可使用邮件服务器");
		}
		//发送邮件
		Map<String,Object> mailRes=EmailManager.sendMail(server, mailMsg);
		if ((Boolean)mailRes.get("flag")) {
			//修改消息状态为已发送
			mailMsg=getObjectById(mailMsg.getId());
			Integer count=mailMsg.getNum();
			count=count==null?1:(count+1);//发送测试+1
			mailMsg.setNum(count);
			mailMsg.setStatus("1");
			mailMsg.setMailServerId(server.getId());
			mailMsgDao.update(mailMsg);
		}else{
			mailRes.put("resMsg", "请检查服务器配置或邮箱格式并规范邮件内容");
		}
		return mailRes;
	}
	
	/**
	 * 模糊查询-分页
	 * @return
	 */
	public Page vaguePage(Map<String, Object> map)throws Exception{
		Page page=new Page();
		List<MailMsg> list = mailMsgDao.getPageData(map);
		Integer count = mailMsgDao.getPageDataCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		return page;
	}

}
