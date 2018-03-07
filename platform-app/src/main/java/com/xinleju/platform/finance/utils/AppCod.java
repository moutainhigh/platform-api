package com.xinleju.platform.finance.utils;

public enum AppCod {
	//SA：销售系统，CO：成本系统，EX：费用系统
	SA("销售系统", "SA"),CO("成本系统", "CO"),EX("费用系统", "EX");  
	    // 成员变量  
	    private String name;  
	    private String code;
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
		private AppCod(String name, String code) {
			this.name = name;
			this.code = code;
		}
	
	    
}
