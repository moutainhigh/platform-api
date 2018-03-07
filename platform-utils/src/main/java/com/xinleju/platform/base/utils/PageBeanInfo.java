package com.xinleju.platform.base.utils;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 *
 * @param <T>  分页泛型
 * 
 * @see 分页对象 
 * 
 */
public class PageBeanInfo<T> implements Serializable {
	
	private Integer total;  //总记录数
	
	private Integer limit=10;  //当前前记录数
	
	private Integer start=0;  //开始记录数
	
	private List<T> list;   //当前记录实体对象列表
	
	private List<T> items;//当前记录实体对象列表  业务系统返回结果

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}


	
	
	
	

	
	

}
