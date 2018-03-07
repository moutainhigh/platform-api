package com.xinleju.platform.sys.res.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.res.dao.DataCtrlDao;
import com.xinleju.platform.sys.res.dto.DataItemDto;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.entity.DataCtrl;
import com.xinleju.platform.sys.res.entity.DataItem;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class DataCtrlDaoImpl extends BaseDaoImpl<String,DataCtrl> implements DataCtrlDao{

	public DataCtrlDaoImpl() {
		super();
	}

	@Override
	public List<DataItemDto> querylistDataItem(String paramater) {
		List<DataItemDto> list = getSqlSession().selectList("com.xinleju.platform.sys.res.entity.DataItem.queryDataItemList", paramater);
		return list;
	}

	
	
}
