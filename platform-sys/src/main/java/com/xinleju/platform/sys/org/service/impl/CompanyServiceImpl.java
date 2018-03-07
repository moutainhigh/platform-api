package com.xinleju.platform.sys.org.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.org.dao.CompanyDao;
import com.xinleju.platform.sys.org.entity.Company;
import com.xinleju.platform.sys.org.service.CompanyService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class CompanyServiceImpl extends  BaseServiceImpl<String,Company> implements CompanyService{
	

	@Autowired
	private CompanyDao companyDao;

}
