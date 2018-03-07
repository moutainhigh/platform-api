package com.xinleju.platform.sys.icon.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.icon.entity.IconTools;

import java.util.Map;

/**
 * Created by ly on 2017/12/1.
 */
public interface IconToolsService extends BaseService<String,IconTools> {
    /**
     * 校验编码重复
     * @param map 参数
     * @return
     * @throws Exception
     */
    public Integer getCodeCount(Map<String, Object> map) throws Exception;

    Page getDataByPage(Map<String, Object> map)throws Exception;
}
