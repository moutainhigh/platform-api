package com.xinleju.platform.flow.enumeration;

/**
 * 代理类型
 * 
 * @author daoqi
 *
 */
public enum ProxyType {

	COPY("流程复制", "1"), 			//2:发起人(GT)
	CUT("流程剪切", "2"),				//1:审批人(DL,ZB,ZC)
	ADD_AC_BEFORE("前加签", "3"),		//3:被协办人(XB)
	ADD_AC_AFTER("后加签", "4");		//3:被协办人(XB)
	
	private String name;
	private String value;

	private ProxyType(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
