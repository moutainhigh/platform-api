package com.xinleju.platform.univ.attachment.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.univ.attachment.dto.AttachmentTempDto;
import com.xinleju.platform.univ.attachment.entity.AttachmentTemp;

/**
 * @author haoqp
 * 
 * 
 */

public interface AttachmentTempService extends  BaseService <String,AttachmentTemp>{
	
	/**
	 * 保存临时文件列表
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int saveFileUpload(List<AttachmentTempDto> list) throws Exception;
	
}
