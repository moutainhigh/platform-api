package com.xinleju.platform.finance.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.finance.dao.AssMappingDao;
import com.xinleju.platform.finance.dto.AssTypeMappingDto;
import com.xinleju.platform.finance.entity.AssMapping;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class AssMappingDaoImpl extends BaseDaoImpl<String,AssMapping> implements AssMappingDao{

	public AssMappingDaoImpl() {
		super();
	}

	@Override
	public void deleteByTypeId(String par) {
		getSqlSession().update("com.xinleju.platform.finance.entity.AssMapping.deleteByTypeId", par);
	}
	
	@Override
	public List<AssTypeMappingDto> queryListByAssTypeIds(List<String> list){
		return getSqlSession().selectList("com.xinleju.platform.finance.entity.AssMapping.queryListByAssTypeIds", list);
	}
}
