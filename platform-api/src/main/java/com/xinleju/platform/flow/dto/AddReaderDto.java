package com.xinleju.platform.flow.dto;

public class AddReaderDto {
	private String blockIdx;
	private String dataType; //1-perosn 2-post 3-org
	private String idValue; //"6be54b98caf744e89e1e758f92e00245,e8aa6c00ee1a42358fd2de9fc91ed2ac,52a38d3d6149437981585fecb27e02ed"
	private String nameValue; //"李龙昌,张勇,崔勇"
	private int sort ;//1
	
	public String getBlockIdx() {
		return blockIdx;
	}
	public void setBlockIdx(String blockIdx) {
		this.blockIdx = blockIdx;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getIdValue() {
		return idValue;
	}
	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}
	public String getNameValue() {
		return nameValue;
	}
	public void setNameValue(String nameValue) {
		this.nameValue = nameValue;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
