package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.BusinessField;

/**
 * @author admin
 *
 */

public interface BusinessFieldDao extends BaseDao<String, BusinessField> {

	List<Map<String, Object>> getMapListByBillId(String billId);

	List<Map<String, Object>> queryTreeList(Map<String, Object> map);

}
