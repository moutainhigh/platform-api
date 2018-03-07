/**   
*
* @version V1.0   
*/
package com.xinleju.platform.sys.base.dto;

import java.lang.reflect.Field;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * xml工具类：
 * 	1、把一个对象转换为xml
 *  2、传入xml格式的String，获取其中某对象的值
 * @author wangjf
 */
public class XMLUtil {

	
	
	/**
	 * 把一个对象转换为xml报文格式的字符串
	 * @param obj
	 * @param sb
	 */
	public static String ObjToXML(List<Object> objList) {  
		/*if(obj==null){
			return null;
		}*/
		StringBuffer result = new StringBuffer("");
		result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		result.append("<RESOURCE>");
		for(Object obj:objList){
			result.append("<R1>");
			Field[] fields = obj.getClass().getDeclaredFields();
			if(fields!=null && fields.length>0){
				for(int i=0;i<fields.length;i++){
					Field field = fields[i];
					if(ReflectionUtils.invokeGetterMethod(obj,  field.getName())!=null){
						result.append("<").append(field.getName().toUpperCase()).append(">");
						result.append(ReflectionUtils.invokeGetterMethod(obj,  field.getName()));
						result.append("</").append(field.getName().toUpperCase()).append(">");
					}
				}
			}
			result.append("</R1>");
		}
        result.append("</RESOURCE>");
        return result.toString();
    }  
	
	
	public static String getValueInXML(String xml,String column) throws Exception{
		if(xml==null || xml.length()==0){
			return null;
		}
		
		 Document document;
		try {
			document = DocumentHelper.parseText(xml);
			//如果不是SOAP返回的报文，是XML字符串则不需要这行代码  
			String beanXml = document.getRootElement().element("Body").element(  
					column).asXML();  
			return beanXml;
		} catch (DocumentException e) {
			e.printStackTrace();
			throw e;
		}  
	}
	
}
