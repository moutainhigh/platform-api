package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface AcDtoServiceCustomer extends BaseDtoServiceCustomer{

    public String queryModifyAcListByPage(String userInfo,String paramJson);

    public String batchUpdateAcAttr(String userInfo,String paramJson);
}
