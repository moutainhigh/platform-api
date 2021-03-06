package com.xinleju.platform.sys.res.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.res.dao.OperationDao;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.dto.OperationDto;
import com.xinleju.platform.sys.res.entity.Operation;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class OperationDaoImpl extends BaseDaoImpl<String,Operation> implements OperationDao{

	public OperationDaoImpl() {
		super();
	}

	/**
	 * 根据资源id获取功能操作点(查询菜单下的一级按钮)
	 * @param map(资源ID)
	 * @return
	 */
	@Override
	public List<DataNodeDto> queryOperationListRootByResourceId(Map<String, Object> paramater)  throws Exception{
		String resourceId = "";
		if (paramater != null) {
			Set<String> set = paramater.keySet();
			for (String key : set) {
				if(key.equals("resourceId")){
					resourceId = (String) paramater.get(key);
				}
				
			}
		}
		List<DataNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Operation.queryOperationListRootByResourceId", resourceId);
		return list;
	}
	
	/**
	 * 根据资源id获取所有按钮
	 * @param map(资源ID)
	 * @return
	 */
	@Override
	public List<DataNodeDto> queryOperationListAllByResourceId(Map<String, Object> paramater)  throws Exception{
		String resourceId = "";
		if (paramater != null) {
			Set<String> set = paramater.keySet();
			for (String key : set) {
				if(key.equals("resourceId")){
					resourceId = (String) paramater.get(key);
				}
				
			}
		}
		List<DataNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Operation.queryOperationListAllByResourceId", resourceId);
		return list;
	}
	
	
	@Override
	public List<OperationDto> queryListByCondition(Map<String, Object> map) throws Exception{
		return getSqlSession().selectList("com.xinleju.platform.sys.res.entity.Operation.queryListByCondition", map);
	}
	
	/**
	 * 校验编码重复
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getCodeCount(Map<String, Object> map) throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.Operation.getCodeCount", map);
	}
	/**
	 * 判断菜单是否存在下级按钮
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer selectSonNum(Map<String, Object> map) throws Exception{
		return getSqlSession().selectOne("com.xinleju.platform.sys.res.entity.Operation.selectSonNum", map);
	}
}
