package com.xinleju.platform.sys.res.utils;

public enum UserType {
        systemUser("系统管理员", "3"),adminUser("管理员", "2"),user("普通用户", "1");  
	    // 成员变量  
	    private String name;  
	    private String code;
	    
	    
		private UserType(String name, String code) {
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
