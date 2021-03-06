package com.xinleju.platform.sys.res.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_RES_APP",desc="系统")
public class AppSystem extends BaseEntity{
	
		
	@Column(value="code",desc="编号")
	private String code;
    
  		
	@Column(value="name",desc="名称")
	private String name;
    
	@Column(value="icon",desc="图标")
	private String icon;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
	@Column(value="sort",desc="排序")
	private Long sort;
    
	@Column(value="is_permission",desc="是否分配")
	private String isPermission;
    	
		
	@Column(value="full_name",desc="显示全名")
	private String fullName;
	
	@Column(value="url",desc="URL")
	private String url;
	
	@Column(value="isextsys",desc="是否外系统")
	private String isextsys;
	
	@Column(value="openmode",desc="打开方式")
	private String openmode;
	
	@Column(value="remark",desc="说明")
	private String remark;
	
	@Column(value="prefix_id",desc="全路径Id")
	private String prefixId;
	@Column(value="prefix_sort",desc="全路径排序")
	private String prefixSort;
	@Column(value="prefix_name",desc="全路径名称")
	private String prefixName;
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public String getPrefixSort() {
		return prefixSort;
	}
	public void setPrefixSort(String prefixSort) {
		this.prefixSort = prefixSort;
	}
	public String getPrefixName() {
		return prefixName;
	}
	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
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
    
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getIsPermission() {
		return isPermission;
	}
	public void setIsPermission(String isPermission) {
		this.isPermission = isPermission;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIsextsys() {
		return isextsys;
	}
	public void setIsextsys(String isextsys) {
		this.isextsys = isextsys;
	}
	public String getOpenmode() {
		return openmode;
	}
	public void setOpenmode(String openmode) {
		this.openmode = openmode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	
}
