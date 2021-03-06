package com.xinleju.platform.finance.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.finance.dao.VoucherBillDao;
import com.xinleju.platform.finance.entity.VoucherBill;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class VoucherBillDaoImpl extends BaseDaoImpl<String,VoucherBill> implements VoucherBillDao{

	public VoucherBillDaoImpl() {
		super();
	}

	/**
	 * @param map
	 * @return
	 */
	public List<VoucherBill> getVoucherBillPage(Map map){
		return getSqlSession().selectList("com.xinleju.platform.finance.entity.VoucherBill.getVoucherBillList", map);
	}

	/**
	 * @param map
	 * @return
	 */
	public  Integer getVoucherBillListCount(Map map){
		return getSqlSession().selectOne("com.xinleju.platform.finance.entity.VoucherBill.getVoucherBillListCount", map);
	}
	
}
