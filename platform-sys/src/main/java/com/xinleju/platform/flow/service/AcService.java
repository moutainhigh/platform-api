package com.xinleju.platform.flow.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.entity.Ac;

import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

public interface AcService extends  BaseService <String,Ac>{

    /**
     * 查询节点列表
     * @param searchParams
     * @return
     * @throws Exception
     */
    public Page queryModifyAcListByPage(Map<String,Object> searchParams) throws Exception;

    /**
     * 批量更新节点属性
     * @param updateAttrMap
     * @return
     * @throws Exception
     */
    public Map<String,Object> batchUpdateAcAttr(Map<String,Object> updateAttrMap) throws Exception;
}
