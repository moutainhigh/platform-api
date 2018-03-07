package com.xinleju.platform.finance.dto.service.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xinleju.erp.flow.flowutils.bean.FlowResult;
import com.xinleju.erp.sm.extend.dto.FinaData;
import com.xinleju.erp.sm.extend.dto.FinaQueryParams;
import com.xinleju.erp.sm.extend.dto.FinaResult;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.finance.dto.CashFlowItemDto;
import com.xinleju.platform.finance.dto.VoucherBillDto;
import com.xinleju.platform.finance.dto.VoucherBillEntryDto;
import com.xinleju.platform.finance.dto.VoucherBillRelationDto;
import com.xinleju.platform.finance.dto.VoucherData;
import com.xinleju.platform.finance.dto.VoucherTemplateDto;
import com.xinleju.platform.finance.dto.service.VoucherBillDtoServiceCustomer;
import com.xinleju.platform.finance.entity.AccountCaption;
import com.xinleju.platform.finance.entity.AccountSet;
import com.xinleju.platform.finance.entity.AssMapping;
import com.xinleju.platform.finance.entity.AssType;
import com.xinleju.platform.finance.entity.BusinessField;
import com.xinleju.platform.finance.entity.BusinessObject;
import com.xinleju.platform.finance.entity.CashFlowItem;
import com.xinleju.platform.finance.entity.SysBizItem;
import com.xinleju.platform.finance.entity.VoucherBill;
import com.xinleju.platform.finance.entity.VoucherBillEntry;
import com.xinleju.platform.finance.entity.VoucherBillRelation;
import com.xinleju.platform.finance.entity.VoucherFormSetting;
import com.xinleju.platform.finance.entity.VoucherTemplate;
import com.xinleju.platform.finance.entity.VoucherTemplateEntry;
import com.xinleju.platform.finance.service.AccountCaptionService;
import com.xinleju.platform.finance.service.AccountSetService;
import com.xinleju.platform.finance.service.AssMappingService;
import com.xinleju.platform.finance.service.AssTypeService;
import com.xinleju.platform.finance.service.BusinessFieldService;
import com.xinleju.platform.finance.service.BusinessObjectService;
import com.xinleju.platform.finance.service.CashFlowItemService;
import com.xinleju.platform.finance.service.SysBizItemService;
import com.xinleju.platform.finance.service.VoucherBillEntryService;
import com.xinleju.platform.finance.service.VoucherBillRelationService;
import com.xinleju.platform.finance.service.VoucherBillService;
import com.xinleju.platform.finance.service.VoucherFormSettingService;
import com.xinleju.platform.finance.service.VoucherTemplateEntryService;
import com.xinleju.platform.finance.service.VoucherTemplateService;
import com.xinleju.platform.finance.service.VoucherTemplateTypeService;
import com.xinleju.platform.finance.utils.CommonConsumer;
import com.xinleju.platform.finance.utils.Jaxb2Util;
import com.xinleju.platform.finance.utils.QMap;
import com.xinleju.platform.finance.utils.voucherxml.AuxiliaryItem;
import com.xinleju.platform.finance.utils.voucherxml.Cashflowcase;
import com.xinleju.platform.finance.utils.voucherxml.Entry;
import com.xinleju.platform.finance.utils.voucherxml.Ufinterface;
import com.xinleju.platform.finance.utils.voucherxml.Voucher;
import com.xinleju.platform.finance.utils.voucherxml.VoucherBody;
import com.xinleju.platform.finance.utils.voucherxml.VoucherHead;
import com.xinleju.platform.out.app.base.service.BaseOutServiceCustomer;
import com.xinleju.platform.sys.base.dto.BaseSupplierDto;
import com.xinleju.platform.tools.data.JacksonUtils;
/**
 * @author admin
 * 
 *
 */
 
public class VoucherBillDtoServiceProducer implements VoucherBillDtoServiceCustomer{
	private static Logger log = Logger.getLogger(VoucherBillDtoServiceProducer.class);
	@Autowired
	private VoucherBillService voucherBillService;
	@Autowired
	private VoucherBillEntryService voucherBillEntryService;
	@Autowired
	private VoucherBillRelationService voucherBillRelationService;
	@Autowired
	private VoucherTemplateTypeService voucherTemplateTypeService;
	@Autowired
	private VoucherTemplateEntryService voucherTemplateEntryService;
	@Autowired
	private AccountSetService accountSetService;
	@Autowired
	private VoucherTemplateService voucherTemplateService;
	@Autowired
	private BusinessFieldService businessFieldService;
	@Autowired
	private AccountCaptionService accountCaptionService;
	@Autowired
	private AssMappingService assMappingService;
	@Autowired
	private AssTypeService assTypeService;
	@Autowired
	private CashFlowItemService cashFlowItemService;
	@Autowired
	private BusinessObjectService businessObjectService;
	@Autowired
	private VoucherFormSettingService voucherFormSettingService;
	@Autowired
	private SysBizItemService sysBizItemService;
	@Autowired
	private BaseOutServiceCustomer baseOutServiceCustomer;
	
	private String appCode;
	/**生成凭证成功的单据状态*/
	private static final Integer SUCCESS_CREATEED = 1;
	/** 借贷金额表达式符号 */
	private static final String D_M_SYMBOL = "[*]";
	/** 凭证分录概要模板 变量表达式 */
	private static final String VOUCHER_ENTRY_SUMMARY_VAR_EXPR = "\\{!(.+?):(.+?);\\}";
	private static final String EX_SE = "taxmny2";
	private static final String EX_NOSE = "notaxmny2";
	private static final String EX_SEN = "taxmny";
	private static final String SA_MX = "sa-mx";
	private static final String HK_SE = "nrushmny"; 
	private static final String FP_SE = "fptaxnmny"; 
	//辅助核算类型
	public static final String ASS_DEPT = "部门档案";
	public static final String ASS_PERSON = "人员档案";
	public static final String ASS_PUBLLER = "供应商辅助核算";
	public static final String ASS_PUBLLER_LJTH = "客商辅助核算";
	public static final String ASS_EXCENTER = "费用中心";
	public static final String ASS_BANKACCONT = "银行账户";
	public static final String ASS_CASHFLOWCASE = "现金流量项目";
	public static final String ASS_HOUSE_WORD = "房地产项目档案";
	public static final String ASS_HOUSE_INFO = "房产资料";
	public static final String ASS_CO_OBJECT = "成本核算对象";//河北添加 2016.9.22 chc
	public static final String ASS_COLLECTION_TYPE = "款项类型";//河北添加 2016.10.21 chc
	public static final String ASS_TYPE_INFO = "产品类型";
	public static final String ASS_DEPT_CO = "部门";
	public static final String ASS_TYPE_INFO_CO = "产品";
	public static final String ASS_PROJECT_INFO = "工程项目";
	public static final String ASS_PROJECT_BRANCE = "项目分期";
	public static final String ASS_PROJECT_HZ = "合作项目";
	public static final String ASS_CONTRACT = "合同编码";
	public static final String ASS_SUPPLIER = "供方档案";
	private String username ;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   VoucherBill voucherBill=JacksonUtils.fromJson(saveJson, VoucherBill.class);
		   voucherBillService.save(voucherBill);
		   info.setResult(JacksonUtils.toJson(voucherBill));
		   info.setSucess(true);
		   info.setMsg("保存对象成功!");
		} catch (Exception e) {
		 log.error("保存对象失败!"+e.getMessage());
		 info.setSucess(false);
		 info.setMsg("保存对象失败!");
		 info.setExceptionMsg(e.getMessage());
		}
	   return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBatch(String userInfo, String saveJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateBatch(String userInfo, String updateJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String userInfo, String updateJson)  {
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   VoucherBill voucherBill=JacksonUtils.fromJson(updateJson, VoucherBill.class);
			   int result=   voucherBillService.update(voucherBill);
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("更新对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   VoucherBill voucherBill=JacksonUtils.fromJson(deleteJson, VoucherBill.class);
			   int result= voucherBillService.deleteObjectById(voucherBill.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= voucherBillService.deleteAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除对象成功!");
				}
			} catch (Exception e) {
			 log.error("删除对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String getObjectById(String userInfo, String getJson)
	 {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			VoucherBill voucherBill=JacksonUtils.fromJson(getJson, VoucherBill.class);
			VoucherBill	result = voucherBillService.getObjectById(voucherBill.getId());
			info.setResult(JacksonUtils.toJson(result));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=voucherBillService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=voucherBillService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取分页对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取分页对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryList(String userInfo, String paramater){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List list=voucherBillService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=voucherBillService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getCount(String userInfo, String paramater)  {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   VoucherBill voucherBill=JacksonUtils.fromJson(deleteJson, VoucherBill.class);
			   int result= voucherBillService.deletePseudoObjectById(voucherBill.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= voucherBillService.deletePseudoAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除对象成功!");
				}
			} catch (Exception e) {
			 log.error("删除对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	/**
	 * @param object
	 * @param paramaterJson
	 * @return
	 */
	public String getVoucherById(String userInfo, String paramaterJson){
		VoucherData voucher = new VoucherData();
		VoucherBill voucherBill=JacksonUtils.fromJson(paramaterJson, VoucherBill.class);
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			VoucherBill	voucherBillEntity = voucherBillService.getObjectById(voucherBill.getId());
			VoucherBillDto voucherBillDto = new VoucherBillDto();
			Map map = new HashMap();
			map.put("voucherBillId", voucherBillEntity.getId());
			Page entryPage = voucherBillEntryService.getVoucherBillEntrypage(map);
			BeanUtils.copyProperties(voucherBillEntity, voucherBillDto);
			voucher.setVoucherBillDto(voucherBillDto);
			List<VoucherBillEntry> list=entryPage.getList();
			List<VoucherBillEntryDto> entryDtoList = new ArrayList<VoucherBillEntryDto>();
			for(VoucherBillEntry entry : list){
				VoucherBillEntryDto dto = new VoucherBillEntryDto();
				BeanUtils.copyProperties(entry, dto);
				entryDtoList.add(dto);
			}
			voucher.setEntryBillList(entryDtoList);
			Page relationPage = voucherBillRelationService.getVoucherBillRelationPage(map);
			List<VoucherBillRelation> relationlist=relationPage.getList();
			List<VoucherBillRelationDto> relationDtoList = new ArrayList<VoucherBillRelationDto>();
			for(VoucherBillRelation entry : relationlist){
				VoucherBillRelationDto dto = new VoucherBillRelationDto();
				BeanUtils.copyProperties(entry, dto);
				relationDtoList.add(dto);
			}
			voucher.setVoucherBillRelationList(relationDtoList);
			info.setResult(JacksonUtils.toJson(voucher));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * @param object
	 * @param paramaterJson
	 * @return
	 */
	@Override
	public String getVoucherBillPage(String userInfo, String paramater){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				String endtime = (String)map.get("enterDateEnd");
				endtime = endtime + " 23:59:59";
				map.put("enterDateEnd", endtime);
				Page page=voucherBillService.getVoucherBillPage(map);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=voucherBillService.getVoucherBillPage(new HashMap());
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取分页对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取分页对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 生成NC系统能识别的同步xml
	 * 
	 * @param voucher
	 * @param entryDataList
	 * @param accountSetDto 所属系统 SA：销售，CO：成本，EX：费用
	 * @return
	 */
	public String createSyncXml2NC(String voucherJson, List<VoucherBillEntryDto> entryDataList,String appCode,String sender){
		// 会计平台和NC系统的对照
		if (StringUtils.isBlank(appCode)) {
			log.error("根据凭证没有找到对应的账套");
		}
		if (StringUtils.isBlank(sender)) {
			log.error("凭证账套,对应的财务系统为空");
		}
		VoucherBillDto voucher=JacksonUtils.fromJson(voucherJson, VoucherBillDto.class);
		VoucherHead vh = new VoucherHead();
		vh.setCompany(voucher.getCompanyCode());
		vh.setReveiver("");
		vh.setVoucherId("0");
		// 凭证字
		vh.setVoucherType(voucher.getWord());
		vh.setFiscalYear(voucher.getFiscalYear().toString());
		String period = voucher.getAccountingPeriod().toString();
		if(period.length()==1){
			period = "0" + period;
		}
		vh.setAccountingPeriod(period);
		vh.setAttachmentNumber(voucher.getAttachmentNumber().toString());
		// 制单日期
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		vh.setPrepareddate(sf.format(voucher.getCreateDate()));
		// 在nc系统中对应的登录的用户名称 暂时设置系统里面的测试数据 wangf
		vh.setEnter(voucher.getEnterCode());
		vh.setCashier("");
		vh.setSignature("N");
		vh.setChecker("");
		// vh.setPostingDate(voucher.getPostingDate().toString());
		vh.setPostingPerson("");
		//外部系统名称 XS
		//vh.setVoucherMakingSystem("GL");
		if(appCode.equals("SA")){
			vh.setVoucherMakingSystem("XS");
		}else if(appCode.equals("CO")){
			vh.setVoucherMakingSystem("CB");
		}else{
			vh.setVoucherMakingSystem("EX");
		}
		vh.setMemo1("");
		vh.setMemo2("");
		vh.setReserve1(voucher.getBillType());
		vh.setReserve2(voucher.getEnterCode());

		List<Entry> enlist = new ArrayList<Entry>();
        Integer entyId = 0;
		for (int i = 0; i < entryDataList.size(); i++) {
			
			Entry entry = new Entry();
			VoucherBillEntryDto fed = entryDataList.get(i);
			String accountCode = fed.getCaptionCode();
			entry.setAccountCode(accountCode);
			entyId++;
			entry.setEntryId("" + entyId);
			entry.setAbstracT(fed.getSummary());
			entry.setSettlement("");
			entry.setDocumentId("");
			entry.setDocumentDate(new Date());
			// 货币
			entry.setCurrency("CNY");
			entry.setUnitPrice("");
			entry.setExchangeRate1("1");
			entry.setExchangeRate2("0");
			entry.setDebitQuantity("");
			// 四个金额配对
			String crmny = "0.0";
			String drmny = "0.0";
			if(fed.getCrmny()!=null && !fed.getCrmny().trim().equals("")){
//				BigDecimal bd = new BigDecimal(fed.getCrmny());  
				crmny = fed.getCrmny();
			}
			if(fed.getDrmny()!=null && !fed.getDrmny().trim().equals("")){
//				BigDecimal bd = new BigDecimal(fed.getDrmny());  
				drmny = fed.getDrmny();
			}
			entry.setPrimaryCreditAmount("" + crmny);
			entry.setPrimaryDebitAmount("" + drmny);
			entry.setNaturalCreditCurrency("" + crmny);
			entry.setNaturalDebitCurrency("" + drmny);
			entry.setSecondaryCreditAmount("");
			entry.setSecondaryDebitAmount("");
			entry.setCreditQuantity("");
			entry.setBillDate("");
			entry.setBillId("");
			entry.setBillType("");
			// 设置辅助核算相关数据
			// 辅助核算明细代码,逗号分隔
			// 辅助核算名称,逗号分隔
			String assCodes = fed.getAssCode();
			String assNames = fed.getAssName();
			
			// 如果辅助核算代码为空
			if (StringUtils.isNotBlank(assNames)) {
	
				Map<String, String[]> map = splitAssNameAndCode(assCodes, assNames);
				String[] assNameArr = map.get("name");
				String[] assCodeArr = map.get("code");
				List<AuxiliaryItem> auxiliaryItemList = new ArrayList<AuxiliaryItem>();
				for (int n = 0; n < assNameArr.length; n++) {
					AuxiliaryItem item = new AuxiliaryItem();
					String mappingName = "";
					String mappingValue = "";
					if(StringUtils.isNotBlank(assNameArr[n])){
						mappingName = assNameArr[n].trim();
					}
					if(StringUtils.isNotBlank(assCodeArr[n])){
						mappingValue = assCodeArr[n].trim();
					}
					item.setName(mappingName);
					item.setValue(mappingValue);
					auxiliaryItemList.add(item);
				}
				entry.setAuxiliaryList(auxiliaryItemList);
			}
			
			
			String cashflowCode = fed.getCashFlowCode();
			if(StringUtils.isNotBlank(cashflowCode)){
				List<Cashflowcase> CashflowcaseList = new ArrayList<Cashflowcase>();
				Cashflowcase cashFlow = new Cashflowcase();
				String money = "0.0";
				if(StringUtils.isNotBlank(entry.getPrimaryDebitAmount()) && !entry.getPrimaryDebitAmount().equals("0.0")){
					money = entry.getPrimaryDebitAmount();
				}else{
					money = entry.getPrimaryCreditAmount();
				}
				cashFlow.setMoney(money);
				cashFlow.setMoneyass("0");
				cashFlow.setMoneymain(money);
				cashFlow.setPk_cashflow(cashflowCode);
				CashflowcaseList.add(cashFlow);
				entry.setOtheruserdata(CashflowcaseList);
			}
			
			enlist.add(entry);
		}
		VoucherBody vb = new VoucherBody();
		vb.setEntry(enlist);
		Voucher voucherxml = new Voucher();
		voucherxml.setVoucherbody(vb);
		voucherxml.setVoucherhead(vh);
		Ufinterface ufi = new Ufinterface();
		ufi.setRoottag("voucher");
		ufi.setBilltype("gl");
		ufi.setReplace("Y");
		//设置发送方
		ufi.setSender(sender);
		ufi.setReceiver(voucher.getCompanyCode());
		ufi.setIsexchange("Y");
		ufi.setFilename(DateUtils.format(new Date(), "MM_DD_HH_mm") + ".xml");
		ufi.setProc("add");
		ufi.setOperation("req");
		ufi.setVoucher(voucherxml);
		String xml = "";
		try {
			xml = Jaxb2Util.objContextXml(ufi);
			System.out.println("生成NC的XML===="+xml);
			log.info("生成NC的XML===="+xml);
		} catch (JAXBException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return xml;
	}
	
	/**
	 * @param selectedList
	 * @param params
	 * @param paramMap
	 * @param companyId
	 * @return
	 */
	
	public FlowResult<String> createVoucher(String userJson,List<FinaData> selectedList, FinaQueryParams params, QMap paramMap,String companyId){
		String btype = (String) paramMap.get("btype");
		String accountSetId = (String) paramMap.get("accountSetId");
		appCode = (String) paramMap.get("appCode");
		username = userJson;
		// 用户登陆信息
//		String userJson = (String) paramMap.get("loginInfo");
		FlowResult<String> flowResult = new FlowResult<String>();
		List<String> errDesc = flowResult.getDebugInfo().getErrDesc();
		List<String> errBills = new ArrayList<String>();
		List<VoucherBillEntry> entryDataList = null;
		try{
		//合并单存放MAP中   KEY为合并单号   VALUE为合并的单据 
		Map<String,List<FinaData>> mergDates = new HashMap<String, List<FinaData>>();
		List<FinaData> datas = null;
		
		//存放不需要合并的单据
		List<FinaData> noMergDates = new ArrayList<FinaData>();
		
		// 根据业务表单类型过滤出来对应的凭证生成模板遍历匹配 凭证模板 status=1 启用的业务模板
		String typeId = params.getBillType();
		   	
		String key = null;
		for (FinaData finaData : selectedList) {
			finaData = voucherTemplateTypeService.setTempTemplate(finaData,typeId,appCode);
		
			//############################单据合并分拣 #######获取合并号
			String mergNum = (String)finaData.get("mergeNum");
			if(StringUtils.isNotBlank(mergNum)){
				//如果合并单号等于MAP的KEY的时候 则直接获取LIST集合，并将该单据放入
				//到该集合中，反之不等于的话则需要改变KEY的值，并将数据存贮到新的集合中
				if(mergNum.equals(key)){
					List<FinaData> dts = mergDates.get(key);
					dts.add(finaData);
				}else{
					key = mergNum;
					
					datas = new ArrayList<FinaData>();
					datas.add(finaData);
					mergDates.put(key, datas);
				}
					
			}else{
				noMergDates.add(finaData);
			}
			
			
		}
		// 如果没有匹配到账套返回错误信息，如果匹配到了继续向下
		if (errDesc.size() == 0) {
			// 生成相关分录
			//没有合并单据号单据生成凭证
			for (FinaData finaData : noMergDates) {
				boolean flag = false;//判断所有的分录都不匹配 // chc add 2016.6.23
				// 贷方金额总和
				double creditAmountSum = 0;
				// 借方金额总和
				double debitAmountSum = 0;
				entryDataList = new ArrayList<VoucherBillEntry>();
				if(!appCode.equals("SA")){
					//根据模板ID获取配置的分录（模板配置的分录）
					Map<String,Object> templateEntryMap = new HashMap<String,Object>();
					templateEntryMap.put("voucherTemplateId", finaData.get("voucherTemplateId"));
					templateEntryMap.put("sidx", "createDate");
					List<VoucherTemplateEntry> voucherEntryList = voucherTemplateEntryService.queryList(templateEntryMap);
					
					if (voucherEntryList.size() > 0) {
						List<VoucherBillEntry> entryDatas = null;
						int entryNumber = entryDataList.size();
						for (VoucherTemplateEntry entryTemp : voucherEntryList) {
							//根据单据生成的凭证分录
							entryDatas = creatEntryDatasByTemp(finaData, entryTemp, errDesc, accountSetId,params.getBillType(),entryNumber,errBills,flag,companyId,typeId);
							if(entryDatas != null){
								entryDataList.addAll(entryDatas);
								for(VoucherBillEntry entryData:entryDatas){
									creditAmountSum += Double.valueOf(entryData.getCrmny());
									debitAmountSum += Double.valueOf(entryData.getDrmny());
								}
							}
						}
						if(entryDataList==null || entryDataList.size()==0){
							errBills.add(finaData.get("id")+"");
						}
					} else {
						errDesc.add("单号：" + finaData.get("id") + "没有对应的分录模板！");
					}
				}else{
					List<VoucherBillEntry> entryDatas = null;
					int entryNumber = entryDataList.size();
					//根据单据生成的凭证分录
					entryDatas = creatEntryDatasByTempSA(finaData, null, errDesc, accountSetId,params.getBillType(),entryNumber,errBills,flag,companyId,typeId);
					if(entryDatas != null){
						entryDataList.addAll(entryDatas);
						for(VoucherBillEntry entryData:entryDatas){
							creditAmountSum += Double.valueOf(entryData.getCrmny());
							debitAmountSum += Double.valueOf(entryData.getDrmny());
						}
					}
					if(entryDataList==null || entryDataList.size()==0){
						errBills.add(finaData.get("id")+"");
					}
				}
				if(errDesc.size() <= 0 ){
				// 匹配到凭证生成模板，生成分录成功（没有报错信息），开始生成凭证，保存分录和业务表单数据
				if (entryDataList.size() > 0) {
					saveOrUpdateVoucherOrEntry(selectedList,flowResult,finaData,null,entryDataList,
							creditAmountSum,debitAmountSum,btype,params,accountSetId,errBills);
				} 
				}else{
					showErrDesc(flowResult);
				}
			}
			
			//
			//##########################存在单据合并号的单据生成凭证############
			//
			Iterator iter = mergDates.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String k = (String) entry.getKey();
				List<FinaData> finas = (List<FinaData>) entry.getValue();

				// 贷方金额总和
				double creditAmountSum = 0;
				// 借方金额总和
				double debitAmountSum = 0;
				
				entryDataList = new ArrayList<VoucherBillEntry>();
				for (FinaData finaData : finas) {
					boolean flag = false;
					if(!appCode.equals("SA")){
						//根据模板ID获取配置的分录（模板配置的分录）
						Map<String,Object> templateEntryMap = new HashMap<String,Object>();
						templateEntryMap.put("voucherTemplateId", finaData.get("voucherTemplateId"));
						List<VoucherTemplateEntry> voucherEntryList = voucherTemplateEntryService.queryList(templateEntryMap);
						
						if (voucherEntryList.size() > 0) {
							List<VoucherBillEntry> entryDatas = null;
							int entryNumber = entryDataList.size();
							for (VoucherTemplateEntry entryTemp : voucherEntryList) {
								//根据单据生成的凭证分录
								entryDatas = creatEntryDatasByTemp(finaData, entryTemp, errDesc, accountSetId,params.getBillType(),entryNumber,errBills,flag,companyId,typeId);
								if(entryDatas != null){
									entryDataList.addAll(entryDatas);
									for(VoucherBillEntry entryData:entryDatas){
										entryData.setId(IDGenerator.getUUID());
										creditAmountSum += Double.valueOf(entryData.getCrmny());
										debitAmountSum += Double.valueOf(entryData.getDrmny());
									}
								}
							}
							if(entryDataList==null || entryDataList.size()==0){
								errBills.add(finaData.get("id")+"");
							}
						} else {
							errDesc.add("单号：" + finaData.get("id") + "没有对应的分录模板！");
						}
					}else{
						List<VoucherBillEntry> entryDatas = null;
						int entryNumber = entryDataList.size();
						//根据单据生成的凭证分录
						entryDatas = creatEntryDatasByTempSA(finaData, null, errDesc, accountSetId,params.getBillType(),entryNumber,errBills,flag,companyId,typeId);
						if(entryDatas != null){
							entryDataList.addAll(entryDatas);
							for(VoucherBillEntry entryData:entryDatas){
								creditAmountSum += Double.valueOf(entryData.getCrmny());
								debitAmountSum += Double.valueOf(entryData.getDrmny());
							}
						}
						if(entryDataList==null || entryDataList.size()==0){
							errBills.add(finaData.get("id")+"");
						}
					}
				}
				// 匹配到凭证生成模板，生成分录成功（没有报错信息），开始生成凭证，保存分录和业务表单数据
				if(errDesc.size() <= 0 ){
				if (entryDataList.size() > 0) {
					saveOrUpdateVoucherOrEntry(selectedList,flowResult,null,finas,entryDataList,
							creditAmountSum,debitAmountSum,btype,params,accountSetId,errBills);
				}  
				}else {
					showErrDesc(flowResult);
				}
			}
			
			
		} else {
			showErrDesc(flowResult);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return flowResult;
	}
	
	private void saveOrUpdateVoucherOrEntry(List<FinaData> selectedList,FlowResult<String> flowResult,FinaData finaData,List<FinaData> finas,List<VoucherBillEntry> entryDataList,
			double creditAmountSum,double debitAmountSum,String btype,FinaQueryParams params,String accountSetId,List<String> errBills)throws Exception{
		try{
		Date now = new Date();
		String voucherWord = (String) selectedList.get(0).get("flag");
		flowResult.setSuccess(true);
		VoucherBill voucher = new VoucherBill();//凭证
		String voucherBillId = IDGenerator.getUUID();
		voucher.setId(voucherBillId);
		if(finaData != null){
			voucher.setTemplateTypeId((String)finaData.get("typeTempId"));
			voucher.setTemplateId((String)finaData.get("voucherTemplateId"));//父级单据类型
			voucher.setTemplateParentTypeId((String)finaData.get("typeTempParentId"));//父级单据类型
		}else{
			finaData = finas.get(0);
			voucher.setTemplateTypeId((String)finaData.get("typeTempId"));
			voucher.setTemplateId((String)finaData.get("voucherTemplateId"));//父级单据类型
			voucher.setTemplateParentTypeId((String)finaData.get("typeTempParentId"));//父级单据类型
		}
		voucher.setAccountSetId(accountSetId);
		voucher.setSendStatus("0");
		voucher.setVoucherNo("");
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		voucher.setCreateDate(Timestamp.valueOf(sdf.format(now)));
		voucher.setWord(voucherWord);
		BigDecimal   b   =   new   BigDecimal(debitAmountSum); 
		NumberFormat nf = NumberFormat.getInstance();
		voucher.setDebitAmount(nf.format(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()));
		BigDecimal   b1   =   new   BigDecimal(creditAmountSum); 
		voucher.setCreditAmount(nf.format(b1.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()));
		
		AccountSet acccountSet = accountSetService.getObjectById(accountSetId);
		
		voucher.setCompanyCode(acccountSet.getCompanyCode());
		if(appCode.equals("CO")){
			voucher.setBillType(finaData.get("billType")+"");
		}else{
			voucher.setBillType(btype);
		}
		SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();

		voucher.setFiscalYear(Integer.parseInt(DateUtils.format(now, "yyyy"))+"");// 会计年度
		voucher.setAccountingPeriod(Integer.parseInt(DateUtils.format(now, "MM"))+"");// 会计期间
		voucher.setAttachmentNumber(selectedList.size()+"");// 附单据数
		voucher.setEnterDate(sdf.format(now));// 制单时间
		voucher.setEnterId(user.getSecurityUserDto().getId());
		voucher.setEnterName(user.getSecurityUserDto().getRealName());// 制单人
		voucher.setEnterCode(user.getSecurityUserDto().getLoginName());
		voucher.setDelflag(false);
		voucherBillService.save(voucher);

		//合并分录
		List<VoucherBillEntry> hebingEntryDataList = new ArrayList<VoucherBillEntry>();
//		if(!appCode.equals("CO")){
			Map<String,VoucherBillEntry> entryMap = new LinkedHashMap<String,VoucherBillEntry>();
			for (VoucherBillEntry entryData : entryDataList) {
				entryData.setVoucherBillId(voucherBillId);
				if(entryMap.get(entryData.getSummary()+entryData.getCaptionName()+entryData.getRealAssName())!=null){
					VoucherBillEntry data = entryMap.get(entryData.getSummary()+entryData.getCaptionName()+entryData.getRealAssName());
					BigDecimal   bcrmny   =   new   BigDecimal(Double.parseDouble(entryData.getCrmny().replace(",", ""))+Double.parseDouble(data.getCrmny().replace(",", "")));
					BigDecimal   bdrmny   =   new   BigDecimal(Double.parseDouble(entryData.getDrmny().replace(",", ""))+Double.parseDouble(data.getDrmny().replace(",", "")));
					entryData.setCrmny(nf.format(bcrmny.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()));
					entryData.setDrmny(nf.format(bdrmny.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()));
				}
				entryMap.put(entryData.getSummary()+entryData.getCaptionName()+entryData.getRealAssName(), entryData);
			}
			for (Map.Entry<String, VoucherBillEntry> entry : entryMap.entrySet()) {
				hebingEntryDataList.add(entry.getValue());
			}
//		}else{
//			hebingEntryDataList.addAll(entryDataList);
//		}
		// 给分录加上voucherId等信息  
		//处理多辅助核算的分录
		for (VoucherBillEntry entryData : hebingEntryDataList) {
			entryData.setId(IDGenerator.getUUID());
			entryData.setVoucherBillId(voucher.getId());
			if(entryData.getCrmny().equals("0") && entryData.getDrmny().equals("0")){
				continue;
			}
			BigDecimal   bcrmny   =   new   BigDecimal(Double.parseDouble(entryData.getCrmny().replace(",", "")));
			BigDecimal   bdrmny   =   new   BigDecimal(Double.parseDouble(entryData.getDrmny().replace(",", "")));
			entryData.setCrmny(nf.format(bcrmny.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()));
			entryData.setDrmny(nf.format(bdrmny.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()));
			entryData = getAssCompant(entryData);
			entryData.setCreateDate(Timestamp.valueOf(sdf.format(now)));
			entryData.setCreatePersonId(user.getSecurityUserDto().getId());
		}
		voucherBillEntryService.saveBatch(hebingEntryDataList);
		
		//校验是否为不完整凭证
		Map<Integer,String> messMap = voucherBillService.isFull(voucher, hebingEntryDataList,accountSetId);
		if(messMap.get(1).equals("true")){
			voucher.setStatus("1");
		}else{
			voucher.setNotFullError(messMap.get(2));
			voucher.setStatus("2");
		}
		voucherBillService.update(voucher);
		
		List<String> billIds = new ArrayList<String>(selectedList.size());
		List<VoucherBillRelation> voucherBillList = new ArrayList<VoucherBillRelation>(selectedList.size());
		// 生成相关单据 并将id放入到list中
		if(finas != null && finas.size() > 0){
			for (FinaData fina : finas) {
				VoucherBillRelation bill = fillExBill(fina, SUCCESS_CREATEED, params.getBillType());
				bill.setVoucherBillId(voucher.getId());
				bill.setUrl(getBillUrl(finaData));
				bill.setId(IDGenerator.getUUID());
				voucherBillList.add(bill);
				billIds.add(String.valueOf(fina.get("id")));
			}
		}else{
			VoucherBillRelation bill = fillExBill(finaData, SUCCESS_CREATEED, params.getBillType());
			bill.setVoucherBillId(voucher.getId());
			bill.setUrl(getBillUrl(finaData));
			bill.setId(IDGenerator.getUUID());
			voucherBillList.add(bill);
			billIds.add(String.valueOf(finaData.get("id")));
		}
		 
		voucherBillRelationService.saveBatch(voucherBillList);
		//######################调用平台的回写接口 费用接口 start##############
		rewrite(voucher, null,errBills,null,appCode);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	private String getBillUrl(FinaData finaData) throws Exception{
		String billUrl = ""; // 业务数据url地址
		VoucherTemplate	voucherTemplate = voucherTemplateService.getObjectById(finaData.get("voucherTemplateId")+"");
//		BusinessObject	businessObject = businessObjectService.getObjectById(voucherTemplate.getBizObjectId());
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bizObjectId", voucherTemplate.getBizObjectId());
		map.put("urlTypeFlag", 1);
		// 查询业务对象注册字段
		List<BusinessField> list = businessFieldService.queryList(map);
		if(list != null && list.size() > 0){
			Map<String,Object> formSettingMap = new HashMap<String,Object>();
			formSettingMap.put("bizObjectId", voucherTemplate.getBizObjectId());
			formSettingMap.put("urlType", list.get(0).getCode());
			List<VoucherFormSetting> formSettingList= voucherFormSettingService.queryList(formSettingMap);
			if(formSettingList != null && formSettingList.size() > 0)
				billUrl = formSettingList.get(0).getUrl();
		}
		return billUrl;
	}
	
	/**
	 * 根据分录模板生成凭证分录
	 * 
	 * @param finaData
	 * @param errDesc
	 * @return
	 */
	private List<VoucherBillEntry> creatEntryDatasByTemp(FinaData finaData,
			VoucherTemplateEntry entryTemp, List<String> errDesc, String accountSetId,
			String billType, int entryNumber,List<String> errBills,boolean flag,String companyId,String typeId) {
		
		String errDescPrefix = String.format("单号：%s在使用业务类型为[%s]的凭证模板解析分录时:",
				finaData.get("id"), finaData.get("voucherTemplateName"));
		List<VoucherBillEntry> feds = new ArrayList<VoucherBillEntry>();
		VoucherBillEntry entryData = null;
		Double symbolValue = null;
		try{
			// 分录筛选条件
			// 如果为空直接生成此分录 存在筛选条件才进行匹配 TODO
			String filter = entryTemp.getFilter();
			
			// 预算科目 标识符 "00"
			String drm = entryTemp.getDrmnyexpr();//借方金额：{!notaxmny2:科目分摊不含税金额;}
			String drmPrarm = "";
			if (StringUtils.isNotBlank(drm)) {
				String[] syboms = drm.split(D_M_SYMBOL);
				drm = syboms[0];
				if(syboms.length == 2){
					symbolValue = Double.parseDouble(syboms[1]);
				}
				drmPrarm = converIK(drm);
			}
			if (StringUtils.isNotBlank(drm)) {
				VoucherTemplate	voucherTemplate = voucherTemplateService.getObjectById(entryTemp.getVoucherTemplateId());
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("bizObjectId", voucherTemplate.getBizObjectId());
				map.put("code", drmPrarm);
				// 查询业务对象注册字段
				List<BusinessField> list = businessFieldService.queryList(map);
				if(list != null && list.size() > 0){
					BusinessField field = list.get(0);
					//如果注册字段有父级，则取分摊数据
					if(StringUtils.isNotBlank(field.getParentId()) && !field.getParentId().equals("0")){
						feds = ftVoucherDrm(feds,finaData,filter,drm,drmPrarm,symbolValue,flag,entryData,entryTemp, errDesc, 
								accountSetId,errDescPrefix,companyId,errBills,entryNumber);
					}
					else {
						feds = voucherEntryDrm(feds,finaData,filter,drm,drmPrarm,symbolValue,flag,entryData,entryTemp, errDesc, 
								accountSetId,errDescPrefix,companyId,errBills,entryNumber);
					}
				}
				// 贷方分录处理
			} else {
				String crm = entryTemp.getCrmnyexpr();
				String crmPrarm = "";
				if (StringUtils.isNotBlank(crm)) {
					String[] syboms = crm.split(D_M_SYMBOL);
					crm = syboms[0];
					if(syboms.length == 2){
						symbolValue = Double.parseDouble(syboms[1]);
					}
					crmPrarm = converIK(crm);
				}
				
				VoucherTemplate	voucherTemplate = voucherTemplateService.getObjectById(entryTemp.getVoucherTemplateId());
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("bizObjectId", voucherTemplate.getBizObjectId());
				map.put("code", crmPrarm);
				// 查询业务对象注册字段
				List<BusinessField> list = businessFieldService.queryList(map);
				if(list != null && list.size() > 0){
					BusinessField field = list.get(0);
					//如果注册字段有父级，则取分摊数据
					if(StringUtils.isNotBlank(field.getParentId()) && !field.getParentId().equals("0")){
						feds = ftVoucherCrm(feds,finaData,filter,crm,crmPrarm,symbolValue,flag,entryData,entryTemp, errDesc, 
								accountSetId,errDescPrefix,companyId,errBills,entryNumber);
					}
					else {
						feds = voucherEntryCrm(feds,finaData,filter,crm,crmPrarm,symbolValue,flag,entryData,entryTemp, errDesc, 
								accountSetId,errDescPrefix,companyId,errBills,entryNumber);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return feds;
	}
	//科目分摊分录凭证  借方
	private List<VoucherBillEntry> ftVoucherDrm(List<VoucherBillEntry> feds,FinaData finaData,String filter,String drm,String drmPrarm,Double symbolValue,
			boolean flag,VoucherBillEntry entryData,VoucherTemplateEntry entryTemp, List<String> errDesc, String accountSetId,String errDescPrefix,
			String companyId,List<String> errBills,int entryNumber) throws Exception{
		// 科目分摊明细
		List<FinaData> fts = null;
		if(appCode.equals("SA")){
			fts = new ArrayList<FinaData>();
			fts.add(finaData);
		}else{
			if (drmPrarm.equals(HK_SE)) {
				fts = (List<FinaData>) finaData.get("hk");
			}else if (drmPrarm.equals(FP_SE)) {
				fts = (List<FinaData>) finaData.get("fp");
			}else if(drmPrarm.equals("nthisrushmny")){
				fts = (List<FinaData>) finaData.get("cz");
			}else{
				fts = (List<FinaData>) finaData.get("ft");
			}
		}
		// 生成科目分摊的分录
		for (FinaData ft : fts) {
			if (StringUtils.isNotBlank(filter)) {
				boolean isMach = voucherTemplateTypeService.macherEntry(filter, ft);
				if (!isMach) {
					continue;
				}
			}
			flag = true; // chc 2016.6.23 add
			entryData = createFiEntry(ft, entryTemp,errDescPrefix, errDesc,finaData);
			// 设置贷方金额为0
			entryData.setCrmny("0");
			// 借方金额
			Double drmAmount = 0d;
			String drmAmountStr = null;
			String[] syboms = drm.split(D_M_SYMBOL);
			drm = syboms[0];
			if(syboms.length == 2){
				symbolValue = Double.parseDouble(syboms[1]);
			}
			drmPrarm = converIK(drm);
			drmAmountStr = String.valueOf(ft.get(drmPrarm.trim()));
			if(StringUtils.isNotBlank(drmAmountStr) && !drmAmountStr.equals("null") && !drmAmountStr.trim().equals("0") && !drmAmountStr.trim().equals("0.0") && !drmAmountStr.trim().equals("0.00") && !drmAmountStr.trim().equals(".00")){
				drmAmount = Double.parseDouble(drmAmountStr);
				if(symbolValue != null){
					drmAmount = drmAmount * symbolValue;
				}
				NumberFormat nf = NumberFormat.getInstance();
		        nf.setGroupingUsed(false);
				entryData.setDrmny(nf.format(drmAmount));
				// 根据科目分摊编码获取对应的财务科目
				Object id = finaData.get("id");
				// 现金流量项目
				entryData = setCashFlow(entryData, ft, entryTemp,drmPrarm,accountSetId);
	
				AccountCaption fc = getAccountCaption(entryTemp,ft,accountSetId);
				
				if(fc != null){
					entryData.setCaptionCode(fc.getCode());
					entryData.setCaptionName(fc.getName());
					entryData.setCaptionId(fc.getId());
					// 设置辅助核算信息 TODO后面配置好需要动态调用对应的辅助核算
					if (StringUtils.isNotBlank(fc.getAssNames())) {
						entryData = setAssMaping(fc, ft, entryData,accountSetId,companyId,finaData,errDesc,appCode);
					}
				}else{
					String err = "没有匹配到财务科目【单据ID：" + id  + "】";
					errDesc.add(err);
					errBills.add(id+"");// chc 2016.6.23 add
					return feds;
				}
				
				feds.add(entryData);
			}
		}
		
		return feds;
	}
	
	//科目分录凭证  借方
	private List<VoucherBillEntry> voucherEntryDrm(List<VoucherBillEntry> feds,FinaData finaData,String filter,String drm,String drmPrarm,Double symbolValue,
			boolean flag,VoucherBillEntry entryData,VoucherTemplateEntry entryTemp, List<String> errDesc, String accountSetId,String errDescPrefix,
			String companyId,List<String> errBills,int entryNumber) throws Exception{
		if (StringUtils.isNotBlank(filter)) {
			boolean isMach = voucherTemplateTypeService.macherEntry(filter, finaData);
			if (!isMach) {
				return null;
			}
		}
		flag = true;
		
		entryData = createFiEntry(finaData, entryTemp, errDescPrefix,errDesc,finaData);
		// 设置贷方金额为0
		entryData.setCrmny("0");
		// 借方金额
		Double drmAmount = 0d;
		String drmAmountStr = String.valueOf(finaData.get(drmPrarm.trim()));
		
		if(StringUtils.isNotBlank(drmAmountStr) && !drmAmountStr.equals("null") &&!drmAmountStr.trim().equals("0") && !drmAmountStr.trim().equals("0.0") && !drmAmountStr.trim().equals("0.00") && !drmAmountStr.trim().equals(".00")){
			drmAmount = Double.parseDouble(drmAmountStr);
			//表达式处理
			if(symbolValue != null){
				drmAmount = drmAmount * symbolValue;
			}
			NumberFormat nf = NumberFormat.getInstance();
	        nf.setGroupingUsed(false);
			entryData.setDrmny(nf.format(drmAmount));

			AccountCaption fc = getAccountCaption(entryTemp,finaData,accountSetId);
			Object id = finaData.get("id");
			if(fc != null){
				entryData.setCaptionCode(fc.getCode());
				entryData.setCaptionName(fc.getName());
				entryData.setCaptionId(fc.getId());
				// 设置辅助核算信息 TODO后面配置好需要动态调用对应的辅助核算
				if (StringUtils.isNotBlank(fc.getAssNames())) {
					entryData = setAssMaping(fc, finaData, entryData,accountSetId,companyId,finaData,errDesc,appCode);
				}
			}else{
				String err = "没有匹配到财务科目【单据ID：" + id  + "】";
				errDesc.add(err);
				errBills.add(id+"");
				return feds;
			}
			feds.add(entryData);
		}
		return feds;
	}
	
	//科目分摊分录凭证  贷方
	private List<VoucherBillEntry> ftVoucherCrm(List<VoucherBillEntry> feds,FinaData finaData,String filter,String crm,String crmPrarm,Double symbolValue,
			boolean flag,VoucherBillEntry entryData,VoucherTemplateEntry entryTemp, List<String> errDesc, String accountSetId,String errDescPrefix,
			String companyId,List<String> errBills,int entryNumber) throws Exception{
		
		// 科目分摊明细
		List<FinaData> fts = null;
		if(appCode.equals("SA")){
			fts = new ArrayList<FinaData>();
			fts.add(finaData);
		}else{
			if (crmPrarm.equals(HK_SE)) {
				fts = (List<FinaData>) finaData.get("hk");
			}else if (crmPrarm.equals(FP_SE)) {
				fts = (List<FinaData>) finaData.get("fp");
			}else if (crmPrarm.equals("nthisrushmny")) {
				fts = (List<FinaData>) finaData.get("cz");
			}else{
				fts = (List<FinaData>) finaData.get("ft");
			}
		}
		// 生成科目分摊的分录
		for (FinaData ft : fts) {
			if (StringUtils.isNotBlank(filter)) {
				boolean isMach = voucherTemplateTypeService.macherEntry(filter, ft);
				if (!isMach) {
					continue;
				}
			}
			
			entryData = createFiEntry(ft, entryTemp,errDescPrefix, errDesc,finaData);
			entryData.setDrmny("0");
			String crmAmountStr = ft.get(crmPrarm.trim())+"";
			Double crmAmount = 0d;
			if(StringUtils.isNotBlank(crmAmountStr) && !crmAmountStr.equals("null") && !crmAmountStr.trim().equals("0") && !crmAmountStr.trim().equals("0.0") && !crmAmountStr.trim().equals("0.00") && !crmAmountStr.trim().equals(".00")){
				crmAmount = Double.parseDouble(crmAmountStr);
				//表达式处理
				if(symbolValue != null){
					crmAmount = crmAmount * symbolValue;
				}
				NumberFormat nf = NumberFormat.getInstance();
		        nf.setGroupingUsed(false);
				entryData.setCrmny(nf.format(crmAmount));
				// 根据科目分摊编码获取对应的财务科目
				Object id = finaData.get("id");
				// 现金流量项目
				entryData = setCashFlow(entryData, ft, entryTemp,crmPrarm,accountSetId);

				AccountCaption fc = getAccountCaption(entryTemp,ft,accountSetId);
				
				if(fc != null){
					entryData.setCaptionCode(fc.getCode());
					entryData.setCaptionName(fc.getName());
					entryData.setCaptionId(fc.getId());
					// 设置辅助核算信息 TODO后面配置好需要动态调用对应的辅助核算
					if (StringUtils.isNotBlank(fc.getAssNames())) {
						entryData = setAssMaping(fc, ft, entryData,accountSetId,companyId,finaData,errDesc,appCode);
					}
				}else{
					String err = "没有匹配到财务科目【单据ID：" + id  + "】";
					errDesc.add(err);
					errBills.add(id+"");
					return feds;
				}
				
				feds.add(entryData);
			}
		}
		return feds;
	}
	
	//科目分录凭证  贷方
	private List<VoucherBillEntry> voucherEntryCrm(List<VoucherBillEntry> feds,FinaData finaData,String filter,String crm,String crmPrarm,Double symbolValue,
			boolean flag,VoucherBillEntry entryData,VoucherTemplateEntry entryTemp, List<String> errDesc, String accountSetId,String errDescPrefix,
			String companyId,List<String> errBills,int entryNumber) throws Exception{
		if (StringUtils.isNotBlank(filter)) {
			boolean isMach = voucherTemplateTypeService.macherEntry(filter, finaData);
			if (!isMach) {
				return null;
			}
		}
		flag = true;// chc 2016.6.23 add
		entryData = createFiEntry(finaData, entryTemp, errDescPrefix,errDesc,finaData);
		// 贷方金额
		entryData.setDrmny("0");
		Object co = finaData.get(crmPrarm.trim());
		String crmAmountStr = String.valueOf(co);
		Double crmAmount = 0d;
		if(StringUtils.isNotBlank(crmAmountStr) && !crmAmountStr.equals("null") && !crmAmountStr.trim().equals("0") && !crmAmountStr.trim().equals("0.0") && !crmAmountStr.trim().equals("0.00") && !crmAmountStr.trim().equals(".00")){
			crmAmount = Double.parseDouble(crmAmountStr);
			//表达式处理
			if(symbolValue != null){
				crmAmount = crmAmount * symbolValue;
			}
			NumberFormat nf = NumberFormat.getInstance();
	        nf.setGroupingUsed(false);
			entryData.setCrmny(nf.format(crmAmount));

			AccountCaption fc = getAccountCaption(entryTemp,finaData,accountSetId);
			Object id = finaData.get("id");
			if(fc != null){
				entryData.setCaptionCode(fc.getCode());
				entryData.setCaptionName(fc.getName());
				entryData.setCaptionId(fc.getId());
				// 设置辅助核算信息 TODO后面配置好需要动态调用对应的辅助核算
				if (StringUtils.isNotBlank(fc.getAssNames())) {
					entryData = setAssMaping(fc, finaData, entryData,accountSetId,companyId,finaData,errDesc,appCode);
				}
			}else{
				String err = "没有匹配到财务科目【单据ID：" + id  + "】";
				errDesc.add(err);
				errBills.add(id+"");
				return feds;
			}
			feds.add(entryData);
		}
		return feds;
	}
	
	//获取会计科目
	private AccountCaption getAccountCaption(VoucherTemplateEntry entryTemp,FinaData finaData,String accountSetId) throws Exception{
		AccountCaption fc = accountCaptionService.getObjectById(entryTemp.getCaptionId());
		if(fc == null && appCode.equals("EX")){
			String budgetcap = String.valueOf(finaData.get("vcurrentcode"));
			Map<String,Object> capMap = new HashMap<String,Object>();
			capMap.put("bizItemIds", budgetcap);
			capMap.put("accountSetId", accountSetId);
			List<AccountCaption> caplist = accountCaptionService.queryByBudCodeList(capMap);
			if(caplist != null && caplist.size()>0){
				fc = caplist.get(0);
			}
		}
		if(!appCode.equals("EX")){
			Map<String,Object> capMap = new HashMap<String,Object>();
			capMap.put("parentId", fc.getId());
			List<AccountCaption> capSunlist = accountCaptionService.queryList(capMap);
			String itemCode = "";
			String assName = "";
			//如果科目有系统业务数据，根据业务数据获取对照的科目
			if(capSunlist != null && capSunlist.size() > 0){
				itemCode = fc.getBizItemIds();
				assName = fc.getAssNames();
				if(StringUtils.isNotBlank(itemCode)){
					SysBizItem	result = sysBizItemService.getObjectById(itemCode);
					fc = getCapAss(capSunlist,result.getCode(),assName,finaData);
				}
			}//else{
				//获取辅助核算
			if(null != fc){
				while(null == fc.getAssNames() || StringUtils.isBlank(fc.getAssNames())  || fc.getAssNames().trim().equals("null")){
					AccountCaption fcParent = accountCaptionService.getObjectById(fc.getParentId());
					if(null != fcParent){
						fc.setParentId(fcParent.getParentId());
					}
					if (null != fcParent && StringUtils.isNotBlank(fcParent.getAssNames())  && !fcParent.getAssNames().trim().equals("null")) {
						fc.setAssNames(fcParent.getAssNames());
						break;
					}else if(null == fcParent){
						break;
					}
				}
			}
	//		}
		}
		return fc;
	}
	
	private AccountCaption getCapAss(List<AccountCaption> capSunlist,String itemCode,String assName,FinaData finaData){
		AccountCaption cap = null;
		try{
			if(capSunlist != null && capSunlist.size() > 0){
				for(AccountCaption dto : capSunlist){
					Map<String,Object> capSunMap = new HashMap<String,Object>();
					capSunMap.put("parentId", dto.getId());
					List<AccountCaption> capSun1list = accountCaptionService.queryList(capSunMap);
					if(cap == null && capSun1list != null && capSun1list.size() > 0 ){
						cap = getCapAss(capSun1list,itemCode,assName,finaData);
					}else{
//						for(AccountCaption dto1 : capSun1list){
							if(dto.getBizItemIds()!=null && dto.getBizItemIds().indexOf(finaData.get(itemCode)+"") > -1){
								if(StringUtils.isBlank(dto.getAssNames())){
									dto.setAssNames(assName);
								}
								cap = dto;
								break;
							}
//						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return cap;
	}
	
	/**
	 * 根据科目设置分录的辅助核算信息
	 * @param ac 会计科目
	 * @param finaData 业务数据
	 * @param entryData 生成的分录
	 * @param accountSetId 账套ID
	 * @return 
	 */
	public VoucherBillEntry setAssMaping(AccountCaption ac,FinaData finaData,VoucherBillEntry entryData,String accountSetId,String companyId,FinaData finaData1,List<String> errDesc,String appCode)throws Exception{
		String assNames = ac.getAssNames();
		String aicode = ac.getCode();
		if (StringUtils.isNotBlank(assNames)) {
			String assnames = "";
			String realAssNames = "";
			String asscodes = "";
			String assNameArrStr = "";
			String[] assNameArr = assNames.split(",");
			
			for (String assName : assNameArr){
				assNameArrStr += assName;
			}
			int i = 0;
				for (String assName : assNameArr) {
					Map<String,Object> assTypeMap = new HashMap<String,Object>();
					assTypeMap.put("assName", assName.trim());
					assTypeMap.put("accountSetId", accountSetId);
					assTypeMap.put("companyId", companyId);
					List<AssType> list=assTypeService.queryList(assTypeMap);
					AssType assType = null;
					if(list != null && list.size() > 0) {
						assType = list.get(0);
						//传输方式  1：核算代码  2：核算名称  3：业务对象代码  4：业务对象名称
						Integer isDireCode = Integer.valueOf(assType.getIsDirectCode());
						//默认设置  默认取第一个明细对照
						if(isDireCode == 5){
							Map<String,Object> assMap = new HashMap<String,Object>();
							assMap.put("assMappingId", assType.getId());
							List<AssMapping> assMaplist=assMappingService.queryList(assMap);
							if(assMaplist != null && assMaplist.size() > 0){
								assnames += assName + ",";
								realAssNames += assMaplist.get(0).getAssItemName() +",";
								asscodes += assMaplist.get(0).getAssItemCode()+",";
							}
						}else{
							Map<String, String> map = this.transformAss(finaData, assName, accountSetId,companyId,finaData1,errDesc,appCode);
							
							try{
								if(null != map && StringUtils.isNotBlank(map.get("err"))){
									errDesc.add(map.get("err"));
								}else{
									assnames += assName + ",";
									realAssNames += map.get("name")+",";
									asscodes += map.get("code")+",";
								}
							}catch(Exception e){
								if(null != map && StringUtils.isNotBlank(map.get("err"))){
									errDesc.add(map.get("err"));
								}else{
									String dataid = "";
									if(finaData.get("sourceid")!=null){
										dataid = finaData.get("sourceid")+"";
									}else{
										dataid = finaData.get("id")+"";
									}
									errDesc.add("单号"+dataid+"没有找到对应的辅助核算明细");
								}
							}
						}
					}
					
				entryData.setAssCode(asscodes);
				entryData.setAssName(assnames);
				entryData.setRealAssName(realAssNames);
			}
		}
		return entryData;
	}
	
	/**
	 * 转换辅助核算.根据给定的辅助核算名字和单据数据，从单据数据中找到对应的辅助核算字段，然后进行转换
	 * 
	 * @param finaData
	 * @param assName
	 * @param accountSetId
	 * @return {code:"代码",name:"名称"}
	 */
	private Map<String, String> transformAss(FinaData finaData, String assName, String accountSetId,String companyId,FinaData finaData1,List<String> errDesc,String appCode)throws Exception {
		StringBuffer debugInfo = new StringBuffer();
		debugInfo.append("\t要转换的辅助核算名称:").append(assName).append("\n");
		
		AssType assType = null;
		Map<String,Object> assTypeMap = new HashMap<String,Object>();
		assTypeMap.put("assName", assName.trim());
		assTypeMap.put("accountSetId", accountSetId);
		assTypeMap.put("companyId", companyId);
		List<AssType> assTypeList=assTypeService.queryList(assTypeMap);
		if(assTypeList != null && assTypeList.size() > 0){
			assType = assTypeList.get(0);
		}
		if (assType == null) {
			String err = "根据指定的账套id和辅助核算名字没有找到辅助核算信息【账套id:" + accountSetId + ",辅助核算名字:" + assName + "】";
			throw new RuntimeException(err);
		}
		Map<String, String> map = new HashMap<String, String>();
		//传输方式  1：核算代码  2：核算名称  3：业务对象代码  4：业务对象名称
		Integer isDireCode = Integer.valueOf(assType.getIsDirectCode());
		//传输核算代码或者名称
		if(isDireCode == 1 || isDireCode == 2){
			//根据辅助核算编码 获取辅助核算字段
			String assField = getFieldByAssName(assName.trim(),appCode);
			debugInfo.append("\t对应单据中的字段名称:").append(assField).append("\n");
			String assFieldValue = null;
			if (StringUtils.isNotBlank(assField)) {
				try {
					assFieldValue = finaData.get(assField)+"";
					//成本供应商辅助核算与合同辅助核算
					if(appCode.equals("CO") && (assName.trim().equals(ASS_PUBLLER) || assName.trim().equals(ASS_SUPPLIER))){
						if(StringUtils.isNotBlank(assFieldValue)){
							String dubboResultInfo=baseOutServiceCustomer.getSupplierById(username, "{\"id\":\""+assFieldValue+"\"}");
							DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
							if(dubboServiceResultInfo.isSucess()){
								String resultInfo= dubboServiceResultInfo.getResult();
								BaseSupplierDto voucherBillDto=JacksonUtils.fromJson(resultInfo, BaseSupplierDto.class);
								if(StringUtils.isNotBlank(voucherBillDto.getFinanceCode())){
									if(isDireCode == 1) map.put("code", voucherBillDto.getFinanceCode());
									if(isDireCode == 2) map.put("code", voucherBillDto.getName());
									map.put("name", voucherBillDto.getName());
									return map;
								}else{
									String err = "没有名称为'" + voucherBillDto.getName() + "'的供应商,没有对应的财务编码";
									map.put("err", err);
									return map;
								}
							}else{
								String err = "没有id为'" + assFieldValue + "'的供应商,请检查业务数据是否正确";
//								errDesc.add(err);
								map.put("err", err);
								return map;
							}
						}else{
							String err = "单据数据中没有属性'" + assField + "',请检查配置文件FiBizObject.json是否正确";
							map.put("err", err);
							return map;
						}
					}
					if(assFieldValue == null && assName.equals(ASS_PUBLLER)){
						assFieldValue = finaData.get("receiveunitid")+"";
					}
					if(assName.equals(ASS_DEPT) || assName.equals(ASS_DEPT_CO)){
						assFieldValue = finaData.get("entitydeptid")+"";
					    //TODO 后续添加科目分摊单据和主单据判断来获取属性
						if(assFieldValue == null){
					    	assFieldValue = finaData.get("deptid")+"";
					    }
					}
					if(assFieldValue == null && assName.equals(ASS_PERSON)){
						assFieldValue = finaData.get("vapplicant")+"";
					}
					if(assFieldValue == null && assName.equals(ASS_PERSON)){
						assFieldValue = finaData.get("receiveunitid")+"";
					}
					//如果以上在分摊数据中查找属性值，没有找到，则到主数据取属性值
					if(assFieldValue == null && assName.equals(ASS_PUBLLER)){
						assFieldValue = finaData.get("receiveunitid")+"";
					}
					if(assName.equals(ASS_DEPT)){
						assFieldValue = finaData.get("entitydeptid")+"";
					    //TODO 后续添加科目分摊单据和主单据判断来获取属性
						if(assFieldValue == null){
					    	assFieldValue = finaData.get("deptid")+"";
					    }
					}
					if(assFieldValue == null && assName.equals(ASS_PERSON)){
						assFieldValue = finaData.get("vapplicant")+"";
					}
					if(assFieldValue == null && assName.equals(ASS_PERSON)){
						assFieldValue = finaData.get("receiveunitid")+"";
					}
					
				} catch (Exception e) {
					String err = "单据数据中没有属性" + assField + "',请检查配置文件FiBizObject.json是否正确";
					map.put("err", err);
					return map;
				}
			} else {
				String err = "辅助核算属性未取到值";
				map.put("err", err);
				return map;
			}
			Map<String,Object> assMap = new HashMap<String,Object>();
			assMap.put("assMappingId", assType.getId());
			List<AssMapping> assMaplist=assMappingService.queryList(assMap);
			if (assMaplist == null) {
				String err = "根据辅助核算id没有查询出辅助核算明细对照【辅助核算名称：" + assName + "】";
				map.put("err", err);
				return map;
			}

			
			for (AssMapping detail : assMaplist) {
				String name = "";
				if(assName.equals(ASS_PERSON)){
					name =  String.valueOf(detail.getAssItemName());
				}else if(assName.trim().equals(ASS_HOUSE_INFO)){
//					name = String.valueOf(detail.getAssItemCode());
					name = String.valueOf(detail.getObjectItemCode());
				}else if(assName.trim().equals("合同编码")){
					name = String.valueOf(detail.getObjectItemCode());
				}
				else{
					name =  String.valueOf(detail.getObjectId());
				}
				           
				if (assFieldValue != null && assFieldValue.trim().equals(name.trim())) {
					String asscode = detail.getAssItemCode();
					String assname = detail.getAssItemName();
					//如果1传核算编码  2核算名称
					if(isDireCode == 1) map.put("code", asscode);
					if(isDireCode == 2) map.put("code", assname);
					
					map.put("name", assname);
					return map;
				}
			}
			if(map == null){
				map = new HashMap<String, String>();
				map.put("code", "");
				map.put("name", "");
				return map;
			}
		
		}
		//直接传输 编码或者名称
		if(isDireCode == 3 || isDireCode == 4){
			//TODO 暂时没有此情况  后续确定之后再继续补充代码
			map = new HashMap<String, String>();
			
			try {
				String fataxratio = finaData.get("fptaxratio")+"";
				map.put("code", fataxratio.substring(0, fataxratio.indexOf("."))+"%");
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			map.put("name", " ");
			return map;
		}
		debugInfo.append("没有找到对应的辅助核算明细");
		return null;

	}
	
	protected void rewrite(VoucherBill voucher,Map<String, Object> result,List<String> errBills,String errBillType,String appCode) throws Exception{
		List<String> billIds = new ArrayList<String>();
		String btype = "";
		String voucherWord = "";
		String voucherNo = "";
		String voucherId = "";
		Integer creatStatus = null;
		String createDate = DateUtils.format(voucher.getCreateDate(),"yyyy-MM-dd");
		
		VoucherTemplate voucherTemplate = voucherTemplateService.getObjectById(voucher.getTemplateId());
		BusinessObject	businessObject = businessObjectService.getObjectById(voucherTemplate.getBizObjectId());
		
		btype = businessObject.getName();
		
		if(errBills != null && errBills.size() > 0){
			billIds = errBills;
			//生成失败
			creatStatus = 2;
		}else{
			//生成成功
			creatStatus = 1;
			voucherId = voucher.getId();
			voucherWord = voucher.getWord();
			voucherNo = voucher.getVoucherNo();
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("voucherBillId", voucherId);
			List<VoucherBillRelation> bills = voucherBillRelationService.queryList(map);
			for(VoucherBillRelation bill:bills){
				String billId = bill.getBizId();
				billIds.add(billId);
			}
		}
		
		FinaResult fr = new FinaResult();
		fr.setBillIds(billIds);
		fr.setBillType(btype);
		if(creatStatus==1){ // chc add 2016.6.23
			fr.setVoucherWord(voucherWord);
			fr.setVoucherNumber(voucherNo);
			fr.setVoucherid(voucherId);
		}
		fr.setVoucherDate(createDate);
		fr.setCreateVoucherStatus(creatStatus);
		
		if(businessObject != null ){
//			HttpServletRequest  req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	      //业务对象 获取类
			 String fetchClass = businessObject.getCallbackClass();
			//业务对象 获取方法
			 String fetchMethod = businessObject.getCallbackMethod();
			//通过完整的类型路径获取类
		    Class<?> classType = Class.forName(fetchClass);
		    
		    CommonConsumer commonConsumer = new CommonConsumer();
		    
		    commonConsumer.loadConsumerToMap1(classType, fetchClass);
		    
		    Map<String,Object> consumerMap = commonConsumer.getConsumerMap();
		    
		    Object obj = consumerMap.get(fetchClass);
		    
		    
		    //获取InvokeTester类的方法
		    Method addMethod = classType.getDeclaredMethod(fetchMethod, new Class[]{FinaResult.class});
		    
		    //使用默认构造函数获取对象
	        //Object invokeTester = classType.newInstance();
	        //调用invokeTester对象上的方法
		    FlowResult flowResultRewrite  = (FlowResult) addMethod.invoke(obj,fr);
		    if(flowResultRewrite.isSuccess()){
	        	log.info("回写成功!");
		    }else{
		    	log.info("回写失败，因为：" + flowResultRewrite.getResult());
		    }
		}

	}
	
	public String converIK(String ikStr){
		//转换IK表达式
		Pattern pt = Pattern.compile(VOUCHER_ENTRY_SUMMARY_VAR_EXPR);
		Matcher mt = pt.matcher(ikStr);
		StringBuffer filResult = new StringBuffer();
		while (mt.find()) {
			mt.appendReplacement(filResult, mt.group(1));
		}
		mt.appendTail(filResult);
		return filResult.toString();
	}
	
	private Map<String, String[]> splitAssNameAndCode(String assCodes, String assNames) {
		String[] assNameArr = null;
		String[] assCodeArr = null;
		if (StringUtils.isNotBlank(assNames)) {
			assNameArr = assNames.split(",");
		}
		if (StringUtils.isNotBlank(assCodes)) {
			assCodeArr = assCodes.split(",");
		}
		if (assNameArr == null && assCodeArr != null) {
			String err = "凭证分录数据中指定了辅助核算明细代码，但是没有指定辅助核算名称";
			log.error(err);
			throw new RuntimeException(err);
		}
		if (assCodeArr == null && assNameArr != null) {
			String err = "凭证分录数据中指定了辅助核算名称，但是没有指定辅助核算明细代码";
			log.error(err);
			throw new RuntimeException(err);
		}
		if (assNameArr.length != assCodeArr.length) {
			String err = "辅助核算名称有" + assNameArr.length + "个,而辅助核算明细代码有" + assCodeArr.length + "个" + "。两个值的个数应该一致";
			log.error(err);
			throw new RuntimeException(err);
		}
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("name", assNameArr);
		map.put("code", assCodeArr);
		return map;
	}
	
	/**
	 * 组装辅助核算项目
	 * @param entryData
	 * @return
	 */
	protected VoucherBillEntry getAssCompant(VoucherBillEntry entryData){
		
		String conmpantStr = "";
		String[] assNameArray = null;
		String[] assCodeArray = null;
		String assName = entryData.getAssName();
        String assCode = entryData.getAssCode();
        String realAssName = entryData.getRealAssName();
        if(StringUtils.isNotBlank(assName)){
        	 assNameArray = assName.split(",");
        }
        if(StringUtils.isNotBlank(assCode)){
        	 assCodeArray = assCode.split(",");
        }
        String[] realAssNameArray = null;
        if(StringUtils.isNotBlank(realAssName) && !realAssName.equals(",")){
        	realAssNameArray = realAssName.split(",");
        }
        if(assNameArray != null && assCodeArray != null){
        	for(int i=0;i<assNameArray.length;i++){
				conmpantStr += "【" + assNameArray[i].substring(0, assNameArray[i].length())
						+ "：" + assCodeArray[i].substring(0, assCodeArray[i].length())
						+ "/"
						+ realAssNameArray[i].substring(0, realAssNameArray[i].length())
						+ "】";
				
			}
	        entryData.setAssCompent(conmpantStr);
	    }
        return entryData;
	}
	
	private void showErrDesc(FlowResult<String> flowResult) {
		flowResult.setSuccess(false);
	}
	
	/**
	 * 保存费用单据
	 * @param finaData
	 * @param status
	 */
	public VoucherBillRelation fillExBill(FinaData finaData,int status,String btype){
		VoucherBillRelation feb = new VoucherBillRelation();
		feb.setBizId(finaData.get("id")+"");
		feb.setCode(finaData.get("code")+"");
//		feb.setBillName(finaData.get("name")+"");
		if(btype.equals("领借款")){
	    	feb.setBillName(String.valueOf(finaData.get("vtheme")));
	    }
	    else if(btype.equals("日常报销")){
	    	feb.setBillName(String.valueOf(finaData.get("vtheme")));
	    }
	    else if(btype.equals("合同付款")){
	    	feb.setBillName(String.valueOf(finaData.get("vcontname")));
	    }
	    else if(btype.equals("领借款还款")){
	    	feb.setBillName(String.valueOf(finaData.get("vcontname")));
	    }
	    else if(btype.equals("收款单") || btype.equals("退款单") || btype.equals("结转单") || btype.equals("换票单") || btype.equals("放款单") || btype.equals("收结转")){
	    	feb.setBillName(String.valueOf(finaData.get("houseno")));
	    }else if(btype.equals("事项请示")){
	    	feb.setBillName(String.valueOf(finaData.get("vtheme")));
	    }else if(btype.equals("付款记录") || btype.equals("工程投入") || btype.equals("单独收发票")){
	    	feb.setBillName(String.valueOf(finaData.get("contractName")));
	    }else{
	    	feb.setBillName(String.valueOf(finaData.get("houseno")));
	    }
	    return feb;
	}
	
	/**
	 * 生成摘要和备注
	 * @param finaData
	 * @param crmny
	 * @param drmny
	 * @param ft
	 * @return
	 */
	private VoucherBillEntry createFiEntry(FinaData finaData,VoucherTemplateEntry entryTemp,String errDescPrefix,List<String> errDesc,FinaData finaData1){
		VoucherBillEntry entryData = new VoucherBillEntry();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		entryData.setCreateDate(Timestamp.valueOf(sdf.format(new Date())));
    	entryData.setSummary(createSummary(finaData, entryTemp.getSummary(), errDescPrefix, errDesc,finaData1));
    	entryData.setRemark(createSummary(finaData, entryTemp.getRemark(), errDescPrefix, errDesc,finaData1));
        return entryData;
	}
	
	/**
	 * 生成概要 解析模板 分析出其中的变量和名称
	 * 
	 * @param finaData
	 * @param summaryTemp
	 * @param errDescPrefix
	 * @param errDesc
	 * @return
	 */
	private String createSummary(FinaData finaData, String summaryTemp, String errDescPrefix, List<String> errDesc,FinaData finaData1) {
		Pattern pt = Pattern.compile(VOUCHER_ENTRY_SUMMARY_VAR_EXPR);
		Matcher mt = pt.matcher(summaryTemp);
		StringBuffer result = new StringBuffer();
		String key = "";
		while (mt.find()) {
			key = mt.group(1).trim();
			Object value = finaData.get(key);
			if(value == null){
				value = finaData1.get(key);
			}
			if(value == null){
				List<FinaData> fts = (List<FinaData>) finaData.get("ft");
				for (FinaData ft : fts) {
					value = ft.get(key);
					if(value !=null)
						break;
				}
			}
			if (value != null) {
				if (value instanceof Date) {
					value = DateUtils.format((Date) value, "yyyy-MM-dd");
				}
				if((value+"").indexOf("00:00:00") > 0){
					value = value.toString().substring(0, 10);
				}
				mt.appendReplacement(result, value.toString());
//				logger.info(finaData.get(key) + " " + finaData.get(key).getClass());
			} else {
				errDesc.add(errDescPrefix + "解析:【" + summaryTemp + "】无法从单据中找到【" + mt.group().toString() + "】对应的值！");
			}
		}
		mt.appendTail(result);
		return result.toString();
	}
	
	/**
	 * 设置凭证分录的现金流量项目
	 * @param fiEntryDate
	 * @param finaData
	 * @param entryTemp
	 * @return
	 */
	public VoucherBillEntry setCashFlow(VoucherBillEntry fiEntryDate,FinaData finaData,VoucherTemplateEntry entryTemp,String drmPrarm,String accountSetId)throws Exception{
		Map<String,Object> budgetMap = new HashMap<String,Object>();
	    budgetMap.put("accountSetId", accountSetId);
		if(entryTemp.getCashFlowId() != null && StringUtils.isNotBlank(entryTemp.getCashFlowName())){
			String cashFlowCode = "";
			String cashFlowName = "";
			if(!entryTemp.getCashFlowId().equals("00") && !entryTemp.getCashFlowId().equals("04")){
				//当选择模板中选择“预算科目对应的现金流量项目”时
				cashFlowCode = entryTemp.getCashFlowCode();
				cashFlowName = entryTemp.getCashFlowName();
			}else {
				if(entryTemp.getCashFlowId().equals("00")){
		    	    String capCode = String.valueOf(finaData.get("vcurrentcode"));
		    	    budgetMap.put("subjectCodes", capCode);
		    	    budgetMap.put("accountSetId", accountSetId);
		    	    CashFlowItem ficap = cashFlowItemService.queryBudgetcapByBudget(budgetMap);
		    		cashFlowCode = ficap.getCode();
					cashFlowName = ficap.getName();
				}else if(entryTemp.getCashFlowId().equals("04")){
					String capCode = String.valueOf(finaData.get("full_code"));
					budgetMap.put("subjectCodes", capCode);
					budgetMap.put("accountSetId", accountSetId);
					CashFlowItem fiCoCap = cashFlowItemService.queryBudgetcapByBudget(budgetMap); //取成本科目
					cashFlowCode = fiCoCap.getCode();
					cashFlowName = fiCoCap.getName();
				}
			}
			
			fiEntryDate.setCashFlowCode(cashFlowCode);
			fiEntryDate.setCashFlowName(cashFlowName);
		}
		return fiEntryDate;
	}
	
	/**
	 * 根据辅助核算名称获取辅助核算字段
	 * TODO 暂时硬编码  
	 * @param assName
	 * @return
	 */
	public String getFieldByAssName(String assName,String appCode){
		String field = "";
		if(assName.equals(ASS_PERSON)){
			field = "vapplicant";
			//field = "vreceiveunit";
		}
		if(assName.equals(ASS_DEPT) || assName.equals(ASS_DEPT_CO)){
			field = "deptid";
		}
		if(assName.equals(ASS_PUBLLER)){//ASS_SUPPLIER
			if(appCode.equals("CO")){
				field = "partyBOrgn_Id";
			}else{
				field = "skunitid";
			}
		}
		if(assName.equals(ASS_SUPPLIER)){
			if(appCode.equals("CO")){
				field = "partyBOrgn_Id";
			}else{
				field = "skunitid";
			}
		}
		if(assName.equals(ASS_PUBLLER_LJTH)){
			if(appCode.equals("CO")){
				field = "partyBOrgn_Id";
			}else{
				field = "skunitid";
			}
		}
		if(assName.equals(ASS_EXCENTER)){
			field = "ec";
		}
        if(assName.equals(ASS_CASHFLOWCASE)){
        	field = "cf";
		}
        if(assName.equals(ASS_BANKACCONT)){
        	if(appCode.equals("SA")){
        		field = "inassid";
        	}else{
        		field = "acount_Number";
        	}
		}
        if(assName.equals(ASS_COLLECTION_TYPE)){
        	if(appCode.equals("SA")){
        		field = "fundid";
        	}
		}
        if(assName.equals(ASS_HOUSE_WORD)){
        	if(appCode.equals("CO")){
        		field = "project_branch_id";
        	}else{
        		field = "projectid";
        	}
		}
        
        if(assName.equals(ASS_PROJECT_BRANCE)){
        	if(appCode.equals("CO")){
        		field = "project_branch_id";
        	}else{
        		field = "projectid";
        	}
		}
      //销售添加 2016.10.27 chc
        if(assName.equals(ASS_PROJECT_INFO)){
        	if(appCode.equals("CO")){
        		field = "project_branch_id";
        	}else{
        		field = "projectid";
        	}
		}
        if(assName.equals(ASS_TYPE_INFO) || assName.equals(ASS_TYPE_INFO_CO)){
        	field = "typeid";
		}
        if(assName.equals(ASS_HOUSE_INFO)){
        	field = "houseno";
		}
        //河北添加 2016.9.22 chc
		if(assName.equals(ASS_CO_OBJECT)){
			if(appCode.equals("CO")){
        		field = "project_branch_id";
        	}else{
        		field = "projectid";
        	}
		}
		//香港置地添加 2016.11.25 chc  ASS_PROJECT_HZ
		if(assName.equals(ASS_PROJECT_HZ)){
        	field = "subjectid";
		}
		if(assName.equals(ASS_CONTRACT)){
			field = "contractCode";
		}
		return field;
	}
	
	public FlowResult<String> rewrite(List<String> ids, VoucherTemplateDto voucherTemplateDto, String appCode){
		FlowResult<String> flowResult = new FlowResult<String>();
		flowResult.setSuccess(true);
		Integer creatStatus = 5;
		
		FinaResult fr = new FinaResult();
		fr.setBillIds(ids);
		fr.setBillType(voucherTemplateDto.getBizObjectName());
		fr.setCreateVoucherStatus(creatStatus);
    	
		try {
			BusinessObject	businessObject = businessObjectService.getObjectById(voucherTemplateDto.getBizObjectId());
			if(businessObject != null ){
				HttpServletRequest  req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		      //业务对象 获取类
				 String fetchClass = businessObject.getFetchClass();
				//业务对象 获取方法
				 String fetchMethod = businessObject.getFetchMethod();
				//通过完整的类型路径获取类
			    Class<?> classType = Class.forName(fetchClass);
			    
			    CommonConsumer commonConsumer = new CommonConsumer();
			    
			    commonConsumer.loadConsumerToMap(classType, fetchClass,req);
			    
			    Map<String,Object> consumerMap = commonConsumer.getConsumerMap();
			    
			    Object obj = consumerMap.get(fetchClass);
			    
			    
			    //获取InvokeTester类的方法
			    Method addMethod = classType.getDeclaredMethod(fetchMethod, new Class[]{String.class,String.class});
			    
			    //使用默认构造函数获取对象
		        //Object invokeTester = classType.newInstance();
		        //调用invokeTester对象上的方法
			    String ResultInfoJson  = (String) addMethod.invoke(obj,fr);
			    DubboServiceResultInfo voucherBillServiceResultInfo= JacksonUtils.fromJson(ResultInfoJson, DubboServiceResultInfo.class);
			    if(voucherBillServiceResultInfo.isSucess()){
		        	String mappingResultInfo= voucherBillServiceResultInfo.getResult();
		        	PageBeanInfo<FinaData> page=JacksonUtils.fromJson(mappingResultInfo, PageBeanInfo.class);
			    }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return flowResult;
	}
	
	/**
	 * 根据分录模板生成凭证分录---销售
	 * 
	 * @param finaData
	 * @param errDesc
	 * @return
	 */
	private List<VoucherBillEntry> creatEntryDatasByTempSA(FinaData finaData,
			VoucherTemplateEntry entryTemp, List<String> errDesc, String accountSetId,
			String billType, int entryNumber,List<String> errBills,boolean flag,String companyId,String typeId) {
		
		String errDescPrefix = String.format("单号：%s在使用业务类型为[%s]的凭证模板解析分录时:",
				finaData.get("id"), finaData.get("voucherTemplateName"));
		List<VoucherBillEntry> feds = new ArrayList<VoucherBillEntry>();
		VoucherBillEntry entryData = null;
		Double symbolValue = null;
		try{
			List<FinaData> fts = (List<FinaData>) finaData.get("ft");
			// 生成科目分摊的分录
			for (FinaData ft : fts) {
				//根据模板ID获取配置的分录（模板配置的分录）
				Map<String,Object> templateEntryMap = new HashMap<String,Object>();
				templateEntryMap.put("voucherTemplateId", ft.get("voucherTemplateId"));
				templateEntryMap.put("sidx", "createDate");
				List<VoucherTemplateEntry> voucherEntryList = voucherTemplateEntryService.queryList(templateEntryMap);
				
				if (voucherEntryList.size() > 0) {
					String filter = "";
					String drm = "";
					String drmPrarm = "";
					for (VoucherTemplateEntry entryTemp1 : voucherEntryList) {
						symbolValue = null;
						filter = entryTemp1.getFilter();
						if (StringUtils.isNotBlank(filter)) {
							boolean isMach = voucherTemplateTypeService.macherEntry(filter, ft);
							if (!isMach) {
								continue;
							}
						}
						drm = entryTemp1.getDrmnyexpr();//借方金额：{!notaxmny2:科目分摊不含税金额;}
						
						if (StringUtils.isNotBlank(drm)) {
							String[] syboms = drm.split(D_M_SYMBOL);
							drm = syboms[0];
							if(syboms.length == 2){
								symbolValue = Double.parseDouble(syboms[1]);
							}
							drmPrarm = converIK(drm);
						}
						if (StringUtils.isNotBlank(drm)) {
							VoucherTemplate	voucherTemplate = voucherTemplateService.getObjectById(entryTemp1.getVoucherTemplateId());
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("bizObjectId", voucherTemplate.getBizObjectId());
							map.put("code", drmPrarm);
							// 查询业务对象注册字段
							List<BusinessField> list = businessFieldService.queryList(map);
							if(list != null && list.size() > 0){
								BusinessField field = list.get(0);
								//如果注册字段有父级，则取分摊数据
								if(StringUtils.isNotBlank(field.getParentId()) && !field.getParentId().equals("0")){
									feds = ftVoucherDrm(feds,ft,filter,drm,drmPrarm,symbolValue,flag,entryData,entryTemp1, errDesc, 
											accountSetId,errDescPrefix,companyId,errBills,entryNumber);
								}
								else {
									feds = voucherEntryDrm(feds,ft,filter,drm,drmPrarm,symbolValue,flag,entryData,entryTemp1, errDesc, 
											accountSetId,errDescPrefix,companyId,errBills,entryNumber);
								}
							}
							// 贷方分录处理
						} else {
							String crm = entryTemp1.getCrmnyexpr();
							String crmPrarm = "";
							if (StringUtils.isNotBlank(crm)) {
								String[] syboms = crm.split(D_M_SYMBOL);
								crm = syboms[0];
								if(syboms.length == 2){
									symbolValue = Double.parseDouble(syboms[1]);
								}
								crmPrarm = converIK(crm);
							}
							
							VoucherTemplate	voucherTemplate = voucherTemplateService.getObjectById(entryTemp1.getVoucherTemplateId());
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("bizObjectId", voucherTemplate.getBizObjectId());
							map.put("code", crmPrarm);
							// 查询业务对象注册字段
							List<BusinessField> list = businessFieldService.queryList(map);
							if(list != null && list.size() > 0){
								BusinessField field = list.get(0);
								//如果注册字段有父级，则取分摊数据
								if(StringUtils.isNotBlank(field.getParentId()) && !field.getParentId().equals("0")){
									feds = ftVoucherCrm(feds,ft,filter,crm,crmPrarm,symbolValue,flag,entryData,entryTemp1, errDesc, 
											accountSetId,errDescPrefix,companyId,errBills,entryNumber);
								}
								else {
									feds = voucherEntryCrm(feds,ft,filter,crm,crmPrarm,symbolValue,flag,entryData,entryTemp1, errDesc, 
											accountSetId,errDescPrefix,companyId,errBills,entryNumber);
								}
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return feds;
	}
}
