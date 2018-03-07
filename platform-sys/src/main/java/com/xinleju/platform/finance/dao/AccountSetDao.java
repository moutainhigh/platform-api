package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.AccountSet;
import com.xinleju.platform.finance.entity.SysRegister;

/**
 * @author admin
 *
 */

public interface AccountSetDao extends BaseDao<String, AccountSet> {
	
	/**
	 * @param map
	 * @return
	 */
	public List<SysRegister> getpageList(Map map);

	/**
	 * @param map
	 * @return
	 */
	public  Integer getpageListCount(Map map);

}
