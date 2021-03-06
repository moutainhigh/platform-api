package com.xinleju.platform.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.finance.dao.AssMappingDao;
import com.xinleju.platform.finance.dto.AssMappingDto;
import com.xinleju.platform.finance.entity.AssMapping;
import com.xinleju.platform.finance.service.AssMappingService;
import com.xinleju.platform.finance.utils.DataType;
import com.xinleju.platform.finance.utils.Jaxb2Util;
import com.xinleju.platform.finance.utils.contract.Bill;
import com.xinleju.platform.finance.utils.contract.BillHead;
import com.xinleju.platform.finance.utils.contract.Ufinterface;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 * 
 */

@Service
public class AssMappingServiceImpl extends  BaseServiceImpl<String,AssMapping> implements AssMappingService{
	

	@Autowired
	private AssMappingDao assMappingDao;

	@Override
	public int saveAllAssMapp(String saveJson) throws Exception {
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> mappingList = JacksonUtils.fromJson(saveJson, List.class);//转Map
		if(mappingList.size()>0){
			String TypeId = (String) mappingList.get(0).get("assMappingId");//获取主键
			Map<String,Object> typePar = new HashMap<String,Object>();
			typePar.put("assMappingId", TypeId);
			
			List<AssMapping> entityList = assMappingDao.queryList(typePar);//获取数据库ruler对象列表
			
			for(int i=0;i<mappingList.size();i++){
				if(DataType.DATA_ADD.getCode()==mappingList.get(i).get("dataType")){
					assMappingDao.save(JacksonUtils.fromJson(JacksonUtils.toJson(mappingList.get(i)),AssMapping.class));//新增方法
				}else if(DataType.DATA_UPDATE.getCode()==mappingList.get(i).get("dataType")){
					//匹配数据库已存在对象
					for(int j=0;j<entityList.size();j++){
						if(entityList.get(j).getId().equals(mappingList.get(i).get("id"))){
							@SuppressWarnings("unchecked")
							Map<String,Object> RulerMap = JacksonUtils.fromJson(JacksonUtils.toJson(mappingList.get(i)), HashMap.class);
							@SuppressWarnings("unchecked")
							Map<String,Object> oldRulerMap = JacksonUtils.fromJson(JacksonUtils.toJson(entityList.get(j)), HashMap.class);
							oldRulerMap.putAll(RulerMap);
							assMappingDao.update(JacksonUtils.fromJson(JacksonUtils.toJson(oldRulerMap), AssMapping.class));//修改方法
						}
					}
				}else if(DataType.DATA_DELETE.getCode()==mappingList.get(i).get("dataType")){
					assMappingDao.deletePseudoObjectById((String)mappingList.get(i).get("id"));//伪删除方法
				}
			}
		}
		return 1;//返回成功
	}
	
	@Override
	public List queryListByAssTypeIds(List<String> paramList){
		return assMappingDao.queryListByAssTypeIds(paramList);
	}
	
	/**
	 * 推送到NC 
	 * @param map
	 * @return
	 */
	@Override
	public String createSyncXml2NC(String createJson,String sender){
		AssMappingDto dto = JacksonUtils.fromJson(createJson,AssMappingDto.class);
		Ufinterface ufi = new Ufinterface();
		ufi.setSubbilltype("");
		ufi.setSender(sender);
		ufi.setRoottag("");
		ufi.setReplace("Y");
		ufi.setReceiver("xlj");//集团公司编码
		ufi.setProc("add");
		ufi.setIsexchange("Y");
		ufi.setFilename("");
		ufi.setBilltype("defdoc");
		ufi.setAccount("02");//集团公司账套
		
		BillHead billhead = new BillHead();
		billhead.setDoccode(dto.getAssItemCode());
		billhead.setDocname(dto.getAssItemName());
		billhead.setDocsystype("0");
		billhead.setPkcorp("0001");
		billhead.setPkdefdoc("");
		billhead.setPkdefdoclist("XY04");
		billhead.setPkdefdoc1("");
		billhead.setSealflag("N");
		
		Bill bill = new Bill();
		bill.setId(dto.getAssItemCode());
		bill.setBillhead(billhead);
		
		ufi.setBill(bill);
		
		String xml = "";
		try {
			xml = Jaxb2Util.objContextXml(ufi);
//			System.out.println("生成NC的XML===="+xml);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return xml;
	}
}
