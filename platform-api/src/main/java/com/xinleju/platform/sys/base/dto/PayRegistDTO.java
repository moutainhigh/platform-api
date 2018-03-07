package com.xinleju.platform.sys.base.dto;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * @author Jack
 *
 */
public class PayRegistDTO extends BaseDto {
	//公司主键
	private Integer corpid;
	//公司名称
	private String corpname;
	//部门主键
	private Integer deptid;
	//部门名称
	private String deptname;
	//类别
	private String vcategory;
	//导入状态
	private Integer importstatus;//0未导入,1导入
	//支付状态
	private Integer paystatus;//0未支付,1部分支付,2支付完全,3资金平台那边撤回
	//是否支付驳回
	private Integer bisoverrule;//0正常未驳回,1已支付驳回不能操作
	//业务单据编号
	private String vbusinesscode;
	//主题
	private String vtheme;
	//业务类型
	private String vbusinesstype;
	//申请金额
	private Double napplymny;
	//支付金额
	private Double npaymny;
	//申请日期
	private String dapplydate;
	//导入日期
	private String dimportdate;
	//支付日期
	private String dpaydate;
	//来源主键
	private Integer sourceid;
	//来源类型
	private String vsourcetype;
	//经办人
	private Integer operatorid;
	//经办人
	private String voperator;
	//审批人
	private Integer vapproveid;
	//审批状态
	private String vapprovestatus;//0:草稿 1:审批中 2:审批通过 4:已作废
	//审批日期
	private String dapprovedate;
	//审批批语
	private String vapprovenote;
	//审批单号
	private String vapprovecode;
	//结束状态 
	private Integer vendstatus;
	//是否可编辑
	private Integer biscanedit;//是否走线上审批,1为线上审批
	//资金平台同步方法,返回结果中资金平台的唯一标识
	private String payformid;
	//预留字段
	private String reverse1;//资金平台撤回日期
	private String reverse2;
	private String reverse3;
	private String reverse4;
	private String reverse5;
	private Double reverse6;
	private Double reverse7;
	private Double reverse8;
	private Double reverse9;
	private Double reverse10;
	private String reverse11;
	private String reverse12;
	private String reverse13;
	//waylonglong 2016.12.21 资金平台同步属性
    /** 付款单位编号 */
    private String payorgcode;
    /** 付款单位名称 */
    public String payorgname;
    /** 付款银行账号 */
    public String payaccountcode;
    /** 付款账户名称 */
    public String payaccountname;
    /** 付款银行 */
    public String paybankname;
    /**
     * 表单模板id
     */
    private String customFormId;
    /** 收款单位编号 */
    private String recorgcode;
    /** 收款单位名称 */
    private String recorgname;
    /** 收款账户编号 */
    private String recaccountno;
    /** 收款账户名称 */
    private String recaccountname;
    /** 收款行名称 */
    private String recbankname;
    /** 付款对象类型   1：公对公2：公对私*/
    private String bankpaytype;
    /** 收款行所在省*/
    private String recprovince;
    /** 收款行所在市*/
    private String recareanameofcity;
    /**
	 * 支付方式  ,Y
	 * 1.电汇 2.现金 3.支票 4.代扣代缴 5.其他
	 */
	private String paymenttype;
	/**
	 * 收款行机构号  ,N
	 */
	private String bankcodeofrec;//收款行机构号  ,N
	/**
	 * 收款行联行号  ,N
	 */
	private String bankexccodeofrec;//收款行联行号  ,N
	/**
	 * 收款行CNAPS号  ,N
	 */
	private String cnapsofrec;//收款行CNAPS号  ,N
	/**
	 * 表单模板格式
	 */
	private String formSearchSeniorKey;
	public String getFormSearchSeniorKey() {
		return formSearchSeniorKey;
	}
	public void setFormSearchSeniorKey(String formSearchSeniorKey) {
		this.formSearchSeniorKey = formSearchSeniorKey;
	}
	public String getBankcodeofrec() {
		return bankcodeofrec;
	}
	public void setBankcodeofrec(String bankcodeofrec) {
		this.bankcodeofrec = bankcodeofrec;
	}
	public String getBankexccodeofrec() {
		return bankexccodeofrec;
	}
	public void setBankexccodeofrec(String bankexccodeofrec) {
		this.bankexccodeofrec = bankexccodeofrec;
	}
	public String getCnapsofrec() {
		return cnapsofrec;
	}
	public void setCnapsofrec(String cnapsofrec) {
		this.cnapsofrec = cnapsofrec;
	}
	public String getPaymenttype() {
		return paymenttype;
	}
	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}
	public String getBankpaytype() {
		return bankpaytype;
	}
	public void setBankpaytype(String bankpaytype) {
		this.bankpaytype = bankpaytype;
	}
	public String getRecprovince() {
		return recprovince;
	}
	public void setRecprovince(String recprovince) {
		this.recprovince = recprovince;
	}
	public String getRecareanameofcity() {
		return recareanameofcity;
	}
	public void setRecareanameofcity(String recareanameofcity) {
		this.recareanameofcity = recareanameofcity;
	}
	public String getRecaccountno() {
		return recaccountno;
	}
	public void setRecaccountno(String recaccountno) {
		this.recaccountno = recaccountno;
	}
	public String getRecaccountname() {
		return recaccountname;
	}
	public void setRecaccountname(String recaccountname) {
		this.recaccountname = recaccountname;
	}
	public String getRecbankname() {
		return recbankname;
	}
	public void setRecbankname(String recbankname) {
		this.recbankname = recbankname;
	}
	public String getRecorgcode() {
		return recorgcode;
	}
	public void setRecorgcode(String recorgcode) {
		this.recorgcode = recorgcode;
	}
	public String getRecorgname() {
		return recorgname;
	}
	public void setRecorgname(String recorgname) {
		this.recorgname = recorgname;
	}
	public String getCustomFormId() {
		return customFormId;
	}
	public void setCustomFormId(String customFormId) {
		this.customFormId = customFormId;
	}
	public Integer getCorpid() {
		return corpid;
	}
	public void setCorpid(Integer corpid) {
		this.corpid = corpid;
	}
	public String getCorpname() {
		return corpname;
	}
	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}
	public Integer getDeptid() {
		return deptid;
	}
	public void setDeptid(Integer deptid) {
		this.deptid = deptid;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getVcategory() {
		return vcategory;
	}
	public void setVcategory(String vcategory) {
		this.vcategory = vcategory;
	}
	public Integer getImportstatus() {
		return importstatus;
	}
	public void setImportstatus(Integer importstatus) {
		this.importstatus = importstatus;
	}
	public Integer getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
	}
	public Integer getBisoverrule() {
		return bisoverrule;
	}
	public void setBisoverrule(Integer bisoverrule) {
		this.bisoverrule = bisoverrule;
	}
	public String getVbusinesscode() {
		return vbusinesscode;
	}
	public void setVbusinesscode(String vbusinesscode) {
		this.vbusinesscode = vbusinesscode;
	}
	public String getVtheme() {
		return vtheme;
	}
	public void setVtheme(String vtheme) {
		this.vtheme = vtheme;
	}
	public String getVbusinesstype() {
		return vbusinesstype;
	}
	public void setVbusinesstype(String vbusinesstype) {
		this.vbusinesstype = vbusinesstype;
	}
	public Double getNapplymny() {
		return napplymny;
	}
	public void setNapplymny(Double napplymny) {
		this.napplymny = napplymny;
	}
	public Double getNpaymny() {
		return npaymny;
	}
	public void setNpaymny(Double npaymny) {
		this.npaymny = npaymny;
	}
	public String getDapplydate() {
		return dapplydate;
	}
	public void setDapplydate(String dapplydate) {
		this.dapplydate = dapplydate;
	}
	public String getDimportdate() {
		return dimportdate;
	}
	public void setDimportdate(String dimportdate) {
		this.dimportdate = dimportdate;
	}
	public String getDpaydate() {
		return dpaydate;
	}
	public void setDpaydate(String dpaydate) {
		this.dpaydate = dpaydate;
	}
	public Integer getSourceid() {
		return sourceid;
	}
	public void setSourceid(Integer sourceid) {
		this.sourceid = sourceid;
	}
	public String getVsourcetype() {
		return vsourcetype;
	}
	public void setVsourcetype(String vsourcetype) {
		this.vsourcetype = vsourcetype;
	}
	public Integer getOperatorid() {
		return operatorid;
	}
	public void setOperatorid(Integer operatorid) {
		this.operatorid = operatorid;
	}
	public String getVoperator() {
		return voperator;
	}
	public void setVoperator(String voperator) {
		this.voperator = voperator;
	}
	public Integer getVapproveid() {
		return vapproveid;
	}
	public void setVapproveid(Integer vapproveid) {
		this.vapproveid = vapproveid;
	}
	public String getVapprovestatus() {
		return vapprovestatus;
	}
	public void setVapprovestatus(String vapprovestatus) {
		this.vapprovestatus = vapprovestatus;
	}
	public String getDapprovedate() {
		return dapprovedate;
	}
	public void setDapprovedate(String dapprovedate) {
		this.dapprovedate = dapprovedate;
	}
	public String getVapprovenote() {
		return vapprovenote;
	}
	public void setVapprovenote(String vapprovenote) {
		this.vapprovenote = vapprovenote;
	}
	public String getVapprovecode() {
		return vapprovecode;
	}
	public void setVapprovecode(String vapprovecode) {
		this.vapprovecode = vapprovecode;
	}
	public Integer getVendstatus() {
		return vendstatus;
	}
	public void setVendstatus(Integer vendstatus) {
		this.vendstatus = vendstatus;
	}
	public Integer getBiscanedit() {
		return biscanedit;
	}
	public void setBiscanedit(Integer biscanedit) {
		this.biscanedit = biscanedit;
	}
	public String getPayformid() {
		return payformid;
	}
	public void setPayformid(String payformid) {
		this.payformid = payformid;
	}
	public String getReverse1() {
		return reverse1;
	}
	public void setReverse1(String reverse1) {
		this.reverse1 = reverse1;
	}
	public String getReverse2() {
		return reverse2;
	}
	public void setReverse2(String reverse2) {
		this.reverse2 = reverse2;
	}
	public String getReverse3() {
		return reverse3;
	}
	public void setReverse3(String reverse3) {
		this.reverse3 = reverse3;
	}
	public String getReverse4() {
		return reverse4;
	}
	public void setReverse4(String reverse4) {
		this.reverse4 = reverse4;
	}
	public String getReverse5() {
		return reverse5;
	}
	public void setReverse5(String reverse5) {
		this.reverse5 = reverse5;
	}
	public Double getReverse6() {
		return reverse6;
	}
	public void setReverse6(Double reverse6) {
		this.reverse6 = reverse6;
	}
	public Double getReverse7() {
		return reverse7;
	}
	public void setReverse7(Double reverse7) {
		this.reverse7 = reverse7;
	}
	public Double getReverse8() {
		return reverse8;
	}
	public void setReverse8(Double reverse8) {
		this.reverse8 = reverse8;
	}
	public Double getReverse9() {
		return reverse9;
	}
	public void setReverse9(Double reverse9) {
		this.reverse9 = reverse9;
	}
	public Double getReverse10() {
		return reverse10;
	}
	public void setReverse10(Double reverse10) {
		this.reverse10 = reverse10;
	}
	public String getReverse11() {
		return reverse11;
	}
	public void setReverse11(String reverse11) {
		this.reverse11 = reverse11;
	}
	public String getReverse12() {
		return reverse12;
	}
	public void setReverse12(String reverse12) {
		this.reverse12 = reverse12;
	}
	public String getReverse13() {
		return reverse13;
	}
	public void setReverse13(String reverse13) {
		this.reverse13 = reverse13;
	}
	public String getPayorgcode() {
		return payorgcode;
	}
	public void setPayorgcode(String payorgcode) {
		this.payorgcode = payorgcode;
	}
	public String getPayorgname() {
		return payorgname;
	}
	public void setPayorgname(String payorgname) {
		this.payorgname = payorgname;
	}
	public String getPayaccountcode() {
		return payaccountcode;
	}
	public void setPayaccountcode(String payaccountcode) {
		this.payaccountcode = payaccountcode;
	}
	public String getPayaccountname() {
		return payaccountname;
	}
	public void setPayaccountname(String payaccountname) {
		this.payaccountname = payaccountname;
	}
	public String getPaybankname() {
		return paybankname;
	}
	public void setPaybankname(String paybankname) {
		this.paybankname = paybankname;
	}
    
	
}