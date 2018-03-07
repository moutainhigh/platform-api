package com.xinleju;

import java.io.IOException;

import com.xinleju.platform.utils.SpringContextHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
   public static void main(String[] args) {
	   try {
      	  ApplicationContext ctx = new  ClassPathXmlApplicationContext(new String[]{"applicationContext.xml","dubbo-producer.xml","dubbo-customer.xml"});
		  SpringContextHelper.setApplicationContext(ctx);
 		    //为保证服务一直开着， 利用输入流的阻塞来模拟
		  System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

   }
}
