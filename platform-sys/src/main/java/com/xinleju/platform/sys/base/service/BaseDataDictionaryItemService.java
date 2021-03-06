package com.xinleju.platform.sys.base.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.base.dto.BaseDataDictionaryItemDto;
import com.xinleju.platform.sys.base.entity.BaseDataDictionaryItem;

/**
 * @author admin
 * 
 * 
 */

public interface BaseDataDictionaryItemService extends  BaseService <String,BaseDataDictionaryItem>{

	public  List<BaseDataDictionaryItemDto> getItemListByDictionaryId(String id) throws Exception;

	
}
