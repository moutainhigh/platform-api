package com.xinleju.platform.flow.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.InstanceGroupDao;
import com.xinleju.platform.flow.entity.InstanceGroup;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class InstanceGroupDaoImpl extends BaseDaoImpl<String,InstanceGroup> implements InstanceGroupDao{

	public InstanceGroupDaoImpl() {
		super();
	}

	@Override
	public List<Map<String,Object>> queryListByInstanceId(Map<String, Object> paramMap) {
		return this.getSqlSession().selectList(InstanceGroup.class.getName()+".queryListByInstanceId",paramMap);
	}
}
