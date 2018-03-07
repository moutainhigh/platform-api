package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.entity.FlowUserOpinion;

/**
 * @author 
 *
 */

public interface FlowUserOpinionDao extends BaseDao<String, FlowUserOpinion> {
	
	
	/**
	 * 获取用户自定义意见
	 * @param param
	 * @return
	 */
	public List<FlowUserOpinion> queryUserOpinion(Map<String, Object> param) ;
	/**
	 * 删除用户自定义意见
	 * @param param
	 * @return
	 */
	public int delUserOpinion(Map<String, Object> param) ;
}
