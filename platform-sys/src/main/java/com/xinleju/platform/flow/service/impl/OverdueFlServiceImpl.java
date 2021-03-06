package com.xinleju.platform.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.OverdueFlDao;
import com.xinleju.platform.flow.entity.OverdueFl;
import com.xinleju.platform.flow.service.OverdueFlService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class OverdueFlServiceImpl extends  BaseServiceImpl<String,OverdueFl> implements OverdueFlService{
	

	@Autowired
	private OverdueFlDao overdueFlDao;
	

}
