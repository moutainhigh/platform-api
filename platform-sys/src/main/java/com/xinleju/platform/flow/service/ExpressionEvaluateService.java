package com.xinleju.platform.flow.service;

import com.xinleju.platform.flow.exception.FlowException;

public interface ExpressionEvaluateService {

	public boolean validate(String expression) throws FlowException;
}
