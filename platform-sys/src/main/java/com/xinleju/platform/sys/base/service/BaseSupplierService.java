package com.xinleju.platform.sys.base.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dto.BaseSupplierDto;
import com.xinleju.platform.sys.base.entity.BaseSupplier;

/**
 * @author admin
 * 
 * 
 */

public interface BaseSupplierService extends  BaseService <String,BaseSupplier>{
    /**
     * 
     * @param dto
     * @return
     * @throws Exception
     * 保存供方账号和供方档案
     */
	
	public String saveSupplierAndAccont(BaseSupplierDto dto) throws Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * 根据供方档案id 查询供方档案和供方账号
	 */
	public BaseSupplierDto getSupplierAndAccontById(String id)throws Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * 根据供方档案id 修改供方档案和供方账号
	 */
	public String updateSupplierAndAccont(BaseSupplierDto baseSupplierDto) throws Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * 根据供方档案id 删除供方档案和供方账号
	 */
	public int deleteSupplierAndAccont(String id)throws Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * 根据供方档案id 修改为启用和禁用
	 */
	public int updateStatus(BaseSupplierDto baseSupplierDto) throws Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * 根据供方档案id 删除供方档案和供方账号
	 */
	public int deleteAllByIds(String ids) throws Exception;
	/**
	 * 
	 * @param id
	 * @return
	 * 获取分页列表数据
	 */
	
	public Page getSupplierDataByPage(Map map)throws Exception;
	
	/**
	 * 根据公司Id获取供方档案
	 * @param companyId
	 * @return
	 */
	public List<Map<String,Object>> getSupplierByCompanyId(Map<String, Object> param)throws Exception;
	
	/**
	 * 生成NC系统能识别的同步xml
	 * 
	 * @param supplierList
	 * @param list
	 * @return
	 */
	public String createSyncXml2NC(String createJson,String sendUser);
}
