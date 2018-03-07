package com.xinleju.platform.finance.dto.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.flow.flowutils.bean.PageBean;
import com.xinleju.erp.sm.cache.api.SyncFinaCoData;
import com.xinleju.erp.sm.extend.dto.FinaData;
import com.xinleju.erp.sm.extend.dto.FinaQueryParams;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.finance.dto.service.AssMappingTaskServiceCustomer;
import com.xinleju.platform.finance.entity.AssMapping;
import com.xinleju.platform.finance.entity.AssType;
import com.xinleju.platform.finance.entity.SysRegister;
import com.xinleju.platform.finance.service.AssMappingService;
import com.xinleju.platform.finance.service.AssTypeService;
import com.xinleju.platform.finance.service.SysRegisterService;
import com.xinleju.platform.sys.base.utils.NCSendData;
import com.xinleju.platform.sys.base.utils.NCXMlParse;
import com.xinleju.platform.sys.org.service.OrgnazationService;
import com.xinleju.platform.tools.data.JacksonUtils;

public class AssMappingTaskServiceProducer implements AssMappingTaskServiceCustomer {
	private static Logger log = Logger.getLogger(AssMappingTaskServiceProducer.class);
	public static final String SKEY_CO_TASK_SEND_NC="getAndSendNC";
	
	@Autowired
	private OrgnazationService orgnazationService;
	/*@Resource
	private SyncFinaCoData syncFinaCoData;*/
	@Autowired
	private AssTypeService assTypeService;
	@Autowired
	private AssMappingService assMappingService;
	@Autowired
	private SysRegisterService sysRegisterService;
	
	@Override
	public String executeTask(String userJson, String jsontaskCode) {
		System.out.println("------MD----taskCode-----"+jsontaskCode);
		log.error("*****************************合同同步NC定时任务--"+jsontaskCode+"--");
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		String msg = "异常结束！";
		if (StringUtils.isNotBlank(jsontaskCode)) {
			Map<String, String> code = JacksonUtils.fromJson(jsontaskCode, HashMap.class);
			String taskCode = code.get("taskCode").trim();
			if(SKEY_CO_TASK_SEND_NC.equals(taskCode.trim())){
				try{
					getContractAndSendNC();
					msg = "任务正常结束！";
				}catch (Exception e) {
					e.printStackTrace();
					msg = "业务系统执行  合同送到辅助核算 任务失败，异常结束!";
				}
			}
		} 
		
		info.setSucess(true);
		info.setMsg(msg);
		log.error("*****************************执行完成"+JacksonUtils.toJson(info));
		return JacksonUtils.toJson(info);
	}
	private String getContractAndSendNC() throws Exception{
		   String msg = null;
		   String xmlFile = "";
		   String res = null;
		   Map<String,Object> param = new HashMap<String,Object>();
		   param.put("type", "company");
		   List<Map<String, Object>> companyRes=orgnazationService.getSubOrgByComId(param);
		   Map<String,Object> assMap = new HashMap<String,Object>();
		   assMap.put("assName", "合同编码");
		   assMap.put("delflag", "0");
		   List<AssType> assTypeList=assTypeService.queryList(assMap);
		   ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				   new String[] { "dubbo-customer-finance.xml" });
		   context.start();
		   SyncFinaCoData syncFinaCoData = (SyncFinaCoData) context.getBean("syncFinaCoData");
		   
		   List<AssMapping> assMappingList = new ArrayList<AssMapping>();
		   for(Map map : companyRes){
			   	FinaQueryParams finaQueryParams = new FinaQueryParams();
			    finaQueryParams.setCorpId(map.get("id")+"");
			    finaQueryParams.setCurrentPage(0);
			    finaQueryParams.setFlag("3");
			    FlowResult ResultInfoJson  = (FlowResult)syncFinaCoData.findContract(finaQueryParams);
			    ResultInfoJson  = (FlowResult)syncFinaCoData.findContract(finaQueryParams);
			    PageBean<FinaData> page=(PageBean<FinaData>)ResultInfoJson.getResult();
			    List<FinaData> list = page.getItems();
				List<Long> finaSuccessList = new ArrayList<Long>();
				List<Long> finaErrorList = new ArrayList<Long>();
				FinaData finaData = new FinaData();
				if(null!=list){
					for(FinaData objMap : list){
						for(AssType assType:assTypeList){
							if(assType.getCompanyId().equals(objMap.get("companyId")+"")){
								AssMapping assMappingDto = new AssMapping();
								assMappingDto.setId(IDGenerator.getUUID());
								assMappingDto.setAssMappingId(assType.getId());
								assMappingDto.setAssItemCode((String)objMap.get("code"));
								assMappingDto.setAssItemName((String)objMap.get("name"));
								assMappingDto.setObjectId(objMap.get("id")+"");
								assMappingDto.setObjectItemCode((String)objMap.get("code"));
								assMappingDto.setObjectItemName((String)objMap.get("name"));
								try{
									assMappingService.save(assMappingDto);
									finaSuccessList.add(Long.valueOf(objMap.get("id")+""));
									assMappingList.add(assMappingDto);
								}catch(Exception e){
									finaErrorList.add(Long.valueOf(objMap.get("id")+""));
									e.printStackTrace();
								}
								finaData.put("success", finaSuccessList);
								finaData.put("error", finaErrorList);
							}
						}
					}
				}
				syncFinaCoData.updateContractFiType(finaData);
		   }
		   
		   Map<String,Object> regmap = new HashMap<String,Object>();
		   regmap.put("status", 1);
		   regmap.put("delflag", 0);
		   List<SysRegister>  regList= sysRegisterService.queryList(regmap);
		   if(null != regList && regList.size() > 0){
			   SysRegister sysDto = regList.get(0);
			   String webUrl = sysDto.getWebUrl();
			   String url = webUrl + "?account=02&receiver=0001" ;
			   SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
			   for(AssMapping dto : assMappingList){
					String createJson = JacksonUtils.toJson(dto);
					xmlFile = assMappingService.createSyncXml2NC(createJson,sysDto.getSender());
					if (xmlFile != null && !xmlFile.trim().equals("") && !url.trim().equals("") && url != null) {
						res = NCSendData.getPostResponse(url, xmlFile);
					}
//						System.out.println("NC返回的信息===="+res);
					if (null != res) {
						if (NCXMlParse.XmlErrorCode(res) >= 0) {
							dto.setSendStatus("1");// 输出成功
							dto.setSendDate(sf.format(new Date()));
							dto.setErrmsg("");
						} else {
							String errorinfo = NCXMlParse.XmlErrorInfo(res);
							Integer errCode = NCXMlParse.XmlErrorCode(res);
							dto.setSendStatus("2");//  输出失败
							dto.setSendDate(sf.format(new Date()));
							String error = "错误代码：" + errCode.toString() + " 错误内容："+ errorinfo;
							dto.setErrmsg(error);
						}
					} else {
						dto.setSendStatus("0");// 未输出
						dto.setSendDate(sf.format(new Date()));
						dto.setErrmsg("生成xml文件失败，未输出！");
						msg = "推送失败";
					}
					assMappingService.update(dto);
				}
				msg = "推送成功";
		   }
		   return msg;
	   }
}
