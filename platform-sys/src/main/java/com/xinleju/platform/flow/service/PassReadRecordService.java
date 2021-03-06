package com.xinleju.platform.flow.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.dto.InstanceTransitionRecordDto;
import com.xinleju.platform.flow.entity.PassReadRecord;

/**
 * @author admin
 * 
 * 
 */

public interface PassReadRecordService extends  BaseService <String,PassReadRecord>{

	List<PassReadRecord> queryPassReadList(String instanceId);

	
}
