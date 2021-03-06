package com.xinleju.platform.flow.utils;

import org.apache.log4j.Logger;

import com.xinleju.platform.tools.data.JacksonUtils;

public class FlowLogUtil {

	public static Logger flowLogger = Logger.getLogger("flowLogger");
	public static Logger msgSyncLogger = Logger.getLogger("msgSyncLogger");
	
	public static void log(Object message) {
		flowLogger.info(message);
	}
	
	/**
	 * 日志格式：[时间][A系统->B系统][操作][接收人][消息正文]
	 * @param from
	 * @param to
	 * @param reciver
	 * @param message
	 * @param result
	 */
	public static void logSyncMsg(String from, String to, String operate, String reciver, Object message, Object result) {
		String newMessage = "[" + from + " -> " + to + "],[操作: " + operate + "][" + reciver + "]消息正文:" 
				+ JacksonUtils.toJson(message) + "发送结果: " + JacksonUtils.toJson(result);
		flowLogger.info(newMessage);
		msgSyncLogger.info(newMessage);
	}
}
