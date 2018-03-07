package com.xinleju.platform.flow.dao.impl;

import com.xinleju.platform.base.utils.Page;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.AcDao;
import com.xinleju.platform.flow.entity.Ac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class AcDaoImpl extends BaseDaoImpl<String,Ac> implements AcDao{

	public AcDaoImpl() {
		super();
	}

	@Override
	public Page queryModifyAcListByPage(Map<String, Object> searchParams) {
		Page page = new Page();
		page.setLimit((Integer) searchParams.get("limit"));
		page.setStart((Integer) searchParams.get("start"));

		SqlSession sqlSession = this.getSqlSession();
		List<Map<String,Object>> list = sqlSession.selectList(Ac.class.getName() + ".queryModifyAcListByPage", searchParams);
		page.setList(list);

		Integer count = sqlSession.selectOne(Ac.class.getName() + ".queryModifyAcListByPageCount", searchParams);
		page.setTotal(count);
		return page;
	}

	@Override
	public int batchUpdateAcAttr(Map<String, Object> batchUpdateAcData) {
		SqlSession sqlSession = this.getSqlSession();

		return sqlSession.update(Ac.class.getName() + ".batchUpdateAcAttr", batchUpdateAcData);
	}

	@Override
	public int batchUpdateFlOperationPerson(Map<String, Object> batchUpdateAcData) {
		SqlSession sqlSession = this.getSqlSession();

		return sqlSession.update(Ac.class.getName() + ".batchUpdateFlOperationPerson", batchUpdateAcData);
	}
}
