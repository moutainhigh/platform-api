package com.xinleju.platform.sys.base.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.base.dto.BaseDataDictionaryDto;
import com.xinleju.platform.sys.base.entity.BaseDataDictionary;

/**
 * @author admin
 * 
 * 
 */

public interface BaseDataDictionaryService extends  BaseService <String,BaseDataDictionary>{

	public BaseDataDictionaryDto getBaseDataDictionaryAndItem(String id) throws Exception;

	public void saveBaseDataDictionaryAndItem(
			BaseDataDictionaryDto baseDataDictionaryDto)throws Exception;

	public int updateBaseDataDictionaryAndItem(
			BaseDataDictionaryDto baseDataDictionaryDto) throws Exception;

	public int deleteBaseDataDictionaryAndItem(String id) throws Exception;


	
}
