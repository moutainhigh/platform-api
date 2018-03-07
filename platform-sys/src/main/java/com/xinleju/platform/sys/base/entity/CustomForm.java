package com.xinleju.platform.sys.base.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_BASE_CUSTOM_FORM",desc="自定义表单")
public class CustomForm extends BaseEntity{
	
		
	@Column(value="code",desc="编码")
	private String code;
    
  		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="parent_id",desc="父节点ID")
	private String parentId;
    
  		
	@Column(value="parent_name",desc="父节点名称")
	private String parentName;
    
  		
	@Column(value="biz_flow_code",desc="业务对象编码")
	private String bizFlowCode;
    
  		
	@Column(value="biz_flow_name",desc="业务对象名称")
	private String bizFlowName;
    
  		
	@Column(value="flow_path_id",desc="流程路径ID")
	private String flowPathId;
    
  		
	@Column(value="flow_path_name",desc="流程路径名称")
	private String flowPathName;
	
	
	@Column(value="flow_template_id",desc="流程模板ID")
	private String flowTemplateId;
    
  		
	@Column(value="flow_template_name",desc="流程模板名称")
	private String flowTemplateName;
    
  		
	@Column(value="flow_path_show_name",desc="流程路径显示名称")
	private String flowPathShowName;
    
  		
	@Column(value="menu_path_id",desc="菜单路径ID")
	private String menuPathId;
    
  		
	@Column(value="menu_path_name",desc="菜单路径名称")
	private String menuPathName;
    
  		
	@Column(value="menu_path_show_name",desc="菜单路径显示名称")
	private String menuPathShowName;
    
  		
	@Column(value="default_flow_id",desc="默认流程模板ID")
	private String defaultFlowId;
    
  		
	@Column(value="default_flow_name",desc="默认流程模板名称")
	private String defaultFlowName;
	
	@Column(value="form_format_json",desc="表单格式JSON")
	private String formFormatJson;
	
	@Column(value="form_format_html",desc="表单格式HTML")
	private String formFormatHtml;
	
	@Column(value="form_show_column",desc="显示列JSON")
	private	String formShowColumn;
		
	@Column(value="form_search_key",desc="查询关键字")
	private String formSearchKey;
	
	@Column(value="flow_variable",desc="流程变量")
	private String flowVariable;

	@Column(value="copy_source_id",desc="复制原模板ID")
	private String copySourceId;

	@Column(value="is_complete",desc="是否复制完成")
	private String isComplete;
    
	@Column(value="sort",desc="序号")
	private Long sort;	
	
	@Column(value="form_search_senior_key",desc="高级查询格式")
	private String formSearchSeniorKey;	
	
	@Column(value="is_inner",desc="是否内部链接")
	private Integer isInner;
    
	@Column(value="resource_id",desc="资源id")
	private String resourceId;
    
	@Column(value="resource_name",desc="资源名称")
	private String resourceName;
  		
	@Column(value="url",desc="地址")
	private String url;
    
	@Column(value="status",desc="状态")
	private Integer status;
	
	@Column(value="data_item_control",desc="数据权限控制")
	private Integer dataItemControl;
	
	@Column(value="data_item_id",desc="数据权限id")
	private String dataItemId;
		
	@Column(value="is_ex",desc="费用表单")
	private Integer isEx;

	@Column(value="business_type",desc="业务类型")
	private String businessType;

	@Column(value="is_mobile_allowed",desc="是否移动端允许发起审批")
	private Integer isMobileAllowed;

	@Column(value="mobile_url",desc="移动地址")
	private String mobileUrl;
	
	public String getMobileUrl() {
		return mobileUrl;
	}
	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}
	public Integer getIsMobileAllowed() {
		return isMobileAllowed;
	}
	public void setIsMobileAllowed(Integer isMobileAllowed) {
		this.isMobileAllowed = isMobileAllowed;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public Integer getIsEx() {
		return isEx;
	}
	public void setIsEx(Integer isEx) {
		this.isEx = isEx;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
  		
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
    
  		
	public String getBizFlowCode() {
		return bizFlowCode;
	}
	public void setBizFlowCode(String bizFlowCode) {
		this.bizFlowCode = bizFlowCode;
	}
    
  		
	public String getBizFlowName() {
		return bizFlowName;
	}
	public void setBizFlowName(String bizFlowName) {
		this.bizFlowName = bizFlowName;
	}
    
  		
	public String getFlowPathId() {
		return flowPathId;
	}
	public void setFlowPathId(String flowPathId) {
		this.flowPathId = flowPathId;
	}
    
  		
	public String getFlowPathName() {
		return flowPathName;
	}
	public void setFlowPathName(String flowPathName) {
		this.flowPathName = flowPathName;
	}
    
  		
	public String getFlowPathShowName() {
		return flowPathShowName;
	}
	public void setFlowPathShowName(String flowPathShowName) {
		this.flowPathShowName = flowPathShowName;
	}
    
  		
	public String getMenuPathId() {
		return menuPathId;
	}
	public void setMenuPathId(String menuPathId) {
		this.menuPathId = menuPathId;
	}
    
  		
	public String getMenuPathName() {
		return menuPathName;
	}
	public void setMenuPathName(String menuPathName) {
		this.menuPathName = menuPathName;
	}
    
  		
	public String getMenuPathShowName() {
		return menuPathShowName;
	}
	public void setMenuPathShowName(String menuPathShowName) {
		this.menuPathShowName = menuPathShowName;
	}
    
  		
	public String getDefaultFlowId() {
		return defaultFlowId;
	}
	public void setDefaultFlowId(String defaultFlowId) {
		this.defaultFlowId = defaultFlowId;
	}
    
  		
	public String getDefaultFlowName() {
		return defaultFlowName;
	}
	public void setDefaultFlowName(String defaultFlowName) {
		this.defaultFlowName = defaultFlowName;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getFlowTemplateId() {
		return flowTemplateId;
	}
	public void setFlowTemplateId(String flowTemplateId) {
		this.flowTemplateId = flowTemplateId;
	}
	public String getFlowTemplateName() {
		return flowTemplateName;
	}
	public void setFlowTemplateName(String flowTemplateName) {
		this.flowTemplateName = flowTemplateName;
	}
	public String getFormFormatJson() {
		return formFormatJson;
	}
	public void setFormFormatJson(String formFormatJson) {
		this.formFormatJson = formFormatJson;
	}
	public String getFormFormatHtml() {
		return formFormatHtml;
	}
	public void setFormFormatHtml(String formFormatHtml) {
		this.formFormatHtml = formFormatHtml;
	}
	public String getFormShowColumn() {
		return formShowColumn;
	}
	public void setFormShowColumn(String formShowColumn) {
		this.formShowColumn = formShowColumn;
	}
	public String getFormSearchKey() {
		return formSearchKey;
	}
	public void setFormSearchKey(String formSearchKey) {
		this.formSearchKey = formSearchKey;
	}
	public String getFlowVariable() {
		return flowVariable;
	}
	public void setFlowVariable(String flowVariable) {
		this.flowVariable = flowVariable;
	}
	public String getCopySourceId() {
		return copySourceId;
	}
	public void setCopySourceId(String copySourceId) {
		this.copySourceId = copySourceId;
	}
	public String getIsComplete() {
		return isComplete;
	}
	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}
	public String getFormSearchSeniorKey() {
		return formSearchSeniorKey;
	}
	public void setFormSearchSeniorKey(String formSearchSeniorKey) {
		this.formSearchSeniorKey = formSearchSeniorKey;
	}
	public Integer getIsInner() {
		return isInner;
	}
	public void setIsInner(Integer isInner) {
		this.isInner = isInner;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDataItemId() {
		return dataItemId;
	}
	public void setDataItemId(String dataItemId) {
		this.dataItemId = dataItemId;
	}
	public Integer getDataItemControl() {
		return dataItemControl;
	}
	public void setDataItemControl(Integer dataItemControl) {
		this.dataItemControl = dataItemControl;
	}
}
