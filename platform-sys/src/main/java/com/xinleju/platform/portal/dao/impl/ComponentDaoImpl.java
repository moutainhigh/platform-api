package com.xinleju.platform.portal.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.utils.SqlStatementUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.portal.dao.ComponentDao;
import com.xinleju.platform.portal.entity.Component;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class ComponentDaoImpl extends BaseDaoImpl<String,Component> implements ComponentDao{

	public ComponentDaoImpl() {
		super();
	}

	@Override
	public List<Component> queryFuzzyList(Map map) {
		return this.getSqlSession().selectList("com.xinleju.platform.portal.entity.Component.queryFuzzyList", map);
	}

	@Override
	public Integer queryFuzzyCount(Map map) {
		return this.getSqlSession().selectOne("com.xinleju.platform.portal.entity.Component.queryFuzzyCount", map);
	}

	@Override
	public Component getComponentBySerialNo(Component param) {
		return this.getSqlSession().selectOne("com.xinleju.platform.portal.entity.Component.getComponentBySerialNo", param);
	}

	@Override
	public List<Map<String, Object>> queryAllList() {
		return this.getSqlSession().selectList("com.xinleju.platform.portal.entity.Component.queryAllList");
	}

	@Override
	public Page queryObjectsByPage(Map map) {
		map.put("delflag", false);
		Page page = new Page();
		page.setLimit((Integer) map.get("limit"));
		page.setStart((Integer) map.get("start"));

		//设置总条数查询条件
		Map countMap = new HashMap(map.size());
		countMap.putAll(map);

		//查询数据
		SqlSession sqlSession = this.getSqlSession();
		String pageSqlStatment = SqlStatementUtil.getPageSqlStatment(map, Component.class);
		List<Component> list = sqlSession.selectList(Component.class.getName() + ".queryObjectsByPage", pageSqlStatment);
		page.setList(list);

		//查询总数
		String pageCountSqlStatment = SqlStatementUtil.getPageCountSqlStatment(countMap, Component.class);
		Integer count = sqlSession.selectOne(Component.class.getName() + ".queryObjectsCountByPage", pageCountSqlStatment);
		page.setTotal(count);
		return page;
	}
}
