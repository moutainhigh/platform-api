package com.xinleju.platform.finance.service.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.finance.dao.CashFlowItemDao;
import com.xinleju.platform.finance.dto.CashFlowItemDto;
import com.xinleju.platform.finance.entity.CashFlowItem;
import com.xinleju.platform.finance.service.CashFlowItemService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class CashFlowItemServiceImpl extends  BaseServiceImpl<String,CashFlowItem> implements CashFlowItemService{
	

	@Autowired
	private CashFlowItemDao cashFlowItemDao;

	@Override
	public List<CashFlowItemDto> queryTreeList(Map<String, Object> map)throws Exception {
		List<CashFlowItemDto> list=new ArrayList<>();
		   Long level=0l;
		   Long num=1l;
		   List<Map<String, Object>> baseEntryList = cashFlowItemDao.queryTreeList(map);
		   if(baseEntryList!=null&&baseEntryList.size()>0){
		    	for (Map<String, Object> entryMap : baseEntryList) {
		    		CashFlowItemDto beanDto=new CashFlowItemDto();
		    		transMapToBean(entryMap,beanDto);
		    		beanDto.setLevel(level);
		    		beanDto.setLft(num);
		    		beanDto.setIsLeaf(false);
		    		beanDto.setExpanded(true);
		    		beanDto.setLoaded(true);
		    		list.add(beanDto);
		    		Map<String, Object> typeList = getbaseProjectTypeList(entryMap,list,level,num);
		    		num= (Long) typeList.get("num");
		    		beanDto.setRgt(num);
		    		String isleaf = (String)typeList.get("isleaf");
		    		if("1".equals(isleaf)){
		    			beanDto.setIsLeaf(true);
					}else{
						beanDto.setIsLeaf(false);
					}
				}
		    }
		   return list;
	}
	
	public Map<String,Object> getbaseProjectTypeList(Map<String, Object> pMap,List<CashFlowItemDto> list,Long level,Long num)throws Exception {
		  Map<String,Object> map=new HashMap<String, Object>();
		  Map<String,Object> resultmap=new HashMap<String, Object>();
		  map.put("parentId", pMap.get("id"));
		  map.put("accountSetId", pMap.get("accountSetId"));
		  List<Map<String, Object>> baseEntryList = cashFlowItemDao.queryTreeList(map);
		  if(baseEntryList!=null&&baseEntryList.size()>0){
			  ++level;
			  for (Map<String, Object> entryMap : baseEntryList) {
				  ++num;
				  CashFlowItemDto beanDto=new CashFlowItemDto();
				  transMapToBean(entryMap,beanDto);
				  beanDto.setLft(num);
				  beanDto.setLevel(level);
				  beanDto.setIsLeaf(true);
				  beanDto.setExpanded(true);
				  beanDto.setLoaded(true);
				  list.add(beanDto);
				  Map<String, Object> projectTypeList = getbaseProjectTypeList(entryMap,list,level,num);
				  num= (Long) projectTypeList.get("num");
				  beanDto.setRgt(num);
			}
			  ++num;
		  }else{
			  resultmap.put("isleaf", "1");
			  ++num;
		  }
		  resultmap.put("num", num);
		  resultmap.put("list", list);
		  return resultmap;
	  }
	
	public static void transMapToBean(Map<String, Object> map, Object obj) throws Exception {  
		  
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
      PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  

      for (PropertyDescriptor property : propertyDescriptors) {  
          String key = property.getName();  
          if (map.containsKey(key)) {  
              Object value = map.get(key);  
              // 得到property对应的setter方法  
              Method setter = property.getWriteMethod();  
              setter.invoke(obj, value);  
          }  
      }  
  }
	
	public CashFlowItem queryBudgetcapByBudget(Map<String, Object> map)throws Exception {
		List<CashFlowItem> baseEntryList = cashFlowItemDao.queryBudgetcapByBudget(map);
		return baseEntryList.get(0);
	}

	@Override
	public List<CashFlowItem> queryCashFlowItemList(Map<String, Object> map)throws Exception {
		return cashFlowItemDao.queryCashFlowItemList(map);
	}

}
