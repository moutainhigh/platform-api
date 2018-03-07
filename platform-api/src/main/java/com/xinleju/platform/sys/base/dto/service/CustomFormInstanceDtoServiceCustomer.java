package com.xinleju.platform.sys.base.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface CustomFormInstanceDtoServiceCustomer extends BaseDtoServiceCustomer{

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
	  * @date 2017年4月14日 上午9:24:26
	  * @version V1.0
	 */
	String getVariableById(String userInfo, String idJson);

	/**
	  * @Description:流程回调修改状态
	  * @author:zhangfangzhi
	  * @date 2017年4月20日 下午7:33:41
	  * @version V1.0
	 */
	String updateStatus(String userInfo, String updateJson);
	
	/**
	  * @Description:流程回调修改状态
	  * @author:zhangfangzhi
	  * @date 2017年4月20日 下午7:33:41
	  * @version V1.0
	 */
	String updateStatusForCustomForm(String userInfo, String updateJson);

	/**
	  * @Description:付款管理分页查询
	  * @author:zhangfangzhi
	  * @date 2017年11月6日 下午4:36:41
	  * @version V1.0
	 */
	String getFundPage(String json, String paramaterJson);

	/**
	  * @Description:导入资金平台
	  * @author:zhangfangzhi
	  * @date 2017年11月7日 下午2:26:02
	  * @version V1.0
	 */
	String importFundPayment(String json, String dataParam);

	/**
	  * @Description:资金平台同步
	  * @author:zhangfangzhi
	  * @date 2017年11月8日 下午2:13:40
	  * @version V1.0
	 */
	String synFund(String userInfo, String dataParam);

	/**
	  * @Description:自定义表单分页查询
	  * @author:zhangfangzhi
	  * @date 2017年11月6日 下午4:36:41
	  * @version V1.0
	 */
	String getPageCustom(String json, String paramaterJson);

	/**
	  * @Description:我的发起（手机端）
	  * @author:zhangfangzhi
	  * @date 2018年1月19日 下午7:12:16
	  * @version V1.0
	 */
	String getMyFormPage(String json, String paramaterJson);

}
