package com.xinleju.platform.sys.org.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.RootDao;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.entity.Root;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class RootDaoImpl extends BaseDaoImpl<String,Root> implements RootDao{

	public RootDaoImpl() {
		super();
	}

	/**
	 * 获取目录子节点目录
	 * @param parentId
	 * @return
	 */
	@Override
	public List<OrgnazationNodeDto> queryListRoot(Map<String,Object> map) throws Exception {
		List<OrgnazationNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Root.queryRootList", map);
		return list;
	}
	
	/**
	 * 获取所有目录节点
	 * @return
	 */
	@Override
	public List<OrgnazationNodeDto> queryAllRoot(Map<String,Object> map) throws Exception {
		List<OrgnazationNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.Root.queryAllRoot",map);
		return list;
	}

	
	
}
