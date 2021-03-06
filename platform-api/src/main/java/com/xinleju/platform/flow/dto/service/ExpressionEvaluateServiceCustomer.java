package com.xinleju.platform.flow.dto.service;

/**
 * 表达式相关服务
 * 
 * @author daoqi
 *
 */
public interface ExpressionEvaluateServiceCustomer {

	/**
	 * 表达式校验
	 * 
	 * @param userInfo
	 * @param expression
	 * @return
	 */
	public String validate(String userInfo, String expression);
}
