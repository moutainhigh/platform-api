package com.xinleju.platform.finance.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.entity.AccountSetCompany;

/**
 * @author admin
 * 
 * 
 */

public interface AccountSetCompanyService extends  BaseService <String,AccountSetCompany>{

	/**
	 * @param map
	 * @return
	 */
	public Page getaccountSetCompanypage(Map map) throws Exception;



	
}
