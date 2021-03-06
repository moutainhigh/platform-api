package com.xinleju.platform.flow.operation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.enumeration.TaskStatus;
import com.xinleju.platform.flow.model.ACUnit;
import com.xinleju.platform.flow.model.ApproverUnit;
import com.xinleju.platform.flow.model.InstanceUnit;
import com.xinleju.platform.flow.model.PostUnit;
import com.xinleju.platform.flow.model.TaskUnit;

/**
 * 跳过当前审批人
 * 
 * @author daoqi
 *
 */
public class SkipCurrentApproverOperation extends DefaultOperation implements Operation{

	public SkipCurrentApproverOperation() {
		super(OperationType.SKIPCURRENT);
	}

	/**
	 * 跳过当前审批人
	 */
	@Override
	protected void operate(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto) throws Exception {
		//查找点亮行
		List<ApproverUnit> currentApprovers = new ArrayList<ApproverUnit>();
		for(ACUnit acUnit : instanceUnit.getAcList()) {
			List<PostUnit> posts = acUnit.getPosts();
			if(CollectionUtils.isEmpty(posts)) {
				continue;
			}
			for(PostUnit postUnit : posts) {
				List<ApproverUnit> approvers = postUnit.getApprovers();
				if(CollectionUtils.isEmpty(approvers)) {
					continue;
				}
				for(ApproverUnit approverUnit : approvers) {
					TaskUnit task = approverUnit.getTask();
					if(task == null) {
						continue;
					}
					
					if(TaskStatus.RUNNING.getValue().equals(task.getTaskStatus())) {
						currentApprovers.add(approverUnit);
					}
				}
			}
		}
		
		for(ApproverUnit currentApprover : currentApprovers) {
			PostUnit currentPost = currentApprover.getOwner();
			ACUnit currentAc = currentPost.getOwner();
			setCurrentLocation(currentAc, currentPost, currentApprover);		//此时action中的设置无效！
			
			//撤回当前人的待办消息
			super.completeMessage(instanceUnit, currentApprover.getTask().getMsgId());
			
			//=2被跳过
			if(currentApprover.getDbAction() != 2) {
				next(instanceUnit, approvalDto);			//多人之间的跳过可能存在影响？？？
			}
		}
	}
}
