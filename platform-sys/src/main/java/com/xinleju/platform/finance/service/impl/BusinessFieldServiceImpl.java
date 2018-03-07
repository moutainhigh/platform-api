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
import com.xinleju.platform.finance.dao.BusinessFieldDao;
import com.xinleju.platform.finance.dto.BusinessFieldDto;
import com.xinleju.platform.finance.entity.BusinessField;
import com.xinleju.platform.finance.service.BusinessFieldService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class BusinessFieldServiceImpl extends  BaseServiceImpl<String,BusinessField> implements BusinessFieldService{
	

	@Autowired
	private BusinessFieldDao businessFieldDao;

	@Override
	public List<Map<String, Object>> getMapListByObjId(String paramater)
			throws Exception {
		return businessFieldDao.getMapListByBillId(paramater);
	}

	@Override
	public List<BusinessFieldDto> queryTreeList(Map<String, Object> map)throws Exception {
		List<BusinessFieldDto> list=new ArrayList<>();
	    Long level=0l;
	    Long num=1l;
	    List<Map<String, Object>> baseEntryList = businessFieldDao.queryTreeList(map);
	    if(baseEntryList!=null&&baseEntryList.size()>0){
	    	for (Map<String, Object> entryMap : baseEntryList) {
	    		BusinessFieldDto fieldDto=new BusinessFieldDto();
	    		transMapToBean(entryMap,fieldDto);
	    		fieldDto.setLevel(level);
	    		fieldDto.setLft(num);
	    		fieldDto.setIsLeaf(false);
	    		fieldDto.setExpanded(true);
	    		fieldDto.setLoaded(true);
	    		list.add(fieldDto);
	    		Map<String, Object> typeList = getbaseProjectTypeList(entryMap.get("id").toString(),list,level,num);
	    		num= (Long) typeList.get("num");
	    		fieldDto.setRgt(num);
	    		String isleaf = (String)typeList.get("isleaf");
	    		if("1".equals(isleaf)){
	    			fieldDto.setIsLeaf(true);
				}else{
					fieldDto.setIsLeaf(false);
				}
			}
	    }
	    return list;
	}
	
	public Map<String,Object> getbaseProjectTypeList(String id,List<BusinessFieldDto> list,Long level,Long num)throws Exception {
	   Map<String,Object> map=new HashMap<String, Object>();
	   Map<String,Object> resultmap=new HashMap<String, Object>();
	   map.put("parentId", id);
	   List<Map<String, Object>> baseEntryList = businessFieldDao.queryTreeList(map);
	   if(baseEntryList!=null&&baseEntryList.size()>0){
		  ++level;
		  for (Map<String, Object> entryMap : baseEntryList) {
			  ++num;
			  BusinessFieldDto fieldDto=new BusinessFieldDto();
			  transMapToBean(entryMap,fieldDto);
			  fieldDto.setLft(num);
			  fieldDto.setLevel(level);
			  fieldDto.setIsLeaf(true);
			  fieldDto.setExpanded(true);
			  fieldDto.setLoaded(true);
			  list.add(fieldDto);
			  Map<String, Object> projectTypeList = getbaseProjectTypeList(entryMap.get("id").toString(),list,level,num);
			  num= (Long) projectTypeList.get("num");
			  fieldDto.setRgt(num);
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

}
