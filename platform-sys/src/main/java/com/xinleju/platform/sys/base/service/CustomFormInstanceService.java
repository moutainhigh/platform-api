package com.xinleju.platform.sys.base.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dto.CustomFormInstanceDto;
import com.xinleju.platform.sys.base.dto.PayRegistDTO;
import com.xinleju.platform.sys.base.entity.CustomFormInstance;

/**
 * @author admin
 * 
 * 
 */

public interface CustomFormInstanceService extends  BaseService <String,CustomFormInstance>{

	/**
	  * @Description:分页查询（排序）
	  * @author:zhangfangzhi
	  * @date 2017年3月30日 下午4:28:57
	  * @version V1.0
	 * @param userInfo 
	 */
	Page getPageForForm(String userInfo, Map<String, Object> paramater);

	/**
	  * @Description:获取实例对象（包括模板格式）
	  * @author:zhangfangzhi
	  * @date 2017年3月30日 下午4:26:51
	  * @version V1.0
	 */
	String getInstanceByFormId(String userInfo, String jsonStr);

	/**
	  * @Description:获取流程变量值
	  * @author:zhangfangzhi
	  * @date 2017年4月14日 上午9:25:58
	  * @version V1.0
	 */
	String getVariableById(String userInfo, String idJson);

	/**
	  * @Description:流程回调修改状态
	  * @author:zhangfangzhi
	  * @date 2017年4月20日 下午7:35:55
	  * @version V1.0
	 */
	int updateStatus(CustomFormInstance customFormInstance);

	/**
	  * @Description:流程回调修改状态
	  * @author:zhangfangzhi
	  * @date 2017年4月27日 下午4:27:26
	  * @version V1.0
	 */
	int updateStatusForCustomForm(String updateJson);

	/**
	  * @Description:付款管理分页查询
	  * @author:zhangfangzhi
	  * @date 2017年11月6日 下午4:37:55
	  * @version V1.0
	 */
	Page getFundPageForForm(String userInfo, Map map);

	/**
	  * @Description:导入资金平台
	  * @author:zhangfangzhi
	  * @date 2017年11月7日 下午2:28:54
	  * @version V1.0
	 */
	DubboServiceResultInfo importFundPayment(List<String> list);

	/**
	  * @Description:资金平台同步
	  * @author:zhangfangzhi
	  * @date 2017年11月8日 下午2:16:35
	  * @version V1.0
	 */
	void synFund(Map map);

	/**
	  * @Description:更新单据
	  * @author:zhangfangzhi
	  * @date 2017年12月19日 下午3:23:08
	  * @version V1.0
	 */
	int updateInstance(CustomFormInstanceDto customFormInstanceDto);

	/**
	  * @Description:判断是否是自定义表单
	  * @author:zhangfangzhi
	  * @date 2018年1月17日 上午10:43:00
	  * @version V1.0
	 */
	String isCustomformInstance(String id);

	/**
	  * @Description:我的发起（手机端）
	  * @author:zhangfangzhi
	  * @date 2018年1月19日 下午7:13:39
	  * @version V1.0
	 */
	Page getMyFormPage(String userInfo, Map map);

	/**
	  * @Description:保存实例
	  * @author:zhangfangzhi
	  * @date 2018年2月5日 下午1:49:42
	  * @version V1.0
	 */
	void saveInstance(CustomFormInstance customFormInstance);

	/**
	  * @Description:删除单据同时删除草稿
	  * @author:zhangfangzhi
	  * @date 2018年2月5日 下午5:56:48
	  * @version V1.0
	 */
	int deleteByIds(List<String> list);

	
}
