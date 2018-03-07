package com.xinleju.platform.out.app.notice.service;

public interface MailAndPhoneServiceCustomer {
	
	/**
	 * @param userJson
	 * @param paramaterJson(json格式Map:{msg=发送信息  ,phones=155XXXXXXXX,133XXXXXXXX})
	 * @return json格式 DubboServiceResultInfo (正确result返回1) 
	 * @author ly
	 */
	String sendPhoneMsg(String userJson, String paramaterJson);
	/**
	 * 
	 * @param userJson
	 * @param paramaterJson(
	 * {
		sendAddress:"收件人如：1041954045@qq.com，多个用英文逗号隔开",
		copyAddress:"抄送人如：1041954045@qq.com，多个用英文逗号隔开",
		context:"正文",
		title:"主题"
		})
		@author gyh
	 */
	String sendMailMsg(String userJson, String paramaterJson);
}