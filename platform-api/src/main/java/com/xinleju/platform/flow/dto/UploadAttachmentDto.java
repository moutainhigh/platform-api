package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class UploadAttachmentDto extends BaseDto{

		
	//分类Id
	private String categoryId;
    
  		
	//业务Id
	private String businessId;
    
  		
	//实例Id
	private String instanceId;
    
  		
	//阅读类型: 1-通用, 2-PC端使用 3- 移动端使用 
	private String readType;

	//文件名称
	private String filename;

	//url地址
	private String url;

  		
		
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
    
  		
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    
  		
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
    
  		
	public String getReadType() {
		return readType;
	}
	public void setReadType(String readType) {
		this.readType = readType;
	}

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
  		
}
