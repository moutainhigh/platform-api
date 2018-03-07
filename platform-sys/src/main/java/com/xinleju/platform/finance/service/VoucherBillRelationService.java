package com.xinleju.platform.finance.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.entity.VoucherBillRelation;

/**
 * @author admin
 * 
 * 
 */

public interface VoucherBillRelationService extends  BaseService <String,VoucherBillRelation>{

	/**
	 * @param map
	 * @return
	 */
	public Page getVoucherBillRelationPage(Map map)throws Exception;
}
