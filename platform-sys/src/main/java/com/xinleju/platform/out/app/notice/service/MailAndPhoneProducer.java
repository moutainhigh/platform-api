package com.xinleju.platform.out.app.notice.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.sys.notice.dto.service.MailMsgDtoServiceCustomer;
import com.xinleju.platform.sys.notice.service.SysNoticePhoneMsgService;
import com.xinleju.platform.tools.data.JacksonUtils;

public class MailAndPhoneProducer implements MailAndPhoneServiceCustomer{
	private static Logger log = Logger.getLogger(MailAndPhoneProducer.class);
	
	@Autowired
	private SysNoticePhoneMsgService sysNoticePhoneMsgService;
	@Autowired
	private MailMsgDtoServiceCustomer mailMsgDtoServiceCustomer;
	
	/**
	 * 
	 * @param userJson
	 * @param paramaterJson({msg:发送信息;phones:155XXXXXXXX,133XXXXXXXX})
	 * @return json格式 DubboServiceResultInfo (正确result返回1) 
	 */
	public String sendPhoneMsg(String userJson, String paramaterJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramaterJson)){
				@SuppressWarnings("unchecked")
				Map<String,Object> map=JacksonUtils.fromJson(paramaterJson, HashMap.class);
				String str=sysNoticePhoneMsgService.saveSendMsg(map);
				info.setResult(JacksonUtils.toJson(str));
			    info.setSucess(true);
			    info.setMsg("发送短信成功!");
			}else{
				log.error("发送短信失败!参数为空。");
				info.setSucess(false);
				info.setMsg("发送短信失败!参数为空。");
				info.setExceptionMsg("paramaterJson为空。");
			}
		} catch (Exception e) {
			 log.error("发送短信失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("发送短信失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
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
	 */
	public String sendMailMsg(String userJson, String paramaterJson) {
		return mailMsgDtoServiceCustomer.sendMailMsg(userJson, paramaterJson);
	}
}
