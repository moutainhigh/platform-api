package com.xinleju.platform.utils;

import com.alibaba.dubbo.rpc.RpcContext;
import com.xinleju.platform.base.utils.ConfigurationUtil;
import com.xinleju.platform.sys.res.dto.service.impl.ResourceDtoServiceProducer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 校验白名单
 */
public class WhiteIpUtils {
    private static Logger log = Logger.getLogger(WhiteIpUtils.class);


    /**
     * dubbo接口校验白名单
     * @return
     */
    public static boolean checkDubboMethod(){
        boolean flag = true;
        // 本端是否为提供端，这里会返回true
       /* boolean isProviderSide = RpcContext.getContext().isProviderSide();
        if(isProviderSide){
            return flag;
        }*/
        // 获取调用方IP地址
        String clientIP = RpcContext.getContext().getRemoteHost();
        //服务器ip
        String localIp = null;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.info("ip白名单内报错" + e.getMessage());
            e.printStackTrace();
        }
        //本地调用，默认放行
        if(clientIP.equals(localIp) || "127.0.0.1".equals(clientIP) || "0:0:0:0:0:0:0:1".equals(clientIP)){
            return flag;
        }
        //配置的白名单列表
        String whiteIpList = ConfigurationUtil.getValue("whiteIpList");
        String whiteIps[] = null;
        //如果没有设置白名单，默认不校验ip
        if(StringUtils.isBlank(whiteIpList)){
            return flag;
        }
        whiteIps = whiteIpList.split(",");
        if(ArrayUtils.indexOf(whiteIps,clientIP) >= 0){
            return flag;
        }else{
            log.info("用户不在ip白名单内，clientIP=" + clientIP);
            flag = false;
            return flag;
        }
    }
    /**
     * 获取对象的真实IP地址
     */
    public static String getIpAddress() {
        return  RpcContext.getContext().getRemoteHost();
    }
}
