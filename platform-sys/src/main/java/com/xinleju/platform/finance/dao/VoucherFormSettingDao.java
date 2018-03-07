package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.VoucherFormSetting;

/**
 * @author admin
 *
 */

public interface VoucherFormSettingDao extends BaseDao<String, VoucherFormSetting> {
	
	/**
	 * @param map
	 * @return
	 */
	public List<VoucherFormSetting> getFormSettingList(Map map);

	/**
	 * @param map
	 * @return
	 */
	public  Integer getFormSettingListCount(Map map);

}
