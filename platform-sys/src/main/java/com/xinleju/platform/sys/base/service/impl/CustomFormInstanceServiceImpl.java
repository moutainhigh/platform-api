package com.xinleju.platform.sys.base.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.AttachmentDto;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;
import com.xinleju.platform.flow.dao.InstanceDao;
import com.xinleju.platform.flow.dao.MobileFormDao;
import com.xinleju.platform.flow.entity.Instance;
import com.xinleju.platform.flow.entity.MobileForm;
import com.xinleju.platform.flow.utils.DateUtils;
import com.xinleju.platform.out.app.org.entity.UserAuthDataOrgList;
import com.xinleju.platform.sys.base.dao.BaseCorporationDao;
import com.xinleju.platform.sys.base.dao.CommonDraftDao;
import com.xinleju.platform.sys.base.dao.CustomFormDao;
import com.xinleju.platform.sys.base.dao.CustomFormGroupDao;
import com.xinleju.platform.sys.base.dao.CustomFormInstanceDao;
import com.xinleju.platform.sys.base.dao.CustomFormVersionHistoryDao;
import com.xinleju.platform.sys.base.dto.CustomFormInstanceDto;
import com.xinleju.platform.sys.base.dto.CustomFormMobileConvert;
import com.xinleju.platform.sys.base.dto.ExternalDataDTO;
import com.xinleju.platform.sys.base.dto.ExternalInfo;
import com.xinleju.platform.sys.base.dto.MD5;
import com.xinleju.platform.sys.base.dto.PayRegistDTO;
import com.xinleju.platform.sys.base.dto.XMLUtil;
import com.xinleju.platform.sys.base.entity.BaseCorporation;
import com.xinleju.platform.sys.base.entity.CommonDraft;
import com.xinleju.platform.sys.base.entity.CustomForm;
import com.xinleju.platform.sys.base.entity.CustomFormGroup;
import com.xinleju.platform.sys.base.entity.CustomFormInstance;
import com.xinleju.platform.sys.base.entity.CustomFormVersionHistory;
import com.xinleju.platform.sys.base.service.CustomFormInstanceService;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import com.xinleju.platform.sys.org.service.OrgnazationService;
import com.xinleju.platform.sys.res.dao.AppSystemDao;
import com.xinleju.platform.sys.res.entity.AppSystem;
import com.xinleju.platform.sys.security.dto.AuthenticationDto;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentDtoServiceCustomer;
import com.xinleju.platform.utils.Common;

/**
 * @author admin
 * 
 * 
 */

@Service
public class CustomFormInstanceServiceImpl extends  BaseServiceImpl<String,CustomFormInstance> implements CustomFormInstanceService{
	private static Logger log = Logger.getLogger(CustomFormInstanceServiceImpl.class);

	@Autowired
	private CustomFormInstanceDao customFormInstanceDao;

	@Autowired
	private CustomFormDao customFormDao;

	@Autowired
	private BaseCorporationDao baseCorporationDao;
	
	@Autowired
	private AppSystemDao appSystemDao;

	@Autowired
	private InstanceDao instanceDao;
	
	@Autowired
	private OrgnazationService orgnazationService;

	@Autowired
	private CustomFormVersionHistoryDao customFormVersionHistoryDao;

	@Autowired
	private CommonDraftDao commonDraftDao;
	
	@Autowired
	private AttachmentDtoServiceCustomer attachmentDtoServiceCustomer;

	@Autowired
	private MobileFormDao mobileFormDao;

	@Autowired
	private CustomFormGroupDao customFormGroupDao;
	
	@Value("#{configuration['deploy.path']}")
	private String deployPath;

	@Value("#{configuration['endpoint']}")
	private String endpoint;
	
	@Override
	public Page getPageForForm(String userInfo,Map<String, Object> map) {
		SecurityUserDto securityUserDto = LoginUtils.getSecurityUserBeanInfo().getSecurityUserDto();
		//organizationType=company,dept,branch,group,zb
		String organizationType=LoginUtils.getSecurityUserBeanInfo().getSecurityOrganizationType();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		if(map!=null && map.get("customFormId")!=null){
			String customFormId=(String) map.get("customFormId");
			CustomForm customForm=customFormDao.getObjectById(customFormId);
			String sql=" and ( create_person_id = '"+securityUserDto.getId()+ "' or operator_id = '"+securityUserDto.getId()+"')";
			if(customForm.getDataItemControl()!=null){
				if(customForm.getDataItemControl()==1){//不启用数据权限
					//没有授权取经办人或制单人为当前登录人的表单实例数据
					map.put("dataAuthority", sql);
				}else if(customForm.getDataItemControl()==2 || customForm.getDataItemControl()==3){//使用默认数据权限
					paramMap.put("userIds", securityUserDto.getId());
					paramMap.put("appCode", "OA");
					paramMap.put("itemCode", customForm.getDataItemId());
					try {
						Map<String, UserAuthDataOrgList> orgResultMap = orgnazationService.getUserDataAuthCoAndDeptList(paramMap);
						UserAuthDataOrgList orgAuth=orgResultMap.get(securityUserDto.getId());
						String deptSql=getGrantSql("dept",orgAuth);
						log.info("##deptSql=="+deptSql);
						//人在公司或部门下
						if("and".equals(deptSql.trim())){
							//没有对角色进行授权
							map.put("dataAuthority", sql);
						}else{
							map.put("dataAuthority", deptSql);
						}
					} catch (Exception e) {
						log.error("数据权限过滤失败!"+"参数：userIds="+securityUserDto.getId()+",appCode=oa,itemCode="+customForm.getDataItemId()+","+"异常信息："+e);
						map.put("dataAuthority", sql);
					}
				}else{
					//没有授权取经办人或制单人为当前登录人的表单实例数据
					map.put("dataAuthority", sql);
				}
			}else{
				//没有授权取经办人或制单人为当前登录人的表单实例数据
				map.put("dataAuthority", sql);
			}
//			if(customForm.getDataItemId()==null || "".equals(customForm.getDataItemId())){
//				//没有授权取经办人或制单人为当前登录人的表单实例数据
//				map.put("dataAuthority", sql);
//			}else{
//				paramMap.put("userIds", securityUserDto.getId());
//				paramMap.put("appCode", "OA");
//				paramMap.put("itemCode", customForm.getDataItemId());
//				try {
////					if("company".equals(organizationType) || "dept".equals(organizationType)){
//						Map<String, UserAuthDataOrgList> orgResultMap = orgnazationService.getUserDataAuthCoAndDeptList(paramMap);
//						UserAuthDataOrgList orgAuth=orgResultMap.get(securityUserDto.getId());
//						String deptSql=getGrantSql("dept",orgAuth);
//						log.info("##deptSql=="+deptSql);
//						//人在公司或部门下
//						if("and".equals(deptSql.trim())){
//							//没有对角色进行授权
//							map.put("dataAuthority", sql);
//						}else{
//							map.put("dataAuthority", deptSql);
//						}
////					}else if("group".equals(organizationType) || "branch".equals(organizationType)){
////						Map<String,UserAuthDataOrgList> proResultMap = orgnazationService.getUserDataAuthGroupAndBranchList(paramMap);
////						UserAuthDataOrgList proAuth=proResultMap.get(securityUserDto.getId());
////						String projectSql=getGrantSql("branch",proAuth);
////						log.info("##projectSql=="+projectSql);
////						log.error("##projectSql=="+projectSql);
////						//人在项目下
////						if("and".equals(projectSql.trim())){
////							//没有对角色进行授权
////							map.put("dataAuthority", sql);
////						}else{
////							map.put("dataAuthority", projectSql);
////						}
////					}
//				} catch (Exception e) {
//					log.error("数据权限过滤失败!"+"参数：userIds="+securityUserDto.getId()+",appCode=oa,itemCode="+customForm.getDataItemId()+","+"异常信息："+e);
//					map.put("dataAuthority", sql);
//				}
//			}
		}
		// 分页显示
		Page page=new Page();
		// 获取分页list 数据
		List<Map<String,Object>> list = customFormInstanceDao.getPageSort(map);
		// 获取条件的总数据量
		Integer count = customFormInstanceDao.getPageSortCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		//封装成page对象 传到前台
		return page;
	}

	/**
	  * @Description:获取授权sql
	  * @author:zhangfangzhi
	  * @date 2017年7月18日 下午3:42:04
	  * @version V1.0
	 */
	private String getGrantSql(String type, UserAuthDataOrgList userAuthDataOrg) {
		String sqlStr=" and ";
		int index = 0;
		//公司
		if("company".equals(type)){
			List<OrgnazationDto> companyList=userAuthDataOrg.getCompanyList();
			for(int i=0;i<companyList.size();i++){
				OrgnazationDto orgnazationDto=companyList.get(i);
				if(index == 0){
	                sqlStr += " ( operate_company_id = '" + orgnazationDto.getId() +"'";
	            }else{
	                sqlStr += " or operate_company_id = '" + orgnazationDto.getId() +"'";
	            }
	            if(companyList.size() - 1 == index){
	                sqlStr += " ) ";
	            }
	            index ++;
			}
		}else if("dept".equals(type)){//部门
			List<OrgnazationDto> deptList=userAuthDataOrg.getDeptList();
			for(int i=0;i<deptList.size();i++){
				OrgnazationDto orgnazationDto=deptList.get(i);
				if(index == 0){
	                sqlStr += " ( operate_department_id = '" + orgnazationDto.getId() +"'";
	            }else{
	                sqlStr += " or operate_department_id = '" + orgnazationDto.getId() +"'";
	            }
	            if(deptList.size() - 1 == index){
	                sqlStr += " ) ";
	            }
	            index ++;
			}
		}else if("project".equals(type)){//项目
			List<OrgnazationDto> groupList=userAuthDataOrg.getGroupList();
			for(int i=0;i<groupList.size();i++){
				OrgnazationDto orgnazationDto=groupList.get(i);
				if(index == 0){
	                sqlStr += " ( operate_project_id = '" + orgnazationDto.getId() +"'";
	            }else{
	                sqlStr += " or operate_project_id = '" + orgnazationDto.getId() +"'";
	            }
	            if(groupList.size() - 1 == index){
	                sqlStr += " ) ";
	            }
	            index ++;
			}
		}else if("branch".equals(type)){//分期
			List<OrgnazationDto> branchList=userAuthDataOrg.getBranchList();
			for(int i=0;i<branchList.size();i++){
				OrgnazationDto orgnazationDto=branchList.get(i);
				if(index == 0){
	                sqlStr += " ( operate_qi_id = '" + orgnazationDto.getId() +"'";
	            }else{
	                sqlStr += " or operate_qi_id = '" + orgnazationDto.getId() +"'";
	            }
	            if(branchList.size() - 1 == index){
	                sqlStr += " ) ";
	            }
	            index ++;
			}
		}
		return sqlStr;
	}

	@Override
	public String getInstanceByFormId(String userInfo, String jsonStr) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			SecurityUserBeanInfo securityUserBeanInfo = JacksonUtils.fromJson(userInfo, SecurityUserBeanInfo.class);
			
			CustomFormInstanceDto customFormInstanceDto = new CustomFormInstanceDto();
			CustomFormInstance customFormInstanceBack=new CustomFormInstance();
			CustomFormInstance customFormInstance=JacksonUtils.fromJson(jsonStr, CustomFormInstance.class);
			CustomForm customForm=customFormDao.getObjectById(customFormInstance.getCustomFormId());
			if(customForm!=null){
				customFormInstanceBack.setFormFormatJson(customForm.getFormFormatJson());
				
				customFormInstance = customFormInstanceDao.getObjectById(customFormInstance.getId());
				if(customFormInstance==null){
					customFormInstance=customFormInstanceBack;
				}else{
					if(customFormInstance.getCustomFormHisId()!=null){
						CustomFormVersionHistory customFormVersionHistory=customFormVersionHistoryDao.getObjectById(customFormInstance.getCustomFormHisId());
						if(customFormVersionHistory!=null){
							customFormInstance.setFormFormatJson(customFormVersionHistory.getFormFormatJson());
						}
					}else{
						customFormInstance.setFormFormatJson(customFormInstanceBack.getFormFormatJson());
					}
				}
			}
			BeanUtils.copyProperties(customFormInstance, customFormInstanceDto);
			if(customForm!=null){
				customFormInstanceDto.setCode(customForm.getCode());
				customFormInstanceDto.setFlowVariable(customForm.getFlowVariable());
				if(customForm.getBusinessType()!=null){
					customFormInstanceDto.setVbusinesstype(this.getBusinessTypeName(customForm.getBusinessType()));
				}
			}
			customFormInstanceDto.setUpdatePersonId(securityUserBeanInfo.getSecurityUserDto().getId());
			customFormInstanceDto.setUpdatePersonName(securityUserBeanInfo.getSecurityUserDto().getRealName());
			customFormInstanceDto.setUpdateDate(new Timestamp(System.currentTimeMillis()));
			
			//查询系统appid
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("delflag", false);
			map.put("code", "OA");
			List<AppSystem> appList=appSystemDao.queryList(map);
			String appId=(appList!=null && appList.size()>0)?appList.get(0).getId():null;
			customFormInstanceDto.setAppId(appId);
			customFormInstanceDto.setUserType(securityUserBeanInfo.getSecurityUserDto().getType());
			//新增表单时经办人经办部门取当前登陆人信息
			if(customFormInstanceDto.getId()==null || "".equals(customFormInstanceDto.getId())){
				customFormInstanceDto.setOperatorId(securityUserBeanInfo.getSecurityUserDto().getId());
				customFormInstanceDto.setOperatorName(securityUserBeanInfo.getSecurityUserDto().getRealName());
				customFormInstanceDto.setOperateDepartmentId(securityUserBeanInfo.getSecurityDirectDeptDto()!=null?securityUserBeanInfo.getSecurityDirectDeptDto().getId():null);
				customFormInstanceDto.setOperateDepartmentName(securityUserBeanInfo.getSecurityDirectDeptDto()!=null?securityUserBeanInfo.getSecurityDirectDeptDto().getName():null);
				customFormInstanceDto.setOperateCompanyId(securityUserBeanInfo.getSecurityDirectCompanyDto()!=null?securityUserBeanInfo.getSecurityDirectCompanyDto().getId():null);
				customFormInstanceDto.setOperateCompanyName(securityUserBeanInfo.getSecurityDirectCompanyDto()!=null?securityUserBeanInfo.getSecurityDirectCompanyDto().getName():null);
				customFormInstanceDto.setOperateProjectId(securityUserBeanInfo.getSecurityGroupDto()!=null?securityUserBeanInfo.getSecurityGroupDto().getId():null);
				customFormInstanceDto.setOperateProjectName(securityUserBeanInfo.getSecurityGroupDto()!=null?securityUserBeanInfo.getSecurityGroupDto().getName():null);
				customFormInstanceDto.setOperateQiId(securityUserBeanInfo.getSecurityBranchDto()!=null?securityUserBeanInfo.getSecurityBranchDto().getId():null);
				customFormInstanceDto.setOperateQiName(securityUserBeanInfo.getSecurityBranchDto()!=null?securityUserBeanInfo.getSecurityBranchDto().getName():null);
				
				//设置制单人、制单时间
				customFormInstanceDto.setCreatePersonId(securityUserBeanInfo.getSecurityUserDto().getId());
				customFormInstanceDto.setCreatePersonName(securityUserBeanInfo.getSecurityUserDto().getRealName());
				customFormInstanceDto.setCreateDate(new Timestamp(System.currentTimeMillis()));
				customFormInstanceDto.setUpdatePersonId(null);
				customFormInstanceDto.setUpdatePersonName(null);
				customFormInstanceDto.setUpdateDate(null);
				
			}
			info.setResult(JacksonUtils.toJson(customFormInstanceDto));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	/**
	  * @Description:业务种类
	  * @author:zhangfangzhi
	  * @date 2017年11月21日 下午2:22:46
	  * @version V1.0
	 */
	private String getBusinessTypeName(String cellvalue) {
		if ("1".equals(cellvalue)) {
			return "外部结算-有无合同付款";
		} else if("2".equals(cellvalue)){
			return "外部结算-退工程保证金";
		} else if("3".equals(cellvalue)){
			return "外部结算-销售类退款";
		} else if("4".equals(cellvalue)){
			return "外部结算-报销(含工资)";
		} else if("5".equals(cellvalue)){
			return "外部借款";
		} else if("6".equals(cellvalue)){
			return "员工结算-报销";
		} else if("7".equals(cellvalue)){
			return "员工结算-借款";
		} else if("8".equals(cellvalue)){
			return "资金调拨";
		} else if("9".equals(cellvalue)){
			return "退质保金";
		} else{
			return "";
		}
	}

	@Override
	public String getVariableById(String userInfo, String idJson) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		SecurityUserBeanInfo securityUserBeanInfo = JacksonUtils.fromJson(userInfo, SecurityUserBeanInfo.class);
		try {
			CustomFormInstanceDto customFormInstanceDto=JacksonUtils.fromJson(idJson, CustomFormInstanceDto.class);
			CustomFormInstance	result = customFormInstanceDao.getObjectById(customFormInstanceDto.getBusinessId());
			Map<String, Object> map=JacksonUtils.fromJson(result.getFormFlowVariableValue(), HashMap.class);
			
			//移动表单处理
			Map<String, Object> paramMap = new HashMap<String, Object>();
			List<Map<String, Object>> mobileArrachmentValueList = new ArrayList<Map<String, Object>>();
			Map<String, List<Map<String, Object>>> mobileFormat=new HashMap<String, List<Map<String, Object>>>();
			List<Map<String, Object>> mobileFormValueList=JacksonUtils.fromJson(result.getFormMobileValueJson(), ArrayList.class);
			if(mobileFormValueList!=null && mobileFormValueList.size()>0){
				for(int i=0;i<mobileFormValueList.size();i++){
					Map<String, Object> remap=mobileFormValueList.get(i);
					if(remap!=null){
						remap.remove("type");
					}
					if(deployPath!=null){
						String keyName=(String) remap.get("name");
						if("经办人".equals(keyName)){
							remap.put("name", "operator");
						}else if("经办公司".equals(keyName)){
							remap.put("name", "operator-co");
						}else if("经办部门".equals(keyName)){
							remap.put("name", "operator-dept");
						}else if("经办项目".equals(keyName)){
							remap.put("name", "operator-pro");
						}else if("经办分期".equals(keyName)){
							remap.put("name", "operator-project-stage");
						}
					}
				}
			}
			mobileFormat.put("dataList", mobileFormValueList);
			
			//移动表单附件处理
			Map<String, Object> resultArrachMap = new HashMap<String, Object>();
			String[] stringArray = {customFormInstanceDto.getBusinessId()};  
			paramMap.put("businessId",stringArray);
    		String dubboResultInfo = attachmentDtoServiceCustomer.queryListByObject(userInfo, JacksonUtils.toJson(paramMap));
    		DubboServiceResultInfo dubboServiceResultInfo = JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
            if (dubboServiceResultInfo.isSucess()) {
                String resultInfo = dubboServiceResultInfo.getResult();
                List<AttachmentDto> list = JacksonUtils.fromJson(resultInfo, ArrayList.class, AttachmentDto.class);
                if(list.size() > 0){
                 	for (AttachmentDto attachmentDto : list) {
                 		resultArrachMap = new HashMap<String, Object>();
                 		resultArrachMap.put("filename", attachmentDto.getFullName());
                 		resultArrachMap.put("url", attachmentDto.getUrl());
                 		mobileArrachmentValueList.add(resultArrachMap);
					}
                 	mobileFormat.put("uploadEntityList",mobileArrachmentValueList);
                }
            }
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
			this.dealFlowBusiness(dataMap,securityUserBeanInfo,result);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("flow_phone_data", mobileFormat);
			if(map!=null && map.size()>0){
				dataMap.putAll(map);
			}
			resultMap.put("flow_business_variable_data", dataMap);
			resultMap.put("flow_business_data", mobileFormat);
			
			info.setResult(JacksonUtils.toJson(resultMap));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	/**
	  * @Description:处理流程变量取值
	  * @author:zhangfangzhi
	  * @date 2017年4月20日 下午3:48:22
	  * @version V1.0
	 */
	private void dealFlowBusiness(Map<String, Object> dataMap, SecurityUserBeanInfo securityUserBeanInfo,CustomFormInstance customFormInstance) {
		Map<String,Object> mapParam = new HashMap<String,Object>();
		try {
			if(customFormInstance!=null){
//				if (customFormInstance.getOperateDepartmentId() != null) {
//					mapParam.put("post_orgId", customFormInstance.getOperateDepartmentId());
//				}else {
//					mapParam.put("post_orgId", securityUserBeanInfo.getSecurityDirectDeptDto().getId());
//				}
//				mapParam.put("searchType", "Org");
//				AuthenticationDto authenticationDto = orgnazationService.getUserRPOMInfoByUserId(mapParam);
//				if(authenticationDto!=null){
//					dealForm(dataMap, authenticationDto);
//				}
				//修改获取流程所需的经办部门公司项目分期数据的取数逻辑20170524zfz
				dataMap.put("flow_business_company_id", customFormInstance.getOperateCompanyId());
				dataMap.put("flow_business_company_name", customFormInstance.getOperateCompanyName());
				dataMap.put("flow_business_dept_id", customFormInstance.getOperateDepartmentId());
				dataMap.put("flow_business_dept_name", customFormInstance.getOperateDepartmentName());
				dataMap.put("flow_business_project_id", customFormInstance.getOperateProjectId());
				dataMap.put("flow_business_project_name", customFormInstance.getOperateProjectName());
				dataMap.put("flow_business_project_branch_id", customFormInstance.getOperateQiId());
				dataMap.put("flow_business_project_branch_name", customFormInstance.getOperateQiName());
				
				if(customFormInstance.getOperatorId()!=null && !"".equals(customFormInstance.getOperatorId())){
					dataMap.put("start_user_id", customFormInstance.getOperatorId());
					dataMap.put("start_user_name", customFormInstance.getOperatorName());
				}else{
					dataMap.put("start_user_id", securityUserBeanInfo.getSecurityUserDto().getId());
					dataMap.put("start_user_name", securityUserBeanInfo.getSecurityUserDto().getRealName());
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	/**
	  * @Description:变量赋值
	  * @author:zhangfangzhi
	  * @date 2017年5月2日 上午10:12:59
	  * @version V1.0
	 */
	private void dealForm(Map<String, Object> dataMap, AuthenticationDto authenticationDto) {
		dataMap.put("flow_business_company_id", authenticationDto.getDirectCompanyDto()!=null?authenticationDto.getDirectCompanyDto().getId():null);
		dataMap.put("flow_business_company_name", authenticationDto.getDirectCompanyDto()!=null?authenticationDto.getDirectCompanyDto().getName():null);
		dataMap.put("flow_business_dept_id", authenticationDto.getDirectDeptDto()!=null?authenticationDto.getDirectDeptDto().getId():null);
		dataMap.put("flow_business_dept_name", authenticationDto.getDirectDeptDto()!=null?authenticationDto.getDirectDeptDto().getName():null);
		dataMap.put("flow_business_project_id", authenticationDto.getGroupDto()!=null?authenticationDto.getGroupDto().getId():null);
		dataMap.put("flow_business_project_name", authenticationDto.getGroupDto()!=null?authenticationDto.getGroupDto().getName():null);
		dataMap.put("flow_business_project_branch_id", authenticationDto.getBranchDto()!=null?authenticationDto.getBranchDto().getId():null);
		dataMap.put("flow_business_project_branch_name", authenticationDto.getBranchDto()!=null?authenticationDto.getBranchDto().getName():null);
	}

	@Override
	public int updateStatus(CustomFormInstance customFormInstance) {
		return customFormInstanceDao.updateStatus(customFormInstance);
	}

	@Override
	public int updateStatusForCustomForm(String updateJson) {
		CustomFormInstance customFormInstance=new CustomFormInstance();
		Map<String,String> paramMap=JacksonUtils.fromJson(updateJson, HashMap.class);
		String id=paramMap.get("id");
		String status=paramMap.get("status");
		String instanceId=paramMap.get("instanceId");
		String endStatus=paramMap.get("endStatus");
		customFormInstance.setId(id);
		customFormInstance.setInstanceId(instanceId);
		
		if (status!=null && Common.SH_SHZ_STATE.equals(status)) {
			customFormInstance.setStatus(Common.SH_SHZ_STATE);
        } else if (status!=null && Common.SH_SHWC_STATE.equals(status) && endStatus!=null && "1".equals(endStatus)) {
        	customFormInstance.setStatus(Common.SH_SHWC_STATE);
        } else {
        	customFormInstance.setStatus(Common.SH_WSH_STATE);
        }
		return customFormInstanceDao.updateStatus(customFormInstance);
	}

	@Override
	public Page getFundPageForForm(String userInfo, Map map) {
		SecurityUserDto securityUserDto = LoginUtils.getSecurityUserBeanInfo().getSecurityUserDto();
		//organizationType=company,dept,branch,group,zb
		String organizationType=LoginUtils.getSecurityUserBeanInfo().getSecurityOrganizationType();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("delflag", "0");
		paramMap.put("isEx", "1");
		List<CustomForm> customformList=customFormDao.queryList(paramMap);
		List<String> finalAuthIds=new ArrayList<String>();
		// 分页显示
		Page page=new Page();
		// 获取分页list 数据
		List<PayRegistDTO> payRegistDTOList=null;
		// 获取条件的总数据量
		Integer count = 0;
		if(customformList!=null && customformList.size()>0){
			String sql=" and ( create_person_id = '"+securityUserDto.getId()+ "' or operator_id = '"+securityUserDto.getId()+"')";
			for(int i=0;i<customformList.size();i++){
				CustomForm customForm=customformList.get(i);
				map.put("customFormId", customForm.getId());
				if(customForm.getDataItemControl()!=null){
					if(customForm.getDataItemControl()==1){//不启用数据权限
						//没有授权取经办人或制单人为当前登录人的表单实例数据
						map.put("dataAuthority", sql);
					}else if(customForm.getDataItemControl()==2 || customForm.getDataItemControl()==3){//使用默认数据权限
						paramMap.put("userIds", securityUserDto.getId());
						paramMap.put("appCode", "OA");
						paramMap.put("itemCode", customForm.getDataItemId());
						try {
							Map<String, UserAuthDataOrgList> orgResultMap = orgnazationService.getUserDataAuthCoAndDeptList(paramMap);
							UserAuthDataOrgList orgAuth=orgResultMap.get(securityUserDto.getId());
							String deptSql=getGrantSql("dept",orgAuth);
							log.info("##deptSql=="+deptSql);
							//人在公司或部门下
							if("and".equals(deptSql.trim())){
								//没有对角色进行授权
								map.put("dataAuthority", sql);
							}else{
								map.put("dataAuthority", deptSql);
							}
						} catch (Exception e) {
							log.error("数据权限过滤失败!"+"参数：userIds="+securityUserDto.getId()+",appCode=oa,itemCode="+customForm.getDataItemId()+","+"异常信息："+e);
							map.put("dataAuthority", sql);
						}
					}else{
						//没有授权取经办人或制单人为当前登录人的表单实例数据
						map.put("dataAuthority", sql);
					}
				}else{
					//没有授权取经办人或制单人为当前登录人的表单实例数据
					map.put("dataAuthority", sql);
				}
				finalAuthIds.addAll(customFormInstanceDao.getAuthsInstanceIds(map));
			}
			map.put("finalAuthIds",finalAuthIds);
			payRegistDTOList=this.customFormJsonToDto(customFormInstanceDao.getFundPageSort(map));
			count = customFormInstanceDao.getFundPageSortCount(map);
		}
		page.setLimit((Integer) map.get("limit") );
		page.setList(payRegistDTOList);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		//封装成page对象 传到前台
		return page;
	}

	private List<PayRegistDTO> customFormJsonToDto(List<CustomFormInstanceDto> list) {
		List<PayRegistDTO> payRegisteDtoList=new ArrayList<PayRegistDTO>();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				PayRegistDTO payRegistDTO=new PayRegistDTO();
				CustomFormInstanceDto customFormInstance=list.get(i);
				payRegistDTO.setId(customFormInstance.getId());
				payRegistDTO.setCustomFormId(customFormInstance.getCustomFormId());
				payRegistDTO.setVbusinesstype(customFormInstance.getVbusinesstype());
				payRegistDTO.setFormSearchSeniorKey(customFormInstance.getFormSearchSeniorKey());
				HashMap<String, Object> resMap = JSONObject.parseObject(customFormInstance.getFormValueJson(), HashMap.class);
				if(resMap != null && resMap.size() > 0){
	        		Iterator<Map.Entry<String, Object>> it = resMap.entrySet().iterator();
	        		while (it.hasNext()) {
	        			Map.Entry<String, Object> entry = it.next();
	        			Map<String,Object> map=(Map<String, Object>) entry.getValue();
	        			if(map!=null && map.get("rewriteFlag")!=null){
	        				String rewriteFlag=(String) map.get("rewriteFlag");
	        				switch (rewriteFlag) {
							case "importstatus"://导入状态 
								if(map.get("cmpValueShowName")==null || "".equals(map.get("cmpValueShowName"))){
	        						payRegistDTO.setImportstatus(0);
	        					}else{
	        						payRegistDTO.setImportstatus(Integer.valueOf(String.valueOf(map.get("cmpValueShowName"))));
	        					}
								break;
							case "paystatus"://支付状态 
								if(map.get("cmpValueShowName")==null || "".equals(map.get("cmpValueShowName"))){
	        						payRegistDTO.setPaystatus(0);
	        					}else{
	        						payRegistDTO.setPaystatus(Integer.valueOf(String.valueOf(map.get("cmpValueShowName"))));
	        					}
								break;
							case "vbusinesscode"://单据编号
								payRegistDTO.setVbusinesscode(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);				
								break;
							case "vtheme"://主题
								if(map.get("cmpValueShowName")!=null && !"".equals(map.get("cmpValueShowName"))){
									payRegistDTO.setVtheme(map.get("cmpValueShowName").toString());
								}
								break;
							case "napplymny"://申请金额
								if(map.get("cmpValueShowName")!=null && !"".equals(map.get("cmpValueShowName"))){
	        						payRegistDTO.setNapplymny(Double.valueOf(map.get("cmpValueShowName").toString()));
	        					}
								break;
							case "npaymny"://支付金额
								if(map.get("cmpValueShowName")!=null && !"".equals(map.get("cmpValueShowName"))){
	        						payRegistDTO.setNpaymny(Double.valueOf(map.get("cmpValueShowName").toString()));
	        					}
								break;
							case "dapplydate"://申请日期
								payRegistDTO.setDapplydate(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "dimportdate"://导入日期
								payRegistDTO.setDimportdate(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "dpaydate"://支付日期
								payRegistDTO.setDpaydate(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "paybankname"://付款银行
								payRegistDTO.setPaybankname(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "payaccountcode"://付款银行账号
								payRegistDTO.setPayaccountcode(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "payorgname"://付款单位名称
								payRegistDTO.setPayorgname(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								BaseCorporation baseCorporation=baseCorporationDao.getObjectById(map.get("cmpValue").toString());
								payRegistDTO.setPayorgcode(baseCorporation!=null?baseCorporation.getCode():map.get("cmpValue").toString());
								break;
							case "recorgcode"://收款单位编号
								payRegistDTO.setRecorgcode(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "recorgname"://收款单位名称
								payRegistDTO.setRecorgname(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "recaccountno"://收款账户编号
								payRegistDTO.setRecaccountno(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "recaccountname"://收款账户名称
								payRegistDTO.setRecaccountname(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "recbankname"://收款行名称
								payRegistDTO.setRecbankname(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "bankpaytype"://付款对象类型
								payRegistDTO.setBankpaytype(map.get("cmpValue")!=null?map.get("cmpValue").toString():null);
								break;
							case "recprovince"://收款行所在省
								payRegistDTO.setRecprovince(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "recareanameofcity"://收款行所在市
								payRegistDTO.setRecareanameofcity(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "paymenttype"://支付方式
								payRegistDTO.setPaymenttype(map.get("cmpValue")!=null?map.get("cmpValue").toString():null);
								break;
							case "bankexccodeofrec"://收款行联行号 
								payRegistDTO.setBankexccodeofrec(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "bankcodeofrec"://收款行机构号
								payRegistDTO.setBankcodeofrec(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							case "cnapsofrec"://收款行CNAPS号
								payRegistDTO.setCnapsofrec(map.get("cmpValueShowName")!=null?map.get("cmpValueShowName").toString():null);
								break;
							default:
								break;
							}
	        			}
	        		}
	        	}
				payRegisteDtoList.add(payRegistDTO);
			}
		}
		return payRegisteDtoList;
	}

	@Override
	public DubboServiceResultInfo importFundPayment(List<String> list) {
		DubboServiceResultInfo resInfo=new DubboServiceResultInfo();
		Map<String,Object> paramMap=new HashMap<String,Object>();
		paramMap.put("idLists",list);
		String errormsg=null;
		List<CustomFormInstanceDto> customFormInstanceList=customFormInstanceDao.queryListByParam(paramMap);
		List<PayRegistDTO> payRegistDtoList=this.customFormJsonToDto(customFormInstanceList);
		Map<String,PayRegistDTO> map = new HashMap<String,PayRegistDTO>();
		for(PayRegistDTO payregister:payRegistDtoList){
			payregister.setImportstatus(1);//0 未导入,1 已导入
			payregister.setDimportdate(DateUtils.formatDate(new Date()));
			map.put("CUSTOMFORM-"+payregister.getId(), payregister);
		}
		try {
			List<ExternalInfo> listExt = changeToExternal(payRegistDtoList,errormsg);
			if(listExt!=null && listExt.size()>0){
				if(errormsg==null){
					errormsg = "";
				}
				int errorsize = 0;
				for(ExternalInfo info:listExt){
					if(!"1".equals(info.getReceivestatus())){
						payRegistDtoList.remove(map.get(info.getBillcode()));
						errormsg+=info.getErrormsg()+"、";
						errorsize++;
					}
				}
				if(payRegistDtoList!=null && payRegistDtoList.size()>0){
					this.dealImportStatus(payRegistDtoList);
				}
				if(errorsize==0){
					resInfo.setResult(JacksonUtils.toJson(list.size()));
					resInfo.setSucess(true);
					resInfo.setMsg("导入成功!");
				}else{
					errormsg = errormsg.substring(0, errormsg.lastIndexOf("、"));
					resInfo.setResult(JacksonUtils.toJson(list.size()));
					resInfo.setSucess(false);
					resInfo.setMsg("导入失败"+errorsize+"条;失败原因:"+errormsg);
				}
			}else{
				resInfo.setSucess(false);
				resInfo.setMsg(errormsg);
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error(e.getMessage());
			resInfo.setSucess(false);
			resInfo.setMsg(e.getMessage());
		}
		return resInfo;
	}

	/**
	 * 导入资金平台的导入方法
	 * @param errormsg 
	 * @param id
	 * @return
	 */
	private List<ExternalInfo> changeToExternal(List<PayRegistDTO> list, String errormsg) {
		try {
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress( new java.net.URL(endpoint) );//为Call设置服务的位置
	        call.setOperationName(new QName("http://ws.externalbill.itms.iss.com/","importExternalData"));
	        call.addParameter(new QName("http://ws.externalbill.itms.iss.com/","arg0"),
	        		org.apache.axis.encoding.XMLType.XSD_STRING,
	        		javax.xml.rpc.ParameterMode.IN);// 接口的参数
	        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
	        List<ExternalDataDTO> dataList = new ArrayList<ExternalDataDTO>();
	        for(PayRegistDTO dto : list){
	        	CustomForm customForm=customFormDao.getObjectById(dto.getCustomFormId());
				ExternalDataDTO data = genExternal(dto);
				data.setBusinesstype(customForm.getBusinessType());
				if(customForm.getBusinessType()!=null){
					switch (customForm.getBusinessType()) {
					case "6":
					case "7":
						data.setTranstype("2");//业务类型  内部
						//针对通用付款根据付款对象类型再判断，公对公推到资金公对公下，公对私推到资金公对私下所以做如下特殊处理zfz
//						if("1".equals(dto.getBankpaytype())){
//							data.setBusinesstype("1");//业务种类置为合同付款
//							data.setTranstype("1");//业务类型置为外部
//						}
						break;
					default:
						data.setTranstype("1");//业务类型  外部
						break;
					}
				}
				data.setPayorgcode(dto.getPayorgcode());//付款单位编号
				data.setPayorgname(dto.getPayorgname());//付款单位名称
				data.setOrgcode(dto.getPayorgname());  //业务申请单位
				String vskunit = dto.getRecorgname();
	            if (vskunit.length() >= 15) {
	                vskunit = vskunit.substring(0,14);
	            }
				data.setRecorgcode(vskunit);//收款单位编码
				data.setRecorgname(dto.getRecorgname());//收款方单位名称
				data.setRecaccountno(dto.getRecaccountno());  //收款账户编号
				data.setRecaccountname(dto.getRecaccountname());//收款账户名称
				data.setRecbankname(dto.getRecbankname());  //收款行名称
				data.setBankpaytype(dto.getBankpaytype());//付款对象类型   1：公对公2：公对私
				data.setRecprovince(dto.getRecprovince()==null?"":dto.getRecprovince());//收款行所在省
				data.setRecareanameofcity(dto.getRecareanameofcity()==null?"":dto.getRecareanameofcity());//收款行所在市
				//设置MD5密文  业务单据编号+业务申请单位(付款单位)+收款单位编号+收款单位名称+收款账号+金额+业务类型(代码)+@
				String free1 = data.getBillcode().trim() +"" + (data.getOrgcode()==null?"":data.getOrgcode().trim()) +
						""+ (data.getRecorgcode()==null?"":data.getRecorgcode().trim()) +
						""+ (data.getRecorgname()==null?"":data.getRecorgname().trim()) + 
						""+ (data.getRecaccountno()==null?"":data.getRecaccountno().trim()) +
						""+ (data.getAmount()==null?"":data.getAmount()) +
						""+ data.getTranstype()+"@";
				System.out.println("加密前:"+free1);
				data.setFree1(MD5.toMD5(free1));
				System.out.println("加密后:"+MD5.toMD5(free1));
				dataList.add(data);
	        }
	        List<Object> listObj = new ArrayList<Object>();
	        for(ExternalDataDTO ext:dataList){
	        	listObj.add(ext);
	        }
			String inValue = XMLUtil.ObjToXML(listObj);
			call.addParameter(new QName("http://ws.externalbill.itms.iss.com/","return"),
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.OUT);// 接口的参数
			String result = (String) call.invoke(new Object[]{inValue}); // 给方法传递参数，并且调用方法
			List<ExternalInfo> listinfo = this.resolveXML1(result);
			return listinfo;
		} catch (Exception e) {
			errormsg = e.getMessage().lastIndexOf("Exception:")>0?(e.getMessage().substring(e.getMessage().lastIndexOf("Exception:")+11)):(e.getMessage());
			Logger.getLogger(this.getClass()).error("e:"+e.getMessage());
		}
		return null;
	}

	
	/**
	 * 导入资金平台公共字段赋值
	 * @param dto
	 * @return
	 */
	private ExternalDataDTO genExternal(PayRegistDTO dto) {
		ExternalDataDTO result = new ExternalDataDTO();
		
		//result.setBillcode(dto.getId()+"");   //业务单据编号 Y
		result.setBillcode("CUSTOMFORM-"+dto.getId());   //业务单据编号 Y
		result.setTransferdate(dto.getDapplydate()); //业务单据日期
		result.setDatasource("XLJEX"); //
		//result.setDatasource("MY"); //
		//result.setOrgcode(null);  //业务申请单位
		result.setIssplit("0"); //是否允许拆单
		result.setIsbatch("0");  //是否批量
		result.setOperator(dto.getVoperator()); //经办人
		result.setAbstracts(dto.getVapprovenote());
		result.setTranstype("1");//1：外部结算		2：员工结算  业务种类
		//result.setBankpaytype("2");//1：公对公		2：公对私 ,付款对象类型
		//result.setCnapsofrec("CNY");  //收款行CNAPS号
		result.setCurrencycode("CNY");//人民币：CNY		美元：USD ,结算币种
		result.setAmount(dto.getNapplymny());  //付款金额
		result.setPaymenttype("1");//1.电汇    2.现金   3.支票   4.代扣代缴 ,支付方式
		result.setAbstracts(dto.getVtheme());//摘要  N
		
		return result;
	}

	@Override
	public void synFund(Map map) {
		try {
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress( new java.net.URL(endpoint) );//为Call设置服务的位置queryDataStatus1
	        call.setOperationName(new QName("http://ws.externalbill.itms.iss.com/","queryDataStatus1"));
	        String result = (String) call.invoke(new Object[]{"XLJEX"});
			Logger.getLogger(this.getClass()).error("---------资金平台同步的报文Action-------:"+result);
			List<ExternalInfo> list = this.resolveXML1(result);
			this.synPay(list);
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("e:"+e.getMessage());
		}
	}
	
	private void synPay(List<ExternalInfo> list) {
		if(list==null || list.size()==0){
			return;
		}
		Logger.getLogger(this.getClass()).error("------------------进入刷新方法--------------");
		List<PayRegistDTO> listBack = covertDto(list);//返回主表list,明细list,明细对应主表code的Set
		dealHuixie(listBack);
	}
	
	/**
	  * @Description:处理导入状态
	  * @author:zhangfangzhi
	  * @date 2017年11月9日 下午5:51:53
	  * @version V1.0
	 */
	private void dealImportStatus(List<PayRegistDTO> list) {
		if(list==null || list.size()==0){
			return;
		}
		List<CustomFormInstance> customFormInstanceList = new ArrayList<CustomFormInstance>();
		for(PayRegistDTO payregist:list){
			CustomFormInstance customFormInstance=customFormInstanceDao.getObjectById(payregist.getId()); 
			if(customFormInstance!=null){
				Map<String,Map<String,Object>> resultFormJsonMap = JacksonUtils.fromJson(customFormInstance.getFormValueJson(), HashMap.class);
				if(resultFormJsonMap!=null && resultFormJsonMap.size()>0){
					for (Map<String,Object> reMap : resultFormJsonMap.values()) {
						String cmpName=(String) reMap.get("rewriteFlag");		
						if("importstatus".equals(cmpName)){//导入状态
							reMap.put("cmpValue", payregist.getImportstatus());
							reMap.put("cmpValueShowName", payregist.getImportstatus());
						}else if("dimportdate".equals(cmpName)){//导入日期
							reMap.put("cmpValue", payregist.getDimportdate());
							reMap.put("cmpValueShowName", payregist.getDimportdate());
						}
					}
					customFormInstance.setFormValueJson(JacksonUtils.toJson(resultFormJsonMap));
					String importDate="cmp_fundPayment_importDate_v1:"+payregist.getDimportdate()+":cmp_fundPayment_importDate_v1";
					customFormInstance.setFormSearchSeniorValue(customFormInstance.getFormSearchSeniorValue()+","+importDate);
					customFormInstanceList.add(customFormInstance);
				}
			}
		}
		
		try {
			if(customFormInstanceList!=null && customFormInstanceList.size()>0){
				customFormInstanceDao.updateBatch(customFormInstanceList);
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error(e.getMessage());
		}
	}
	
	/**
	  * @Description:资金平台状态回写
	  * @author:zhangfangzhi
	  * @date 2017年11月9日 下午5:51:14
	  * @version V1.0
	 */
	private void dealHuixie(List<PayRegistDTO> list) {
		if(list==null || list.size()==0){
			return;
		}
		List<CustomFormInstance> customFormInstanceList = new ArrayList<CustomFormInstance>();
		StringBuilder strbuild = new StringBuilder();
		for(PayRegistDTO payregist:list){
			strbuild.append(",").append(payregist.getPayformid());//资金平台唯一标识
			
			CustomFormInstance customFormInstance=customFormInstanceDao.getObjectById(payregist.getId()); 
			if(customFormInstance!=null){
				Map<String,Map<String,Object>> resultFormJsonMap = JacksonUtils.fromJson(customFormInstance.getFormValueJson(), HashMap.class);
				if(resultFormJsonMap!=null && resultFormJsonMap.size()>0){
					for (Map<String,Object> reMap : resultFormJsonMap.values()) {
						String cmpName=(String) reMap.get("rewriteFlag");		
						if(cmpName==null){
							continue;
						}
						if("paystatus".equals(cmpName)){//支付状态 
							reMap.put("cmpValue", payregist.getPaystatus());
							reMap.put("cmpValueShowName", payregist.getPaystatus());
						}else if("npaymny".equals(cmpName)){//支付金额
							reMap.put("cmpValue", payregist.getNpaymny());
							reMap.put("cmpValueShowName", payregist.getNpaymny());
						}else if("dpaydate".equals(cmpName)){//支付日期
							reMap.put("cmpValue", payregist.getDpaydate());
							reMap.put("cmpValueShowName", payregist.getDpaydate());
						}
//						else if("payaccountcode".equals(cmpName)){//付款银行账号
//							reMap.put("cmpValue", payregist.getPayaccountcode());
//							reMap.put("cmpValueShowName", payregist.getPayaccountcode());
//						}else if("payaccountname".equals(cmpName)){//付款账户名称
//							reMap.put("cmpValue", payregist.getPayaccountname());
//							reMap.put("cmpValueShowName", payregist.getPayaccountname());
//						}else if("paybankname".equals(cmpName)){//付款银行
//							reMap.put("cmpValue", payregist.getPaybankname());
//							reMap.put("cmpValueShowName", payregist.getPaybankname());
//						}else if("payorgcode".equals(cmpName)){//付款单位编号
//							reMap.put("cmpValue", payregist.getPayorgcode());
//							reMap.put("cmpValueShowName", payregist.getPayorgcode());
//						}else if("payorgname".equals(cmpName)){//付款单位名称
//							reMap.put("cmpValue", payregist.getPayorgname());
//							reMap.put("cmpValueShowName", payregist.getPayorgname());
//						}
						else if("importstatus".equals(cmpName)){//导入日期
							if(payregist.getImportstatus()!=null){
								reMap.put("cmpValue", payregist.getImportstatus());
								reMap.put("cmpValueShowName", payregist.getImportstatus());
							}
						}
					}
					customFormInstance.setPayformid(payregist.getPayformid());
					customFormInstance.setFormValueJson(JacksonUtils.toJson(resultFormJsonMap));
					String payDate="cmp_fundPayment_payDate_v1:"+payregist.getDpaydate()+":cmp_fundPayment_payDate_v1";
					customFormInstance.setFormSearchSeniorValue(customFormInstance.getFormSearchSeniorValue()+","+payDate);
					customFormInstanceList.add(customFormInstance);
				}
			}
		}
		
		try {
			if(customFormInstanceList!=null && customFormInstanceList.size()>0){
				customFormInstanceDao.updateBatch(customFormInstanceList);
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error(e.getMessage());
		}
		if(strbuild!=null && strbuild.indexOf(",")>=0){//调用第三个接口,回写支付完成的记录
			Logger.getLogger(this.getClass()).error("--------------------调用第三个接口-----------------");
			huixie(strbuild.substring(1));
			Logger.getLogger(this.getClass()).error("--------------------同步完成-----------------");
		}
	}
	
	/**
	 * 导入资金平台第三个接口,支付完成的回写
	 * @param ids
	 */
	public void huixie(String ids){
		try {
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress( new java.net.URL(endpoint) );//为Call设置服务的位置
	        call.setOperationName(new QName("http://ws.externalbill.itms.iss.com/","regResult"));
	        call.addParameter(new QName("http://ws.externalbill.itms.iss.com/","arg0"),
	        		org.apache.axis.encoding.XMLType.XSD_STRING,
	        		javax.xml.rpc.ParameterMode.IN);// 接口的参数
	        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_INT);
			Object obj = call.invoke(new Object[]{ids});
			System.out.print(obj);
			
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("e:"+e.getMessage());
		}
	}

	/**
	 * 资金平台同步返回结果封装
	 * @param listinfo
	 * @return
	 */
	public List<PayRegistDTO> covertDto(List<ExternalInfo> listinfo){
		List<PayRegistDTO> list = new ArrayList<PayRegistDTO>();//主单集合
		PayRegistDTO payregist ;//主单变量
		if(listinfo==null || listinfo.size()==0){
			return null;
		}
		for(ExternalInfo ext : listinfo){
			String businessId=ext.getBillcode();
			if(businessId.startsWith("CUSTOMFORM-")){
				businessId=businessId.substring(businessId.indexOf("-")+1);
			}else{
				businessId=customFormInstanceDao.queryBusinessIdByCode(businessId);
				if(businessId==null || "".equals(businessId)){
					continue;
				}
			}
			CustomFormInstance customFormInstance=customFormInstanceDao.getObjectById(businessId);
//			payregist = this.getIdByCode(ext.getBillcode());
			payregist=new PayRegistDTO();
			if(customFormInstance==null){
				continue;
			}else{
				payregist.setId(customFormInstance.getId());
			}
			payregist.setPayformid(ext.getPayformid());//主表维护资金平台唯一标识
			
			payregist.setPayaccountcode(ext.getPayaccountcode()); //付款银行账号
			payregist.setPayaccountname(ext.getPayaccountname()); //付款账户名称
			payregist.setPaybankname(ext.getPaybankname());       //付款银行
			payregist.setPayorgcode(ext.getPayorgcode());         //付款单位编号
			payregist.setPayorgname(ext.getPayorgname());         //付款单位名称
			
			if("2".equals(ext.getStatus())){//部分支付,代表拆单（注：自定义表单导入资金平台时默认不允许拆单）
				
			}else{//完全支付,或者撤回,代表不拆单
				if("1".equals(ext.getStatus())){//完成支付
					payregist.setNpaymny(ext.getAmount()); //回写支付金额
					payregist.setDpaydate(ext.getEnddate());  //支付日期
					payregist.setPaystatus(2); //支付状态
				}else{//撤回
					payregist.setPaystatus(3); //支付状态:撤回
					payregist.setImportstatus(0);//未导入
					payregist.setReverse1(ext.getEnddate());//Reverse1维护撤回日期
					System.out.println("------3------");
				}
				list.add(payregist);
			}
		}
		return list;
	}
	
	/**
	 * 结算方式转换
	 * @param type
	 * @return
	 */
	private String jiesuancovert(String type){
		if(type==null || "".equals(type)){
			return "";
		}
		if("1".equals(type)){
			return "电汇";
		}else if("2".equals(type)){
			return "现金";
		}else if("3".equals(type)){
			return "支票";
		}else if("4".equals(type)){
			return "代扣代缴";
		}else{
			return "电汇";
		}
	}

	/**
	 * 资金平台同步结果集转换为DTO
	 * @param xml1
	 * @return
	 */
	public List<ExternalInfo> resolveXML1(String xml1){
		List<ExternalInfo> list = new ArrayList<ExternalInfo>();
		String[] strarr = xml1.split("</R1>");
		ExternalInfo ext ;
		for(String xml:strarr){
			ext = new ExternalInfo();
			if(xml.contains("<BILLCODE>")){
				ext.setBillcode(xml.substring(xml.indexOf("<BILLCODE>")+"<BILLCODE>".length(), xml.indexOf("</BILLCODE>")));
			}
			if(xml.contains("<RECEIVESTATUS>")){
				ext.setReceivestatus(xml.substring(xml.indexOf("<RECEIVESTATUS>")+"<RECEIVESTATUS>".length(), xml.indexOf("</RECEIVESTATUS>")));
			}
			if(xml.contains("<ERRORMSG>")){
				ext.setErrormsg(xml.substring(xml.indexOf("<ERRORMSG>")+"<ERRORMSG>".length(), xml.indexOf("</ERRORMSG>")));
			}
			if(xml.contains("<PAYFORMID>")){
				ext.setPayformid(xml.substring(xml.indexOf("<PAYFORMID>")+"<PAYFORMID>".length(), xml.indexOf("</PAYFORMID>")));
			}
			if(xml.contains("<STATUS>")){
				ext.setStatus(xml.substring(xml.indexOf("<STATUS>")+"<STATUS>".length(), xml.indexOf("</STATUS>")));
			}
			if(xml.contains("<ENDDATE>")){
				ext.setEnddate(xml.substring(xml.indexOf("<ENDDATE>")+"<ENDDATE>".length(), xml.indexOf("</ENDDATE>")));
			}
			if(xml.contains("<AMOUNT>")){
				ext.setAmount(Double.valueOf(xml.substring(xml.indexOf("<AMOUNT>")+"<AMOUNT>".length(), xml.indexOf("</AMOUNT>"))));
			}
			if(xml.contains("<PAYMENTTYPE>")){
				ext.setPaymenttype(xml.substring(xml.indexOf("<PAYMENTTYPE>")+"<PAYMENTTYPE>".length(), xml.indexOf("</PAYMENTTYPE>")));
			}
			//waylonglong 2016.12.21 资金平台同步属性:付款单位编号、付款单位名称、付款银行账号、付款账户名称、付款银行
			if(xml.contains("<PAYORGCODE>")){
				ext.setPayorgcode(xml.substring(xml.indexOf("<PAYORGCODE>")+"<PAYORGCODE>".length(), xml.indexOf("</PAYORGCODE>")));
			}
			if(xml.contains("<PAYORGNAME>")){
				ext.setPayorgname(xml.substring(xml.indexOf("<PAYORGNAME>")+"<PAYORGNAME>".length(), xml.indexOf("</PAYORGNAME>")));
			}
			if(xml.contains("<PAYACCOUNTCODE>")){
				ext.setPayaccountcode(xml.substring(xml.indexOf("<PAYACCOUNTCODE>")+"<PAYACCOUNTCODE>".length(), xml.indexOf("</PAYACCOUNTCODE>")));
			}
			if(xml.contains("<PAYACCOUNTNAME>")){
				ext.setPayaccountname(xml.substring(xml.indexOf("<PAYACCOUNTNAME>")+"<PAYACCOUNTNAME>".length(), xml.indexOf("</PAYACCOUNTNAME>")));
			}
			if(xml.contains("<PAYBANKNAME>")){
				ext.setPaybankname(xml.substring(xml.indexOf("<PAYBANKNAME>")+"<PAYBANKNAME>".length(), xml.indexOf("</PAYBANKNAME>")));
			}
			if(ext.getBillcode()!=null && !"".equals(ext.getBillcode())){
				list.add(ext);
			}
		}
		return list;
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public int updateInstance(CustomFormInstanceDto customFormInstanceDto) {
		List<MobileForm> mobileForms = new ArrayList<MobileForm>();
		CustomFormInstance customFormInstance=new CustomFormInstance();
		BeanUtils.copyProperties(customFormInstanceDto, customFormInstance);
		customFormInstanceDao.update(customFormInstance);
		CommonDraft commonDraft=commonDraftDao.getObjectById(customFormInstance.getId());
		CustomForm customForm=customFormDao.getObjectById(customFormInstance.getCustomFormId());
		if(commonDraft!=null){
			BeanUtils.copyProperties(customFormInstanceDto, commonDraft);
			commonDraft.setContent(customFormInstance.getFormValueJson());
			commonDraft.setTitle(customFormInstance.getFormTitle());
			if(customForm!=null){
				commonDraft.setBusinessOjbectName(customForm.getName());
			}
			commonDraftDao.update(commonDraft);
		}
		if(customFormInstanceDto!=null && "2".equals(customFormInstanceDto.getUserType())){
			//管理员修订
			if(customFormInstance.getInstanceId()!=null && !"0".equals(customFormInstance.getStatus())){
				Instance instance=instanceDao.getObjectById(customFormInstance.getInstanceId());
				int sort = 1000;
				List<CustomFormMobileConvert> mobileFormValueList=JacksonUtils.fromJson(customFormInstance.getFormMobileValueJson(), ArrayList.class,CustomFormMobileConvert.class);
				if(mobileFormValueList!=null && mobileFormValueList.size()>0){
					for(int i=0;i<mobileFormValueList.size();i++){
						CustomFormMobileConvert customFormMobileConvert=mobileFormValueList.get(i);
						MobileForm mobileForm = new MobileForm();
						mobileForm.setId(IDGenerator.getUUID());
						mobileForm.setInstanceId(instance.getId());
						mobileForm.setFlId(instance.getFlId());
						mobileForm.setBusinessObjectId(instance.getBusinessObjectId());
						mobileForm.setBusinessId(instance.getBusinessId());
						mobileForm.setSort(sort++);
						mobileForm.setDelflag(false);
						if(deployPath!=null){//美国环境特殊处理
							if("经办人".equals(customFormMobileConvert.getName())){
								mobileForm.setName("operator");
							}else if("经办公司".equals(customFormMobileConvert.getName())){
								mobileForm.setName("operator-co");
							}else if("经办部门".equals(customFormMobileConvert.getName())){
								mobileForm.setName("operator-dept");
							}else if("经办项目".equals(customFormMobileConvert.getName())){
								mobileForm.setName("operator-pro");
							}else if("经办分期".equals(customFormMobileConvert.getName())){
								mobileForm.setName("operator-project-stage");
							}else{
								mobileForm.setName(customFormMobileConvert.getName());
							}
						}else{
							mobileForm.setName(customFormMobileConvert.getName());
						}
						mobileForm.setValue(customFormMobileConvert.getValue());
						mobileForms.add(mobileForm);
					}
				}
				if(mobileForms.size()>0){
					mobileFormDao.delMobileFormByBusinessId(instance.getBusinessId());
					mobileFormDao.saveBatch(mobileForms);
					log.info("管理员修订手机表单数据保存成功： size=" + mobileForms.size());	
				}
			}
		}
		return 0;
	}

	@Override
	public String isCustomformInstance(String id) {
		return customFormInstanceDao.isCustomformInstance(id);
	}

	@Override
	public Page getMyFormPage(String userInfo, Map map) {
		// 分页显示
		Page page=new Page();
		// 获取分页list 数据
		List<Map<String,Object>> list = customFormInstanceDao.getMyFormPageSort(map);
		// 获取条件的总数据量
		Integer count = customFormInstanceDao.getMyFormPageCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		//封装成page对象 传到前台
		return page;
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void saveInstance(CustomFormInstance customFormInstance) {
		customFormInstanceDao.save(customFormInstance);
		CommonDraft commonDraft=new CommonDraft();
		BeanUtils.copyProperties(customFormInstance, commonDraft);
		commonDraft.setBusinessType("1");
		commonDraft.setContent(customFormInstance.getFormValueJson());
		commonDraft.setTitle(customFormInstance.getFormTitle());
		commonDraft.setDelflag(false);
		commonDraft.setId(customFormInstance.getId());
		CustomForm customForm=customFormDao.getObjectById(customFormInstance.getCustomFormId());
		if(customForm!=null){
			commonDraft.setBusinessOjbectName(customForm.getName());
			commonDraft.setBackup2(customForm.getParentId());
			CustomFormGroup customFormGroup=customFormGroupDao.getObjectById(customForm.getParentId());
			if(customFormGroup!=null){
				commonDraft.setBackup1(customFormGroup.getParentId());
			}
		}
		commonDraftDao.save(commonDraft);
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public int deleteByIds(List<String> list) {
		customFormInstanceDao.deletePseudoAllObjectByIds(list);
		commonDraftDao.deletePseudoAllObjectByIds(list);
		return 1;
	}
}
