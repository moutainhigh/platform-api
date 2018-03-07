package com.xinleju.platform.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.PcFormDao;
import com.xinleju.platform.flow.entity.PcForm;
import com.xinleju.platform.flow.service.PcFormService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class PcFormServiceImpl extends  BaseServiceImpl<String,PcForm> implements PcFormService{
	

	@Autowired
	private PcFormDao pcFormDao;
	

}
