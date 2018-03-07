package com.xinleju.platform.sys.num.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.num.entity.FormVariable;

/**
 * @author ly
 * 
 * 
 */

public interface FormVariableService extends  BaseService <String,FormVariable>{

	public Page getFormVariableByPage(Map<String, Object> map)throws Exception;

	public void saveAllFormVariable(List<Map<String, Object>> formVariableList)throws Exception;
}
