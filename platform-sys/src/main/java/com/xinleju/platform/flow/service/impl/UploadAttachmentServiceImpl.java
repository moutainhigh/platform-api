package com.xinleju.platform.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.UploadAttachmentDao;
import com.xinleju.platform.flow.entity.UploadAttachment;
import com.xinleju.platform.flow.service.UploadAttachmentService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class UploadAttachmentServiceImpl extends  BaseServiceImpl<String,UploadAttachment> implements UploadAttachmentService{
	

	@Autowired
	private UploadAttachmentDao uploadAttachmentDao;
	

}
