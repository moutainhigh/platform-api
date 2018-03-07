package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.ParticipantDao;
import com.xinleju.platform.flow.dto.ParticipantDto;
import com.xinleju.platform.flow.entity.Participant;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class ParticipantDaoImpl extends BaseDaoImpl<String,Participant> implements ParticipantDao{

	public ParticipantDaoImpl() {
		super();
	}

	@Override
	public void deleteReaderDataByFlId(String flId) {
		String methodId = "com.xinleju.platform.flow.entity.Participant.deleteReaderDataByFlId";
		int result = getSqlSession().update(methodId, flId);
	}

	@Override
	public void deleteReaderDataByReader(Participant reader) {
		String methodId = "com.xinleju.platform.flow.entity.Participant.deleteReaderDataByReader";
		int result = getSqlSession().update(methodId, reader);
	}

	@Override
	public List<ParticipantDto> queryFlowReaderList(Map<String, Object> paramMap) {
		String method = "com.xinleju.platform.flow.entity.Participant.queryFlowReaderList";
		return getSqlSession().selectList(method, paramMap);
	}

	@Override
	public void deleteReaderDataByParamMap(Map<String, Object> paramMap) {
		String method = "com.xinleju.platform.flow.entity.Participant.deleteReaderDataByParamMap";
		getSqlSession().update(method, paramMap);
		
	}

	@Override
	public int replaceFlowParticipant(ParticipantDto participantDto) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		//oldParticipantId :oldParticipantId, oldParticipantType: oldType, flId: item.id, 
		//participantId :replacerId, participantType :type, participantScope :scope
		paramMap.put("oldParticipantId", participantDto.getOldParticipantId());
		paramMap.put("oldParticipantType", participantDto.getOldParticipantType());
		paramMap.put("flId", participantDto.getFlId());
		
		paramMap.put("participantId", participantDto.getParticipantId());
		paramMap.put("participantType", participantDto.getParticipantType());
		paramMap.put("participantScope", participantDto.getParticipantScope());
		paramMap.put("paramValue", participantDto.getParamValue());
		paramMap.put("acId", participantDto.getAcId());
		
		String method = "com.xinleju.platform.flow.entity.Participant.replaceFlowParticipant";
		return getSqlSession().update(method, paramMap);
	}

	@Override
	public List<Participant> queryReplaceParticipantList(Map<String, Object> paramMap) {
		return getSqlSession().selectList(Participant.class.getName()+".queryReplaceParticipantList",paramMap);
	}

	@Override
	public List<Participant> queryReplaceParticipantListForUpdate(Map<String, Object> paramMap) {
		return getSqlSession().selectList(Participant.class.getName()+".queryReplaceParticipantListForUpdate",paramMap);
	}

	@Override
	public List<Participant> queryListByAcIdGroup(Map<String, Object> paramMap) {
		return getSqlSession().selectList(Participant.class.getName()+".queryListByAcIdGroup",paramMap);
	}

	@Override
	public List<Participant> getAllParticipantByFlId(Map<String, Object> paramMap) {
		return getSqlSession().selectList(Participant.class.getName()+".getAllParticipantByFlId",paramMap);
	}

	@Override
	public int deleteApproverByAcIds(Map<String, Object> paramMap) {
		return getSqlSession().delete(Participant.class.getName()+".deleteApproverByAcIds",paramMap);
	}
}
