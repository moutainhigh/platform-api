package com.xinleju.platform.univ.attachment.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.univ.attachment.dao.AttachmentCategoryDao;
import com.xinleju.platform.univ.attachment.entity.AttachmentCategory;

/**
 * @author haoqp
 * 
 * 
 */

@Repository
public class AttachmentCategoryDaoImpl extends BaseDaoImpl<String,AttachmentCategory> implements AttachmentCategoryDao{

	public AttachmentCategoryDaoImpl() {
		super();
	}

	
	
}
