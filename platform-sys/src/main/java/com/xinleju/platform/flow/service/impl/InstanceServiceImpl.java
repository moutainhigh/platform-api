package com.xinleju.platform.flow.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.AttachmentDto;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanRelationInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;
import com.xinleju.platform.flow.dao.InstanceAcDao;
import com.xinleju.platform.flow.dao.InstanceDao;
import com.xinleju.platform.flow.dao.InstancePostDao;
import com.xinleju.platform.flow.dao.PassReadRecordDao;
//import org.apache.commons.beanutils.BeanUtils;
import com.xinleju.platform.flow.dao.SysNoticeMsgTempDao;
import com.xinleju.platform.flow.dto.AcDto;
import com.xinleju.platform.flow.dto.ApprovalList;
import com.xinleju.platform.flow.dto.ApprovalListDto;
import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.dto.ApproveOperationDto;
import com.xinleju.platform.flow.dto.FlowApproveViewBean;
import com.xinleju.platform.flow.dto.FlowQueryBean;
import com.xinleju.platform.flow.dto.InstanceAcDto;
import com.xinleju.platform.flow.dto.InstanceDto;
import com.xinleju.platform.flow.dto.InstanceTransitionRecordDto;
import com.xinleju.platform.flow.dto.InstanceVariableDto;
import com.xinleju.platform.flow.dto.MobileApproveDto;
import com.xinleju.platform.flow.dto.MobileFormDto;
import com.xinleju.platform.flow.dto.PcFormDto;
import com.xinleju.platform.flow.dto.PostDto;
import com.xinleju.platform.flow.dto.UploadAttachmentDto;
import com.xinleju.platform.flow.dto.UserDto;
import com.xinleju.platform.flow.entity.BusinessObject;
import com.xinleju.platform.flow.entity.Fl;
import com.xinleju.platform.flow.entity.Instance;
import com.xinleju.platform.flow.entity.InstanceAc;
import com.xinleju.platform.flow.entity.InstanceAccessible;
import com.xinleju.platform.flow.entity.InstanceCollection;
import com.xinleju.platform.flow.entity.InstanceCs;
import com.xinleju.platform.flow.entity.InstanceGroup;
import com.xinleju.platform.flow.entity.InstancePost;
import com.xinleju.platform.flow.entity.InstanceStep;
import com.xinleju.platform.flow.entity.InstanceTask;
import com.xinleju.platform.flow.entity.InstanceTransitionRecord;
import com.xinleju.platform.flow.entity.InstanceVariable;
import com.xinleju.platform.flow.entity.MobileForm;
import com.xinleju.platform.flow.entity.MobileParam;
import com.xinleju.platform.flow.entity.PassReadRecord;
import com.xinleju.platform.flow.entity.PcForm;
import com.xinleju.platform.flow.entity.SysNoticeMsg;
import com.xinleju.platform.flow.entity.UploadAttachment;
import com.xinleju.platform.flow.enumeration.ACStatus;
import com.xinleju.platform.flow.enumeration.ApproverRepeatHandleType;
import com.xinleju.platform.flow.enumeration.ApproverStatus;
import com.xinleju.platform.flow.enumeration.AutoPassType;
import com.xinleju.platform.flow.enumeration.FlAcType;
import com.xinleju.platform.flow.enumeration.FlowChangeType;
import com.xinleju.platform.flow.enumeration.FlowRole;
import com.xinleju.platform.flow.enumeration.InstanceOperateType;
import com.xinleju.platform.flow.enumeration.InstanceStatus;
import com.xinleju.platform.flow.enumeration.OverdueAcHandle;
import com.xinleju.platform.flow.enumeration.PostStatus;
import com.xinleju.platform.flow.enumeration.TaskStatus;
import com.xinleju.platform.flow.enumeration.TaskType;
import com.xinleju.platform.flow.exception.FlowException;
import com.xinleju.platform.flow.model.ACUnit;
import com.xinleju.platform.flow.model.ApproverUnit;
import com.xinleju.platform.flow.model.InstanceUnit;
import com.xinleju.platform.flow.model.OverdueAc;
import com.xinleju.platform.flow.model.PostUnit;
import com.xinleju.platform.flow.model.TaskUnit;
import com.xinleju.platform.flow.operation.EmptyOperation;
import com.xinleju.platform.flow.operation.Operation;
import com.xinleju.platform.flow.operation.OperationFactory;
import com.xinleju.platform.flow.operation.OperationType;
import com.xinleju.platform.flow.service.AgentService;
import com.xinleju.platform.flow.service.ApproveOperationService;
import com.xinleju.platform.flow.service.BusinessObjectService;
import com.xinleju.platform.flow.service.FlService;
import com.xinleju.platform.flow.service.FlowUserOpinionService;
import com.xinleju.platform.flow.service.InstanceAcService;
import com.xinleju.platform.flow.service.InstanceAccessibleService;
import com.xinleju.platform.flow.service.InstanceCollectionService;
import com.xinleju.platform.flow.service.InstanceCsService;
import com.xinleju.platform.flow.service.InstanceGroupService;
import com.xinleju.platform.flow.service.InstanceOperateLogService;
import com.xinleju.platform.flow.service.InstanceReadRecordService;
import com.xinleju.platform.flow.service.InstanceService;
import com.xinleju.platform.flow.service.InstanceStepService;
import com.xinleju.platform.flow.service.InstanceTaskService;
import com.xinleju.platform.flow.service.InstanceTransitionRecordService;
import com.xinleju.platform.flow.service.InstanceVariableService;
import com.xinleju.platform.flow.service.MobileFormService;
import com.xinleju.platform.flow.service.MonitorSettingService;
import com.xinleju.platform.flow.service.ParticipantService;
import com.xinleju.platform.flow.service.PcFormService;
import com.xinleju.platform.flow.service.StepService;
import com.xinleju.platform.flow.service.SysNoticeMsgService;
import com.xinleju.platform.flow.service.UploadAttachmentService;
import com.xinleju.platform.flow.utils.ConfigurationUtil;
import com.xinleju.platform.flow.utils.DateUtils;
import com.xinleju.platform.flow.utils.JavaBeanCopier;
import com.xinleju.platform.out.app.notice.service.MailAndPhoneServiceCustomer;
import com.xinleju.platform.out.app.org.service.OrgnazationOutServiceCustomer;
import com.xinleju.platform.sys.base.dao.CustomFormDao;
import com.xinleju.platform.sys.base.dao.CustomFormInstanceDao;
import com.xinleju.platform.sys.base.dto.service.CustomFormInstanceDtoServiceCustomer;
import com.xinleju.platform.sys.base.entity.CustomForm;
import com.xinleju.platform.sys.base.entity.CustomFormInstance;
import com.xinleju.platform.sys.org.dto.FlowAcPostDto;
import com.xinleju.platform.sys.org.dto.FlowPostParticipantDto;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.dto.service.OrgnazationDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.service.PostDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.service.UserDtoServiceCustomer;
import com.xinleju.platform.sys.res.dto.UserAuthDataOrgList;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentDtoServiceCustomer;
import com.xinleju.platform.univ.search.dto.service.SearchIndexDtoServiceCustomer;

/**
 * 流程实例相关服务
 * 
 * @author admin
 */
@Service
public class InstanceServiceImpl extends  BaseServiceImpl<String,Instance> implements InstanceService{
	
	private static final String FLOWRUNNING = "flowrunning";

	private static Logger log = Logger.getLogger("flowLogger");
	
	@Autowired
	private InstanceDao instanceDao;
	
	@Autowired
	private InstanceAcDao instanceAcDao;

	@Autowired
	private PassReadRecordDao passReadRecordDao;
	
	@Autowired
	private FlService flService;
	
	@Autowired
	private StepService stepService;
	
	@Autowired
	private InstanceAcService instanceAcService;
	@Autowired
	private InstanceStepService instanceStepService;
	@Autowired
	private ParticipantService participantService;
	@Autowired
	private InstanceGroupService instanceGroupService;
	@Autowired
	private InstanceVariableService instanceVariableService;
	@Autowired
	private InstanceAccessibleService instanceAccessibleService;
	@Autowired
	private InstanceCsService instanceCsService;
	
	@Autowired
	private InstanceTaskService instanceTaskService;
	
	@Autowired
	private InstancePostDao instancePostDao;
	
	@Autowired
	private SysNoticeMsgService msgService;
	
	@Autowired
	private InstanceReadRecordService instanceReadRecordService;
	
	@Autowired
	private SearchIndexDtoServiceCustomer searchIndexDtoServiceCustomer;
	
	@Autowired
	private CustomFormInstanceDtoServiceCustomer customFormInstanceDtoServiceCustomer;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private PostDtoServiceCustomer postDtoServiceCustomer;	
	
	@Autowired
	private InstanceOperateLogService instanceLogService;
	
	@Autowired
	private BusinessObjectService businessObjectService;
	
	@Autowired
	private InstanceCollectionService instanceCollectionService;
	
	@Autowired
	private OrgnazationDtoServiceCustomer orgnazationDtoServiceCustomer;
	
	@Autowired
	private OrgnazationOutServiceCustomer orgnazationOutServiceCustomer;
	
	@Autowired
	private UserDtoServiceCustomer userDtoServiceCustomer;
	
	@Autowired
	private MonitorSettingService monitorSettingService;
	
	@Autowired
	private PcFormService pcFormService;
	
	@Autowired
	private MobileFormService mobileFormService;
	
	@Autowired
	private UploadAttachmentService uploadAttachmentService;
	
	@Autowired
	private InstanceTransitionRecordService instanceTransitionRecordService;
	
	@Autowired
	private ApproveOperationService approveOperationService;
	@Autowired
	private SysNoticeMsgTempDao sysNoticeMsgTempDao;
	@Autowired
	private FlowUserOpinionService opinionServcie;
	
	@Value("#{configuration['flow.approve.url']}")
	private String flowApproveUrl;
	
	@Autowired
	protected RedisTemplate<String, String> redisTemplate;
	
	@Resource(name="redisTemplate")
	private SetOperations<String, String> flowRunningSet;

	//自定义表单模板dao
	@Autowired
	private CustomFormDao customFormDao;
	//自定义表单实例dao
	@Autowired
	private CustomFormInstanceDao customFormInstanceDao;
	
	@Autowired
	private MailAndPhoneServiceCustomer mailAndPhoneServiceCustomer;

	@Autowired
	private AttachmentDtoServiceCustomer attachmentDtoServiceCustomer;

	@Override
	public List<Instance> queryInstanceBy(String flId, String businessId) {
		return instanceDao.queryInstanceByFlIdAndBusinessId(flId, businessId);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ApprovalList> queryApprovalList(String instanceId, String requestSource, String status, String model) {
		//记录查询记录
		instanceReadRecordService.record(instanceId, requestSource);

		List<ApprovalList> approvalList = queryApprovalList(instanceId, status);
		
		if("0".equals(model)) {
			removeForkAndJoin(approvalList);
		}
		
		return approvalList;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ApprovalList> queryApprovalListNoRecord(String instanceId, String requestSource, String status, String model) {
		
		List<ApprovalList> approvalList = queryApprovalList(instanceId, status);
		
		if("0".equals(model)) {
			removeForkAndJoin(approvalList);
		}
		
		return approvalList;
	}
	
	/**
	 * 外部系统查询审批记录 TODO zhangdaoqiang
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ApprovalListDto queryApprovalListExternal(String flCode, String businessId, String appId, String userId) {
		ApprovalListDto retDto = new ApprovalListDto();
		
		//1、记录查询痕迹
		
		//2、判断流程权限
		
		//3、查询流程记录
		List<ApprovalList> approvalList = instanceDao.queryApprovalListExternal(flCode, businessId, appId);
		
		removeForkAndJoin(approvalList);
		
		retDto.setList(approvalList);
		if(CollectionUtils.isNotEmpty(approvalList)) {
			retDto.setCustomFormURL(approvalList.get(0).getPcUrl());
		}
		return retDto;
	}
	
	private void removeForkAndJoin(List<ApprovalList> approvalList) {
		Iterator<ApprovalList> iterator = approvalList.iterator();
		while(iterator.hasNext()) {
			ApprovalList next = iterator.next();
			String acType = next.getAcType();
			if(FlAcType.FORK.getAcType().equals(acType)
			||FlAcType.JOIN.getAcType().equals(acType)
			||FlAcType.CC.getAcType().equals(acType)) {
				iterator.remove();
			}
		}
	}
	
	public List<ApprovalList> queryApprovalList(String instanceId, String status) {
		List<ApprovalList> approvalList = instanceDao.queryApprovalList(instanceId, status);
		return approvalList;
	}
	
	@Override
	public List<String> queryNext(String instanceId, String taskId) {
		Map<String, String> filter = new LinkedHashMap<String, String>();
		//1、查询审批列表，目标数据集
		List<ApprovalList> approvalList = queryApprovalList(instanceId, null);
		Instance instance = null;
		try {
			instance = this.getObjectById(instanceId);
			InstanceUnit instanceUnit = this.translate(instance, approvalList);
			EmptyOperation operation = new EmptyOperation();
			operation.setService(this);
			ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
			approvalDto.setInstanceId(instanceId);
			approvalDto.setTaskId(taskId);
			
			//设置当前位置
			operation.setCurrentLocation(instanceUnit, approvalDto);
			
			//如果下一环节是结束节点，直接返回
			ACUnit currentAc = operation.getCurrentAc();
			List<ACUnit> nextAcs = currentAc.getNextAcs();
			if(CollectionUtils.isNotEmpty(nextAcs)) {
				if(FlAcType.END.getAcType().equals(nextAcs.get(0).getAcType())) {
					List<String> next = new ArrayList<String>();
					next.add("结束");
					return next;
				}
			}
			
			//判断当前环节是否全点亮:
			boolean allOn = checkAllPersonOn(operation.getCurrentAc());
			
			//全点亮直接点亮下一环节
			if(allOn) {
				for(ACUnit ac : nextAcs) {
					if(FlAcType.JOIN.getAcType().equals(ac.getAcType())) {
						ac.setLeftPost(0);
					}
				}
				operation.turnOn(nextAcs, instanceUnit);
				//重新设置当前位置
				operation.setCurrentLocation(instanceUnit, approvalDto);

				//不完全点亮
			} else {
				operation.next(instanceUnit, approvalDto);
				
			}
			
			for(ACUnit acUnit : instanceUnit.getAcList()) {
				List<PostUnit> posts = acUnit.getPosts();
				if(CollectionUtils.isEmpty(posts)) {
					continue;
				}
				for(PostUnit post : posts) {
					List<ApproverUnit> approvers = post.getApprovers();
					if(CollectionUtils.isEmpty(approvers)) {
						continue;
					}
					for(ApproverUnit approver : approvers) {
						if(TaskStatus.RUNNING.getValue().equals(approver.getTask().getTaskStatus())
								&& approver.getDbAction() != 2) {
							
							//去除与当前人同环节的人
							String currentAcId = operation.getCurrentAc().getAcId();
							if(!"3".equals(approver.getProxyType())){
								if(approver.getOwner().getOwner().getAcId().equals(currentAcId)) {
									continue;
								}
							}

							
//							String postFullName = approver.getOwner().getPostName();
//							int lastIndex = postFullName.lastIndexOf("/");
//							String roleName = postFullName.substring(lastIndex + 1);
//							String next = approver.getApproverName() + " - (" + roleName + ")";
//							retList.add(approver.getApproverName());
							filter.put(approver.getApproverId(), approver.getApproverName());
						}
					}
				}
			}
			
		} catch (Exception e) {
		}
		
		List<String> retList = new ArrayList<String>(filter.values());
		if(CollectionUtils.isEmpty(retList)) {
			retList.add("结束");
		}
		return retList;
	}
	
	private boolean checkAllPersonOn(ACUnit acUnit) {
		for(PostUnit post : acUnit.getPosts()) {
			List<ApproverUnit> approvers = post.getApprovers();
			if(CollectionUtils.isEmpty(approvers)) {
				continue;
			}
			for(ApproverUnit approver : approvers) {
				if (approver.getTask() == null || null ==  approver.getTask().getTaskId()
						|| TaskStatus.NOT_RUNNING.getValue().equals(approver.getTask().getTaskStatus())) {
					return false;
				}
			}
		}		
		return true;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String doWithDrawFlow(String instanceId) throws Exception {
		List<ApprovalList> approvalList = queryApprovalList(instanceId, null);
		Instance instance = this.getObjectById(instanceId);
		InstanceUnit instanceUnit = this.translate(instance, approvalList);
		Operation operation = new OperationFactory().newOperation(OperationType.WITHDRAW_FLOW.getCode(), this);
		ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
		operation.action(instanceUnit, approvalDto);
		String starter = instanceUnit.getAcList().get(0).getPosts().get(0).getApprovers().get(0).getApproverName();
		saveTransition(instanceUnit.getId(), starter, "撤回流程");
		return "success";
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String doWithDrawTask(String instanceId, String taskId) throws Exception {
		List<ApprovalList> approvalList = queryApprovalList(instanceId, null);
		Instance instance = this.getObjectById(instanceId);
		InstanceUnit instanceUnit = this.translate(instance, approvalList);
		Operation operation = new OperationFactory().newOperation(OperationType.WITHDRAW_TASK.getCode(), this);
		ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
		approvalDto.setTaskId(taskId);
		approvalDto.setOperationName("撤回任务");
		return operation.action(instanceUnit, approvalDto);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean doFinishApproval(String instanceId) throws Exception {
		List<ApprovalList> approvalList = queryApprovalList(instanceId, null);
		Instance instance = this.getObjectById(instanceId);
		InstanceUnit instanceUnit = this.translate(instance, approvalList);
		Operation operation = new OperationFactory().newOperation(OperationType.FINISHFLOW.getCode(), this);
		ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
		approvalDto.setOperationType(OperationType.AGREE.getCode());
		approvalDto.setOperationName("通过");
		approvalDto.setUserNote("管理员审结通过！");
		operation.action(instanceUnit, approvalDto);
		
		//记录流转日志
		this.saveTransition(instanceId, "管理员", "审结");
		return true;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean doSkipCurrentApprover(String instanceId) throws Exception {
		List<ApprovalList> approvalList = queryApprovalList(instanceId, null);
		Instance instance = this.getObjectById(instanceId);
		InstanceUnit instanceUnit = this.translate(instance, approvalList);
		Operation operation = new OperationFactory().newOperation(OperationType.SKIPCURRENT.getCode(), this);
		ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
		approvalDto.setUserNote("管理员操作：跳过当前审批人！");
		//approvalDto.setOperationName("管理员操作：跳过当前审批人！");
		operation.action(instanceUnit, approvalDto);
		
		//记录流转日志
		this.saveTransition(instanceId, "管理员", "跳过当前审批人");
		
		return true;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean doFlowRestart(String instanceId) throws Exception {
		List<ApprovalList> approvalList = queryApprovalList(instanceId, null);
		Instance instance = this.getObjectById(instanceId);
		InstanceUnit instanceUnit = this.translate(instance, approvalList);
		Operation operation = new OperationFactory().newOperation(OperationType.RESTART.getCode(), this);
		ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
		approvalDto.setUserNote("管理员操作：流程放行！");
		operation.action(instanceUnit, approvalDto);
		
		//记录流转日志
		this.saveTransition(instanceId, "管理员", "流程放行");
		return true;	
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public DubboServiceResultInfo doCancelInstance(String instanceId) throws Exception {
		DubboServiceResultInfo dubboServiceResultInfo=new DubboServiceResultInfo();
		
		List<ApprovalList> approvalList = queryApprovalList(instanceId, null);
		Instance instance = this.getObjectById(instanceId);
		InstanceUnit instanceUnit = this.translate(instance, approvalList);
		
		//审批完成才判断是否能作废
		if("2".equals(instance.getStatus())){
			//查询业务系统流程是否可作废
			BusinessObject businessObject = businessObjectService.getObjectById(instanceUnit.getBusinessObjectId());
			if(businessObject!=null && businessObject.getInvalidUrl()!=null && !"".equals(businessObject.getInvalidUrl())){
				dubboServiceResultInfo=this.getInvalidBusinessSystem(instanceUnit);
				if(dubboServiceResultInfo.isSucess()){
					doInvalid(dubboServiceResultInfo, instanceUnit);
				}
			}else{
				doInvalid(dubboServiceResultInfo, instanceUnit);
			}
		}else{
			doInvalid(dubboServiceResultInfo, instanceUnit);
		}
		
		return dubboServiceResultInfo;
	}

	private void doInvalid(DubboServiceResultInfo dubboServiceResultInfo, InstanceUnit instanceUnit) throws Exception {
		Operation operation = new OperationFactory().newOperation(OperationType.INVALID.getCode(), this);
		ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
		approvalDto.setUserNote("管理员流程作废操作成功！");
		operation.action(instanceUnit, approvalDto);
		saveTransition(instanceUnit.getId(), "管理员", "作废流程");
		dubboServiceResultInfo.setMsg("管理员执行流程作废成功！");
		dubboServiceResultInfo.setSucess(true);
	}

	/**
	  * @Description:查询业务系统流程是否可作废
	  * @author:zhangfangzhi
	  * @date 2018年1月19日 上午11:30:26
	  * @version V1.0
	 * @throws Exception 
	 */
	private DubboServiceResultInfo getInvalidBusinessSystem(InstanceUnit instanceUnit) throws Exception {
		DubboServiceResultInfo dubboServiceResultInfo = new DubboServiceResultInfo();
		SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", instanceUnit.getBusinessId());
		params.put("businessObjectCode", instanceUnit.getBusinessObjectCode());
		params.put("status", instanceUnit.getStatus());
		params.put("instanceId", instanceUnit.getId());
		params.put("flCode", instanceUnit.getFlCode());
		String token = userInfo.getSecurityUserDto().getLoginName() + "@" + userInfo.getTendCode();
		params.put("token", token);
		BusinessObject businessObject = businessObjectService.getObjectById(instanceUnit.getBusinessObjectId());
		
		//往redis中设置当前用户信息
		SecurityUserBeanRelationInfo relationInfo = new SecurityUserBeanRelationInfo();
		this.setLoginInfo(token, JacksonUtils.toJson(userInfo), JacksonUtils.toJson(relationInfo));
		
		try {
			log.info("调用业务系统是否可作废：" + params + ", userInfo=" + JacksonUtils.toJson(userInfo));
			String url=businessObject.getInvalidUrl();
			String updateDubboResultInfo = LoginUtils.httpPost(url, JacksonUtils.toJson(params));
			log.info("业务系统返回值：" + updateDubboResultInfo);
			MessageResult messageResult = JacksonUtils.fromJson(updateDubboResultInfo,MessageResult.class);
			if (messageResult.isSuccess()) {
				log.info("是否可作废通知成功：流程ID=" + instanceUnit.getId() + "状态=" + instanceUnit.getStatus() + ", bizId=" + instanceUnit.getBusinessId());
			} else {
				log.info("是否可作废通知失败：流程ID=" + instanceUnit.getId() + "状态=" + instanceUnit.getStatus() + ", bizId=" + instanceUnit.getBusinessId());
			}
			dubboServiceResultInfo.setSucess(messageResult.isSuccess());
			dubboServiceResultInfo.setMsg(messageResult.getMsg());
			dubboServiceResultInfo.setResult((String)messageResult.getResult());
			dubboServiceResultInfo.setCode(messageResult.getCode());
		}catch (Exception e) {
			throw new FlowException("是否可作废调用业务系统失败：URL=" + businessObject.getInvalidUrl() + ";参数=" + params  + ", bizId=" + instanceUnit.getBusinessId(), e);
		}
		return dubboServiceResultInfo;
	}

	/**
	 * 传阅时给每个人发送待阅消息
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean doPassAndRead(String instanceId, List<UserDto> userList) throws Exception {
		SecurityUserDto securityUserDto=LoginUtils.getSecurityUserBeanInfo().getSecurityUserDto();
		Instance instance = this.getObjectById(instanceId);
		List<SysNoticeMsg> messages = new ArrayList<SysNoticeMsg>();
		BusinessObject businessObject = businessObjectService.getObjectById(instance.getBusinessObjectId());

		List<InstanceAccessible> accessibleList = new ArrayList<InstanceAccessible>();
		List<PassReadRecord> readPassList=new ArrayList<PassReadRecord>();
		Timestamp nowTime=new Timestamp(System.currentTimeMillis());
		for(UserDto user : userList) {
			String mobileUrl = "mobile/approve/approve_detail.html";
			MobileParam mobileParamBean = new MobileParam();
			mobileParamBean.setInstanceId(instanceId);
			mobileParamBean.setBusinessId(instance.getBusinessId());
			mobileParamBean.setAppId(instance.getAppId());
			//mobileParamBean.setTypeCode("START");
			//mobileParamBean.setTaskId(startApprover.getTask().getTaskId());
			//mobileParamBean.setApproveRole(startApprover.getTask().getTaskType());
			
			String url = "/flow/runtime/approve/flow.html"
					+ "?instanceId=" + instanceId
					+ "&time=" + new Date().getTime();			
			SysNoticeMsg msg = msgService.newFlowMsg(user, "DY", instance.getName(), url, mobileUrl, JacksonUtils.toJson(mobileParamBean));
			messages.add(msg);

			//记录传阅类型的可阅人
			/*InstanceAccessible instanceAccessible = new InstanceAccessible();
			instanceAccessible.setId(IDGenerator.getUUID());
			instanceAccessible.setFiId(instanceId);
			instanceAccessible.setType("2");//传阅类型的可阅人
			instanceAccessible.setAccessibleId(user.getId());
			instanceAccessible.setAccessibleName(user.getName());
			instanceAccessible.setDelflag(false);
			instanceAccessible.setConcurrencyVersion(0);
			accessibleList.add(instanceAccessible);*/
			
			//记录操作日志
			instanceLogService.saveLogData(instanceId, null, null, null, 
					InstanceOperateType.PASS_READ.getValue(), user.getId(), 
					null, null);
			
			//记录传阅日志
			PassReadRecord passReadRecord=new PassReadRecord();
			passReadRecord.setId(IDGenerator.getUUID());
			passReadRecord.setFiId(instanceId);
			passReadRecord.setTransationUserName(securityUserDto.getRealName());//操作人名称 
			passReadRecord.setTransationUserId(securityUserDto.getId());//操作人id
			passReadRecord.setToUserId(user.getId());//传阅人id
			passReadRecord.setToUserName(user.getName());//传阅人名称
			passReadRecord.setTransationDate(nowTime);
			passReadRecord.setActionName("传阅");
			passReadRecord.setDelflag(false);
			passReadRecord.setMsgId(msg.getId());
			passReadRecord.setCancelPassReadTime(null);
			readPassList.add(passReadRecord);
		}
		passReadRecordDao.saveBatch(readPassList);
		//this.msgService.saveBatch(messages);
		this.msgService.batchSaveAndNotifyOthers(messages);

		//将传阅人保存至可阅人中
		//this.instanceAccessibleService.saveBatch(accessibleList);
		
		if(CollectionUtils.isNotEmpty(userList)) {
			for(UserDto user : userList) {
				log.info("发送待阅消息成功：instanceId=" + instanceId
						+ ", 待阅人=" + user.getName()
						+ ", 待阅人ID=" + user.getLoginName() 
						+ ", 发送人：" + securityUserDto.getRealName()
						+ ", 发送时间：" + new Date());			
			}
		}
		
		return true;
	}
	

	/**
	 * 取消传阅
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public DubboServiceResultInfo cancelPassAndRead(String id){
		DubboServiceResultInfo dubboServiceResultInfo=new DubboServiceResultInfo();
		SecurityUserDto securityUserDto=LoginUtils.getSecurityUserBeanInfo().getSecurityUserDto();
		List<PassReadRecord> passReadRecordList=passReadRecordDao.queryPassReadListById(id);
		List<String> msgIdList=new ArrayList<String>();
		if(passReadRecordList!=null && passReadRecordList.size()>0){
			PassReadRecord passRead=passReadRecordList.get(0);
			if(!"2".equals(securityUserDto.getType()) && !securityUserDto.getId().equals(passRead.getTransationUserId())){
				dubboServiceResultInfo.setSucess(false);
				dubboServiceResultInfo.setMsg("您没有权限撤回传阅！");
				return dubboServiceResultInfo;
			}
			Timestamp nowTime=new Timestamp(System.currentTimeMillis());
			Map<String, String> queryParam = new HashMap<String, String>();
			try {
				for(int i=0;i<passReadRecordList.size();i++){
					PassReadRecord passReadRecord=passReadRecordList.get(i);
					msgIdList.add(passReadRecord.getMsgId());
					passReadRecord.setCancelPassReadTime(nowTime);
					passReadRecord.setCancelPassReadUserId(securityUserDto.getId());
					passReadRecord.setCancelPassReadUserName(securityUserDto.getRealName());
					passReadRecord.setDelflag(true);
					queryParam.put("instanceId", passReadRecord.getFiId());
					queryParam.put("operateType", InstanceOperateType.PASS_READ.getValue());
					queryParam.put("operatorIds", passReadRecord.getToUserId());
					instanceLogService.deleteDataByParamMap(queryParam);//删除待阅日志
					queryParam.put("operateType", InstanceOperateType.HAVE_READ.getValue());
					instanceLogService.deleteDataByParamMap(queryParam);//删除已阅日志
				}
				int count=passReadRecordDao.updateBatch(passReadRecordList);
				if(count>0){
					count=msgService.deleteAllObjectByIds(msgIdList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dubboServiceResultInfo.setSucess(true);
		dubboServiceResultInfo.setMsg("撤回传阅成功！");
		return dubboServiceResultInfo;
	}
	
	/**
	 * 发起人催办消息
	 * @throws Exception 
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean doRemind(String instanceId) throws Exception {
		List<ApprovalList> approvalList = queryApprovalList(instanceId, null);
		Instance instance = this.getObjectById(instanceId);
		InstanceUnit instanceUnit = this.translate(instance, approvalList);
		List<ApproverUnit> currentApprovers = instanceUnit.getCurrentApprover();
		List<SysNoticeMsg> messages = new ArrayList<SysNoticeMsg>();
		BusinessObject businessObject = businessObjectService.getObjectById(instance.getBusinessObjectId());
		
		for(ApproverUnit approver : currentApprovers) {
			String mobileUrl = "mobile/approve/approve_detail.html";
			MobileParam mobileParamBean = new MobileParam();
			mobileParamBean.setInstanceId(instanceUnit.getId());
			mobileParamBean.setBusinessId(instanceUnit.getBusinessId());
			mobileParamBean.setAppId(instanceUnit.getAppId());
			//mobileParamBean.setTypeCode("START");
			//mobileParamBean.setTaskId(startApprover.getTask().getTaskId());
			//mobileParamBean.setApproveRole(startApprover.getTask().getTaskType());
			
			String url = "/flow/runtime/approve/flow.html"
					+ "?instanceId=" + instanceId
					+ "&time=" + new Date().getTime();			
			UserDto user = new UserDto(approver.getApproverId(), approver.getApproverName());
			SysNoticeMsg message = this.msgService.newFlowMsg(user, "DY", "请尽快处理:" + instance.getName(), url, mobileUrl, JacksonUtils.toJson(mobileParamBean));
			messages.add(message);
			
			//记录操作日志
			instanceLogService.saveLogData(instanceId, null, null, null, 
					InstanceOperateType.TO_READ.getValue(), user.getId(), 
					null, null);
		}
		
		//this.msgService.saveBatch(messages);
		this.msgService.batchSaveAndNotifyOthers(messages);
		log.info("发送催办消息成功!");
		
		return true;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean collection(String instanceId) throws Exception {
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userId = securityUserBeanInfo.getSecurityUserDto().getId();
		InstanceCollection collection = new InstanceCollection();
		collection.setId(IDGenerator.getUUID());
		collection.setUserId(userId);
		collection.setInstanceId(instanceId);
		instanceCollectionService.save(collection);
		
		//TODO zhangdaoqiang 记录操作日志
		instanceLogService.saveLogData(instanceId, null, null, null, 
				InstanceOperateType.COLLECT.getValue(), userId, null, null);
		
		return true;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean doAdjustAc(List<ApprovalList> approvalList) throws Exception {
		return doAdjustAcInternal(approvalList);
	}
	
	private boolean doAdjustAcInternal(List<ApprovalList> approvalList) throws Exception {
		
		//处理同岗多人问题，分裂处理
		approvalList = handleMutipulUser(approvalList);
		
		//重新排序（环节、岗位、人员）
		setRelations(approvalList);
		
		Instance instance = this.getObjectById(approvalList.get(0).getInstanceId());
		InstanceUnit instanceUnit = this.translate(instance, approvalList);
		
		//设置模型【环节层】【岗位层】DB操作类型(人员层已在translate设置)，此方法从下往上设置，同时适用调整AC及审批人
		setDbAction(instanceUnit);
	
		//重新排序(环节、岗位、人员)
		sort(instanceUnit);
		
		EmptyOperation operation = new EmptyOperation();
		operation.setService(this);
		operation.setCurrentApprovers(instanceUnit);	// TODO zhangdaoqiang 待重构，抽成一个具体的操作。
		operation.save(instanceUnit);
		ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
		operation.handleMessages(instanceUnit, approvalDto);
		
		return true;
	}
	
	private List<ApprovalList> handleMutipulUser(List<ApprovalList> approvalList) {
		List<ApprovalList> newList = new ArrayList<ApprovalList>();
		for (ApprovalList approval : approvalList) {
			if (!StringUtils.isEmpty(approval.getApproverId()) && approval.getApproverId().contains(",")) {
				String[] userIdArray = approval.getApproverId().split(",");
				String[] userNameArray = approval.getApproverName().split(",");
				if (userIdArray.length != userNameArray.length) {
					throw new FlowException(
							"调整环节时数据错误：审批人ID与名称个数不一致！userIdArray=" + userIdArray + ", userNameArray" + userNameArray);
				}

				for (int i = 0; i < userIdArray.length; i++) {
					ApprovalList newApproval = (ApprovalList) JavaBeanCopier.copy(approval);
					newApproval.setApproverId(userIdArray[i]);
					newApproval.setApproverName(userNameArray[i]);
					newList.add(newApproval);
				}
			} else {
				newList.add(approval);
			}
		}
		return newList;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ApprovalList> doCreateAc(InstanceAcDto instanceAcDto) throws Exception {
		List<ApprovalList> approvalLists = new ArrayList<ApprovalList>();
		String code=instanceAcDto.getCode();
		code=code+"-"+DateUtils.getDateSSSText();
		instanceAcDto.setCode(code);
		Map<String,String> map=new HashMap<>();
		map.put("fiId", instanceAcDto.getFiId());
		map.put("id", instanceAcDto.getId());
		map.put("code", instanceAcDto.getCode());
		//校验编码重复   add by gyh  
		Integer i=instanceAcDao.checkCode(map);
		if(i!=null && i>0){
			throw new FlowException("该流程此编码已存在，不可重复！");
		}
		/*
		 * 构建AcDto
		 */
		AcDto acDto = new AcDto();
		acDto.setId(instanceAcDto.getId());
		acDto.setCode(instanceAcDto.getCode());
		acDto.setName(instanceAcDto.getName());
		acDto.setApproveTypeId(instanceAcDto.getApproveTypeId());
		acDto.setParticipant(instanceAcDto.getParticipant());
		acDto.setCcPerson(instanceAcDto.getCcPerson());
		acDto.setPostMultiPerson(instanceAcDto.getPostMultiPerson());
		acDto.setApproveStrategy(instanceAcDto.getApproveStrategy());

		/*
		 * 构建业务变量Map
		 */
		Map<String, Object> businessVariable = new HashMap<String, Object>();
		Instance instance = getObjectById(instanceAcDto.getFiId());
		businessVariable.put("flow_business_company_id", instance.getFlowBusinessCompanyId());
		businessVariable.put("flow_business_company_name", instance.getFlowBusinessCompanyName());
		businessVariable.put("flow_business_dept_id", instance.getFlowBusinessDeptId());
		businessVariable.put("flow_business_dept_name", instance.getFlowBusinessDeptName());
		businessVariable.put("flow_business_project_id", instance.getFlowBusinessProjectId());
		businessVariable.put("flow_business_project_name", instance.getFlowBusinessProjectName());
		businessVariable.put("flow_business_project_branch_id", instance.getFlowBusinessProjectBranchId());
		businessVariable.put("flow_business_project_branch_name", instance.getFlowBusinessProjectBranchName());
		businessVariable.put("start_user_id", instance.getStartUserId());
		businessVariable.put("start_user_name", instance.getStartUserName());
		
		/*
		 * 解析人员岗位
		 */
		List<FlowAcPostDto> flowAcPostDtos = parsePost(businessVariable, acDto);
		if(CollectionUtils.isEmpty(flowAcPostDtos)) {
			throw new FlowException("调整环节时后台查询对应岗位为空！");
		}
		for (FlowAcPostDto flowAcPostDto : flowAcPostDtos) {
			if(flowAcPostDto.getId().equals(acDto.getId())){
			List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
			if (flowPostParticipantDtos != null && flowPostParticipantDtos.size() > 0) {
				for (FlowPostParticipantDto post : flowPostParticipantDtos) {
					ApprovalList approvalList = new ApprovalList();
					approvalList.setAcId(acDto.getId());
					approvalList.setAcName(acDto.getName());
					approvalList.setAcCode(acDto.getCode());
					approvalList.setAcType(acDto.getAcType());
					approvalList.setPostId(post.getPostId());
					approvalList.setPostName(post.getPostPrefixName());
					approvalList.setApproverId(post.getUserId());
					approvalList.setApproverName(post.getUserName());
					approvalList.setApprovalTypeId(acDto.getApproveTypeId());
					approvalList.setMultiPerson(acDto.getPostMultiPerson());
					approvalList.setMultiPost(acDto.getApproveStrategy());
					approvalList.setChangeType(1);
					approvalLists.add(approvalList);
				}
			} else {
				ApprovalList approvalList = new ApprovalList();
				approvalList.setAcId(acDto.getId());
				approvalList.setAcName(acDto.getName());
				approvalList.setAcType(acDto.getAcType());
				approvalList.setAcCode(acDto.getCode());
				approvalList.setChangeType(1);
				approvalList.setMultiPerson(acDto.getPostMultiPerson());
				approvalList.setMultiPost(acDto.getApproveStrategy());
				approvalLists.add(approvalList);
			}
		}
	}
		return approvalLists;
	}

	
	
	/**
	 * 根据人员层DBaction设置AC层及岗位层DBAction
	 * 
	 * @param instanceUnit
	 */
	private void setDbAction(InstanceUnit instanceUnit) {
		for(ACUnit acUnit : instanceUnit.getAcList()) {
			
			//排序岗位
			int postDelCount = 0;
			List<PostUnit> posts = acUnit.getPosts();
			if(CollectionUtils.isEmpty(posts)) {
				continue;
			}
			for(PostUnit post : posts) {
				
				int approverDelCount = 0;
				List<ApproverUnit> approvers = post.getApprovers();
				if(CollectionUtils.isEmpty(approvers)) {
					continue;
				}
				for(ApproverUnit approver : approvers) {
					if(approver.getDbAction() == 2) {
						approver.getTask().setDbAction(2);
						approverDelCount++;
						
						//对应消息作删除处理
						instanceUnit.getMessagesToDel().add(approver.getTask().getMsgId());
					}
					
					if(approver.getDbAction() == 1 
							&& TaskStatus.RUNNING.getValue().equals(approver.getTask().getTaskStatus())) {
						approver.getTask().setDbAction(1);
					}
				}
				if(approverDelCount == approvers.size()) {
					post.setDbAction(2);
					postDelCount++;
				}
			}
			
			if(postDelCount == posts.size()) {
				acUnit.setDbAction(2);
			}
		}
	}

	private void sort(InstanceUnit instanceUnit) {
		int acIndex = 0;
		for(ACUnit acUnit : instanceUnit.getAcList()) {
			
			//非删除时
			if(acUnit.getDbAction() != 2) {
				acUnit.setAcPx(++acIndex);
			}
			
			//排序岗位
			int postIndex = 0;
			List<PostUnit> posts = acUnit.getPosts();
			if(CollectionUtils.isEmpty(posts)) {
				continue;
			}
			for(PostUnit post : posts) {
				
				if(post.getDbAction() != 2) { 
					post.setPostSeq(++postIndex);
				}
				
				List<ApproverUnit> approvers = post.getApprovers();
				if(CollectionUtils.isEmpty(approvers)) {
					continue;
				}
				int approverIndex = 0;
				for(ApproverUnit approver : approvers) {
					
					if(approver.getDbAction() != 2) {
						approver.setApproverSeq(++approverIndex);	//实际只有一个人，岗位未合并！！！
					}
				}
			}
		}
	}

	private void setRelations(List<ApprovalList> approvalList) {
		for(ApprovalList approver : approvalList) {
			
			//新增AC或人员
			if(approver.getChangeType() == 1) {
				approver.setGroupKey(IDGenerator.getUUID());	//人员KEY
				
				if(!checkPostExist(approver.getPostId(), approvalList)) {
					approver.setAcPostId(IDGenerator.getUUID());	//Post表KEY	多人岗位相同时未合并！！！
				}
				
				approver.setAcType(FlAcType.TASK.getAcType());
//				approver.setAcStatus(ACStatus.NOT_RUNNING.getValue());
				
				//删除审批人？？？
			} else if(approver.getChangeType() == 2) {	
				//不需要作关系维护
			}
			
		}
		//设置前后关联关系
		for(int i=1; i<approvalList.size() - 1; i++) {
			ApprovalList current = approvalList.get(i);
			
			if(StringUtils.isEmpty(current.getPreAcIds())) {
				ApprovalList pre = preAc(approvalList, i -1, approvalList.get(i-1));
				if(pre != null) {
					current.setPreAcIds(pre.getAcId());
					pre.setNextAcIds(nextAc(approvalList, i-1, pre).getAcId());
					
					if(pre.getChangeType() == 0) {
						pre.setChangeType(3);	//修改态
					}
				}
			}
			
			if(StringUtils.isEmpty(current.getNextAcIds())) {
				ApprovalList next = nextAc(approvalList, i, current);
				if(next != null) {
					current.setNextAcIds(next.getAcId());
					next.setPreAcIds(current.getAcId());
					
					if(next.getChangeType() == 0) {
						next.setChangeType(3);
					}
				}
			}
		}
	}
	
	private boolean checkPostExist(String postId, List<ApprovalList> approvalList) {
		int count = 0;
		for(ApprovalList list : approvalList) {
			if(postId.equals(list.getPostId()) && list.getChangeType() != 2
					&& PostStatus.RUNNING.getValue().equals(list.getPostStatus())) {
				count++;
			}
		}
		if(count > 1) {
			return true;
		}
		return false;
	}

	/**
	 * 下一个ACID不同的位置
	 * @param approvalList
	 * @param currentIndex
	 * @param current
	 * @return
	 */
	private ApprovalList nextAc(List<ApprovalList> approvalList, int currentIndex, ApprovalList current) {
		ApprovalList next = null;
		do {
			currentIndex++;
			next = approvalList.get(currentIndex);
		} while(current.getAcId().equals(next.getAcId()));
		
		return next;
	}
	
	/**
	 * 第一个ACID相同的位置
	 * @param approvalList
	 * @param currentIndex
	 * @param current
	 * @return
	 */
	private ApprovalList preAc(List<ApprovalList> approvalList, int currentIndex, ApprovalList current) {
		for(int i=currentIndex; i>0; i--) {
			ApprovalList pre = approvalList.get(i);
			if(!current.getAcId().equals(pre.getAcId())) {
				return approvalList.get(i + 1);
			}
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean doUpdateApprover(List<ApprovalList> approvalList) throws Exception {
		
		log.info("修改审批人操作开始：页面值=" + approvalList);
		
		//处理被删除的审批人
		//处理新增的审批人
		
//		//同调整AC
//		doAdjustAcInternal(list);
		
		//处理同岗多人问题，分裂处理
		approvalList = handleMutipulUser(approvalList);
		
		//重新排序（环节、岗位、人员）
		setRelations(approvalList);
		log.info("重新排序（环节、岗位、人员）后的列表值=" + approvalList);
		
		Instance instance = this.getObjectById(approvalList.get(0).getInstanceId());
		InstanceUnit instanceUnit = this.translate(instance, approvalList);
		
		//不作环节新增（与调整环节不同的地方）
		for(ACUnit acUnit: instanceUnit.getAcList()) {
			if(acUnit.getDbAction() == 1) {
				acUnit.setDbAction(0);
				acUnit.setChange(true);
			}
		}
		
		//设置模型【环节层】【岗位层】DB操作类型(人员层已在translate设置)，此方法从下往上设置，同时适用调整AC及审批人
		setDbAction(instanceUnit);
	
		//重新排序(环节、岗位、人员)
		sort(instanceUnit);
		
		countLeft(instanceUnit);
		
		EmptyOperation operation = new EmptyOperation();
		operation.setService(this);
		operation.setCurrentApprovers(instanceUnit);	// TODO zhangdaoqiang 待重构，抽成一个具体的操作。
		operation.save(instanceUnit);
		ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
		operation.handleMessages(instanceUnit, approvalDto);
		
		return true;
	}

	private void countLeft(InstanceUnit instanceUnit) {
		for(ACUnit acUnit : instanceUnit.getAcList()) {
			List<PostUnit> posts = acUnit.getPosts();
			if(CollectionUtils.isNotEmpty(posts)) {
				int leftPost = 0;
				for(PostUnit post : posts) {
					List<ApproverUnit> approvers = post.getApprovers();
					if(CollectionUtils.isNotEmpty(approvers)) {
						int leftPerson = 0;
						for(ApproverUnit approver : approvers) {
							if(approver.getDbAction() != 2
									&& TaskStatus.RUNNING.getValue().equals(approver.getTask().getTaskStatus())) {
								leftPerson++;
							}
						}
						post.setLeftPerson(leftPerson);
					}
					
					if(post.getDbAction() != 2
							&& PostStatus.RUNNING.getValue().equals(post.getPostStatus())) {
						leftPost++;
					}
				}
				acUnit.setLeftPost(leftPost);
			}
		}
	}

	/**
	 * 根据任务ID修改审批意见
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean doUpdateApprovalComments(List<ApprovalList> list) {
		for(ApprovalList approver : list) {
			if(approver.getChangeType() == 3) {	//修改
				instanceTaskService.updateComment(approver.getTaskId(), approver.getTaskComments());
			}
		}
		
		String instanceId = list.get(0).getInstanceId();
		String starter = list.get(0).getApproverId();
		
		//TODO zhangdaoqiang 记录操作日志
		try {
			instanceLogService.saveLogData(instanceId, null, null, null, 
					InstanceOperateType.MODIFY_ADVICE.getValue(), starter, null, null);
		} catch (Exception e) {
			throw new FlowException("保存流程操作日志异常！", e);
		}
		return true;
	}

	/**
	 * 查询打回人列表
	 */
	@Override
	public List<Map<String, String>> queryApproverBeReturn(String instanceId) {
		List<Map<String, String>> retList = new ArrayList<Map<String, String>>();
		List<ApprovalList> approverDone = queryApproverDone(instanceId);
		for(ApprovalList approver : approverDone) {
			
			//去除协办人
			if(TaskType.ASSIST.getValue().equals(approver.getTaskType())) {
				continue;
			}
			//去除沟通发起人
			if(TaskType.STARTER.getValue().equals(approver.getTaskType())
					&& !StringUtils.isEmpty(approver.getFromId())) {	//区别于发起人
				continue;
			}
			
			Map<String, String> map = new HashMap<String, String>();
			String approverId = approver.getApproverId();
			String acId = approver.getAcId();
			map.put("id", acId + "." + approverId);
			String displayName = approver.getAcName() + "【" + approver.getApproverName() + "】";
			map.put("displayName", displayName);
			map.put("name", approver.getApproverName());
			retList.add(map);
		}
		
		return retList;
	}
	
	/**
	 * 查询审批完毕列表
	 * 
	 * @param instanceId
	 * @return
	 */
	public List<ApprovalList> queryApproverDone(String instanceId) {
		List<ApprovalList> approverDone = queryApprovalList(instanceId, TaskStatus.FINISHED.getValue());
		Iterator<ApprovalList> iterator = approverDone.iterator();
		while(iterator.hasNext()) {
			
			//过滤自动跳过的人
			ApprovalList approver = iterator.next();
			String currentApproverTypeId = approver.getApprovalTypeId();
			String acType = approver.getAcType();
			
			//去除非（会签+校稿+开始）自动跳过的人
			if (!"HQ".equals(currentApproverTypeId)
					&& !"JG".equals(currentApproverTypeId)
					&& !FlAcType.START.getAcType().equals(acType)
					&& approver.getAutoPass() == 1) {
				iterator.remove();
			}
			
			//去除分支聚合节点
			if(FlAcType.FORK.getAcType().equals(acType)
					|| FlAcType.JOIN.getAcType().equals(acType)) {
				iterator.remove();
			}
		}
		
		return approverDone;
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean approval(ApprovalSubmitDto approvalDto) throws Exception {
		
		if(approvalDto.getOperationType() == null) {
			throw new FlowException("用户选择的操作为空，请检查！");
		}
		
		//重复提交处理
		String runKey = approvalDto.getInstanceId() + "." + approvalDto.getTaskId();
		boolean isRunning = flowRunningSet.isMember(FLOWRUNNING, runKey);
		log.info("重复提交处理：" + runKey + "-" + isRunning);
		if(isRunning) {
			throw new FlowException("流程处理中，请稍候！");
		} else {
			flowRunningSet.add(FLOWRUNNING, runKey);
		}
		
		//检查流程状态
		String result = checkInstanceStatus(approvalDto.getInstanceId());
		if(!"success".equals(result)) {
			flowRunningSet.remove(FLOWRUNNING, runKey);
			throw new FlowException(result);
		}
		
		//检查任务是否已完成
		if(instanceTaskService.isFinished(approvalDto)) {
			flowRunningSet.remove(FLOWRUNNING, runKey);
			throw new FlowException("任务已被处理");
		}
		String instanceId="";
		try {
			//1、查询审批列表，目标数据集
			InstanceUnit instanceUnit = this.getInstanceUnit(approvalDto.getInstanceId());
			if(instanceUnit!=null && instanceUnit.getId()!=null){
				instanceId=instanceUnit.getId();
			}
			//2、根据操作处理:
			Operation operation = new OperationFactory().newOperation(approvalDto.getOperationType(),this);
		
			operation.action(instanceUnit, approvalDto);
			//审批成功保存用户默认意见。需求变更，此方法去掉20171121
			/*String userOpinionId = approvalDto.getUserOpinionId();
			if(org.apache.commons.lang.StringUtils.isNotBlank(userOpinionId)){
				opinionServcie.saveDefaultOpinion(userOpinionId);
			}*/
		} catch (Exception e) {
			log.info(e);
			throw e;
		} finally {
			flowRunningSet.remove(FLOWRUNNING, runKey);
		}
		
		log.info("审批执行成功！instanceId=" + instanceId);
		return true;
	}
	
	private String checkInstanceStatus(String instanceId) throws Exception {
		String message = "success";
		Instance instance = this.getObjectById(instanceId);
		if(InstanceStatus.WITHDRAW.getValue().equals(instance.getStatus())) {
			message = "流程已被撤回";
		} else if(InstanceStatus.REJECT.getValue().equals(instance.getStatus())) {
			message = "流程已被打回";
		} else if(InstanceStatus.CANCEL.getValue().equals(instance.getStatus())) {
			message = "流程已被作废";
		}
		return message;
	}
	
	private InstanceUnit getInstanceUnit(String instanceId) throws Exception {
		List<ApprovalList> approvalList = queryApprovalList(instanceId, null);
		Instance instance = this.getObjectById(instanceId);
		log.info("审核列表：" + approvalList);
		log.info("实例信息：" + instance);
		
		return this.translate(instance, approvalList);
	}

	@Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String saveAllInstanceData(InstanceDto instanceDto) throws Exception {
		
		log.info("--------------流程实例初始化数据saveAllInstanceData------------------" + JacksonUtils.toJson(instanceDto));
		
		//1、保存流程实例
		Instance instance = generateInstanceData(instanceDto);
		
		//2、保存流程变量
		List<InstanceVariable> varList = generateInstanceVariable(instanceDto, instance);
		
	    List<InstanceAc> acList = new ArrayList<InstanceAc>();
	    Map<String, InstanceAc> acMap = new HashMap<String, InstanceAc>();	//key=nodeid,一次保存中各节点的nodeid不能重复
	    
	    List<InstancePost> posts = new ArrayList<InstancePost>();
	    List<InstanceGroup> groups = new ArrayList<InstanceGroup>();
	    List<InstanceCs> csList = new ArrayList<InstanceCs>();
	    List<InstanceStep> stepList = new ArrayList<InstanceStep>();
	    InstanceTask startTask = null;
	    
	    //3、保存AC实例
	    long acIndex = 0;
	    List<AcDto> acDtoList = instanceDto.getAcDtoList();
		String flId = acDtoList.get(0).getFlId();
	    for(AcDto acDto : acDtoList) {
	    	InstanceAc instanceAc = generateInstanceAc(instanceDto, acDto, ++acIndex);
			acList.add(instanceAc);
			acMap.put(acDto.getNodeId(), instanceAc);
			
			//6、保存流程抄送人
			csList.addAll(generateCsData(instance, instanceAc, acDto));
			
			//4、保存AC对应的岗位
			long postIndex = 0;
			String postStr = acDto.getPosts();
			if(!StringUtils.isEmpty(postStr) && postStr.contains("\'")) {
				postStr = postStr.replaceAll("\'", "\\\\'");
			}
			List<PostDto> postList = JacksonUtils.fromJson(postStr, ArrayList.class, PostDto.class);
			
			//手选时存在两个岗位相同的情况
			deleteDuplicate(postList);
			
			if(CollectionUtils.isEmpty(postList)) {
				//join节点时记录已到达分支数，判断是否往下走
				if(FlAcType.JOIN.getAcType().equals(acDto.getAcType())) {
					instanceAc.setLeftPost(acDto.getPreviousAcDtos().size());
				}
				continue;
				
			} else {
				//普通节点初始剩余未处理岗位数
				instanceAc.setLeftPost(postList.size());
			}
			for(PostDto postDto : postList) {
				InstancePost post = generatePosts(postDto, instanceAc.getId(), ++postIndex);
				posts.add(post);
				
				//5、保存岗位对应的group
				long groupIndex = 0;
				int skipPerson = 0;
				List<UserDto> userList = postDto.getUsers();
				for(UserDto user : userList) {
					
					//此处空的人应该在发起流程时处理。
					if(StringUtils.isEmpty(user.getId())) {
						skipPerson++;
						continue;
					}
					String[] ids = user.getId().split(",");
					String[] names = user.getName().split(",");
					
					post.setLeftPerson(post.getLeftPerson() + ids.length);
					
					for(int i=0; i<ids.length; i++) {
						InstanceGroup group = generateGroup(ids[i], names[i], post, instanceAc, ++groupIndex);
						groups.add(group);
						
						//为发起人初始化任务(只初始化数据，状态在启动时设置)
						if(acIndex == 1 && postIndex == 1 && groupIndex == 1) {
							
							startTask = generateStartTask(group, instanceDto);	//发起节点默认只有一个人
						}
					}
				}
				
				if(CollectionUtils.isEmpty(userList)) {
					continue;
				} else {
					post.setLeftPerson(post.getLeftPerson() - skipPerson);	//初始剩余未处理人数据
				}
			}
	    }
	   /* Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("flId",flId);
		paramMap.put("delflag",false);
	    List<Step> flStepList = stepService.queryList(paramMap);
		Map<String,String> stepNameMap = new HashMap<String,String>();
		for (Step step : flStepList) {
			String sourceId = step.getSourceId();
			sourceId = sourceId==null?"":sourceId;
			String targetId = step.getTargetId();
			String stepName = step.getName();
			stepNameMap.put(sourceId+"_"+targetId,stepName);
		}*/
		//设置环节的上下环节
	    setPreAndNextAc(acList, acDtoList, acMap);
	    
	    //保存实例连线
	    stepList = generateInstanceStep(acList);
	    
	    setAutoPass(groups, instance);
		
		//7、保存流程可阅人
	    List<InstanceAccessible> accessibleList = generateAccessibleData(instanceDto,instance);
	    
	    //8、生成表单数据列表
	    List<PcForm> pcForms = new ArrayList<PcForm>();
	    List<MobileForm> mobileForms = new ArrayList<MobileForm>();
	    generateFormData(instance,instanceDto,pcForms,mobileForms);

	    //9、生成UploadAttachment列表
	    List<UploadAttachment> attachmentList = new ArrayList<UploadAttachment>(); 
	    generateUploadAttachmentData(instance, instanceDto, attachmentList);
		
		//8、保存数据
	    Instance instanceDb=instanceDao.getObjectById(instance.getId());
	    if(instanceDb!=null){
	    	throw new FlowException("流程【" + instanceDto.getName() + "】已运行！");
	    }else{
	    	instanceDao.save(instance);
	    }
	    
//	    Instance newer = instanceDao.getObjectById(instance.getId());
//	    newer.setName("[" + newer.getSerialNo() + "]" + newer.getName());
//	    
//	    newer.setStartUserName(StringUtil.convertSingleQuotes(newer.getStartUserName()));
//	    newer.setCreatePersonName(StringUtil.convertSingleQuotes(newer.getCreatePersonName()));
//	    newer.setUpdatePersonName(StringUtil.convertSingleQuotes(newer.getUpdatePersonName()));
//	    
//	    instanceDao.update(newer);
	    log.info("===1.2 流程实例保存成功能： instanceId=" + instance.getId());
	    instanceVariableService.saveBatch(varList);
	    log.info("===1.3 流程变量保存成功：size=" + varList.size());
	    instanceAcService.saveBatch(acList);
	    log.info("===1.4 流程环节保存成功：size" + acList.size());
	    instancePostDao.saveBatch(posts);
	    log.info("===1.5 流程岗位保存成功：size=" + posts.size());
	    instanceGroupService.saveBatch(groups);
	    log.info("===1.6 流程审处理人保存成功： size=" + groups.size());
	    instanceAccessibleService.saveBatch(accessibleList);
	    log.info("===1.7 流程可阅人保存成功： size=" + accessibleList.size());	  
	    instanceCsService.saveBatch(csList);
	    log.info("===1.8 流程抄送人保存成功： size=" + csList.size());	  
	    instanceStepService.saveBatch(stepList);
	    log.info("===1.9 环节连线保存成功： size=" + stepList.size());	  
	    pcFormService.saveBatch(pcForms);
	    log.info("===1.10 pc表单数据保存成功： size=" + pcForms.size());	 
	    mobileFormService.saveBatch(mobileForms);
	    log.info("===1.11 mobile表单数据保存成功： size=" + mobileForms.size());	
	    
	    if(attachmentList!=null && attachmentList.size()>0){
	    	log.info("================ uploadAttachmentService.saveBatch() attachmentList="+JacksonUtils.toJson(attachmentList));
	    	uploadAttachmentService.saveBatch(attachmentList);
	    }
	    
	    //发起人为空，场景：发起人的主岗为空
	    if(startTask == null) {
	    	throw new FlowException("流程【" + instanceDto.getName() + "】发起人为空！");
	    }	
//	    } else {
//		    instanceTaskService.save(startTask);
//		    log.info("===1.7 流程任务保存成功  taskId=" + startTask.getId());
//		    log.info("===1.8、实例数据保存成功：instanceId = " + instanceDto.getId());
//	    }
//	    throw new FlowException("test");
	    
	    //启动流程
	    start(instanceDto);
	    log.info("===2、流程启动成功：instanceId = " + instanceDto.getId());
		return instanceDto.getId();
	}
	
	private void deleteDuplicate(List<PostDto> postList) {
		if(CollectionUtils.isEmpty(postList)) {
			return ;
		}
		Map<String, PostDto> filter = new HashMap<String, PostDto>();
		Iterator<PostDto> iter = postList.iterator();
		while(iter.hasNext()) {
			PostDto next = iter.next();
			String postId = next.getId();
			if(filter.containsKey(postId)) {
				PostDto postDto = filter.get(postId);
				postDto.getUsers().addAll(next.getUsers());
				iter.remove();
			} else {
				filter.put(postId, next);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String saveAllInstanceDataForMobile(InstanceDto instanceDto) throws Exception {
		
		log.info("--------------流程实例初始化数据saveAllInstanceData------------------" + JacksonUtils.toJson(instanceDto));
		
		//1、保存流程实例
		Instance instance = generateInstanceData(instanceDto);
		
		//2、保存流程变量
		List<InstanceVariable> varList = generateInstanceVariable(instanceDto, instance);
		
		List<InstanceAc> acList = new ArrayList<InstanceAc>();
		Map<String, InstanceAc> acMap = new HashMap<String, InstanceAc>();	//key=nodeid,一次保存中各节点的nodeid不能重复
		
		List<InstancePost> posts = new ArrayList<InstancePost>();
		List<InstanceGroup> groups = new ArrayList<InstanceGroup>();
		List<InstanceCs> csList = new ArrayList<InstanceCs>();
		List<InstanceStep> stepList = new ArrayList<InstanceStep>();
		InstanceTask startTask = null;
		
		//3、保存AC实例
		long acIndex = 0;
		List<AcDto> acDtoList = instanceDto.getAcDtoList();
		for(AcDto acDto : acDtoList) {
			InstanceAc instanceAc = generateInstanceAc(instanceDto, acDto, ++acIndex);
			acList.add(instanceAc);
			acMap.put(acDto.getNodeId(), instanceAc);
			
			//6、保存流程抄送人
			csList.addAll(generateCsData(instance, instanceAc, acDto));
			
			//4、保存AC对应的岗位
			long postIndex = 0;
			List<PostDto> postList = JacksonUtils.fromJson(acDto.getPosts(), ArrayList.class, PostDto.class);
			if(CollectionUtils.isEmpty(postList)) {
				//join节点时记录已到达分支数，判断是否往下走
				if(FlAcType.JOIN.getAcType().equals(acDto.getAcType())) {
					instanceAc.setLeftPost(acDto.getPreviousAcDtos().size());
				}
				continue;
				
			} else {
				//普通节点初始剩余未处理岗位数
				instanceAc.setLeftPost(postList.size());
			}
			for(PostDto postDto : postList) {
				InstancePost post = generatePosts(postDto, instanceAc.getId(), ++postIndex);
				posts.add(post);
				
				//5、保存岗位对应的group
				long groupIndex = 0;
				List<UserDto> userList = postDto.getUsers();
				if(CollectionUtils.isEmpty(userList)) {
					continue;
				} else {
					post.setLeftPerson(userList.size());	//初始剩余未处理人数据
				}
				for(UserDto user : userList) {
					
					if(user.getId() == null) {
						continue;
					}
					String[] ids = user.getId().split(",");
					String[] names = user.getName().split(",");
					
					post.setLeftPerson(ids.length);
					
					for(int i=0; i<ids.length; i++) {
						InstanceGroup group = generateGroup(ids[i], names[i], post, instanceAc, ++groupIndex);
						groups.add(group);
						
						//为发起人初始化任务(只初始化数据，状态在启动时设置)
						if(acIndex == 1 && postIndex == 1 && groupIndex == 1) {
							
							startTask = generateStartTask(group, instanceDto);	//发起节点默认只有一个人
						}
					}
				}
			}
		}
		
		//设置环节的上下环节
		setPreAndNextAc(acList, acDtoList, acMap);
		
		//保存实例连线
		stepList = generateInstanceStep(acList);
		
		setAutoPass(groups, instance);
		
		//7、保存流程可阅人
		List<InstanceAccessible> accessibleList = generateAccessibleData(instanceDto,instance);
		
		//8、生成表单数据列表
		List<PcForm> pcForms = new ArrayList<PcForm>();
		List<MobileForm> mobileForms = new ArrayList<MobileForm>();
		generateFormData(instance,instanceDto,pcForms,mobileForms);
		
		//9、生成UploadAttachment列表
		List<UploadAttachment> attachmentList = new ArrayList<UploadAttachment>(); 
		generateUploadAttachmentData(instance, instanceDto, attachmentList);
		
		//8、保存数据
		instanceDao.save(instance);
		log.info("===1.2 流程实例保存成功能： instanceId=" + instance.getId());
		instanceVariableService.saveBatch(varList);
		log.info("===1.3 流程变量保存成功：size=" + varList.size());
		instanceAcService.saveBatch(acList);
		log.info("===1.4 流程环节保存成功：size" + acList.size());
		instancePostDao.saveBatch(posts);
		log.info("===1.5 流程岗位保存成功：size=" + posts.size());
		instanceGroupService.saveBatch(groups);
		log.info("===1.6 流程审处理人保存成功： size=" + groups.size());
		instanceAccessibleService.saveBatch(accessibleList);
		log.info("===1.7 流程可阅人保存成功： size=" + accessibleList.size());	  
		instanceCsService.saveBatch(csList);
		log.info("===1.8 流程抄送人保存成功： size=" + csList.size());	  
		instanceStepService.saveBatch(stepList);
		log.info("===1.9 环节连线保存成功： size=" + stepList.size());	  
		pcFormService.saveBatch(pcForms);
		log.info("===1.10 pc表单数据保存成功： size=" + pcForms.size());	 
		mobileFormService.saveBatch(mobileForms);
		log.info("===1.11 mobile表单数据保存成功： size=" + mobileForms.size());	
		
		if(attachmentList!=null && attachmentList.size()>0){
			log.info("================ uploadAttachmentService.saveBatch() attachmentList="+JacksonUtils.toJson(attachmentList));
			uploadAttachmentService.saveBatch(attachmentList);
		}
		
		//发起人为空，场景：发起人的主岗为空
		if(startTask == null) {
			throw new FlowException("流程【" + instanceDto.getName() + "】发起人为空！");
		}	
//	    } else {
//		    instanceTaskService.save(startTask);
//		    log.info("===1.7 流程任务保存成功  taskId=" + startTask.getId());
//		    log.info("===1.8、实例数据保存成功：instanceId = " + instanceDto.getId());
//	    }
//	    throw new FlowException("test");
		
		//启动流程
		start(instanceDto);
		log.info("===2、流程启动成功：instanceId = " + instanceDto.getId());
		return instanceDto.getId();
	}

	private void generateUploadAttachmentData(Instance instance, InstanceDto instanceDto,
			List<UploadAttachment> attachmentList) throws Exception {
		log.info("================ generateUploadAttachmentData()  UploadAttachmentDtoList="+JacksonUtils.toJson(instanceDto.getUploadAttachmentDtoList()));
		List<UploadAttachmentDto> attachmentDtoList = instanceDto.getUploadAttachmentDtoList();
		if (attachmentDtoList != null) {
			for (UploadAttachmentDto uploadAttachmentDto : attachmentDtoList) {
				UploadAttachment uploadAttachment = new UploadAttachment();
				// BeanUtils.copyProperties(uploadAttachment,
				// uploadAttachmentDto);//old-20170718
				BeanUtils.copyProperties(uploadAttachmentDto, uploadAttachment);// new-20170718
				uploadAttachment.setId(IDGenerator.getUUID());
				uploadAttachment.setDelflag(false);
				uploadAttachment.setInstanceId(instance.getId());
				uploadAttachment.setReadType("1");
				attachmentList.add(uploadAttachment);
			}
		}
	}

	/**
	 * 设置前后节点
	 * @param acList
	 * @param acDtoList
	 * @param acMap：<nodeid, Ac>:一次保存中各节点的nodeid不能重复
	 */
	private void setPreAndNextAc(List<InstanceAc> acList, List<AcDto> acDtoList, Map<String, InstanceAc> acMap) {
		for(int i=0; i<acList.size(); i++) {
			AcDto acDto = acDtoList.get(i);
			List<String> previousAcNodeIds = acDto.getPreviousAcDtos();
			List<String> nextNodeNodeIds = acDto.getNextNodeDtos();

			log.info("流程发起时前后环节关系：");
			log.info("当前环节：[" + acDto.getNodeId() + ", " + acDto.getName() + "], 上一环节：[" + previousAcNodeIds + "], 下一环节：["+ nextNodeNodeIds  + "]");
			
			InstanceAc ac = acList.get(i);
			ac.setPreAcIds(getAcIds(previousAcNodeIds, acMap));
			ac.setNextAcIds(getAcIds(nextNodeNodeIds, acMap));
		}
	}

	private String getAcIds(List<String> acNodeIds, Map<String, InstanceAc> acMap) {
		if(CollectionUtils.isEmpty(acNodeIds)) {
			return "";
		}
		
		//排重
		LinkedHashSet<String> acIdSet = new LinkedHashSet<String>(acNodeIds);
		
		StringBuilder acIds = new StringBuilder();
		for(String nodeId : acIdSet) {
			String acId = acMap.get(nodeId).getId();
			acIds.append(acId).append(",");
		}
		acIds.deleteCharAt(acIds.length() - 1);
		return acIds.toString();
	}

	private List<InstanceStep> generateInstanceStep(List<InstanceAc> acList) {
		Map<String, InstanceAc> acMap = new HashMap<String, InstanceAc>();
		for(InstanceAc ac : acList) {
			acMap.put(ac.getId(), ac);
		}
		List<InstanceStep> stepList = new ArrayList<InstanceStep>();
		for(InstanceAc ac: acList) {
			String nextAcIds = ac.getNextAcIds();
			if(!StringUtils.isEmpty(nextAcIds)) {
				String[] nextAcIdArray = nextAcIds.split(",");
				for(String acId : nextAcIdArray) {
					InstanceStep step = new InstanceStep();
					step.setId(IDGenerator.getUUID());
					//step.setName(ac.getName() + "->" + acMap.get(acId).getName());
					step.setFiId(ac.getFiId());
					step.setSourceId(ac.getId());
					step.setTargetId(acId);
					stepList.add(step);
				}
			}
		}
		return stepList;
	}

	private List<InstanceStep> generateInstanceStep(List<InstanceAc> acList,Map stepMap) {
		Map<String, InstanceAc> acMap = new HashMap<String, InstanceAc>();
		for(InstanceAc ac : acList) {
			acMap.put(ac.getId(), ac);
		}
		List<InstanceStep> stepList = new ArrayList<InstanceStep>();
		for(InstanceAc ac: acList) {
			String nextAcIds = ac.getNextAcIds();
			if(!StringUtils.isEmpty(nextAcIds)) {
				String[] nextAcIdArray = nextAcIds.split(",");
				for(String acId : nextAcIdArray) {
					InstanceStep step = new InstanceStep();
					step.setId(IDGenerator.getUUID());
					step.setName(ac.getName() + "->" + acMap.get(acId).getName());
					step.setFiId(ac.getFiId());
					step.setSourceId(ac.getId());
					step.setTargetId(acId);
					stepList.add(step);
				}
			}
		}
		return stepList;
	}

	/**
	 * 流程启动时根据模板配置，设置审批人是否自动通过标志
	 * 
	 * @param groups
	 * @param instance
	 */
	private void setAutoPass(List<InstanceGroup> groups, Instance instance) {
		String approvalRepeat = instance.getApprovalRepeat();
		if(ApproverRepeatHandleType.NOT_SKIP.getValue().equals(approvalRepeat)) {
			return ;
		}
		Map<String, List<InstanceGroup>> filterMap = new HashMap<String, List<InstanceGroup>>();
		for(InstanceGroup group : groups) {
			String participantId = group.getParticipantId();
			List<InstanceGroup> sameApprovers = filterMap.get(participantId);
			if(sameApprovers == null) {
				sameApprovers = new ArrayList<InstanceGroup>();
				filterMap.put(participantId, sameApprovers);
			}
			sameApprovers.add(group);
		}
		
		for(List<InstanceGroup> sameApprovers : filterMap.values()) {
			if(sameApprovers != null && sameApprovers.size() > 1) {
				if(ApproverRepeatHandleType.FIRST.getValue().equals(approvalRepeat)) {
					for(int i=1; i<sameApprovers.size(); i++) {
						sameApprovers.get(i).setAutoPass(AutoPassType.AUTO_PASS.getValue());
						sameApprovers.get(0).setAutoPass(AutoPassType.NOT_PASS.getValue());
					}
				} else if(ApproverRepeatHandleType.LAST.getValue().equals(approvalRepeat)) {
					for(int i=0; i<sameApprovers.size() - 1; i++) {
						sameApprovers.get(i).setAutoPass(AutoPassType.AUTO_PASS.getValue());
						sameApprovers.get(sameApprovers.size() - 1).setAutoPass(AutoPassType.NOT_PASS.getValue());
					}
				}
			}
		}
	}

	private InstanceTask generateStartTask(InstanceGroup group, InstanceDto instanceDto) {
		InstanceTask startTask = new InstanceTask();
	    startTask.setId(IDGenerator.getUUID());
	    startTask.setGroupId(group.getId());
	    startTask.setType(TaskType.STARTER.getValue());
	    startTask.setActivateDate(new Timestamp(System.currentTimeMillis()));
	    startTask.setOperationCode("start");
	    startTask.setOperationName("发起流程");
	    startTask.setUserNote(instanceDto.getUserNote());
	    startTask.setStatus(TaskStatus.NOT_RUNNING.getValue());
	    startTask.setApproverId(instanceDto.getStartUserId());
		return startTask;
	}

	/**
	 * 流程实例数据设置为启动状态：启动流程实例（已保存实例数据）
	 * 1、开始节点状态置为已完成
	 * 2、查询下一节点数据
	 * 3、将下一节点数据置为运行中
	 * 
	 * @throws Exception 
	 */
	private boolean start(InstanceDto instanceDto) throws Exception {
		String instanceId = instanceDto.getId();
		List<ApprovalList> acList = this.queryApprovalList(instanceId, null);
		Instance instance = this.getObjectById(instanceId);
		InstanceUnit instanceUnit = translate(instance, acList);
		
		ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
		approvalDto.setOperationName("发起流程");
		approvalDto.setOperationType(OperationType.START.getCode());
		approvalDto.setUserNote(instanceDto.getUserNote());
		approvalDto.setInstanceIdBeforeReturn(instanceDto.getInstanceIdBeforeReturn());
		Operation startOperation = new OperationFactory().newOperation(OperationType.START.getCode(), this);
		startOperation.action(instanceUnit, approvalDto);
		
		return true;
	}
	
	/**
	 * 1、将扁平的结构转换成多层结构
	 * 
	 * @param instance
	 * @param approvalList
	 * @return
	 */
	private InstanceUnit translate(Instance instance, List<ApprovalList> approvalList) {
		InstanceUnit instanceUnit = new InstanceUnit();
		
		//1、流程实例转换
		fillInstanceUnit(instanceUnit, instance, approvalList);
		
		//2、环节转换
		Map<String, ACUnit> acMap = fillACUnit(instanceUnit, approvalList);
		
		//3、岗位转换
		Map<String, PostUnit> postMap = fillPost(acMap, approvalList);
		
		//4、人员（组）转换
		fillApprovers(postMap, approvalList);
		
		return instanceUnit;
	}
	
	/**
	 * 1、将扁平的结构转换成多层结构
	 * 
	 * @param instanceUnit
	 * @param approvalList
	 * @return
	 */
	public InstanceUnit translate(InstanceUnit instanceUnit, List<ApprovalList> approvalList) {
		InstanceUnit newInstanceUnit = new InstanceUnit();
		newInstanceUnit.setId(instanceUnit.getId());
		newInstanceUnit.setFlId(instanceUnit.getFlId());
		newInstanceUnit.setFlCode(instanceUnit.getFlCode());
		
		//2、环节转换
		Map<String, ACUnit> acMap = fillACUnit(newInstanceUnit, approvalList);
		
		//3、岗位转换
		Map<String, PostUnit> postMap = fillPost(acMap, approvalList);
		
		//4、人员（组）转换
		fillApprovers(postMap, approvalList);
		
		return newInstanceUnit;
	}

	private void fillApprovers(Map<String, PostUnit> postMap, List<ApprovalList> approvalList) {
		String preGroupKey = null;
		for(ApprovalList row : approvalList) {
			String groupKey = row.getGroupKey();
			if(StringUtils.isEmpty(groupKey)) {
				continue;
			}
			if(!groupKey.equals(preGroupKey)) {
				preGroupKey = groupKey;
				ApproverUnit approver = new ApproverUnit();
				approver.setId(groupKey);
				approver.setAcPostId(row.getAcPostId());
				approver.setApproverId(row.getApproverId());
				approver.setApproverName(row.getApproverName());
				approver.setApproverSeq(row.getApproverSeq());
				approver.setAutoPass(row.getAutoPass());
				
				approver.setStatus(row.getApproverStatus());
				
				//代理相关
				approver.setProxy(row.getProxy());
				approver.setProxyed(row.getProxyed());
				approver.setProxyType(row.getProxyType());
				
				//页面调整环节时使用！
				approver.setDbAction(row.getChangeType());
				
				TaskUnit task = new TaskUnit();
				task.setTaskId(row.getTaskId());
				task.setTaskType(row.getTaskType());
				task.setTaskStatus(row.getTaskStatus());
				task.setStartTime(row.getTaskStartTime());
				task.setEndTime(row.getTaskEndTime());
				task.setTaskResult(row.getTaskResult());
				task.setTaskResultName(row.getTaskResultName());
				task.setTaskComments(row.getTaskComments());
				task.setFromId(row.getFromId());
				task.setFromName(row.getFromName());
				task.setMsgId(row.getMsgId());
				approver.setTask(task);
				
				PostUnit currentPost = postMap.get(row.getAcPostId());
				approver.setOwner(currentPost);
				
				List<ApproverUnit> approvers = currentPost.getApprovers();
				if(approvers == null) {
//					approvers = new ArrayList<ApproverUnit>();
					approvers = new CopyOnWriteArrayList<ApproverUnit>();
					currentPost.setApprovers(approvers);
				}
				approvers.add(approver);
			}
		}
	}

	private Map<String, PostUnit> fillPost(Map<String, ACUnit> acMap, List<ApprovalList> approvalList) {
		Map<String, PostUnit> postMap = new HashMap<String, PostUnit>();
		String preAcPostId = null;
		for(int i=0; i<approvalList.size(); i++) {
			ApprovalList row = approvalList.get(i);
			String acPostId = row.getAcPostId();
			if(StringUtils.isEmpty(acPostId)) {
				continue;
			}
			
			//算同岗位的人数
			log.info("fillPost内：acPostId=" + acPostId + ", preAcPostId" + preAcPostId);
			if(acPostId.equals(preAcPostId)) {
				ApprovalList pre = approvalList.get(i - 1);
				row.setAcPostId(pre.getAcPostId());
			} else {
				preAcPostId = row.getAcPostId();
				PostUnit postUnit = new PostUnit();
				postUnit.setPostId(row.getPostId());
				postUnit.setPostName(row.getPostName());
				postUnit.setPostStatus(row.getPostStatus());
				postUnit.setPostSeq(row.getPostSeq());
				postUnit.setStartTime(row.getPostStartTime());
				postUnit.setEndTime(row.getPostEndTime());
				postUnit.setAcId(row.getAcId());
				
				if(row.getChangeType() == 1) {
					row.setAcPostId(IDGenerator.getUUID());
				}
				
				postUnit.setId(row.getAcPostId());
				postUnit.setLeftPerson(row.getLeftPerson());
				
//				if(row.getChangeType() == 1) {
					postUnit.setDbAction(row.getChangeType());
//				}
				log.info("fillPost内：postId=" + postUnit.getId() + ", dbaction=" + postUnit.getDbAction());
				
				ACUnit currentAc = acMap.get(row.getAcId());
				if(currentAc != null) {
					postUnit.setOwner(currentAc);
					
					List<PostUnit> posts = currentAc.getPosts();
					if(posts == null) {
	//					posts = new ArrayList<PostUnit>();	//在遍历过程中，有可能出现删除元素的情况，如跳过审批人等 
						posts = new CopyOnWriteArrayList<PostUnit>();
						currentAc.setPosts(posts);
					}
					posts.add(postUnit);
				}
				postMap.put(postUnit.getId(), postUnit);
			}
		}
		return postMap;
	}

	private Map<String, ACUnit> fillACUnit(InstanceUnit instanceUnit, List<ApprovalList> approvalList) {
		Map<String, ACUnit> acMap = new HashMap<String, ACUnit>();
		List<ACUnit> acList = new ArrayList<ACUnit>();
		String preAcId = null;
		int preChangeType = 0;
		for(ApprovalList row : approvalList) {
			if(!row.getAcId().equals(preAcId)
//					|| row.getChangeType() != preChangeType) {	//#15486,修改当前审批人时！TODO
					&& row.getChangeType() != 2) {
				preAcId = row.getAcId();
				preChangeType = row.getChangeType();
				ACUnit acUnit = new ACUnit();
				acUnit.setAcId(row.getAcId());
				acUnit.setAcCode(row.getAcCode());
				acUnit.setAcName(row.getAcName());
				acUnit.setAcStatus(row.getAcStatus());
				acUnit.setAcType(row.getAcType());
				acUnit.setAcStartTime(row.getAcStartTime());
				acUnit.setAcEndTime(row.getAcEndTime());
				acUnit.setApprovalTypeId(row.getApprovalTypeId());
				acUnit.setApprovalType(row.getApprovalType());
				acUnit.setAddLabel(row.isAddLabel());
				acUnit.setMultiPerson(row.getMultiPerson());
				acUnit.setMultiPost(row.getMultiPost());
				acUnit.setAcPx(row.getAcPx());
				acUnit.setReturnPx(row.getReturnPx());
				acUnit.setSource(row.getSource());
				acUnit.setLeftPost(row.getLeftPost());
				acUnit.setCcPerson(getCCPerson(row));
				acUnit.setPreAcIds(row.getPreAcIds());
				acUnit.setNextAcIds(row.getNextAcIds());
				log.info("acid=" + acUnit.getAcId() + ", 下一环节关联ID=" + acUnit.getNextAcIds());
				acUnit.setFromReturn(row.getFromReturn());
				acUnit.setApproverNull(row.getApproverNull());
				acUnit.setPostNull(row.getPostNull());
				
				//新增环节
				if(row.getChangeType() == 1) {
					acUnit.setDbAction(1);
					acUnit.setLeftPost(acUnit.getLeftPost() + 1);
				}
				if(row.getChangeType() == 2) {
					acUnit.setDbAction(2);
				}
				//修改环节
				if(row.getChangeType() == 3) {
					acUnit.setChange(true);
				}
				
				acUnit.setOwner(instanceUnit);
				
				acList.add(acUnit);
				acMap.put(acUnit.getAcId(), acUnit);
			}
		}
		
		setPreAndNextAc(acList, acMap);
		
		instanceUnit.setAcList(acList);
		return acMap;
	}
	
	private void setPreAndNextAc(List<ACUnit> acList, Map<String, ACUnit> acMap) {
		for(int i=0; i<acList.size(); i++) {
			ACUnit acUnit = acList.get(i);
			String preAcIds = acUnit.getPreAcIds();
			if(!StringUtils.isEmpty(preAcIds)) {
				List<ACUnit> preAcs = new ArrayList<ACUnit>();
				String[] preAcIdArray = preAcIds.split(",");
				for(String preAcId : preAcIdArray) {
					ACUnit pre = acMap.get(preAcId);
					if(pre != null) {
						preAcs.add(pre);
					}
				}
				acUnit.setPreAcs(preAcs);
			}
			
			String nextAcIds = acUnit.getNextAcIds();
			if(!StringUtils.isEmpty(nextAcIds)) {
				List<ACUnit> nextAcs = new ArrayList<ACUnit>();
				String[] nextAcIdArray = nextAcIds.split(",");
				for(String nextAcId : nextAcIdArray) {
					ACUnit next = acMap.get(nextAcId);
					if(next != null) {
						nextAcs.add(next);
					}
				}
				acUnit.setNextAcs(nextAcs);
			}
		}
	}

	private List<UserDto> getCCPerson(ApprovalList row) {
		String ccIds = row.getCcIds();
		String ccNames = row.getCcNames();
		if(!StringUtils.isEmpty(ccIds)) {
			List<UserDto> ccPersons = new ArrayList<UserDto>();
			String[] idArray = ccIds.split(",");
			String[] nameArray = ccNames.split(",");
			for(int i=0; i<idArray.length; i++) {
				ccPersons.add(new UserDto(idArray[i], nameArray[i]));
			}
			return ccPersons;
		}
		return null;
	}

	/**
	 * @param approvalList 
	 * @param instanceUnit
	 * @param instance
	 * @throws
	 * @throws  
	 */
	private void fillInstanceUnit(InstanceUnit instanceUnit, Instance instance, List<ApprovalList> approvalList){
//		try {
//			// TODO 有些字段不统一，先统一copy一份
//			BeanUtils.copyProperties(instanceUnit, instance);
//		} catch (IllegalAccessException | InvocationTargetException e) {
//			throw new RuntimeException(e);
//		}
		instanceUnit.setId(instance.getId());
		instanceUnit.setCode(instance.getCode());
		instanceUnit.setName(instance.getName());
		instanceUnit.setStatus(instance.getStatus());
		instanceUnit.setAppId(instance.getAppId());
		instanceUnit.setFlId(instance.getFlId());
		instanceUnit.setBusinessObjectId(instance.getBusinessObjectId());
		instanceUnit.setBusinessId(instance.getBusinessId());
		instanceUnit.setRetract(instance.getRetract());
		instanceUnit.setPostMultiPerson(instance.getPostMultiPerson());
		instanceUnit.setTendId(instance.getTendId());
		instanceUnit.setBusinessObjectCode(instance.getBusinessObjectCode());
		instanceUnit.setCustomFormId(instance.getCustomFormId());
		instanceUnit.setCustomFormURL(approvalList.get(0).getPcUrl());
		
		instanceUnit.setApproverNullStrategy(instance.getApprovalPersonIsNull());
		instanceUnit.setApproverRepaet(instance.getApprovalRepeat());
		instanceUnit.setPostNull(instance.getPostIsNull());
		instanceUnit.setBusinessCorpId(instance.getFlowBusinessCompanyId());
		instanceUnit.setBusinessDeptId(instance.getFlowBusinessDeptId());
		instanceUnit.setBusinessProjectId(instance.getFlowBusinessProjectId());
		instanceUnit.setBusinessProjectStage(instance.getFlowBusinessProjectBranchId());
		instanceUnit.setDoArchive(instance.getDoArchive());
		instanceUnit.setStartDate(instance.getStartDate());
		instanceUnit.setStartUserName(instance.getStartUserName());
		instanceUnit.setReturnRepeatApproval(instance.getReturnRepeatApproval());
		instanceUnit.setFlCode(approvalList.get(0).getFlCode());
	}

	private InstanceGroup generateGroup(String userId, String userName, InstancePost post, InstanceAc instanceAc, long groupIndex) {
		InstanceGroup instanceGroup = new InstanceGroup();
		instanceGroup.setAcId(instanceAc.getId());
		instanceGroup.setAcPostId(post.getId());
//    	instanceGroup.setParseType(postDto.getParseType()); TODO zhangdaoqiang	
		instanceGroup.setPostId(post.getPostId());
		instanceGroup.setPostName(post.getPostName());
		instanceGroup.setParticipantId(userId);
		instanceGroup.setParticipantName(userName);
		instanceGroup.setPx(groupIndex);
		instanceGroup.setSource("1");//1-模板; 2-加签
//    	instanceGroup.setSourceId(flId);//flId
		instanceGroup.setDisable(false);
		instanceGroup.setId(IDGenerator.getUUID());
		instanceGroup.setStatus(ApproverStatus.NOT_RUNNING.getValue());
		return instanceGroup;
	}

	private InstanceAc generateInstanceAc(InstanceDto instanceDto, AcDto acDto, long acIndex) throws Exception {
		String flId = instanceDto.getFlId();
	    String instanceId = instanceDto.getId();
    	InstanceAc instanceAc = new InstanceAc();
    	//BeanUtils.copyProperties(instanceAc, acDto);//将ac的属性copy到instanceAc上//old-20170718
    	BeanUtils.copyProperties(acDto, instanceAc );//new 20170718
    	//ac_type、approve_type_id、x、y、width、height、is_add_label、approve_strategy、post_multiperson、
    	//source、source_id需要设置，status默认是1-未运行；
    	instanceAc.setSource("1");//1：模板，2加签 ，3 前置代理  4后置代理
		instanceAc.setSourceId(flId);
		instanceAc.setFiId(instanceId);
		instanceAc.setId(IDGenerator.getUUID());
    	instanceAc.setCode(acDto.getCode()+"-"+DateUtils.getDateSSSText());
    	instanceAc.setName(acDto.getName());
    	instanceAc.setPx(acIndex);
    	instanceAc.setStatus(ACStatus.NOT_RUNNING.getValue());
    	
    	System.out.println("\n>>> save(instanceAc) is done. instanceAc="+JacksonUtils.toJson(instanceAc));
		return instanceAc;
	}

	private InstancePost generatePosts(PostDto postDto, String acId, long postIndex) {
		InstancePost post = new InstancePost(); 
		post.setId(IDGenerator.getUUID());
		post.setPostId(postDto.getId());
		post.setPostName(postDto.getName());
		post.setActivateDate(new Timestamp(System.currentTimeMillis()));
		post.setStatus(PostStatus.NOT_RUNNING.getValue());
		post.setPx(postIndex);
		post.setAcId(acId);
		return post;
	}

	/**
	 * 生成表单数据列表
	 * 
	 * @param instanceDto
	 * @param pcForms
	 * @param mobileForms
	 * @throws Exception
	 */
	private void generateFormData(Instance instance, InstanceDto instanceDto, List<PcForm> pcForms, List<MobileForm> mobileForms)
			throws Exception {
		List<PcFormDto> pcFormDtoList = instanceDto.getPcFormDtoList();
		for (PcFormDto pcFormDto : pcFormDtoList) {
			PcForm pcForm = new PcForm();
			//BeanUtils.copyProperties(pcForm, pcFormDto);//old-20170718
			BeanUtils.copyProperties(pcFormDto, pcForm);
			pcForm.setId(IDGenerator.getUUID());
			pcForm.setInstanceId(instance.getId());
			pcForm.setFlId(instance.getFlId());
			pcForm.setDelflag(false);
			pcForm.setBusinessObjectId(instance.getBusinessObjectId());
			pcForm.setBusinessId(instance.getBusinessId());
			pcForms.add(pcForm);
		}
		List<MobileFormDto> mobileFormDtoList = instanceDto.getMobileFormDtoList(); 
		int sort = 1000;
		for (MobileFormDto mobileFormDto : mobileFormDtoList) {
			MobileForm mobileForm = new MobileForm();
			//BeanUtils.copyProperties(mobileForm, mobileFormDto);//old-20170718
			BeanUtils.copyProperties(mobileFormDto, mobileForm);
			mobileForm.setId(IDGenerator.getUUID());
			mobileForm.setInstanceId(instance.getId());
			mobileForm.setFlId(instance.getFlId());
			mobileForm.setBusinessObjectId(instance.getBusinessObjectId());
			mobileForm.setBusinessId(instance.getBusinessId());
			mobileForm.setSort(sort++);
			mobileForm.setDelflag(false);
			//针对考勤移动端处理单引号问题

			mobileForms.add(mobileForm);
		}
	}
	
	public static void main(String[] args) {
		String test = "<a title=\"下载\" href=\"javascript:void(0);\" onclick=\"downLoad(this,3224,' qu ')\">数据库表字段修改依据.xlsx</a>";
		System.out.println(test.replace("'", "\\'"));
	}
	
	/**
	 * 查询模板数据,赋值给Instance并保存Instance数据到数据库中, 同时保存实例的相关业务变量 
	 * @param instanceDto
	 * @return 实例ID
	 * @throws Exception
	 */
	private Instance generateInstanceData(InstanceDto instanceDto) throws Exception {
		String dateText = DateUtils.getDateSSSText();
		String flId = instanceDto.getFlId();
		Instance instance = new Instance();
		//BeanUtils.copyProperties(instance, instanceDto);//将instanceDto的业务属性copy到newInstance上old-20170718
		BeanUtils.copyProperties(instanceDto, instance);
	    Fl fl = flService.getObjectById(flId);
	    System.out.println("\n fl="+JacksonUtils.toJson(fl));
	    /*app_id、business_object_id、fl_id、retract、approval_repeat、
	     post_isnull、approval_person_is_nul、post_multi_person 来自FL模板FL的字段；*/
	    //BeanUtils.copyProperties(instance, fl);//将fl的属性copy到newInstance上//old20170718
	    BeanUtils.copyProperties(fl, instance);//new 20170718
	    instance.setId(instanceDto.getId());//对id和flId再进行单独的赋值
		instance.setFlId(flId);
	    
	    //实例Instance的code的值是从模板FL的code复制过来，再加上时间戳；
	    //name的值是从模板FL的flow_title[默认标题规则] 的值解析出来，进行赋值
	    instance.setCode(fl.getCode()+"-"+dateText);
	    instance.setName(instanceDto.getName());//
	    instance.setStatus(null);
	    instance.setStartDate(new Timestamp(System.currentTimeMillis()));
	    
	    return instance;
	}

	private List<InstanceAccessible> generateAccessibleData(InstanceDto instanceDto, Instance instance) throws Exception {
		List<PostDto> postList = JacksonUtils.fromJson(instanceDto.getAccessiblePosts(), ArrayList.class, PostDto.class);
		List<InstanceAccessible> accessibleList = new ArrayList<InstanceAccessible>();
		
		if (postList != null) {
			for (PostDto postDto : postList) {
				List<UserDto> users = postDto.getUsers();
				if(CollectionUtils.isEmpty(users)) {
					continue;
				}
				for (UserDto userDto : users) {
					if(StringUtils.isEmpty(userDto.getId())) {
						continue;
					}
					String[] userIdArr = userDto.getId().split(",");
					String[] userNameArr = userDto.getName().split(",");
					if (userIdArr != null && userIdArr.length > 0 && userNameArr != null && userNameArr.length > 0) {
						for (int i = 0; i < userIdArr.length; i++) {
							InstanceAccessible instanceAccessible = new InstanceAccessible();
							instanceAccessible.setId(IDGenerator.getUUID());
							instanceAccessible.setFiId(instance.getId());
							instanceAccessible.setAccessibleId(userIdArr[i]);
							instanceAccessible.setAccessibleName(userNameArr[i]);

							accessibleList.add(instanceAccessible);
						}
					}
				}
			}			
		}

	    return accessibleList;
	}
	
	private List<InstanceCs> generateCsData(Instance instance, InstanceAc instanceAc, AcDto acDto) throws Exception {
		List<PostDto> postList = JacksonUtils.fromJson(acDto.getCsPosts(), ArrayList.class, PostDto.class);
		List<InstanceCs> instanceCsList = new ArrayList<InstanceCs>();

		if (postList != null) {
			for (PostDto postDto : postList) {
				List<UserDto> users = postDto.getUsers();
				for (UserDto userDto : users) {
					if(userDto.getId() == null) {
						continue;
					}
					String[] userIdArr = userDto.getId().split(",");
					String[] userNameArr = userDto.getName().split(",");
					if (userIdArr != null && userIdArr.length > 0 && userNameArr != null && userNameArr.length > 0) {
						for (int i = 0; i < userIdArr.length; i++) {
							InstanceCs instanceCs = new InstanceCs();
							instanceCs.setId(IDGenerator.getUUID());
							instanceCs.setPostName(postDto.getName());
							instanceCs.setPostId(postDto.getId());
							instanceCs.setParticipantName(userNameArr[i]);
							instanceCs.setParticipantId(userIdArr[i]);
							instanceCs.setFiId(instance.getId());
							instanceCs.setAcId(instanceAc.getId());

							instanceCsList.add(instanceCs);
						}
					}
				}
			}	
		}
		
		return instanceCsList;
	}
	
	private List<InstanceVariable> generateInstanceVariable(InstanceDto instanceDto, Instance instance) throws Exception {
	    //暂时注释掉变量的细节
	    List<InstanceVariableDto> variableDtoList = instanceDto.getVariableDtoList();
	    List<InstanceVariable> varList = new ArrayList<InstanceVariable>();
	    for(InstanceVariableDto variableDto : variableDtoList){
	    	InstanceVariable variable = new InstanceVariable();
	    	variable.setId(IDGenerator.getUUID());
	    	variable.setName(variableDto.getName());
	    	variable.setFiId(instance.getId());
	    	variable.setVal(variableDto.getVal());
	    	varList.add(variable);
			
	    	if("flow_business_company_id".equals(variableDto.getName())){
	    		instance.setFlowBusinessCompanyId(variableDto.getVal());
	    	}
	    	if("flow_business_dept_id".equals(variableDto.getName())){
	    		instance.setFlowBusinessDeptId(variableDto.getVal());
	    	}
	    	if("flow_business_project_id".equals(variableDto.getName())){
	    		instance.setFlowBusinessProjectId(variableDto.getVal());
	    	}
	    	if("flow_business_project_branch_id".equals(variableDto.getName())){
	    		instance.setFlowBusinessProjectBranchId(variableDto.getVal());
	    	}
	    	if("start_user_id".equals(variableDto.getName())){
	    		instance.setStartUserId(variableDto.getVal());
	    	}
	    	if("flow_business_company_name".equals(variableDto.getName())){
	    		instance.setFlowBusinessCompanyName(variableDto.getVal());
	    	}
	    	if("flow_business_dept_name".equals(variableDto.getName())){
	    		instance.setFlowBusinessDeptName(variableDto.getVal());
	    	}
	    	if("flow_business_project_name".equals(variableDto.getName())){
	    		instance.setFlowBusinessProjectName(variableDto.getVal());
	    	}
	    	if("flow_business_project_branch_name".equals(variableDto.getName())){
	    		instance.setFlowBusinessProjectBranchName(variableDto.getVal());
	    	}
	    	if("start_user_name".equals(variableDto.getName())){
	    		instance.setStartUserName(variableDto.getVal());
	    	}
	    }
	    return varList;
	}

	@Override
	public boolean processChange(String userInfo, String fiId, String changeType, List<ACUnit> acUnitList) {		
		
		if(FlowChangeType.PROXY.getValue().equals(changeType)) {
			proxyHandle(userInfo, fiId, acUnitList);
			
		} else if(FlowChangeType.ADDLABEL.getValue().equals(changeType)) {
			addLabelHandle(userInfo, fiId, acUnitList);
			
		} else if(FlowChangeType.REPALCEAPPROVER.getValue().equals(changeType)) {
			replaceApproverHandle(userInfo, fiId, acUnitList);
			
		} else if(FlowChangeType.WITHDRAW.getValue().equals(changeType)) {
			withdrawHandle(userInfo, fiId, acUnitList);
		}
		return true;
	}
	
	/**
	 * 代理处理
	 * 
	 * @param userInfo
	 * @param fiId
	 * @param acUnitList
	 * @return
	 */
	private boolean proxyHandle(String userInfo, String fiId, List<ACUnit> acUnitList) {
		return true;
	}
	
	/**
	 * 加签处理
	 * 
	 * @param userInfo
	 * @param fiId
	 * @param acUnitList
	 * @return
	 */
	private boolean addLabelHandle(String userInfo, String fiId, List<ACUnit> acUnitList) {
		return true;
	}
	
	/**
	 * 替换审批人处理
	 * 
	 * @param userInfo
	 * @param fiId
	 * @param acUnitList
	 * @return
	 */
	private boolean replaceApproverHandle(String userInfo, String fiId, List<ACUnit> acUnitList) {
		return true;
	}
	
	/**
	 * 撤回处理
	 * @param userInfo
	 * @param fiId
	 * @param acUnitList
	 * @return
	 */
	private boolean withdrawHandle(String userInfo, String fiId, List<ACUnit> acUnitList) {
		return true;
	}

	public InstanceDao getInstanceDao() {
		return instanceDao;
	}

	public void setInstanceDao(InstanceDao instanceDao) {
		this.instanceDao = instanceDao;
	}

	public FlService getFlService() {
		return flService;
	}

	public void setFlService(FlService flService) {
		this.flService = flService;
	}

	public StepService getStepService() {
		return stepService;
	}

	public void setStepService(StepService stepService) {
		this.stepService = stepService;
	}

	public InstanceAcService getInstanceAcService() {
		return instanceAcService;
	}

	public void setInstanceAcService(InstanceAcService instanceAcService) {
		this.instanceAcService = instanceAcService;
	}

	public InstanceStepService getInstanceStepService() {
		return instanceStepService;
	}

	public void setInstanceStepService(InstanceStepService instanceStepService) {
		this.instanceStepService = instanceStepService;
	}

	public ParticipantService getParticipantService() {
		return participantService;
	}

	public void setParticipantService(ParticipantService participantService) {
		this.participantService = participantService;
	}

	public InstanceGroupService getInstanceGroupService() {
		return instanceGroupService;
	}

	public void setInstanceGroupService(InstanceGroupService instanceGroupService) {
		this.instanceGroupService = instanceGroupService;
	}

	public InstanceVariableService getInstanceVariableService() {
		return instanceVariableService;
	}

	public void setInstanceVariableService(InstanceVariableService instanceVariableService) {
		this.instanceVariableService = instanceVariableService;
	}

	public InstanceTaskService getInstanceTaskService() {
		return instanceTaskService;
	}

	public void setInstanceTaskService(InstanceTaskService instanceTaskService) {
		this.instanceTaskService = instanceTaskService;
	}
	
	public InstanceAccessibleService getInstanceAccessibleService() {
		return instanceAccessibleService;
	}

	public void setInstanceAccessibleService(InstanceAccessibleService instanceAccessibleService) {
		this.instanceAccessibleService = instanceAccessibleService;
	}

	@Override
	public Page queryInstanceByPageParam(Map<String, Object> map) {
		String starterIdText = (String)map.get("starterId");
		if(starterIdText!=null && starterIdText.length()>5 ){
			String starterIds[] = starterIdText.split(",");
			map.remove("starterId");
			map.put("starterId", starterIds);
		}
		
		String orgIdText = (String)map.get("orgId");
		if(orgIdText!=null  && orgIdText.length()>5){
			String orgIds[] = orgIdText.split(",");
			map.remove("orgId");
			map.put("orgId", orgIds);
		}
		Integer start = (Integer) ( map.get("start"));//new Integer( (String) map.get("start"));
		Integer limit = (Integer) ( map.get("limit"));//new Integer( (String) map.get("limit"));
		map.remove("start");
		map.remove("limit");
		map.put("start", start);
		map.put("limit", limit);
		List<InstanceDto>  list = instanceDao.queryInstanceListByParam(map);
		Integer total = instanceDao.queryInstanceCountByParam(map);
		Page page = new Page();
		page.setList(list);
		page.setTotal(total);
		page.setStart(start);
		page.setLimit(limit);
		return page;
	}

	public InstancePostDao getInstancePostDao() {
		return instancePostDao;
	}

	public void setInstancePostDao(InstancePostDao instancePostDao) {
		this.instancePostDao = instancePostDao;
	}

	@Override
	public SysNoticeMsgService getMsgService() {
		return msgService;
	}

	public void setMsgService(SysNoticeMsgService msgService) {
		this.msgService = msgService;
	}

	public String getFlowApproveUrl() {
		return flowApproveUrl;
	}

	public void setFlowApproveUrl(String flowApproveUrl) {
		this.flowApproveUrl = flowApproveUrl;
	}

	public SearchIndexDtoServiceCustomer getSearchIndexDtoServiceCustomer() {
		return searchIndexDtoServiceCustomer;
	}

	public void setSearchIndexDtoServiceCustomer(SearchIndexDtoServiceCustomer searchIndexDtoServiceCustomer) {
		this.searchIndexDtoServiceCustomer = searchIndexDtoServiceCustomer;
	}

	public CustomFormInstanceDtoServiceCustomer getCustomFormInstanceDtoServiceCustomer() {
		return customFormInstanceDtoServiceCustomer;
	}

	public void setCustomFormInstanceDtoServiceCustomer(
			CustomFormInstanceDtoServiceCustomer customFormInstanceDtoServiceCustomer) {
		this.customFormInstanceDtoServiceCustomer = customFormInstanceDtoServiceCustomer;
	}

	public AgentService getAgentService() {
		return agentService;
	}

	public void setAgentService(AgentService agentService) {
		this.agentService = agentService;
	}
	
	@Override
	public BusinessObjectService getBusinessObjectService() {
		return businessObjectService;
	}

	public void setBusinessObjectService(BusinessObjectService businessObjectService) {
		this.businessObjectService = businessObjectService;
	}

	@Override
	public List<FlowAcPostDto> parsePost(Map<String, Object> businessVariable, AcDto acDto) {

		List<AcDto> acDtos = new ArrayList<AcDto>();
		AcDto startAcDto = new AcDto();
		startAcDto.setId(IDGenerator.getUUID());
		startAcDto.setAcType("1");
		startAcDto.setParticipant(null);
		startAcDto.setCcPerson(null);
		startAcDto.setPosts(null);
		startAcDto.setCsPosts(null);
		startAcDto.setFlowPostParticipantDtos(null);
		String starter = (String) businessVariable.get("start_user_id");
		startAcDto.setParticipant(starter);
		acDtos.add(startAcDto);
		acDtos.add(acDto);

		return parsePost(businessVariable, acDtos);
	}

	@Override
	public List<FlowAcPostDto> parsePost(Map<String, Object> businessVariable, List<AcDto> acDtos) {

		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userInfo = JacksonUtils.toJson(securityUserBeanInfo);

		Map<String, Object> postBodyMap = new HashMap<String, Object>();
		postBodyMap.put("flow_business_variable_data", JacksonUtils.toJson(businessVariable));
		postBodyMap.put("flow_ac_data", JacksonUtils.toJson(acDtos));

		// 请求获取岗位人员信息接口
		log.info ("调用请求获取岗位人员信息接口："+JacksonUtils.toJson(postBodyMap));
		String flowPostDataStr = postDtoServiceCustomer.getFlowPostData(userInfo, JacksonUtils.toJson(postBodyMap));
		Map<String, Object> flowPostDataMap = JacksonUtils.fromJson(flowPostDataStr, Map.class);
		// 获取岗位人员信息
		List<FlowAcPostDto> flowAcPostDtoList = JacksonUtils.fromJson((String) flowPostDataMap.get("result"),
				ArrayList.class, FlowAcPostDto.class);

		return flowAcPostDtoList;
	}

	@Override
	public InstanceOperateLogService getInstanceLogService() {
		return instanceLogService;
	}

	public void setInstanceLogService(InstanceOperateLogService instanceLogService) {
		this.instanceLogService = instanceLogService;
	}

	public OrgnazationDtoServiceCustomer getOrgnazationDtoServiceCustomer() {
		return orgnazationDtoServiceCustomer;
	}

	public void setOrgnazationDtoServiceCustomer(OrgnazationDtoServiceCustomer orgnazationDtoServiceCustomer) {
		this.orgnazationDtoServiceCustomer = orgnazationDtoServiceCustomer;
	}

	public MonitorSettingService getMonitorSettingService() {
		return monitorSettingService;
	}

	public void setMonitorSettingService(MonitorSettingService monitorSettingService) {
		this.monitorSettingService = monitorSettingService;
	}

	public UserDtoServiceCustomer getUserDtoServiceCustomer() {
		return userDtoServiceCustomer;
	}

	public void setUserDtoServiceCustomer(UserDtoServiceCustomer userDtoServiceCustomer) {
		this.userDtoServiceCustomer = userDtoServiceCustomer;
	}
	
	public List<UserDto> queryAdminList() {
		UserDtoServiceCustomer userService = this.getUserDtoServiceCustomer();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String queryAdminList = userService.queryAdminList(JacksonUtils.toJson(securityUserBeanInfo));
		log.info("查询系统管理员：result = " + queryAdminList);
		DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(queryAdminList,
				DubboServiceResultInfo.class);
		List<UserDto> adminList = new ArrayList<UserDto>();
		if(dubboServiceResultInfo.isSucess()) {
			List<Map<String, Object>> list = JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), ArrayList.class, Map.class);
			for(Map<String, Object> user : list) {
				UserDto u = new UserDto();
				u.setId((String) user.get("id"));
				u.setName((String) user.get("realName"));
				adminList.add(u);
			}
		}
		return adminList;
	}
	
	private boolean isAdmin(String userId) {
		List<UserDto> adminList = queryAdminList();
		for(UserDto user : adminList) {
			if(userId.equals(user.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public Map<String, String> queryOrgBy(String postId) {
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userJson = JacksonUtils.toJson(userBeanInfo);
		Map<String, String> params = new HashMap<String, String>();
		params.put("postId", postId);
		String orgInfos = orgnazationDtoServiceCustomer.getOrgsByPostId(userJson, JacksonUtils.toJson(params));
		log.info("调用权限接口查询组织信息：result = " + orgInfos);
		DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(orgInfos,
				DubboServiceResultInfo.class);
		Map<String, String> result = new HashMap<String, String>();
		if(dubboServiceResultInfo.isSucess()) {
			List<OrgnazationDto> list = JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), ArrayList.class, OrgnazationDto.class);
			for(OrgnazationDto org : list) {
				if ("zb".equals(org.getType())||"company".equals(org.getType())) {
					result.put("companyId", org.getId());
				}else if("dept".equals(org.getType())){
					result.put("deptId", org.getId());
				}else if("group".equals(org.getType())){
					result.put("projectId", org.getId());
				}else if("branch".equals(org.getType())){
					result.put("branchId", org.getId());
				}
			}
		}
		return result;
	}

	@Override
	public MobileApproveDto queryMobileApproveByParamMap(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public OrgnazationOutServiceCustomer getOrgnazationOutServiceCustomer() {
		return orgnazationOutServiceCustomer;
	}

	public void setOrgnazationOutServiceCustomer(OrgnazationOutServiceCustomer orgnazationOutServiceCustomer) {
		this.orgnazationOutServiceCustomer = orgnazationOutServiceCustomer;
	}

	@Override
	public List<InstanceDto> getRelateInstances(String instanceId) {
		return instanceDao.getRelateInstances(instanceId);
	}

	@Override
	public String scanOverdueAc() throws Exception {
		List<OverdueAc> overdueAcs = instanceDao.queryOverdueAc();
		
		List<SysNoticeMsg> messages = new ArrayList<SysNoticeMsg>();
		for(OverdueAc overdueAc : overdueAcs) {
			if(OverdueAcHandle.NOTICE_APPROVER.getValue().equals(overdueAc.getOverdueHandle())) {
				messages.addAll(newMessages(overdueAc));
				
			} else if(OverdueAcHandle.BACKTO_STARTER.getValue().equals(overdueAc.getOverdueHandle())) {
				Operation operation = new OperationFactory().newOperation(OperationType.RETURN.getCode(),this);
				InstanceUnit instanceUnit = this.getInstanceUnit(overdueAc.getInstanceId());
				ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
				operation.action(instanceUnit, approvalDto);
				
			} else {
				ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
				boolean isApproved = approval(approvalDto);
				
			}
		}
		
		try {
			msgService.batchSaveAndNotifyOthers(messages);
		} catch (Exception e) {
			throw new FlowException("流程逾期扫描定时任务中发送消息失败！", e);
		}

		return null;
	}

	private List<SysNoticeMsg> newMessages(OverdueAc overdueAc) {
		List<SysNoticeMsg> msgList = new ArrayList<SysNoticeMsg>();
		String msgTitle = "流程【" + overdueAc.getInstanceName() + "】中的环节【"
					+ overdueAc.getAcName() + "】逾期" + overdueAc.getDuration() + "小时，请及时处理！";
		String msgUrl = "/flow/runtime/approve/flow.html"
				+ "?instanceId=" + overdueAc.getInstanceId()
				+ "&time=" + new Date().getTime();
		String receiverIds = overdueAc.getCurrentApproverIds();
		String receiverNames = overdueAc.getCurrentApproverNames();
		if(StringUtils.isEmpty(receiverIds)) {
			return null;
		}
		String mobileUrl = "mobile/approve/approve_detail.html";
		MobileParam mobileParamBean = new MobileParam();
		mobileParamBean.setInstanceId(overdueAc.getInstanceId());
		mobileParamBean.setBusinessId(overdueAc.getBusinessId());
		//mobileParamBean.setAppId(overdueAc.);
		//mobileParamBean.setTypeCode("START");
		//mobileParamBean.setTaskId(startApprover.getTask().getTaskId());
		//mobileParamBean.setApproveRole(startApprover.getTask().getTaskType());
		
		String[] idArray = receiverIds.split(",");
		String[] nameArray = receiverNames.split(",");
		for(int i=0; i<idArray.length; i++) {
			UserDto user = new UserDto(idArray[i], nameArray[i]);
			SysNoticeMsg msg = this.msgService.newFlowMsg(user, "DY", msgTitle, msgUrl, mobileUrl, JacksonUtils.toJson(mobileParamBean));
			msgList.add(msg);
		}
		return msgList;
	}
	
	public InstanceTransitionRecordService getInstanceTransitionRecordService() {
		return instanceTransitionRecordService;
	}

	public void setInstanceTransitionRecordService(InstanceTransitionRecordService instanceTransitionRecordService) {
		this.instanceTransitionRecordService = instanceTransitionRecordService;
	}	
	
	public void saveTransition(String instanceId, String userName, String actionName) throws Exception {
		InstanceTransitionRecord transitionRecord = new InstanceTransitionRecord();
		transitionRecord.setId(IDGenerator.getUUID());
		transitionRecord.setFiId(instanceId);
		transitionRecord.setTransationUserName(userName);
		transitionRecord.setTransationDate(new Timestamp(System.currentTimeMillis()));
		transitionRecord.setActionName(actionName);
		instanceTransitionRecordService.save(transitionRecord);
	}

	@Override
	public List<InstanceTransitionRecordDto> queryTransferList(String instanceId) {
		return instanceTransitionRecordService.queryTransferList(instanceId);
	}

	//@Override
	//public List<InstanceDto> personalQueryList(Map<String, String> map) {	
	//	return instanceDao.personalQueryList(map);
	//}
	@Override
	public Page personalQueryListByPageParam(Map<String, Object> map) {
		Integer start = (Integer) map.get("start");
		//Integer limit = new Integer( (String) map.get("limit"));
		Integer limit = (Integer) map.get("limit");
		map.remove("start");
		map.remove("limit");
		map.put("start", start);
		map.put("limit", limit);
		List<InstanceDto>  list = instanceDao.personalQueryListByParam(map);
		Integer total = instanceDao.personalQueryCountByParam(map);
		Page page = new Page();
		page.setList(list);
		page.setTotal(total);
		page.setStart(start);
		page.setLimit(limit);
		return page;
	}

	//@Override
	//public List<InstanceDto> queryListByApprover(Map<String, String> map) {
	//	return instanceDao.queryListByApprover(map);
	//}
	//优化修改为分页查询
	@Override
	public Page queryListByApproverParam(Map<String, Object> map) {
		Integer start = (Integer) map.get("start");
		//Integer limit = new Integer( (String) map.get("limit"));
		Integer limit = (Integer) map.get("limit");
		map.remove("start");
		map.remove("limit");
		map.put("start", start);
		map.put("limit", limit);
		List<InstanceDto>  list = instanceDao.queryListByApproverParam(map);
		Integer total = instanceDao.queryListCountByApproveParam(map);
		Page page = new Page();
		page.setList(list);
		page.setTotal(total);
		page.setStart(start);
		page.setLimit(limit);
		return page;
	}
	/**
	 * 获取节点审批类型
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String queryApprovalTypeId(Map<String, Object> map)throws Exception{
		return instanceDao.queryApprovalTypeId(map);
	}
	
	@Override
	public FlowApproveViewBean flowView(FlowQueryBean queryBean) throws Exception {
		FlowApproveViewBean result = new FlowApproveViewBean();
		
		//1、判断流程权限
		
		//查询审批记录
		String instanceId = queryBean.getInstanceId();
		String flCode = queryBean.getFlCode();
		String bizId = queryBean.getBusinessId();
		String appId = queryBean.getAppId();
		String taskId = queryBean.getTaskId();
		List<ApprovalList> list = null;
		if(StringUtils.isEmpty(instanceId)) {
			list = instanceDao.queryApprovalListExternal(flCode, bizId, appId);
			if(list!=null && list.size()>0){
				instanceId = list.get(0).getInstanceId();
			}else{
				//业务系统上线审批中流程不迁移导致报错，所以这块给出提示去老系统查看
				log.info("业务系统上线审批中流程不迁移导致报错，所以这块给出提示去老系统查看！");
				result.setInstanceName("BusinessType");
				return result;
			}
		} else {
			list = instanceDao.queryApprovalList(instanceId, null);
		}
		
		//2、记录查询痕迹
		instanceReadRecordService.record(instanceId, null);
		
		if(CollectionUtils.isEmpty(list)) {
			Instance instance = instanceDao.getObjectById(instanceId);
			BusinessObject objectDto = businessObjectService.getObjectById(instance.getBusinessObjectId());
			result.setFlId(instance.getFlId());
			result.setBusinessId(instance.getBusinessId());
			result.setPcUrl(objectDto.getPcUrl());
			result.setInstanceId(instanceId);
			result.setInstanceName(instance.getName());
			result.setInstanceStatus(instance.getStatus());
			result.setRelateFlows(this.getRelateInstances(instanceId));
			log.info("审批记录查询为空！");
			return result;
		}
		removeForkAndJoin(list);
		
		String currentUserId = null;
		SecurityUserDto securityUserDto = LoginUtils.getSecurityUserBeanInfo().getSecurityUserDto();
		if(securityUserDto != null) {
			currentUserId = securityUserDto.getId();
		}
		
		Set<String> roles = new HashSet<String>();
//		String role = FlowRole.OTHER.getValue();	//可阅人、抄送人
		if(currentUserId.equals(list.get(0).getApproverId())) {
			roles.add(FlowRole.STARTER.getValue());
		}
		ApprovalList currentApproval=null;
		List<String> currentPersonTaskList=new ArrayList<String>();
		
		//统计一个人多个点亮任务时的任务个数
		Integer currentTaskCount=0;
		for(ApprovalList approval : list){
			//点亮的行
			if(TaskStatus.RUNNING.getValue().equals(approval.getTaskStatus())) {
				//一个人只能有一个点亮行
				if(currentUserId.equals(approval.getApproverId())) {
					currentTaskCount++;
				}
			}
		}

		for(ApprovalList approval : list){
			
			//点亮的行
			if(TaskStatus.RUNNING.getValue().equals(approval.getTaskStatus())) {
				if(currentTaskCount>=2){
					if(taskId!=null && !taskId.equals(approval.getTaskId())){
						continue;
					}
				}
				//当前环节的审批类型
				result.setCurrentApprovalTypeId(approval.getApprovalTypeId());
				String iframeMode = newMode(approval.getApprovalTypeId(), approval.getApproverId(), currentUserId);
				result.setIframeMode(iframeMode);
				
				//一个人只能有一个点亮行
				if(currentUserId.equals(approval.getApproverId())) {
					roles.add(FlowRole.APPROVER_BEFORE.getValue());
					//查询审批人操作选项
					List<ApproveOperationDto> operations = approveOperationService
							.queryListByApproveRoleCode(approval.getApprovalTypeId(), approval.getTaskType());
					result.setOperations(operations);
					
					//查询下一环节
					result.setNextAc(this.queryNext(instanceId, approval.getTaskId()));
					result.setCurrentGroupKey(approval.getGroupKey());
					currentApproval=approval;
					break;
				}
				
			} else	if(TaskStatus.FINISHED.getValue().equals(approval.getTaskStatus())
					&& !FlAcType.START.getAcType().equals(approval.getAcType())) {
				
				if(currentUserId.equals(approval.getApproverId())) {
					if("HF".equals(approval.getTaskResult()) || "JS".equals(approval.getTaskResult())){
						currentPersonTaskList.add(approval.getTaskId());
					}
					if(approval.getAutoPass() == 1 && approval.getTaskComments()!=null && approval.getTaskComments().contains("责任人相同，系统自动通过")){
						continue;
					}else{
						roles.add(FlowRole.APPROVER_AFTER.getValue());
					}
				}
			}
		}
		
		//如果同时是管理员，存在多重角色
		if(isAdmin(currentUserId)) {
			roles.add(FlowRole.ADMIN.getValue());
		}
		
		//解决当前审批人有多个任务但是审批通过的不是第一个的情况，这时也应该显示撤回任务按钮
		if(!roles.contains(FlowRole.APPROVER_AFTER.getValue())){
			for(ApprovalList approval : list){
				if(currentApproval!=null && currentApproval.getApproverId().equals(approval.getApproverId())){
					if(TaskStatus.FINISHED.getValue().equals(approval.getTaskStatus())
							&& !FlAcType.START.getAcType().equals(approval.getAcType())
							&& approval.getAutoPass() != 1 && approval.getTaskComments()!=null && !approval.getTaskComments().contains("责任人相同，系统自动通过")) {
						roles.add(FlowRole.APPROVER_AFTER.getValue());
					}
				}
			}
		}
		
		/**
		 * 沟通发起人时如果当前人有多个任务,但是当前任务不最近一次处理的任务时,不应该显示撤回任务按钮
		 */
		if(currentPersonTaskList!=null && currentPersonTaskList.size()>1){
			for(int i=0;i<currentPersonTaskList.size();i++){
				if(i+1==currentPersonTaskList.size()){
					break;
				}
				String currTaskId=currentPersonTaskList.get(i);
				if(currTaskId.equals(taskId)){
					roles.remove(FlowRole.APPROVER_AFTER.getValue());
				}
			}
		}
		
		//查询功能区按钮
		List<String> btnCodes = queryBtnCodes(roles, list.get(0).getInstanceStatus());
		result.setBtnCodes(btnCodes);
		
		//设置页面需要的流程数据
		ApprovalList starter = list.get(0);
		result.setFlCode(starter.getFlCode());
		result.setFlId(starter.getFlId());
		result.setPcUrl(starter.getPcUrl());
		result.setBusinessId(starter.getBusinessId());
		result.setInstanceId(starter.getInstanceId());
		result.setInstanceName(starter.getInstanceName());
		result.setInstanceStatus(starter.getInstanceStatus());
		result.setCurrentUserId(securityUserDto.getId());
		result.setList(list);
		result.setRelateFlows(this.getRelateInstances(instanceId));
		result.setAppId(starter.getAppId());
		
		return result;
	}

	/**
	 * 目前只用于新闻
	 * @param approvalTypeId
	 * @param approverId
	 * @param currentUserId
	 * @return
	 */
	private String newMode(String approvalTypeId, String approverId, String currentUserId) {
		if("JG".equals(approvalTypeId) && approverId.equals(currentUserId)) {
			return "edit";
		}
		return "view";
	}

	/**
	 * 流程查询页面按钮控制
	 * @param roles
	 * @param instanceStatus
	 * @return
	 */
	private List<String> queryBtnCodes(Set<String> roles, String instanceStatus) {
		List<String> btnCodes = new ArrayList<String>();
		for(String role : roles) {
			//发起人
			if(FlowRole.STARTER.getValue().equals(role)) {
				//流程运行中
				if(InstanceStatus.RUNNING.getValue().equals(instanceStatus)) {
					btnCodes.add("withDrawFlow");			//撤回流程
					btnCodes.add("remind");					//催办
				}
				
				//审批人、转办人、协办人
			} else if(FlowRole.APPROVER_AFTER.getValue().equals(role)) {
				//流程运行中
				if(InstanceStatus.RUNNING.getValue().equals(instanceStatus)) {
					btnCodes.add("withDrawTask");			//撤回任务
				}
				
				//管理员
			} else if(FlowRole.ADMIN.getValue().equals(role)) {
				//流程运行中
				
				if(InstanceStatus.RUNNING.getValue().equals(instanceStatus)) {
					btnCodes.add("cancelInstance");			//作废流程
					btnCodes.add("finishApproval");			//审结
//					btnCodes.add("modifyReader");			//修改可阅人
					btnCodes.add("skipCurrentApprover");	//跳过当前审批人
					btnCodes.add("modifyAc");				//调整环节
//					btnCodes.add("modifyApprover");			//修改审批人
					btnCodes.add("modifyApproverAdvice");	//修改处理意见
					btnCodes.add("pass");					//传阅 进行中的流程只有管理员可以传阅
					
					//流程正常完成
				} else if(InstanceStatus.FINISHED.getValue().equals(instanceStatus)) {
					btnCodes.add("cancelInstance");			//作废流程
//					btnCodes.add("modifyReader");			//修改可阅人
					
					//流程挂起
				} else if(InstanceStatus.HANGUP.getValue().equals(instanceStatus)) {
					btnCodes.add("letItGo");				//放行
					btnCodes.add("cancelInstance");			//作废流程
					btnCodes.add("finishApproval");			//审结
//					btnCodes.add("modifyReader");			//修改可阅人
//					btnCodes.add("skipCurrentApprover");	//跳过当前审批人
					btnCodes.add("modifyAc");				//调整环节
//					btnCodes.add("modifyApprover");			//修改审批人
					btnCodes.add("modifyApproverAdvice");	//修改处理意见

				}
			}
		}
		
		if(InstanceStatus.FINISHED.getValue().equals(instanceStatus)) {
			btnCodes.add("pass");		//传阅
		}
		
		//通用按钮
		btnCodes.add("shut");		//关闭
		btnCodes.add("print");		//打印
		btnCodes.add("collect");	//收藏
		
		return btnCodes;
	}
	
	public void setLoginInfo(String token, String userInfo, String menuInfo) {
		long endTime = Calendar.getInstance().getTime().getTime();
		redisTemplate.opsForValue().set(SecurityUserBeanInfo.TOKEN_TEND_USER + token, userInfo, endTime, TimeUnit.MILLISECONDS);
		redisTemplate.opsForValue().set(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token, menuInfo, endTime, TimeUnit.MILLISECONDS);
		log.info("往redis中设置当前用户信息：token=" + token + ", userInfo" + userInfo + ", menuInfo" + menuInfo);

	}

	@Override
	public Map<String, List<UserDto>> queryCurrentApproversOf(List<FlowQueryBean> flowInfos) {
		Map<String, List<UserDto>>  usersMap = new HashMap<String, List<UserDto>>();
		List<Instance> instanceList = null;
		String instanceId = flowInfos.get(0).getInstanceId();
		List<String> instanceIds = new ArrayList<String>();
		List<String> businessIds = new ArrayList<String>();
		for(FlowQueryBean flowInfo : flowInfos) {
			instanceIds.add(flowInfo.getInstanceId());
			businessIds.add(flowInfo.getBusinessId());
		}
		if(!StringUtils.isEmpty(instanceId)) {
			instanceList = instanceDao.queryListByInstanceIds(instanceIds);
			
		} else {
			instanceList = instanceDao.queryListByFlCodeAndBusinessIds(flowInfos.get(0).getFlCode(),
					businessIds);
		}

		for(Instance instance : instanceList) {
			List<UserDto> userList = new ArrayList<UserDto>();
			String currentApproverIds = instance.getCurrentApproverIds();
			String currentApprovers = instance.getCurrentApprovers();
			if(!StringUtils.isEmpty(currentApproverIds)) {
				String[] userIds = currentApproverIds.split(",");
				String[] userNames = currentApprovers.split(",");
				if(userIds != null && userIds.length > 0) {
					for(int i=0; i<userIds.length; i++) {
						UserDto user = new UserDto();
						user.setId(userIds[i]);
						user.setName(userNames[i]);
						userList.add(user);
					}
				}
			}
			usersMap.put(instance.getBusinessId(), userList);
		}
		
		return usersMap;
	}

	@Override
	public List<InstanceAcDto> queryCurrentNodeOf(FlowQueryBean flowInfo) {
		String instanceId = flowInfo.getInstanceId();
		return instanceDao.queryCurrentNodeOf(instanceId);
	}
	
	@Override
	public SysNoticeMsgTempDao getSysNoticeMsgTempDao(){
		return  sysNoticeMsgTempDao;
	}

	public InstanceCsService getInstanceCsService() {
		return instanceCsService;
	}

	public void setInstanceCsService(InstanceCsService instanceCsService) {
		this.instanceCsService = instanceCsService;
	};

	@Override
	public Boolean validateInstanceDataAuth(Map<String,Object> paramMap,String userInfo) {
		Boolean instanceAccess = false;
		String sourceInstanceId = (String) paramMap.get("sourceInstanceId");
		String instanceId = sourceInstanceId!=null?sourceInstanceId:(String) paramMap.get("instanceId");


		//获取当前登录人信息
		SecurityUserBeanInfo securityUserBeanInfo = JacksonUtils.fromJson(userInfo,SecurityUserBeanInfo.class);
		SecurityUserDto securityUserDto = securityUserBeanInfo.getSecurityUserDto();

		if("2".equals(securityUserDto.getType())){
			return true;
		}

		//判断附件来源
		String attachmentUrlId = (String) paramMap.get("attachmentUrl_id");
		if(org.apache.commons.lang.StringUtils.isNotBlank(attachmentUrlId)){
			Map<String,Object> attachParam = new HashMap<String,Object>();
			attachParam.put("id",attachmentUrlId);
			String attachResultInfoJson = attachmentDtoServiceCustomer.getObjectById(userInfo,JacksonUtils.toJson(attachParam));
			DubboServiceResultInfo attachResultInfo = JacksonUtils.fromJson(attachResultInfoJson,DubboServiceResultInfo.class);
			String result = attachResultInfo.getResult();
			if(org.apache.commons.lang.StringUtils.isNotBlank(result)){
				AttachmentDto attachmentDto = JacksonUtils.fromJson(result, AttachmentDto.class);
				if(attachmentDto!=null){
					String url = attachmentDto.getUrl();
					String pattern = "(instanceId="+(String) paramMap.get("instanceId")+")";

					// 创建 Pattern 对象
					Pattern r = Pattern.compile(pattern);

					// 现在创建 matcher 对象
					Matcher m = r.matcher(url);
					if(m.find()){
						return true;
					}
				}
			}
		}

		String currentUserId = securityUserDto.getId();

		//获取流程实例
		Instance instance = instanceDao.getObjectById(instanceId);
		String deptId = instance.getFlowBusinessDeptId();
		String businessId = instance.getBusinessId();

		//获取自定义表单
		CustomFormInstance customFormInstance = customFormInstanceDao.getObjectById(businessId);
		//判断自定义表单是否为空
		if(customFormInstance!=null){
			String customFormId = customFormInstance.getCustomFormId();
			CustomForm customForm=customFormDao.getObjectById(customFormId);
			Integer customFormDataItemControl = customForm.getDataItemControl();
			if(customFormDataItemControl == 2 || customFormDataItemControl == 3){
				//获取流程数据权限，并且判断当前流程实例是否数据权限范围之内
				Map<String,String> dataAuthMap = new HashMap<> ();
				dataAuthMap.put ("appCode","OA");
				dataAuthMap.put("userIds",currentUserId);
				dataAuthMap.put ("itemCode", customForm.getDataItemId());
				String authResultJson = orgnazationOutServiceCustomer.getUserDataAuthCoAndDeptListByItemCode(userInfo,JacksonUtils.toJson (dataAuthMap));
				DubboServiceResultInfo authResultInfo = JacksonUtils.fromJson (authResultJson,DubboServiceResultInfo.class);
				if(authResultInfo.isSucess ()){
					Map<String,UserAuthDataOrgList> authDataMap = JacksonUtils.fromJson (authResultInfo.getResult (),Map.class);
					String userAuthDataOrgListJson = JacksonUtils.toJson(authDataMap.get (currentUserId));
					UserAuthDataOrgList userAuthDataOrgList = JacksonUtils.fromJson(userAuthDataOrgListJson,UserAuthDataOrgList.class);
					List<OrgnazationDto> orgnazationDtos = userAuthDataOrgList.getDeptList();
					for (OrgnazationDto orgnazationDto:orgnazationDtos) {
						String orgId = orgnazationDto.getId();
						if(orgId.equals(deptId)){
							instanceAccess = true;
							break;
						}
					}
				}
			}
		}

		//获取流程相关人员id列表
		List<String> userIdList = this.instanceDao.queryFlowRelationUserIds(instanceId);
		if(!instanceAccess&&userIdList.contains(currentUserId)){
			instanceAccess = true;
		}
		return instanceAccess;
	}

	@Override
	public InstanceDto getInstanceGraph(String instanceId) throws Exception {
		if(org.apache.commons.lang.StringUtils.isBlank(instanceId)){
			return  null;
		}

		//获取实例
		Instance instance = this.instanceDao.getObjectById(instanceId);
		InstanceDto instanceDto = new InstanceDto();
		BeanUtils.copyProperties(instance,instanceDto);

		List<String> currentAndPreviousNodeList = instanceDto.getCurrentAndPreviousNodeList();
		//获取实例节点信息
		Map<String,Object> acParamMap = new HashMap<String,Object>();
		acParamMap.put("fiId",instanceId);
		//acParamMap.put("delflag",false);
		acParamMap.put("sidx","px");
		acParamMap.put("sord","ASC");
		List<InstanceAc> instanceAcList = this.instanceAcDao.queryList(acParamMap);
		Iterator<InstanceAc> iter = instanceAcList.iterator();
		//过滤掉老流程的打回环节
		/*while (iter.hasNext()){
			InstanceAc instanceAc = iter.next();
			String sourceId = instanceAc.getSourceId();
			if(sourceId==null){
				iter.remove();
			}
		}*/

		List<InstanceAcDto> instanceAcDtoList = new ArrayList<InstanceAcDto>();
		int currentAcIndex = 0;
		Map<String , InstanceAcDto> acNewOldMap = new HashMap<String,InstanceAcDto>();
		for (int i = 0; i < instanceAcList.size(); i++) {
			InstanceAc instanceAc = instanceAcList.get(i);
			String acId = instanceAc.getId();
			String acStatus = instanceAc.getStatus();
			currentAndPreviousNodeList.add(acId);
			if("2".equals(acStatus)||"4".equals(acStatus)){
				currentAcIndex = i;
			}
			InstanceAcDto instanceAcDto = new InstanceAcDto();
			BeanUtils.copyProperties(instanceAc, instanceAcDto);
			instanceAcDtoList.add(instanceAcDto);

			acNewOldMap.put(acId,instanceAcDto);
		}

		currentAndPreviousNodeList = currentAcIndex==0?currentAndPreviousNodeList:currentAndPreviousNodeList.subList(0,currentAcIndex+1);

		//流程实例可阅人列表
		Map<String,Object> accessibleParamMap = new HashMap<String,Object>();
		accessibleParamMap.put("delflag",false);
		accessibleParamMap.put("fiId",instanceId);
		List<InstanceAccessible> instanceAccessibleList = instanceAccessibleService.queryList(accessibleParamMap);
		instanceDto.setAccessibles(JacksonUtils.toJson(instanceAccessibleList));

		//环节审批人列表
		Map<String,Object> instanceGroupParamMap = new HashMap<String,Object>();
		instanceGroupParamMap.put("instanceId",instanceDto.getId());
		List<Map<String,Object>> instanceGroupList = instanceGroupService.queryListByInstanceId(instanceGroupParamMap);
		Iterator<Map<String,Object>> groupIterator = instanceGroupList.iterator();
		while (groupIterator.hasNext()){
			Map<String,Object> instanceGroup = groupIterator.next();
			String postName = (String) instanceGroup.get("postName");
			/*if(postName!=null&&!postName.endsWith("/")){
				postName += "/";
			}else if(postName==null){
				postName = "";
			}*/
			String participantName = (String) instanceGroup.get("participantName");
//			participantName = postName+(participantName==null?"":participantName);
			participantName = (participantName==null?"":participantName);
			instanceGroup.put("participantName",participantName);
			String acId = (String) instanceGroup.get("acId");
			InstanceAcDto acDto = acNewOldMap.get(acId);
			if(acDto==null){
				continue;
			}
			String participantJson = acDto.getParticipant();
			List<Map<String,Object>> groupList = JacksonUtils.fromJson(participantJson,List.class,Map.class);
			groupList = groupList==null?new ArrayList<Map<String,Object>>():groupList;
			groupList.add(instanceGroup);
			acDto.setParticipant(JacksonUtils.toJson(groupList));
		}

		//环节抄送人列表
		Map<String,Object> instanceCsParamMap = new HashMap<String,Object>();
		instanceCsParamMap.put("fiId",instanceDto.getId());
		instanceCsParamMap.put("delflag",false);
		List<InstanceCs> instanceCsList = instanceCsService.queryList(instanceCsParamMap);
		Iterator<InstanceCs> csIterator = instanceCsList.iterator();
		while (csIterator.hasNext()){
			InstanceCs instanceCs = csIterator.next();
			String acId = instanceCs.getAcId();
			String postName = instanceCs.getPostName();
			/*if(postName!=null&&!postName.endsWith("/")){
				postName += "/";
			}else if(postName==null){
				postName = "";
			}*/
			String participantName = instanceCs.getParticipantName();
//			participantName = postName+(participantName==null?"":participantName);
			participantName = (participantName==null?"":participantName);
			instanceCs.setParticipantName(participantName);
			InstanceAcDto acDto = acNewOldMap.get(acId);
			if(acDto==null){
				continue;
			}
			String participantJson = acDto.getCcPerson();
			List<InstanceCs> csList = JacksonUtils.fromJson(participantJson,List.class,InstanceCs.class);
			csList = csList==null?new ArrayList<InstanceCs>():csList;
			csList.add(instanceCs);
			acDto.setCcPerson(JacksonUtils.toJson(csList));
		}


		//获取实例连接线信息
		Map<String,Object> stepParamMap = new HashMap<String,Object>();
		stepParamMap.put("fiId",instanceId);
		stepParamMap.put("delflag",false);
		List<InstanceStep> instanceStepList = this.instanceStepService.queryList(stepParamMap);
		//暂时清空一存储的连线信息，等隐藏环节逻辑修改后再注释掉
		instanceStepList = Collections.emptyList();
		//如果数据库中未保存环节连线信息，则根据节点生成
		if(CollectionUtils.isEmpty(instanceStepList)){
			instanceStepList = this.generateInstanceStep(instanceAcList);
		}

		for (InstanceStep instanceStep:instanceStepList){
			String stepId = instanceStep.getId();
			String targetId = instanceStep.getTargetId();
			if(currentAndPreviousNodeList.contains(targetId)){
				currentAndPreviousNodeList.add(stepId);
			}
		}
		instanceDto.setCurrentAndPreviousNodeList(currentAndPreviousNodeList);

		//创建画布XML
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("mxGraphModel").addElement("root");
		root.addElement("mxCell").addAttribute("id", "0");
		root.addElement("mxCell").addAttribute("id", "1").addAttribute("parent", "0");
		parseNodeToXMLElement(root, instanceAcDtoList, "ac",currentAndPreviousNodeList,currentAcIndex==0?null:currentAndPreviousNodeList.get(currentAcIndex));
		parseNodeToXMLElement(root, instanceStepList, "step",currentAndPreviousNodeList,currentAcIndex==0?null:currentAndPreviousNodeList.get(currentAcIndex));

		instanceDto.setGraphXml(document.asXML());
		return instanceDto;
	}

	/**
	 * 将流程图元数据解析为可展示的XML
	 * @param root
	 * @param elements
	 * @param type
	 * @throws Exception
	 */
	private void parseNodeToXMLElement(Element root, Object elements, String type,List<String> currentAndPreviousNodeList,String currentNodeId) throws Exception {
		if (root != null && elements != null && org.apache.commons.lang.StringUtils.isNotBlank(type)) {
			if ("ac".equals(type)) {
				List<InstanceAcDto> acs = (List<InstanceAcDto>) elements;
				for (InstanceAcDto ac : acs) {
					Element cellElem = root.addElement("mxCell")
							.addAttribute("vertex", "1")
							.addAttribute("parent", "1")
							.addAttribute("id", ac.getId())
							.addAttribute("value",ac.getName())
							.addAttribute("label", ac.getName())
							.addAttribute("description", ac.getName());

					String acType = ac.getAcType();
					//String approvalPersonIsNull = ac.getApprovalPersonIsNull();
					String approveStrategy = ac.getApproveStrategy();
					String approveTypeId = ac.getApproveTypeId();
					String code = ac.getCode();
					Boolean isAddLabel = ac.getIsAddLabel();
					Integer overdueTime = ac.getOverdueTime();
					String overdueHandle = ac.getOverdueHandle();
					//Boolean isStart = ac.getIsStart();
					String name = ac.getName();
					String sourceId = ac.getSourceId();
					//String nodeId = ac.getNodeId();
					//String overdueHandle = ac.getOverdueHandle();
					//Integer overdueTime = ac.getOverdueTime();
					//String personRepeatIsSkipped = ac.getPersonRepeatIsSkipped();
					//String postIsNull = ac.getPostIsNull();
					String postMultiPerson = ac.getPostMultiPerson();
					Map<String,Object> extraMap = new HashMap<String,Object>();
					extraMap.put("acType",acType);
					//extraMap.put("approvalPersonIsNull",approvalPersonIsNull);
					extraMap.put("approveStrategy",approveStrategy);
					extraMap.put("approveTypeId",approveTypeId);
					extraMap.put("code",code);
					extraMap.put("overdueTime",overdueTime);
					extraMap.put("overdueHandle",overdueHandle);
					extraMap.put("isAddLabel",isAddLabel);
					//extraMap.put("isStart",isStart);
					extraMap.put("name",name);
					//extraMap.put("nodeId",nodeId);
					//extraMap.put("overdueHandle",overdueHandle);
					//extraMap.put("overdueTime",overdueTime);
					//extraMap.put("personRepeatIsSkipped",personRepeatIsSkipped);
					//extraMap.put("postIsNull",postIsNull);
					extraMap.put("postMultiPerson",postMultiPerson);
					extraMap.put("sourceId",sourceId);
					String extra = JacksonUtils.toJson(extraMap);
					cellElem.addAttribute("extra",extra);
					cellElem.addAttribute("acStatus",ac.getStatus().toString());


					//环节审批人
					String participant = ac.getParticipant();
					if(org.apache.commons.lang.StringUtils.isNotBlank(participant)){
						cellElem.addAttribute("participant",participant);
						cellElem.addAttribute("isValidateNode","true");
					}
					//环节抄送人
					String ccPerson = ac.getCcPerson();
					if(org.apache.commons.lang.StringUtils.isNotBlank(ccPerson)){
						cellElem.addAttribute("ccPerson",ccPerson);

					}

					String styleColor = "";
					if(ACStatus.FINISHED.getValue().equals(ac.getStatus())){
						styleColor = ";strokeColor=#458B74;fillColor=#458B74;";
						cellElem.addAttribute("isExecuteNode","true");
					}

					if(org.apache.commons.lang.StringUtils.isNotBlank(currentNodeId)&&currentNodeId.equals(ac.getId())||"2".equals(ac.getStatus())){
						if("4".equals(ac.getStatus())){
							styleColor = ";strokeColor=#AFEEEE;fillColor=#FF0000;";
						}else{
							styleColor = ";strokeColor=#AFEEEE;fillColor=#AFEEEE;";
						}
						cellElem.addAttribute("isExecuteNode","false");

					}


					if (FlAcType.START.getAcType().equals(ac.getAcType())){
						cellElem.addAttribute("style", FlAcType.START.getNodeType()+styleColor)
								.addAttribute("nodeType", FlAcType.START.getNodeType());
					}else if (FlAcType.TASK.getAcType().equals(ac.getAcType())) {
						cellElem.addAttribute("style", FlAcType.TASK.getNodeType()+styleColor)
								.addAttribute("nodeType", FlAcType.TASK.getNodeType());
					} else if (FlAcType.FORK.getAcType().equals(ac.getAcType())) {
						cellElem.addAttribute("style", FlAcType.FORK.getNodeType()+styleColor)
								.addAttribute("nodeType", FlAcType.FORK.getNodeType());
					} else if (FlAcType.JOIN.getAcType().equals(ac.getAcType())) {
						cellElem.addAttribute("style", FlAcType.JOIN.getNodeType()+styleColor)
								.addAttribute("nodeType", FlAcType.JOIN.getNodeType());
					} else if (FlAcType.END.getAcType().equals(ac.getAcType())){
						cellElem.addAttribute("style", FlAcType.END.getNodeType()+styleColor)
								.addAttribute("nodeType", FlAcType.END.getNodeType());
					} else if (FlAcType.CC.getAcType().equals(ac.getAcType())){
						cellElem.addAttribute("style", FlAcType.CC.getNodeType()+styleColor)
								.addAttribute("nodeType", FlAcType.CC.getNodeType());
					}
					cellElem.addElement("mxGeometry")
							.addAttribute("x", String.valueOf(ac.getX()))
							.addAttribute("y", String.valueOf(ac.getY()))
							.addAttribute("width", String.valueOf(ac.getWidth()))
							.addAttribute("height", String.valueOf(ac.getHeight()))
							.addAttribute("as", "geometry");

				}

			} else if ("step".equals(type)) {
				List<InstanceStep> steps = (List<InstanceStep>) elements;
				for (InstanceStep step : steps) {
					Element cellElem = root.addElement("mxCell");
					String label = "";//step.getName();
					if(label.indexOf("->")!=-1){
						label = label.substring(0,label.indexOf("->"));
					}

					String styleColor = "";
					if(currentAndPreviousNodeList.contains(step.getId())){
						styleColor = ";strokeColor=#458B74;";
						cellElem.addAttribute("isExecuteNode","true");
					}


					cellElem.addAttribute("style", "defaultEdge" + styleColor)
							.addAttribute("edge", "1")
							.addAttribute("parent", "1")
							.addAttribute("source", step.getSourceId())
							.addAttribute("target", step.getTargetId())
							.addAttribute("id", step.getId())
							.addAttribute("value", StringEscapeUtils.unescapeHtml (label));
					/*String conditionExpression = step.getConditionExpression();
					String conditionTranslation = step.getConditionTranslation();
					if (org.apache.commons.lang.StringUtils.isNotBlank(conditionExpression)){
						cellElem.addAttribute("conditionExpression", conditionExpression);
					}

					if (org.apache.commons.lang.StringUtils.isNotBlank(conditionTranslation)){
						cellElem.addAttribute("conditionTranslation", conditionTranslation);
					}*/
					cellElem.addElement("mxGeometry")
							.addAttribute("relative", "1")
							.addAttribute("as", "geometry");
				}
			}
		}
	}

	/**
	 * 创建实例流程图xml格式数据
	 * @param root
	 * @param elements
	 * @param type
	 * @throws Exception
	 */
	@Deprecated
	private void createXMLElement(Element root, Object elements, String type) throws Exception {
		if (root != null && elements != null && org.apache.commons.lang.StringUtils.isNotBlank(type)) {
			if ("ac".equals(type)) {
				List<InstanceAc> acs = (List<InstanceAc>) elements;
				for (InstanceAc ac : acs) {
					String label = ac.getName();
					if(label.indexOf("->")!=-1){
						label = label.substring(0,label.indexOf("->"));
					}
					Element parentElem = null;
					if (FlAcType.START.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.START.getNodeName())
								.addAttribute("id", ac.getId())
								.addAttribute("label", label)
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.START.getNodeType());
					} else if (FlAcType.TASK.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.TASK.getNodeName())
								.addAttribute("id", ac.getId())
								.addAttribute("label", label)
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.TASK.getNodeType());
					} else if (FlAcType.FORK.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.FORK.getNodeName())
								.addAttribute("id", ac.getId())
								.addAttribute("label", label)
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.FORK.getNodeType());
					} else if (FlAcType.JOIN.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.JOIN.getNodeName())
								.addAttribute("id", ac.getId())
								.addAttribute("label", label)
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.JOIN.getNodeType());
					} else if (FlAcType.END.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.END.getNodeName())
								.addAttribute("id", ac.getId())
								.addAttribute("label", label)
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.END.getNodeType());
					}else{
						//如果没有类型默认保存为task
						parentElem = root.addElement(FlAcType.TASK.getNodeName())
								.addAttribute("id", ac.getId())
								.addAttribute("label", label)
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.TASK.getNodeType());
					}

					Element cellElem = parentElem.addElement("mxCell")
							.addAttribute("vertex", "1")
							.addAttribute("parent", "1");
					if (FlAcType.START.getAcType().equals(ac.getAcType())){
						cellElem.addAttribute("style", FlAcType.START.getNodeType());
					}else if (FlAcType.TASK.getAcType().equals(ac.getAcType())) {
						cellElem.addAttribute("style", "rounded");
					} else if (FlAcType.FORK.getAcType().equals(ac.getAcType())) {
						cellElem.addAttribute("style", "symbol;image=/platform-app/flow/editor/images/symbols/fork.png");
					} else if (FlAcType.JOIN.getAcType().equals(ac.getAcType())) {
						cellElem.addAttribute("style", "symbol;image=/platform-app/flow/editor/images/symbols/merge.png");
					} else if (FlAcType.END.getAcType().equals(ac.getAcType())){
						cellElem.addAttribute("style", FlAcType.END.getNodeType());
					}
					cellElem.addElement("mxGeometry")
							.addAttribute("x", String.valueOf(ac.getX()))
							.addAttribute("y", String.valueOf(ac.getY()))
							.addAttribute("width", String.valueOf(ac.getWidth()))
							.addAttribute("height", String.valueOf(ac.getHeight()))
							.addAttribute("as", "geometry");

				}

			} else if ("step".equals(type)) {
				List<InstanceStep> steps = (List<InstanceStep>) elements;
				for (InstanceStep step : steps) {
					String label = step.getName();
					if(label.indexOf("->")!=-1){
						label = label.substring(0,label.indexOf("->"));
					}
					Element parentElem = root.addElement(FlAcType.CONNECTOR.getNodeName())
							.addAttribute("id", step.getId())
							.addAttribute("label", StringEscapeUtils.unescapeHtml (label))
							.addAttribute("nodeType", FlAcType.CONNECTOR.getNodeType());
					Element cellElem = parentElem.addElement("mxCell")
							.addAttribute("style", "defaultEdge")
							.addAttribute("edge", "1")
							.addAttribute("parent", "1")
							.addAttribute("source", step.getSourceId())
							.addAttribute("target", step.getTargetId());
					cellElem.addElement("mxGeometry")
							.addAttribute("relative", "1")
							.addAttribute("as", "geometry");
				}
			}
		}
	}

	/**
	 * 修改这个实例最新的那条 我的发起 消息
	 * @param instanceId
	 * @param editTitleName
	 */
	@Override
	public void editLastStartMsgTitle(String instanceId,String editTitleName) throws  Exception{
		List<String> list = instanceDao.getMsgIdsByInstanceId(instanceId);
		if(CollectionUtils.isNotEmpty(list)){
			String firstMsgId =	list.get(list.size()-1);
			SysNoticeMsg firstMsg = msgService.getObjectById(firstMsgId);
			if(firstMsg != null){
				firstMsg.setTitle(editTitleName+firstMsg.getTitle());
				msgService.update(firstMsg);
				log.info("修改我的发起 消息标题，增加"+editTitleName+"：msgId=" + firstMsg);
			} else {
				//旧的数据可能会有这种情况
				log.info("发起人任务表的消息ID为空：instanceId=" + instanceId);
			}
		}
	}
	
	@Override
	public void scanExceptionFlow(String userInfo) {
		String approvers = ConfigurationUtil.getValue("scan.approvers");
		try {
			approvers = new String(approvers.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String phones = ConfigurationUtil.getValue("scan.reciever.phones");
		log.info("approvers=" + approvers + ", phones=" + phones);
		List<String> exceptionInstanceIds = scanExceptionInstanceIds(approvers);
		if(CollectionUtils.isNotEmpty(exceptionInstanceIds)) {
			sendMessage(userInfo, phones, exceptionInstanceIds);
		}
	}

	private void sendMessage(String userJson, String phones, List<String> exceptionInstanceIds) {
		String ids = "巨洲云OA异常数据监控报告：";
		Object[] array = exceptionInstanceIds.toArray();
		if(array!=null && array.length>0){
			for(int i=0; i<array.length; i++) {
				ids+=array[i];
				ids+=",";
			}
		}
		if(ids!=null && ids.endsWith(",")){
			ids=ids.substring(0, ids.length()-1);
		}
		log.info("短信内容 ：" + ids);
		Map<String, String> params = new HashMap<String, String>();
		params.put("msg", ids);
		params.put("phones", phones);
		String result = mailAndPhoneServiceCustomer.sendPhoneMsg(userJson, JacksonUtils.toJson(params));
		log.info("短信发送结果：" + result);
	}

	private List<String> scanExceptionInstanceIds(String approvers) {
		List<String> ids = new ArrayList<String>();
//		List<String> names = Arrays.asList(approvers.split(","));
//		List<Map<String, String>> flows = instanceDao.scanFlowOf(names);
//		log.info("相关流程数量：flows.size=" + flows.size());
//		if(CollectionUtils.isEmpty(flows)) {
//			return ids;
//		}
//		log.info("重要人物流程：" + flows);
		Integer totalCount=0;
		Integer count=0;
		//监控1-任务未处理，待办消了
		List<Map<String, String>> monitor1 = instanceDao.monitor1(null);
		count=monitor1==null?0:monitor1.size();
		String instanceIds=this.getInstanceIds(monitor1);
		log.info("监控1-任务未处理，待办消了:"+instanceIds);
		if(count>0){
			ids.add("监控1-任务未处理，待办消了("+count+")");
			totalCount+=count;
		}
		
		//监控2-待办未消，任务已处理
		List<Map<String, String>> monitor2 = instanceDao.monitor2(null);
		count=monitor2==null?0:monitor2.size();
		instanceIds=this.getInstanceIds(monitor2);
		log.info("监控2-待办未消，任务已处理:"+instanceIds);
		if(count>0){
			ids.add("监控2-待办未消，任务已处理("+count+")");
			totalCount+=count;
		}
		
		//监控3-同一人多个任务
		List<Map<String, String>> monitor3 = instanceDao.monitor3(null);
		count=monitor3==null?0:monitor3.size();
		instanceIds=this.getInstanceIds(monitor3);
		log.info("监控3-同一人多个任务:"+instanceIds);
		if(count>0){
			ids.add("监控3-同一人多个任务("+count+")");
			totalCount+=count;
		}
		
		//监控4-审批中流程但移动表单数据为空
		List<Map<String, String>> monitor4 = instanceDao.monitor4(null);
		count=monitor4==null?0:monitor4.size();
		instanceIds=this.getInstanceIds(monitor4);
		log.info("监控4-审批中流程但移动表单数据为空:"+instanceIds);
		if(count>0){
			ids.add("监控4-审批中流程但移动表单数据为空("+count+")");
			totalCount+=count;
		}
		
		//监控5-任务与审批人不一致
		List<Map<String, String>> monitor5 = instanceDao.monitor5(null);
		count=monitor5==null?0:monitor5.size();
		instanceIds=this.getInstanceIds(monitor5);
		log.info("监控5-任务与审批人不一致:"+instanceIds);
		if(count>0){
			ids.add("监控5-任务与审批人不一致("+count+")");
			totalCount+=count;
		}
		
		//监控6-岗位与环节状态异常
		List<Map<String, String>> monitor6 = instanceDao.monitor6(null);
		count=monitor6==null?0:monitor6.size();
		instanceIds=this.getInstanceIds(monitor6);
		log.info("监控6-岗位与环节状态异常:"+instanceIds);
		if(count>0){
			ids.add("监控6-岗位与环节状态异常("+count+")");
			totalCount+=count;
		}
		
		//监控7-JVM异常数据
		List<Map<String, String>> monitor7 = instanceDao.monitor7(null);
		count=monitor7==null?0:monitor7.size();
		instanceIds=this.getInstanceIds(monitor7);
		log.info("监控7-JVM异常数据:"+instanceIds);
		if(count>0){
			ids.add("监控7-JVM异常数据("+count+")");
			totalCount+=count;
		}
		
		//监控8-流程实例当前审批人异常
		List<Map<String, String>> monitor8 = instanceDao.monitor8(null);
		count=monitor8==null?0:monitor8.size();
		instanceIds=this.getInstanceIds(monitor8);
		log.info("监控8-流程实例当前审批人异常:"+instanceIds);
		if(count>0){
			ids.add("监控8-流程实例当前审批人异常("+count+")");
			totalCount+=count;
		}
		
		//监控9-任务审批中对应的消息状态异常
		List<Map<String, String>> monitor9 = instanceDao.monitor9(null);
		count=monitor9==null?0:monitor9.size();
		instanceIds=this.getInstanceIds(monitor9);
		log.info("监控9-任务审批中对应的消息状态异常:"+instanceIds);
		if(count>0){
			ids.add("监控9-任务审批中对应的消息状态异常("+count+")");
			totalCount+=count;
		}

		//监控10-HR系统业务表单状态与平台流程状态不一致
		List<Map<String, String>> monitor10 = instanceDao.monitor10(null);
		count=monitor10==null?0:monitor10.size();
		instanceIds=this.getInstanceIds(monitor10);
		log.info("监控10-HR系统业务表单状态与平台流程状态不一致:"+instanceIds);
		if(count>0){
			ids.add("监控10-HR系统业务表单状态与平台流程状态不一致("+count+")");
			totalCount+=count;
		}
		if(totalCount>0){
			ids.add(0, "异常数据共计("+totalCount+")条");
		}else{
			ids.clear();
			ids.add("无异常");
		}
		return ids;
	}

	private String getInstanceIds(List<Map<String, String>> monitor1) {
		List<String> instanceIds=new ArrayList<String>();
		if(monitor1!=null && monitor1.size()>0){
			for(int i=0;i<monitor1.size();i++){
				Map<String, String> map=monitor1.get(i);
				if(map.get("instanceId")!=null){
					instanceIds.add(map.get("instanceId"));
				}
			}
		}
		return instanceIds.toString();
	}

	private void checkContains(List<Map<String, String>> flows, List<Map<String, String>> monitor, Set<String> ids) {
		if(CollectionUtils.isEmpty(monitor)) {
			return ;
		}
		for(Map<String, String> flow : flows) {
			for(Map<String, String> rec : monitor) {
				String instanceId = flow.get("id");
				if(instanceId.equals(rec.get("instanceId"))) {
					ids.add(instanceId);
					log.info("查询到一条异常流程：" + instanceId);
					break;
				}
			}
		}
	}

	private boolean checkException(Map<String, String> flow) {
		String instanceId = flow.get("id");
		String status = flow.get("status");
		if(InstanceStatus.RUNNING.getValue().equals(status)) {
			
			//监控1-任务未处理，待办消了
			List<Map<String, String>> monitor1 = instanceDao.monitor1(instanceId);
			if(CollectionUtils.isNotEmpty(monitor1)) {
				log.info("监控1-任务未处理，待办消了:" + instanceId);
				return true;
			}
			
			//监控2-待办未消，任务已处理
			List<Map<String, String>> monitor2 = instanceDao.monitor2(instanceId);
			if(CollectionUtils.isNotEmpty(monitor2)) {
				log.info("监控2-待办未消，任务已处理:" + instanceId);
				return true;
			}
			
			//监控3-同一人多个任务
			List<Map<String, String>> monitor3 = instanceDao.monitor3(instanceId);
			if(CollectionUtils.isNotEmpty(monitor3)) {
				log.info("监控3-同一人多个任务:" + instanceId);
				return true;
			}
			
			//监控4-审批中流程但移动表单数据为空
			List<Map<String, String>> monitor4 = instanceDao.monitor4(instanceId);
			if(CollectionUtils.isNotEmpty(monitor4)) {
				log.info("监控4-审批中流程但移动表单数据为空:" + instanceId);
				return true;
			}
			
			//监控5-任务与审批人不一致
			List<Map<String, String>> monitor5 = instanceDao.monitor5(instanceId);
			if(CollectionUtils.isNotEmpty(monitor5)) {
				log.info("监控5-任务与审批人不一致:" + instanceId);
				return true;
			}
			
			//监控6-岗位与环节状态异常
			List<Map<String, String>> monitor6 = instanceDao.monitor6(instanceId);
			if(CollectionUtils.isNotEmpty(monitor6)) {
				log.info("监控6-岗位与环节状态异常:" + instanceId);
				return true;
			}
			
			//监控7-JVM异常数据
			List<Map<String, String>> monitor7 = instanceDao.monitor7(instanceId);
			if(CollectionUtils.isNotEmpty(monitor7)) {
				log.info("监控7-JVM异常数据:" + instanceId);
				return true;
			}
			
			//监控8-普通异常
			List<Map<String, String>> monitor8 = instanceDao.monitor8(instanceId);
			if(CollectionUtils.isNotEmpty(monitor8)) {
				log.info("监控8-普通异常:" + instanceId);
				return true;
			}
			
			//监控9-待办消息，流程异常
			List<Map<String, String>> monitor9 = instanceDao.monitor9(instanceId);
			if(CollectionUtils.isNotEmpty(monitor9)) {
				log.info("监控9-待办消息，流程异常" + instanceId);
				return true;
			}
			
		} else if(InstanceStatus.WITHDRAW.getValue().equals(status)) {
			
		} else if(InstanceStatus.REJECT.getValue().equals(status)) {
			
		} else if(InstanceStatus.CANCEL.getValue().equals(status)) {
			
		} else if(InstanceStatus.HANGUP.getValue().equals(status)) {
			
		}
		
		return false;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Map<String, Object> saveModifyInstance(Map<String, Object> saveMap) throws Exception{
		String instanceId = (String) saveMap.get("instanceId");
		String currentAcId = (String) saveMap.get("currentAcId");
		String oldCurrentAcId = (String) saveMap.get("oldCurrentAcId");

		Instance instance = this.getObjectById(instanceId);

		//获取实例节点列表
		List<Map<String,Object>> acList = (List<Map<String, Object>>) saveMap.get("acList");

		//获取审批人
		List<Map<String,Object>> approverList = (List<Map<String, Object>>) saveMap.get("approverList");
		//对审批人进行排序
		Map<String,Object> sortResult = sortInstanceApprover(approverList);
		Map<String,Integer> acPostNumMap = (Map<String, Integer>) sortResult.get("acPostNumMap");//同一环节岗位数量
		Map<String,Integer> acRepeatPostApproverNumMap = (Map<String, Integer>) sortResult.get("acRepeatPostApproverNumMap");//同一环节同一岗位人员数量
		Set<String> autoPassParticipantIdSet = (Set<String>) sortResult.get("autoPassParticipantIdSet");

		/*Map<String,Map<String,Object>> acIdObjMap = new HashMap<String,Map<String,Object>>();
		for (Map<String,Object> acMap:acList) {
			String acId = (String) acMap.get("id");
			acIdObjMap.put(acId,acMap);
		}*/
		Map<String,Object> processAcResult = batchProcessModifyInstanceAc(instanceId,acList,currentAcId,oldCurrentAcId,acPostNumMap);
		InstanceAc oldRuntimeAc = (InstanceAc) processAcResult.get("oldRuntimeAc");

		//处理审批人
		Map<String,Object> processApproverResult = batchProcessModifyInstanceApprover(instance,approverList,currentAcId,oldRuntimeAc,acRepeatPostApproverNumMap,autoPassParticipantIdSet);
		List<InstanceAc> batchSaveAcList = (List<InstanceAc>) processAcResult.get("batchSaveAcList");
		List<InstanceAc> batchUpdateAcList = (List<InstanceAc>) processAcResult.get("batchUpdateAcList");
		List<String> deleteAcIds = (List<String>) processAcResult.get("deleteAcIds");

		//保存新增节点信息
		if(CollectionUtils.isNotEmpty(batchSaveAcList)){
			instanceAcService.saveBatch(batchSaveAcList);
		}
		//保存更新后的节点信息
		if(CollectionUtils.isNotEmpty(batchUpdateAcList)){
			instanceAcService.updateBatch(batchUpdateAcList);
		}
		//删除需要删除的节点信息
		if(CollectionUtils.isNotEmpty(deleteAcIds)){
			instanceAcService.deleteAllObjectByIds(deleteAcIds);
		}

		//获取节点连线列表
		List<Map<String,Object>> stepList = (List<Map<String, Object>>) saveMap.get("stepList");
		batchProcessModifyInstanceStep(instanceId,stepList);


		//获取可阅人
		List<Map<String,Object>> accessibles = (List<Map<String, Object>>) saveMap.get("accessibles");
		accessibles = accessibles==null?new ArrayList<Map<String,Object>>():accessibles;
		batchProcessModifyInstanceAccessible(instanceId,accessibles);

		//获取抄送人
		List<Map<String,Object>> ccPersonList = (List<Map<String, Object>>) saveMap.get("ccPersonList");
		batchProcessModifyInstanceCs(instanceId,ccPersonList);
		return saveMap;
	}

	//处理节点信息
	private Map<String,Object> batchProcessModifyInstanceAc(String instanceId,List<Map<String,Object>> acList,String currentAcId,String oldCurrentAcId,Map<String,Integer> acPostNumMap) throws Exception{
		/*NOT_RUNNING("未运行", "1"), RUNNING("运行中", "2"), FINISHED("完成", "3"), HANGUP("挂起", "4");*/
		Map<String,Object> oldAcParam = new HashMap<String,Object>();
		oldAcParam.put("fiId",instanceId);
		List<InstanceAc> oldInstanceAcList = instanceAcService.queryList(oldAcParam);
		Map<String,Object> oldInstancAcMaps = new HashMap<String,Object>();
		for (InstanceAc oldInstanceAc:oldInstanceAcList) {
			String oldInstanceAcJson = JacksonUtils.toJson(oldInstanceAc);
			Map<String,Object> oldInstancAcMap = JacksonUtils.fromJson(oldInstanceAcJson,Map.class);

			oldInstancAcMaps.put(oldInstanceAc.getId(),oldInstancAcMap);
		}


		Set<String> oldAcIdSet = oldInstancAcMaps.keySet();

		//Map<String,Object> newInstancAcMaps = new HashMap<String,Object>();
		List<InstanceAc> batchUpdateAcList = new ArrayList<InstanceAc>();
		List<InstanceAc> batchSaveAcList = new ArrayList<InstanceAc>();
		InstanceAc oldRuntimeAc = null;
		InstanceAc currentRuntimeAc = null;
		for (Map<String,Object> instanceAcMap:acList) {
			String acId = (String) instanceAcMap.get("id");
			String acType = (String) instanceAcMap.get("acType");
			if(oldAcIdSet.contains(acId)){
				Map<String,Object> oldInstanceAcMap = (Map<String, Object>) oldInstancAcMaps.get(acId);
				oldInstanceAcMap.putAll(instanceAcMap);

				String udpdateAcJson = JacksonUtils.toJson(oldInstanceAcMap);
				InstanceAc updateInstanceAc = JacksonUtils.fromJson(udpdateAcJson,InstanceAc.class);
				updateInstanceAc.setDelflag(false);
				Integer acPostNum = acPostNumMap.get(acId);
				acPostNum = acPostNum==null?0:acPostNum;
				String acStatus = updateInstanceAc.getStatus();
				if(ACStatus.FINISHED.equals(acStatus)){
					acPostNum = 0;
				}
				//计算聚合网关leftPost数量
				if("4".equals(acType)){
					String preAcIds = (String) instanceAcMap.get("preAcIds");
					String[] preAcIdArr = preAcIds.split(",");
					acPostNum = preAcIdArr.length;
					for (int i = 0; i < preAcIdArr.length; i++) {
						String preAcId = preAcIdArr[i];
						Map<String,Object> oldAc = (Map<String,Object>) oldInstancAcMaps.get(preAcId);
						if(oldAc!=null){
							String oldAcStatus = (String) oldAc.get("status");
							String oldAcType = (String) oldAc.get("acType");
							if(ACStatus.FINISHED.equals(oldAcStatus)||(!FlAcType.TASK.getAcType().equals(oldAcType))){
								acPostNum -= 1;
							}
						}
					}
				}
				updateInstanceAc.setLeftPost(acPostNum);
				batchUpdateAcList.add(updateInstanceAc);
				if(updateInstanceAc.getId().equals(currentAcId)){
					currentRuntimeAc = updateInstanceAc;
					updateInstanceAc.setStatus(ACStatus.RUNNING.getValue());
				}

				if(updateInstanceAc.getId().equals(oldCurrentAcId)){
					oldRuntimeAc = updateInstanceAc;
				}

				oldAcIdSet.remove(acId);
			}else{
				String newInstanceAcJson = JacksonUtils.toJson(instanceAcMap);
				InstanceAc newInstanceAc = JacksonUtils.fromJson(newInstanceAcJson,InstanceAc.class);
				if(newInstanceAc.getId().equals(currentAcId)){
					newInstanceAc.setStatus(ACStatus.RUNNING.getValue());
				}else{
					newInstanceAc.setStatus(ACStatus.NOT_RUNNING.getValue());
				}

				Integer acPostNum = acPostNumMap.get(acId);
				acPostNum = acPostNum==null?0:acPostNum;
				newInstanceAc.setLeftPost(acPostNum);
				newInstanceAc.setDelflag(false);
				if(newInstanceAc.getId().equals(currentAcId)){
					currentRuntimeAc = newInstanceAc;
				}
				batchSaveAcList.add(newInstanceAc);
			}
		}

		//如果将原始当前节点删除，则不计算原始
		if(oldRuntimeAc!=null){
			Long oldRuntimeAcPx = oldRuntimeAc.getPx();
			Long currentRuntimeAcPx = currentRuntimeAc.getPx();
			if(currentRuntimeAcPx>oldRuntimeAcPx){
				oldRuntimeAc.setStatus(ACStatus.FINISHED.getValue());
				batchUpdateAcList.add(oldRuntimeAc);
			}else if(currentRuntimeAcPx<oldRuntimeAcPx){
				oldRuntimeAc.setStatus(ACStatus.NOT_RUNNING.getValue());
				batchUpdateAcList.add(oldRuntimeAc);
			}
		}


		//保存新增节点信息
		//instanceAcService.saveBatch(batchSaveAcList);
		//保存更新后的节点信息
		//instanceAcService.updateBatch(batchUpdateAcList);

		//删除json
		List<String> deleteAcIds = null;
		if(CollectionUtils.isNotEmpty(oldAcIdSet)){
			String deleteAcIdsJson = JacksonUtils.toJson(oldAcIdSet);
			deleteAcIds = JacksonUtils.fromJson(deleteAcIdsJson,List.class,String.class);
			//instanceAcService.deleteAllObjectByIds(deleteAcIds);
		}

		Map<String,Object> result = new HashMap<String,Object>();
		result.put("batchSaveAcList",batchSaveAcList);
		result.put("batchUpdateAcList",batchUpdateAcList);
		result.put("deleteAcIds",deleteAcIds);
		result.put("oldRuntimeAc",oldRuntimeAc);


		return result;
	}

	//处理连线信息
	private List<InstanceStep> batchProcessModifyInstanceStep(String instanceId,List<Map<String,Object>> stepList) throws Exception{
		Map<String,Object> oldStepParam = new HashMap<String,Object>();
		oldStepParam.put("fiId",instanceId);
		List<InstanceStep> oldInstanceStepList = instanceStepService.queryList(oldStepParam);
		List<String> deleteStepIds = new ArrayList<String>();
		for (InstanceStep oldInstanceStep:oldInstanceStepList) {
			deleteStepIds.add(oldInstanceStep.getId());
		}
		if(CollectionUtils.isNotEmpty(deleteStepIds)){
			instanceStepService.deleteAllObjectByIds(deleteStepIds);
		}


		List<InstanceStep> batchSaveInstanceStepList = new ArrayList<InstanceStep>();
		for (Map<String,Object> instanceStepMap:stepList) {
			String stepJson = JacksonUtils.toJson(instanceStepMap);
			InstanceStep instanceStep = JacksonUtils.fromJson(stepJson,InstanceStep.class);
			instanceStep.setDelflag(false);
			batchSaveInstanceStepList.add(instanceStep);
		}

		instanceStepService.saveBatch(batchSaveInstanceStepList);
		return batchSaveInstanceStepList;
	}

	//处理可阅人
	private List<InstanceAccessible> batchProcessModifyInstanceAccessible(String instanceId,List<Map<String,Object>> accessibles) throws Exception{
		Map<String ,Object> accesibleParam = new HashMap<String,Object>();
		accesibleParam.put("fiId",instanceId);
		List<InstanceAccessible> instanceAccessibleList = instanceAccessibleService.queryList(accesibleParam);
		List<String> deleteAccessibleIdList = new ArrayList<String>();
		for (InstanceAccessible instanceAccessible:instanceAccessibleList) {
			deleteAccessibleIdList.add(instanceAccessible.getId());
		}
		if(CollectionUtils.isNotEmpty(deleteAccessibleIdList)){
			instanceAccessibleService.deleteAllObjectByIds(deleteAccessibleIdList);
		}


		List<InstanceAccessible> batchSaveInstanceAccessibleList = new ArrayList<InstanceAccessible>();
		for (Map<String,Object> accessible:accessibles) {
			String accessibleJson = JacksonUtils.toJson(accessible);
			InstanceAccessible newInstanceAccessible = JacksonUtils.fromJson(accessibleJson,InstanceAccessible.class);
			if(org.apache.commons.lang.StringUtils.isBlank(newInstanceAccessible.getId())){
				newInstanceAccessible.setId(IDGenerator.getUUID());
			}
			newInstanceAccessible.setFiId(instanceId);
			newInstanceAccessible.setDelflag(false);
			batchSaveInstanceAccessibleList.add(newInstanceAccessible);
		}
		instanceAccessibleService.saveBatch(batchSaveInstanceAccessibleList);


		return batchSaveInstanceAccessibleList;
	}

	//对人员进行排序
	private Map<String,Object> sortInstanceApprover(List<Map<String,Object>> approverList){
		Map<String,Integer> acRepeatPostApproverNumMap = new HashMap<String,Integer>();
		Map<String,Integer> repeatPostApproverNumMap = new HashMap<String,Integer>();
		Map<String,Map<String,Integer>> acRepeatPostMap = new HashMap<String,Map<String,Integer>>();
		Map<String,Map<String,Integer>> acRepeatPostMap1 = new HashMap<String,Map<String,Integer>>();

		Set<String> autoPassParticipantIdSet = new HashSet<String>();
		Set<String> unAutoPassParticipantIdSet = new HashSet<String>();
		//重新对post和人员分别排序
		for (Map<String,Object> newApproverMap:approverList) {
			String acId = (String) newApproverMap.get("acId");
			String postId = (String) newApproverMap.get("postId");
			Integer repeatPostNum = acRepeatPostApproverNumMap.get(acId+"/"+postId);
			if(repeatPostNum==null){
				repeatPostNum = 0;
			}
			repeatPostNum += 1;
			newApproverMap.put("px",repeatPostNum);
			acRepeatPostApproverNumMap.put(acId+"/"+postId,repeatPostNum);

			//计算同岗重复人数
			Integer repeatPostApproverNum = repeatPostApproverNumMap.get(acId+"/"+postId);
			if(repeatPostApproverNum==null){
				repeatPostApproverNum = 0;
			}
			String approverStatus = (String) newApproverMap.get("status");
			String approverTaskStatus = (String) newApproverMap.get("taskStatus");
			if(!TaskStatus.FINISHED.getValue().equals(approverTaskStatus)){
				repeatPostApproverNum += 1;
				repeatPostApproverNumMap.put(acId+"/"+postId,repeatPostApproverNum);
			}

			Map<String,Integer> postRepeatMap = acRepeatPostMap.get(acId);
			if(postRepeatMap==null){
				postRepeatMap = new HashMap<String,Integer>();
			}
			Integer postPx = postRepeatMap.get(postId);
			postPx = postPx==null?postRepeatMap.size()+1:postPx;
			//postPx += 1;
			newApproverMap.put("postPx",postPx);
			postRepeatMap.put(postId,postPx);
			acRepeatPostMap.put(acId,postRepeatMap);


			//计算统一环节岗位重复
			String postStatus = (String) newApproverMap.get("postStatus");
			if(!PostStatus.FINISHED.getValue().equals(postStatus)){
				Map<String,Integer> postRepeatMap1 = acRepeatPostMap1.get(acId);
				if(postRepeatMap1==null){
					postRepeatMap1 = new HashMap<String,Integer>();
				}
				Integer postPx1 = postRepeatMap1.get(postId);
				postPx1 = postPx1==null?0:postPx1;
				postPx1 += 1;
				postRepeatMap1.put(postId,postPx1);
				acRepeatPostMap1.put(acId,postRepeatMap1);
			}

			Integer autoPass = (Integer) newApproverMap.get("autoPass");
			String participantId = (String) newApproverMap.get("participantId");
			if(autoPass!=null&&autoPass==1&&(!autoPassParticipantIdSet.contains(participantId)&&!unAutoPassParticipantIdSet.contains(participantId))){
				autoPassParticipantIdSet.add(participantId);
			}else if(autoPass!=null&&autoPass==-1){
				autoPassParticipantIdSet.remove(participantId);
				unAutoPassParticipantIdSet.add(participantId);
			}

		}

		//计算同一节点下共有多少岗位
		/*Set<String> postReatSet = repeatPostApproverNumMap.keySet();
		Map<String,Integer> acPostNumMap = new HashMap<String,Integer>();
		for (String acAndPostId:postReatSet) {
			String[] arr = acAndPostId.split("/");
			String acId = arr[0];
			String postId = arr[1];
			Integer postNum = acPostNumMap.get(acId);

			if(postNum==null){
				postNum = 0;
			}

			postNum++;
			acPostNumMap.put(acId,postNum);
		}*/
		Set<String> acIdList = acRepeatPostMap1.keySet();
		Map<String,Integer> acPostNumMap = new HashMap<String,Integer>();
		for (String acId:acIdList) {
			Map<String,Integer> map = acRepeatPostMap1.get(acId);
			acPostNumMap.put(acId,map.keySet().size());
		}


		Map<String,Object> result = new HashMap<String,Object>();
		result.put("acPostNumMap",acPostNumMap);//同一环节岗位数量
		result.put("acRepeatPostApproverNumMap",repeatPostApproverNumMap);//同一环节同一岗位人员数量
		result.put("autoPassParticipantIdSet",autoPassParticipantIdSet);//自动通过人员id集合

		return result;
	}
	//处理审批人
	private Map<String,Object> batchProcessModifyInstanceApprover(Instance instance,List<Map<String,Object>> approverList,String currentAcId,InstanceAc oldRuntimeAc,Map<String,Integer> acRepeatPostApproverNumMap,Set<String> autoPassParticipantIdSet) throws Exception {
		String instanceId = instance.getId();
		String oldRuntimeAcId = oldRuntimeAc!=null?oldRuntimeAc.getId():null;
		String oldRuntimeAcStatus = oldRuntimeAc!=null?oldRuntimeAc.getStatus():null;
		//环节审批人列表
		Map<String,Object> instanceGroupParamMap = new HashMap<String,Object>();
		instanceGroupParamMap.put("instanceId",instanceId);
		List<Map<String,Object>> oldInstanceGroupList = instanceGroupService.queryListByInstanceId(instanceGroupParamMap);

		Map<String,Map<String,Object>> oldInstancePostMap = new HashMap<String,Map<String,Object>>();
		Map<String,Map<String,Object>> oldInstanceTaskMap = new HashMap<String,Map<String,Object>>();
		Map<String,Map<String,Object>> oldInstanceGroupMap = new HashMap<String,Map<String,Object>>();

		//去除重复岗位
		Map<String,String> acAndPostIdMap = new HashMap<String,String>();
		for (Map<String,Object> oldInstanceGroup:oldInstanceGroupList) {
			String acPostId = (String) oldInstanceGroup.get("acPostId");
			String acTaskId = (String) oldInstanceGroup.get("taskId");
			String groupId = (String) oldInstanceGroup.get("id");

			String acId = (String) oldInstanceGroup.get("acId");
			String postId = (String) oldInstanceGroup.get("postId");
			acAndPostIdMap.put(acId+"/"+postId,acPostId);
			if(acPostId!=null){
				Map<String,Object> oldPostMap = new HashMap<String,Object>();
				oldPostMap.put("id",oldInstanceGroup.get("acPostId"));
				oldPostMap.put("acId",oldInstanceGroup.get("acId"));
				oldPostMap.put("postName",oldInstanceGroup.get("postName"));
				oldPostMap.put("postId",oldInstanceGroup.get("postId"));
				oldPostMap.put("status",oldInstanceGroup.get("postStatus"));
				oldPostMap.put("activateDate",oldInstanceGroup.get("postActivateDate"));
				oldPostMap.put("endDate",oldInstanceGroup.get("postEndDate"));
				oldPostMap.put("leftPerson",oldInstanceGroup.get("leftPerson"));
				oldPostMap.put("px",oldInstanceGroup.get("postPx"));
				oldPostMap.put("concurrencyVersion",oldInstanceGroup.get("postVersion"));
				oldInstancePostMap.put(acPostId,oldPostMap);
			}

			if (groupId != null) {
				Map<String,Object> oldGroupMap = new HashMap<String,Object>();
				oldGroupMap.put("id",oldInstanceGroup.get("id"));
				oldGroupMap.put("acId",oldInstanceGroup.get("acId"));
				oldGroupMap.put("acPostId",oldInstanceGroup.get("acPostId"));
				oldGroupMap.put("postName",oldInstanceGroup.get("postName"));
				oldGroupMap.put("postId",oldInstanceGroup.get("postId"));
				oldGroupMap.put("activateDate",oldInstanceGroup.get("activateDate"));
				oldGroupMap.put("endDate",oldInstanceGroup.get("endDate"));
				oldGroupMap.put("status",oldInstanceGroup.get("status"));
				oldGroupMap.put("disable",oldInstanceGroup.get("disable"));
				oldGroupMap.put("disableType",oldInstanceGroup.get("disableType"));
				oldGroupMap.put("parseType",oldInstanceGroup.get("parseType"));
				oldGroupMap.put("participantName",oldInstanceGroup.get("participantName"));
				oldGroupMap.put("participantId",oldInstanceGroup.get("participantId"));
				oldGroupMap.put("px",oldInstanceGroup.get("px"));
				oldGroupMap.put("sourceId",oldInstanceGroup.get("sourceId"));
				oldGroupMap.put("source",oldInstanceGroup.get("source"));
				Integer autoPass = 0;
				if(oldInstanceGroup.get("autoPass")!=null){
					String autoPassStr = oldInstanceGroup.get("autoPass").toString();
					autoPassStr = autoPassStr!=null?autoPassStr:"-1";
					autoPass = ("1".equals(autoPassStr)||"true".equals(autoPassStr))?1:Integer.parseInt(autoPassStr);//Boolean.valueOf(oldInstanceGroup.get("autoPass").toString())==true?1:0;
					String participantId = (String) oldInstanceGroup.get("participantId");
					if(autoPass==1&&autoPassParticipantIdSet.contains(participantId)){
						autoPass = 0;
					}
				}
				oldGroupMap.put("autoPass",autoPass==null?0:autoPass);
				oldGroupMap.put("proxy",oldInstanceGroup.get("proxy"));
				oldGroupMap.put("proxyed",oldInstanceGroup.get("proxyed"));
				oldGroupMap.put("proxyType",oldInstanceGroup.get("proxyType"));
				oldGroupMap.put("concurrencyVersion",oldInstanceGroup.get("concurrencyVersion"));
				oldInstanceGroupMap.put(groupId,oldGroupMap);
			}

			if(acTaskId!=null){
				Map<String,Object> oldTaskMap = new HashMap<String,Object>();
				oldTaskMap.put("id",oldInstanceGroup.get("taskId"));
				oldTaskMap.put("status",oldInstanceGroup.get("taskStatus"));
				oldTaskMap.put("groupId",oldInstanceGroup.get("groupId"));
				oldTaskMap.put("msgId",oldInstanceGroup.get("msgId"));
				oldTaskMap.put("operationName",oldInstanceGroup.get("operationName"));
				oldTaskMap.put("operationCode",oldInstanceGroup.get("operationCode"));
				oldTaskMap.put("userNote",oldInstanceGroup.get("userNote"));
				oldTaskMap.put("relationTaskId",oldInstanceGroup.get("relationTaskId"));
				oldTaskMap.put("relationParticipantId",oldInstanceGroup.get("relationParticipantId"));
				oldTaskMap.put("relationParticipant",oldInstanceGroup.get("relationParticipant"));
				oldTaskMap.put("disable",oldInstanceGroup.get("taskDisable"));
				oldTaskMap.put("disableType",oldInstanceGroup.get("taskDisableType"));
				oldTaskMap.put("sourceId",oldInstanceGroup.get("taskSourceId"));
				oldTaskMap.put("source",oldInstanceGroup.get("taskSource"));
				oldTaskMap.put("type",oldInstanceGroup.get("type"));
				oldTaskMap.put("approverName",oldInstanceGroup.get("approverName"));
				oldTaskMap.put("approverId",oldInstanceGroup.get("approverId"));
				oldTaskMap.put("activateDate",oldInstanceGroup.get("taskActivateDate"));
				oldTaskMap.put("endDate",oldInstanceGroup.get("taskEndDate"));
				oldTaskMap.put("concurrencyVersion",oldInstanceGroup.get("taskVersion"));
				oldTaskMap.put("endDate",oldInstanceGroup.get("taskEndTime"));
				oldInstanceTaskMap.put(acTaskId,oldTaskMap);
			}


		}


		List<String> instancePostIdList = new ArrayList<String>();
		List<String> instanceTaskIdList = new ArrayList<String>();
		List<String> instanceGroupIdList = new ArrayList<String>();

		List<InstancePost> batchUpdatePostList = new ArrayList<InstancePost>();
		List<InstancePost> batchSavePostList = new ArrayList<InstancePost>();

		List<InstanceGroup> batchUpdateGroupList = new ArrayList<InstanceGroup>();
		List<InstanceGroup> batchSaveGroupList = new ArrayList<InstanceGroup>();

		List<InstanceTask> batchUpdateTaskList = new ArrayList<InstanceTask>();
		List<InstanceTask> batchSaveTaskList = new ArrayList<InstanceTask>();

		Set<String> oldInstancePostIdSet = oldInstancePostMap.keySet();
		Set<String> deleteInstancePostIdSet = new HashSet<String>();
		deleteInstancePostIdSet.addAll(oldInstancePostIdSet);

		Set<String> oldInstanceGroupIdSet = oldInstanceGroupMap.keySet();
		Set<String> deleteInstanceGroupIdSet = new HashSet<String>();
		deleteInstanceGroupIdSet.addAll(oldInstanceGroupIdSet);

		Set<String> oldInstanceTaskIdSet = oldInstanceTaskMap.keySet();
		Set<String> deleteInstanceTaskIdSet = new HashSet<String>();
		deleteInstanceTaskIdSet.addAll(oldInstanceTaskIdSet);

		//需要删除的消息id列表
		List<String> deleteMsgIdList = new ArrayList<String>();

		//需要发送消息的任务
		Map<String,InstanceTask> sendMsgTaskMap = new HashMap<String,InstanceTask>();
		//需要发送消息的集合
		List<SysNoticeMsg> saveBatchMsgList = new ArrayList<SysNoticeMsg>();

		//获取新审批人
		for (Map<String,Object> approverMap:approverList) {

			/*节点状态：NOT_RUNNING("未运行", "1"), RUNNING("运行中", "2"), FINISHED("完成", "3"), HANGUP("挂起", "4");*/

			/*NOT_RUNNING("未运行", "1"), RUNNING("运行中", "2"), FINISHED("完成", "3"), HANGUP("挂起", "4");*/
			String groupStatus = (String) approverMap.get("status");
			/*NOT_RUNNING("未运行", "1"), RUNNING("运行中", "2"), FINISHED("完成", "3"), HANGUP("挂起", "4");*/
			String postStatus = (String) approverMap.get("postStatus");
			/*NOT_RUNNING("未运行", "1"), RUNNING("运行中", "2"), FINISHED("完成", "3"), SKIP("跳过", "4"),//竞争时跳过 WITHDRAW("流程撤回", "5");	//流程撤回*/
			//String taskStatus = (String) approverMap.get("taskStatus");

			String groupId = (String) approverMap.get("id");
			//审批人id
			String participantId = (String) approverMap.get("participantId");
			//审批人名称
			String participantName = (String) approverMap.get("participantName");
			//
			String postId = (String) approverMap.get("postId");
			String postName = (String) approverMap.get("postName");
			participantId = participantId.replace(postId+'/',"");
			participantName = participantName.replace(postName+'/',"");
			Integer px = (Integer) approverMap.get("px");
			Integer postPx = (Integer) approverMap.get("postPx");
			/*String postStatus = (String) approverMap.get("postStatus");
			String groupStatus = (String) approverMap.get("status");*/

			//节点ID
			String acId = (String) approverMap.get("acId");


			String acPostId = (String) approverMap.get("acPostId");
			if(org.apache.commons.lang.StringUtils.isBlank(acPostId)){
				if(acAndPostIdMap.keySet().contains(acId+"/"+postId)){
					acPostId = acAndPostIdMap.get(acId+"/"+postId);
				}else{
					acPostId = IDGenerator.getUUID();
				}

			}
			String taskId = (String) approverMap.get("taskId");
			if(org.apache.commons.lang.StringUtils.isBlank(taskId)&& org.apache.commons.lang.StringUtils.isNotBlank(groupId)){
				taskId = IDGenerator.getUUID();
			}

			if (groupId != null) {
				instanceGroupIdList.add(groupId);
			}

			if(acPostId!=null){
				instancePostIdList.add(acPostId);
			}

			if(taskId!=null){
				instanceTaskIdList.add(taskId);
			}


			//岗位
			Map<String,Object> newInstancePostMap = new HashMap<String,Object>();
			newInstancePostMap.put("id",acPostId);
			newInstancePostMap.put("acId",acId);
			newInstancePostMap.put("postName",postName);
			newInstancePostMap.put("postId",postId);
			newInstancePostMap.put("px",postPx);
			if(oldInstancePostIdSet.contains(acPostId)){
				Map<String,Object> updateInstancePostMap = oldInstancePostMap.get(acPostId);
				updateInstancePostMap.putAll(newInstancePostMap);
				String updatePostJson = JacksonUtils.toJson(updateInstancePostMap);
				InstancePost updateInstancePost = JacksonUtils.fromJson(updatePostJson,InstancePost.class);
				if(acId.equals(currentAcId)){
					if(!PostStatus.FINISHED.getValue().equals(updateInstancePost.getStatus())&&!PostStatus.HANGUP.getValue().equals(updateInstancePost.getStatus())){
						updateInstancePost.setStatus(PostStatus.RUNNING.getValue());
					}
				}
				if(acId.equals(oldRuntimeAcId)&&!currentAcId.equals(oldRuntimeAcId)){
					if(!PostStatus.FINISHED.getValue().equals(updateInstancePost.getStatus())&&!PostStatus.HANGUP.getValue().equals(updateInstancePost.getStatus())){
						if(ACStatus.RUNNING.getValue().equals(oldRuntimeAcStatus)){
							updateInstancePost.setStatus(PostStatus.NOT_RUNNING.getValue());
						}

					}
				}
				Integer acRepeatPostApproverNum = acRepeatPostApproverNumMap.get(acId+"/"+postId);
				acRepeatPostApproverNum = acRepeatPostApproverNum==null?0:acRepeatPostApproverNum;
				if(PostStatus.FINISHED.getValue().equals(updateInstancePost.getStatus())){
					acRepeatPostApproverNum = 0;
				}
				//判断同节点同岗位新加人员
				if(!oldInstanceGroupIdSet.contains(groupId)){
					if(acId.equals(currentAcId)){
						updateInstancePost.setStatus(PostStatus.RUNNING.getValue());

					}
				}else if(oldInstanceGroupIdSet.contains(taskId)){
					Map<String,Object> oldTaskMap = oldInstanceTaskMap.get(taskId);
					String oldTaskStatus = (String) oldTaskMap.get("status");
					if(TaskStatus.RUNNING.getValue().equals(oldTaskStatus)){
						updateInstancePost.setStatus(PostStatus.RUNNING.getValue());
					}
				}
				updateInstancePost.setLeftPerson(acRepeatPostApproverNum);
				batchUpdatePostList.add(updateInstancePost);
				deleteInstancePostIdSet.remove(acPostId);
			}else{
				if(!acAndPostIdMap.keySet().contains(acId+"/"+postId)){
					newInstancePostMap.put("delflag",false);
					newInstancePostMap.put("status","1");
					String newPostJson = JacksonUtils.toJson(newInstancePostMap);
					InstancePost newInstancePost = JacksonUtils.fromJson(newPostJson,InstancePost.class);

					if(acId.equals(currentAcId)){
						if(!PostStatus.FINISHED.getValue().equals(newInstancePost.getStatus())&&!PostStatus.HANGUP.getValue().equals(newInstancePost.getStatus())){
							newInstancePost.setStatus(PostStatus.RUNNING.getValue());
						}
					}

					Integer acRepeatPostApproverNum = acRepeatPostApproverNumMap.get(acId+"/"+postId);
					acRepeatPostApproverNum = acRepeatPostApproverNum==null?0:acRepeatPostApproverNum;
					if(PostStatus.FINISHED.getValue().equals(newInstancePost.getStatus())){
						acRepeatPostApproverNum = 0;
					}
					newInstancePost.setLeftPerson(acRepeatPostApproverNum);
					batchSavePostList.add(newInstancePost);
					acAndPostIdMap.put(acId+"/"+postId,newInstancePost.getId());
				}

			}


			//审批人
			if(org.apache.commons.lang.StringUtils.isNotBlank(groupId)){

				Map<String,Object> newInstanceGroupMap = new HashMap<String,Object>();
				newInstanceGroupMap.put("id",groupId);
				newInstanceGroupMap.put("participantName",participantName);
				newInstanceGroupMap.put("participantId",participantId);
				newInstanceGroupMap.put("acPostId",acPostId);
				newInstanceGroupMap.put("postId",postId);
				newInstanceGroupMap.put("postName",postName);
				newInstanceGroupMap.put("acId",acId);
				newInstanceGroupMap.put("px",px);
				if(oldInstanceGroupIdSet.contains(groupId)){
					Map<String,Object> updateInstanceGroupMap = oldInstanceGroupMap.get(groupId);
					updateInstanceGroupMap.putAll(newInstanceGroupMap);
					String updateGroupJson = JacksonUtils.toJson(updateInstanceGroupMap);
					InstanceGroup updateInstanceGroup = JacksonUtils.fromJson(updateGroupJson,InstanceGroup.class);
					batchUpdateGroupList.add(updateInstanceGroup);
					if(acId.equals(currentAcId)){
						if(!ApproverStatus.FINISHED.getValue().equals(updateInstanceGroup.getStatus())&&!ApproverStatus.HANGUP.getValue().equals(updateInstanceGroup.getStatus())){
							updateInstanceGroup.setStatus(ApproverStatus.RUNNING.getValue());
						}
					}
					if(acId.equals(oldRuntimeAcId)&&!currentAcId.equals(oldRuntimeAcId)){
						if(!ApproverStatus.FINISHED.getValue().equals(updateInstanceGroup.getStatus())&&!ApproverStatus.HANGUP.getValue().equals(updateInstanceGroup.getStatus())){
							if(ACStatus.RUNNING.getValue().equals(oldRuntimeAcStatus)){
								updateInstanceGroup.setStatus(ApproverStatus.NOT_RUNNING.getValue());
							}

						}
					}
					deleteInstanceGroupIdSet.remove(groupId);
				}else{
					newInstanceGroupMap.put("delflag",false);
					newInstanceGroupMap.put("status","1");
					String newGroupJson = JacksonUtils.toJson(newInstanceGroupMap);
					InstanceGroup newInstanceGroup = JacksonUtils.fromJson(newGroupJson,InstanceGroup.class);
					String oldAcPostId = acAndPostIdMap.get(acId+"/"+postId);
					if(oldAcPostId!=null){
						newInstanceGroup.setAcPostId(oldAcPostId);
					}
					if(acId.equals(currentAcId)){
						if(!ApproverStatus.FINISHED.getValue().equals(newInstanceGroup.getStatus())&&!ApproverStatus.HANGUP.getValue().equals(newInstanceGroup.getStatus())){
							newInstanceGroup.setStatus(ApproverStatus.RUNNING.getValue());
						}
					}
					batchSaveGroupList.add(newInstanceGroup);

				}
			}

			//任务
			if(org.apache.commons.lang.StringUtils.isNotBlank(taskId)&& org.apache.commons.lang.StringUtils.isNotBlank(groupId)){
				Map<String,Object> newInstanceTaskMap = new HashMap<String,Object>();
				newInstanceTaskMap.put("id",taskId);
				newInstanceTaskMap.put("approverName",participantName);
				newInstanceTaskMap.put("approverId",participantId);
				newInstanceTaskMap.put("groupId",groupId);
				if(oldInstanceTaskIdSet.contains(taskId)){

					Map<String,Object> updateInstanceTaskMap = oldInstanceTaskMap.get(taskId);
					updateInstanceTaskMap.putAll(newInstanceTaskMap);
					String updateTaskJson = JacksonUtils.toJson(updateInstanceTaskMap);
					InstanceTask updateInstanceTask = JacksonUtils.fromJson(updateTaskJson,InstanceTask.class);
					String taskType = updateInstanceTask.getType();
					if(!TaskType.ASSIST.getValue().equals(taskType)){
						updateInstanceTask.setType(TaskType.APPROVER.getValue());
					}
					if(acId.equals(currentAcId)){
						if(!TaskStatus.FINISHED.getValue().equals(updateInstanceTask.getStatus())
								&&!TaskStatus.SKIP.getValue().equals(updateInstanceTask.getStatus())
								&&!TaskStatus.WITHDRAW.getValue().equals(updateInstanceTask.getStatus())){
							updateInstanceTask.setStatus(TaskStatus.RUNNING.getValue());
							if(!currentAcId.equals(oldRuntimeAcId)){
								//sendMsgTaskMap.put(taskId,updateInstanceTask);
								SysNoticeMsg msg = newModifyInstanceMsg(instance,updateInstanceTask);
								saveBatchMsgList.add(msg);
							}
						}
					}
					//判断原当前运行节点与当前运行节点是否相同
					//当前运行节点与原运行节点不同时撤销原运行节点的任务
					//相同时保持原来任务状态及待办消息
					if(acId.equals(oldRuntimeAcId)&&!currentAcId.equals(oldRuntimeAcId)){
						/*if(!TaskStatus.FINISHED.getValue().equals(updateInstanceTask.getStatus())
								&&!TaskStatus.SKIP.getValue().equals(updateInstanceTask.getStatus())
								&&!TaskStatus.WITHDRAW.getValue().equals(updateInstanceTask.getStatus())){
							if(ACStatus.RUNNING.getValue().equals(oldRuntimeAcStatus)){
								updateInstanceTask.setStatus(TaskStatus.NOT_RUNNING.getValue());
								deleteMsgIdList.add(updateInstanceTask.getMsgId());
							}

						}*/
						//如果任务是运行状态则撤销任务和待办消息，否则保持原来状态
						if(!TaskStatus.RUNNING.getValue().equals(updateInstanceTask.getStatus())){
							batchUpdateTaskList.add(updateInstanceTask);
							deleteInstanceTaskIdSet.remove(taskId);
						}
					}else{
						batchUpdateTaskList.add(updateInstanceTask);
						deleteInstanceTaskIdSet.remove(taskId);
					}

				}else{
					boolean newTaskFlag = (ApproverStatus.RUNNING.equals(groupStatus)&&PostStatus.RUNNING.getValue().equals(postStatus)&&acId.equals(currentAcId))||acId.equals(currentAcId);
					if(newTaskFlag){
						newInstanceTaskMap.put("delflag",false);
						newInstanceTaskMap.put("status",TaskStatus.NOT_RUNNING.getValue());
						String newTaskJson = JacksonUtils.toJson(newInstanceTaskMap);
						InstanceTask newInstanceTask = JacksonUtils.fromJson(newTaskJson,InstanceTask.class);
						newInstanceTask.setType(TaskType.APPROVER.getValue());
						if(acId.equals(currentAcId)){
							if(!TaskStatus.FINISHED.getValue().equals(newInstanceTask.getStatus())
									&&!TaskStatus.SKIP.getValue().equals(newInstanceTask.getStatus())
									&&!TaskStatus.WITHDRAW.getValue().equals(newInstanceTask.getStatus())){
								newInstanceTask.setStatus(TaskStatus.RUNNING.getValue());
								//sendMsgTaskMap.put(taskId,newInstanceTask);
								SysNoticeMsg msg = newModifyInstanceMsg(instance,newInstanceTask);
								saveBatchMsgList.add(msg);
							}
						}
						batchSaveTaskList.add(newInstanceTask);
					}

				}
			}

		}

		instancePostDao.updateBatch(batchUpdatePostList);
		instancePostDao.saveBatch(batchSavePostList);
		if(CollectionUtils.isNotEmpty(deleteInstancePostIdSet)){
			String deletePostIdListJson = JacksonUtils.toJson(deleteInstancePostIdSet);
			List<String> deletePostIdList = JacksonUtils.fromJson(deletePostIdListJson,List.class,String.class);
			instancePostDao.deleteAllObjectByIds(deletePostIdList);
		}


		instanceGroupService.updateBatch(batchUpdateGroupList);
		instanceGroupService.saveBatch(batchSaveGroupList);
		if(CollectionUtils.isNotEmpty(deleteInstanceGroupIdSet)){
			String deleteGroupIdListJson = JacksonUtils.toJson(deleteInstanceGroupIdSet);
			List<String> deleteGroupIdList = JacksonUtils.fromJson(deleteGroupIdListJson,List.class,String.class);
			instanceGroupService.deleteAllObjectByIds(deleteGroupIdList);
		}


		instanceTaskService.updateBatch(batchUpdateTaskList);
		instanceTaskService.saveBatch(batchSaveTaskList);
		if(CollectionUtils.isNotEmpty(deleteInstanceTaskIdSet)){
			for(String taskId:deleteInstanceTaskIdSet){
				Map<String,Object> taskMap  = oldInstanceTaskMap.get(taskId);
				String msgId = (String) taskMap.get("msgId");
				deleteMsgIdList.add(msgId);

			}
			String deleteTaskIdListJson = JacksonUtils.toJson(deleteInstanceTaskIdSet);
			List<String> deleteTaskIdList = JacksonUtils.fromJson(deleteTaskIdListJson,List.class,String.class);
			instanceTaskService.deleteAllObjectByIds(deleteTaskIdList);
		}

		//处理消息
		if(CollectionUtils.isNotEmpty(saveBatchMsgList)){
			msgService.saveBatch(saveBatchMsgList);
		}

		//删除消息
		if(CollectionUtils.isNotEmpty(deleteMsgIdList)){
			msgService.deleteAllObjectByIds(deleteMsgIdList);
		}

		Map<String,Object> result = new HashMap<String,Object>();
		result.put("deleteMsgIdList",deleteMsgIdList);
		result.put("sendMsgTaskMap",sendMsgTaskMap);
		return result;
	}

	//处理抄送人
	private List<InstanceCs> batchProcessModifyInstanceCs(String instanceId,List<Map<String,Object>> instanceCsList) throws Exception{
		Map<String ,Object> csParam = new HashMap<String,Object>();
		csParam.put("fiId",instanceId);
		List<InstanceCs> instanceAccessibleList = instanceCsService.queryList(csParam);
		List<String> deleteCsIdList = new ArrayList<String>();
		for (InstanceCs instanceCs:instanceAccessibleList) {
			deleteCsIdList.add(instanceCs.getId());
		}
		if(CollectionUtils.isNotEmpty(deleteCsIdList)){
			instanceCsService.deleteAllObjectByIds(deleteCsIdList);
		}


		List<InstanceCs> batchSaveInstancesList = new ArrayList<InstanceCs>();
		for (Map<String,Object> instanceCs:instanceCsList) {
			String csJson = JacksonUtils.toJson(instanceCs);
			InstanceCs newInstanceCs = JacksonUtils.fromJson(csJson,InstanceCs.class);
			newInstanceCs.setDelflag(false);
			newInstanceCs.setFiId(instanceId);
			if(org.apache.commons.lang.StringUtils.isBlank(newInstanceCs.getId())){
				newInstanceCs.setId(IDGenerator.getUUID());
			}

			batchSaveInstancesList.add(newInstanceCs);
		}
		instanceCsService.saveBatch(batchSaveInstancesList);


		return batchSaveInstancesList;
	}

	//创建新消息
	private SysNoticeMsg newModifyInstanceMsg(Instance instance,InstanceTask instanceTask){
		//String taskId = taskEntry.getKey();
		//InstanceTask instanceTask = taskEntry.getValue();
		String mobileUrl = "mobile/approve/approve_detail.html";

		MobileParam mobileParamBean = new MobileParam();
		mobileParamBean.setInstanceId(instance.getId());
		mobileParamBean.setBusinessId(instance.getBusinessId());
		mobileParamBean.setAppId(instance.getAppId());
		mobileParamBean.setTypeCode(instanceTask.getOperationCode());
		mobileParamBean.setTaskId(instanceTask.getId());
		mobileParamBean.setApproveRole(instanceTask.getType());

		String msgType = "DB";
		String url = "flow/runtime/approve/flow.html"
				+ "?instanceId=" + instance.getId()
				+ "&taskId=" + instanceTask.getId()
				+ "&time=" + new Date().getTime();
		UserDto user = new UserDto(instanceTask.getApproverId(), instanceTask.getApproverName());

		SysNoticeMsg message = msgService.newFlowMsg(user,"DB",instance.getName(),url,mobileUrl,JacksonUtils.toJson(mobileParamBean));
		//batchSaveMsgList.add(message);
		instanceTask.setMsgId(message.getId());

		return message;

	}
	//处理消息
	private Map<String,Object> modifyInstanceSendMsg(Instance instance,Map<String,InstanceTask> sendMsgTaskMap,List<String> deleteMsgIdList) throws Exception{
		List<SysNoticeMsg> batchSaveMsgList = new ArrayList<SysNoticeMsg>();
		List<InstanceTask> batchUpdateInstanceTaskList = new ArrayList<InstanceTask>();
		Map<String,Object> taskIdMsgMap = new HashMap<String,Object>();
		if(sendMsgTaskMap.size()>0){
			for (Map.Entry<String,InstanceTask> taskEntry:sendMsgTaskMap.entrySet()) {
				String taskId = taskEntry.getKey();
				InstanceTask instanceTask = taskEntry.getValue();
				String mobileUrl = "mobile/approve/approve_detail.html";

				MobileParam mobileParamBean = new MobileParam();
				mobileParamBean.setInstanceId(instance.getId());
				mobileParamBean.setBusinessId(instance.getBusinessId());
				mobileParamBean.setAppId(instance.getAppId());
				mobileParamBean.setTypeCode(instanceTask.getOperationCode());
				mobileParamBean.setTaskId(instanceTask.getId());
				mobileParamBean.setApproveRole(instanceTask.getType());

				String msgType = "DB";
				String url = "flow/runtime/approve/flow.html"
						+ "?instanceId=" + instance.getId()
						+ "&taskId=" + instanceTask.getId()
						+ "&time=" + new Date().getTime();
				UserDto user = new UserDto(instanceTask.getApproverId(), instanceTask.getApproverName());

				SysNoticeMsg message = msgService.newFlowMsg(user,"DB",instance.getName(),url,mobileUrl,JacksonUtils.toJson(mobileParamBean));
				batchSaveMsgList.add(message);
				instanceTask.setMsgId(message.getId());

				batchUpdateInstanceTaskList.add(instanceTask);
				taskIdMsgMap.put(taskId,message.getId());

				log.info("待办消息成功：" + message);
			}

			/*if(CollectionUtils.isNotEmpty(batchSaveMsgList)){
				msgService.saveBatch(batchSaveMsgList);
			}
			if(CollectionUtils.isNotEmpty(batchUpdateInstanceTaskList)){
				instanceTaskService.updateBatch(batchUpdateInstanceTaskList);
			}*/

		}

		/*if(CollectionUtils.isNotEmpty(deleteMsgIdList)){
			msgService.deleteAllObjectByIds(deleteMsgIdList);
		}*/
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("taskIdMsgMap",taskIdMsgMap);
		result.put("batchSaveMsgList",batchSaveMsgList);
		return result;
	}
	/**
	 *
	 * 发起人撤回流程审批记录&管理员作废流程记录 yangning
	 * @param instanceUnit
	 * @param type 0 发起人撤回  1管理员作废
	 * @throws Exception
	 */
	@Override
	public void addDrawFlowNode(InstanceUnit instanceUnit,int type ) throws Exception {

		InstancePost post = null;
		InstancePost startPost = null;
		/************************
		 * 新增岗位信息
		 ************************/
		for (ACUnit acUnit : instanceUnit.getAcList()) {
			if (CollectionUtils.isEmpty(acUnit.getPosts())) {
				continue;
			}
			List temp = acUnit.getPosts();
			for (PostUnit postUnit : acUnit.getPosts()) {

				if ("1".equals(postUnit.getPostStatus())) {
					startPost =  addDrawFlowPost(postUnit);
					break;
				} else if(!"3".equals(postUnit.getPostStatus())){
					break;
				}
				post =  addDrawFlowPost(postUnit);
			}
		}

		if(null == post){
			post = startPost;
		}
		instancePostDao.save(post);
		/************************************
		 * 新增岗位对应审批人
		 ***********************************/

		InstanceGroup instanceGroup = addDrawFlowGroup(post,type);


		instanceGroupService.save(instanceGroup);

		/***************************************
		 * 新增审批记录
		 ***************************************/

		InstanceTask task = addDrawFlowTask(instanceGroup,type);
		instanceTaskService.save(task);


	}

	private InstancePost addDrawFlowPost(PostUnit postUnit){
		InstancePost post = new InstancePost();
		post.setId(IDGenerator.getUUID());
		post.setAcId(postUnit.getAcId());
		post.setActivateDate(postUnit.getStartTime());
		post.setLeftPerson(postUnit.getLeftPerson());
		post.setPostId(postUnit.getPostId());
		post.setPostName(postUnit.getPostName());
		post.setPx((long) postUnit.getPostSeq());
		post.setStatus(postUnit.getPostStatus());
		return post;
	}
	private InstanceGroup addDrawFlowGroup(InstancePost post ,int type ){
		InstanceGroup instanceGroup = new InstanceGroup();
		instanceGroup.setAcId(post.getAcId());
		instanceGroup.setAcPostId(post.getId());
		instanceGroup.setPostId(post.getPostId());
		instanceGroup.setPostName(post.getPostName());
		//为空会自动跳过
		instanceGroup.setParticipantId("1");
		if(0 == type){
			instanceGroup.setParticipantName("发起人");
		}else{
			instanceGroup.setParticipantName("管理员");
		}

		instanceGroup.setPx(post.getPx() + 1);
		instanceGroup.setSource("1");// 1-模板; 2-加签
		instanceGroup.setDisable(false);
		instanceGroup.setId(IDGenerator.getUUID());
		instanceGroup.setStatus(ApproverStatus.FINISHED.getValue());
		return instanceGroup;
	}

	/**
	 *
	 *
	 * @param group
	 * @param type  0发起人撤回 1管理员作废
	 * @return
	 */
	private InstanceTask addDrawFlowTask(InstanceGroup group,int type){
		InstanceTask task = new InstanceTask();
		task.setId(IDGenerator.getUUID());
		task.setGroupId(group.getId());
		task.setType(TaskType.APPROVER.getValue());
		task.setActivateDate(new Timestamp(System.currentTimeMillis()));
		task.setStatus(TaskStatus.FINISHED.getValue());
		//为空会自动跳过
		task.setApproverId("1");
		//task.setApproverName("管理员");
		String userNote = "";
		if(type == 1){
            task.setApproverName("管理员");
			userNote = "管理员作废！";
		}else{
            task.setApproverName("发起人");
            userNote = "发起人撤回！";
        }

		task.setUserNote(userNote);

		return task;
	}

	@Override
	public Map<String, Object> scanOverdueAcTask()  {
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String,Object>> overdueAcTaskList = instanceDao.queryOverdueAcList();
		List<SysNoticeMsg> messages = new ArrayList<SysNoticeMsg>();
		//未执行的打回发起人实例id集合
		Set<String> backInstanceIdSet = new HashSet<String>();
		//未执行的自动通过实例id集合
		Set<String> autoInstanceIdSet = new HashSet<String>();
		//未执行的自动通过实例任务id集合
		Set<String> autoInstanceTaskIdSet = new HashSet<>();
		for(Map<String,Object> overdueAcMap : overdueAcTaskList) {
			String overdueHandle = (String) overdueAcMap.get("overdueHandle");
			String instanceId = (String) overdueAcMap.get("instanceId");
			String taskId = (String) overdueAcMap.get("taskId");
			if(OverdueAcHandle.BACKTO_STARTER.getValue().equals(overdueHandle)){
				backInstanceIdSet.add(instanceId);
			}

			if(OverdueAcHandle.AUTO_PASS.getValue().equals(overdueHandle)){
				autoInstanceIdSet.add(instanceId);
				autoInstanceTaskIdSet.add(taskId);
			}
		}

		//已执行的打回发起人实例id集合
		Set<String> backStartInstanceIdSet = new HashSet<String>();
		//已执行的自动通过实例id集合
		Set<String> autoPassInstanceIdSet = new HashSet<String>();
		for(Map<String,Object> overdueAcMap : overdueAcTaskList) {
			String overdueHandle = (String) overdueAcMap.get("overdueHandle");
			String instanceId = (String) overdueAcMap.get("instanceId");
			String instanceName = (String) overdueAcMap.get("instanceName");
			String taskId = (String) overdueAcMap.get("taskId");
			String participantId = (String) overdueAcMap.get("participantId");
			//逾期发送待阅,需要先检查逾期打回及逾期自动通过并行节点是否存在，如果存在就不需要发送待阅
			//逾期自动审批通过，需要先检查逾期打回并行节点是否存在，如果存在就不需要自动审批通过
			//逾期自动通过是不能忽略逾期消息,非同一个任务的情况
			if(OverdueAcHandle.NOTICE_APPROVER.getValue().equals(overdueHandle)&&!autoInstanceTaskIdSet.contains(taskId)&&!backInstanceIdSet.contains(instanceId)) {//逾期发送待阅 &&!autoInstanceIdSet.contains(instanceId)
				SysNoticeMsg msg = newOverdueMessage(overdueAcMap);
				messages.add(msg);

			} else if(OverdueAcHandle.BACKTO_STARTER.getValue().equals(overdueHandle)&&!backStartInstanceIdSet.contains(instanceId)) {//逾期打回发起人
				try {
					Operation operation = new OperationFactory().newOperation(OperationType.RETURN.getCode(),this);
					InstanceUnit instanceUnit = this.getInstanceUnit(instanceId);
					List<ACUnit> acList = instanceUnit.getAcList();
					String returnApprover = null;
					String returnApproverName = null;
					for (ACUnit acUnit:acList) {
						if("1".equals(acUnit.getAcType())){
							String acId = acUnit.getAcId();
							List<PostUnit> postUnitList = acUnit.getPosts();
							PostUnit postUnit = postUnitList.get(0);
							List<ApproverUnit> approvers = postUnit.getApprovers();
							String userId = approvers.get(0).getApproverId();
							returnApprover = acId+"."+userId;
							returnApproverName = approvers.get(0).getApproverName();
							break;
						}
					}
					ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
					approvalDto.setTaskId(taskId);
					approvalDto.setReturnApprover(returnApprover);
					approvalDto.setReturnApproverName(returnApproverName);
					approvalDto.setCurrentSubmitUserId(participantId);
					approvalDto.setUserNote("逾期自动打回！");
					approvalDto.setOperationName("打回");
					operation.action(instanceUnit, approvalDto);
					log.info("流程逾期自动打回成功！==> 流程名称："+instanceName+"流程ID："+instanceId);

					backStartInstanceIdSet.add(instanceId);
				} catch (Exception e) {
					log.error("流程逾期自动打回失败！==> 流程名称："+instanceName+"流程ID："+instanceId,e);

				}

			} else if(OverdueAcHandle.AUTO_PASS.getValue().equals(overdueHandle)&&!backInstanceIdSet.contains(instanceId)){//逾期自动审批通过  &&!autoPassInstanceIdSet.contains(instanceId)
				String acId = (String) overdueAcMap.get("acId");
				try {
					List<ApprovalList> approvalList = queryApprovalList(instanceId, null);
					for (ApprovalList approval:approvalList) {
						String approverAcId = approval.getAcId();
						String approverTaskId = approval.getTaskId();
						String approverAcStatus = approval.getAcStatus();
						String approverPostStatus = approval.getPostStatus();
						String approverGroupStatus = approval.getApproverStatus();
						if(ACStatus.RUNNING.getValue().equals(approverAcStatus)&&
								(PostStatus.NOT_RUNNING.getValue().equals(approverPostStatus)||ApproverStatus.NOT_RUNNING.getValue().equals(approverGroupStatus))&&
								approverAcId.equals(acId)){
							approval.setAutoPass(1);
							approval.setTaskComments("逾期自动通过！");
						}
					}
					Instance instance = this.getObjectById(instanceId);
					InstanceUnit instanceUnit = this.translate(instance, approvalList);

					Operation operation = new OperationFactory().newOperation(OperationType.AGREE.getCode(), this);
					ApprovalSubmitDto approvalDto = new ApprovalSubmitDto();
					String currentTaskId = taskId;
					approvalDto.setTaskId(currentTaskId);
					approvalDto.setCurrentSubmitUserId(participantId);
					approvalDto.setUserNote("逾期自动通过！");
					approvalDto.setOperationName("通过");
					approvalDto.setOperationType(OperationType.AGREE.getCode());
					operation.action(instanceUnit, approvalDto);
					autoPassInstanceIdSet.add(instanceId);
					log.info("流程逾期自动通过成功！==> 流程名称："+instanceName+"流程ID："+instanceId);

					//记录流转日志
					//this.saveTransition(instanceId, "管理员", "逾期自动通过");
				} catch (Exception e) {
					log.error("流程逾期自动通过失败！==> 流程名称："+instanceName+"流程ID："+instanceId,e);
				}
			}
		}

		try {
			msgService.batchSaveAndNotifyOthers(messages);
		} catch (Exception e) {
			//throw new FlowException("流程逾期扫描定时任务中发送消息失败！", e);
			log.error("流程逾期扫描定时任务中发送消息失败！",e);
		}

		return result;
	}

	/**
	 * 创建逾期消息
	 * @param overdueAcMap
	 * @return
	 */
	private SysNoticeMsg newOverdueMessage(Map<String,Object> overdueAcMap) {
		String instanceId = (String) overdueAcMap.get("instanceId");
		String instanceName = (String) overdueAcMap.get("instanceName");
		String acName = (String) overdueAcMap.get("acName");
		String businessId = (String) overdueAcMap.get("businessId");
		Integer duration = (Integer) overdueAcMap.get("duration");
		String msgTitle = "流程【" + instanceName + "】中的环节【"
				+ acName + "】逾期" + duration  + "小时，请及时处理！";
		String msgUrl = "/flow/runtime/approve/flow.html?instanceId=" + instanceId;
		String receiverId = (String) overdueAcMap.get("participantId");
		String receiverName = (String) overdueAcMap.get("participantName");
		if(StringUtils.isEmpty(receiverId)) {
			return null;
		}
		String mobileUrl = "mobile/approve/approve_detail.html";
		MobileParam mobileParamBean = new MobileParam();
		mobileParamBean.setInstanceId(instanceId);
		mobileParamBean.setBusinessId(businessId);
		UserDto user = new UserDto(receiverId, receiverName);
		SysNoticeMsg msg = this.msgService.newFlowMsg(user, "DY", msgTitle, msgUrl, mobileUrl, JacksonUtils.toJson(mobileParamBean));

		return msg;
	}

}