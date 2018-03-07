package com.xinleju.platform.flow.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.dto.ApproveTypeDto;
import com.xinleju.platform.flow.entity.ApproveType;

/**
 * @author admin
 * 
 * 
 */

public interface ApproveTypeService extends  BaseService <String,ApproveType>{

	/**
	 * 根据ApproveType的ID查询ApproveType的单个对象、与之相关联的Operation数据
	 * 主要是 operationTypeList 和  approvOperationList的数据
	 * @param userinfo
	 * @param id
	 * @return 返回字符串
	 */ 
	public ApproveTypeDto getApproveTypeAndOperationData(String approveTypeId);
	
	/**
	 * 根据ApproveType的ID去更新 name数据和 与之相关联的Operation数据
	 * 主要是  approvOperationList的数据
	 * @param userinfo
	 * @param id
	 * @return 返回字符串
	 */
	
	public int updateApproveTypeAndOperationData(ApproveTypeDto approveTypeDto) throws Exception;

	/**
	 * 重置基础类型的数据 (code=approve审批类型; code=operation 操作类型)
	 * @param t
	 * @return
	 */
	public int resetBasicTypeData(ApproveTypeDto approveTypeDto) throws Exception;
}
