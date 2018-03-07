package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class CalendarDetailDto extends BaseDto{

		
	//年份
	private Integer year;
    
  		
	//月份
	private Integer month;
    
  		
	//日期
	private Integer day;
    
	//周几
	private Integer weekDay;
	//1到7默认的是1-周日、2-周一、3-周二、4-周三、5-周四、6-周五、7-周六
	
	//日期类型: [1-工作日 2-节假日]
	private String dayType;
    
	private String dayText;//yyyy-mm-dd格式显示
	
	//备注说明
	private String remark;
    
  		
		
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
    
  		
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
    
  		
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
    
  		
	public String getDayType() {
		return dayType;
	}
	public void setDayType(String dayType) {
		this.dayType = dayType;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(Integer weekDay) {
		this.weekDay = weekDay;
	}
	public String getDayText() {
		return dayText;
	}
	public void setDayText(String dayText) {
		this.dayText = dayText;
	}
    
  		
}
