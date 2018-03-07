package com.xinleju.platform.sys.org.dto;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class FlowAcPostDto {
	//流程环节
	private String id;
	
	private  List<FlowPostParticipantDto> flowPostParticipantDtos;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<FlowPostParticipantDto> getFlowPostParticipantDtos() {
		return flowPostParticipantDtos;
	}

	public void setFlowPostParticipantDtos(
			List<FlowPostParticipantDto> flowPostParticipantDtos) {
		this.flowPostParticipantDtos = flowPostParticipantDtos;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
