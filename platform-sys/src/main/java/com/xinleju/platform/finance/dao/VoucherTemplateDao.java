package com.xinleju.platform.finance.dao;

import java.util.List;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.VoucherTemplate;

/**
 * @author admin
 *
 */

public interface VoucherTemplateDao extends BaseDao<String, VoucherTemplate> {
	
	public List queryListByTypeIds(List<String> paramList);

}
