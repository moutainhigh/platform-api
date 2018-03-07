package com.xinleju.platform.sys.icon.service.impl;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.icon.dao.IconToolsDao;
import com.xinleju.platform.sys.icon.entity.IconTools;
import com.xinleju.platform.sys.icon.service.IconToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ly on 2017/12/1.
 */
@Service
public class IconToolsServiceImpl extends BaseServiceImpl<String,IconTools> implements IconToolsService{

    @Autowired
    private IconToolsDao iconToolsDao;

    /**
     * 校验编码重复
     * @param map 参数
     * @return
     * @throws Exception
     */
    @Override
    public Integer getCodeCount(Map<String, Object> map) throws Exception{
        return iconToolsDao.getCodeCount(map);
    }

    @Override
    public Page getDataByPage(Map<String, Object> map) throws Exception {
        Page page=new Page();
        List<Map<String,Object>> list = iconToolsDao.getIconData(map);
        Integer count = iconToolsDao.getIconDataCount(map);
        page.setLimit((Integer) map.get("limit") );
        page.setList(list);
        page.setStart((Integer) map.get("start"));
        page.setTotal(count);
        return page;
    }
}
