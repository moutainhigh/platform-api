package com.xinleju.platform.sys.base.dto;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5{
 
	public static String getMd5(String plainText) {  
            MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
	            try {
					md.update(plainText.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					md.update(plainText.getBytes());
				}  
	            byte b[] = md.digest();  
	  
	            int i;  
	  
	            StringBuffer buf = new StringBuffer("");  
	            for (int offset = 0; offset < b.length; offset++) {  
	                i = b[offset];  
	                if (i < 0)  
	                    i += 256;  
	                if (i < 16)  
	                    buf.append("0");  
	                buf.append(Integer.toHexString(i));  
	            }  
	            //32位加密  
	            return buf.toString();  
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
			return null;
    } 
	
	
	 private static String dumpBytes(byte[] bytes)
	  {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < bytes.length; ++i) {
	      if ((i % 32 == 0) && (i != 0)) {
	        sb.append("\n");
	      }
	      String s = Integer.toHexString(bytes[i]);
	      if (s.length() < 2) {
	        s = "0" + s;
	      }
	      if (s.length() > 2) {
	        s = s.substring(s.length() - 2);
	      }
	      sb.append(s);
	    }
	    return sb.toString();
	  }

	  public static final String toMD5(String strPassword) {
	    try
	    {
	      byte[] strTemp = strPassword.getBytes("UTF-8");
	      MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	      mdTemp.update(strTemp);
	      byte[] md = mdTemp.digest();
	      return new String(dumpBytes(md).toUpperCase());
	    }
	    catch (Exception e) {
	    }
	    return null;
	  }
	
}