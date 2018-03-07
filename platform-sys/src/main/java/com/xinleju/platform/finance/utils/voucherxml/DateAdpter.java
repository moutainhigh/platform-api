package com.xinleju.platform.finance.utils.voucherxml;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;


public class DateAdpter extends XmlAdapter<String, Date> {
   public static String SIM_DATA_FORMAT="yyyy-MM-dd";
	@Override
	public Date unmarshal(String v) throws Exception {
		// TODO Auto-generated method stub
		if(v==null){
			return null;		
		}
		DateFormat df=new SimpleDateFormat(SIM_DATA_FORMAT);
		return df.parse(v);
	}

	@Override
	public String marshal(Date v) throws Exception {
		// TODO Auto-generated method stub
		DateFormat df=new SimpleDateFormat(SIM_DATA_FORMAT);
		return df.format(v);
	}

}
