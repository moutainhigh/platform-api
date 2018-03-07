package com.xinleju.platform.flow.dto.utils;

/**
 * <p>
 * 简单的返回结果
 * </p>
 * <p>
 * </p>
 * 
 * @author 孙朝辉
 * @version $Id: SimpleResult.java 837 2014-07-03 09:53:17Z sunchaohui $
 * @since
 * @see
 */
public class SimpleResult implements IResultBean {
    
    /**
     * 序列化
     */
    private static final long serialVersionUID = 2406971898432425794L;
    
    private boolean result;
    
    private String  msg;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    


}
