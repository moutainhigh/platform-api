package com.xinleju.platform.sys.notice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.notice.dao.MailMsgHistoryDao;
import com.xinleju.platform.sys.notice.entity.MailMsgHistory;
import com.xinleju.platform.sys.notice.service.MailMsgHistoryService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class MailMsgHistoryServiceImpl extends  BaseServiceImpl<String,MailMsgHistory> implements MailMsgHistoryService{
	

	@Autowired
	private MailMsgHistoryDao mailMsgHistoryDao;
	

}
