package com.xinleju.platform.base.utils;

import java.io.IOException;

import com.xinleju.platform.tools.data.JacksonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class LoginUtils {
	private static Logger logger = Logger.getLogger (LoginUtils.class);
	private static ThreadLocal<SecurityUserBeanInfo> threadLocal = new ThreadLocal<SecurityUserBeanInfo>();

    
	public static void setSecurityUserBeanInfo(SecurityUserBeanInfo securityUserBeanInfo){
		threadLocal.set(securityUserBeanInfo);
	}
	/**
	 * @param
	 * @return
	 */
	public static SecurityUserBeanInfo getSecurityUserBeanInfo(){
		return threadLocal.get();
	}
	
	
	/**
	 * 
	 * 调用Post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String  httpPost(String url,String params){
		String result=null;
		CloseableHttpClient httpClient= HttpClients.createDefault(); 
		HttpPost httpPost = new HttpPost(url);
		if(url.indexOf ("customFormInstance/getVariable")>-1){
			logger.info("before Set httpPost Cookie url:"+url);
		}
		if(getSecurityUserBeanInfo().getCookies()!=null){
			try {
				logger.info ("before Set httpPost Cookie:" + JacksonUtils.toJson (httpPost.getHeaders ("Cookie")));
				logger.info ("before Set httpPost userInfo-Cookie:" + JacksonUtils.toJson (getSecurityUserBeanInfo ().getCookies ()));
			}catch (Exception e){}
			httpPost.removeHeaders("Cookie");
			httpPost.addHeader("Cookie",getSecurityUserBeanInfo().getCookies());
			httpPost.addHeader("tendCode",getSecurityUserBeanInfo().getTendCode());
		}else if(url.indexOf ("customFormInstance/getVariable")>-1){
			logger.info("=====cookies异常   httpPost 用户cookies为空========");
		}
		try {
			logger.info("after Set httpPost Cookie:"+ JacksonUtils.toJson (httpPost.getHeaders ("Cookie")));
		}catch (Exception e){
		}
		httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
		httpPost.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
		StringEntity se = new StringEntity(params, "utf-8");
		httpPost.setEntity(se);
		try {
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, "utf-8");
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return result;
		
	}
	
	


	
}
