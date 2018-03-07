package com.xinleju.platform.sys.icon.dao.impl;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.icon.dao.IconToolsDao;
import com.xinleju.platform.sys.icon.entity.IconTools;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by ly on 2017/12/1.
 */
@Repository
public class IconToolsDaoImpl extends BaseDaoImpl<String,IconTools> implements IconToolsDao {

    public IconToolsDaoImpl() {
        super();
    }

    /**
     * 校验编码重复
     * @param map 参数
     * @return
     * @throws Exception
     */
    public Integer getCodeCount(Map<String, Object> map) throws Exception{
        return getSqlSession().selectOne("com.xinleju.platform.sys.icon.entity.IconTools.getCodeCount", map);
    }

    @Override
    public List<Map<String, Object>> getIconData(Map<String, Object> map) {
        return  getSqlSession().selectList("com.xinleju.platform.sys.icon.entity.IconTools.getIconData", map);
    }

    @Override
    public Integer getIconDataCount(Map<String, Object> map) {
        return  getSqlSession().selectOne("com.xinleju.platform.sys.icon.entity.IconTools.getIconDataCount", map);
    }
}