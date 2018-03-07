package com.xinleju.platform.univ.mq.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.univ.mq.dao.MessageDao;
import com.xinleju.platform.univ.mq.entity.Message;

/**
 * @author xubaoyong
 * 
 * 
 */

@Repository
public class MessageDaoImpl extends BaseDaoImpl<String,Message> implements MessageDao{

	public MessageDaoImpl() {
		super();
	}

	
	
}
