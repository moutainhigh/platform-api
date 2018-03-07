package com.xinleju.platform.flow.operation;

import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.model.InstanceUnit;
import com.xinleju.platform.flow.service.InstanceOperateLogService;

/**
 * 同意操作
 * 
 * @author daoqi
 *
 */
public class AgreeOperation extends DefaultOperation implements Operation {
	
	public AgreeOperation() {
		super(OperationType.AGREE);
	}

//	@Override
//	public String action(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto)
//			throws Exception {
//		
//		//下一步
//		next(instanceUnit, approvalDto);
//		
//		//4、保存DB
//		save(instanceUnit);
//		
//		//5、发送待办消息
//		handleMessages(instanceUnit, approvalDto);
//		
//		flowEnd(instanceUnit, approvalDto);
//		
//		return "success";
//	}

	@Override
	public void operate(InstanceUnit instanceUnit, 
			ApprovalSubmitDto approvalDto) throws Exception {

		//下一步
		next(instanceUnit, approvalDto);
		
		//添加将待办转为已办的操作日志@zhengjiajie 20170705--经测试，此处暂时不需要调用changeToDoIntoHaveDone()
		/*InstanceOperateLogService logService = service.getInstanceLogService();
		String instanceId = instanceUnit.getId();
		String acId = super.currentAc.getAcId();
		String groupId = null;//找不到对应的字段
		String taskId = approvalDto.getTaskId();
		String operatorIds = super.currentApprover.getApproverId();
		String approveType = super.currentAc.getApprovalTypeId();
		String operationType = approvalDto.getOperationType();
		String operateContent = "approveType:"+approveType+",operationType:"+operationType;
		String remark = approvalDto.getUserNote();
		System.out.println("\n\n001  instanceId="+instanceId+"; acId="+acId+"; taskId"+taskId);
		System.out.println("002  operatorIds="+operatorIds+"; operateContent="+operateContent+"; remark"+remark);
		System.out.println("003  logService.changeToDoIntoHaveDone() will be called...");
		logService.changeToDoIntoHaveDone(instanceId, acId, groupId, taskId, operatorIds, operateContent, remark);*/
	}

	@Override
	protected void customerHandle(InstanceUnit instanceUnit, ApprovalSubmitDto submitDto) throws Exception {
		//当校稿环节时，将附件相关信息通知业务系统(submitDto.approvalTypeId在设置当前位置时已赋值，不是从页面传回)
		submitDto.setApprovalTypeId(currentAc.getApprovalTypeId());
		if("JG".equals(submitDto.getApprovalTypeId())) {
			submitDto.setCategoryId(currentApprover.getId());	//页面上以用户的groupKey作为附件的categoryId
			super.noticeBusinessSystem(instanceUnit, submitDto);
		}

	}
	
}
