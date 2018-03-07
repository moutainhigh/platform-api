package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.CustomFormDao;
import com.xinleju.platform.sys.base.dto.GeneralPaymentDTO;
import com.xinleju.platform.sys.base.entity.CustomForm;
import com.xinleju.platform.sys.base.entity.CustomFormVersionHistory;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class CustomFormDaoImpl extends BaseDaoImpl<String,CustomForm> implements CustomFormDao{

	public CustomFormDaoImpl() {
		super();
	}

	@Override
	public Integer queryMaxSort(String parentId) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomForm.getMaxSort",parentId);
	}

	@Override
	public Integer validateIsExist(CustomForm customForm,String type) {
		if("code".equals(type)){
			return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomForm.validateIsExistCode",customForm);
		}else if("name".equals(type)){
			return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomForm.validateIsExistName",customForm);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getPageSort(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomForm.getPageSort", map);
	}

	@Override
	public Integer getPageSortCount(Map map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomForm.getPageSortCount", map);
	}

	@Override
	public Integer isHasInstance(String id) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomForm.isHasInstance",id);
	}

	@Override
	public List queryListForQuickEntry(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomForm.queryListForQuickEntry", map);
	}
	@Override
	public List queryListForUpdateSort(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomForm.queryListForUpdateSort", map);
	}

	@Override
	public void updateSort(CustomForm customForm) {
		getSqlSession().update("com.xinleju.platform.sys.base.entity.CustomForm.updateSort", customForm);
	}

	@Override
	public CustomFormVersionHistory queryCustomFormVersionHistoryMax(String id) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomForm.queryCustomFormVersionHistoryMax",id);
	}

	@Override
	public List<GeneralPaymentDTO> queryGeneralPaymentByFkbdId(Map<String, Object> paramMap) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomForm.queryGeneralPaymentByFkbdId", paramMap);
	}

	@Override
	public List<CustomForm> queryListByEx() {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomForm.queryListByEx");
	}

	
	
}
