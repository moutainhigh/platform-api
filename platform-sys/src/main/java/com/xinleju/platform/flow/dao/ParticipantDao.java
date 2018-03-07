package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.dto.ParticipantDto;
import com.xinleju.platform.flow.entity.Participant;

/**
 * @author admin
 *
 */

public interface ParticipantDao extends BaseDao<String, Participant> {

	void deleteReaderDataByFlId(String flId);

	void deleteReaderDataByReader(Participant reader);

	List<ParticipantDto> queryFlowReaderList(Map<String, Object> paramMap);

	void deleteReaderDataByParamMap(Map<String, Object> params);

	int replaceFlowParticipant(ParticipantDto participantDto);
	
	List<Participant> queryReplaceParticipantList(Map<String,Object> paramMap);

	List<Participant> queryReplaceParticipantListForUpdate(Map<String,Object> paramMap);

	List<Participant> queryListByAcIdGroup(Map<String,Object> paramMap);

	List<Participant> getAllParticipantByFlId(Map<String, Object> paramMap);

	int deleteApproverByAcIds(Map<String, Object> paramMap);
}
