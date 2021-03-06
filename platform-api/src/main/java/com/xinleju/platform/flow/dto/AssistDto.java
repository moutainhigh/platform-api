package com.xinleju.platform.flow.dto;

import java.io.Serializable;

/**
 * @author admin
 * 
 *
 */
public class AssistDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//协办总人数
	private Integer total;
	//协办剩余未办人数
	private Integer remainder;
	//协办剩余人名称
	private String remainderNames;
	//已协办人员名称
	private String replyNames;
	public String getReplyNames() {
		return replyNames;
	}
	public void setReplyNames(String replyNames) {
		this.replyNames = replyNames;
	}
	public String getRemainderNames() {
		return remainderNames;
	}
	public void setRemainderNames(String remainderNames) {
		this.remainderNames = remainderNames;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getRemainder() {
		return remainder;
	}
	public void setRemainder(Integer remainder) {
		this.remainder = remainder;
	}
}
