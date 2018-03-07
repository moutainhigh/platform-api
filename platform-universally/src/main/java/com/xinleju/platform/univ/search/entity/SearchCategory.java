package com.xinleju.platform.univ.search.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author haoqp
 * 
 * 
 */

@Table(value="PT_UNIV_SEARCH_CATEGORY",desc="分类表")
public class SearchCategory extends BaseEntity{
	
		
	@Column(value="code",desc="分类编码")
	private String code;
    
  		
	@Column(value="name",desc="分类名称")
	private String name;
	
	@Column(value="hostUrl",desc="主机url")
	private String hostUrl;
    
	@Column(value="status",desc="状态：0-启用;1-禁用")
	private Boolean status;	
		
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
	public String getHostUrl() {
		return hostUrl;
	}
	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
    
  		
	
}
