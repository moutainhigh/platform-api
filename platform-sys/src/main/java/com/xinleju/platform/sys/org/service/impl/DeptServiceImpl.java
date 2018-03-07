package com.xinleju.platform.sys.org.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.org.dao.DeptDao;
import com.xinleju.platform.sys.org.entity.Dept;
import com.xinleju.platform.sys.org.service.DeptService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class DeptServiceImpl extends  BaseServiceImpl<String,Dept> implements DeptService{
	

	@Autowired
	private DeptDao deptDao;
	

}
