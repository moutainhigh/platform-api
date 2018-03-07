package com.xinleju.platform.finance.utils;

/**
 * Created by ly on 2017/6/8.
 */
public enum ExcelSheet {
    ACCOUNT_CAPTION("ACCOUNT_CAPTION", 0),CASH_FLOW_ITEM("CASH_FLOW_ITEM", 1),ASS_TYPE("ASS_TYPE", 2),
    ASS_MAPPING("ASS_MAPPING", 3),VOUCHER_TEMPLATE_TYPE("VOUCHER_TEMPLATE_TYPE", 4),VOUCHER_TEMPLATE("VOUCHER_TEMPLATE", 5),VOUCHER_TEMPLATE_ENTRY("VOUCHER_TEMPLATE_ENTRY", 6);

    private String name;
    private int code;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    private ExcelSheet(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
