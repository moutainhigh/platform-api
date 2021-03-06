package com.xinleju.platform.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * 检验是否符合某一个规则。
 * 
 */

public class MyChecker
{
	
	 public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

	/**
	 * 是否为手机
	 * @param phone
	 * @return
	 */
	public static final boolean isPhoneNo(String phone){
		Pattern p = Pattern.compile(REGEX_MOBILE);
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 是否为email
	 * @param email
	 * @return
	 */
	public static final boolean isEmail(String email){
		Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");;
		Matcher m = p.matcher(email);
		return m.matches();
	}


}
