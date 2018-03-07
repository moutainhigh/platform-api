package com.xinleju.platform.sys.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.base.dao.BaseSupplierAccontDao;
import com.xinleju.platform.sys.base.entity.BaseSupplierAccont;
import com.xinleju.platform.sys.base.service.BaseSupplierAccontService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class BaseSupplierAccontServiceImpl extends  BaseServiceImpl<String,BaseSupplierAccont> implements BaseSupplierAccontService{
	

	@Autowired
	private BaseSupplierAccontDao baseSupplierAccontDao;

	@Override
	public List<Map<String,Object>> getSupplierAccontMapBySupplierId(String id) throws Exception {
		return baseSupplierAccontDao.getSupplierAccontMapBySupplierId(id);
	}
	@Override
	public List<BaseSupplierAccont> getSupplierAccontBySupplierId(String id) throws Exception {
		return baseSupplierAccontDao.getSupplierAccontBySupplierId(id);
	}
	

}
