package com.xinleju.platform.flow.operation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.datasource.DataSourceContextHolder;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanRelationInfo;
import com.xinleju.platform.flow.dao.InstanceDao;
import com.xinleju.platform.flow.dao.InstancePostDao;
import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.dto.UserDto;
import com.xinleju.platform.flow.entity.Agent;
import com.xinleju.platform.flow.entity.BusinessObject;
import com.xinleju.platform.flow.entity.Instance;
import com.xinleju.platform.flow.entity.InstanceAc;
import com.xinleju.platform.flow.entity.InstanceGroup;
import com.xinleju.platform.flow.entity.InstancePost;
import com.xinleju.platform.flow.entity.InstanceTask;
import com.xinleju.platform.flow.entity.InstanceTransitionRecord;
import com.xinleju.platform.flow.entity.MobileParam;
import com.xinleju.platform.flow.entity.SysNoticeMsg;
import com.xinleju.platform.flow.entity.SysNoticeMsgTemp;
import com.xinleju.platform.flow.enumeration.ACStatus;
import com.xinleju.platform.flow.enumeration.ApproverNullStrategy;
import com.xinleju.platform.flow.enumeration.ApproverStatus;
import com.xinleju.platform.flow.enumeration.AutoPassType;
import com.xinleju.platform.flow.enumeration.FlAcType;
import com.xinleju.platform.flow.enumeration.FlowMonitorHandleType;
import com.xinleju.platform.flow.enumeration.FlowMonitorPoint;
import com.xinleju.platform.flow.enumeration.InstanceOperateType;
import com.xinleju.platform.flow.enumeration.InstanceStatus;
import com.xinleju.platform.flow.enumeration.PostNullStrategy;
import com.xinleju.platform.flow.enumeration.PostStatus;
import com.xinleju.platform.flow.enumeration.ProxyType;
import com.xinleju.platform.flow.enumeration.TaskSource;
import com.xinleju.platform.flow.enumeration.TaskStatus;
import com.xinleju.platform.flow.enumeration.TaskType;
import com.xinleju.platform.flow.exception.FlowException;
import com.xinleju.platform.flow.model.ACUnit;
import com.xinleju.platform.flow.model.ApproverUnit;
import com.xinleju.platform.flow.model.FlowMonitorBean;
import com.xinleju.platform.flow.model.InstanceUnit;
import com.xinleju.platform.flow.model.PostUnit;
import com.xinleju.platform.flow.model.TaskUnit;
import com.xinleju.platform.flow.operation.concurrent.CompetitionStrategy;
import com.xinleju.platform.flow.operation.concurrent.ConcurrentStrategy;
import com.xinleju.platform.flow.operation.concurrent.ConcurrentStrategyFactory;
import com.xinleju.platform.flow.operation.concurrent.SerialStrategy;
import com.xinleju.platform.flow.operation.concurrent.TogetherStrategy;
import com.xinleju.platform.flow.service.AgentService;
import com.xinleju.platform.flow.service.InstanceAcService;
import com.xinleju.platform.flow.service.InstanceGroupService;
import com.xinleju.platform.flow.service.InstanceService;
import com.xinleju.platform.flow.service.InstanceTaskService;
import com.xinleju.platform.flow.service.InstanceTransitionRecordService;
import com.xinleju.platform.flow.service.MonitorSettingService;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.search.dto.SearchIndexDto;

public abstract class DefaultOperation implements Operation {
	
	private static Logger log = Logger.getLogger("flowLogger");
	
	private static final String SYNC = "sync";

	protected OperationType type;
	
	protected InstanceService service;
	
	//当前位置
	protected ACUnit currentAc;
	protected PostUnit currentPost;
	protected ApproverUnit currentApprover;
	
	protected InstanceUnit instanceUnit;
	protected ApprovalSubmitDto approvalDto;

	public DefaultOperation() {
		super();
	}

	public DefaultOperation(OperationType type) {
		super();
		this.type = type;
	}
	

	@Override
	public String action(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto) throws Exception {
		
		//1、设置模型中的当前位置
		setCurrentLocation(instanceUnit, approvalDto);
		
		//记录流转日志(在模型操作之前，是因为模型操作中会改变当前审批人)
		saveTransition(currentApprover, approvalDto.getOperationName());
		
		//2、模型操作
		try {
			operate(instanceUnit, approvalDto);
		}catch(FlowException e) {
			//由于监控原因流程挂起，穿透多层处理，结束模型操作，直接返回到模型保存！！！
		}
		
		//3、设置实例当前审批人
		setCurrentApprovers(instanceUnit);
		
		//4、保存模型
		save(instanceUnit);
		
		//5、处理消息
		handleMessages(instanceUnit, approvalDto);
		
		//6、流程结束处理
		flowEnd(instanceUnit, approvalDto);
		
		//7、个性化操作
		customerHandle(instanceUnit, approvalDto);
		
		return null;
	}

	protected void operate(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto) throws Exception {
	}
	
	protected void customerHandle(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto) throws Exception {
	}

	private void flowEnd(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto) throws Exception {
		if(InstanceStatus.FINISHED.getValue().equals(instanceUnit.getStatus())) {
			Operation endOperation = new OperationFactory().newOperation(OperationType.END.getCode(), service);
			endOperation.action(instanceUnit, approvalDto);
		}
	}

	/**
	 * 设置流程实例的当前审批人列表
	 * @param instanceUnit
	 */
	public void setCurrentApprovers(InstanceUnit instanceUnit) {
		List<String> currentApprovers = new ArrayList<String>();
		List<String> currentApproverIds = new ArrayList<String>();
		List<ACUnit> acList = instanceUnit.getAcList();
		if(CollectionUtils.isEmpty(acList)) {
			return ;
		}
		for(ACUnit acUnit : acList) {
			List<PostUnit> posts = acUnit.getPosts();
			if(CollectionUtils.isEmpty(posts)) {
				continue;
			}
			for(PostUnit postUnit : posts) {
				List<ApproverUnit> approvers = postUnit.getApprovers();
				if(CollectionUtils.isEmpty(approvers)) {
					continue;
				}
				for(ApproverUnit approver : approvers) {
					TaskUnit task = approver.getTask();
					if(task != null) {
						String taskStatus = task.getTaskStatus();
						if(taskStatus != null && taskStatus.equals(TaskStatus.RUNNING.getValue())
								&& approver.getDbAction() != 2 && task.getDbAction() != 2) {
							currentApprovers.add(approver.getApproverName());
							currentApproverIds.add(approver.getApproverId());
						}
					}
				}
			}
		}
		instanceUnit.setCurrentApprovers(currentApprovers);
		instanceUnit.setCurrentApproverIds(currentApproverIds);
	}

	public String next(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto)
			throws Exception {
		
//		//设置当前位置
//		setCurrentLocation(instanceUnit, approvalDto);
		
		//代理人或被代理人审批处理
		if(!handleProxyApprove(currentApprover)) {
			return "return after proxy approve!";
		}
		
		//完成当前人
		complate(currentApprover, approvalDto);
		log.info("完成当前人: " + currentApprover.getId());
		
		//设置所在岗位的剩余人数
		ConcurrentStrategy personStrategy = getPersonStrategy(instanceUnit, currentAc);
		int leftPerson = currentPost.getLeftPerson();
		if(personStrategy instanceof CompetitionStrategy) {	//加此判断是由于在前面complate已处理人数
			leftPerson = personStrategy.calculateLeftCnt(currentPost.getLeftPerson());
			currentPost.setLeftPerson(leftPerson);
			log.info("设置所在岗位的剩余人数: leftPerson = " + leftPerson);
		}
		
		ConcurrentStrategy postStrategy = getPostStrategy(currentAc);
		if(postStrategy == null) {
			throw new FlowException("环节【id=" + currentAc.getAcId() + ", name=" + currentAc.getAcName() + "】没有配置多岗策略，可能是加签环节设置错误");
		}
		
		//下一人在同岗内(只有并发或串行时且不是最后一人,针对人)
		if(leftPerson > 0) {
			log.info("下一人在同岗内(只有并发或串行时且不是最后一人,针对人)");
			
			//人并发时：当前人完成，其他不作任何处理，当前人排序调整
			if(personStrategy instanceof TogetherStrategy) {
				sortIn(currentPost);
				log.info("人并发时：当前人完成，其他不作任何处理，当前人排序调整");
				
				//人串行时：当前人完成，寻找串行的下一人点亮
			} else if(personStrategy instanceof SerialStrategy) {
				turnOnNextInSamePost(currentPost, currentApprover);
				log.info("人串行时：当前人完成，寻找串行的下一人点亮");
			}
			
			//竞争掉当前人所在岗位的竞争岗位
			//处理被跳过的岗位与人(岗位抢占)
			if(postStrategy instanceof CompetitionStrategy) {
				jumpOver(currentAc, currentPost);
				log.info("处理被跳过的岗位与人(岗位抢占)");
			}
			
			//下一人在不同岗内，跳岗(只有人抢占或并发串行的最后一人)
		} else {
			log.info("下一人在不同岗内，跳岗(只有人抢占或并发串行的最后一人)");
			
			//处理被跳过的人(人抢占)
			if(personStrategy instanceof CompetitionStrategy) {
				jumpOver(currentPost, currentApprover);
			}
			
			//完成当前岗位（人已完成）
			complate(currentPost);
			log.info("完成当前岗位（人已完成）" + currentPost.getId());
			
			int leftPost = currentAc.getLeftPost();
			if(postStrategy instanceof CompetitionStrategy) {
				leftPost = postStrategy.calculateLeftCnt(currentAc.getLeftPost());
				currentAc.setLeftPost(leftPost);
				log.info("计算剩余岗位：leftPost=" + leftPost);
			}
			
			//下一人在同一环节内（只有岗位并发或串行时且不是最后一岗，针对岗）
			if(leftPost > 0) {
				log.info("下一人在同一环节内（只有岗位并发或串行时且不是最后一岗，针对岗）");
				
				//岗并发时：当前人与对应的岗完成，其他不作任何处理，当前岗位排序
				if(postStrategy instanceof TogetherStrategy) {
					sortIn(currentAc);
					log.info("岗并发时：当前人与对应的岗完成，其他不作任何处理，当前岗位排序");
					
				} else if(postStrategy instanceof SerialStrategy) {
					
					//岗串行时：当前人与对应的岗完成，寻找一下岗位，根据同岗多人策略启动岗位下的人
						//并行或抢占，点亮下面所有的人
						//串行：点亮下面第一人
					turnOnNextInSameAc(currentAc, currentPost, instanceUnit);
					log.info("岗串行时：当前人与对应的岗完成，寻找一下岗位，根据同岗多人策略启动岗位下的人");
				}
				
				//下一人在不同环节内，跳环节（只有岗位抢占或并发串行的最后一岗）
			} else {
				log.info("下一人在不同环节内，跳环节（只有岗位抢占或并发串行的最后一岗）");
				
				//处理被跳过的岗位与人(岗位抢占)
				if(postStrategy instanceof CompetitionStrategy) {
					jumpOver(currentAc, currentPost);
					log.info("处理被跳过的岗位与人(岗位抢占)");
				}
				
				//完成当前环节(人岗已完成)
				complate(currentAc);
				log.info("完成当前环节(人岗已完成), acId=" + currentAc.getAcId());
				
//				//结束节点处理
//				ACUnit next = findNextAc(instanceUnit, currentAc);
//				if(FlAcType.END.getAcType().equals(next.getAcType())) {
//					instanceUnit.setEndDate(new Timestamp(System.currentTimeMillis()));
//					instanceUnit.setStatus(InstanceStatus.FINISHED.getValue());
//					next.setAcStatus(ACStatus.FINISHED.getValue());
//					
//				} else {
//					//点亮下一环节：根据两级策略点亮下面的岗位，人
//					turnOn(next, instanceUnit);
//				}
				
				List<ACUnit> nextList = currentAc.getNextAcs();
				if(CollectionUtils.isNotEmpty(nextList) && nextList.size() == 1) {
					ACUnit next = nextList.get(0);
					log.info("点亮下一环节：根据两级策略点亮下面的岗位，人, acId=" + next.getAcId());
					
					if(FlAcType.END.getAcType().equals(next.getAcType())) {
						instanceUnit.setEndDate(new Timestamp(System.currentTimeMillis()));
						instanceUnit.setStatus(InstanceStatus.FINISHED.getValue());
//						next.setAcStatus(ACStatus.FINISHED.getValue());
						log.info("处理结束节点");
						complate(next);
						
					} else if(FlAcType.JOIN.getAcType().equals(next.getAcType())) {
						if(next.getLeftPost() <= 0) {
							log.info("处理聚合节点的下一环节");
							turnOn(next.getNextAcs(), instanceUnit);
						}
						
						//普通节点
					} else {
						//点亮下一环节：根据两级策略点亮下面的岗位，人
						log.info("点亮下一环节：根据两级策略点亮下面的岗位，人");
						turnOn(next, instanceUnit);
						
					}
					
					//分支环节时
				} else {
					log.info("分支环节时");
					turnOn(nextList, instanceUnit);
				}
			}
		}
		
		return "success";
	}
	
	/**
	 * 审批人为空导致岗位跳过时用！！！
	 * 
	 * @param instanceUnit
	 * @param post
	 * @return
	 * @throws Exception
	 */
	public String next(InstanceUnit instanceUnit, PostUnit post)
			throws Exception {
			
		//完成当前岗位（人已完成）
		complate(post);
		
		ACUnit acUnit = post.getOwner();
		ConcurrentStrategy postStrategy = getPostStrategy(acUnit);
		
		int leftPost = currentAc.getLeftPost();
		
//		delete when #12299
//		if(postStrategy instanceof CompetitionStrategy) {
//			leftPost = postStrategy.calculateLeftCnt(currentAc.getLeftPost());
//			currentAc.setLeftPost(leftPost);
//		}
		
		//下一人在同一环节内（只有岗位并发或串行时且不是最后一岗，针对岗）
		if(leftPost > 0) {
			//岗并发时：当前人与对应的岗完成，其他不作任何处理，当前岗位排序
			if(postStrategy instanceof TogetherStrategy
					|| postStrategy instanceof CompetitionStrategy) {
				sortIn(acUnit);
			} else if(postStrategy instanceof SerialStrategy) {
				
				//岗串行时：当前人与对应的岗完成，寻找一下岗位，根据同岗多人策略启动岗位下的人
					//并行或抢占，点亮下面所有的人
					//串行：点亮下面第一人
				turnOnNextInSameAc(acUnit, post, instanceUnit);
			}
			
			//下一人在不同环节内，跳环节（只有岗位抢占或并发串行的最后一岗）
		} else {
			
			//岗位抢占时，删除当前岗位，以便其他有人岗位审批时留下空行
			if(postStrategy instanceof CompetitionStrategy) {
				jumpOver(currentAc, currentPost);
			}
			
			//完成当前环节(人岗已完成)
			complate(acUnit);
			
//			//结束节点处理
//			ACUnit next = findNextAc(instanceUnit, acUnit);
//			if(FlAcType.END.getAcType().equals(next.getAcType())) {
//				instanceUnit.setEndDate(new Timestamp(System.currentTimeMillis()));
//				instanceUnit.setStatus(InstanceStatus.FINISHED.getValue());
//				next.setAcStatus(ACStatus.FINISHED.getValue());
//				
//			} else {
//				//点亮下一环节：根据两级策略点亮下面的岗位，人
//				turnOn(next, instanceUnit);
//			}
			
			List<ACUnit> nextList = currentAc.getNextAcs();
			if(CollectionUtils.isNotEmpty(nextList) && nextList.size() == 1) {
				ACUnit next = nextList.get(0);
				if(FlAcType.END.getAcType().equals(next.getAcType())) {
					instanceUnit.setEndDate(new Timestamp(System.currentTimeMillis()));
					instanceUnit.setStatus(InstanceStatus.FINISHED.getValue());
					next.setAcStatus(ACStatus.FINISHED.getValue());
					
				} else if(FlAcType.JOIN.getAcType().equals(next.getAcType())) {
					
//					next.setLeftPost(next.getLeftPost() - 1);	//已在complate环节中处理聚合节点的剩余未达数！！！
					if(next.getLeftPost() == 0) {
						turnOn(next.getNextAcs(), instanceUnit);
					}
					
					//普通节点
				} else {
					//点亮下一环节：根据两级策略点亮下面的岗位，人
					turnOn(next, instanceUnit);
				}
				
				//分支环节时
			} else {
				turnOn(nextList, instanceUnit);
			}
		}
		
		return "success";
	}
	
	/**
	 * 代理人或被代理人审批处理，并返回流程是否往下走标志
	 * 
	 * @param currentApprover
	 * @return true:流程往下走；false：流程不往下走
	 * @throws Exception 
	 */
	private boolean handleProxyApprove(ApproverUnit currentApprover) throws Exception {
		String proxyType = currentApprover.getProxyType();
		if(StringUtils.isEmpty(proxyType)) {
			return true;
		}
		
		//流程复制时删除竞争者，当前人审批接着往下走
		if(ProxyType.COPY.getValue().equals(proxyType)) {
			ApproverUnit proxy = getProxy(currentApprover);
			if(proxy != null) {
				proxy.setDbAction(2);
			}
			return true;
			
			//流程剪切时当前审批人往下走（另一人在构建模型时已删除）
		} else if(ProxyType.CUT.getValue().equals(proxyType)) {
			//无动作
			return true;
			
			//前加签
		} else if(ProxyType.ADD_AC_BEFORE.getValue().equals(proxyType)) {
			
			//当前审批人是代理人，点亮被代理人，流程不往下走！！！
			if(StringUtils.isNotEmpty(currentApprover.getProxyed())) {
				ApproverUnit proxyed = getProxy(currentApprover);
				turnOnSelf(proxyed);
				this.complate(currentApprover, approvalDto);
				return false;
				
				//当前审批人是被代理人，流程往下走
			} else {
				return true;
			}
			
			//后加签
		} else if(ProxyType.ADD_AC_AFTER.getValue().equals(proxyType)) {
			
			//当前审批人是被代理人，点亮代理，流程不往下走
			if(StringUtils.isNotEmpty(currentApprover.getProxy())) {
				ApproverUnit proxy = getProxy(currentApprover);
				turnOnSelf(proxy);
				this.complate(currentApprover, approvalDto);
				return false;
				
				//当前审批人是代理人，流程往下走
			} else {
				return true;
			}
		} else {
			throw new FlowException("审批人的代理类型未知：" + currentApprover);
		}
	}
	
	private ApproverUnit getProxy(ApproverUnit approver) {
		String proxyGroupId = approver.getProxy();
		String proxyedGroupId = approver.getProxyed();
		String groupId = null;
		if(StringUtils.isNotEmpty(proxyGroupId)) {
			groupId = proxyGroupId;
		} else {
			groupId = proxyedGroupId;
		}
		
		List<ApproverUnit> approvers = approver.getOwner().getApprovers();
		if(CollectionUtils.isEmpty(approvers)) {
			return null;
		}
		for(ApproverUnit proxy : approvers) {
			if(proxy.getId().equals(groupId)) {
				return proxy;
			}
		}
		return null;
	}

	private void jumpOver(ACUnit currentAc, PostUnit currentPost) {
		List<PostUnit> posts = currentAc.getPosts();
		if(CollectionUtils.isEmpty(posts)) {
			return ;
		}
		for(PostUnit post : posts) {
			if(!currentPost.getId().equals(post.getId())) {
//				post.setDbAction(2);	//删除当前岗位???不能简单删除，下面可能还有已审批过的人员
				jumpOver(post, null);
			}
		}
	}
	
	protected void jumpOver(ACUnit currentAc) {
		List<PostUnit> posts = currentAc.getPosts();
		if(CollectionUtils.isEmpty(posts)) {
			return ;
		}
		for(PostUnit post : posts) {
			post.setDbAction(2);
			jumpOver(post, null);
		}
	}

	private void jumpOver(PostUnit currentPost, ApproverUnit currentApprover) {
		List<ApproverUnit> approvers = currentPost.getApprovers();
		if(CollectionUtils.isEmpty(approvers)) {
			return ;
		}
		if(currentApprover == null) {
			int i = 0;
			for(ApproverUnit approver : approvers) {
				TaskUnit task = approver.getTask();
				String taskStatus = task.getTaskStatus();
				if(TaskStatus.RUNNING.getValue().equals(taskStatus)
						|| TaskStatus.NOT_RUNNING.getValue().equals(taskStatus)
						|| taskStatus == null) {	//未运行时也删除，如代理后加签时
					approver.setDbAction(2);
					task.setDbAction(2);
					
					//收集待撤回消息ID
					instanceUnit.getMessagesToDel().add(task.getMsgId());
					
					i++;
				}
			}
			if(i == approvers.size()) {
				currentPost.setDbAction(2);
			}
			if(i < approvers.size()) {
				currentPost.setPostStatus(PostStatus.FINISHED.getValue());
			}
		} else {
			for(ApproverUnit approver : approvers) {
				if(!currentApprover.getId().equals(approver.getId())	//非当前人(用Id判断)
//						if(!currentApprover.getApproverId().equals(approver.getApproverId())	//非当前人
//						&& approver.getAutoPass() == 0) {								//排除：审批人重复检查时强制不跳过的情况
					){
					TaskUnit task = approver.getTask();
					String taskStatus = task.getTaskStatus();
					if(TaskStatus.RUNNING.getValue().equals(taskStatus)
//							|| taskStatus == null) {	//代理人前后加签中没有任务存在的情况！！！	在转办协办中跳过其他人时，代理人任务为空，此时不应该跳过！
						|| ProxyType.ADD_AC_AFTER.getValue().equals(approver.getProxyType())	//代理时被跳过
						|| ProxyType.ADD_AC_BEFORE.getValue().equals(approver.getProxyType())	//代理时被跳过
						) {
						approver.setDbAction(2);
						task.setDbAction(2);
						
						//收集待撤回消息ID
						instanceUnit.getMessagesToDel().add(task.getMsgId());
					}
				}
			}
		}
	}
	
	/**
	 * 跳过竞争的岗位
	 * 跳过当前人同岗位中的竞争者
	 */
	protected void jumpOver() {
		
		//跳过竞争的岗位
		ConcurrentStrategy postStrategy = getPostStrategy(currentAc);
		if(postStrategy instanceof CompetitionStrategy) {
			this.jumpOver(currentAc, currentPost);
		}
		
		//跳过当前人同岗位中的竞争者
		ConcurrentStrategy personStrategy = getPersonStrategy(instanceUnit, currentAc);
		if(personStrategy instanceof CompetitionStrategy) {
			jumpOver(currentPost, currentApprover);
		}
	}

	protected ConcurrentStrategy getPersonStrategy(InstanceUnit instanceUnit, ACUnit currentAc) {
		String multiPerson = currentAc.getMultiPerson();
		if(StringUtils.isEmpty(multiPerson)) {
			multiPerson = instanceUnit.getPostMultiPerson();
		}
		ConcurrentStrategy personStrategy = ConcurrentStrategyFactory.newStrategy(multiPerson);
		return personStrategy;
	}
	
	protected ConcurrentStrategy getPostStrategy(ACUnit acUnit) {
		String multiPost = acUnit.getMultiPost();
		ConcurrentStrategy postStrategy = ConcurrentStrategyFactory.newStrategy(multiPost);
		return postStrategy;
	}

	@SuppressWarnings("unused")
	private ACUnit findNextAc(InstanceUnit instanceUnit, ACUnit currentAc) {
		ACUnit next = null;
		List<ACUnit> acList = instanceUnit.getAcList();
		for(int i=0; i<acList.size(); i++) {
			if(currentAc.getAcId().equals(acList.get(i).getAcId())) {
				next = acList.get(i + 1);
				break;
			}
		}
		return next;
	}
	
	/**
	 * 点亮环节及下面的岗位、人员
	 * 
	 * @param acUnit
	 * @param instanceUnit
	 * @throws Exception
	 */
	protected void turnOn(ACUnit acUnit, InstanceUnit instanceUnit) throws Exception {
		
		log.info("进行点亮环节处理：instanceId=" + instanceUnit.getId() + ",acId=" + acUnit.getAcId());
		
		//先处理三种特殊情况
		//2、此环节是结束节点
		if(FlAcType.END.getAcType().equals(acUnit.getAcType())) {
			instanceUnit.setEndDate(new Timestamp(System.currentTimeMillis()));
			instanceUnit.setStatus(InstanceStatus.FINISHED.getValue());
			acUnit.setAcStatus(ACStatus.FINISHED.getValue());
			return ;
		}
		
		//3、此环节是join环节
		if(FlAcType.JOIN.getAcType().equals(acUnit.getAcType())) {
			if(acUnit.getLeftPost() <= 0) {
				acUnit.setAcStatus(ACStatus.FINISHED.getValue());
				this.turnOn(acUnit.getNextAcs(), instanceUnit);
			}
		}
		
		//3、此环节是fork环节
		if(FlAcType.FORK.getAcType().equals(acUnit.getAcType())
				|| FlAcType.CC.getAcType().equals(acUnit.getAcType())) {
			complate(acUnit);
			this.turnOn(acUnit.getNextAcs(), instanceUnit);
		}
		
		//挂起状态检查
		if(!ACStatus.HANGUP.getValue().equals(acUnit.getAcStatus())) {
			//岗位为空检查
			boolean autoHangup = checkPostIsAllNull(acUnit, instanceUnit);
			if(autoHangup) {
				log.info("流程【id=" + instanceUnit.getId() + ", name=" + instanceUnit.getName() + "】由于 岗位为空，自动挂起！");
				//给管理员发待办
				List<UserDto> adminList = service.queryAdminList();
				log.info("给管理员发消息，查询管理员列表：" + adminList);
				if(CollectionUtils.isNotEmpty(adminList)) {
					for(UserDto admin : adminList) {
						String admiViewUrl = "flow/runtime/approve/flow.html";
						SysNoticeMsg toDoMsg = this.createToDoMsg(admin, admiViewUrl, instanceUnit);
						toDoMsg.setExtendInfo(instanceUnit.getId());	//用于消除多个管理员时被竞争掉的任务
						instanceUnit.getMessages().add(toDoMsg);
					}
					log.info("构建管理员消息成功！");
				} else {
					log.info("系统管理员为空，审批人为空检查挂起时给管理员发送消息失败！");
				}
				
				//给监控人发待办
				log.info("给监控人发消息：");
				monitorHandle(null, null, FlowMonitorPoint.FLOW_HANGUP);
				
				return ;
			}
		}
		
		//1、流程之前操作环节已结束
		if(ACStatus.FINISHED.getValue().equals(acUnit.getAcStatus())) {
			return ;
		}
		
		//点亮环节
		acUnit.setAcStatus(ACStatus.RUNNING.getValue());
		acUnit.setAcStartTime(new Timestamp(System.currentTimeMillis()));
		
		List<PostUnit> posts = acUnit.getPosts();
		if(CollectionUtils.isNotEmpty(posts)) {
			//发送抄送人待阅消息
//			createToReadMsg(acUnit.getCcPerson(), instanceUnit);	TODO 调整到节点完成
			
			//点亮下面的岗位人员
			ConcurrentStrategy postStrategy = getPostStrategy(acUnit);
			if(postStrategy instanceof SerialStrategy) {
				PostUnit firstPost = posts.get(0);
				
				log.info("点亮环节下的岗位（串行）：instancePostId=" + firstPost.getId());
				turnOn(firstPost, instanceUnit, acUnit);
			} else {
				for(PostUnit post: posts) {
					turnOn(post, instanceUnit, acUnit);
				}
			}
		}
		
//		//处理下一环节是join
//		ACUnit nextJoin = acUnit.nextJoin();
//		if(nextJoin != null) {
//			int branchCount = nextJoin.getLeftPost();
//			nextJoin.setLeftPost(branchCount - 1);
//		}
	}
	/**
	 * 岗位为空是指环节下面所有的岗位都为空，部分为空的情况算正常情况。
	 * 检查为真时，给管理员发待办
	 * 
	 * @param acUnit
	 * @param instanceUnit
	 * @return:true:流程挂起，false:流程往下走
	 * @throws Exception 
	 */
	private boolean checkPostIsAllNull(ACUnit acUnit, InstanceUnit instanceUnit) throws Exception {
		
		//非普通环节不参与岗位为空判断！
		if(!FlAcType.TASK.getAcType().equals(acUnit.getAcType())) {
			return false;
		}
		
		List<PostUnit> posts = acUnit.getPosts();
		if(CollectionUtils.isEmpty(posts)) {
			log.info("环节【id=" + acUnit.getAcId() + ",name=" + acUnit.getAcName() + "】下的岗位为空:进行为空处理");
			
//			String postNullStrategy = instanceUnit.getPostNull();
			String postNullStrategy = acUnit.getPostNull();
			
			//当模板上配置本环节发起时指定审批人且不选择时可以发起，默认为岗位为空可以发起且显示
			if(acUnit.isAddLabel()) {	//还需要加上“可以不指定审批人，依旧可以发起流程”的判断，另外，isAddLabel与是否加签标识可能重复 TODO
				postNullStrategy = PostNullStrategy.START_SKIP_DISPLAY.getValue();
			}
			
			//允许起发，跳过，并显示该行
			if(PostNullStrategy.START_SKIP_DISPLAY.getValue().equals(postNullStrategy)) {
				log.info("岗位为空配置为 跳过：" + postNullStrategy);
				complate(acUnit);
				log.info("完成岗位为空的环节");

				turnOn(acUnit.getNextAcs(), instanceUnit);
				log.info("点亮下一环节完成");
				
				//允许起发，跳过，不显示该行
			} else if(PostNullStrategy.START_SKIP_DISPLAY_NONE.getValue().equals(postNullStrategy)) {
				//在发起后已处理岗位为空不显示的情况！！！
				
				//允许发起，挂起
			} else if(PostNullStrategy.START_HUANGUP.getValue().equals(postNullStrategy)) {
				log.info("岗位为空配置为 挂起：" + postNullStrategy);
				instanceUnit.setStatus(InstanceStatus.HANGUP.getValue());
				acUnit.setAcStatus(ACStatus.HANGUP.getValue());	//放行时使用
				return true;
				
				//其他非法值或<不允许发起时>流程发起时没有检查
			} else {
				throw new FlowException("岗位为空策略非法值或<不允许发起时>流程发起时没有检查，所在环节：" + acUnit.getAcName());
			}
		}
		return false;
	}
	
	/**
	 * 点亮环节及下面的岗位、人员
	 * 
	 * @param acUnitList
	 * @param instanceUnit
	 * @throws Exception
	 */
	public void turnOn(List<ACUnit> acUnitList, InstanceUnit instanceUnit) throws Exception {
		for(ACUnit acUnit : acUnitList) {
			turnOn(acUnit, instanceUnit);
		}
	}

	protected void complate(ACUnit currentAc) {
		
		//下一环节是聚合环节，其到达数-1
		joinLeftPostCal(currentAc);
		
		if(!ACStatus.FINISHED.getValue().equals(currentAc.getAcStatus())) {
			currentAc.setAcStatus(ACStatus.FINISHED.getValue());
			currentAc.setAcEndTime(new Timestamp(System.currentTimeMillis()));
			
			//抄送人处理
			createToReadMsg(currentAc.getCcPerson(), instanceUnit);
		}
	}
	
	private void joinLeftPostCal(ACUnit acUnit) {
		List<ACUnit> nextList = acUnit.getNextAcs();
		if(CollectionUtils.isNotEmpty(nextList)) {
			for(ACUnit ac : nextList) {
				if(FlAcType.JOIN.getAcType().equals(ac.getAcType())) {
					ac.setLeftPost(ac.getLeftPost() - 1);
					joinLeftPostCal(ac);
				}
			}
		}
	}

	private void turnOnNextInSameAc(ACUnit currentAc, PostUnit currentPost, InstanceUnit instanceUnit) throws Exception {
		List<PostUnit> posts = currentAc.getPosts();
		if(CollectionUtils.isEmpty(posts)) {
			return;
		}
		for(int i=0; i<posts.size(); i++) {
			if(posts.get(i).getId().equals(currentPost.getId())) {
				PostUnit next = posts.get(i + 1);
				
				log.info("点亮环节中下一岗位：acPostId=" + next.getId() + ", postName=" + next.getPostName());
				turnOn(next, instanceUnit, currentAc);
				break;
			}
		}
	}
	
	/**
	 * 点亮岗位：
	 * @param post
	 * @param instanceUnit
	 * @param currentAc
	 * @throws Exception
	 */
	protected void turnOn(PostUnit post, InstanceUnit instanceUnit, ACUnit currentAc) throws Exception {
		
		log.info("进行点亮岗位的处理：acPostId=" + post.getId());
		
		//挂起状态检查
		if(!PostStatus.HANGUP.getValue().equals(post.getPostStatus())) {
			//审批人为空检查
			boolean autoHangup = checkApproverIsAllNull(post, instanceUnit, currentAc);
			if(autoHangup) {
				log.info("流程【" + instanceUnit.getId() + "】由于审批人为空，自动挂起！");
				
				//给管理员发待办
//			UserDto admin = new UserDto("admin", "管理员");	// TODO zhangdaoqiang 需要调用权限接口
				List<UserDto> adminList = service.queryAdminList();
				log.info("查询管理员列表： " + adminList);
				
				if(CollectionUtils.isNotEmpty(adminList)) {
					for(UserDto admin : adminList) {
						String admiViewUrl = "flow/runtime/approve/flow.html"
								+ "?instanceId=" + instanceUnit.getId()
								+ "&time=" + new Date().getTime();
						SysNoticeMsg toDoMsg = this.createToDoMsg(admin, admiViewUrl, instanceUnit);
						toDoMsg.setExtendInfo(instanceUnit.getId());	//用于消除多个管理员时被竞争掉的
						instanceUnit.getMessages().add(toDoMsg);
						log.info("构建管理员待办消息" + toDoMsg);
					}
				} else {
					log.info("系统管理员为空，审批人为空检查挂起时给管理员发送消息失败！");
				}
				
				//给监控人发待办
				monitorHandle(null, null, FlowMonitorPoint.FLOW_HANGUP);
				
//				return ;
				//多岗位时一个岗位挂起，其他岗位不再往下走
				throw new FlowException("审批人为空挂起！");
			}
		}
		
		if(PostStatus.FINISHED.getValue().equals(post.getPostStatus())) {
			return ;
		}
		post.setPostStatus(PostStatus.RUNNING.getValue());
		post.setStartTime(new Timestamp(System.currentTimeMillis()));
		
		List<ApproverUnit> approvers = post.getApprovers();
		if(CollectionUtils.isNotEmpty(approvers)) {
			ConcurrentStrategy personStrategy = getPersonStrategy(instanceUnit, currentAc);
			if(personStrategy instanceof SerialStrategy) {
				ApproverUnit firstPersonInNextPost = approvers.get(0);
				turnOn(firstPersonInNextPost);
				
			} else {
				for(ApproverUnit approver: approvers) {
					turnOn(approver);
				}
			}
		}
	}
	
	/**
	 * 审批人为空是指岗位下面所有的人都为空，部分为空的情况算正常情况。
	 * 检查为真时，给管理员发待办
	 * 
	 * @param post
	 * @param instanceUnit
	 * @param currentAc
	 * 
	 * @return:true:流程挂起，false:流程往下走
	 * @throws Exception 
	 */
	private boolean checkApproverIsAllNull(PostUnit post, InstanceUnit instanceUnit, ACUnit currentAc) throws Exception {
		List<ApproverUnit> approvers = post.getApprovers();
		if(CollectionUtils.isEmpty(approvers)) {
//			String approverNullStrategy = instanceUnit.getApproverNullStrategy();
			String approverNullStrategy = post.getOwner().getApproverNull();
			log.info("流程=" + instanceUnit + ", 环节=" + currentAc + ", 岗位=" + post + "的审批人为空，进行为空处理：");
			
			//允许起发，跳过，并显示该行
			if(ApproverNullStrategy.START_SKIP_DISPLAY.getValue().equals(approverNullStrategy)) {
				log.info("审批人为空配置=跳过");
				this.setCurrentAc(currentAc);
				this.setCurrentPost(post);
				next(instanceUnit, post);	//处理竞争、并发、串行多种情况
				log.info("跳过完成");
				
				//允许起发，跳过，不显示该行
			} else if(ApproverNullStrategy.START_SKIP_DISPLAY_NONE.getValue().equals(approverNullStrategy)) {
				//在发起后已处理人为空不显示的情况！！！
				
				//允许发起，挂起
			} else if(ApproverNullStrategy.START_HUANGUP.getValue().equals(approverNullStrategy)) {
				log.info("审批人为空配置=挂起");
				instanceUnit.setStatus(InstanceStatus.HANGUP.getValue());
				post.setPostStatus(PostStatus.HANGUP.getValue());	//放行时使用
				return true;
				
				//其他非法值或<不允许发起时>流程发起时没有检查
			} else {
				throw new FlowException("其他非法值或<不允许发起时>流程发起时没有检查");
			}
		}
		return false;
	}

	/**
	 * 点亮某一审批人
	 * 如果此人自动通过标志为true，则此人自动完成，且寻找下一人点亮！！！TODO zhangdaoqiang 待重构
	 * 
	 * @param approver
	 * @throws Exception 
	 */
	protected void turnOn(ApproverUnit approver) throws Exception {
		
		log.info("进入点亮审批人的处理：groupId=" + approver.getId() + ", name=" + approver.getApproverName());
		
		//针对某一审批人，如果代理与监控同时存在，先处理监控，如果挂起，则不再处理代理，如果是提醒，再往下处理代理！
		monitorHandle(approver, null, FlowMonitorPoint.TODO_RECEIVE);
//		boolean processIsHangup = monitorHandle(approver, null, FlowMonitorPoint.TODO_RECEIVE);
//		if(processIsHangup) {
//			return ;
//		}
		
		/* 代理处理：
		 * 1、同岗多人，代理人与被代理人岗位不同时，代理人岗位不显示，维持原有岗位不变
		 * 2、只处理数据模型，针对代理的审批逻辑在审批时处理
		 * 3、四种代理方式，模型处理逻辑相同，针对代理的审批逻辑不同！！！
		 * 4、在流程发起时临时指定的审批人，不进行代理
		 * 5、调整环节时增加的岗位与人？？？
		 */
		ACUnit acUnit = approver.getOwner().getOwner();
		if(!acUnit.isAddLabel()) {	//发起时指定审批人不走代理
			log.info("对审批人进行代理处理：isAddLabel=" + acUnit.isAddLabel());
			proxyHandle(approver);
			
		} else {
			turnOnSelf(approver);
		}
		
	}
	
	private void proxyHandle(ApproverUnit approver) throws Exception {
		AgentService agentService = service.getAgentService();
		String postId = approver.getOwner().getPostId();
		String flCode = approver.getOwner().getOwner().getOwner().getFlCode();
		Agent agent = agentService.queryAgentBy(approver.getApproverId(), postId, flCode);
		if(agent != null) {
			if(ProxyType.COPY.getValue().equals(agent.getProxyType())) {
				proxyHandleCopy(approver, agent);
				
			} else if(ProxyType.CUT.getValue().equals(agent.getProxyType())) {
				proxyHandleCut(approver, agent);
				
			} else if(ProxyType.ADD_AC_BEFORE.getValue().equals(agent.getProxyType())) {
				proxyHandleAddBefore(approver, agent);
				
			} else if(ProxyType.ADD_AC_AFTER.getValue().equals(agent.getProxyType())) {
				proxyHandleAddAfter(approver, agent);
				
			}
			
		} else {
			turnOnSelf(approver);
		}
	}

	/**
	 * 针对指定审批人做监控处理
	 * 
	 * @param approver：
	 * @param flId：流程模板ID
	 * @param monitorPoint: 监控点
	 * @return true:流程挂起， false:流程正常
	 * @throws Exception 
	 */
	protected void monitorHandle(ApproverUnit approver, String flId, FlowMonitorPoint monitorPoint) {
		MonitorSettingService monitorService = service.getMonitorSettingService();

		
		List<FlowMonitorBean> monitors = null;
		//按人员查询
		if(approver != null) {
			//监控对协办转办不起作用，因为协办或转办人的岗位（审批列表中）不是自身真实的岗位
			String postId = approver.getOwner().getPostId();
			monitors = monitorService.queryMonitorByUser(approver.getApproverId(), 
					postId, monitorPoint.getValue());
			log.info("监控类型为：人员监控，监控人=" + monitors);
			
			//按模板查询
		} else if(StringUtils.isNotEmpty(flId)) {
			monitors = monitorService.queryMonitorByFlow(flId, monitorPoint.getValue());
			log.info("监控类型为：模板监控，监控人=" + monitors);
			
			//按异常查询
		} else {
			monitors = monitorService.queryMonitorWhenException(monitorPoint.getValue());
			log.info("监控类型为：异常监控，监控人=" + monitors);
		}
		
		if(CollectionUtils.isNotEmpty(monitors)) {
//			InstanceUnit instanceUnit = approver.getOwner().getOwner().getOwner();
			InstanceUnit instanceUnit = this.getInstanceUnit();
			log.info("监控人不为空，进行监控处理，handle=" + monitors.get(0).getHandle());
			
			String mobileUrl = null;
			String mobileParam = null;
			
			//处理监控者的消息
			for(FlowMonitorBean monitor : monitors) {
				SysNoticeMsg msg = new SysNoticeMsg();
				UserDto user = new UserDto(monitor.getMonitorId(), monitor.getMonitorName());
				//发送待阅方法
				if(FlowMonitorHandleType.SEND_TOREAD.getValue().equals(monitor.getHandle())) {
					msg = createToReadMsg(instanceUnit, instanceUnit.getCustomFormURL(), user);
					log.info("发送待阅消息：" + msg);
					
					//待办和挂起发送待办消息
				} else {
					String url = "flow/runtime/approve/flow.html"
							+ "?instanceId=" + instanceUnit.getId()
							+ "&time=" + new Date().getTime();
					msg = service.getMsgService().newFlowMsg(user, "DB", instanceUnit.getName(), url, mobileUrl, mobileParam);
					log.info("发送待办消息：" + msg);
					
				}
				instanceUnit.getMessages().add(msg);
			}
			
			//如果是挂起
			if(FlowMonitorHandleType.FLOW_HANGUP.getValue().equals(monitors.get(0).getHandle())) {
				instanceUnit.setStatus(InstanceStatus.HANGUP.getValue());
				if(approver != null) {
					
					//将挂起标志设置在当前人上，当放行时，只需要设置当前人，然后执行next即可！！！
//					this.currentApprover.setStatus(ApproverStatus.HANGUP.getValue());		//控制放行逻辑
					approver.setStatus(ApproverStatus.HANGUP.getValue());	//控制页面行显示
					
				}
				log.info("由于监控原因流程挂起");
				throw new FlowException("由于监控原因流程挂起:monitor=" + monitors);
			}
		}
	}

	private void proxyHandleAddAfter(ApproverUnit approver, Agent agent) throws Exception {
		ApproverUnit proxy = newProxy(approver, agent);
		
		//挂载代理人
		List<ApproverUnit> approvers = proxy.getOwner().getApprovers();
		if(CollectionUtils.isEmpty(approvers)) {
			return ;
		}
		approvers.add(approver.getApproverSeq(), proxy);
		
		setPxOfApprovers(approvers);
		
		turnOnSelf(approver);
		proxy.setTask(new TaskUnit());	//点亮后加签时不需要生成任务！！！???
	}

	private void proxyHandleAddBefore(ApproverUnit approver, Agent agent) throws Exception {
		ApproverUnit proxy = newProxy(approver, agent);
		
		//挂载代理人
		List<ApproverUnit> approvers = proxy.getOwner().getApprovers();
		if(CollectionUtils.isEmpty(approvers)) {
			return ;
		}
		approvers.add(approver.getApproverSeq() - 1, proxy);
		
		setPxOfApprovers(approvers);
		
		turnOnSelf(proxy);
	}

	private void proxyHandleCut(ApproverUnit approver, Agent agent) throws Exception {
		ApproverUnit proxy = newProxy(approver, agent);
		String displayName = proxy.getApproverName() + "(代" + approver.getApproverName() + ")";
		proxy.setApproverName(displayName);
		
		//删除被代理人
		approver.setDbAction(2);
		
		//挂载代理人
		List<ApproverUnit> approvers = proxy.getOwner().getApprovers();
		if(CollectionUtils.isEmpty(approvers)) {
			return ;
		}
		approvers.add(approver.getApproverSeq(), proxy);
		
		setPxOfApprovers(approvers);
		
		turnOnSelf(proxy);
	}

	/**
	 * 流程代理复制处理
	 * 
	 * @param approver
	 * @throws Exception 
	 */
	private void proxyHandleCopy(ApproverUnit approver, Agent agent) throws Exception {
		ApproverUnit proxy = newProxy(approver, agent);
		
		//挂载代理人
		List<ApproverUnit> approvers = proxy.getOwner().getApprovers();
		if(CollectionUtils.isEmpty(approvers)) {
			return ;
		}
		approvers.add(approver.getApproverSeq(), proxy);	//依赖于原有的排序!
		
		//重新设置px
		setPxOfApprovers(approvers);
		
		//剩余审批个数+1
		int leftPerson = approver.getOwner().getLeftPerson();
		approver.getOwner().setLeftPerson(leftPerson + 1);
		
		turnOnSelf(approver);
		turnOnSelf(proxy);
	}

	private void setPxOfApprovers(List<ApproverUnit> approvers) {
		int index = 1;
		for(ApproverUnit approver : approvers) {
			
			//去除删除状态的审批人
			if(approver.getDbAction() != 2) {
				approver.setApproverSeq(index);
				index++;
			}
		}
	}

	private ApproverUnit newProxy(ApproverUnit approver, Agent agent) {
		ApproverUnit proxy = new ApproverUnit();
		proxy.setId(IDGenerator.getUUID());
		proxy.setApproverId(agent.getAuthorizedId());
		proxy.setApproverName(agent.getAuthorized());
		proxy.setAcPostId(approver.getAcPostId());
		proxy.setAutoPass(AutoPassType.NOT_PASS.getValue());
		proxy.setOwner(approver.getOwner());
		proxy.setProxyed(approver.getId());	//对被代理人的引用！
		proxy.setProxyType(agent.getProxyType());
		approver.setProxy(proxy.getId());	//对代理人的引用！
		approver.setProxyType(agent.getProxyType());
		proxy.setDbAction(1);
		
		TaskUnit task = new TaskUnit();
		task.setTaskId(IDGenerator.getUUID());
		task.setStartTime(new Timestamp(System.currentTimeMillis()));
//		task.setTaskStatus(TaskStatus.RUNNING.getValue());
		task.setTaskType(TaskType.APPROVER.getValue());
		task.setDbAction(1);
		proxy.setTask(task);
		
		//代理人与授权人的任务设置代理标志
		approver.getTask().setSource(TaskSource.AUTHORIZER.getValue());
		approver.getTask().setSourceId(agent.getId());
		task.setSource(TaskSource.PROXY.getValue());
		task.setSourceId(agent.getId());
		
		//TODO zhangdaoqiang 记录操作日志
		try {
			String taskId = approver.getTask().getTaskId();
			String groupId = approver.getId();
			String acId = approver.getOwner().getAcId();
			String instanceId = instanceUnit.getId();
			service.getInstanceLogService().saveLogData(instanceId, acId, groupId, taskId, 
					InstanceOperateType.AUTHEN_OTHERS.getValue(), approver.getApproverId(), null, null);
			
			service.getInstanceLogService().saveLogData(instanceId, acId, groupId, taskId, 
					InstanceOperateType.AGENT_OTHERS.getValue(), agent.getAuthorizedId(), null, null);
		} catch (Exception e) {
			throw new FlowException("记录流程日志异常：", e);
		}
		
		return proxy;
	}

	/**
	 * 点亮当前审批人本身，不处理代理情况
	 * 
	 * @param approver
	 * @throws Exception
	 */
	protected void turnOnSelf(ApproverUnit approver) throws Exception {
		if(approver == null) {
			return ;
		}
		
		TaskUnit task = approver.getTask();
		task.setTaskStatus(TaskStatus.RUNNING.getValue());
		task.setTaskType(TaskType.APPROVER.getValue());
		task.setStartTime(new Timestamp(System.currentTimeMillis()));
		task.setDbAction(1);
		
		//查询下一环节时，自动跳过的人查不出来，所以模拟下一步时不走自动跳过
		if(this instanceof EmptyOperation) {
			return ;
		}
		
		String currentApproverTypeId = approver.getOwner().getOwner().getApprovalTypeId();
		
		//发起、会签、校稿、核对、办理，是不参与重复人跳过的逻辑计算。即只有审批、审核参与跳过计算
//		if(!"HQ".equals(currentApproverTypeId) && !"JG".equals(currentApproverTypeId)
		if(("SP".equals(currentApproverTypeId) 
				|| "SH".equals(currentApproverTypeId))	
				&& approver.getAutoPass() == 1
				&& !approver.getOwner().getOwner().isAddLabel()) {	//非手选环节
			
			//设置当前位置
			setCurrentLocation(approver.getOwner().getOwner(), approver.getOwner(), approver);
			
			ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
			approvalDto.setOperationType(OperationType.AGREE.getCode());
			approvalDto.setOperationName("通过");

			//update by dgh on 2018/02/22 BEGIN
			//修改内容：将自动通过逻辑稍作修改以适应逾期自动通过功能
			//    具体以taskComments是否为空作为判断，如果为空则是原来逻辑否则即为逾期处理方式
			//approvalDto.setUserNote("(责任人相同，系统自动通过)");

			TaskUnit taskUnit = approver.getTask();
			String taskComments = taskUnit!=null?taskUnit.getTaskComments():null;
			if(taskComments==null){
				approvalDto.setUserNote("(责任人相同，系统自动通过)");
			}else{
				approvalDto.setUserNote(taskComments);
			}
			//update by dgh on 2018/02/22 END

			
			log.info("审批人相同，跳过处理：leftPerson=" + currentPost.getLeftPerson() 
				+ ", leftPost=" + currentAc.getLeftPost());
			
			//审批人重复跳过不同于审批人提交，此处将特殊情况挑出来后等同于提交！！！
			int leftPerson = currentPost.getLeftPerson() - 1;
			if(leftPerson > 0) {
				log.info("审批人相同，跳过处理：完成当前审批人");
				this.complate(approver, approvalDto);
				
			} else {
				
				if(currentAc.getLeftPost() - 1 > 0) {
					log.info("审批人相同，跳过处理：完成当前审批人与岗位");
					this.complate(approver, approvalDto);
					this.complate(currentPost);
					
					//人自动跳过时，顺序时执行一下 （TODO zhangdaoqing 点亮人的过程中岗位完成，导致跳到下一岗位）
					ConcurrentStrategy postStrategy = getPostStrategy(currentAc);
					if(postStrategy instanceof TogetherStrategy) {
						sortIn(currentAc);
					} else if(postStrategy instanceof SerialStrategy) {
						log.info("审批人相同，跳过处理：点亮下一岗位：");
						turnOnNextInSameAc(currentAc, currentPost, instanceUnit);
					}
					
				} else {
					next(this.instanceUnit, approvalDto);
				}
			}
		}
	}

	private void sortIn(ACUnit currentAc) {
		List<PostUnit> posts = currentAc.getPosts();
		if(CollectionUtils.isEmpty(posts)) {
			return;
		}
		
//		ArrayList<PostUnit> newPosts = new ArrayList<PostUnit>(posts);
//		Collections.sort(newPosts, new Comparator<PostUnit>() {
//
//			@Override
//			public int compare(PostUnit first, PostUnit second) {
//				String firstStatus = first.getPostStatus();
//				String secondStatus = second.getPostStatus();
//				if(firstStatus.equals(secondStatus)) {
//					return (first.getPostSeq() < second.getPostSeq())? -1 : 1;
//				} else {
//					return (PostStatus.FINISHED.getValue().equals(firstStatus))? -1 : 1;
//				}
//			}
//		});
//		posts = new CopyOnWriteArrayList<PostUnit>(newPosts);
		
		posts.sort(new Comparator<PostUnit>() {

			@Override
			public int compare(PostUnit first, PostUnit second) {
				String firstStatus = first.getPostStatus();
				String secondStatus = second.getPostStatus();
				if(firstStatus.equals(secondStatus)) {
					return (first.getPostSeq() < second.getPostSeq())? -1 : 1;
				} else {
					return (PostStatus.FINISHED.getValue().equals(firstStatus))? -1 : 1;
				}
			}
		});
		
		for(int i=0; i<posts.size(); i++) {
			posts.get(i).setPostSeq(i+1);
		}
	}

	protected void complate(PostUnit currentPost) {
		currentPost.setPostStatus(PostStatus.FINISHED.getValue());
		currentPost.setEndTime(new Timestamp(System.currentTimeMillis()));
		
		int leftPost = currentPost.getOwner().getLeftPost();
		currentPost.getOwner().setLeftPost(leftPost - 1);
	}

	private void turnOnNextInSamePost(PostUnit currentPost, ApproverUnit currentApprover) throws Exception {
		List<ApproverUnit> approvers = currentPost.getApprovers();
		if(CollectionUtils.isEmpty(approvers)) {
			return ;
		}
		for(int i=0; i<approvers.size(); i++) {
			if(approvers.get(i).getApproverId().equals(currentApprover.getApproverId())) {
				ApproverUnit next = approvers.get(i + 1);
				turnOn(next);
				break;
			}
		}
		
	}

	protected void sortIn(PostUnit currentPost) {
		List<ApproverUnit> approvers = currentPost.getApprovers();
		if(CollectionUtils.isEmpty(approvers)) {
			return ;
		}
		
//		ArrayList<ApproverUnit> newApprovers = new ArrayList<ApproverUnit>(approvers);
//		Collections.sort(newApprovers, new Comparator<ApproverUnit>() {
//
//			@Override
//			public int compare(ApproverUnit first, ApproverUnit second) {
//				String firstStatus = first.getTask().getTaskStatus();
//				String secondStatus = second.getTask().getTaskStatus();
//				if(firstStatus.equals(secondStatus)) {
//					return (first.getApproverSeq() < second.getApproverSeq())? -1 : 1;
//				} else {
//					return (TaskStatus.FINISHED.getValue().equals(firstStatus))? -1 : 1;
//				}
//			}
//		});
//		approvers = new CopyOnWriteArrayList<ApproverUnit>(newApprovers);
		
		approvers.sort(new Comparator<ApproverUnit>() {

			@Override
			public int compare(ApproverUnit first, ApproverUnit second) {
				String firstStatus = first.getTask().getTaskStatus();
				String secondStatus = second.getTask().getTaskStatus();
				if(firstStatus.equals(secondStatus)) {
					return (first.getApproverSeq() < second.getApproverSeq())? -1 : 1;
				} else {
					return (TaskStatus.FINISHED.getValue().equals(firstStatus))? -1 : 1;
				}
			}
		});
		
		setPxOfApprovers(approvers);
	}

	protected void complate(ApproverUnit approver, ApprovalSubmitDto approvalDto) throws Exception {
		TaskUnit task = approver.getTask();
		task.setTaskStatus(TaskStatus.FINISHED.getValue());
		task.setEndTime(new Timestamp(System.currentTimeMillis()));
		task.setTaskResult(approvalDto.getOperationType());
		task.setTaskResultName(approvalDto.getOperationName());
		task.setTaskComments(approvalDto.getUserNote());
		
		int leftPerson = approver.getOwner().getLeftPerson();
		approver.getOwner().setLeftPerson(leftPerson - 1);
		
		//处理由他发起的协办人
		complateAssistersOf(approver, approvalDto);
		
		//如果当前审批人配置了监控人，则发送待阅消息
		this.monitorHandle(approver, null, FlowMonitorPoint.TODO_DONE);
	}	
	
	private void complateAssistersOf(ApproverUnit approver, ApprovalSubmitDto approvalDto) {
		String assisterId = approver.getTask().getFromId();
		if(StringUtils.isNotEmpty(assisterId)) {
			List<ApproverUnit> approvers = approver.getOwner().getApprovers();
			for(ApproverUnit current : approvers) {
				if(TaskStatus.RUNNING.getValue().equals(current.getTask().getTaskStatus())
						&& StringUtils.isNotEmpty(current.getTask().getFromId())
						&& current.getTask().getFromId().equals(approver.getApproverId())
						&& !current.getTask().getFromId().equals(current.getApproverId())) {
					TaskUnit task = current.getTask();
					task.setTaskStatus(TaskStatus.FINISHED.getValue());
					task.setEndTime(new Timestamp(System.currentTimeMillis()));
					task.setTaskResult(approvalDto.getOperationType());
					task.setTaskResultName(approvalDto.getOperationName());
					task.setTaskComments("流程已被协办人处理！");
					task.setDbAction(2);
					current.setDbAction(2);
					InstanceUnit instanceUnit = approver.getOwner().getOwner().getOwner();
					instanceUnit.getMessagesToDel().add(current.getTask().getMsgId());
				}
			}
		}
		
	}

	public boolean save(InstanceUnit instanceUnit) throws Exception {
		
		//1、更新流程实例状态
		updateInstance(instanceUnit);
		
		//2、更新流程环节状态或顺序
		updateAc(instanceUnit);
		
		//3、更新岗位状态或顺序
		updatePost(instanceUnit);
		
		//4、更新人员及任务
		updateApprover(instanceUnit);
		
		return true;
	}
	
	/**
	 * 处理流程操作的消息
	 * 1、发送待办消息
	 * 2、待办转已办消息
	 * 3、撤回消息
	 * 
	 * @param instanceUnit
	 * @throws Exception
	 */
	public void handleMessages(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto) throws Exception {
		sendMessages(instanceUnit.getMessages());
		completeMessage(instanceUnit, approvalDto.getMsgId());
		withDrawMessage(instanceUnit);
	}
	
	protected void sendMessages(List<SysNoticeMsg> messages) throws Exception {
		if(CollectionUtils.isNotEmpty(messages)) {
			//service.getMsgService().saveBatch(messages);
			service.getMsgService().batchSaveAndNotifyOthers(messages);
			log.info("发送待办消息成功！");
		}
	}
	
	protected void completeMessage(InstanceUnit instanceUnit, String msgId) throws Exception {
		if(StringUtils.isEmpty(msgId)) {
			return ;
		}
		service.getMsgService().completeMessage(msgId, "YB");
		log.info("待办消息已完成：" + msgId);
		//TODO zhangdaoqiang 记录操作日志
		try {
			if(currentAc == null || currentApprover == null) {
				service.getInstanceLogService().changeToDoIntoHaveDone(instanceUnit.getId(),  null, null, null, null, null, null);
				
			} else {
				String operateContent = "approveType:" + currentAc.getApprovalTypeId()+ ",operationType:" + approvalDto.getOperationType();
//				 operateContent = currentAc.getApprovalTypeId() + "," + currentApprover.getTask().getTaskResult() + "," + currentApprover.getTask().getTaskComments();
				service.getInstanceLogService().changeToDoIntoHaveDone(instanceUnit.getId(), 
						currentAc.getAcId(),currentApprover.getId(), currentApprover.getTask().getTaskId(), 
						currentApprover.getApproverId(), operateContent, null);
			}
		} catch (Exception e) {
			throw new FlowException("记录流程日志异常：", e);
		}
	}
	
	protected void withDrawMessage(InstanceUnit instanceUnit) throws Exception {
		List<String> msgIds = instanceUnit.getMessagesToDel();
		if(CollectionUtils.isNotEmpty(msgIds))  {
			log.info("--- withDrawMessage=" + JacksonUtils.toJson(msgIds) );
			getService().getMsgService().deletePseudoAllObjectByIds(msgIds);
			
			//同步旧OA消息：删除待办
			getService().getMsgService().deleteTodo(msgIds);
			
			//TODO zhangdaoqiang 记录操作日志
			try {
				String acId = (currentAc == null)? "": currentAc.getAcId();
				String approverId = (currentApprover == null)? "": currentApprover.getId();
				String taskId = (currentApprover == null)? "": currentApprover.getTask().getTaskId();
				String operatorIds = (currentApprover == null)? "": currentApprover.getApproverId();
				service.getInstanceLogService().deleteOperateLogBySpecialAction(instanceUnit.getId(),
						acId, approverId, taskId, operatorIds);
				
			} catch (Exception e) {
				throw new FlowException("记录流程日志异常：", e);
			}
		}
	}

	private boolean updateApprover(InstanceUnit instanceUnit) throws Exception {
		InstanceGroupService groupService = service.getInstanceGroupService();
		InstanceTaskService taskService = service.getInstanceTaskService();
		for(ACUnit acUnit : instanceUnit.getAcList()) {
			if(CollectionUtils.isEmpty(acUnit.getPosts())) {
				continue;
			}
			for(PostUnit postUnit : acUnit.getPosts()) {
				if(CollectionUtils.isEmpty(postUnit.getApprovers())) {
					continue;
				}
				for(ApproverUnit approver : postUnit.getApprovers()) {
					
					//4、更新人员顺序(人员的状态在对应的任务上！)
					if(approver.isChange()) {
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("px", approver.getApproverSeq());
						params.put("id", approver.getId());
						
						//被代理人信息更新
						params.put("proxy", approver.getProxy());
						params.put("proxyType", approver.getProxyType());
						params.put("status", approver.getStatus());
						
						
						groupService.update(InstanceGroup.class.getName() + ".updatePx", params);
						log.info("人员更新成功：id=" + approver.getId() + ", name=" + approver.getApproverName());
						
						//5、更新任务状态（complete）
						Map<String, Object> taskParams = new HashMap<String, Object>();
						TaskUnit task = approver.getTask();
						taskParams.put("status", task.getTaskStatus());
						taskParams.put("operationCode", task.getTaskResult());
						taskParams.put("operationName", task.getTaskResultName());
						taskParams.put("endDate", task.getEndTime());
						taskParams.put("userNote", task.getTaskComments());
						taskParams.put("taskId", task.getTaskId());
						taskParams.put("source", task.getSource());
						taskParams.put("sourceId", task.getSourceId());
						taskParams.put("msgId", task.getMsgId());
						int updateCnt = taskService.update(InstanceTask.class.getName() + ".completeTask", taskParams);
						log.info("更新任务成功： taskId=" + task.getTaskId() + ", updateCnt=" + updateCnt);
					}
					
					//6、新增人员（协办或转办时）
					if(approver.getDbAction() == 1) {
						InstanceGroup newPerson = new InstanceGroup();
						newPerson.setId(approver.getId());
						newPerson.setAcPostId(approver.getAcPostId());
						newPerson.setPx((long) approver.getApproverSeq());
						newPerson.setParticipantId(approver.getApproverId());
						newPerson.setParticipantName(approver.getApproverName());
						newPerson.setAcPostId(postUnit.getId());
						newPerson.setAutoPass(approver.getAutoPass());
						newPerson.setAcId(acUnit.getAcId());
						newPerson.setPostId(postUnit.getPostId());
						newPerson.setStatus(approver.getStatus());
						
						//代理人保存
						newPerson.setProxy(approver.getProxy());
						newPerson.setProxyed(approver.getProxyed());
						newPerson.setProxyType(approver.getProxyType());
						
						groupService.save(newPerson);
						log.info("新增人员成功：" + approver.getApproverName());
					}
					
					//7、新增任务（new）
					if(approver.getTask().getDbAction() == 1) {
						InstanceTask newTask = new InstanceTask();
						newTask.setId(approver.getTask().getTaskId());
						newTask.setActivateDate(approver.getTask().getStartTime());
						newTask.setApproverId(approver.getApproverId());		//！！！
						newTask.setApproverName(approver.getApproverName());
						newTask.setType(approver.getTask().getTaskType());
						newTask.setGroupId(approver.getId());
						newTask.setStatus(approver.getTask().getTaskStatus());
						newTask.setRelationParticipantId(approver.getTask().getFromId());
						newTask.setRelationParticipant(approver.getTask().getFromName());
						newTask.setSource(approver.getTask().getSource());
						newTask.setSourceId(approver.getTask().getSourceId());
						
						//仅开始节点任务时这三个字段有值
						newTask.setOperationCode(approver.getTask().getTaskResult());
						newTask.setOperationName(approver.getTask().getTaskResultName());
						newTask.setUserNote(approver.getTask().getTaskComments());
						newTask.setEndDate(approver.getTask().getEndTime());
						newTask.setActivateDate(approver.getTask().getStartTime());
						newTask.setXbStartDate(approver.getTask().getXbStartDate());
						newTask.setMsgId(approver.getTask().getMsgId());
						
						//创建待办消息
						if(TaskStatus.RUNNING.getValue().equals(newTask.getStatus())) {
							if(OperationType.ASSIST.getCode().equals(this.getApprovalDto().getOperationType()))  {
                               if(TaskType.ASSIST.getValue().equals(approver.getTask().getTaskType())){
                            	   String msgId = newMessage(instanceUnit, approver, acUnit.getApprovalTypeId());
                                   newTask.setMsgId(msgId);
                               }
                            }else{
                            	 String msgId = newMessage(instanceUnit, approver, acUnit.getApprovalTypeId());
                                 newTask.setMsgId(msgId);
                            }
						}
						//当自动通过时是新增任务但不发送消息
						
						taskService.save(newTask);
						log.info("新增任务成功： taskId=" + newTask.getId() + ", groupId = " + newTask.getGroupId()
						+ ", name=" + newTask.getApproverName());
					}
					
					//8、删除人员（竞争时跳过）
					if(approver.getDbAction() == 2) {
						groupService.deletePseudoObjectById(approver.getId());
						log.info("删除人员成功：" + approver.getId());
					}
					
					//9、删除任务（与人员删除一致）
					if(approver.getTask().getDbAction() == 2) {
						taskService.deletePseudoObjectById(approver.getTask().getTaskId());
						log.info("删除任务成功" + approver.getTask().getTaskId());
						
						if(StringUtils.isNotEmpty(approver.getTask().getTaskType())){
							//我的发起 消息 不删
							if(TaskType.STARTER.getValue().equals(approver.getTask().getTaskType()) && StringUtils.isEmpty(approver.getTask().getFromId())){
								
							}else{
								//删除对应的消息
								service.getMsgService().deletePseudoObjectById(approver.getTask().getMsgId());
							}
						}

						//同步旧OA消息：删除待办	TODO 待重构
						List<String> msgIds = new ArrayList<String>();
						msgIds.add(approver.getTask().getMsgId());
						getService().getMsgService().deleteTodo(msgIds);
					}
				}
			}
		}
		return true;
	}

	public String newMessage(InstanceUnit instanceUnit, ApproverUnit approver, String approveType) {
		String mobileUrl = "mobile/approve/approve_detail.html";
		MobileParam mobileParamBean = new MobileParam();
		mobileParamBean.setInstanceId(instanceUnit.getId());
		mobileParamBean.setBusinessId(instanceUnit.getBusinessId());
		mobileParamBean.setAppId(instanceUnit.getAppId());
		mobileParamBean.setTypeCode(approveType);
		if(approver.getTask() != null) {
			mobileParamBean.setTaskId(approver.getTask().getTaskId());
			mobileParamBean.setApproveRole(approver.getTask().getTaskType());
		}
		
		TaskUnit task = approver.getTask();
		String msgType = "DB";
		String url = "flow/runtime/approve/flow.html"
				+ "?instanceId=" + instanceUnit.getId()
				+ "&taskId=" + task.getTaskId()
				+ "&time=" + new Date().getTime();
		UserDto user = new UserDto(approver.getApproverId(), approver.getApproverName());
		SysNoticeMsg message = service.getMsgService().newFlowMsg(user, msgType, instanceUnit.getName(), 
				url, mobileUrl, JacksonUtils.toJson(mobileParamBean));
		instanceUnit.getMessages().add(message);
		log.info("待办消息成功：" + message);
		
		//记录操作日志 TODO 操作日志方案 待重构
		String logType = InstanceOperateType.TO_DO.getValue();
		Map<String, String> orgInfos = this.service.queryOrgBy(approver.getOwner().getPostId());
		String companyId = orgInfos.get("companyId");
		String deptId = orgInfos.get("deptId");
		String projectId = orgInfos.get("projectId");
		String branchId = orgInfos.get("branchId");
		try {
			//格式：approveType:SP,operationType:TY
			String operateContent = null;
			String operatorGoupId = null;
			String operatorId = null;
			if(currentApprover == null) {
				operateContent = "approveType:" + approveType+ ",operationType:" + this.getType();	//管理员操作
				operatorId = LoginUtils.getSecurityUserBeanInfo().getSecurityUserDto().getId();
			} else {
				operateContent = "approveType:" + approveType+ ",operationType:" + approvalDto.getOperationType();
				operatorId = approver.getApproverId();
				operatorGoupId = currentApprover.getId();
			}
			service.getInstanceLogService().saveLogData(instanceUnit.getId(), approver.getOwner().getAcId(), 
					operatorGoupId, task.getTaskId(), logType, operatorId, 
					companyId, deptId, projectId, branchId, operateContent, null);
		} catch (Exception e) {
			throw new FlowException("记录流程日志异常：", e);
		}
		
		return message.getId();
	}
	
	protected void createToReadMsg(List<UserDto> ccPerson, InstanceUnit instanceUnit) {
		if(CollectionUtils.isEmpty(ccPerson)) {
			return ;
		}
		for(UserDto user : ccPerson) {
			SysNoticeMsg message = createToReadMsg(instanceUnit, instanceUnit.getCustomFormURL(), user);
			instanceUnit.getMessages().add(message);
			
			//记录操作日志
			try {
				service.getInstanceLogService().saveLogData(instanceUnit.getId(), currentAc.getAcId(), 
						null, null, InstanceOperateType.TO_READ.getValue(), user.getId(), null, null);
			} catch (Exception e) {
				throw new FlowException("记录流程日志异常：", e);
			}
		}
	}

	protected SysNoticeMsg createToReadMsg(InstanceUnit instanceUnit, String pcUrl, UserDto user) {
		String mobileUrl = "mobile/approve/approve_detail.html";
		Map<String, String> map = new HashMap<String, String>();
		map.put("instanceId", instanceUnit.getId());
		map.put("businessId", instanceUnit.getBusinessId());
		map.put("flCode", instanceUnit.getFlCode());
		map.put("appId", instanceUnit.getAppId());
		String mobileParam = JacksonUtils.toJson(map);
		String url = "/flow/runtime/approve/flow.html"
				+ "?instanceId=" + instanceUnit.getId()
				+ "&time=" + new Date().getTime();
		SysNoticeMsg message = service.getMsgService().newFlowMsg(user, "DY", instanceUnit.getName(), url, mobileUrl, mobileParam);
		return message;
	}
	
	private SysNoticeMsg createToDoMsg(UserDto user, String url, InstanceUnit instanceUnit) {
		String mobileUrl = null;
		String mobileParam = null;
		String newUrl = url + "?instanceId=" + instanceUnit.getId()
				+ "&time=" + new Date().getTime();
		SysNoticeMsg message = service.getMsgService().newFlowMsg(user, "DB", instanceUnit.getName(), newUrl, mobileUrl, mobileParam);
		return message;
	}

	private boolean updatePost(InstanceUnit instanceUnit) {
		InstancePostDao postDao = service.getInstancePostDao();
		for(ACUnit acUnit : instanceUnit.getAcList()) {
			if(CollectionUtils.isEmpty(acUnit.getPosts())) {
				continue;
			}
			for(PostUnit postUnit : acUnit.getPosts()) {
				if(postUnit.isChange()) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("status", postUnit.getPostStatus());
					params.put("activateDate", postUnit.getStartTime());
					params.put("endDate", postUnit.getEndTime());
					params.put("px", postUnit.getPostSeq());
					params.put("id", postUnit.getId());
					params.put("leftPerson", postUnit.getLeftPerson());
					int updateCnt = postDao.update(InstancePost.class.getName() + "." + SYNC, params);
					log.info("岗位【" + postUnit.getPostName() + "】更新成功：status="
					+ postUnit.getPostStatus() + ", updateCnt=" + updateCnt);
				}
				if(postUnit.getDbAction() == 2) {
					postDao.deletePseudoObjectById(postUnit.getId());
					log.info("删除岗位成功：" + postUnit.getId());
					
				}
				
				if(postUnit.getDbAction() == 1) {
					InstancePost post = new InstancePost();
					post.setId(postUnit.getId());
					post.setAcId(postUnit.getAcId());
					post.setActivateDate(postUnit.getStartTime());
					post.setLeftPerson(postUnit.getLeftPerson());
					post.setPostId(postUnit.getPostId());
					post.setPostName(postUnit.getPostName());
					post.setPx((long) postUnit.getPostSeq());
					post.setStatus(postUnit.getPostStatus());
					
					postDao.save(post);
				}
			}
		}
		return true;
	}

	private boolean updateAc(InstanceUnit instanceUnit) throws Exception {
		InstanceAcService acService = service.getInstanceAcService();
		List<ACUnit> acList = instanceUnit.getAcList();
		for(ACUnit acUnit : acList) {
			
			//新增AC
			if(acUnit.getDbAction() == 1) {
				InstanceAc ac = new InstanceAc();
				ac.setId(acUnit.getAcId());
				ac.setCode(acUnit.getAcCode());
				ac.setName(acUnit.getAcName());
				ac.setFiId(acUnit.getOwner().getId());
				ac.setApproveStrategy(acUnit.getMultiPost());
				ac.setPostMultiPerson(acUnit.getMultiPerson());
				ac.setApproveTypeId(acUnit.getApprovalTypeId());
				ac.setLeftPost(acUnit.getLeftPost());
				ac.setPx((long) acUnit.getAcPx());
				ac.setStatus(acUnit.getAcStatus());
				ac.setAcType(acUnit.getAcType());
				ac.setIsAddLabel(acUnit.isAddLabel());
				ac.setSource(acUnit.getSource());
				ac.setPreAcIds(acUnit.getPreAcIds());
				ac.setNextAcIds(acUnit.getNextAcIds());
				ac.setFromReturn(acUnit.getFromReturn());
				ac.setReturnPx(acUnit.getReturnPx());
				acService.save(ac);
				
				if("2".equals(ac.getAcType()) && CollectionUtils.isNotEmpty(acUnit.getPosts())
						&& acUnit.getLeftPost() == 0) {
					log.error("流程发起时计算节点下剩余岗位数异常：instanceId=" + instanceUnit.getId()
					+ ", acId=" + acUnit.getAcId());
				}
				
			} else if(acUnit.getDbAction() == 2) {
//				acService.deletePseudoObjectById(acUnit.getAcId());
				acService.deleteObjectById(acUnit.getAcId());	//在#15486，修改当前审批人时主键重复 
				
			} else if(acUnit.isChange()) {//更新AC
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("status", acUnit.getAcStatus());
				params.put("activateDate", acUnit.getAcStartTime());
				params.put("endDate", acUnit.getAcEndTime());
				params.put("px", acUnit.getAcPx());
				params.put("acId", acUnit.getAcId());
				params.put("leftPost", acUnit.getLeftPost());
				params.put("preAcIds", acUnit.getPreAcIds());
				params.put("nextAcIds", acUnit.getNextAcIds());
				params.put("returnPx", acUnit.getReturnPx());
				int updateCnt = acService.update(InstanceAc.class.getName() + "." + SYNC, params);	// TODO zhangdaoqiang 待优化
				log.info("环节【" + acUnit.getAcId() + ", " + acUnit.getAcName() + "】更新成功：status="
				+ acUnit.getAcStatus() + ", updateCnt=" + updateCnt);
			}
		}
		return true;
	}

	private boolean updateInstance(InstanceUnit instanceUnit) {
		if(instanceUnit.isChange()) {
			InstanceDao instanceDao = service.getInstanceDao();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", instanceUnit.getStatus());
//			params.put("startDate", instanceUnit.getStartDate());
			params.put("endDate", instanceUnit.getEndDate());
			params.put("instanceId", instanceUnit.getId());
			params.put("returnRepeatApproval", instanceUnit.getReturnRepeatApproval());
			params.put("flUpdateDate", instanceUnit.getFlUpdateDate());
			List<String> currentApprovers = instanceUnit.getCurrentApprovers();
			List<String> currentApproverIds = instanceUnit.getCurrentApproverIds();
			params.put("currentApprovers", CollectionUtils.isEmpty(currentApprovers) ? "" : String.join(",", currentApprovers));
			params.put("currentApproverIds", CollectionUtils.isEmpty(currentApproverIds) ? "" : String.join(",", currentApproverIds));
			instanceDao.update(Instance.class.getName() + "." + SYNC, params);
		}
		
		log.info("流程实例更新成功：instanceId=" + instanceUnit.getId() + ", status=" + instanceUnit.getStatus());
		return true;
	}
	
	/**
	 * 根据任务ID，设置当前审批人、当前岗位、当前节点
	 * 
	 * @param instanceUnit
	 * @param approvalDto
	 */
	public void setCurrentLocation(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto){
		
		//自动通过用
		this.setInstanceUnit(instanceUnit);
		this.setApprovalDto(approvalDto);
		Integer approvalCount=0;
		//update by dgh begin自动任务执行时当前提交人不是从session中获取的情况
		String currentUserId = approvalDto.getCurrentSubmitUserId();
		currentUserId = currentUserId==null?LoginUtils.getSecurityUserBeanInfo().getSecurityUserDto().getId():currentUserId;
		//update by dgh end自动任务执行时当前提交人不是从session中获取的情况
		String curentTaskId = approvalDto.getTaskId();
		if(curentTaskId!=null && !"".equals(curentTaskId)){
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("instanceId", instanceUnit.getId());
			paramMap.put("approvalId", currentUserId);
			approvalCount=service.getInstanceTaskService().queryListCountByFiId(paramMap);
		}
		
		for(ACUnit acUnit : instanceUnit.getAcList()) {
			List<PostUnit> posts = acUnit.getPosts();
			if(CollectionUtils.isEmpty(posts)) {
				continue;
			}
			for(PostUnit postUnit : posts) {
				List<ApproverUnit> approvers = postUnit.getApprovers();
				if(CollectionUtils.isEmpty(approvers)) {
					continue;
				}
				for(ApproverUnit approver : approvers) {
					TaskUnit task = approver.getTask();
					if(task == null) {
						continue;
					}
					
					//以点亮行审批人ID确认当前审批人（默认一个人同时只有一个点亮行）
					boolean bingo = false;
//					if(StringUtils.isEmpty(curentTaskId)) {
						if(TaskStatus.RUNNING.getValue().equals(task.getTaskStatus())) {
							if(approver.getApproverId().equals(currentUserId)) {
								if(approvalCount>1){//zfz解决当同一个人有多个任务处于运行中时点亮相应的任务而不是第一个
									if(curentTaskId.equals(task.getTaskId())){
										bingo = true;
									}
								}else{
									bingo = true;
								}
							}
						}
						
						//撤回任务成匹配不到当前人
						if(!bingo) {
							if(StringUtils.isNotEmpty(task.getTaskId())
									&& task.getTaskId().equals(curentTaskId)) {
								if(TaskStatus.RUNNING.getValue().equals(task.getTaskStatus())) {
									bingo = true;
								}
								
								if(TaskStatus.FINISHED.getValue().equals(task.getTaskStatus())
										&& OperationType.WITHDRAW_TASK.getCode().equals(this.getType().getCode())) {
									bingo = true;
								}
							}
						}
						
//					} else {
//						//以任务ID确认当前审批人
						//由于存在前后两个环节同一人的情况，当前一节点审批后，再直接审批时存在问题，所以不再以任务ID确定当前位置
//						if(curentTaskId.equals(task.getTaskId())) {
//							bingo = true;
//							
//							//在流转中currentAc可能被 重新赋值，此处保存提交时的审批类型（业务上），校稿用
////						approvalDto.setApprovalTypeId(currentAc.getApprovalTypeId());
////						approvalDto.setCategoryId(currentApprover.getId());	//页面上以用户的groupKey作为附件的categoryId
//						} else {
//							if(TaskStatus.RUNNING.getValue().equals(task.getTaskStatus())) {
//								if(approver.getApproverId().equals(currentUserId)) {
//									bingo = true;
//								}
//							}
//						}
//					}
					
					if(bingo) {
						currentAc = acUnit;
						currentPost = postUnit;
						currentApprover = approver;
						
						//当从非待办打开的flow.html时,taskId, msgId为空，此时审批无法消除待办
						if(StringUtils.isEmpty(approvalDto.getTaskId())) {
							approvalDto.setTaskId(currentApprover.getTask().getTaskId());
						}
						String msgId = currentApprover.getTask().getMsgId();
						if(StringUtils.isEmpty(approvalDto.getMsgId())) {
							approvalDto.setMsgId(msgId);
						} else {
							if(StringUtils.isNotEmpty(msgId) && !msgId.equals(approvalDto.getMsgId())) {
								approvalDto.setMsgId(msgId);	//以DB中查出的为准
							}
						}
						
						return ;
					}
				}
			}
		}
	}
	
	protected void setCurrentLocation(ACUnit acUnit, PostUnit postUnit, ApproverUnit approverUnit) {
		this.setCurrentAc(acUnit);
		this.setCurrentPost(postUnit);
		this.setCurrentApprover(approverUnit);
	}
	
	/**
	 * 给业务系统发送流程状态变更
	 * 
	 * @param instanceUnit
	 */
	protected void noticeBusinessSystem(InstanceUnit instanceUnit, ApprovalSubmitDto submitDto) {
		SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", instanceUnit.getBusinessId());
		params.put("businessObjectCode", instanceUnit.getBusinessObjectCode());
		params.put("status", instanceUnit.getStatus());
		params.put("instanceId", instanceUnit.getId());
		params.put("categoryId", submitDto.getCategoryId());
		params.put("flCode", instanceUnit.getFlCode());
		String token = userInfo.getSecurityUserDto().getLoginName() + "@" + userInfo.getTendCode();
		params.put("token", token);
		BusinessObject businessObject = new BusinessObject();
		try {
			businessObject = service.getBusinessObjectService().getObjectById(instanceUnit.getBusinessObjectId());
		} catch (Exception e1) {
			throw new FlowException(e1);
		}
		
		//往redis中设置当前用户信息
		SecurityUserBeanRelationInfo relationInfo = new SecurityUserBeanRelationInfo();
		service.setLoginInfo(token, JacksonUtils.toJson(userInfo), JacksonUtils.toJson(relationInfo));
		
		try {
			log.info("通知业务系统流程状态变更：" + params + ", userInfo=" + JacksonUtils.toJson(userInfo));
			String url=businessObject.getCallbackClass();
			String updateDubboResultInfo = LoginUtils.httpPost(url, JacksonUtils.toJson(params));
			log.info("业务系统返回值：" + updateDubboResultInfo);
			DubboServiceResultInfo updateDubboServiceResultInfo = JacksonUtils.fromJson(updateDubboResultInfo,
					DubboServiceResultInfo.class);
			if (updateDubboServiceResultInfo.isSucess()) {
				log.info("通知成功：流程ID=" + instanceUnit.getId() + "状态=" + instanceUnit.getStatus() + ", bizId=" + instanceUnit.getBusinessId());
			} else {
				//TODO 通知业务系统失败记录重发
				// postUrl:businessObject.getCallbackClass(),webService,webServiceMethod,postParam:params,userInfoJson,postType:httpClient
				saveFailMsg(businessObject.getCallbackClass(),JacksonUtils.toJson(params),"","","httpClient",JacksonUtils.toJson (userInfo));
				log.info("通知失败：流程ID=" + instanceUnit.getId() + "状态=" + instanceUnit.getStatus() + ", bizId=" + instanceUnit.getBusinessId());
			}
		}catch (Exception e) {
			//TODO 通知业务系统失败记录重发
			// postUrl:businessObject.getCallbackClass(),webService,webServiceMethod,postParam:params,userInfoJson,postType:httpClient
			saveFailMsg(businessObject.getCallbackClass(),JacksonUtils.toJson(params),"","","httpClient",JacksonUtils.toJson (userInfo));
			throw new FlowException("通知业务系统流程状态失败：URL=" + businessObject.getCallbackClass() + ";参数=" + params  + ", bizId=" + instanceUnit.getBusinessId(), e);
		}
	}
	
	/**
	 * 将正常结束且能归档的流程加入全文检索系统
	 * 
	 * @param instanceUnit
	 */
	@SuppressWarnings("unchecked")
	protected void addInstanceInfoIntoContentSearch(InstanceUnit instanceUnit) {
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
		String instanceId = instanceUnit.getId();
		String url = "platform-app/flow/runtime/approve/flow.html?instanceId="+ instanceId	//查看任务的URL
		+ "&time=" + new Date().getTime();
		
		Map<String, Object> content = new ObjectMapper().convertValue(instanceUnit, Map.class);
		content.put("url", url);
		content.put("title", instanceUnit.getName());
		content.put("content", instanceUnit.getName()+";"+instanceUnit.getCode());
		//content.put("files", instanceUnit.getName());//附件暂时不传
		content.put("createPersonName", instanceUnit.getStartUserName());
		content.put("createDate", instanceUnit.getStartDate().getTime());
		SearchIndexDto dto = new SearchIndexDto();
		dto.setId(instanceId);
		dto.setEsDocId(instanceId);//此处填写实例ID
		dto.setEsDocIndex(securityUserBeanInfo.getTendId());
		dto.setEsDocType("flow");//此处固定填写flow
		dto.setContent(JacksonUtils.toJson(content));
		String contentToSearch = JacksonUtils.toJson(dto);
		log.info("发送全文检索数据：" + contentToSearch);
		System.out.println("\n\n 发送全文检索数据：" + contentToSearch);
		if(service.getSearchIndexDtoServiceCustomer() == null){
			System.out.println("\n\n SearchIndexDtoServiceCustomer is null ----");
		}else{
			try {
				String result = service.getSearchIndexDtoServiceCustomer().save(userInfo, contentToSearch);
				System.out.println("\n\n save() >>> result="+result);
			} catch (Exception e) {
				throw new FlowException("发送全文检索失败！", e);
			}
			
		}
	}

	public void deleteIndexFromSearch(final InstanceUnit instanceUnit) throws Exception {
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		final String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
		final List<String> businessIds = new ArrayList<String>();
		businessIds.add(instanceUnit.getBusinessId());
		Executors.newCachedThreadPool().execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					service.getSearchIndexDtoServiceCustomer().deleteAllSearchIndex(userInfo, 
							instanceUnit.getTendId(), "flow", JacksonUtils.toJson(businessIds));
				} catch (Exception e) {
					throw new FlowException("通知全文检索删除对应流程索引失败：userInfo=" + userInfo 
							+ "tendId=" + instanceUnit.getTendId() + "businessId=" + businessIds);
				}
				
			}
		});
		log.info("通知全文检索删除对应流程索引：instanceId=" + instanceUnit.getId());
	}
	
	protected void resort(List<ACUnit> acList) {
		for(int i=0; i<acList.size(); i++) {
			ACUnit acUnit = acList.get(i);
			acUnit.setAcPx(i + 1);
		}
	}
	
	protected void saveTransition(ApproverUnit approver, String actionName) throws Exception {
		if(approver == null) {
			return ;
		}
		InstanceTransitionRecordService transitionRecordService = this.service.getInstanceTransitionRecordService();
		InstanceTransitionRecord transitionRecord = new InstanceTransitionRecord();
		transitionRecord.setId(IDGenerator.getUUID());
		ACUnit currentAc = approver.getOwner().getOwner();
		transitionRecord.setFiId(currentAc.getOwner().getId());
		transitionRecord.setTransationUserId(approver.getApproverId());
		transitionRecord.setTransationUserName(approver.getApproverName());
		transitionRecord.setTransationDate(new Timestamp(System.currentTimeMillis()));
		transitionRecord.setAcId(currentAc.getAcId());
		transitionRecord.setAcName(currentAc.getAcName());
		transitionRecord.setActionName(actionName);
		transitionRecordService.save(transitionRecord);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public OperationType getType() {
		return type;
	}

	public InstanceService getService() {
		return service;
	}

	public void setService(InstanceService service) {
		this.service = service;
	}

	public ACUnit getCurrentAc() {
		return currentAc;
	}

	public void setCurrentAc(ACUnit currentAc) {
		this.currentAc = currentAc;
	}

	public PostUnit getCurrentPost() {
		return currentPost;
	}

	public void setCurrentPost(PostUnit currentPost) {
		this.currentPost = currentPost;
	}

	public ApproverUnit getCurrentApprover() {
		return currentApprover;
	}

	public void setCurrentApprover(ApproverUnit currentApprover) {
		this.currentApprover = currentApprover;
	}

	public InstanceUnit getInstanceUnit() {
		return instanceUnit;
	}

	public void setInstanceUnit(InstanceUnit instanceUnit) {
		this.instanceUnit = instanceUnit;
	}

	public ApprovalSubmitDto getApprovalDto() {
		return approvalDto;
	}

	public void setApprovalDto(ApprovalSubmitDto approvalDto) {
		this.approvalDto = approvalDto;
	}


	private void saveFailMsg(String postUrl, String postParam, String webService, String webServiceMethod, String postType,String userInfoJson){
		SysNoticeMsgTemp temp = new SysNoticeMsgTemp ();
		temp.setPostParam (postParam);
		temp.setPostUrl (postUrl);
		temp.setWebService (webService);
		temp.setWebServiceMethod (webServiceMethod);
		temp.setPostType (postType);
		temp.setSuccess (false);
		temp.setId (IDGenerator.getUUID ());
		temp.setUserInfoJson (userInfoJson);
		SecurityUserBeanInfo securityUserBeanInfo = JacksonUtils.fromJson (userInfoJson,SecurityUserBeanInfo.class);
		changeDateSource (securityUserBeanInfo.getTendCode ());
		service.getSysNoticeMsgTempDao().save (temp);
	}
	private void changeDateSource(String tendCode){
		DataSourceContextHolder.clearDataSourceType ();
		DataSourceContextHolder.setDataSourceType (tendCode);
		DataSourceContextHolder.getDataSourceType ();
	}
}
