package com.xinleju.platform.portal.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.portal.entity.Component;

/**
 * @author admin
 */

public interface ComponentDao extends BaseDao<String, Component> {

    List<Component> queryFuzzyList(Map map);

    Integer queryFuzzyCount(Map map);

    Component getComponentBySerialNo(Component param);

    public List<Map<String, Object>> queryAllList();

    /**
     * 获取分页数据
     * @param map 前台参数组织格式：
     *            {
     *            start:0,
     *            limit:20,
     *            fuzzyQueryFields:JSON.stringify(['name','code']),
     *            sortFields:JSON.stringify({"sortNum":"asc"},
     *            name:'xxx',
     *            code:'xxx',
     *            ........其他查询条件
     *            }
     * @return page分页对象
     */
    public Page queryObjectsByPage(Map map);
}
