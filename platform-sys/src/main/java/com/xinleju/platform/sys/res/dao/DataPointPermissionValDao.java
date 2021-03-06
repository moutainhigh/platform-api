package com.xinleju.platform.sys.res.dao;

import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.res.entity.DataPointPermissionVal;

/**
 * @author admin
 *
 */

public interface DataPointPermissionValDao extends BaseDao<String, DataPointPermissionVal> {
	
	/**
	 * 保存指定数据授权值
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer saveDataAuthVal(Map<String,Object> param)throws Exception;
	
	/**
	 * 删除指定数据授权值
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer delDataAuthVal(Map<String,Object> param)throws Exception;

}
