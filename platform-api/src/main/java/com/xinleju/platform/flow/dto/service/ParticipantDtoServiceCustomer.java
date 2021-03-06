package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface ParticipantDtoServiceCustomer extends BaseDtoServiceCustomer{

	String batchModifyReader(String userInfo, String modifyJson);

	String replaceFlowParticipant(String userJson, String saveJson);

	String queryFlowReaderList(String userInfo, String paramaterJson);

	String deleteReaderByFormData(String userInfo, String saveJson);

	String addResetReaderFormData(String userInfo, String saveJson);

	String queryReplaceParticipantList(String userInfo, String saveJson);

	String saveReplaceApprover(String userInfo, String saveReplaceJson);

}
