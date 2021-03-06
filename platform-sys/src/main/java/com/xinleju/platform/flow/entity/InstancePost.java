package com.xinleju.platform.flow.entity;

import java.sql.Timestamp;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * 流程岗位实体
 * 
 * @author daoqi
 *
 */
@Table(value="PT_FLOW_INSTANCE_POST",desc="流程岗位")
public class InstancePost extends BaseEntity{
	
	@Column(value="post_name",desc="岗位名称")
	private String postName;
	
	@Column(value="post_id",desc="岗位id")
	private String postId;
		
	@Column(value="ac_id",desc="流程环节id")
	private String acId;
  		
	@Column(value="activate_date",desc="岗位激活时间")
	private Timestamp activateDate;
    
	@Column(value="end_date",desc="岗位完成时间")
	private Timestamp endDate;
	
	@Column(value="left_person",desc="岗位中剩余待处理人的数量")
	private int leftPerson;
  		
	@Column(value="status",desc="状态: 1:未运行,2:运行中 ,3:完成")
	private String status;
	
	@Column(value="px",desc="排序")
	private Long px;
	
	public String getAcId() {
		return acId;
	}
	public void setAcId(String acId) {
		this.acId = acId;
	}
	public String getPostName() {
		return postName;
	}
	public void setPostName(String postName) {
		this.postName = postName;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getPx() {
		return px;
	}
	public void setPx(Long px) {
		this.px = px;
	}
	public int getLeftPerson() {
		return leftPerson;
	}
	public void setLeftPerson(int leftPerson) {
		this.leftPerson = leftPerson;
	}
}
