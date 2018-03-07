package com.xinleju.platform.finance.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.finance.entity.VoucherTemplateEntry;

/**
 * @author admin
 * 
 * 
 */

public interface VoucherTemplateEntryService extends  BaseService <String,VoucherTemplateEntry>{

	public List queryListByTemplateIds(List<String> paramList);
}
