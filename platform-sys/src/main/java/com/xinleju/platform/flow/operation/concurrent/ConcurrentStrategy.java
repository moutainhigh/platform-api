package com.xinleju.platform.flow.operation.concurrent;

import com.xinleju.platform.flow.model.PostUnit;

/**
 * 岗位、人员并发策略
 * 
 * @author daoqi
 *
 */
public interface ConcurrentStrategy {

	/**
	 * 计算环节或岗位中剩余的未处理数
	 * 
	 * @param currentCnt：当前未处理数
	 * @return
	 */
	public int calculateLeftCnt(PostUnit currentPost);
	
	/**
	 * 计算环节或岗位中剩余的未处理数
	 * 
	 * @param currentCnt：当前未处理数
	 * @return
	 */
	public int calculateLeftCnt(int currentCnt);
}
