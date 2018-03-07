package com.xinleju.platform.flow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.PassReadRecordDao;
import com.xinleju.platform.flow.entity.PassReadRecord;
import com.xinleju.platform.flow.service.PassReadRecordService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class PassReadRecordServiceImpl extends  BaseServiceImpl<String,PassReadRecord> implements PassReadRecordService{
	

	@Autowired
	private PassReadRecordDao passReadRecordDao;

	@Override
	public List<PassReadRecord> queryPassReadList(String instanceId) {
		return passReadRecordDao.queryPassReadList(instanceId);
	}
	

}
