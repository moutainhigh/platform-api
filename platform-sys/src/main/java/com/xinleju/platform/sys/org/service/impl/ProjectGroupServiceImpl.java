package com.xinleju.platform.sys.org.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.org.dao.ProjectGroupDao;
import com.xinleju.platform.sys.org.entity.ProjectGroup;
import com.xinleju.platform.sys.org.service.ProjectGroupService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class ProjectGroupServiceImpl extends  BaseServiceImpl<String,ProjectGroup> implements ProjectGroupService{
	

	@Autowired
	private ProjectGroupDao projectGroupDao;
	

}
