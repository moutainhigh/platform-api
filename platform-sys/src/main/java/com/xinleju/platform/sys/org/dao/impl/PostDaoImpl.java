package com.xinleju.platform.sys.org.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.PostDao;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.dto.PostDto;
import com.xinleju.platform.sys.org.dto.PostQueryDto;
import com.xinleju.platform.sys.org.entity.Post;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class PostDaoImpl extends BaseDaoImpl<String,Post> implements PostDao{

	public PostDaoImpl() {
		super();
	}

	@Override
	public List<PostQueryDto> queryPostListByUserId(Map<String, Object> paramater)  throws Exception{
		String userId = "";
		if (paramater != null) {
			Set<String> set = paramater.keySet();
			for (String key : set) {
				if(key.equals("userId")){
					userId = (String) paramater.get(key);
				}
				
			}
		}
		List<PostQueryDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Post.queryPostListByUserId", userId);
		return list;
	}
	
	@Override
	public List<PostDto> queryAuthPostListByUserId(Map<String, Object> paramater)  throws Exception{
		List<PostDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Post.queryAuthPostListByUserId", paramater);
		return list;
	}
	
	@Override
	public List<Map<String,Object>> queryPostRoleListByUserId(Map<String, Object> paramater)  throws Exception{
		/*String userId = "";
		if (paramater != null) {
			Set<String> set = paramater.keySet();
			for (String key : set) {
				if(key.equals("userId")){
					userId = (String) paramater.get(key);
				}
				
			}
		}*/
		List<Map<String,Object>> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Post.queryPostRoleListByUserId", paramater);
		return list;
	}
	
	/**
	 * 根据组织结构查询岗位列表
	 * 
	 * @param paramater
	 * @return
	 */
	@Override
	public List<PostQueryDto> queryPostListByOrgId(Map<String, Object> paramater) throws Exception {
		/*String orgId = "";
		if (paramater != null) {
			Set<String> set = paramater.keySet();
			for (String key : set) {
				if(key.equals("orgId")){
					orgId = (String) paramater.get(key);
				}
				
			}
		}*/
		return getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Post.queryPostListByOrgId", paramater);
	}
	
	/**
	 * 根据角色查询岗位列表
	 * 
	 * @param paramater
	 * @return
	 */
	@Override
	public List<PostQueryDto> queryPostListByRoleId(Map<String, Object> paramater) throws Exception {
		/*String roleId = "";
		if (paramater != null) {
			Set<String> set = paramater.keySet();
			for (String key : set) {
				if(key.equals("roleId")){
					roleId = (String) paramater.get(key);
				}
				
			}
		}*/
		return getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Post.queryPostListByRoleId", paramater);
	}
	
	@Override
	public List<OrgnazationNodeDto> queryAllPostList(Map<String, Object> map) throws Exception {
		List<OrgnazationNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Post.queryAllPostList",map);
		return list;
		
	}
	@Override
	public List<Map<String,String>> queryPostsByIds(Map map)throws Exception{
		List<Map<String, String>> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Post.queryPostsByIds", map);
		return list;
	}
	
	/**
	 * 查询用户主岗组织
	 * @param paramater
	 * @return
	 */
	@Override
	public String getDefaultPostOrg(Map map)throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.org.entity.Post.getDefaultPostOrg",map);
	}
	@Override
	public List<PostDto> selectPostDtoListByOrgId(Map map)throws Exception{
		return getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Post.selectPostDtoListByOrgId",map);
	}
	@Override
	public List<PostDto> selectPostDtoListByUserId(Map map)throws Exception{
		return getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Post.selectPostDtoListByUserId",map);
	}
	
	/**
	 * 批量设置领导岗位
	 */
	@Override
	public Integer updateBatchLeaderId(Map map)throws Exception{
		return getSqlSession().update("com.xinleju.platform.sys.org.entity.Post.updateBatchLeaderId",map);
	}
	//查询组织及其下级是否存在岗位
	@Override
	public Integer selectSonRefCount(Map map) throws Exception {
		return getSqlSession().selectOne("com.xinleju.platform.sys.org.entity.Post.selectSonRefCount",map);
	}
}
