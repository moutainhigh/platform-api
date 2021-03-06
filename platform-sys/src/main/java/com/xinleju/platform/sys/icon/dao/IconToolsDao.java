package com.xinleju.platform.sys.icon.dao;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.icon.entity.IconTools;

import java.util.List;
import java.util.Map;

/**
 * Created by ly on 2017/12/1.
 */
public interface IconToolsDao extends BaseDao<String, IconTools> {
    /**
     * 校验编码重复
     * @param map 参数
     * @return
     * @throws Exception
     */
    public Integer getCodeCount(Map<String, Object> map) throws Exception;

    List<Map<String,Object>> getIconData(Map<String, Object> map);

    Integer getIconDataCount(Map<String, Object> map);
}
