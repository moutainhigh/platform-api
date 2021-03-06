package com.xinleju.platform.sys.num.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class RulerDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//备注
	private String remark;
    
  		
	//名称
	private String name;
    
  		
	//编号规则id
	private String billId;
    
  		
	//规则类别
	private String type;
    
  		
	//是否输出
	private String isOut;
    
  		
	//日期格式
	private String dateFormat;
    
  		
	//强制连续
	private String isSerial;
    
  		
	//初始值
	private String initVar;
    
  		
	//步长
	private Integer stepLength;
    
  		
	//起步值
	private Long initSerial;
    
  		
	//最大值
	private Long maxSerial;
    
  		
	//序号格式
	private String serialFormat;
    
  		
	//枚举库
	private String serialLibrary;
    
  	//连接符
	private String connectorSymbol;
	//流水号位数
	private String serialNumberLength;
	//流水类型
	private String serialNumberType;
	//排序号
	private Integer sort;
    
	//数据标识(判断数据新增1删除2修改3)
	private Integer dataType;
  		
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getIsOut() {
		return isOut;
	}
	public void setIsOut(String isOut) {
		this.isOut = isOut;
	}
    
  		
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
    
  		
	public String getIsSerial() {
		return isSerial;
	}
	public void setIsSerial(String isSerial) {
		this.isSerial = isSerial;
	}
    
  		

    
  		
	public String getInitVar() {
		return initVar;
	}
	public void setInitVar(String initVar) {
		this.initVar = initVar;
	}
	public Integer getStepLength() {
		return stepLength;
	}
	public void setStepLength(Integer stepLength) {
		this.stepLength = stepLength;
	}
    
  		
	public Long getInitSerial() {
		return initSerial;
	}
	public void setInitSerial(Long initSerial) {
		this.initSerial = initSerial;
	}
    
  		
	public Long getMaxSerial() {
		return maxSerial;
	}
	public void setMaxSerial(Long maxSerial) {
		this.maxSerial = maxSerial;
	}
    
  		
	public String getSerialFormat() {
		return serialFormat;
	}
	public void setSerialFormat(String serialFormat) {
		this.serialFormat = serialFormat;
	}
    
  		
	public String getSerialLibrary() {
		return serialLibrary;
	}
	public void setSerialLibrary(String serialLibrary) {
		this.serialLibrary = serialLibrary;
	}
    
  		
	public String getConnectorSymbol() {
		return connectorSymbol;
	}
	public void setConnectorSymbol(String connectorSymbol) {
		this.connectorSymbol = connectorSymbol;
	}
	public String getSerialNumberLength() {
		return serialNumberLength;
	}
	public void setSerialNumberLength(String serialNumberLength) {
		this.serialNumberLength = serialNumberLength;
	}
	public String getSerialNumberType() {
		return serialNumberType;
	}
	public void setSerialNumberType(String serialNumberType) {
		this.serialNumberType = serialNumberType;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
    
  		
}
