package com.xinleju.platform.sys.res.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface DataPermissionDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * 保存数据授权
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String saveDataAuth(String userInfo, String paramater)throws Exception;

	/**
	 * 保存数据授权（角色到数据）
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String saveDataAuthRoleToData(String userInfo, String paramater)throws Exception;

	/**
	 * 保存数据授权（数据到角色）
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String saveDataAuthDataToRole(String userInfo, String paramater)throws Exception;
	
	/**
	 * 保存数据授权（对象到数据（控制点数据以及指定数据val））
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String saveBatchDataToObjectBytypeAndItemId(String userInfo, String paramater)throws Exception;

	/**
	 * 查询数据授权（根据控制项Id和角色Ids）
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String queryAuthDataByitemIdAndroleIds(String userInfo, String paramer)throws Exception;
	
	/**
	 * 根据（控制项Id和控制点Id或者指定数据ID（类型判断如果类型是dataPoint是控制点ID））查询已授权数据
	 * @param map
	 * @return
	 */
	public String queryAuthDataByitemIdAndPointId(String userInfo, String paramer)throws Exception;

	/**
	 * 保存引入数据授权
	 * @param map
	 * @return
	 */
	public String saveBatchDataImport(String userInfo, String paramater) throws Exception;
}
