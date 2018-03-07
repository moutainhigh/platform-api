package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.AccountCaption;

/**
 * @author admin
 *
 */

public interface AccountCaptionDao extends BaseDao<String, AccountCaption> {

	/**
	 * @return
	 */
	public List<AccountCaption> getAccountCaptionList(Map map);

	/**
	 * @return
	 */
	public List<String> getAccountCaptionParentIdsList(Map map);

	/**
	 * @param id
	 * @return
	 */
	public List<String> getAccountCaptionListById(String id);

	/**
	 * @return
	 */
	public List<AccountCaption> getAccountCaptionListByParentId(Map<String,Object>map);
	
	/**
	 * @return
	 */
	public List<String> queryByBudCodeList(Map map);

}
