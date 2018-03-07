package com.xinleju.platform.sys.logo.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class SysLogoDto extends BaseDto{

		
	//logo名称
	private String name;
    
  		
	//logo图标
	private byte[] icon;
	
	//logo图标
	private String pic;
	
	//isDelPic
	private String isDelPic;

	private String url;
		
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
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public String getIsDelPic() {
		return isDelPic;
	}
	public void setIsDelPic(String isDelPic) {
		this.isDelPic = isDelPic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
