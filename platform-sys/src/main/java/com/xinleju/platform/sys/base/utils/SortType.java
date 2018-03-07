package com.xinleju.platform.sys.base.utils;

public enum SortType {
	 SHIFTUP("上移", "1"),SHIFTDOWN("下移", "2"),STICK("置顶", "3"),TOBOTTOM ("置底", "4");  
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
		private SortType(String name, String code) {
			this.name = name;
			this.code = code;
		}
	
	    
}
