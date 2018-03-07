package com.xinleju.platform.sys.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.base.dao.BaseSupplierCompanyDao;
import com.xinleju.platform.sys.base.entity.BaseSupplierCompany;
import com.xinleju.platform.sys.base.service.BaseSupplierCompanyService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class BaseSupplierCompanyServiceImpl extends  BaseServiceImpl<String,BaseSupplierCompany> implements BaseSupplierCompanyService{
	

	@Autowired
	private BaseSupplierCompanyDao baseSupplierCompanyDao;

	@Override
	public List queryCompanyList(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.service.BaseSupplierCompanyService#getObjectBySupplierId(java.lang.String)
	 */
	@Override
	public List<String> getObjectBySupplierId(String id) throws Exception {
		  return baseSupplierCompanyDao.getObjectBySupplierId(id);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.service.BaseSupplierCompanyService#getIdsBySupplierId(java.lang.String)
	 */
	@Override
	public List<String> getIdsBySupplierId(String id) throws Exception {
		return baseSupplierCompanyDao.getIdsBySupplierId(id);
	}
	

}
