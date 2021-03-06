package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.AccountSetCompany;

/**
 * @author admin
 *
 */

public interface AccountSetCompanyDao extends BaseDao<String, AccountSetCompany> {

	/**
	 * @param map
	 * @return
	 */

	/**
	 * @param map
	 * @return
	 */
	public Integer getAccountSetCompanyTotal(Map map);

	/**
	 * @param map
	 * @return
	 */
	 public List<AccountSetCompany> getAccountSetCompanypage(Map map);
	
	

}
