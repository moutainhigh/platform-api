package com.xinleju.platform.sys.res.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class AppSystemDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//名称
	private String name;
    
	//图标
	private byte[] icon;
    
  		
	//状态
	private String status;
    
  		
	//排序
	private Long sort;
    
  	//是否分配
	private String isPermission;
	
	//显示全名
	private String fullName;
	
	//URL
	private String url;
	
	//是否外系统
	private String isextsys;
	
	//打开方式
	private String openmode;
	
	private String isDelPic;
	//说明
	private String remark;
	
	//全路径Id
	private String prefixId;
	//全路径排序
	private String prefixSort;
	//全路径名称
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
    
  
  		
	public byte[] getIcon() {
		return icon;
	}
	public void setIcon(byte[] icon) {
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
	public String getIsDelPic() {
		return isDelPic;
	}
	public void setIsDelPic(String isDelPic) {
		this.isDelPic = isDelPic;
	}
    
  		
}
