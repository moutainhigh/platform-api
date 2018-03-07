package com.xinleju.platform.sys.num.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.num.dao.BillDao;
import com.xinleju.platform.sys.num.entity.Bill;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BillDaoImpl extends BaseDaoImpl<String,Bill> implements BillDao{

	public BillDaoImpl() {
		super();
	}

	@Override
	public List<Map<String, Object>> getBillData(Map<String, Object> map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.num.entity.Bill.getBillData", map);
	}

	@Override
	public Integer getBillDataCount(Map<String, Object> map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.num.entity.Bill.getBillDataCount", map);
	}

	@Override
	public Integer getCountByCode(Map<String, Object> map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.num.entity.Bill.getCountByCode", map);
	}

	
	
}
