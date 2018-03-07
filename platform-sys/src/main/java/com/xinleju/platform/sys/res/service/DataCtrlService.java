package com.xinleju.platform.sys.res.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.res.dto.DataCtrlDto;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.entity.DataCtrl;
import com.xinleju.platform.sys.res.entity.DataItem;

/**
 * @author admin
 * 
 * 
 */

public interface DataCtrlService extends  BaseService <String,DataCtrl>{
	/**
	 * 根据数据控制对象id获取所有数据对象控制项集合
	 * @param  paramater(数据控制对象id)
	 */
	public List<DataNodeDto> queryListDataItem(String id)  throws Exception;

	public  DataCtrlDto getDataPointAll(String id) throws Exception;

	
}
