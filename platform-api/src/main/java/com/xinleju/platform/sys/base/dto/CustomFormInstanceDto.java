package com.xinleju.platform.sys.base.dto;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * @author admin
 * 
 *
 */
public class CustomFormInstanceDto extends BaseDto{

		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	//表单格式JSON
	private String formFormatJson;
    
  		
	//表单值JSON
	private String formValueJson;
    
  		
	//表单ID
	private String customFormId;
    
  		
	//查询字段
	private String formSearchKey;
    
	//表单流程变量
	private String formFlowVariableValue;
		
	//状态   0草稿  1审批中   2已审核
	private String status;
	
	//编号流程发起需要-不对应库
	private String code;
	
	//流程变量发起需要-不对应库
	private String flowVariable;
	
	//经办人ID
	private String operatorId;
	
	//经办人名称
	private String operatorName;
	
	//经办部门ID
	private String operateDepartmentId;
	
	//经办部门名称
	private String operateDepartmentName;
	
	//经办公司ID
	private String operateCompanyId;
	
	//经办公司名称
	private String operateCompanyName;
	
	//经办项目ID
	private String operateProjectId;
	
	//经办项目名称
	private String operateProjectName;
	
	//经办分期ID
	private String operateQiId;
	
	//经办分期名称
	private String operateQiName;
	
	//流程实例id
	private String instanceId;
	
	//高级查询
	private String formSearchSeniorValue;
	
	//业务id(流程统一传递)
	private String businessId;
	
	//表单移动审批值
	private String formMobileValueJson;
	
	//附件上传appid
	private String appId;
	
	private String customFormName;
	
	//单据编号
	private String formNumber;
	
	//单据编号每日叠加数
	private Integer dayNumber;
	
	//资金平台标识
	private String payformid;

	//业务类型
	private String vbusinesstype;
	
	//高级查询格式
	private String formSearchSeniorKey;
	
	//历史模板id
	private String customFormHisId;
	
	//用户类型
	private String userType;
	
	//单据类型（用于判断是否是自定义表单单据）
	private String documentType;
	
	//标题
	private String formTitle;
	
	public String getFormTitle() {
		return formTitle;
	}
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getCustomFormHisId() {
		return customFormHisId;
	}
	public void setCustomFormHisId(String customFormHisId) {
		this.customFormHisId = customFormHisId;
	}
	public String getFormSearchSeniorKey() {
		return formSearchSeniorKey;
	}
	public void setFormSearchSeniorKey(String formSearchSeniorKey) {
		this.formSearchSeniorKey = formSearchSeniorKey;
	}
	public String getVbusinesstype() {
		return vbusinesstype;
	}
	public void setVbusinesstype(String vbusinesstype) {
		this.vbusinesstype = vbusinesstype;
	}
	public String getFormNumber() {
		return formNumber;
	}
	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
	}
	public Integer getDayNumber() {
		return dayNumber;
	}
	public void setDayNumber(Integer dayNumber) {
		this.dayNumber = dayNumber;
	}
	public String getPayformid() {
		return payformid;
	}
	public void setPayformid(String payformid) {
		this.payformid = payformid;
	}
	public String getFormFormatJson() {
		return formFormatJson;
	}
	public void setFormFormatJson(String formFormatJson) {
		this.formFormatJson = formFormatJson;
	}
    
  		
	public String getFormValueJson() {
		return formValueJson;
	}
	public void setFormValueJson(String formValueJson) {
		this.formValueJson = formValueJson;
	}
    
  		
	public String getCustomFormId() {
		return customFormId;
	}
	public void setCustomFormId(String customFormId) {
		this.customFormId = customFormId;
	}
	public String getFormSearchKey() {
		return formSearchKey;
	}
	public void setFormSearchKey(String formSearchKey) {
		this.formSearchKey = formSearchKey;
	}
	public String getFormFlowVariableValue() {
		return formFlowVariableValue;
	}
	public void setFormFlowVariableValue(String formFlowVariableValue) {
		this.formFlowVariableValue = formFlowVariableValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFlowVariable() {
		return flowVariable;
	}
	public void setFlowVariable(String flowVariable) {
		this.flowVariable = flowVariable;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getOperateDepartmentId() {
		return operateDepartmentId;
	}
	public void setOperateDepartmentId(String operateDepartmentId) {
		this.operateDepartmentId = operateDepartmentId;
	}
	public String getOperateDepartmentName() {
		return operateDepartmentName;
	}
	public void setOperateDepartmentName(String operateDepartmentName) {
		this.operateDepartmentName = operateDepartmentName;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getFormSearchSeniorValue() {
		return formSearchSeniorValue;
	}
	public void setFormSearchSeniorValue(String formSearchSeniorValue) {
		this.formSearchSeniorValue = formSearchSeniorValue;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getOperateCompanyId() {
		return operateCompanyId;
	}
	public void setOperateCompanyId(String operateCompanyId) {
		this.operateCompanyId = operateCompanyId;
	}
	public String getOperateCompanyName() {
		return operateCompanyName;
	}
	public void setOperateCompanyName(String operateCompanyName) {
		this.operateCompanyName = operateCompanyName;
	}
	public String getOperateProjectId() {
		return operateProjectId;
	}
	public void setOperateProjectId(String operateProjectId) {
		this.operateProjectId = operateProjectId;
	}
	public String getOperateProjectName() {
		return operateProjectName;
	}
	public void setOperateProjectName(String operateProjectName) {
		this.operateProjectName = operateProjectName;
	}
	public String getOperateQiId() {
		return operateQiId;
	}
	public void setOperateQiId(String operateQiId) {
		this.operateQiId = operateQiId;
	}
	public String getOperateQiName() {
		return operateQiName;
	}
	public void setOperateQiName(String operateQiName) {
		this.operateQiName = operateQiName;
	}
	public String getFormMobileValueJson() {
		return formMobileValueJson;
	}
	public void setFormMobileValueJson(String formMobileValueJson) {
		this.formMobileValueJson = formMobileValueJson;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCustomFormName() {
		return customFormName;
	}
	public void setCustomFormName(String customFormName) {
		this.customFormName = customFormName;
	}
  		
}
