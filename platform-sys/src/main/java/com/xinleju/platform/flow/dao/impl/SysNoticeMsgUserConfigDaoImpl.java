package com.xinleju.platform.flow.dao.impl;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.SysNoticeMsgUserConfigDao;
import com.xinleju.platform.flow.entity.SysNoticeMsgUserConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class SysNoticeMsgUserConfigDaoImpl extends BaseDaoImpl<String,SysNoticeMsgUserConfig> implements SysNoticeMsgUserConfigDao {

	public SysNoticeMsgUserConfigDaoImpl() {
		super();
	}

}
