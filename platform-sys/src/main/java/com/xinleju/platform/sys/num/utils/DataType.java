package com.xinleju.platform.sys.num.utils;

public enum DataType {
	 DATA_ADD("新增", 1),DATA_DELETE("删除", 2),DATA_UPDATE("修改", 3);  
	    //数据维护状态  
	    private String name;  
	    private Integer code;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getCode() {
			return code;
		}
		public void setCode(Integer code) {
			this.code = code;
		}
		private DataType(String name, Integer code) {
			this.name = name;
			this.code = code;
		}
}
