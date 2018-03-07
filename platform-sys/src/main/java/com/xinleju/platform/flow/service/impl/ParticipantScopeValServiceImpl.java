package com.xinleju.platform.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.ParticipantScopeValDao;
import com.xinleju.platform.flow.entity.ParticipantScopeVal;
import com.xinleju.platform.flow.service.ParticipantScopeValService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class ParticipantScopeValServiceImpl extends  BaseServiceImpl<String,ParticipantScopeVal> implements ParticipantScopeValService{
	

	@Autowired
	private ParticipantScopeValDao participantScopeValDao;
	

}
