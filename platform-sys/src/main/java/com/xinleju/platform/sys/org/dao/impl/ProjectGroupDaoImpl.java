package com.xinleju.platform.sys.org.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.ProjectGroupDao;
import com.xinleju.platform.sys.org.entity.ProjectGroup;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class ProjectGroupDaoImpl extends BaseDaoImpl<String,ProjectGroup> implements ProjectGroupDao{

	public ProjectGroupDaoImpl() {
		super();
	}

	
	
}
