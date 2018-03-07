package com.xinleju.platform.sys.base.dto;

import java.sql.Timestamp;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * @author admin
 * 
 *
 */
public class CustomFormVersionHistoryDto extends BaseDto{

		
	//编码
	private String code;
    
  		
	//名称
	private String name;
    
  		
	//父节点ID
	private String parentId;
    
  		
	//父节点名称
	private String parentName;
    
  		
	//业务对象编码
	private String bizFlowCode;
    
  		
	//业务对象名称
	private String bizFlowName;
    
  		
	//流程路径ID
	private String flowPathId;
    
  		
	//流程路径名称
	private String flowPathName;
    
  		
	//流程路径显示名称
	private String flowPathShowName;
	
	
	//流程模板ID
	private String flowTemplateId;
    
  		
	//流程模板名称
	private String flowTemplateName;
    
  		
	//菜单路径ID
	private String menuPathId;
    
  		
	//菜单路径名称
	private String menuPathName;
    
  		
	//菜单路径显示名称
	private String menuPathShowName;
    
  		
	//默认流程模板ID
	private String defaultFlowId;
    
  		
	//默认流程模板名称
	private String defaultFlowName;
	
	//表单格式JSON
	private String formFormatJson;
	
	//表单格式HTML
	private String formFormatHtml;
	
	//显示列JSON
	private String formShowColumn;
	
	//查询关键字
	private String formSearchKey;

	//流程变量
	private String flowVariable;
	
	//序号
	private Long sort;
	
	//复制原模板ID
	private String copySourceId;

	//是否复制完成 0待复制  1复制完成
	private String isComplete;

	//高级查询格式
	private String formSearchSeniorKey;
	
	//是否内部链接
	private Integer isInner;
    
	//资源id
	private String resourceId;
    
	//资源名称
	private String resourceName;
  		
	//地址
	private String url;
    
	//状态
	private Integer status;
	
	//数据权限id
	private String dataItemId;
	
	//数据权限控制1不启用数据权限 2使用默认数据权限 3使用指定数据权
	private Integer dataItemControl;
    
	//父ID(zTree需要格式)
	private String pId;  	
	
	//校验类型
	private String valType;
	
	private boolean codeExist=false;
	
	private boolean nameExist=false;
	
	//是否有实例(用于设计界面校验)
	private boolean isHasInstance;
	
	private Integer validateRow;
	
	//表单分类编号（流程所需）
	private String parentCode;
	
	//数据权限下拉数据
	private String dataItem;
	
	//一级分类名称
	private String levelOneName;
		
	//费用表单
	private Integer isEx;
	
	//业务类型
	private String businessType;
	//表单模板id
	private String customFormId;

	//版本名称
	private String versionName;
	
	//版本创建人Id
	private String versionCreatePersonId;
	
	//版本创建人名称
	private String versionCreatePersonName;  
	
	//版本创建时间
	private Timestamp versionCreateDate; 
	public String getVersionCreatePersonId() {
		return versionCreatePersonId;
	}
	public void setVersionCreatePersonId(String versionCreatePersonId) {
		this.versionCreatePersonId = versionCreatePersonId;
	}
	public String getVersionCreatePersonName() {
		return versionCreatePersonName;
	}
	public void setVersionCreatePersonName(String versionCreatePersonName) {
		this.versionCreatePersonName = versionCreatePersonName;
	}
	public Timestamp getVersionCreateDate() {
		return versionCreateDate;
	}
	public void setVersionCreateDate(Timestamp versionCreateDate) {
		this.versionCreateDate = versionCreateDate;
	}
	public String getCustomFormId() {
		return customFormId;
	}
	public void setCustomFormId(String customFormId) {
		this.customFormId = customFormId;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
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
	public String getLevelOneName() {
		return levelOneName;
	}
	public void setLevelOneName(String levelOneName) {
		this.levelOneName = levelOneName;
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
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getValType() {
		return valType;
	}
	public void setValType(String valType) {
		this.valType = valType;
	}
	public boolean isCodeExist() {
		return codeExist;
	}
	public void setCodeExist(boolean codeExist) {
		this.codeExist = codeExist;
	}
	public boolean isNameExist() {
		return nameExist;
	}
	public void setNameExist(boolean nameExist) {
		this.nameExist = nameExist;
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
	public boolean isHasInstance() {
		return isHasInstance;
	}
	public void setHasInstance(boolean isHasInstance) {
		this.isHasInstance = isHasInstance;
	}
	public String getFormSearchSeniorKey() {
		return formSearchSeniorKey;
	}
	public void setFormSearchSeniorKey(String formSearchSeniorKey) {
		this.formSearchSeniorKey = formSearchSeniorKey;
	}
	public Integer getValidateRow() {
		return validateRow;
	}
	public void setValidateRow(Integer validateRow) {
		this.validateRow = validateRow;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
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
	public String getDataItem() {
		return dataItem;
	}
	public void setDataItem(String dataItem) {
		this.dataItem = dataItem;
	}
	public Integer getDataItemControl() {
		return dataItemControl;
	}
	public void setDataItemControl(Integer dataItemControl) {
		this.dataItemControl = dataItemControl;
	}
}
