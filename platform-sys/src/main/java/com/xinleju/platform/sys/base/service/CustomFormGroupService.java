package com.xinleju.platform.sys.base.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.base.dto.CustomFormDto;
import com.xinleju.platform.sys.base.dto.CustomFormNodeDto;
import com.xinleju.platform.sys.base.entity.CustomFormGroup;

/**
 * @author admin
 * 
 * 
 */

public interface CustomFormGroupService extends  BaseService <String,CustomFormGroup>{

	/**
	  * @Description:查询对象树
	  * @author:zhangfangzhi
	  * @date 2017年3月8日 下午3:39:17
	  * @version V1.0
	 */
	List<CustomFormDto> getTree(Map<String, Object> map);

	/**
	  * @Description:查询最大序号
	  * @author:zhangfangzhi
	  * @date 2017年3月9日 上午9:36:34
	  * @version V1.0
	 */
	Integer queryMaxSort();

	/**
	  * @Description:查询自定义分类（排序）
	  * @author:zhangfangzhi
	  * @date 2017年3月9日 下午2:53:52
	  * @version V1.0
	 * @param map 
	 */
	List<CustomFormNodeDto> queryCustomGroupSort(Map map);

	/**
	  * @Description:查询自定义表单（排序）
	  * @author:zhangfangzhi
	  * @date 2017年3月9日 下午3:02:46
	  * @version V1.0
	 */
	List<CustomFormNodeDto> queryCustomSort();

	/**
	  * @Description:查询该分类下是否有表单
	  * @author:zhangfangzhi
	  * @date 2017年3月17日 下午5:39:19
	  * @version V1.0
	 */
	Integer getCustomFormCountByGroupId(String jsonString);

	/**
	  * @Description:表单分类保存并且校验
	  * @author:zhangfangzhi
	  * @date 2017年3月23日 下午5:40:49
	  * @version V1.0
	 */
	String validateBeforeSave(String userInfo, String saveJson);

	/**
	  * @Description:表单分类更新并且校验
	  * @author:zhangfangzhi
	  * @date 2017年3月23日 下午5:55:22
	  * @version V1.0
	 */
	String validateBeforeUpdate(String userInfo, String updateJson);

	/**
	  * @Description:删除表单分类并且同步删除快速入口分类
	  * @author:zhangfangzhi
	  * @date 2017年4月11日 下午1:43:18
	  * @version V1.0
	 * @param id 
	 */
	int deleteCustomGroupById(String userInfo, String id);

	/**
	  * @Description:表单分类生成数据 
	  * @author:zhangfangzhi
	  * @date 2017年6月14日 下午6:39:46
	  * @version V1.0
	 */
	String saveGenerateData(String userInfo, String saveJson);

	/**
	  * @Description:查询一级表单分类下是否存在二级分类
	  * @author:zhangfangzhi
	  * @date 2017年7月10日 下午7:55:22
	  * @version V1.0
	 */
	Integer getCustomFormGroupCountByPID(String id);

	/**
	  * @Description:快速入口查询接口
	  * @author:zhangfangzhi
	  * @date 2017年7月13日 下午7:54:15
	  * @version V1.0
	 */
	List queryListForQuickEntry(Map map);

	/**
	  * @Description:上移下移
	  * @author:zhangfangzhi
	  * @date 2017年8月4日 上午10:16:16
	  * @version V1.0
	 */
	int updateSort(CustomFormGroup object, Map<String, Object> map);

}
