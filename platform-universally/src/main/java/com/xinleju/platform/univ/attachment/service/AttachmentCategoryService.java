package com.xinleju.platform.univ.attachment.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.univ.attachment.entity.AttachmentCategory;

/**
 * @author haoqp
 * 
 * 
 */

public interface AttachmentCategoryService extends  BaseService <String,AttachmentCategory>{
	
	/**
	 * 为更新操作查询code、name是否重复
	 * 
	 * @param parameter
	 * @return 更新记录以外code条数
	 * @throws Exception
	 */
	int getCountOfCodeForUpdate(Map<String, Object> parameter) throws Exception;

	
}
