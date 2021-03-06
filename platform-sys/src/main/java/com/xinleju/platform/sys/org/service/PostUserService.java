package com.xinleju.platform.sys.org.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.dto.AcDto;
import com.xinleju.platform.sys.org.dto.FlowAcPostDto;
import com.xinleju.platform.sys.org.entity.PostUser;
import com.xinleju.platform.sys.org.vo.OrgnazationPostUserVo;

/**
 * @author admin
 * 
 * 
 */

public interface PostUserService extends  BaseService <String,PostUser>{

	/**
	 * 批量保存post_user和role_user
	 * @param objectList
	 */
	public Integer savePostUserAndRoleUser(List<Map<String,Object>> list) throws Exception;
	/**
	 * 删除岗位用户
	 * @param userInfo
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Integer delPostUserAndRoleUser(String userId) throws Exception;
	
	/**
	 * 设置主岗
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer setDefaultPost(Map<String,Object> map) throws Exception;
	
	
	
	
	/**
	 * 
	 * 获取流程岗位 
	 * @param businesInfo
	 * @param flowAcInfo
	 * @return
	 * @throws Exception
	 */
	public List<FlowAcPostDto> getFlowPostData(Map<String,Object> businesInfo,List<AcDto> flowAcInfo)  throws Exception;
	
	//批量保存post_user和role_user
	public Integer savePostUserBatch(Map<String,Object> param)throws Exception;
	//制定人员算人新规则
	public OrgnazationPostUserVo getUserPostNew(Map<String,Object> map)throws Exception;
}
