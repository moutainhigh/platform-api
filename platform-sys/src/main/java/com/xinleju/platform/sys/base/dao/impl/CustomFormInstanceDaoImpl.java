package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.CustomFormInstanceDao;
import com.xinleju.platform.sys.base.dto.CustomFormInstanceDto;
import com.xinleju.platform.sys.base.entity.CustomFormInstance;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class CustomFormInstanceDaoImpl extends BaseDaoImpl<String,CustomFormInstance> implements CustomFormInstanceDao{

	public CustomFormInstanceDaoImpl() {
		super();
	}

	@Override
	public List<Map<String, Object>> getPageSort(Map<String, Object> map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomFormInstance.getPageSort", map);
	}

	@Override
	public Integer getPageSortCount(Map<String, Object> map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormInstance.getPageSortCount", map);
	}

	@Override
	public int updateStatus(CustomFormInstance customFormInstance) {
		return getSqlSession().update("com.xinleju.platform.sys.base.entity.CustomFormInstance.updateStatus", customFormInstance);
	}

	@Override
	public Integer queryCurrentNumber() {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormInstance.getMaxNumberToday");
	}

	@Override
	public List<CustomFormInstanceDto> getFundPageSort(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomFormInstance.getFundPageSort", map);
	}

	@Override
	public Integer getFundPageSortCount(Map map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormInstance.getFundPageSortCount", map);
	}

	@Override
	public List<CustomFormInstanceDto> queryListByParam(Map<String, Object> paramMap) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomFormInstance.queryListByParam", paramMap);
	}

	@Override
	public CustomFormInstance getObjectByBusinessCode(String billcode) {
		List<CustomFormInstance> list=getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomFormInstance.getObjectByBusinessCode", billcode);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<String> getAuthsInstanceIds(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomFormInstance.getAuthsInstanceIds", map);
	}

	@Override
	public void updateInstanceToHisTemplate(Map<String, Object> paramMap) {
		getSqlSession().update("com.xinleju.platform.sys.base.entity.CustomFormInstance.updateInstanceToHisTemplate", paramMap);
	}

	@Override
	public String queryBusinessIdByCode(String businessId) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormInstance.queryBusinessIdByCode", businessId);
	}

	@Override
	public String isCustomformInstance(String id) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormInstance.isCustomformInstance", id);
	}

	@Override
	public List<Map<String, Object>> getMyFormPageSort(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomFormInstance.getMyFormPageSort", map);
	}

	@Override
	public Integer getMyFormPageCount(Map map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomFormInstance.getMyFormPageCount", map);
	}
}
