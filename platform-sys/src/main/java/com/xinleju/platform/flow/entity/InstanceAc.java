package com.xinleju.platform.flow.entity;

import java.sql.Timestamp;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * 实例环节
 * 
 * @author admin
 */
@Table(value="PT_FLOW_INSTANCE_AC",desc="流程环节实例")
public class InstanceAc extends BaseEntity{
	
	@Column(value="code",desc="编号")
	private String code;
  		
	@Column(value="is_add_label",desc="是否加签")
	private Boolean isAddLabel;

	@Column(value="is_start",desc="手工选择审批人为空是否发起 ")
	private Boolean isStart;
  		
	@Column(value="approve_strategy",desc="多岗策略")
	private String approveStrategy;
  		
	@Column(value="post_multi_person",desc="同岗多人策略")
	private String postMultiPerson;
	
	//1环节中剩余责任岗位数：环节对应的初始岗位数据
	//2join节点时记录已到达分支数，判断是否往下走
	@Column(value="left_post",desc="")
	private int leftPost;
  		
	@Column(value="status",desc="状态")
	private String status;
  		
	@Column(value="disable",desc="是否有效")
	private Boolean disable;
  		
	@Column(value="source",desc="来源")
	private String source;
  		
	@Column(value="source_id",desc="来源id")
	private String sourceId;
  		
	@Column(value="remark",desc="备注")
	private String remark;
  		
	@Column(value="activate_date",desc="激活时间")
	private Timestamp activateDate;
  		
	@Column(value="end_date",desc="完成时间")
	private Timestamp endDate;
    
	@Column(value="name",desc="环节名称")
	private String name;
  		
	@Column(value="fi_id",desc="流程实例id")
	private String fiId;
  		
	@Column(value="ac_type",desc="环节类型")
	private String acType;
  		
	@Column(value="approve_type_id",desc="审批类型id")
	private String approveTypeId;
  		
	@Column(value="x",desc="x坐标")
	private Long x;
  		
	@Column(value="y",desc="y坐标")
	private Long y;
  		
	@Column(value="width",desc="宽")
	private Long width;
    
	@Column(value="height",desc="高")
	private Long height;
    
	@Column(value="px",desc="显示顺序")
	private Long px;	
	
	@Column(value="pre_ac_ids",desc="来源环节ID")
	private String preAcIds;
	
	@Column(value="next_ac_ids",desc="后续环节ID")
	private String nextAcIds;
	
	@Column(value="from_return",desc="产生于打回操作")
	private String fromReturn = "0";
	
	@Column(value="overdue_time",desc="环节逾期时间")
  	private Integer overdueTime;
  	
	@Column(value="overdue_handle",desc="逾期处理方式")
  	private String overdueHandle;
	
	@Column(value="post_is_null",desc="岗位为空策略")
	private String postIsNull;
	
	@Column(value="approval_person_is_null",desc="审批人为空策略")
	private String approvalPersonIsNull;
	

	@Column(value="return_px",desc="打回时当前节点的px")
	private Integer returnPx;
		
	public Integer getReturnPx() {
		return returnPx;
	}
	public void setReturnPx(Integer returnPx) {
		this.returnPx = returnPx;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
  		
	public Boolean getIsAddLabel() {
		return isAddLabel;
	}

	public void setIsAddLabel(Boolean isAddLabel) {
		this.isAddLabel = isAddLabel;
	}
  		
	public String getApproveStrategy() {
		return approveStrategy;
	}

	public void setApproveStrategy(String approveStrategy) {
		this.approveStrategy = approveStrategy;
	}
  		
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
  		
	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
    
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
    
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
    
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
  		
	public Timestamp getActivateDate() {
		return activateDate;
	}

	public void setActivateDate(Timestamp activateDate) {
		this.activateDate = activateDate;
	}
    
	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
  		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	public String getFiId() {
		return fiId;
	}

	public void setFiId(String fiId) {
		this.fiId = fiId;
	}
    
	public String getAcType() {
		return acType;
	}

	public void setAcType(String acType) {
		this.acType = acType;
	}
  		
	public String getApproveTypeId() {
		return approveTypeId;
	}

	public void setApproveTypeId(String approveTypeId) {
		this.approveTypeId = approveTypeId;
	}
  		
	public Long getX() {
		return x;
	}

	public void setX(Long x) {
		this.x = x;
	}
  		
	public Long getY() {
		return y;
	}

	public void setY(Long y) {
		this.y = y;
	}
    
	public Long getWidth() {
		return width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}
    
	public Long getHeight() {
		return height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}
	
	public String getPostMultiPerson() {
		return postMultiPerson;
	}
	
	public void setPostMultiPerson(String postMultiPerson) {
		this.postMultiPerson = postMultiPerson;
	}
	
	public Long getPx() {
		return px;
	}
	
	public void setPx(Long px) {
		this.px = px;
	}
	
	public int getLeftPost() {
		return leftPost;
	}
	
	public void setLeftPost(int leftPost) {
		this.leftPost = leftPost;
	}
	
	public String getPreAcIds() {
		return preAcIds;
	}
	
	public void setPreAcIds(String preAcIds) {
		this.preAcIds = preAcIds;
	}
	
	public String getNextAcIds() {
		return nextAcIds;
	}
	
	public void setNextAcIds(String nextAcIds) {
		this.nextAcIds = nextAcIds;
	}
	
	public String getFromReturn() {
		return fromReturn;
	}
	
	public void setFromReturn(String fromReturn) {
		this.fromReturn = fromReturn;
	}
	
	public Integer getOverdueTime() {
		return overdueTime;
	}
	
	public void setOverdueTime(Integer overdueTime) {
		this.overdueTime = overdueTime;
	}
	
	public String getOverdueHandle() {
		return overdueHandle;
	}
	
	public void setOverdueHandle(String overdueHandle) {
		this.overdueHandle = overdueHandle;
	}
	public String getPostIsNull() {
		return postIsNull;
	}
	public void setPostIsNull(String postIsNull) {
		this.postIsNull = postIsNull;
	}
	public String getApprovalPersonIsNull() {
		return approvalPersonIsNull;
	}
	public void setApprovalPersonIsNull(String approvalPersonIsNull) {
		this.approvalPersonIsNull = approvalPersonIsNull;
	}	
}
