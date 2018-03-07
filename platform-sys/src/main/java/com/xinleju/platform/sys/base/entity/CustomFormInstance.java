package com.xinleju.platform.sys.base.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_BASE_CUSTOM_FORM_INSTANCE",desc="自定义表单实例")
public class CustomFormInstance extends BaseEntity{
	
		
	@Column(value="form_format_json",desc="表单格式JSON")
	private String formFormatJson;
    
  		
	@Column(value="form_value_json",desc="表单值JSON")
	private String formValueJson;
    
  		
	@Column(value="custom_form_id",desc="表单ID")
	private String customFormId;
    
  		
	@Column(value="form_search_key",desc="查询字段")
	private String formSearchKey;

	@Column(value="form_flow_variable_value",desc="表单流程变量")
	private String formFlowVariableValue;
	
	@Column(value="operator_id",desc="经办人ID")
	private String operatorId;
	
	@Column(value="operator_name",desc="经办人名称")
	private String operatorName;
	
	@Column(value="operate_department_id",desc="经办部门ID")
	private String operateDepartmentId;
	
	@Column(value="operate_department_name",desc="经办部门名称")
	private String operateDepartmentName;
	
	@Column(value="operate_company_id",desc="经办公司ID")
	private String operateCompanyId;
	
	@Column(value="operate_company_name",desc="经办公司名称")
	private String operateCompanyName;
	
	@Column(value="operate_project_id",desc="经办项目ID")
	private String operateProjectId;
	
	@Column(value="operate_project_name",desc="经办项目名称")
	private String operateProjectName;
	
	@Column(value="operate_qi_id",desc="经办分期ID")
	private String operateQiId;
	
	@Column(value="operate_qi_name",desc="经办分期名称")
	private String operateQiName;
  		
	@Column(value="status",desc="状态")
	private String status;
	
	@Column(value="instance_id",desc="流程实例id")
	private String instanceId;
	
	@Column(value="form_search_senior_value",desc="高级查询")
	private String formSearchSeniorValue;
	
	@Column(value="form_mobile_value_json",desc="表单移动审批值")
	private String formMobileValueJson;
	
	@Column(value="form_number",desc="单据编号")
	private String formNumber;
	
	@Column(value="day_number",desc="单据编号每日叠加数")
	private Integer dayNumber;
	
	@Column(value="pay_form_id",desc="资金平台标识")
	private String payformid;

	@Column(value="custom_form_his_id",desc="历史模板id")
	private String customFormHisId;

	@Column(value="form_title",desc="标题")
	private String formTitle;
	
	public String getFormTitle() {
		return formTitle;
	}
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}
	public String getCustomFormHisId() {
		return customFormHisId;
	}
	public void setCustomFormHisId(String customFormHisId) {
		this.customFormHisId = customFormHisId;
	}
	public String getPayformid() {
		return payformid;
	}
	public void setPayformid(String payformid) {
		this.payformid = payformid;
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
	
}
