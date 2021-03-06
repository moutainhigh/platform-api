package com.xinleju.platform.finance.utils;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinleju.platform.finance.dto.service.VoucherBillDtoServiceCustomer;

public class SendXmlToUrl {
  public static String  sendxml(String url,String xml){
	    URL realURL;
	    Logger logger = LoggerFactory.getLogger(VoucherBillDtoServiceCustomer.class);
		try {
			realURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)realURL.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Contect-type", "text/xml");
			connection.setRequestMethod("POST");
	        Writer writer =new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
	       //System.out.println(xml);
	        logger.info(xml);
	        IOUtils.write(xml, writer);
	        
	        InputStream inputStream = connection.getInputStream();
	        List<String> listString=IOUtils.readLines(inputStream);
	        StringBuffer sb=new StringBuffer();
	        for(int i=0;i<listString.size();i++){
	        	sb.append(listString.get(i));
	        }
	        String result=sb.toString();
	        return result;
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			return null;
		 }
		  
  }
  
     /**
     * 获取xml返回的错误信息 
     * @param xml
     * @return
     */
    public static String XmlErrorInfo(String xml){
    	 try {
			Document document = DocumentHelper.parseText(xml);
			Element root=document.getRootElement();
			Element sendresult=root.element("sendresult");
			if(sendresult!=null){
				Element resultdescription=sendresult.element("resultdescription");
				String result= resultdescription.getText();
				return result;
			}
		  } catch (Exception e) {
		 	// TODO Auto-generated catch block
			 e.printStackTrace();
			return null;
		  } 
    	 return null;
    	 
     }
    public static int XmlErrorCode(String xml){
    	try{
    		Document document = DocumentHelper.parseText(xml);
			Element root=document.getRootElement();
			Element sendresult=root.element("sendresult");
			if(sendresult!=null){
				Element resultdescription=sendresult.element("resultcode");
				String result= resultdescription.getText();
				return Integer.parseInt(result);
			}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return -1;
    	}
    	return -1;
    }
  
}
