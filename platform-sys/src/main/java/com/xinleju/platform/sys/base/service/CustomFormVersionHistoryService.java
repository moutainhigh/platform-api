package com.xinleju.platform.sys.base.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.entity.CustomFormVersionHistory;

/**
 * @author admin
 * 
 * 
 */

public interface CustomFormVersionHistoryService extends  BaseService <String,CustomFormVersionHistory>{

	/**
	  * @Description:分页查询（排序）
	  * @author:zhangfangzhi
	  * @date 2017年11月23日 下午1:55:04
	  * @version V1.0
	 */
	Page getPageSort(String userInfo, Map map);

	
}
