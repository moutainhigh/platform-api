package com.xinleju.platform.flow.service.impl;

import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xinleju.platform.flow.utils.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.xinleju.platform.base.annotation.Description;
import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanRelationInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;
import com.xinleju.platform.flow.dao.FlDao;
import com.xinleju.platform.flow.dto.AcDto;
import com.xinleju.platform.flow.dto.ApprovalList;
import com.xinleju.platform.flow.dto.BusinessObjectDto;
import com.xinleju.platform.flow.dto.BusinessObjectVariableDto;
import com.xinleju.platform.flow.dto.FlDto;
import com.xinleju.platform.flow.dto.InstanceDto;
import com.xinleju.platform.flow.dto.InstanceVariableDto;
import com.xinleju.platform.flow.dto.MobileFormDto;
import com.xinleju.platform.flow.dto.NodeDto;
import com.xinleju.platform.flow.dto.ParticipantDto;
import com.xinleju.platform.flow.dto.PcFormDto;
import com.xinleju.platform.flow.dto.PostDto;
import com.xinleju.platform.flow.dto.StepDto;
import com.xinleju.platform.flow.dto.UserDto;
import com.xinleju.platform.flow.entity.Ac;
import com.xinleju.platform.flow.entity.BusinessObject;
import com.xinleju.platform.flow.entity.Fl;
import com.xinleju.platform.flow.entity.Instance;
import com.xinleju.platform.flow.entity.Participant;
import com.xinleju.platform.flow.entity.Step;
import com.xinleju.platform.flow.enumeration.FlAcType;
import com.xinleju.platform.flow.enumeration.FlStatus;
import com.xinleju.platform.flow.enumeration.InstanceStatus;
import com.xinleju.platform.flow.exception.FlowException;
import com.xinleju.platform.flow.service.AcService;
import com.xinleju.platform.flow.service.BusinessObjectService;
import com.xinleju.platform.flow.service.BusinessObjectVariableService;
import com.xinleju.platform.flow.service.FlService;
import com.xinleju.platform.flow.service.InstanceService;
import com.xinleju.platform.flow.service.ParticipantService;
import com.xinleju.platform.flow.service.StepService;
import com.xinleju.platform.flow.utils.ExpressionUtils;
import com.xinleju.platform.flow.utils.FlowApproverUtils;
import com.xinleju.platform.flow.utils.FlowPathUtils;
import com.xinleju.platform.out.app.log.service.LogOutServiceCustomer;
import com.xinleju.platform.sys.org.dto.FlowAcPostDto;
import com.xinleju.platform.sys.org.dto.FlowPostParticipantDto;
import com.xinleju.platform.sys.org.dto.StandardRoleDto;
import com.xinleju.platform.sys.org.dto.service.OrgnazationDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.service.PostDtoServiceCustomer;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.utils.BrowseTool;

/**
 * @author admin
 * 
 * 
 */
@Service
public class FlServiceImpl extends BaseServiceImpl<String, Fl> implements FlService {
	private static Logger log = Logger.getLogger(FlServiceImpl.class);
	@Autowired
	private FlDao flDao;
	@Autowired
	private AcService acService;
	@Autowired
	private ParticipantService participantService;
	@Autowired
	private StepService stepService;
	@Autowired
	private BusinessObjectService businessObjectService;	
	@Autowired
	private BusinessObjectVariableService businessObjectVariableService;	
	@Autowired
	private InstanceService instanceService;
	@Autowired
	private PostDtoServiceCustomer postDtoServiceCustomer;
	
	@Autowired
	private OrgnazationDtoServiceCustomer orgnazationDtoServiceCustomer;
	
	@Autowired
	private LogOutServiceCustomer logOutServiceCustomer;
	
	@Value("#{configuration['business.test.url']}")
	private String businessTestUrl;
	
	@Autowired
	protected RedisTemplate<String, String> redisTemplate;
	
	@Override
	@Description(instruction = "根据条件获取流程模板列表")
	public Page queryFlList(Map<String, Object> paramater) throws Exception {
		// 按修改时间及版本号降序排列
//		Page page = flDao.queryFlList(paramater);
 		Page page = flDao.queryFlListNew(paramater);

		if(page.getList()==null){
			return page;
		}
		List<FlDto> flDtos_ = JacksonUtils.fromJson(JacksonUtils.toJson(page.getList()), ArrayList.class,FlDto.class);
		//update by gyh 让生效版本在第一
		Set<String> codes = new HashSet<>();
		for (int i = 0; i < flDtos_.size(); i++) {
			String codeStr = flDtos_.get(i).getCode();
			codes.add(codeStr);
		}
		paramater.put("codes", codes);
		List<FlDto> fristFlDtos = flDao.getPublishFls(paramater);
		List<FlDto> flDtos = new ArrayList<>();
		flDtos.addAll(fristFlDtos);
		flDtos.addAll(flDtos_);
		//end

		if (flDtos != null && flDtos.size() > 0) {
//			int end=flDtos.size()>3?3:flDtos.size();
			Map<String, FlDto> flDtoMap = new LinkedHashMap<String, FlDto>();
			for (FlDto flDto : flDtos) {
				String codeStr = flDto.getCode();
				//解析流程模板名称
				String flowTitle = flDto.getFlowTitle();
				List<BusinessObjectVariableDto> variables = flDto.getVariables();
				if(variables != null){
					String vCode = null;
					String vName = null;
					for (int i = 0; i <variables.size() ; i++) {
						BusinessObjectVariableDto variableDto = variables.get(i);
						vCode = "@" + variableDto.getCode() + "@";
						vName = "@" + variableDto.getName() + "@";
						flowTitle = flowTitle.replaceAll(vCode,vName);
						flDto.setFlowTitle(flowTitle);
					}
				}

				if (StringUtils.isNotBlank(codeStr) && flDtoMap.get(codeStr) == null) {
					// 组装模板版本列表
					Map<String, String> vasMap = new LinkedHashMap<String, String>();
//					for (FlDto flDto2 : flDtos) {
					for (int i=0;i<flDtos.size()&&vasMap.size()<3;i++) {
						FlDto flDto2=flDtos.get(i);
						if(codeStr.equals(flDto2.getCode())){
							String idStr = flDto2.getId();
							String vas = flDto2.getVersion() + "-";
							if ("0".endsWith(flDto2.getStatus())) {
								vas += FlStatus.DRAFT.getName();
							} else if ("1".endsWith(flDto2.getStatus())) {
								vas += FlStatus.PUBLISH.getName();
							} else if ("2".endsWith(flDto2.getStatus())) {
								vas += FlStatus.DISABLED.getName();
							}
							vasMap.put(idStr, vas);
						}
					}
					flDto.setVersionAndStatus(JacksonUtils.toJson(vasMap));
					flDtoMap.put(codeStr, flDto);
				}
			}
			
			page.setList(mapSort(flDtoMap));
		}
		
		return page;
	}
	
	public List<FlDto> mapSort(Map<String,FlDto> map){
		 //这里将map.entrySet()转换成list
        List<Map.Entry<String,FlDto>> list = new ArrayList<Map.Entry<String,FlDto>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String,FlDto>>() {
            //升序排序
            public int compare(Entry<String, FlDto> o1,
                    Entry<String, FlDto> o2) {
            	FlDto flDto1 = o1.getValue();
            	FlDto flDto2 = o2.getValue();
                return flDto1.getSort().compareTo(flDto2.getSort());
            }
            
        });
        List<FlDto> flDtoList = new ArrayList<FlDto>();
        for(int i = list.size() - 1; i >=0; i--){ 
        	flDtoList.add(list.get(i).getValue());
        }
		return flDtoList; 
	}
	
	
	@Override
	@Description(instruction = "根据条件分组获取流程模板列表")
	public Page queryFlByGroupList(Map<String, Object> paramater) throws Exception {
		// 按修改时间及版本号降序排列
//		Page page = flDao.queryFlList(paramater);
		Page page = flDao.queryFlByGroupList(paramater);
		List<FlDto> flDtos = JacksonUtils.fromJson(JacksonUtils.toJson(page.getList()), ArrayList.class,FlDto.class);
		if (flDtos != null && flDtos.size() > 0) {
//			int end=flDtos.size()>3?3:flDtos.size();
			Map<String, FlDto> flDtoMap = new LinkedHashMap<String, FlDto>();
			for (FlDto flDto : flDtos) {
				String codeStr = flDto.getCode();
				if (StringUtils.isNotBlank(codeStr) && flDtoMap.get(codeStr) == null) {
					// 组装模板版本列表
					Map<String, String> vasMap = new LinkedHashMap<String, String>();
//					for (FlDto flDto2 : flDtos) {
					for (int i=0,j=0;i<flDtos.size()&&j<3;i++) {
						FlDto flDto2=flDtos.get(i);
						if(codeStr.equals(flDto2.getCode())){
							String idStr = flDto2.getId();
							String vas = flDto2.getVersion() + "-";
							if ("0".endsWith(flDto2.getStatus())) {
								vas += FlStatus.DRAFT.getName();
							} else if ("1".endsWith(flDto2.getStatus())) {
								vas += FlStatus.PUBLISH.getName();
							} else if ("2".endsWith(flDto2.getStatus())) {
								vas += FlStatus.DISABLED.getName();
							}
							vasMap.put(idStr, vas);
							j++;
						}
					}
					flDto.setVersionAndStatus(JacksonUtils.toJson(vasMap));
					flDtoMap.put(codeStr, flDto);
				}
			}
			
			page.setList(new ArrayList<FlDto>(flDtoMap.values()));
		}
		
		return page;
	}
	@Override
	@Description(instruction = "保存流程模板相关信息")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveAll(String userInfo, FlDto flDto) throws Exception {

		//验证业务对象是否有默认模板，如果没有则将当前模板置为默认模板  Begin
		//add by dinggh on 2017/11/03
		Boolean isDefualt = flDto.getIsDefualt();
		if(isDefualt==null||!isDefualt){
			Map<String,Object> isDefualtParam = new HashMap<String,Object>();
			isDefualtParam.put("businessObjectId",flDto.getBusinessObjectId());
			isDefualtParam.put("isDefualt",true);
			isDefualtParam.put("delflag",false);
			List<Fl> flList = flDao.queryList(isDefualtParam);
			if(flList!=null&&flList.size()>0){
				isDefualt = false;
			}else{
				isDefualt = true;
			}
		}
		flDto.setIsDefualt(isDefualt);
		//验证业务对象是否有默认模板，如果没有则将当前模板置为默认模板  End

		// 第1步:保存流程模板的基本信息
		Timestamp currentTimestamp = new Timestamp(new Date().getTime());
	   	flDto.setUpdateDate(currentTimestamp);
		Fl fl = new Fl();
		BeanUtils.copyProperties(flDto, fl);
		String oldCode=flDto.getOldCode();//修改前的code
		if(StringUtils.isBlank(oldCode)){
			oldCode=fl.getCode();			
		}
		Map<String, Object> queryCond = new HashMap<String, Object>();
		queryCond.put("code", oldCode);
		queryCond.put("delflag",false);
		if(flDto == null){
			return;
		}
		String oldFlId = flDto.getId();
		String newFlId = null;
		/**
		 * update by gyh
		 * 暂存：生成草稿新记录，版本号和来源版本号一致
		 * 发布	>生成新版本（新记录），且发布
		 * 		>不生成新版本，直接在版本基础上保存修改，且发布；若由草稿状态直接发布，则草稿直接变发布状态，草稿来源版本那条记录删除
		 * 	begin
		 */
		if("0".equals(flDto.getStatus())){
			//保存最新草稿：若无此版本的草稿，生成草稿新记录，版本号和来源版本号一致
			//如果草稿已存在，直接修改草稿
			queryCond.put("version", flDto.getVersion());
			queryCond.put("status", "0");
			List<Fl> fls = flDao.queryList(queryCond);
			if (fls.size() > 0) {
				newFlId = fls.get(0).getId();
				fl.setId(newFlId);
				fl.setSourceVersion(fl.getVersion());
				flDao.update(fl);
			}else {
				newFlId = IDGenerator.getUUID();
				fl.setId(newFlId);
				fl.setSourceVersion(fl.getVersion());
				flDao.save(fl);
			}
		}else if("1".equals(flDto.getStatus())) {
			//设置之前的生效版本为失效
			queryCond.put("status", "1");
			List<Fl> fls2 = flDao.queryList(queryCond);
			if (fls2.size() > 0) {
				for (Fl fl2 : fls2) {
					if(fl2.getId().equals(oldFlId)){
						int v = fl.getConcurrencyVersion()==null ? 0 : fl.getConcurrencyVersion();
						v++;
						fl.setConcurrencyVersion(v);
					}
					fl2.setStatus("2");
					fl2.setCode(fl.getCode());
				}
			}
			flDao.updateBatch(fls2);

			//生成新版本，且发布
			if(flDto.isIfNewVersion()!=null && flDto.isIfNewVersion()){
				log.debug("\n\n 开始进行flow_activity_Participant_Step_Reader的数据的更新和保存.....");
				String flowId = fl.getId();
				//判断id是否已存在，存在则生成新版本，不存在说明第一次发布
				Fl checkFlow = flDao.getObjectById(flowId);
				if(checkFlow!=null && checkFlow.getCode()!=null){
					newFlId = IDGenerator.getUUID();
					fl.setId(newFlId);
					fl.setVersion(flDto.getNewVersion());
				}
				flDao.save(fl);
			}else{
				//不生成新版本，直接在版本基础上保存修改，且发布；若由草稿状态直接发布，则草稿直接变发布状态，草稿来源版本那条记录删除
				String flowId = fl.getId();
				if("0".equals(flDto.getOldStatus())){
					Long sourceVersion = flDto.getSourceVersion();
					queryCond.remove("status");
					queryCond.put("version",sourceVersion);
					List<Fl> fls3 = flDao.queryList(queryCond);
					List<String> ids = new ArrayList<>();
					if (fls3.size() > 0) {
						for (Fl fl2 : fls3) {
							//数据库中记录的草稿记录
							if(fl2.getId().equals(flowId)){
								continue;
							}
							ids.add(fl2.getId());
						}
					}
					flDao.deletePseudoAllObjectByIds(ids);
				}
				//判断id是否已存在，存在则直接修改，不存在则生成
				Fl checkFlow = flDao.getObjectById(flowId);
				if(checkFlow!=null && checkFlow.getCode()!=null){
				    fl.setConcurrencyVersion(checkFlow.getConcurrencyVersion());
					flDao.update(fl);
				}else{
					flDao.save(fl);
				}

			}

		}

		String acJson = flDto.getAc();
		String stepJson = flDto.getStep();
		String participantJson = flDto.getParticipant();
		if (StringUtils.isNotBlank(newFlId)){
			acJson = acJson.replaceAll(oldFlId,newFlId);
			stepJson = stepJson.replaceAll(oldFlId,newFlId);
			participantJson = participantJson.replaceAll(oldFlId,newFlId);
		}
		//生成新版本，环节和连线也重新生成，避免多次暂存后直接发布新版本重用环节和连线的情况
		if(flDto.isIfNewVersion()!=null && flDto.isIfNewVersion()){
			List<AcDto> aclist = JacksonUtils.fromJson(acJson, ArrayList.class, AcDto.class);
			for (int i = 0; i < aclist.size(); i++) {
				String oldId = aclist.get(i).getId();
				String newId = IDGenerator.getUUID();
				acJson = acJson.replaceAll(oldId,newId);
				stepJson = stepJson.replaceAll(oldId,newId);
			}
		}
		//删除环节旧数据
		Map<String,Object> query = new HashMap<>();
		query.put("flId", fl.getId());
		List<Ac> acs = acService.queryList(query);
		List<String> ids = new ArrayList<>();
		if (acs.size() > 0) {
			for (Ac ac : acs) {
				ids.add(ac.getId());
			}
		}
		if(ids.size()>0){
			acService.deleteAllObjectByIds(ids);
		}
		//删除连线旧数据
		List<Step> steps = stepService.queryList(query);
		List<String> steps_ids = new ArrayList<>();
		if (steps.size() > 0) {
			for (Step step : steps) {
				steps_ids.add(step.getId());
			}
		}
		if(steps_ids.size()>0) {
			stepService.deleteAllObjectByIds(steps_ids);
		}
		//删除审批人旧数据
		List<Participant> reads = participantService.queryList(query);
		List<String> read_ids = new ArrayList<>();
		if (reads.size() > 0) {
			for (Participant read : reads) {
				read_ids.add(read.getId());
			}
		}
		if(read_ids.size()>0) {
			participantService.deleteAllObjectByIds(read_ids);
		}
		//update by gyh end

		// 第2步:批量保存环节信息和参与人信息
		saveAcAndParticipantBatch(currentTimestamp,userInfo,acJson, fl.getId());

		// 第3步:批量保存流程模板环节连线信息
		saveStepBatch(currentTimestamp,userInfo,stepJson);

		// 第4步:批量保存模版的可阅读人的数组信息
		saveReaderBatch(currentTimestamp,userInfo,participantJson);


		
		//记录操作日志
//		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
//		String userJson = JacksonUtils.toJson(userBeanInfo);
//		Map<String, Object> content = new HashMap<String, Object>();
//		content.put("sysCode", "PT");
//		content.put("loginIp", BrowseTool.getClientIP());
//		content.put("loginBrowser", BrowseTool.getUserAgent());
//		content.put("node", flDto);
//		if("0".equals(flDto.getStatus())) {
//			content.put("note", "模板暂存成功");
//		} else {
//			content.put("note", "模板暂存失败");
//		}
//		logOutServiceCustomer.saveOpeLog(userJson, JacksonUtils.toJson(content));
	}

	/**
	 * 批量保存环节信息和参与人信息
	 * 
	 * @param acJson
	 * @throws Exception
	 */
	private void saveAcAndParticipantBatch(Timestamp currentTimestamp,String userInfo, String acJson, String flId) throws Exception{
		List<AcDto> list = JacksonUtils.fromJson(acJson, ArrayList.class, AcDto.class);
		List<Participant> personList = new ArrayList<Participant>();

		for(AcDto acDto : list){
			acDto.setUpdateDate(currentTimestamp);			
			Ac ac = new Ac();
			BeanUtils.copyProperties(acDto, ac);
			String participantJson = acDto.getParticipant();
			String ccPersonJson = acDto.getCcPerson();
			List<Participant> dataList1 =  JacksonUtils.fromJson(participantJson, ArrayList.class, Participant.class);
			if (dataList1 != null) {
				personList.addAll(dataList1);
			}
			List<Participant> dataList2 =  JacksonUtils.fromJson(ccPersonJson, ArrayList.class, Participant.class);
			if (dataList2 != null) {
				personList.addAll(dataList2);
			}
			//此处增加判断Flow是否已经存在,用于处理从失效模板变为发布的模板
			String activityId = ac.getId();
			Ac activity = acService.getObjectById(activityId);
			if(activity !=null && activity.getId()!=null){
				acService.update(ac);
			}else{
				acService.save(ac);
			}
		}
		
		for(Participant participant : personList){
			/*participant.setCreatePersonId(userInfoMap.get("id"));
			participant.setCreatePersonName(userInfoMap.get("name"));*/
			participant.setCreateDate(currentTimestamp);
			/*participant.setUpdatePersonId(userInfoMap.get("id"));
			participant.setUpdatePersonName(userInfoMap.get("name"));*/
			participant.setUpdateDate(currentTimestamp);				
			participant.setId(IDGenerator.getUUID());
			participant.setFlId(flId);
			participantService.save(participant);
		}
	}
	
	/**
	 * 批量保存流程模板环节连线信息
	 * 
	 * @param stepJson
	 * @throws Exception
	 */
	private void saveStepBatch(Timestamp currentTimestamp,String userInfo, String stepJson) throws Exception{
//		List<Step> stepList = JacksonUtils.fromJson(stepJson, ArrayList.class, Step.class);
		//bug#15953当连线信息中包含单引号时JacksonUtils转换报错
		List<Step> stepList = JSONArray.parseArray(stepJson, Step.class);
		for(Step step : stepList){
			step.setId(IDGenerator.getUUID());
			step.setCreateDate(currentTimestamp);
			step.setUpdateDate(currentTimestamp);
			String conditionExpression = step.getConditionExpression();
			if (StringUtils.isNotBlank(conditionExpression)) {
				conditionExpression = conditionExpression.replaceAll("\'", "\\\\'");
			}

			String conditionTranslation = step.getConditionTranslation();
			if (StringUtils.isNotBlank(conditionTranslation)) {
				conditionTranslation = conditionTranslation.replaceAll("\'", "\\\\'");
			}
			step.setConditionExpression(conditionExpression);
			step.setConditionTranslation(conditionTranslation);
			stepService.save(step);
		}
	}
	
	/**
	 * 批量保存模版的可阅读人信息
	 * 
	 * @param readerJson
	 * @throws Exception
	 */
	private void saveReaderBatch(Timestamp currentTimestamp,String userInfo, String readerJson) throws Exception{
		List<Participant> readerList = JacksonUtils.fromJson(readerJson, ArrayList.class, Participant.class);
		for(Participant reader : readerList){
			reader.setCreateDate(currentTimestamp);
			reader.setUpdateDate(currentTimestamp);			
			reader.setId(IDGenerator.getUUID());
			participantService.save(reader);
		}
	}

	@Override
	public FlDto getAll(String paramater) throws Exception {
		FlDto result = null;
		Fl fl = getObjectById(paramater);
		if (fl != null) {

			String flJsonStr = JacksonUtils.toJson(fl);
			result = JacksonUtils.fromJson(flJsonStr, FlDto.class);
			
			Map<String, Object> queryCond = new HashMap<String, Object>();
			queryCond.put("flId", fl.getId());

			//查询业务对象信息
			BusinessObject businessObject = businessObjectService.getObjectById(result.getBusinessObjectId());
			//查询环节信息
			List<Ac> oldAcs = acService.queryList(queryCond);
			List<AcDto> acs = new  ArrayList<AcDto>();
			Map<String, Object> acCond = new HashMap<String, Object>();
			acCond.put("flId", fl.getId());
			for (Ac ac : oldAcs) {
				AcDto acDto = new AcDto();
				BeanUtils.copyProperties(ac, acDto);
				acCond.put("acId", acDto.getId());
				String newAcId = IDGenerator.getUUID();
				//查询环节审批人信息
				acCond.put("type", "1");
				acCond.put("sidx", "sort");
				acCond.put("sord", "asc");
				List<Participant> participants = participantService.queryList(acCond);
				for (Participant participant : participants) {
					participant.setAcId(newAcId);
				}
				acDto.setParticipant(JacksonUtils.toJson(participants));
				//查询环节抄送人信息
				acCond.put("type", "2");
				List<Participant> ccPersons = participantService.queryList(acCond);
				for (Participant participant : ccPersons) {
					participant.setAcId(newAcId);
				}
				acDto.setCcPerson(JacksonUtils.toJson(ccPersons));
				
				acDto.setId(newAcId);
				acs.add(acDto);
			}
			//查询环节连线信息
			
			List<Step> oldSteps = stepService.queryList(queryCond);
			//处理环节连线源节点和目标节点ID为画布上的nodeId
			List<Step> steps = new ArrayList<Step>();
			for (Step step : oldSteps) {
				step.setSourceId(acService.getObjectById(step.getSourceId())!=null?acService.getObjectById(step.getSourceId()).getNodeId():"");
				step.setTargetId(acService.getObjectById(step.getTargetId())!=null?acService.getObjectById(step.getTargetId()).getNodeId():"");
				step.setId(IDGenerator.getUUID());
				steps.add(step);
			}			
			//查询模板参与人信息
			queryCond.put("sidx", "sort");
			queryCond.put("sord", "asc");
			List<Participant> participants = participantService.queryList(queryCond);
			//创建画布XML
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement("mxGraphModel").addElement("root");
			root.addElement("mxCell").addAttribute("id", "0");
			root.addElement("mxCell").addAttribute("id", "1").addAttribute("parent", "0");
			createXMLElement(root, acs, "ac");
			createXMLElement(root, steps, "step");
			
			result.setBusinessObjectName(businessObject.getName());
			result.setAc(JacksonUtils.toJson(acs));
			result.setStep(JacksonUtils.toJson(steps));
			result.setParticipant(JacksonUtils.toJson(participants));
			result.setGraphXml(document.asXML());
		}
		return result;
	}

	@Override
	public FlDto getAllForTemplate(String paramater) throws Exception{
		FlDto result = null;
		Fl fl = getObjectById(paramater);
		if (fl != null) {

			String flJsonStr = JacksonUtils.toJson(fl);
			result = JacksonUtils.fromJson(flJsonStr, FlDto.class);

			Map<String, Object> queryCond = new HashMap<String, Object>();
			queryCond.put("flId", fl.getId());
			queryCond.put("delfalg",0);

			//查询业务对象信息
			BusinessObject businessObject = businessObjectService.getObjectById(result.getBusinessObjectId());

			//查询模板参与人信息
			Map<String,Object> participantCond = new HashMap<String,Object>();
			participantCond.put("flId", fl.getId());
			participantCond.put("delflag", false);
			participantCond.put("sidx", "sort");
			participantCond.put("sord", "asc");

			List<Participant> participants = participantService.queryList(participantCond);

			//查询环节信息
			List<Ac> oldAcs = acService.queryList(queryCond);
			List<AcDto> acs = new  ArrayList<AcDto>();
			Map<String , AcDto> acNewOldMap = new HashMap<String,AcDto>();
			Map<String,String> oldNewAcIdMap = new HashMap<String,String>();
			for (Ac ac : oldAcs) {
				AcDto acDto = new AcDto();
				BeanUtils.copyProperties(ac, acDto);
				String newAcId = IDGenerator.getUUID();
				acDto.setId(newAcId);
				acNewOldMap.put(ac.getId(),acDto);
				oldNewAcIdMap.put(ac.getId(),acDto.getId());
				acs.add(acDto);
			}

			//设置环节审批人&&环节抄送人
			Iterator<Participant> participantIterator = participants.iterator();
			while (participantIterator.hasNext()){
				Participant  participant = participantIterator.next();
				String acId = participant.getAcId();
				String type = participant.getType();
				AcDto acDto = acNewOldMap.get(acId);
				if(acDto!=null){
					String participantJson = acDto.getParticipant();
					String ccPersonJson = acDto.getCcPerson();
					//审批人
					List<Participant> participantList = JacksonUtils.fromJson(participantJson,List.class,Participant.class);
					participantList = participantList==null?new ArrayList<Participant>():participantList;
					//抄送人
					List<Participant> ccPersonList = JacksonUtils.fromJson(ccPersonJson,List.class,Participant.class);
					ccPersonList = ccPersonList==null?new ArrayList<Participant>():ccPersonList;
					if("1".equals(type)&&acDto!=null){//环节审批人
						participant.setAcId(acDto.getId());
						participantList.add(participant);
						acDto.setParticipant(JacksonUtils.toJson(participantList));
						participantIterator.remove();
					}else if("2".equals(type)&&acDto!=null){//环节抄送人
						participant.setAcId(acDto.getId());
						ccPersonList.add(participant);
						acDto.setCcPerson(JacksonUtils.toJson(ccPersonList));
						participantIterator.remove();
					}
				}
			}

			//查询环节连线信息
			List<Step> oldSteps = stepService.queryList(queryCond);
			//处理环节连线源节点和目标节点ID为画布上的nodeId
			List<Step> steps = new ArrayList<Step>();
			for (Step step : oldSteps) {
				///step.setSourceId(acService.getObjectById(step.getSourceId())!=null?acService.getObjectById(step.getSourceId()).getNodeId():"");
				//step.setTargetId(acService.getObjectById(step.getTargetId())!=null?acService.getObjectById(step.getTargetId()).getNodeId():"");
				String sourceId = step.getSourceId();
				String targetId = step.getTargetId();
				if(StringUtils.isNotBlank(sourceId)){
					step.setSourceId(acNewOldMap.get(sourceId).getId());
				}
				if(StringUtils.isNotBlank(targetId)){
					step.setTargetId(acNewOldMap.get(targetId).getId());
				}
				step.setId(IDGenerator.getUUID());
				steps.add(step);
			}

			//创建画布XML
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement("mxGraphModel").addElement("root");
			root.addElement("mxCell").addAttribute("id", "0");
			root.addElement("mxCell").addAttribute("id", "1").addAttribute("parent", "0");
			parseNodeToXMLElement(root, acs, "ac");
			parseNodeToXMLElement(root, steps, "step");

			result.setBusinessObjectName(businessObject.getName());
			result.setAc(JacksonUtils.toJson(acs));
			result.setStep(JacksonUtils.toJson(steps));
			result.setParticipant(JacksonUtils.toJson(participants));
			result.setGraphXml(document.asXML());
			result.setOldNewAcIdMapJson(JacksonUtils.toJson(oldNewAcIdMap));
		}
		return result;

	}

	/**
	 * 将流程图元数据解析为可展示的XML
	 * @param root
	 * @param elements
	 * @param type
	 * @throws Exception
	 */
	private void parseNodeToXMLElement(Element root, Object elements, String type) throws Exception {
		if (root != null && elements != null && StringUtils.isNotBlank(type)) {
			if ("ac".equals(type)) {
				List<AcDto> acs = (List<AcDto>) elements;
				for (AcDto ac : acs) {
					Element cellElem = root.addElement("mxCell")
							.addAttribute("vertex", "1")
							.addAttribute("parent", "1")
							.addAttribute("id", ac.getId())
							.addAttribute("value",ac.getName())
							.addAttribute("label", ac.getName())
							.addAttribute("description", ac.getName());

					String acType = ac.getAcType();
					String approvalPersonIsNull = ac.getApprovalPersonIsNull();
					String approveStrategy = ac.getApproveStrategy();
					String approveTypeId = ac.getApproveTypeId();
					String code = ac.getCode();
					Boolean isAddLabel = ac.getIsAddLabel();
					Boolean isStart = ac.getIsStart();
					String name = ac.getName();
					String nodeId = ac.getNodeId();
					String overdueHandle = ac.getOverdueHandle();
					Integer overdueTime = ac.getOverdueTime();
					String personRepeatIsSkipped = ac.getPersonRepeatIsSkipped();
					String postIsNull = ac.getPostIsNull();
					String postMultiPerson = ac.getPostMultiPerson();
					Map<String,Object> extraMap = new HashMap<String,Object>();
					extraMap.put("acType",acType);
					extraMap.put("approvalPersonIsNull",approvalPersonIsNull);
					extraMap.put("approveStrategy",approveStrategy);
					extraMap.put("approveTypeId",approveTypeId);
					extraMap.put("code",code);
					extraMap.put("isAddLabel",isAddLabel);
					extraMap.put("isStart",isStart);
					extraMap.put("name",name);
					extraMap.put("nodeId",nodeId);
					extraMap.put("overdueHandle",overdueHandle);
					extraMap.put("overdueTime",overdueTime);
					extraMap.put("personRepeatIsSkipped",personRepeatIsSkipped);
					extraMap.put("postIsNull",postIsNull);
					extraMap.put("postMultiPerson",postMultiPerson);
					String extra = JacksonUtils.toJson(extraMap);
					cellElem.addAttribute("extra",extra);

					//环节审批人
					String participant = ac.getParticipant();
					if(StringUtils.isNotBlank(participant)){
						cellElem.addAttribute("participant",participant);
					}
					//环节抄送人
					String ccPerson = ac.getCcPerson();
					if(StringUtils.isNotBlank(ccPerson)){
						cellElem.addAttribute("ccPerson",ccPerson);
					}


					if (FlAcType.START.getAcType().equals(ac.getAcType())){
						cellElem.addAttribute("style", FlAcType.START.getNodeType())
								.addAttribute("nodeType", FlAcType.START.getNodeType());
					}else if (FlAcType.TASK.getAcType().equals(ac.getAcType())) {
						cellElem.addAttribute("style", FlAcType.TASK.getNodeType())
								.addAttribute("nodeType", FlAcType.TASK.getNodeType());
					} else if (FlAcType.FORK.getAcType().equals(ac.getAcType())) {
						cellElem.addAttribute("style", FlAcType.FORK.getNodeType())
								.addAttribute("nodeType", FlAcType.FORK.getNodeType());
					} else if (FlAcType.JOIN.getAcType().equals(ac.getAcType())) {
						cellElem.addAttribute("style", FlAcType.JOIN.getNodeType())
								.addAttribute("nodeType", FlAcType.JOIN.getNodeType());
					} else if (FlAcType.END.getAcType().equals(ac.getAcType())){
						cellElem.addAttribute("style", FlAcType.END.getNodeType())
								.addAttribute("nodeType", FlAcType.END.getNodeType());
					} else if (FlAcType.CC.getAcType().equals(ac.getAcType())){
						cellElem.addAttribute("style", FlAcType.CC.getNodeType())
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
				List<Step> steps = (List<Step>) elements;
				for (Step step : steps) {
					/*Element parentElem = root.addElement(FlAcType.CONNECTOR.getNodeName())
							.addAttribute("id", step.getNodeId())
							.addAttribute("label", StringEscapeUtils.unescapeHtml (step.getName()))
							.addAttribute("nodeType", FlAcType.CONNECTOR.getNodeType());*/
					Element cellElem = root.addElement("mxCell")
							.addAttribute("style", "defaultEdge")
							.addAttribute("edge", "1")
							.addAttribute("parent", "1")
							.addAttribute("source", step.getSourceId())
							.addAttribute("target", step.getTargetId())
							.addAttribute("id", step.getId())
							.addAttribute("value", StringEscapeUtils.unescapeHtml (step.getName()));
					String conditionExpression = step.getConditionExpression();
					String conditionTranslation = step.getConditionTranslation();
					if (StringUtils.isNotBlank(conditionExpression)){
						cellElem.addAttribute("conditionExpression", conditionExpression);
					}

					if (StringUtils.isNotBlank(conditionTranslation)){
						cellElem.addAttribute("conditionTranslation", conditionTranslation);
					}
					cellElem.addElement("mxGeometry")
							.addAttribute("relative", "1")
							.addAttribute("as", "geometry");
				}
			}
		}
	}
	/**
	 * 创建画布XML节点
	 * 
	 * @param root     根节点
	 * @param elements 结点元数据
	 * @param type     节点类型 (ac-环节,step-环节连线)
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void createXMLElement(Element root, Object elements, String type) throws Exception {
		if (root != null && elements != null && StringUtils.isNotBlank(type)) {
			if ("ac".equals(type)) {
				List<AcDto> acs = (List<AcDto>) elements;
				for (AcDto ac : acs) {
					Element parentElem = null;
					if (FlAcType.START.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.START.getNodeName())
								.addAttribute("id", ac.getNodeId())
								.addAttribute("label", ac.getName())
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.START.getNodeType());
					} else if (FlAcType.TASK.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.TASK.getNodeName())
								.addAttribute("id", ac.getNodeId())						
								.addAttribute("label", ac.getName())
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.TASK.getNodeType());
					} else if (FlAcType.CC.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.CC.getNodeName())
								.addAttribute("id", ac.getNodeId())						
								.addAttribute("label", ac.getName())
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.CC.getNodeType());
					} else if (FlAcType.FORK.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.FORK.getNodeName())
								.addAttribute("id", ac.getNodeId())						
								.addAttribute("label", ac.getName())
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.FORK.getNodeType());
					} else if (FlAcType.JOIN.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.JOIN.getNodeName())
								.addAttribute("id", ac.getNodeId())							
								.addAttribute("label", ac.getName())
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.JOIN.getNodeType());
					} else if (FlAcType.END.getAcType().equals(ac.getAcType())) {
						parentElem = root.addElement(FlAcType.END.getNodeName())
								.addAttribute("id", ac.getNodeId())							
								.addAttribute("label", ac.getName())
								.addAttribute("description", ac.getName())
								.addAttribute("nodeType", FlAcType.END.getNodeType());
					}else{
						//如果没有类型默认保存为task
						parentElem = root.addElement(FlAcType.TASK.getNodeName())
								.addAttribute("id", ac.getNodeId())						
								.addAttribute("label", ac.getName())
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
						cellElem.addAttribute("style", "symbol;image=images/symbols/fork.png");
					} else if (FlAcType.JOIN.getAcType().equals(ac.getAcType())) {
						cellElem.addAttribute("style", "symbol;image=images/symbols/merge.png");
					} else if (FlAcType.END.getAcType().equals(ac.getAcType())){
						cellElem.addAttribute("style", FlAcType.END.getNodeType());
					} else if (FlAcType.CC.getAcType().equals(ac.getAcType())){
						cellElem.addAttribute("style", "symbol;image=images/symbols/message.png");
					}
					cellElem.addElement("mxGeometry")
							.addAttribute("x", String.valueOf(ac.getX()))
							.addAttribute("y", String.valueOf(ac.getY()))
							.addAttribute("width", String.valueOf(ac.getWidth()))
							.addAttribute("height", String.valueOf(ac.getHeight()))
							.addAttribute("as", "geometry");

				}

			} else if ("step".equals(type)) {
				List<Step> steps = (List<Step>) elements;
				for (Step step : steps) {
					Element parentElem = root.addElement(FlAcType.CONNECTOR.getNodeName())
							.addAttribute("id", step.getNodeId())
							.addAttribute("label", StringEscapeUtils.unescapeHtml (step.getName()))
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

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String start(String businessObjectCode, String flCode, String businessId) throws FlowException {
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userInfo = JacksonUtils.toJson(securityUserBeanInfo);
		
		/**
		 * 第1步：判断流程模板是否存在
		 */
		Fl fl = null;
		BusinessObjectDto businessObjectDto = null;
		try {
			if (StringUtils.isNotBlank(businessObjectCode) && !StringUtils.isNotBlank(flCode)) {
				// 获取业务对象下默认流程模板
				fl = queryDefaultFlow(businessObjectCode);
			} else {
				fl = queryReadyFlow(flCode);
			}

			if (fl == null) {
				throw new FlowException("未找到对应的模板：流程模板编码【 " + flCode + "】，业务对象编码：【" + businessObjectCode + "】");
			} else {
				if (StringUtils.isNotBlank(businessObjectCode) && !StringUtils.isNotBlank(flCode)) {
					fl = queryDefaultFlow(businessObjectCode);
					businessObjectDto = businessObjectService.getObjectByCode(businessObjectCode);
				} else {
					fl = queryReadyFlow(flCode);
					businessObjectDto = businessObjectService.getObjectByFlCode(flCode);
				}
			}
			fl = getObjectById(fl.getId());
		} catch (Exception e1) {
			throw new FlowException(e1.getMessage(), e1);
		}
		/**
		 * 第2步：校验流程发起条件,不符合条件时抛出异常
		 */
		String instanceIdBeforeReturn = validateFlowStartCondition(fl, businessId);
		
		/**
		 * 第3步：获取业务变量值
		 */
		Map<String, Object> businessVariableValue = new HashMap<String, Object>();
		List<Object> pcFormValue = new ArrayList<Object>();
		List<Object> phoneFormValue = new ArrayList<Object>();
		List<Object> phoneAttachment = new ArrayList<Object>();
		if(StringUtils.isEmpty(businessObjectDto.getApproveClass())) {
			throw new FlowException("业务对象【" + businessObjectDto.getName() + "】中的查询业务变量URL为空！");
		}
		getBusinessVariableValue(businessObjectDto.getApproveClass(), businessId, 
				businessObjectDto.getCode(), flCode, businessVariableValue, 
				pcFormValue, phoneFormValue, phoneAttachment);
		log.info("调用业务系统变量查询接口返回数据：" + businessVariableValue);
		businessVariableValue.put("flow_fl_name", fl.getName());
		/**
		 * 第4步：解析流程审批路径
		 */
		List<AcDto> acDtos = new ArrayList<AcDto>();
		try {
			List<NodeDto>	nodeDtos = parsePassFlowNodeDto(fl.getId(), businessVariableValue);
			for (NodeDto nodeDto : nodeDtos) {
				AcDto acDto = nodeDto.getCurrentAcDto();
				List<String> previousNodes = new ArrayList<String>();
				for (AcDto acDto2 : nodeDto.getPreviousAcDtos()) {
					previousNodes.add(acDto2.getNodeId());
				}
				acDto.setPreviousAcDtos(previousNodes);
				List<String> nextNodes = new ArrayList<String>();
				for (AcDto acDto3 : nodeDto.getNextNodeDtos()) {
					nextNodes.add(acDto3.getNodeId());
				}					
				acDto.setNextNodeDtos(nextNodes);
				acDtos.add(acDto);
			}
		} catch (Exception e) {
			throw new FlowException("解析流程审批路径失败！", e);
		}
		/**
		 * 第5步：获取环节参与人相关信息（审批人、抄送人、可阅人）
		 */
		String accessiblePosts = ""; // 可阅人岗位信息
		List<AcDto> accessibleAcDtos = new ArrayList<AcDto>();
		List<ParticipantDto> csList = new ArrayList<ParticipantDto>(); // 抄送人
		List<ParticipantDto> participantList = new ArrayList<ParticipantDto>();// 审批人

		try {
			getParticipants(fl.getId(), acDtos, accessibleAcDtos, csList, participantList);
		} catch (Exception e) {
			throw new FlowException("获取环节审批人、抄送人、可阅人失败！", e);
		}
		
		//给开始节点设置人员,让计算审批人与抄送人的逻辑不同
		AcDto startAc = acDtos.get(0);
		String starter = (String) businessVariableValue.get("start_user_id");
		startAc.setParticipant(starter);
		
		/**
		 * 第6步：获取审批人岗位信息
		 */
		FlowApproverUtils.setPostDtoServiceCustomer(postDtoServiceCustomer);
		try {
			log.info("==== 获取审批人岗位信息  FlowApproverUtils.parsePost() acDtos="+JacksonUtils.toJson(acDtos));
			List<FlowAcPostDto> resultList = FlowApproverUtils.parsePost(businessVariableValue, acDtos);
			if (resultList != null) {
				for (AcDto acDto : acDtos) {
					acDto.setParticipant("");
					for (FlowAcPostDto flowAcPostDto : resultList) {
						log.info("==== 获取审批人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						//过滤重复岗位
					    if(flowPostParticipantDtos!=null&&flowPostParticipantDtos.size()>1){
					         for (int i = 0; i < flowPostParticipantDtos.size(); i++) {
							    for (int j = flowPostParticipantDtos.size()-1;j>i; j--) {
							    //判断是否同岗同人
								 if(flowPostParticipantDtos.get(i).getPostId()==flowPostParticipantDtos.get(j).getPostId()&&flowPostParticipantDtos.get(i).getUserId()==flowPostParticipantDtos.get(j).getUserId()){
									 flowPostParticipantDtos.remove(i);
								  }
						    	}
							}
					    }
//						log.info("==== 获取审批人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (acDto.getId().equals(flowAcPostDto.getId()) && flowPostParticipantDtos != null
								&& flowPostParticipantDtos.size() > 0) {
							acDto.setFlowPostParticipantDtos(flowPostParticipantDtos);
							acDto.setPosts(processAcPosts(flowPostParticipantDtos));
							log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getFlowPostParticipantDtos()="+ JacksonUtils.toJson(acDto.getFlowPostParticipantDtos()));
							log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getPosts()="+ JacksonUtils.toJson(acDto.getPosts()));
						}
					}
					
					log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getParticipant()="+ JacksonUtils.toJson(acDto.getParticipant()));
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取审批人岗位信息失败！", e);
		}
		/**
		 * 第7步：获取抄送人岗位信息
		 */
		try {
			
			for (AcDto acDto : acDtos) {
				acDto.setParticipant(acDto.getCcPerson());
			}

			//add by zdq 发起环节的抄送人解析成发起人，此处构建虚拟开始节点
			AcDto blank = new AcDto();
			blank.setParticipant(starter);
			acDtos.add(0, blank);
			
			log.info("==== 获取抄送人岗位信息  FlowApproverUtils.parsePost() acDtos="+JacksonUtils.toJson(acDtos));
			List<FlowAcPostDto> csResultList = FlowApproverUtils.parsePost(businessVariableValue, acDtos);
			
			////add by zdq 发起环节的抄送人解析成发起人，此处删除虚拟开始节点
			if(csResultList != null && csResultList.size() > 1) {
				if(csResultList.get(0).getId() == null) {
					csResultList.remove(0);
				}
			}
			acDtos.remove(0);
			
			if (csResultList != null) {
				for (AcDto acDto : acDtos) {
					acDto.setParticipant("");
					for (FlowAcPostDto flowAcPostDto : csResultList) {
						log.info("==== 获取抄送人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						log.info("==== 获取抄送人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (acDto.getId().equals(flowAcPostDto.getId()) && flowPostParticipantDtos != null
								&& flowPostParticipantDtos.size() > 0) {
							acDto.setCsPosts(processAcPosts(flowPostParticipantDtos));
							log.info("==== 获取抄送人岗位信息 for loop 003 acDto.getCsPosts()="+ JacksonUtils.toJson(acDto.getCsPosts()));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取抄送人岗位信息失败！", e);
		}
		/**
		 * 第8步：获取可阅人岗位信息
		 */
		try {
			List<AcDto> readerAcDtos = new ArrayList<AcDto>();
			if(accessibleAcDtos!=null && accessibleAcDtos.size()>0){
				AcDto tempAcDto = accessibleAcDtos.get(0);//模拟的开始节点不需要设置审批人信息
				AcDto startAcDto = new AcDto();
				//模拟一个开始环节,id不能使用已有的，否则会使真正的开始环节的审批人消失了
				startAcDto.setId(IDGenerator.getUUID());
				startAcDto.setAcType("1");
				startAcDto.setParticipant(tempAcDto.getParticipant());
				startAcDto.setCcPerson(null);
				startAcDto.setPosts(null);
				startAcDto.setCsPosts(null);
				startAcDto.setFlowPostParticipantDtos(null);
				startAcDto.setParticipant(accessibleAcDtos.get(0).getParticipant());
				readerAcDtos.add(startAcDto);
				
				AcDto approveAcDto = new AcDto(); //模拟一个审核节点
				approveAcDto.setId(IDGenerator.getUUID());
				approveAcDto.setParticipant(tempAcDto.getParticipant());
				approveAcDto.setApproveTypeId("SH");
				approveAcDto.setAcType("2");
				readerAcDtos.add(approveAcDto);
				
				String accessibleAcDtosJSON2 = JacksonUtils.toJson(readerAcDtos);
				log.info("==== 获取可阅人岗位信息 FlowApproverUtils.parsePost() accessibleAcDtosJSON2="+accessibleAcDtosJSON2);
				List<FlowAcPostDto> accessibleResultList = FlowApproverUtils.parsePost(businessVariableValue, readerAcDtos);
				if (accessibleResultList != null) {
					for (FlowAcPostDto flowAcPostDto : accessibleResultList) {
						log.info("====  获取可阅人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						log.info("====  获取可阅人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (flowPostParticipantDtos != null && flowPostParticipantDtos.size() > 0) {
							accessiblePosts = processAcPosts(flowPostParticipantDtos);
							log.info("====  获取可阅人岗位信息 for loop 003 accessiblePosts="+ accessiblePosts);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取可阅人岗位失败！", e);
		}

		/**
		 * 第9步：返回
		 */
		
		Map<String, Object> result = new HashMap<String, Object>();
		
//		String flowTitle = parseFlowTitle(fl,businessVariableValue);
		log.info("==== flowTitle==before="+ fl.getFlowTitle());
		String flowTitle = parseFlowTitle(fl.getFlowTitle(),businessVariableValue);
		log.info("==== flowTitle==after="+ flowTitle);
		List<ApprovalList> approvalLists = generateApprovalList(flowTitle, acDtos, businessVariableValue);
		
		result.put("appId", fl.getAppId());
		result.put("startUserId", businessVariableValue.get("start_user_id"));
		result.put("businessObjectCode", businessObjectDto.getCode());
		result.put("pcUrl", businessObjectDto.getPcUrl());
		result.put("flId", fl.getId());
		result.put("flCode", flCode);
		result.put("flowTitle", flowTitle);
		result.put("businessId", businessId);
		result.put("acDtoList", acDtos);
		result.put("approvalLists", approvalLists);
		result.put("variableDtoList", businessVariableValue);
		result.put("pcFormDtoList", pcFormValue);
		result.put("mobileFormDtoList", phoneFormValue);
		result.put("uploadAttachmentDtoList", phoneAttachment);
		log.info("====>>> start()   uploadAttachmentDtoList 对应的值是="+phoneAttachment);
		result.put("accessiblePosts", accessiblePosts);
		
		//转移至环节上
//		result.put("postIsNull", fl.getPostIsNull()); // 岗位为空策略(1:不允许发起,2:允许发起，挂起,3:允许发起，跳过，并显示该行,4:允许发起，跳过，不显示该行)
//		result.put("approvalPersonIsNull", fl.getApprovalPersonIsNull()); // 审批人为空策略(1:不允许发起,2:允许发起，挂起,3:允许发起，跳过，并显示该行,4:允许发起，跳过，不显示该行)
		result.put("instanceIdBeforeReturn", instanceIdBeforeReturn);
		
		return JacksonUtils.toJson(result);
		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public String emulation(String businessObjectId, String flId, String businessVariableJson) throws Exception {
		//获取当前模板
		Fl fl = flDao.getObjectById(flId);
		//BusinessObject businessObject = businessObjectService.getObjectById(businessObjectId);

		//获取业务变量
		Map<String,Object> businessVariableValueMap = JacksonUtils.fromJson(businessVariableJson,Map.class);

		//解析流程标题
//		String flowTitle = parseFlowTitle(fl,businessVariableValueMap);
		String flowTitle = parseFlowTitle(fl.getFlowTitle(),businessVariableValueMap);
		List<AcDto> acDtos = new ArrayList<AcDto>();
		try {
			List<NodeDto>	nodeDtos = parsePassFlowNodeDto(fl.getId(), businessVariableValueMap);
			for (NodeDto nodeDto : nodeDtos) {
				AcDto acDto = nodeDto.getCurrentAcDto();
				List<String> previousNodes = new ArrayList<String>();
				for (AcDto acDto2 : nodeDto.getPreviousAcDtos()) {
					previousNodes.add(acDto2.getNodeId());
				}
				acDto.setPreviousAcDtos(previousNodes);
				List<String> nextNodes = new ArrayList<String>();
				for (AcDto acDto3 : nodeDto.getNextNodeDtos()) {
					nextNodes.add(acDto3.getNodeId());
				}
				acDto.setNextNodeDtos(nextNodes);
				acDtos.add(acDto);
			}
		} catch (Exception e) {
			throw new FlowException("解析流程审批路径失败！", e);
		}


		/**
		 * 第5步：获取环节参与人相关信息（审批人、抄送人、可阅人）
		 */
		String accessiblePosts = ""; // 可阅人岗位信息
		List<AcDto> accessibleAcDtos = new ArrayList<AcDto>();
		List<ParticipantDto> csList = new ArrayList<ParticipantDto>(); // 抄送人
		List<ParticipantDto> participantList = new ArrayList<ParticipantDto>();// 审批人

		try {
			getParticipants(fl.getId(), acDtos, accessibleAcDtos, csList, participantList);
		} catch (Exception e) {
			throw new FlowException("获取环节审批人、抄送人、可阅人失败！", e);
		}

		//给开始节点设置人员,让计算审批人与抄送人的逻辑不同
		AcDto startAc = acDtos.get(0);
		String starter = (String) businessVariableValueMap.get("start_user_id");
		startAc.setParticipant(starter);

		/**
		 * 第6步：获取审批人岗位信息
		 */
		FlowApproverUtils.setPostDtoServiceCustomer(postDtoServiceCustomer);
		try {
			log.info("==== 获取审批人岗位信息  FlowApproverUtils.parsePost() acDtos="+JacksonUtils.toJson(acDtos));
			List<FlowAcPostDto> resultList = FlowApproverUtils.parsePost(businessVariableValueMap, acDtos);
			if (resultList != null) {
				for (AcDto acDto : acDtos) {
					acDto.setParticipant("");
					for (FlowAcPostDto flowAcPostDto : resultList) {
						log.info("==== 获取审批人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						//过滤重复岗位
						if(flowPostParticipantDtos!=null&&flowPostParticipantDtos.size()>1){
							for (int i = 0; i < flowPostParticipantDtos.size(); i++) {
								for (int j = flowPostParticipantDtos.size()-1;j>i; j--) {
									//判断是否同岗同人
									if(flowPostParticipantDtos.get(i).getPostId()==flowPostParticipantDtos.get(j).getPostId()&&flowPostParticipantDtos.get(i).getUserId()==flowPostParticipantDtos.get(j).getUserId()){
										flowPostParticipantDtos.remove(i);
									}
								}
							}
						}
//						log.info("==== 获取审批人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (acDto.getId().equals(flowAcPostDto.getId()) && flowPostParticipantDtos != null
								&& flowPostParticipantDtos.size() > 0) {
							acDto.setFlowPostParticipantDtos(flowPostParticipantDtos);
							acDto.setPosts(processAcPosts(flowPostParticipantDtos));
							log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getFlowPostParticipantDtos()="+ JacksonUtils.toJson(acDto.getFlowPostParticipantDtos()));
							log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getPosts()="+ JacksonUtils.toJson(acDto.getPosts()));
						}
					}

					log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getParticipant()="+ JacksonUtils.toJson(acDto.getParticipant()));
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取审批人岗位信息失败！", e);
		}
		/**
		 * 第7步：获取抄送人岗位信息
		 */
		try {

			for (AcDto acDto : acDtos) {
				acDto.setParticipant(acDto.getCcPerson());
			}

			//add by zdq 发起环节的抄送人解析成发起人，此处构建虚拟开始节点
			AcDto blank = new AcDto();
			blank.setParticipant(starter);
			acDtos.add(0, blank);

			log.info("==== 获取抄送人岗位信息  FlowApproverUtils.parsePost() acDtos="+JacksonUtils.toJson(acDtos));
			List<FlowAcPostDto> csResultList = FlowApproverUtils.parsePost(businessVariableValueMap, acDtos);

			////add by zdq 发起环节的抄送人解析成发起人，此处删除虚拟开始节点
			if(csResultList != null && csResultList.size() > 1) {
				if(csResultList.get(0).getId() == null) {
					csResultList.remove(0);
				}
			}
			acDtos.remove(0);

			if (csResultList != null) {
				for (AcDto acDto : acDtos) {
					acDto.setParticipant("");
					for (FlowAcPostDto flowAcPostDto : csResultList) {
						log.info("==== 获取抄送人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						log.info("==== 获取抄送人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (acDto.getId().equals(flowAcPostDto.getId()) && flowPostParticipantDtos != null
								&& flowPostParticipantDtos.size() > 0) {
							acDto.setCsPosts(processAcPosts(flowPostParticipantDtos));
							log.info("==== 获取抄送人岗位信息 for loop 003 acDto.getCsPosts()="+ JacksonUtils.toJson(acDto.getCsPosts()));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取抄送人岗位信息失败！", e);
		}
		/**
		 * 第8步：获取可阅人岗位信息
		 */
		try {
			List<AcDto> readerAcDtos = new ArrayList<AcDto>();
			if(accessibleAcDtos!=null && accessibleAcDtos.size()>0){
				AcDto startAcDto = new AcDto();
				//模拟一个开始环节,id不能使用已有的，否则会使真正的开始环节的审批人消失了
				startAcDto.setId(IDGenerator.getUUID());
				startAcDto.setAcType("1");
				startAcDto.setParticipant(null);
				startAcDto.setCcPerson(null);
				startAcDto.setPosts(null);
				startAcDto.setCsPosts(null);
				startAcDto.setFlowPostParticipantDtos(null);
				readerAcDtos.add(startAcDto);

				AcDto tempAcDto = accessibleAcDtos.get(0);//模拟的开始节点不需要设置审批人信息
				AcDto approveAcDto = new AcDto(); //模拟一个审核节点
				approveAcDto.setId(IDGenerator.getUUID());
				approveAcDto.setParticipant(tempAcDto.getParticipant());
				approveAcDto.setApproveTypeId("SH");
				approveAcDto.setAcType("2");
				readerAcDtos.add(approveAcDto);

				String accessibleAcDtosJSON2 = JacksonUtils.toJson(readerAcDtos);
				log.info("==== 获取可阅人岗位信息 FlowApproverUtils.parsePost() accessibleAcDtosJSON2="+accessibleAcDtosJSON2);
				List<FlowAcPostDto> accessibleResultList = FlowApproverUtils.parsePost(businessVariableValueMap, readerAcDtos);
				if (accessibleResultList != null) {
					for (FlowAcPostDto flowAcPostDto : accessibleResultList) {
						log.info("====  获取可阅人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						log.info("====  获取可阅人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (flowPostParticipantDtos != null && flowPostParticipantDtos.size() > 0) {
							accessiblePosts = processAcPosts(flowPostParticipantDtos);
							log.info("====  获取可阅人岗位信息 for loop 003 accessiblePosts="+ accessiblePosts);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取可阅人岗位失败！", e);
		}


		List<ApprovalList> approvalLists = generateApprovalList(flowTitle, acDtos, businessVariableValueMap);
		return JacksonUtils.toJson(approvalLists);
	}

	@Override
	public String emulation(Map<String, Object> paramMap) throws Exception {
		String variableValueJson = (String) paramMap.get("variableValueJson");
		Map<String,Object> flDataMap = (Map<String, Object>) paramMap.get("flData");
		FlDto flData = JacksonUtils.fromJson(JacksonUtils.toJson(flDataMap),FlDto.class) ;
		Map<String,Object> variableValueMap = JacksonUtils.fromJson(variableValueJson,Map.class);
		Fl fl = new Fl();
		BeanUtils.copyProperties(flData,fl);
		//解析流程标题
//		String flowTitle = parseFlowTitle(fl,variableValueMap);
		String flowTitle = parseFlowTitle(fl.getFlowTitle(),variableValueMap);
		//获取流程节点信息
		String acJson = flData.getAc();
		if (StringUtils.isBlank(acJson)) {
			throw new FlowException("流程节点信息不完整！");
		}
		//获取流程节点列表
		List<AcDto> acDtoList = JacksonUtils.fromJson(acJson,List.class,AcDto.class);
		//将节点放入map中
		Map<String,AcDto> acDtoMap = new HashMap<String,AcDto>();
		AcDto firstAc = null;
		for(AcDto acDto:acDtoList){
			if("1".equals(acDto.getAcType())){
				firstAc = acDto;
			}
			acDtoMap.put(acDto.getId(),acDto);
		}

		//获取节点连线信息
		String stepJson = flData.getStep();
		if (StringUtils.isBlank(stepJson)) {
			throw new FlowException("流程节点连线信息不完整！");
		}
		//获取节点连线列表
		List<StepDto> stepDtoList = JacksonUtils.fromJson(stepJson,List.class,StepDto.class);
		Map<String,StepDto> stepDtoMap = new HashMap<String,StepDto>();
		for(StepDto stepDto:stepDtoList){
			//stepDtoMap.put(stepDto.getId(),stepDto);
			String sourceId = stepDto.getSourceId();
			AcDto acDto = acDtoMap.get(sourceId);
			if(acDto!=null){
				List<StepDto> sourceAcStepList = acDto.getStepDtoList();
				sourceAcStepList.add(stepDto);
				acDtoMap.put(sourceId,acDto);
			}
		}

		//解析流程审批路径
		List<AcDto> parsedAcList = new LinkedList<AcDto>();
		if(firstAc!=null){
			parseAcStep(firstAc,acDtoMap,variableValueMap,parsedAcList);
		}

		//给开始节点设置人员,让计算审批人与抄送人的逻辑不同
		AcDto startAc = parsedAcList.get(0);
		String starter = (String) variableValueMap.get("start_user_id");
		startAc.setParticipant(starter);

		/**
		 * 第6步：获取审批人岗位信息
		 */
		FlowApproverUtils.setPostDtoServiceCustomer(postDtoServiceCustomer);
		try {
			log.info("==== 获取审批人岗位信息  FlowApproverUtils.parsePost() acDtos="+JacksonUtils.toJson(parsedAcList));
			List<FlowAcPostDto> resultList = FlowApproverUtils.parsePost(variableValueMap, parsedAcList);
			if (resultList != null) {
				for (AcDto acDto : parsedAcList) {
					acDto.setParticipant("");
					for (FlowAcPostDto flowAcPostDto : resultList) {
						log.info("==== 获取审批人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						//过滤重复岗位
						if(flowPostParticipantDtos!=null&&flowPostParticipantDtos.size()>1){
							for (int i = 0; i < flowPostParticipantDtos.size(); i++) {
								for (int j = flowPostParticipantDtos.size()-1;j>i; j--) {
									//判断是否同岗同人
									if(flowPostParticipantDtos.get(i).getPostId()==flowPostParticipantDtos.get(j).getPostId()&&flowPostParticipantDtos.get(i).getUserId()==flowPostParticipantDtos.get(j).getUserId()){
										flowPostParticipantDtos.remove(i);
									}
								}
							}
						}
//						log.info("==== 获取审批人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (acDto.getId().equals(flowAcPostDto.getId()) && flowPostParticipantDtos != null
								&& flowPostParticipantDtos.size() > 0) {
							acDto.setFlowPostParticipantDtos(flowPostParticipantDtos);
							acDto.setPosts(processAcPosts(flowPostParticipantDtos));
							log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getFlowPostParticipantDtos()="+ JacksonUtils.toJson(acDto.getFlowPostParticipantDtos()));
							log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getPosts()="+ JacksonUtils.toJson(acDto.getPosts()));
						}
					}

					log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getParticipant()="+ JacksonUtils.toJson(acDto.getParticipant()));
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取审批人岗位信息失败！", e);
		}


		List<ApprovalList> approvalLists = generateApprovalList(flowTitle, parsedAcList, variableValueMap);
		return JacksonUtils.toJson(approvalLists);
	}

	/**
	 * 解析审批路径
	 * @param acDto
	 * @param acDtoMap
	 * @param variableValueMap
	 * @param parsedAcList
	 */
	private void parseAcStep(AcDto acDto,Map<String,AcDto> acDtoMap,Map<String,Object> variableValueMap,List<AcDto> parsedAcList){
		List<StepDto> stepDtos = acDto.getStepDtoList();
		parsedAcList.add(acDto);
		if(stepDtos!=null&&stepDtos.size()>0){
			StepDto stepDto = null;
			String targetId = null;
			AcDto targetAc = null;
			if(stepDtos.size()==1){
				stepDto = stepDtos.get(0);
				targetId = stepDto.getTargetId();
				targetAc = acDtoMap.get(targetId);
			}else{
				for(StepDto stepDto1:stepDtos){
					String conditionExpression = stepDto1.getConditionExpression();
					if(ExpressionUtils.evaluate(conditionExpression, variableValueMap)){
						stepDto = stepDto1;
						targetId = stepDto.getTargetId();
						targetAc = acDtoMap.get(targetId);
						break;
					}
				}
			}

			if (StringUtils.isNotBlank(targetId)){
				parseAcStep(targetAc,acDtoMap,variableValueMap,parsedAcList);
			}

		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public InstanceDto startForMobile(String businessObjectCode, String flCode, String businessId) throws FlowException {
		
		/**
		 * 第1步：判断流程模板是否存在
		 */
		Fl fl = null;
		BusinessObjectDto businessObjectDto = null;
		try {
			if (StringUtils.isNotBlank(businessObjectCode) && !StringUtils.isNotBlank(flCode)) {
				// 获取业务对象下默认流程模板
				fl = queryDefaultFlow(businessObjectCode);
			} else {
				fl = queryReadyFlow(flCode);
			}

			if (fl == null) {
				throw new FlowException("未找到对应的模板：流程模板编码【 " + flCode + "】，业务对象编码：【" + businessObjectCode + "】");
			} else {
				if (StringUtils.isNotBlank(businessObjectCode) && !StringUtils.isNotBlank(flCode)) {
					fl = queryDefaultFlow(businessObjectCode);
					businessObjectDto = businessObjectService.getObjectByCode(businessObjectCode);
				} else {
					fl = queryReadyFlow(flCode);
					businessObjectDto = businessObjectService.getObjectByFlCode(flCode);
				}
			}
			fl = getObjectById(fl.getId());
		} catch (Exception e1) {
			throw new FlowException("获取流程模板信息失败！", e1);
		}
		/**
		 * 第2步：校验流程发起条件,不符合条件时抛出异常
		 */
		String instanceIdBeforeReturn = validateFlowStartCondition(fl, businessId);
		
		/**
		 * 第3步：获取业务变量值
		 */
		Map<String, Object> businessVariableValue = new HashMap<String, Object>();
		List<Object> pcFormValue = new ArrayList<Object>();
		List<Object> phoneFormValue = new ArrayList<Object>();
		List<Object> phoneAttachment = new ArrayList<Object>();
		if(StringUtils.isEmpty(businessObjectDto.getApproveClass())) {
			throw new FlowException("业务对象【" + businessObjectDto.getName() + "】中的查询业务变量URL为空！");
		}
		getBusinessVariableValue(businessObjectDto.getApproveClass(), businessId, 
				businessObjectDto.getCode(), flCode, businessVariableValue, 
				pcFormValue, phoneFormValue, phoneAttachment);
		log.info("调用业务系统变量查询接口返回数据：" + businessVariableValue);

		/**
		 * 第4步：解析流程审批路径
		 */
		List<AcDto> acDtos = new ArrayList<AcDto>();
		try {
			List<NodeDto>	nodeDtos = parsePassFlowNodeDto(fl.getId(), businessVariableValue);
			for (NodeDto nodeDto : nodeDtos) {
				AcDto acDto = nodeDto.getCurrentAcDto();
				List<String> previousNodes = new ArrayList<String>();
				Set<String> previouseNodesSet = new HashSet<String>();
				for (AcDto acDto2 : nodeDto.getPreviousAcDtos()) {
					previouseNodesSet.add(acDto2.getNodeId());
				}
				previousNodes.addAll(previouseNodesSet);
				acDto.setPreviousAcDtos(previousNodes);
				List<String> nextNodes = new ArrayList<String>();
				Set<String> nextNodesSet = new HashSet<String>();
				for (AcDto acDto3 : nodeDto.getNextNodeDtos()) {
					nextNodesSet.add(acDto3.getNodeId());
				}					
				nextNodes.addAll(nextNodesSet);
				acDto.setNextNodeDtos(nextNodes);
				acDtos.add(acDto);
			}
		} catch (Exception e) {
			throw new FlowException("解析流程审批路径失败！", e);
		}
		/**
		 * 第5步：获取环节参与人相关信息（审批人、抄送人、可阅人）
		 */
		String accessiblePosts = ""; // 可阅人岗位信息
		List<AcDto> accessibleAcDtos = new ArrayList<AcDto>();
		List<ParticipantDto> csList = new ArrayList<ParticipantDto>(); // 抄送人
		List<ParticipantDto> participantList = new ArrayList<ParticipantDto>();// 审批人

		try {
			getParticipants(fl.getId(), acDtos, accessibleAcDtos, csList, participantList);
		} catch (Exception e) {
			throw new FlowException("获取环节审批人、抄送人、可阅人失败！", e);
		}
		
		//给开始节点设置人员,让计算审批人与抄送人的逻辑不同
		AcDto startAc = acDtos.get(0);
		String starter = (String) businessVariableValue.get("start_user_id");
		startAc.setParticipant(starter);
		
		/**
		 * 第6步：获取审批人岗位信息
		 */
		FlowApproverUtils.setPostDtoServiceCustomer(postDtoServiceCustomer);
		try {
			log.info("==== 获取审批人岗位信息  FlowApproverUtils.parsePost() acDtos="+JacksonUtils.toJson(acDtos));
			List<FlowAcPostDto> resultList = FlowApproverUtils.parsePost(businessVariableValue, acDtos);
			if (resultList != null) {
				for (AcDto acDto : acDtos) {
					acDto.setParticipant("");
					for (FlowAcPostDto flowAcPostDto : resultList) {
						log.info("==== 获取审批人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						//过滤重复岗位
					    if(flowPostParticipantDtos!=null&&flowPostParticipantDtos.size()>1){
					         for (int i = 0; i < flowPostParticipantDtos.size(); i++) {
							    for (int j = flowPostParticipantDtos.size()-1;j>i; j--) {
							    //判断是否同岗同人
								 if(flowPostParticipantDtos.get(i).getPostId()==flowPostParticipantDtos.get(j).getPostId()&&flowPostParticipantDtos.get(i).getUserId()==flowPostParticipantDtos.get(j).getUserId()){
									 flowPostParticipantDtos.remove(i);
								  }
						    	}
							}
					    }
//						log.info("==== 获取审批人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (acDto.getId().equals(flowAcPostDto.getId()) && flowPostParticipantDtos != null
								&& flowPostParticipantDtos.size() > 0) {
							acDto.setFlowPostParticipantDtos(flowPostParticipantDtos);
							acDto.setPosts(processAcPosts(flowPostParticipantDtos));
							log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getFlowPostParticipantDtos()="+ JacksonUtils.toJson(acDto.getFlowPostParticipantDtos()));
							log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getPosts()="+ JacksonUtils.toJson(acDto.getPosts()));
						}
					}
					
					log.info("==== 获取审批人岗位信息 acName=" + acDto.getName() + "nodeId="  + acDto.getNodeId() + "acDto.getParticipant()="+ JacksonUtils.toJson(acDto.getParticipant()));
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取审批人岗位信息失败！", e);
		}
		/**
		 * 第7步：获取抄送人岗位信息
		 */
		try {
			
			for (AcDto acDto : acDtos) {
				acDto.setParticipant(acDto.getCcPerson());
			}

			//add by zdq 发起环节的抄送人解析成发起人，此处构建虚拟开始节点
			AcDto blank = new AcDto();
			blank.setParticipant(starter);
			acDtos.add(0, blank);
			
			log.info("==== 获取抄送人岗位信息  FlowApproverUtils.parsePost() acDtos="+JacksonUtils.toJson(acDtos));
			List<FlowAcPostDto> csResultList = FlowApproverUtils.parsePost(businessVariableValue, acDtos);
			
			////add by zdq 发起环节的抄送人解析成发起人，此处删除虚拟开始节点
			if(csResultList != null && csResultList.size() > 1) {
				if(csResultList.get(0).getId() == null) {
					csResultList.remove(0);
				}
			}
			acDtos.remove(0);
			
			if (csResultList != null) {
				for (AcDto acDto : acDtos) {
					acDto.setParticipant("");
					for (FlowAcPostDto flowAcPostDto : csResultList) {
						log.info("==== 获取抄送人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						log.info("==== 获取抄送人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (acDto.getId().equals(flowAcPostDto.getId()) && flowPostParticipantDtos != null
								&& flowPostParticipantDtos.size() > 0) {
							acDto.setCsPosts(processAcPosts(flowPostParticipantDtos));
							log.info("==== 获取抄送人岗位信息 for loop 003 acDto.getCsPosts()="+ JacksonUtils.toJson(acDto.getCsPosts()));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取抄送人岗位信息失败！", e);
		}
		/**
		 * 第8步：获取可阅人岗位信息
		 */
		try {
			List<AcDto> readerAcDtos = new ArrayList<AcDto>();
			if(accessibleAcDtos!=null && accessibleAcDtos.size()>0){
				AcDto tempAcDto = accessibleAcDtos.get(0);//模拟的开始节点不需要设置审批人信息
				AcDto startAcDto = new AcDto();
				//模拟一个开始环节,id不能使用已有的，否则会使真正的开始环节的审批人消失了
				startAcDto.setId(IDGenerator.getUUID());
				startAcDto.setAcType("1");
				startAcDto.setParticipant(tempAcDto.getParticipant());
				startAcDto.setCcPerson(null);
				startAcDto.setPosts(null);
				startAcDto.setCsPosts(null);
				startAcDto.setFlowPostParticipantDtos(null);
				startAcDto.setParticipant(accessibleAcDtos.get(0).getParticipant());
				readerAcDtos.add(startAcDto);
				
				AcDto approveAcDto = new AcDto(); //模拟一个审核节点
				approveAcDto.setId(IDGenerator.getUUID());
				approveAcDto.setParticipant(tempAcDto.getParticipant());
				approveAcDto.setApproveTypeId("SH");
				approveAcDto.setAcType("2");
				readerAcDtos.add(approveAcDto);
				
				String accessibleAcDtosJSON2 = JacksonUtils.toJson(readerAcDtos);
				log.info("==== 获取可阅人岗位信息 FlowApproverUtils.parsePost() accessibleAcDtosJSON2="+accessibleAcDtosJSON2);
				List<FlowAcPostDto> accessibleResultList = FlowApproverUtils.parsePost(businessVariableValue, readerAcDtos);
				if (accessibleResultList != null) {
					for (FlowAcPostDto flowAcPostDto : accessibleResultList) {
						log.info("====  获取可阅人岗位信息 for loop 001 flowAcPostDto="+ JacksonUtils.toJson(flowAcPostDto));
						List<FlowPostParticipantDto> flowPostParticipantDtos = flowAcPostDto.getFlowPostParticipantDtos();
						log.info("====  获取可阅人岗位信息 for loop 002 flowPostParticipantDtos="+ JacksonUtils.toJson(flowPostParticipantDtos));
						if (flowPostParticipantDtos != null && flowPostParticipantDtos.size() > 0) {
							accessiblePosts = processAcPosts(flowPostParticipantDtos);
							log.info("====  获取可阅人岗位信息 for loop 003 accessiblePosts="+ accessiblePosts);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取可阅人岗位失败！", e);
		}

//		String flowTitle = parseFlowTitle(fl,businessVariableValue);
		String flowTitle = parseFlowTitle(fl.getFlowTitle(),businessVariableValue);
//		List<ApprovalList> approvalLists = generateApprovalList(flowTitle, acDtos, businessVariableValue);

		/**
		 * 第9步：返回
		 */
		List<InstanceVariableDto> varList = new ArrayList<InstanceVariableDto>();
		for(String key : businessVariableValue.keySet()) {
			InstanceVariableDto var = new InstanceVariableDto();
			var.setName(key);
			var.setVal((String)businessVariableValue.get(key).toString());
			varList.add(var);
		}
		
		List<PcFormDto> pcFormDtoList = new ArrayList<PcFormDto>();
		for(Object obj : pcFormValue) {
			PcFormDto pcFormDto = new PcFormDto();
			Map<String, Object> map = (Map<String, Object>)obj;
			pcFormDto.setName((String)map.get("name"));
			pcFormDto.setValue((String)map.get("value"));
			pcFormDtoList.add(pcFormDto);
		}
		
		List<MobileFormDto> mobileFormList = new ArrayList<MobileFormDto>();
		for(Object obj : phoneFormValue) {
			MobileFormDto mobileFormDto = new MobileFormDto();
			Map<String, Object> map = (Map<String, Object>)obj;
			mobileFormDto.setName((String)map.get("name"));
			mobileFormDto.setValue((String)map.get("value"));
			mobileFormList.add(mobileFormDto);
		}
		
		InstanceDto instanceDto = new InstanceDto();
		
		/*
		 * 移动端发起时流程实例ID取businessId，移动端以businessId上传附件，
		 * 流程引擎以instanceId查询发起人的附件，两者关联上
		 */
		instanceDto.setId(IDGenerator.getUUID());
		
		instanceDto.setName(flowTitle);
		instanceDto.setAppId(fl.getAppId());
		instanceDto.setBusinessObjectCode(businessObjectDto.getCode());
		instanceDto.setFlId(fl.getId());
		instanceDto.setFlCode(fl.getCode());
		instanceDto.setPcUrl(businessObjectDto.getPcUrl());
		instanceDto.setBusinessId(businessId);
		instanceDto.setInstanceIdBeforeReturn(instanceIdBeforeReturn);
		instanceDto.setPostIsNull(fl.getPostIsNull());
		instanceDto.setApprovalPersonIsNull(fl.getApprovalPersonIsNull());
		instanceDto.setVariableDtoList(varList);
		instanceDto.setPcFormDtoList(pcFormDtoList);
		instanceDto.setMobileFormDtoList(mobileFormList);
		instanceDto.setAccessiblePosts(accessiblePosts);
		instanceDto.setAcDtoList(acDtos);
		
		return instanceDto;
		
	}

	/**
	 * 检查流程发起条件
	 * 
	 * @param fl
	 * @param businessId
	 * @return 如果是打回再发起情况下，返回打回前的流程实例ID
	 * @throws FlowException
	 */
	public String validateFlowStartCondition(Fl fl, String businessId) throws FlowException {
		
		//1、检查流程模板
		if(!fl.getUseStatus()) {
			throw new FlowException("流程模板被禁用！");
		}
		if(!FlStatus.PUBLISH.getValue().equals(fl.getStatus())) {
			String status=fl.getStatus();
			status=status.equals("0")?"起草":"失效";
			throw new FlowException("流程模板状态错误：" + fl.getStatus());
		}
		
		//2、检查businessId对应的流程实例情况
		String instanceIdBeforeReturn = null;
		List<Instance> flowInstances = instanceService.queryInstanceBy(fl.getCode(), businessId);
		for(Instance instance : flowInstances) {
			if(InstanceStatus.RUNNING.getValue().equals(instance.getStatus())
					|| InstanceStatus.FINISHED.getValue().equals(instance.getStatus())	//同一条业务数据只能发起一次流程
					|| InstanceStatus.HANGUP.getValue().equals(instance.getStatus())) {
				throw new FlowException("流程已运行instanceId=" + instance.getId());
				
				//打回再发起
			} else if(InstanceStatus.REJECT.getValue().equals(instance.getStatus())) {
				instanceIdBeforeReturn = instance.getId();
			}
		}
		return instanceIdBeforeReturn;
	}

	/**
	 * 解析流程审批路径
	 * 
	 * @param flId 流程模板Id
	 * @param businessVariableMap 业务变量Map
	 * @return
	 * @throws Exception 
	 */
	/*private List<NodeDto> parseFlowPath(String flId, Map<String, Object> businessVariableMap) {
		List<NodeDto> nodeDtos = new ArrayList<NodeDto>();
		Map<String, Object> queryCond = new HashMap<String, Object>();	
		queryCond.put("flId", flId);
		*//**
		 * 第1步：查询流程模板所有环节，并组装成Map
		 *//*
		List<AcDto> acDtos = new ArrayList<AcDto>();
		Map<String, AcDto> acDtoMap = new HashMap<String, AcDto>();
		try {
			List<Ac> acs = acService.queryList(queryCond);
			for (Ac ac : acs) {
				AcDto acDto = new AcDto();
				BeanUtils.copyProperties(ac, acDto);
				acDtoMap.put(acDto.getId(), acDto);
				acDtos.add(acDto);
				// 创建路径环节
				NodeDto nodeDto = new NodeDto();
				nodeDto.setId(acDto.getId());
				nodeDto.setName(acDto.getName());
				nodeDto.setCurrentAcDto(acDto);
				nodeDtos.add(nodeDto);
			}
		} catch (Exception e) {
			throw new FlowException("解析流程审批路径【第1步：查询流程模板所有环节】错误！", e);
		}
		*//**
		 * 第2步：查询流程模板所有环节连线，并组装条件表达式Map
		 *//*		
		// 条件表达式Map
		ConcurrentHashMap<String, Step> stepMap = new ConcurrentHashMap<String, Step>();
		// 条件表达式Map
		Map<String, String> conExpMap = new HashMap<String, String>();
		
		try {
			List<Step> steps = stepService.queryList(queryCond);
			// Map化
			for (Step step : steps) {
				stepMap.put(step.getId(), step);
				conExpMap.put(step.getSourceId() + "-" + step.getTargetId(), step.getConditionExpression());
			}

			// 剔除不符合条件的连线
			FlowPathUtils.changeStep(stepMap, businessVariableMap);

		} catch (Exception e) {
			throw new FlowException("解析流程审批路径【第2步：查询流程模板所有环节连线】错误！", e);
		}
		*//**
		 * 第3步：关联所有环节。【 1:开始,2:普通,3:结束,4:聚合网关，5:条件网关】
		 *//*			
		// 开始环节
		NodeDto startNodeDto = null; 
		// 结束环节
		NodeDto endNodeDto = null;  
		Map<String, NodeDto> nodeDtoMap = new HashMap<String, NodeDto>();
		try {
			// 连线去重
			Map<String, Step> tmpStepMap = new HashMap<String, Step>();
			for (Step step : stepMap.values()) {
				tmpStepMap.put(step.getSourceId() + "-" + step.getTargetId(), step);
			}
			for (NodeDto nodeDto : nodeDtos) {
				List<NodeDto> nextNodeDtos = new ArrayList<NodeDto>();
				List<NodeDto> prevAcDtos = new ArrayList<NodeDto>();
				for (Step step : tmpStepMap.values()) {
					StepDto stepDto = new StepDto();
					BeanUtils.copyProperties(step, stepDto);
					stepDtos.add(stepDto);
					if (step.getSourceId().equals(nodeDto.getCurrentAcDto().getId())) {
						AcDto nextAcDto = acDtoMap.get(step.getTargetId());
						NodeDto nextNodeDto = new NodeDto();
						nextNodeDto.setId(nextAcDto.getId());
						nextNodeDto.setName(nextAcDto.getName());
						nextNodeDto.setCurrentAcDto(nextAcDto);
						nextNodeDtos.add(nextNodeDto);
					}
					if (step.getTargetId().equals(nodeDto.getCurrentAcDto().getId())) {
						AcDto prevAcDto = acDtoMap.get(step.getSourceId());
						NodeDto prevNodeDto = new NodeDto();
						prevNodeDto.setId(prevAcDto.getId());
						prevNodeDto.setName(prevAcDto.getName());
						prevNodeDto.setCurrentAcDto(prevAcDto);
						prevAcDtos.add(prevNodeDto);
					}
				}
				nodeDto.setNextNodeDtos(nextNodeDtos);
				nodeDto.setPreviousAcDtos(prevAcDtos);
				if ("1".equals(nodeDto.getCurrentAcDto().getAcType())) {
					startNodeDto = nodeDto;
				} else if ("3".equals(nodeDto.getCurrentAcDto().getAcType())) {
					endNodeDto = nodeDto;
				}
				nodeDtoMap.put(nodeDto.getId(), nodeDto);
			}
		} catch (Exception e) {
			throw new FlowException("解析流程审批路径【第3步：关联所有环节】错误！", e);
		}
		
		*//**
		 * 第4步：搜索流程审批路径
		 *//*			
		List<NodeDto> flowPath = null;
		try {
			FlowPathUtils flowPathUtils = new FlowPathUtils();
			flowPathUtils.serachPath(startNodeDto, endNodeDto, nodeDtoMap);
			flowPath = flowPathUtils.getFlowPath();
		} catch (Exception e) {
			throw new FlowException("解析流程审批路径【第4步：搜索流程审批路径】错误！", e);
		}
		
		return flowPath;
	}*/

	
	/**
	 * 解析出可以执行的流程节点
	 * 
	 * @param flId 流程模板Id
	 * @param businessVariableValue 业务变量
	 * @return
	 * @throws Exception 
	 */
	public List<NodeDto> parsePassFlowNodeDto(String flId, Map<String, Object> businessVariableValue) {
		Map<String, Object> queryCond = new HashMap<String, Object>();	
		queryCond.put("flId", flId);
		/**
		 * 第1步：查询流程模板所有环节，并组装成Map
		 */
		List<Ac> allAc=null;
		Ac firstAc=null;
		Map<String, Ac> acMap = new HashMap<String, Ac>();
		try {
			allAc = acService.queryList(queryCond);
			for (Ac ac : allAc) {
                 if("1".equals(ac.getAcType())){
                	 firstAc=ac;
                 }
                 acMap.put(ac.getId(), ac);
			}
		} catch (Exception e) {
			throw new FlowException("解析流程审批路径【第1步：查询流程模板所有环节】错误！", e);
		}
		/**
		 * 第2步：查询流程模板所有环节连线，并组装条件表达式Map
		 */		
		// 环节连线Map
		List<Step> allStep = new ArrayList<Step>();
		// 条件表达式
		Map<String, List<Step>> targetStepMap = new HashMap<String, List<Step>>();
		Map<String, List<Step>> sourceStepMap = new HashMap<String, List<Step>>();
		try {
//			allStep = stepService.queryList(queryCond);	//模板保存时连线有重复的情况。
			allStep = stepService.queryStepsBy(flId);	//有排重过滤
			// 获取关联节点
			for (Step step : allStep) {
				//数据源
				if(sourceStepMap.get(step.getSourceId())!=null){
					List<Step> nextStep=sourceStepMap.get(step.getSourceId());
					nextStep.add(step);
					sourceStepMap.put(step.getSourceId(), nextStep);
				}else{
					List<Step> nextStep=new ArrayList<Step>();
					nextStep.add(step);
					sourceStepMap.put(step.getSourceId(), nextStep);
				}
				//目标
				if(targetStepMap.get(step.getTargetId())!=null){
					List<Step> nextStep=targetStepMap.get(step.getTargetId());
					nextStep.add(step);
					targetStepMap.put(step.getTargetId(), nextStep);
				}else{
					List<Step> nextStep=new ArrayList<Step>();
					nextStep.add(step);
					targetStepMap.put(step.getTargetId(), nextStep);
				}
			}
		} catch (Exception e) {
			throw new FlowException("解析流程审批路径【第2步：查询流程模板所有环节连线】错误！", e);
		}
		/**
		 * 第4步：搜索流程审批路径
		 */			
		List<NodeDto> flowPath = null;
		try {
			flowPath=FlowPathUtils.getPassAcDtoNode(firstAc, acMap, sourceStepMap,targetStepMap,businessVariableValue);
		} catch (Exception e) {
			throw new FlowException("解析流程审批路径【第4步：搜索流程审批路径】错误！", e);
		}
		
		return flowPath;
	}
	
	
	public Fl queryReadyFlow(String flCode) {
		return flDao.queryReadyFlowBy(flCode);
	}

	@Override
	public Fl queryDefaultFlow(String businessObjectCode) {
		return flDao.queryDefaultFlow(businessObjectCode);
	}

	@Override
	public List<FlDto> queryListByApprover(Map<String, String> paramMap) {
		String approverId = (String)paramMap.get("approverId");
		if(approverId == null || "-1".equals(approverId) ){
			return new ArrayList<FlDto>();
		}
		return flDao.queryListByApprover(paramMap);
	}
	@Override
	public List<FlDto> queryListByParticipant(Map<String, String> paramMap) {
		String approverId = (String)paramMap.get("approverId");
		if(approverId == null || "-1".equals(approverId) ){
			return new ArrayList<FlDto>();
		}
		return flDao.queryListByParticipant(paramMap);
	}

	/**
	 * 获取参与人信息
	 * 
	 * @param flId
	 * @param acDtos
	 * @param accessibleAcDtos
	 * @param csList
	 * @param participantList
	 */
	private void getParticipants(String flId, List<AcDto> acDtos, List<AcDto> accessibleAcDtos,
			List<ParticipantDto> csList, List<ParticipantDto> participantList) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("flId", flId);
		try {
			queryMap.put("sidx", "sort");
			queryMap.put("sord", "asc");
			queryMap.put("delflag", false);
			List<Participant> queryList = participantService.queryList(queryMap);
			AcDto accessibleacDto = new AcDto();
			accessibleacDto.setId(IDGenerator.getUUID());
			for (Participant participant : queryList) {
				String type = participant.getType();
				if ("3".equals(type)) {
					ParticipantDto accessible = new ParticipantDto();
					BeanUtils.copyProperties(participant, accessible);
					participantList.add(accessible);
					
				}
			}
			accessibleacDto.setParticipant(JacksonUtils.toJson(participantList));
			accessibleAcDtos.add(accessibleacDto);
			
			for (AcDto acDto : acDtos) {
				csList.clear();
				participantList.clear();
				for (Participant participant : queryList) {
					ParticipantDto participantDto = new ParticipantDto();
					BeanUtils.copyProperties(participant, participantDto);
					String type = participantDto.getType();
					String acId = participantDto.getAcId();
					if ("2".equals(type) && acDto.getId().equals(acId)) {
						csList.add(participantDto);
					} else if ("1".equals(type) && acDto.getId().equals(acId)) {
						participantList.add(participantDto);
					}
				}
				if (csList.size() > 0) {
					acDto.setCcPerson(JacksonUtils.toJson(csList));
				}
				if (participantList.size() > 0) {
					acDto.setParticipant(JacksonUtils.toJson(participantList));
				}
			}
		} catch (Exception e) {
			throw new FlowException("获取环节审批人、抄送人、可阅人失败！", e);
		}
	}
	
	/**
	 * 获取业务数据
	 * 
	 * @param
	 * @param requestUrl
	 * @param businessId
	 * @param businessObjectCode
	 * @param flCode
	 * @param businessVariableValue
	 * @param pcFormValue
	 * @param phoneFormValue
	 */
	@SuppressWarnings("unchecked")
	public void getBusinessVariableValue(String requestUrl, String businessId,
			String businessObjectCode, String flCode, Map<String, Object> businessVariableValue,
			List<Object> pcFormValue, List<Object> phoneFormValue, List<Object> phoneAttachment) {

		String businessReturnStr = null;
		Map<String, Object> businessMap = null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId", businessId);
		params.put("businessObjectCode", businessObjectCode);
		params.put("flCode", flCode);
		
		//往redis中设置当前用户信息
		SecurityUserBeanInfo userInfo = LoginUtils.getSecurityUserBeanInfo();
		String token = userInfo.getSecurityUserDto().getLoginName() + "@" + userInfo.getTendCode();
		params.put("token", token);
		SecurityUserBeanRelationInfo relationInfo = new SecurityUserBeanRelationInfo();
		setLoginInfo(token, JacksonUtils.toJson(userInfo), JacksonUtils.toJson(relationInfo));
		log.info("调用业务系统获取业务变量接口时当前用户信息：" + JacksonUtils.toJson(userInfo));
		
		try {
			log.info("第3步：获取业务变量值(请求业务系统),requestUrl=" + requestUrl);
			log.info("第3步：获取业务变量值(请求业务系统),params=" + JacksonUtils.toJson(params));
//			requestUrl="http://127.0.0.1:8080/"+requestUrl.substring(requestUrl.indexOf("platform-app"), requestUrl.length());
			businessReturnStr = LoginUtils.httpPost(requestUrl, JacksonUtils.toJson(params));
//			test();
			if(StringUtils.isEmpty(businessReturnStr)) {
				String message = "调用业务系统查询业务变量返回值为空！url=" + requestUrl + ", params=" + JacksonUtils.toJson(params);
				log.info(message);
				throw new FlowException(message);
			}
			Map<String, String> businessReturnMap = JacksonUtils.fromJson(businessReturnStr, Map.class);
			if(businessReturnMap == null) {
				throw new FlowException("调用业务系统查询业务变量返回异常:url=" + requestUrl + "参数=" + JacksonUtils.toJson(params) + "返回值=" + businessReturnStr);
			}
			businessMap = JacksonUtils.fromJson(businessReturnMap.get("result"), Map.class);
			Map<String, Object> flow_business_variable_data = (Map<String, Object>) businessMap.get("flow_business_variable_data");
			List<Object> flow_business_data = (List<Object>) ((Map<String, Object>) businessMap.get("flow_business_data")).get("dataList");
//			List<Object> flow_business_attachment = (List<Object>) ((Map<String, Object>) businessMap.get("flow_business_data")).get("uploadEntityList");
			List<Object> flow_phone_data = (List<Object>) ((Map<String, Object>) businessMap.get("flow_phone_data")).get("dataList");
			List<Object> flow_phone_attachment = (List<Object>) ((Map<String, Object>) businessMap.get("flow_phone_data")).get("uploadEntityList");
			log.info("====== 获取 flow_phone_data>>uploadEntityList'json = "+JacksonUtils.toJson(flow_phone_attachment));
			if (flow_business_variable_data != null) {
				businessVariableValue.putAll(flow_business_variable_data);
				BusinessObjectDto businessObjectDto = businessObjectService.getObjectByCode(businessObjectCode);
				businessVariableValue.put("business_object_name", businessObjectDto.getName());
				// 变量类型适配
				HashMap<String, String> condMap = new HashMap<String, String>();
				condMap.put("businessObjectCode", businessObjectCode);
				List<BusinessObjectVariableDto> variables = businessObjectVariableService.queryListByCondition(condMap);
				businessVariableValue = variableTypeAdapter(variables, businessVariableValue);
				
				//处理流程发起人：如果为空，取当前用户
				if(StringUtils.isEmpty((String)businessVariableValue.get("start_user_id"))) {
					if(LoginUtils.getSecurityUserBeanInfo() != null) {
						SecurityUserDto securityUserDto = LoginUtils.getSecurityUserBeanInfo().getSecurityUserDto();
						if(securityUserDto != null) {
							businessVariableValue.put("start_user_id", securityUserDto.getId());
							businessVariableValue.put("start_user_name", securityUserDto.getRealName());
						}
					}
				}
			}
			if (flow_business_data != null) {
				pcFormValue.addAll(flow_business_data);
			}
			if (flow_phone_data != null) {
				phoneFormValue.addAll(flow_phone_data);
			}
			if (flow_phone_attachment != null) {
				phoneAttachment.addAll(flow_phone_attachment);
			}
			
			if(CollectionUtils.isEmpty(phoneFormValue)) {
				throw new FlowException("流程发起过程中移动表单数据获取为空！");
			}
			
			log.info("第3步：获取业务变量值(请求业务系统), result=" + businessMap);
		} catch (Exception e) {
			throw new FlowException("调用业务系统查询业务变量返回异常:url=" + requestUrl + "参数=" + JacksonUtils.toJson(params) + "返回值=" + businessReturnStr, e);
		}
	}
	
	public void test() {
//		ApplicationContext context = 
//			    new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});  
//		FlServiceImpl service = context.getBean(FlServiceImpl.class);
		String url = "http://192.168.3.134:8080/platform-app/sys/base/customFormInstance/getVariable";
		String businessId = "8bfddef3b1504d08b5ed4a04c6ed134f";
		String businessObjectCode = "xm21";
		String flCode = "";
		List<Object> phoneAttachment = new ArrayList<Object>();
		this.getBusinessVariableValue(url, businessId, businessObjectCode, 
				flCode, new HashMap<String, Object>(), 
				new ArrayList<Object>(), new ArrayList<Object>(), phoneAttachment);
	}
	
	/**
	 * 生成ApprovalList对象
	 * 
	 * @param
	 * @param
	 * @param flowTitle
	 * @param acDtos
	 * @param businessVariableValue
	 * @return
	 */
	private List<ApprovalList> generateApprovalList(String flowTitle, List<AcDto> acDtos,
			Map<String, Object> businessVariableValue) {
		List<ApprovalList> approvalLists = new ArrayList<ApprovalList>();
		for (AcDto acDto : acDtos) {
			if (!FlAcType.FORK.getAcType().equals(acDto.getAcType()) 
					&& !FlAcType.JOIN.getAcType().equals(acDto.getAcType())
					&& !FlAcType.CC.getAcType().equals(acDto.getAcType())) {
				List<FlowPostParticipantDto> posts = acDto.getFlowPostParticipantDtos();
				if (posts != null && posts.size() > 0) {
					for (FlowPostParticipantDto post : posts) {
						ApprovalList approvalList = new ApprovalList();
						approvalList.setInstanceName(flowTitle);
						approvalList.setAcId(acDto.getId());
						approvalList.setAcName(acDto.getName());
						approvalList.setAcType(acDto.getAcType());
						approvalList.setPostId(post.getPostId());
						approvalList.setPostName(post.getPostPrefixName());
						approvalList.setApproverId(post.getUserId());
						approvalList.setApproverName(post.getUserName());
						approvalList.setPostNull(acDto.getPostIsNull());
						approvalList.setApproverNull(acDto.getApprovalPersonIsNull());
						Boolean isStart = acDto.getIsStart();
						if(isStart == null) {
							approvalList.setIsStart(false);
							
						} else {
							approvalList.setIsStart(isStart);
						}
						Boolean isAddLabel = acDto.getIsAddLabel();
						if(isAddLabel == null) {
							approvalList.setSetApproverWhenStart(false);
							
						} else {
							approvalList.setSetApproverWhenStart(isAddLabel);
						}
						log.info("流程发起时环节审批人是否手动指定：acName=" + acDto.getName() + ":" + isAddLabel);
						approvalLists.add(approvalList);
					}
				} else {
					ApprovalList approvalList = new ApprovalList();
					approvalList.setInstanceName(flowTitle);
					approvalList.setAcId(acDto.getId());
					approvalList.setAcName(acDto.getName());
					approvalList.setAcType(acDto.getAcType());
					approvalList.setPostNull(acDto.getPostIsNull());
					approvalList.setApproverNull(acDto.getApprovalPersonIsNull());
					Boolean isStart = acDto.getIsStart();
					if(isStart == null) {
						approvalList.setIsStart(false);
						
					} else {
						approvalList.setIsStart(isStart);
					}
					Boolean isAddLabel = acDto.getIsAddLabel();
					if(isAddLabel == null) {
						approvalList.setSetApproverWhenStart(false);
						
					} else {
						approvalList.setSetApproverWhenStart(isAddLabel);
					}
					log.info("流程发起时环节审批人是否手动指定：acName=" + acDto.getName() + ":" + isAddLabel);

					approvalLists.add(approvalList);
				}
			}
		}
		return approvalLists;
	}	
	/**
	 * 结构化人员岗位信息
	 * @param flowPostParticipantDtos
	 * @return
	 */
	private static String processAcPosts(List<FlowPostParticipantDto> flowPostParticipantDtos) {
		Map<String, PostDto> postMap = new LinkedHashMap<String, PostDto>();
		for (FlowPostParticipantDto flowPostParticipantDto : flowPostParticipantDtos) {
			Map<String, UserDto> userMap = new HashMap<String, UserDto>();
			PostDto postDto = new PostDto();
			postDto.setId(flowPostParticipantDto.getPostId());
			postDto.setName(flowPostParticipantDto.getPostPrefixName());
			for (FlowPostParticipantDto flowPostParticipantDto1 : flowPostParticipantDtos) {
				if (flowPostParticipantDto1.getPostId().equals(flowPostParticipantDto.getPostId())) {
					UserDto userDto = new UserDto();
					userDto.setId(flowPostParticipantDto1.getUserId());
					userDto.setName(flowPostParticipantDto1.getUserName());
					userMap.put(userDto.getId(), userDto);
				}
			}
			postDto.setUsers(new ArrayList<UserDto>(userMap.values()));
			postMap.put(postDto.getId(), postDto);
		}
		return JacksonUtils.toJson(postMap.values());
	}
	
	/**
	 * 翻译流程标题
	 * 
	 * @param
	 * @param businessVariableMap
	 * @return
	 */
	@SuppressWarnings("finally")
	public String parseFlowTitle(Fl fl, Map<String, Object> businessVariableMap) {
		StringBuffer flowTitle = new StringBuffer();
		try {
			int index = 0;
			Pattern pat = Pattern.compile("@[^@]*@");
			Matcher mat = pat.matcher(fl.getFlowTitle());
			while (mat.find()) {
				String varName = mat.group(0).replaceAll("@", "");
				Object obj = businessVariableMap.get(varName);
				if(obj != null && !obj.toString().equals("")) {
					if(index==0){//第一个变量去掉-
						flowTitle.append(obj);
					}else{
						flowTitle.append("-").append(obj);
					}
					index++;
				}
			}
		} catch (Exception e) {

		}finally{
			return flowTitle.toString();
		}
	}

	public String parseFlowTitle(String flowTitle, Map<String, Object> businessVariableMap) {
		//String newFlowTitle = flowTitle;
		try {
			String pattern = "@(.+?)@";
			// 创建 Pattern 对象
			Pattern r = Pattern.compile(pattern);
			// 现在创建 matcher 对象
			Matcher m = r.matcher(flowTitle);
			//Map<String ,String> matchFieldMap = new HashMap<String,String>();
			while (m.find( )) {
				//System.out.println(m.groupCount());
				String field = m.group(0);
				String val = String.valueOf(businessVariableMap.get(field.replaceAll("@","")));
				if(val==null || "null".equals(val)){
					val="";
				}
				//matchFieldMap.put(field, );
				log.info("==== flowItem==field="+ field);
				log.info("==== flowItem==val="+ val);
				flowTitle = flowTitle.replace(field,val);
				System.out.println("Found value: " + field);
				log.info("==== flowItem==flowTitle="+ flowTitle);
			}


		}catch (Exception e){

		}finally {

		}

		return flowTitle;

	}

	@Override
	public List<FlDto> queryViewList(Map<String, Object> paraMap) throws Exception {
//		return flDao.queryViewList(paraMap);
		List<FlDto> res=null;
		Page p=queryFlList(paraMap);
		if(p!=null&&p.getList()!=null){
			res=p.getList();
		}
		return res;
	}

	@Override
	public List<FlDto> queryFlowBusiObjectList(Map<String, String> paramMap) {
		if(paramMap.containsKey("userId") && paramMap.get("userId") != null ){
			return flDao.queryUserFlowBusiObjectList(paramMap);
		}else{
			return flDao.queryFlowBusiObjectList(paramMap);
		}
	}
	@Override
	public Map<String, String>  getFlowRetractForInstance(Map<String, String> map){
		return flDao.getFlowRetractForInstance(map);
	}

	@Override
	public List<String> queryFlByApprover(String userId) {
		List<String> posts = queryPostBy(userId);
		List<String> roles = queryRoleBy(userId);
		List<String> params = new ArrayList<String>();
		params.add(userId);
		params.addAll(posts);
		params.addAll(roles);
		return flDao.queryFlListBy(params);
	}

	@Override
	public List<String> queryPostBy(String userId) {
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userInfo = JacksonUtils.toJson(userBeanInfo);
		Map<String, Object> paramater = new HashMap<String, Object>();
		paramater.put("userId", userId);
		paramater.put("searchType", "Post");
		String dubboResultInfo;
		try {
			dubboResultInfo = orgnazationDtoServiceCustomer.getUserRPOMInfoByUserId(userInfo, JacksonUtils.toJson(paramater));
		} catch (Exception e) {
			throw new FlowException("调用权限接口查询指定人员异常", e);
		}
		DubboServiceResultInfo resultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		List<String> postIds = new ArrayList<String>();
		if(resultInfo.isSucess()) {
			AuthenticationDto authenticationDto = JacksonUtils.fromJson(resultInfo.getResult(), AuthenticationDto.class);
			List<com.xinleju.platform.sys.org.dto.PostDto> postDtoList = authenticationDto.getPostDtoList();
			for(com.xinleju.platform.sys.org.dto.PostDto post : postDtoList) {
				postIds.add(post.getId());
			}
		}
		return postIds;
	}

	@Override
	public List<String> queryRoleBy(String userId) {
		SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		String userInfo = JacksonUtils.toJson(userBeanInfo);
		Map<String, Object> paramater = new HashMap<String, Object>();
		paramater.put("userId", userId);
		paramater.put("searchType", "Role");
		String dubboResultInfo;
		try {
			dubboResultInfo = orgnazationDtoServiceCustomer.getUserRPOMInfoByUserId(userInfo, JacksonUtils.toJson(paramater));
		} catch (Exception e) {
			throw new FlowException("调用权限接口查询指定人员角色异常", e);
		}
		DubboServiceResultInfo resultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		List<String> roleIds = new ArrayList<String>();
		if(resultInfo.isSucess()) {
			AuthenticationDto authenticationDto = JacksonUtils.fromJson(resultInfo.getResult(), AuthenticationDto.class);
			List<StandardRoleDto> roleDtoList = authenticationDto.getStandardRoleDtoList();
			for(StandardRoleDto role : roleDtoList) {
				roleIds.add(role.getId());
			}
		}
		return roleIds;
	}
	
	/**
	 * 变量类型适配
	 * 
	 * @param variables
	 * @param value
	 */
	private Map<String, Object> variableTypeAdapter(List<BusinessObjectVariableDto> variables, Map<String, Object> value) {
		for (String key : value.keySet()) {
			for (BusinessObjectVariableDto variableDto : variables) {
				if (key.equals(variableDto.getCode())) {
					Object object = value.get(key);
					if(object instanceof Double) {
						value.put(key, ((Double) object).doubleValue());
						continue;	
					}
					if(object instanceof Integer) {
						value.put(key, ((Integer) object).intValue());
						continue;	
					}
					if(object instanceof Date) {
						value.put(key, object);
						continue;	
					}
					if(object instanceof Float) {
						value.put(key, object);
						continue;	
					}
					if(object == null || StringUtils.isEmpty((String)object)) {
						continue;
					}
					try {
						// 变量类型: 1:字符串，2:整数，3:浮点数,4:布尔，5:日期，6:日期(带秒)
						if (variableDto.getType().equals("1")) {
							value.put(key, (String) object);
						} else if (variableDto.getType().equals("2")) {
//							value.put(key, Integer.parseInt((String) object));
							//去掉字符串中的逗号
							value.put(key, Integer.parseInt(object.toString().replace(",", "")));
						} else if (variableDto.getType().equals("3")) {
//							value.put(key, Float.parseFloat((String) object));
							value.put(key, Float.parseFloat(object.toString().replace(",", "")));
						} else if (variableDto.getType().equals("4")) {
							value.put(key, Boolean.parseBoolean((String) object));
						} else if (variableDto.getType().equals("5")) {
							//value.put(key, DateUtils.parseDate((String) object, "yyyy-MM-dd HH:mm"));
							//因为传进来的是字符串,所以不需要在转换
							value.put(key, (String) object);
						} else if (variableDto.getType().equals("6")) {
							//value.put(key, DateUtils.parseDate((String) object, "yyyy-MM-dd HH:mm:ss"));
							//因为传进来的是字符串,所以不需要在转换
							value.put(key, (String) object);
						}
					} catch (Exception e) {
						throw new FlowException(
								"流程发起过程中，业务变量类型转换异常：type=" + variableDto.getType() + ", value=" + object, e);
					}
				}
			}
		}
		return value;
	}
	
	/**
	 * 根据流程模板编号字符串来逻辑删除对应的流程模板的所有版本
	 * @param codeText
	 * @return
	 */
	@Override
	public int deleteFlowsByCodeText(String codeText) throws Exception {
		if(codeText!=null && codeText.length()>0){
			String codes[] = codeText.split(",");
			return flDao.deleteFlowsByCodeText(codes);
		}
		return 0;
	}

	@Override
	public List<Map<String, String>> queryFlowTemplateBy(String businessCode) {
		return flDao.queryFlowTemplateBy(businessCode);
	}
	
	public void setLoginInfo(String token, String userInfo, String menuInfo) {
		long endTime = Calendar.getInstance().getTime().getTime();
		redisTemplate.opsForValue().set(SecurityUserBeanInfo.TOKEN_TEND_USER + token, userInfo, endTime, TimeUnit.MILLISECONDS);
		redisTemplate.opsForValue().set(SecurityUserBeanRelationInfo.TOKEN_TEND_USER_MENU + token, menuInfo, endTime, TimeUnit.MILLISECONDS);
		log.info("往redis中设置当前用户信息：token=" + token + ", userInfo" + userInfo + ", menuInfo" + menuInfo);

	}

	@Override
	public Boolean setDefaultFl(Map<String, Object> paramMap) throws Exception {
		Boolean udpateSuccess = true;
		String defaultFlId = (String) paramMap.get("defaultFlId");
		String businessObjectId = (String) paramMap.get("businessObjectId");
		Map<String,Object> flParamMap = new HashMap<String,Object>();
		flParamMap.put("businessObjectId",businessObjectId);
		List<Fl> flList = flDao.queryList(flParamMap);
		for (Fl fl:flList) {
			String flId = fl.getId();
			fl.setIsDefualt(false);
			if(flId.equals(defaultFlId)){
				fl.setIsDefualt(true);
			}

			fl.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		}
		//更新所有模板
		int updateCount = flDao.updateBatch(flList);
		return udpateSuccess;
	}
	public Integer updateFlowsByids(Map<String,Object> paramMap) throws Exception{
		return flDao.updateFlowsByids(paramMap);
	}
}