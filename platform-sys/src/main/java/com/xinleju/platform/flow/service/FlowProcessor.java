package com.xinleju.platform.flow.service;

import java.util.List;

import com.xinleju.platform.flow.model.ACUnit;

public interface FlowProcessor {

	public boolean change(String userInfo, String fiId, List<ACUnit> acUnitList);
}
