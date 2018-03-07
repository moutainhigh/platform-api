package com.xinleju.platform.sys.base.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;
import com.xinleju.platform.sys.base.dto.BaseSupplierDto;

public interface BaseSupplierDtoServiceCustomer extends BaseDtoServiceCustomer{

	public String saveSupplierAndAccont(String userinfo, String saveJson);

	public String getSupplierAndAccontById(String userinfo, String id);

	public String updateSupplierAndAccont(String userinfo, String id);

	public String deleteSupplierAndAccont(String userinfo, String id);

	public String updateStatus(String userinfo, String id);

	public String deleteAllByIds(String userinfo, String string);
	/**
	 * 生成NC系统能识别的同步xml
	 * 
	 * @param supplierList
	 * @param list
	 * @return
	 */
	public String createSyncXml2NC(String createJson,String sendUser) ;
}
