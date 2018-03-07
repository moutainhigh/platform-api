package com.xinleju.platform.univ.task.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.univ.task.dao.TaskParametersDao;
import com.xinleju.platform.univ.task.entity.TaskParameters;
import com.xinleju.platform.univ.task.service.TaskParametersService;

/**
 * @author admin
 * 
 * 
 */
@Transactional
@Service
public class TaskParametersServiceImpl extends  BaseServiceImpl<String,TaskParameters> implements TaskParametersService{
	

	@Autowired
	private TaskParametersDao taskParametersDao;
	

}
