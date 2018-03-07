package com.xinleju.platform.base.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.xinleju.platform.base.datasource.DataSourceContextHolder;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.tools.data.JacksonUtils;


public class DubboServiceFilter  implements Filter{
	private static Logger log = Logger.getLogger(DubboServiceFilter.class);

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		long start = System.currentTimeMillis();
		//处理dubbo异步调用serviceA时，serviceA调用serviceB时异步传染问题
		RpcContext.getContext().getAttachments().remove(Constants.ASYNC_KEY);
		//澶勭悊鏁版嵁杩滃垏鎹�
		Object[] objects=invocation.getArguments();
		if(!"validate".equals(invocation.getMethodName()) && !"getFundPage".equals(invocation.getMethodName()) && !"getPageCustom".equals(invocation.getMethodName())) {
			objects = convertSingleQuotes(objects);
			((RpcInvocation)invocation).setArguments(objects);
		}
		//对参数中关键字进行转义 ，防止sql注入
		((RpcInvocation)invocation).setArguments(convertKeywords(objects));
		if(objects!=null && objects.length>0){
		    Object obj=invocation.getArguments()[0];
		    if(obj!=null && StringUtils.isNotBlank(obj.toString())){
			    SecurityUserBeanInfo securityUserBeanInfo=JacksonUtils.fromJson(obj.toString(), SecurityUserBeanInfo.class);
			    if(securityUserBeanInfo!=null){
				    LoginUtils.setSecurityUserBeanInfo(securityUserBeanInfo);
				    DataSourceContextHolder.clearDataSourceType();
				    DataSourceContextHolder.setDataSourceType(securityUserBeanInfo.getTendCode());
			    }else{
			    	 DataSourceContextHolder.clearDataSourceType();
			    }
		    }else{
		    	 DataSourceContextHolder.clearDataSourceType();
		    }
			Result result = invoker.invoke(invocation);
			long elapsed = System.currentTimeMillis() - start;
			if (invoker.getUrl() != null) {

				// log.info("[" +invoker.getInterface() +"] [" + invocation.getMethodName() +"] [" + elapsed +"]" );
//				log.info("[{}], [{}], {}, [{}], [{}], [{}]   "+"=="+ invoker.getInterface()+"=="+  invocation.getMethodName()+"=="+
//	                         Arrays.toString(invocation.getArguments  ())+"=="+  result.getValue()+"=="+
//	                       result.getException()+"=="+  elapsed);

			}
			return result;
		}else{
			Result result = invoker.invoke(invocation);
			return result;
		}
	}

	public Object[] convertSingleQuotes(Object[] arguments) {
		Object[] retArray = new Object[arguments.length];
		for(int i=0; i<arguments.length; i++) {
			Object arg = arguments[i];
			if(arg instanceof String) {
				arg =  convertSingleQuotes((String)arg);
			}
			retArray[i] = arg;
		}
		return retArray;
	}

	public String convertSingleQuotes(String value) {
		if(StringUtils.isNotEmpty(value)) {
			if(value.contains("\\\\\\'")){
				return value;
			}
			if(value.contains("\\\\") && !value.contains("\\\\'")) {
				return value;
			} else {
				return value.replace("'", "\\\\'").replace("\\\\\\\\", "\\\\");
			}
		}
		return value;
	}
	public Object[] convertKeywords(Object[] arguments) {
		Object[] retArray = new Object[arguments.length];
		for(int i=0; i<arguments.length; i++) {
			Object arg = arguments[i];
			if(arg instanceof String && i>0) {
				arg =  convertKeywords((String)arg);
			}
			retArray[i] = arg;
		}
		return retArray;
	}

	public String convertKeywords(String value) {
		value.toLowerCase();
//		value = value.replace(";", "；");
//		value = value.replace("<", "＜");
//		value = value.replace(">", "＞");
//		value = value.replace("and", "ａｎｄ");
//		value = value.replace("or", "ｏｒ");
//		value = value.replace("select", "ｓｅｌｅｃｔ");
//		value = value.replace("update", "ｕｐｄａｔｅ");
//		value = value.replace("delete", "ｄｅｌｅｔｅ");
//		value = value.replace("drop", "ｄｒｏｐ");
//		value = value.replace("create", "ｃｒｅａｔｅ");
//		value = value.replace("union", "ｕｎｉｏｎ");
//		value = value.replace("insert", "ｉｎｓｅｒｔ");
//		value = value.replace("net", "ｎｅｔ");
//		value = value.replace("truncate", "ｔｒｕｎｃａｔｅ");
//		value = value.replace("exec", "ｅｘｅｃ");
//		value = value.replace("declare", "ｄｅｃｌａｒｅ");
//		value = value.replace("count", "ｃｏｕｎｔ");
//		value = value.replace("chr", "ｃｈｒ");
//		value = value.replace("mid", "ｍｉｄ");
//		value = value.replace("master", "ｍａｓｔｅｒ");
//		value = value.replace("char", "ｃｈａｒ");
		return value;
//		return StringEscapeUtils.escapeSql(value);
	}

}
