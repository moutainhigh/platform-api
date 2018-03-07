package com.xinleju.platform.sys.org.dao.impl;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.UserTrialDao;
import com.xinleju.platform.sys.org.entity.User;
import com.xinleju.platform.sys.org.entity.UserTrial;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author luorx
 * 
 * 
 */

@Repository
public class UserTrialDaoImpl extends BaseDaoImpl<String,UserTrial> implements UserTrialDao {

	public UserTrialDaoImpl() {
		super();
	}

	@Override
	public UserTrial isExistTrialOrgUser(Map map) {
		return this.getSqlSession().selectOne("com.xinleju.platform.sys.org.entity.UserTrial.isExistOrgUserTrial",map);
	}

	@Override
	public User obtainOrgUserTrial(Map map) {
		return this.getSqlSession().selectOne("com.xinleju.platform.sys.org.entity.UserTrial.obtainOrgUserTrial",map);
	}
}
