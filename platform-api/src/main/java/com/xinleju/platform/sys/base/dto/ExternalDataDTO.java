/**   
*
* @version V1.0   
*/
package com.xinleju.platform.sys.base.dto;

/**
 * 资金平台数据实体，没有实际表
 * @author wangjf
 */
public class ExternalDataDTO {
	
	/**
	 * 业务单据编号  ,Y
	 */
	private String billcode;//业务单据编号  ,Y
	/**
	 * 数据来源 ,Y
	 */
	private String datasource = "XLJEX";//数据来源   MY--->明源费用系统   EX--->费用 ,Y
	/**
	 * 业务单据日期  ,Y
	 */
	private String transferdate;//业务单据日期  ,Y
	/**
	 * 业务申请单位(编号)  ,Y
	 */
	private String orgcode;//业务申请单位(编号)  ,Y
	/**
	 * 业务类型编号   ,Y
	 */
	private String transfercode = "001";//业务类型编号   默认001 ,Y
	/**
	 * 付款用途编号   默认001 ,Y
	 */
	private String paytype = "001";//付款用途编号   默认001 ,Y
	/**
	 * 业务种类 7种  ,Y
	 */
	private String businesstype;//业务种类 7种  ,Y
	/**
	 * 业务类型 2种 外部/内部  ,Y
	 */
	private String transtype ;//业务类型 2种 外部/内部  ,Y
	/**
	 * 付款对象类型（银行指令类型）,N
	 */
	private String bankpaytype;//付款对象类型（银行指令类型）,N
	/**
	 * 计划项目编号  ,N
	 */
	private String budgetprojectcode;//计划项目编号  ,N
	/**
	 * 付款单位编号  ,Y
	 */
	private String payorgcode;//付款单位编号  ,Y
	/**
	 * 付款单位名称  ,N
	 */
	private String payorgname;//付款单位名称  ,N
	/**
	 * 付款账户编号  ,N
	 */
	private String payaccountno;//付款账户编号  ,N
	/**
	 * 付款账户名称  ,N
	 */
	private String payaccountname;//付款账户名称  ,N
	/**
	 * 付款方开户行名称  ,N
	 */
	private String paybankname;//付款方开户行名称  ,N
	/**
	 * 收款方单位编号  ,N
	 */
	private String recorgcode;//收款方单位编号  ,N
	/**
	 * 收款方单位名称 ,Y
	 */
	private String recorgname;//收款方单位名称 ,Y
	/**
	 * 收款账户编号  ,Y
	 */
	private String recaccountno;//收款账户编号  ,Y
	/**
	 * 收款账户名称  ,Y
	 */
	private String recaccountname;//收款账户名称  ,Y
	/**
	 * 收款行名称  ,N
	 */
	private String recbankname;//收款行名称  ,N
	/**
	 * 收款行地址（省） ,N
	 */
	private String recprovince;//收款行地址（省） ,N
	/**
	 * 收款行地址（市） ,N
	 */
	private String recareanameofcity;//收款行地址（市） ,N
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
	 * 员工编号  ,N
	 */
	private String personnelcode;//员工编号  ,N
	/**
	 * 员工姓名  ,N
	 */
	private String personnelname;//员工姓名  ,N
	/**
	 * 结算币种  ,Y
	 */
	private String currencycode;//结算币种  ,Y
	/**
	 * 付款金额  ,Y
	 */
	private Double amount;//付款金额  ,Y
	/**
	 * 摘要  ,N
	 */
	private String abstracts;//摘要  ,N
	/**
	 * 是否允许拆单  ,Y
	 */
	private String issplit;//是否允许拆单  ,Y
	/**
	 * 支付方式  ,Y
	 */
	private String paymenttype;//支付方式  ,Y
	/**
	 * 经办人员编码  ,Y
	 */
	private String operator;//经办人员编码  ,Y
	/**
	 * 是否批量
	 */
	private String isbatch;//是否批量
	private String free1;//备用字段1
	private String free2;//备用字段2
	private String free3;//备用字段3
	private String free4;//备用字段4
	private String free5;//备用字段5
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExternalDataDTO [billcode=" + billcode + ", datasource="
				+ datasource + ", transferdate=" + transferdate + ", orgcode="
				+ orgcode + ", transfercode=" + transfercode + ", paytype="
				+ paytype + ", businesstype=" + businesstype + ", transtype="
				+ transtype + ", bankpaytype=" + bankpaytype
				+ ", budgetprojectcode=" + budgetprojectcode + ", payorgcode="
				+ payorgcode + ", payorgname=" + payorgname + ", payaccountno="
				+ payaccountno + ", payaccountname=" + payaccountname
				+ ", paybankname=" + paybankname + ", recorgcode=" + recorgcode
				+ ", recorgname=" + recorgname + ", recaccountno="
				+ recaccountno + ", recaccountname=" + recaccountname
				+ ", recbankname=" + recbankname + ", recprovince="
				+ recprovince + ", recareanameofcity=" + recareanameofcity
				+ ", bankcodeofrec=" + bankcodeofrec + ", bankexccodeofrec="
				+ bankexccodeofrec + ", cnapsofrec=" + cnapsofrec
				+ ", personnelcode=" + personnelcode + ", personnelname="
				+ personnelname + ", currencycode=" + currencycode
				+ ", amount=" + amount + ", abstracts=" + abstracts
				+ ", issplit=" + issplit + ", paymenttype=" + paymenttype
				+ ", operator=" + operator + ", isbatch=" + isbatch + "]";
	}
	/**
	 * 123
	 */
	public String getBillcode() {
		return billcode;
	}
	/**
	 * @param 业务单据编号Y the billcode to set
	 */
	public void setBillcode(String billcode) {
		this.billcode = billcode;
	}
	/**
	 * 123
	 */
	public String getDatasource() {
		return datasource;
	}
	/**
	 * @param 数据来源Y the datasource to set
	 */
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	/**
	 * 123
	 */
	public String getTransferdate() {
		return transferdate;
	}
	/**
	 * @param 业务单据日期Y the transferdate to set
	 */
	public void setTransferdate(String transferdate) {
		this.transferdate = transferdate;
	}
	/**
	 * 123
	 */
	public String getOrgcode() {
		return orgcode;
	}
	/**
	 * @param 业务申请单位(编号)Y the orgcode to set
	 */
	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}
	/**
	 * 123
	 */
	public String getTransfercode() {
		return transfercode;
	}
	/**
	 * @param 业务类型编号Y the transfercode to set
	 */
	public void setTransfercode(String transfercode) {
		this.transfercode = transfercode;
	}
	/**
	 * 123
	 */
	public String getPaytype() {
		return paytype;
	}
	/**
	 * @param 付款用途编号默认001Y the paytype to set
	 */
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	/**
	 * 123
	 */
	public String getBusinesstype() {
		return businesstype;
	}
	/**
	 * @param 业务种类7种Y the businesstype to set
	 */
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
	/**
	 * 123
	 */
	public String getTranstype() {
		return transtype;
	}
	/**
	 * @param 业务类型2种外部内部Y the transtype to set
	 */
	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}
	/**
	 * 123
	 */
	public String getBankpaytype() {
		return bankpaytype;
	}
	/**
	 * @param 付款对象类型（银行指令类型）N the bankpaytype to set
	 */
	public void setBankpaytype(String bankpaytype) {
		this.bankpaytype = bankpaytype;
	}
	/**
	 * 123
	 */
	public String getBudgetprojectcode() {
		return budgetprojectcode;
	}
	/**
	 * @param 计划项目编号N the budgetprojectcode to set
	 */
	public void setBudgetprojectcode(String budgetprojectcode) {
		this.budgetprojectcode = budgetprojectcode;
	}
	/**
	 * 123
	 */
	public String getPayorgcode() {
		return payorgcode;
	}
	/**
	 * @param 付款单位编号Y the payorgcode to set
	 */
	public void setPayorgcode(String payorgcode) {
		this.payorgcode = payorgcode;
	}
	/**
	 * 123
	 */
	public String getPayorgname() {
		return payorgname;
	}
	/**
	 * @param 付款单位名称N the payorgname to set
	 */
	public void setPayorgname(String payorgname) {
		this.payorgname = payorgname;
	}
	/**
	 * 123
	 */
	public String getPayaccountno() {
		return payaccountno;
	}
	/**
	 * @param 付款账户编号N the payaccountno to set
	 */
	public void setPayaccountno(String payaccountno) {
		this.payaccountno = payaccountno;
	}
	/**
	 * 123
	 */
	public String getPayaccountname() {
		return payaccountname;
	}
	/**
	 * @param 付款账户名称N the payaccountname to set
	 */
	public void setPayaccountname(String payaccountname) {
		this.payaccountname = payaccountname;
	}
	/**
	 * 123
	 */
	public String getPaybankname() {
		return paybankname;
	}
	/**
	 * @param 付款方开户行名称N the paybankname to set
	 */
	public void setPaybankname(String paybankname) {
		this.paybankname = paybankname;
	}
	/**
	 * 123
	 */
	public String getRecorgcode() {
		return recorgcode;
	}
	/**
	 * @param 收款方单位编号N the recorgcode to set
	 */
	public void setRecorgcode(String recorgcode) {
		this.recorgcode = recorgcode;
	}
	/**
	 * 123
	 */
	public String getRecorgname() {
		return recorgname;
	}
	/**
	 * @param 收款方单位名称Y the recorgname to set
	 */
	public void setRecorgname(String recorgname) {
		this.recorgname = recorgname;
	}
	/**
	 * 123
	 */
	public String getRecaccountno() {
		return recaccountno;
	}
	/**
	 * @param 收款账户编号Y the recaccountno to set
	 */
	public void setRecaccountno(String recaccountno) {
		this.recaccountno = recaccountno;
	}
	/**
	 * 123
	 */
	public String getRecaccountname() {
		return recaccountname;
	}
	/**
	 * @param 收款账户名称Y the recaccountname to set
	 */
	public void setRecaccountname(String recaccountname) {
		this.recaccountname = recaccountname;
	}
	/**
	 * 123
	 */
	public String getRecbankname() {
		return recbankname;
	}
	/**
	 * @param 收款行名称N the recbankname to set
	 */
	public void setRecbankname(String recbankname) {
		this.recbankname = recbankname;
	}
	/**
	 * 123
	 */
	public String getRecprovince() {
		return recprovince;
	}
	/**
	 * @param 收款行地址（省）N the recprovince to set
	 */
	public void setRecprovince(String recprovince) {
		this.recprovince = recprovince;
	}
	/**
	 * 123
	 */
	public String getRecareanameofcity() {
		return recareanameofcity;
	}
	/**
	 * @param 收款行地址（市）N the recareanameofcity to set
	 */
	public void setRecareanameofcity(String recareanameofcity) {
		this.recareanameofcity = recareanameofcity;
	}
	/**
	 * 123
	 */
	public String getBankcodeofrec() {
		return bankcodeofrec;
	}
	/**
	 * @param 收款行机构号N the bankcodeofrec to set
	 */
	public void setBankcodeofrec(String bankcodeofrec) {
		this.bankcodeofrec = bankcodeofrec;
	}
	/**
	 * 123
	 */
	public String getBankexccodeofrec() {
		return bankexccodeofrec;
	}
	/**
	 * @param 收款行联行号N the bankexccodeofrec to set
	 */
	public void setBankexccodeofrec(String bankexccodeofrec) {
		this.bankexccodeofrec = bankexccodeofrec;
	}
	/**
	 * 123
	 */
	public String getCnapsofrec() {
		return cnapsofrec;
	}
	/**
	 * @param 收款行CNAPS号N the cnapsofrec to set
	 */
	public void setCnapsofrec(String cnapsofrec) {
		this.cnapsofrec = cnapsofrec;
	}
	/**
	 * 123
	 */
	public String getPersonnelcode() {
		return personnelcode;
	}
	/**
	 * @param 员工编号N the personnelcode to set
	 */
	public void setPersonnelcode(String personnelcode) {
		this.personnelcode = personnelcode;
	}
	/**
	 * 123
	 */
	public String getPersonnelname() {
		return personnelname;
	}
	/**
	 * @param 员工姓名N the personnelname to set
	 */
	public void setPersonnelname(String personnelname) {
		this.personnelname = personnelname;
	}
	/**
	 * 123
	 */
	public String getCurrencycode() {
		return currencycode;
	}
	/**
	 * @param 结算币种Y the currencycode to set
	 */
	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}
	/**
	 * 123
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param 付款金额Y the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * 123
	 */
	public String getAbstracts() {
		return abstracts;
	}
	/**
	 * @param 摘要N the abstracts to set
	 */
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}
	/**
	 * 123
	 */
	public String getIssplit() {
		return issplit;
	}
	/**
	 * @param 是否允许拆单Y the issplit to set
	 */
	public void setIssplit(String issplit) {
		this.issplit = issplit;
	}
	/**
	 * 123
	 */
	public String getPaymenttype() {
		return paymenttype;
	}
	/**
	 * @param 支付方式Y the paymenttype to set
	 */
	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}
	/**
	 * 123
	 */
	public String getOperator() {
		return operator;
	}
	/**
	 * @param 经办人员编码Y the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**
	 * 123
	 */
	public String getIsbatch() {
		return isbatch;
	}
	/**
	 * @param 是否批量 the isbatch to set
	 */
	public void setIsbatch(String isbatch) {
		this.isbatch = isbatch;
	}
	/**
	 * 123
	 */
	public String getFree1() {
		return free1;
	}
	/**
	 * @param free1 the free1 to set
	 */
	public void setFree1(String free1) {
		this.free1 = free1;
	}
	/**
	 * 123
	 */
	public String getFree2() {
		return free2;
	}
	/**
	 * @param free2 the free2 to set
	 */
	public void setFree2(String free2) {
		this.free2 = free2;
	}
	/**
	 * 123
	 */
	public String getFree3() {
		return free3;
	}
	/**
	 * @param free3 the free3 to set
	 */
	public void setFree3(String free3) {
		this.free3 = free3;
	}
	/**
	 * 123
	 */
	public String getFree4() {
		return free4;
	}
	/**
	 * @param free4 the free4 to set
	 */
	public void setFree4(String free4) {
		this.free4 = free4;
	}
	/**
	 * 123
	 */
	public String getFree5() {
		return free5;
	}
	/**
	 * @param free5 the free5 to set
	 */
	public void setFree5(String free5) {
		this.free5 = free5;
	}
	
	
}
