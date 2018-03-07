package com.xinleju.platform.flow.operation.concurrent;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.xinleju.platform.flow.enumeration.AutoPassType;
import com.xinleju.platform.flow.enumeration.TaskStatus;
import com.xinleju.platform.flow.model.ApproverUnit;
import com.xinleju.platform.flow.model.PostUnit;

/**
 * 抢占策略
 * 
 * @author daoqi
 *
 */
public class CompetitionStrategy implements ConcurrentStrategy {

	@Override
	public int calculateLeftCnt(PostUnit currentPost) {
		int leftCount = 0;
		List<ApproverUnit> approvers = currentPost.getApprovers();
		if(CollectionUtils.isNotEmpty(approvers)) {
			for(ApproverUnit approver : approvers) {
				if(TaskStatus.RUNNING.getValue().equals(approver.getTask().getTaskStatus())
						&& approver.getAutoPass() == AutoPassType.NOT_PASS.getValue()) {
					leftCount++;
				}
			}
		}
		return  leftCount;
	}

	@Override
	public int calculateLeftCnt(int currentCnt) {
		return  0;
	}

}
