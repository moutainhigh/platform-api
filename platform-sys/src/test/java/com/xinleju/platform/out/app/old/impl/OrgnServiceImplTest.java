package com.xinleju.platform.out.app.old.impl;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.service.api.extend.OrgnService;
import com.xinleju.erp.flow.service.api.extend.dto.CompanyDTO;
import com.xinleju.erp.flow.service.api.extend.dto.DeptDTO;
import com.xinleju.erp.flow.service.api.extend.dto.OrgnDTO;
import com.xinleju.platform.tools.data.JacksonUtils;

public class OrgnServiceImplTest {
	/*private  ApplicationContext ctx=null;
	@Before
	public  void preMain() {
		ctx = new  ClassPathXmlApplicationContext(new String[]{"applicationContext.xml","applicationContext-old.xml","dubbo-customer.xml","dubbo-old-producer.xml"});
	}

	@Test
	public void test() {
		OrgnService orgnService=(OrgnService)ctx.getBean("orgnServiceImpl");
		FlowResult<List<DeptDTO>> res=orgnService.getDeptListByUserId("4653c8df9a6344b889b151628cb5cada","df4a183e1e534336bc86ae9143ff5899");
		//userId=df4a183e1e534336bc86ae9143ff5899  4eed0182314c4aa78d3c60575397a350 13103df55c8848cd9b920c548fc300e7
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println(JacksonUtils.toJson(res));
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
	}
	
	@Test
	public void testMD() {
//		MDProjectCacheService  service=(MDProjectCacheService)ctx.getBean("mDProjectCacheServiceImpl");
		OrgnServiceImpl  service=(OrgnServiceImpl)ctx.getBean("orgnServiceImpl");
		String org[]={"4653c8df9a6344b889b151628cb5cada","fc18917d4f3842c9a824611f8638b2be"};
		FlowResult<List<CompanyDTO>> res=service.getCompanyListAll("PT","haha",null,null);
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println(JacksonUtils.toJson(res));
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
	}
	
	@Test
	public void testBase() {
		BaseAPIImpl  service=(BaseAPIImpl)ctx.getBean("baseAPIImpl");
		FlowResult<CompanyDTO> res=service.getCompanyById("4653c8df9a6344b889b151628cb5cada");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println(JacksonUtils.toJson(res));
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
		System.out.println("---------------------------------");
	}*/
	

}
