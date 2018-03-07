package com.xinleju.platform.sys.base.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface CustomArchivesDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * 获取业务对象树
	 * 
	 * @param parentId
	 * @return
	 */
	String getTree(String userInfo, String paramaterJson);

	/**
	  * @Description:档案批量保存
	  * @author:zhangfangzhi
	  * @date 2017年3月17日 上午8:57:46
	  * @version V1.0
	 */
	String saveList(String userInfo, String saveJson);

	/**
	  * @Description:查询该档案下是否有子节点
	  * @author:zhangfangzhi
	  * @date 2017年3月29日 下午3:25:28
	  * @version V1.0
	 */
	String queryItemsById(String userInfo, String idJson);

	/**
	  * @Description:删除档案和档案子项
	  * @author:zhangfangzhi
	  * @date 2017年5月2日 下午6:17:58
	  * @version V1.0
	 */
	String deletePseudoAndChildById(String json, String string);

	/**
	  * @Description:档案准备数据导入
	  * @author:zhangfangzhi
	  * @date 2017年6月2日 上午9:10:32
	  * @version V1.0
	 */
	String saveImportData(String json, String saveJson);

}
