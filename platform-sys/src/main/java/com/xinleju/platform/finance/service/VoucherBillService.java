package com.xinleju.platform.finance.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.entity.VoucherBill;
import com.xinleju.platform.finance.entity.VoucherBillEntry;

/**
 * @author admin
 * 
 * 
 */

public interface VoucherBillService extends  BaseService <String,VoucherBill>{
	/**
	 * @param map
	 * @return
	 */
	Page getVoucherBillPage(Map map)throws Exception;
	
	/**
	 * 校验是否为完整的凭证
	 * 校验规则：1.辅助核算没有匹配到   2：借贷金额不平
	 * @param voucher
	 * @param entryDataList
	 * @return
	 */
	Map isFull(VoucherBill voucher,List<VoucherBillEntry> entryDataList,String accountSetId);
}
