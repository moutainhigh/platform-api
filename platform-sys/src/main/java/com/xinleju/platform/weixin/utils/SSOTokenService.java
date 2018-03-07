package com.xinleju.platform.weixin.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSOTokenService {
	
	private static Logger logger = LoggerFactory.getLogger(SSOLoginUtils.class);
	
	private static Map<String, SSOToken> tokenMap = new HashMap<String, SSOToken>();
	
	/**
	 * 根据用户Id和loginName获取用户的token信息
	 * @param userId
	 * @param loginName: 必须是带着二级域名的, 如@xy
	 * @return
	 */
	public static String processUrlTextWithSSOTokenByUserInfo(String urlText, String userId, String loginName, String tendId, String tendCode ){ 
		String tokenText = "";
		SSOToken ssoToken = tokenMap.get(userId);
		if(ssoToken == null || !SSOLoginUtils.isValid(ssoToken)){
    		//重新获取token
			com.alibaba.fastjson.JSONObject json = SSOLoginUtils.getToken(loginName, tendId, tendCode);
			if(json.getBoolean("success")){
    			ssoToken = new SSOToken();
    			ssoToken.setToken(json.getString("token"));
    			ssoToken.setExpiredTime(json.getString("token_end_date"));
    			tokenMap.put(userId, ssoToken);
    			tokenText = ssoToken.getToken();
    		}else{
    			System.out.println("获取平台token失败！json="+json.toJSONString());
    			return "get platform token is fail";
    		}
		}else{
			tokenText = ssoToken.getToken();
    	}

		String fullUrlText = SSOLoginUtils.getDomain()+SSOLoginUtils.getSsoUri()+"?token="+tokenText
				+"&redirectUri="+org.apache.commons.codec.binary.Base64.encodeBase64String(urlText.getBytes());
				//+"&redirectUri="+Base64.encodeBase64String(urlText.getBytes());
		return fullUrlText;
	}

}
