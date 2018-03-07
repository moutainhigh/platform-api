package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.VoucherTemplateEntry;

/**
 * @author admin
 *
 */

public interface VoucherTemplateEntryDao extends BaseDao<String, VoucherTemplateEntry> {

	List<Map<String, Object>> getMapListByTypeId(String paramater);

	void deleteObjectByTempId(String id);
	
	public List queryListByTemplateIds(List<String> paramList);

}
