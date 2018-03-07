package com.xinleju.platform.flow.operation.concurrent;

import com.xinleju.platform.flow.model.PostUnit;

/**
 * 并行策略
 * 
 * @author daoqi
 *
 */
public class TogetherStrategy implements ConcurrentStrategy {

	@Override
	public int calculateLeftCnt(PostUnit currentPost) {
		int currentCnt = currentPost.getLeftPerson();
		return  currentCnt - 1;
	}

	@Override
	public int calculateLeftCnt(int currentCnt) {
		return  currentCnt - 1;
	}
}
