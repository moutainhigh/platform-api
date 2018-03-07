package com.xinleju.platform.univ.search.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author haoqp
 * 
 *
 */
public class SearchIndexDto extends BaseDto{

		
	//索引值
	private String content;
	
	private Object contentObject;
	
	// 添加elasticsearch时，作为_index
	private String esDocIndex;
    
	// 关联SearchCategory中的code
	// 添加elasticsearch时，作为_type
	private String esDocType;
	
	// 添加elasticsearch时，作为_id
	private String esDocId;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Object getContentObject() {
		return contentObject;
	}

	public void setContentObject(Object contentObject) {
		this.contentObject = contentObject;
	}

	public String getEsDocIndex() {
		return esDocIndex;
	}

	public void setEsDocIndex(String esDocIndex) {
		this.esDocIndex = esDocIndex;
	}

	public String getEsDocType() {
		return esDocType;
	}

	public void setEsDocType(String esDocType) {
		this.esDocType = esDocType;
	}

	public String getEsDocId() {
		return esDocId;
	}

	public void setEsDocId(String esDocId) {
		this.esDocId = esDocId;
	}
		
}