package com.xinleju.platform.sys.org.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.out.app.org.entity.UserAuthDataOrgList;
import com.xinleju.platform.sys.org.service.OrgnazationService;
import com.xinleju.platform.tools.data.JacksonUtils;
//@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类  
//@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:applicationContext-old.xml","classpath:dubbo-producer.xml","classpath:dubbo-customer.xml"})  
public class OrgnazationServiceImplTest {
	
	/*@Autowired
	private OrgnazationService orgnazationService;
	
	@Test
	public void testNewFunc() {
//		OrgnazationService  service=(OrgnazationService)ctx.getBean("orgnazationService");
		Map<String,Object> map=new HashMap<>();
		map.put("userIds", "df4a183e1e534336bc86ae9143ff5899");
		map.put("appId", "6fbd2eb96cde4bb699e4e481b3bf8ce7");
		Map<String,UserAuthDataOrgList> res = null;
		try {
			res = orgnazationService.getUserDataAuthGroupAndBranchList(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
