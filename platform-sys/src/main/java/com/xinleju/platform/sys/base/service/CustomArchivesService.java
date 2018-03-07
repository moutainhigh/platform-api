package com.xinleju.platform.sys.base.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.sys.base.dto.CustomArchivesItemDto;
import com.xinleju.platform.sys.base.entity.CustomArchives;

/**
 * @author admin
 * 
 * 
 */

public interface CustomArchivesService extends  BaseService <String,CustomArchives>{

	/**
	  * @Description:查询树
	  * @author:zhangfangzhi
	  * @date 2017年3月4日 下午1:50:18
	  * @version V1.0
	 */
	List<CustomArchivesItemDto> getTree(Map<String, Object> map);

	/**
	  * @Description:查询档案明细是否存在
	  * @author:zhangfangzhi
	  * @date 2017年3月6日 下午1:48:39
	  * @version V1.0
	 */
	Integer queryItemsById(String mainId);

	/**
	  * @Description:查询排序
	  * @author:zhangfangzhi
	  * @date 2017年3月17日 上午9:24:02
	  * @version V1.0
	 */
	List<CustomArchives> queryListSort(Map<String, Object> map);

	/**
	  * @Description:校验编码是否重复
	  * @author:zhangfangzhi
	  * @date 2017年3月21日 上午10:57:55
	  * @version V1.0
	 */
	Integer isExistCode(String code);
	
	/**
	  * @Description:档案批量保存
	  * @author:zhangfangzhi
	  * @date 2017年3月22日 上午9:44:19
	  * @version V1.0
	 */
	DubboServiceResultInfo saveList(String saveJson);

	/**
	  * @Description:查询单据分类树
	  * @author:zhangfangzhi
	  * @date 2017年3月22日 下午6:17:54
	  * @version V1.0
	 */
	DubboServiceResultInfo getTree(String paramater);

	
}
