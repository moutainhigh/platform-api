package com.xinleju.platform.sys.base.dto.service;

import java.util.Map;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface CustomFormGroupDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	  * @Description:查询表单树
	  * @author:zhangfangzhi
	  * @date 2017年3月8日 下午3:33:21
	  * @version V1.0
	 */
	String getTree(String userInfo, String paramaterJson);

	/**
	  * @Description:查询该分类下是否有表单
	  * @author:zhangfangzhi
	  * @date 2017年3月17日 下午5:34:52
	  * @version V1.0
	 */
	String getCustomFormCountByGroupId(String username, String string);

	/**
	  * @Description:表单分类保存并且校验
	  * @author:zhangfangzhi
	  * @date 2017年3月23日 下午5:39:40
	  * @version V1.0
	 */
	String validateBeforeSave(String userInfo, String saveJson);

	/**
	  * @Description:表单分类更新并且校验
	  * @author:zhangfangzhi
	  * @date 2017年3月23日 下午5:54:08
	  * @version V1.0
	 */
	String validateBeforeUpdate(String userInfo, String updateJson);

	/**
	  * @Description:表单分类生成数据 
	  * @author:zhangfangzhi
	  * @date 2017年6月14日 下午6:39:06
	  * @version V1.0
	 */
	String saveGenerateData(String json, String saveJson);

	/**
	  * @Description:查询一级表单分类下是否存在二级分类
	  * @author:zhangfangzhi
	  * @date 2017年7月10日 下午7:53:08
	  * @version V1.0
	 */
	String getCustomFormGroupCountByPID(String json, String id);

	/**
	  * @Description:快速入口查询接口
	  * @author:zhangfangzhi
	  * @date 2017年7月13日 下午7:53:13
	  * @version V1.0
	 */
	String queryListForQuickEntry(String json, String paramaterJson);

	/**
	  * @Description:上移下移
	  * @author:zhangfangzhi
	  * @date 2017年8月4日 上午10:02:44
	  * @version V1.0
	 */
	String updateSort(String userJson, String string, Map<String, Object> map);

}
