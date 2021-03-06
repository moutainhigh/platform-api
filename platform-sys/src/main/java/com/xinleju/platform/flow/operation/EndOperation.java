package com.xinleju.platform.flow.operation;

import java.util.List;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.dto.UserDto;
import com.xinleju.platform.flow.entity.SysNoticeMsg;
import com.xinleju.platform.flow.enumeration.FlowMonitorPoint;
import com.xinleju.platform.flow.model.ACUnit;
import com.xinleju.platform.flow.model.ApproverUnit;
import com.xinleju.platform.flow.model.InstanceUnit;

/**
 * 结束操作
 * 
 * @author daoqi
 *
 */
public class EndOperation extends DefaultOperation implements Operation {
	
	private static Logger log = Logger.getLogger(EndOperation.class);
	
	public EndOperation() {
		super(OperationType.END);
	}

	/**
	 * 流程结束处理
	 */
	@Override
	public String action(InstanceUnit instanceUnit, 
			ApprovalSubmitDto approvalDto) throws Exception {
		this.setInstanceUnit(instanceUnit);

		//1、通知业务系统流程结束状态
		noticeBusinessSystem(instanceUnit, approvalDto);
		log.info("\n\n EndOperation action() is called. isDoArchive="+instanceUnit.isDoArchive());
		
		//2、归档流程加入全文检索
		if(instanceUnit.isDoArchive()) {
			log.info("\n\n addInstanceInfoIntoContentSearch will be called");
			Executors.newFixedThreadPool(5).execute(new Runnable() {

				@Override
				public void run() {
					addInstanceInfoIntoContentSearch(instanceUnit);
				}
				
			});
		}
		
		//3、监控处理
		monitorHandle(null, instanceUnit.getFlId(), FlowMonitorPoint.FLOW_END);
		
		//4、抄送人消息推送
		sendMsgToStarter(instanceUnit);
		
		return "success";
	}

	private void sendMsgToStarter(InstanceUnit instanceUnit) throws Exception {
		//给发起人发待阅消息 
		ACUnit startAc = instanceUnit.getAcList().get(0);
		ApproverUnit startApprover = startAc.getPosts().get(0).getApprovers().get(0);
		UserDto user = new UserDto(startApprover.getApproverId(), startApprover.getApproverName());
		SysNoticeMsg toReadMsg = createToReadMsg(instanceUnit, instanceUnit.getCustomFormURL(), user);

		service.getMsgService().saveAndNotifyOthers(toReadMsg);
	}
}
