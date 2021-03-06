package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.dto.AssTypeMappingDto;
import com.xinleju.platform.finance.entity.AssType;
import com.xinleju.platform.finance.entity.SysRegister;

/**
 * @author admin
 *
 */

public interface AssTypeDao extends BaseDao<String, AssType> {
	
	public List<AssTypeMappingDto> queryTree(List<String> list);

}
