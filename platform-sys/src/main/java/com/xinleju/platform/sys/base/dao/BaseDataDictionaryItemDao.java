package com.xinleju.platform.sys.base.dao;

import java.util.List;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.base.dto.BaseDataDictionaryItemDto;
import com.xinleju.platform.sys.base.entity.BaseDataDictionaryItem;

/**
 * @author admin
 *
 */

public interface BaseDataDictionaryItemDao extends BaseDao<String, BaseDataDictionaryItem> {

	public List<BaseDataDictionaryItemDto> getItemListByDictionaryId(String id);
	
	

}
