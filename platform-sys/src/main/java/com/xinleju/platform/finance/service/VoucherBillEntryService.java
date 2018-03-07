package com.xinleju.platform.finance.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.entity.VoucherBillEntry;

/**
 * @author admin
 * 
 * 
 */

public interface VoucherBillEntryService extends  BaseService <String,VoucherBillEntry>{
	/**
	 * @param map
	 * @return
	 */
	public Page getVoucherBillEntrypage(Map map)throws Exception;
	
}
