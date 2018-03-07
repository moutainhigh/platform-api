package com.xinleju.platform.weixin.pojo; 

import java.io.Serializable;

public class WeixinResponse implements Serializable {
	private static final long serialVersionUID = 7465456364387121161L;
	private int errcode;
	private String errmsg;

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
