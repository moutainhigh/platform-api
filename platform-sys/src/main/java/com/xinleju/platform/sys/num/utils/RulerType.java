package com.xinleju.platform.sys.num.utils;

public enum RulerType {
	FIXEDSERIAL("固定字符串", "fixedSerial"),ADDREDUCESERIAL ("自增数字字串", "addReduceSerial"),ENUMSERIAL("枚举字串", "enumSerial"),
	DATESERIAL("时间字串 ","dateSerial"),USERINFOSERIAL("用户字串","userInfoSerial"),FORMSERIAL("表单字串","formSerial"),DATESERIALNUMBER("流水号","dateSerialNumber");  
	    // 成员变量  
	    private String name;  
	    private String code;
	    
	    
		private RulerType(String name, String code) {
			this.name = name;
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}  
	    
    
}
