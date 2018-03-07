package com.xinleju.platform.sys.num.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.num.dao.RulerSubDao;
import com.xinleju.platform.sys.num.entity.RulerSub;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class RulerSubDaoImpl extends BaseDaoImpl<String,RulerSub> implements RulerSubDao{

	public RulerSubDaoImpl() {
		super();
	}

	@Override
	public RulerSub getCurrentByRulerId(String rulerId) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.num.entity.RulerSub.queryCurrentByRulerId", rulerId);
	}


	
	
}
