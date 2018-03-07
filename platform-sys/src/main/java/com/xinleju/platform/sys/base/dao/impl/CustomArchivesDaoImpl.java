package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.CustomArchivesDao;
import com.xinleju.platform.sys.base.dto.CustomArchivesItemDto;
import com.xinleju.platform.sys.base.entity.CustomArchives;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class CustomArchivesDaoImpl extends BaseDaoImpl<String,CustomArchives> implements CustomArchivesDao{

	public CustomArchivesDaoImpl() {
		super();
	}

	@Override
	public List<CustomArchivesItemDto> getTree(Map<String, Object> paramater) {
		return getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomArchivesItem.getTree",paramater);
	}

	@Override
	public Integer queryItemsById(String mainId) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomArchivesItem.getItemCount",mainId);
	}

	@Override
	public List<CustomArchives> queryListSort(Map<String, Object> map) {
		return getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomArchives.queryListSort",map);
	}

	@Override
	public Integer isExistCode(String code) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomArchives.validateIsExistCode",code);
	}

	
	
}
