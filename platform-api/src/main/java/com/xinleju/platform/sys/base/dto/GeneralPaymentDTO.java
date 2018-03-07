/**   
*
* @version V1.0   
*/
package com.xinleju.platform.sys.base.dto;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * @author JonsonLee
 *	通用付款
 */
public class GeneralPaymentDTO extends BaseDto {
	
	/** 公司主键*/
	private String corpid;
	/** 公司名称*/
	private String corpname;
    /**部门主键*/
	private String deptid;
	/**部门名称*/
	private String deptname;
	/**审批状态*/
	private String vstatus;
	/**来源单据主键*/
	private Integer billid;
	/**来源单据名称*/
	private String billtype;
	/**主题*/
	private String vtheme;
	/**申请编号*/
	private String vapplynum;
	/**申请人*/
	private String vapplicant;
	/**申请部门*/
	private String vapplydepart;
	/**申请部门主键*/
	private String vapplydepartid;
	/**申请日期*/
	private String dappludate;
	/**付款类型id*/
	private Integer vapptlytypeid;
	/**付款类型*/
	private String vapplytype;
	/**申请付款金额*/
	private Double npaymentmny;
	 /**付款说明*/
	private String vpaymentmemo;
	/**收款单位*/
	private String vskunit;
	private String skunitid;
	private String vskunitbs;
	/**收款开户银行*/
	private String vskbank;
	/**收款银行账号*/
	private String vskbanknum;
	/**付款单位*/
	private String vpayunit;
	private String payunitid;
	private String vpayunitcode;
	/**付款开户银行*/
	private String vpaybank;
	/**付款银行账号*/
	private String vpaybanknum;
	/**支付金额*/
	public Double nzfpaymny;
	/**支付日期*/
	public String dpaydate;
	/**支付状态*/
	private Integer vpaymentstatus;
	/**审批人*/
	private String approveid;
	/**审批日期*/
	private String dapprovedate;
	/**流程编码*/
	private String flowcode;
	/**当前审批状态*/
	private String vapprovestatus;
	/**当前审批人*/
	public String vapprovenames;
	/**结束状态*/
	public Integer vendstatus;
	//预留字段
	private String reserve1;
	//预留字段
	private String reserve2;
	//预留字段
	private String reserve3;
	//预留字段
	private String reserve4;
	//预留字段
	private String reserve5;
	/**附件用GUID*/
	private String guid;
	/**编辑状态*/
	private Integer biscanedit;
	//资金平台同步属性
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
    private String instanceId;//新平台流程实例
    
    private String vpaymethod;
    private String vdbtype;
    private String vfkdxtype;
    
    private String projectid;
    private String vprojectname;
    private String vcwdept;
    
    
    private Integer fkbdtid;//记录类型id
    
    //导入状态
  	private Integer importstatus;//0未导入,1导入
  	//导入日期
  	private String dimportdate;
  	//支付状态
  	private Integer paystatus;//0未支付,1部分支付,2支付完全,3资金平台那边撤回
  	//支付金额
  	private Double npaymny;
  	//资金平台同步方法,返回结果中资金平台的唯一标识
  	private String payformid;
	public String getPayformid() {
		return payformid;
	}
	public void setPayformid(String payformid) {
		this.payformid = payformid;
	}
	public Double getNpaymny() {
		return npaymny;
	}
	public void setNpaymny(Double npaymny) {
		this.npaymny = npaymny;
	}
	public Integer getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
	}
	public String getDimportdate() {
		return dimportdate;
	}
	public void setDimportdate(String dimportdate) {
		this.dimportdate = dimportdate;
	}
	public Integer getImportstatus() {
		return importstatus;
	}
	public void setImportstatus(Integer importstatus) {
		this.importstatus = importstatus;
	}
	/**
	 * 123
	 */
	public String getCorpid() {
		return corpid;
	}
	/**
	 * @param corpid the corpid to set
	 */
	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}
	/**
	 * 123
	 */
	public String getCorpname() {
		return corpname;
	}
	/**
	 * @param corpname the corpname to set
	 */
	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}
	/**
	 * 123
	 */
	public String getDeptid() {
		return deptid;
	}
	/**
	 * @param deptid the deptid to set
	 */
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	/**
	 * 123
	 */
	public String getDeptname() {
		return deptname;
	}
	/**
	 * @param deptname the deptname to set
	 */
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	/**
	 * 123
	 */
	public String getVstatus() {
		return vstatus;
	}
	/**
	 * @param vstatus the vstatus to set
	 */
	public void setVstatus(String vstatus) {
		this.vstatus = vstatus;
	}
	/**
	 * 123
	 */
	public Integer getBillid() {
		return billid;
	}
	/**
	 * @param billid the billid to set
	 */
	public void setBillid(Integer billid) {
		this.billid = billid;
	}
	/**
	 * 123
	 */
	public String getBilltype() {
		return billtype;
	}
	/**
	 * @param billtype the billtype to set
	 */
	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}
	/**
	 * 123
	 */
	public String getVtheme() {
		return vtheme;
	}
	/**
	 * @param vtheme the vtheme to set
	 */
	public void setVtheme(String vtheme) {
		this.vtheme = vtheme;
	}
	/**
	 * 123
	 */
	public String getVapplynum() {
		return vapplynum;
	}
	/**
	 * @param vapplynum the vapplynum to set
	 */
	public void setVapplynum(String vapplynum) {
		this.vapplynum = vapplynum;
	}
	/**
	 * 123
	 */
	public String getVapplicant() {
		return vapplicant;
	}
	/**
	 * @param vapplicant the vapplicant to set
	 */
	public void setVapplicant(String vapplicant) {
		this.vapplicant = vapplicant;
	}
	/**
	 * 123
	 */
	public String getVapplydepart() {
		return vapplydepart;
	}
	/**
	 * @param vapplydepart the vapplydepart to set
	 */
	public void setVapplydepart(String vapplydepart) {
		this.vapplydepart = vapplydepart;
	}
	/**
	 * 123
	 */
	public String getVapplydepartid() {
		return vapplydepartid;
	}
	/**
	 * @param vapplydepartid the vapplydepartid to set
	 */
	public void setVapplydepartid(String vapplydepartid) {
		this.vapplydepartid = vapplydepartid;
	}
	/**
	 * 123
	 */
	public String getDappludate() {
		return dappludate;
	}
	/**
	 * @param dappludate the dappludate to set
	 */
	public void setDappludate(String dappludate) {
		this.dappludate = dappludate;
	}
	/**
	 * 123
	 */
	public String getVapplytype() {
		return vapplytype;
	}
	/**
	 * @param vapplytype the vapplytype to set
	 */
	public void setVapplytype(String vapplytype) {
		this.vapplytype = vapplytype;
	}
	/**
	 * 123
	 */
	public Double getNpaymentmny() {
		return npaymentmny;
	}
	/**
	 * @param npaymentmny the npaymentmny to set
	 */
	public void setNpaymentmny(Double npaymentmny) {
		this.npaymentmny = npaymentmny;
	}
	/**
	 * 123
	 */
	public String getVpaymentmemo() {
		return vpaymentmemo;
	}
	/**
	 * @param vpaymentmemo the vpaymentmemo to set
	 */
	public void setVpaymentmemo(String vpaymentmemo) {
		this.vpaymentmemo = vpaymentmemo;
	}
	/**
	 * 123
	 */
	public String getVskunit() {
		return vskunit;
	}
	/**
	 * @param vskunit the vskunit to set
	 */
	public void setVskunit(String vskunit) {
		this.vskunit = vskunit;
	}
	/**
	 * 123
	 */
	public String getSkunitid() {
		return skunitid;
	}
	/**
	 * @param skunitid the skunitid to set
	 */
	public void setSkunitid(String skunitid) {
		this.skunitid = skunitid;
	}
	/**
	 * 123
	 */
	public String getVskbank() {
		return vskbank;
	}
	/**
	 * @param vskbank the vskbank to set
	 */
	public void setVskbank(String vskbank) {
		this.vskbank = vskbank;
	}
	/**
	 * 123
	 */
	public String getVskbanknum() {
		return vskbanknum;
	}
	/**
	 * @param vskbanknum the vskbanknum to set
	 */
	public void setVskbanknum(String vskbanknum) {
		this.vskbanknum = vskbanknum;
	}
	
	/**
	 * 123
	 */
	public String getApproveid() {
		return approveid;
	}
	/**
	 * @param approveid the approveid to set
	 */
	public void setApproveid(String approveid) {
		this.approveid = approveid;
	}
	/**
	 * 123
	 */
	public String getDapprovedate() {
		return dapprovedate;
	}
	/**
	 * @param dapprovedate the dapprovedate to set
	 */
	public void setDapprovedate(String dapprovedate) {
		this.dapprovedate = dapprovedate;
	}
	/**
	 * 123
	 */
	public String getFlowcode() {
		return flowcode;
	}
	/**
	 * @param flowcode the flowcode to set
	 */
	public void setFlowcode(String flowcode) {
		this.flowcode = flowcode;
	}
	
	/**
	 * 123
	 */
	public String getReserve1() {
		return reserve1;
	}
	/**
	 * @param reserve1 the reserve1 to set
	 */
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	/**
	 * 123
	 */
	public String getReserve2() {
		return reserve2;
	}
	/**
	 * @param reserve2 the reserve2 to set
	 */
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	/**
	 * 123
	 */
	public String getReserve3() {
		return reserve3;
	}
	/**
	 * @param reserve3 the reserve3 to set
	 */
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	/**
	 * 123
	 */
	public String getReserve4() {
		return reserve4;
	}
	/**
	 * @param reserve4 the reserve4 to set
	 */
	public void setReserve4(String reserve4) {
		this.reserve4 = reserve4;
	}
	/**
	 * 123
	 */
	public String getReserve5() {
		return reserve5;
	}
	/**
	 * @param reserve5 the reserve5 to set
	 */
	public void setReserve5(String reserve5) {
		this.reserve5 = reserve5;
	}
	public String getVapprovestatus() {
		return vapprovestatus;
	}
	public void setVapprovestatus(String vapprovestatus) {
		this.vapprovestatus = vapprovestatus;
	}
	/**
	 * 123
	 */
	public String getVapprovenames() {
		return vapprovenames;
	}
	/**
	 * @param vapprovenames the vapprovenames to set
	 */
	public void setVapprovenames(String vapprovenames) {
		this.vapprovenames = vapprovenames;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public Integer getBiscanedit() {
		return biscanedit;
	}
	public void setBiscanedit(Integer biscanedit) {
		this.biscanedit = biscanedit;
	}
	public String getVskunitbs() {
		return vskunitbs;
	}
	public void setVskunitbs(String vskunitbs) {
		this.vskunitbs = vskunitbs;
	}
	/**
	 * 123
	 */
	public Double getNzfpaymny() {
		return nzfpaymny;
	}
	/**
	 * @param nzfpaymny the nzfpaymny to set
	 */
	public void setNzfpaymny(Double nzfpaymny) {
		this.nzfpaymny = nzfpaymny;
	}
	/**
	 * 123
	 */
	public String getDpaydate() {
		return dpaydate;
	}
	/**
	 * @param dpaydate the dpaydate to set
	 */
	public void setDpaydate(String dpaydate) {
		this.dpaydate = dpaydate;
	}
	public Integer getVpaymentstatus() {
		return vpaymentstatus;
	}
	public void setVpaymentstatus(Integer vpaymentstatus) {
		this.vpaymentstatus = vpaymentstatus;
	}
	/**
	 * 123
	 */
	public String getPayorgcode() {
		return payorgcode;
	}
	/**
	 * @param payorgcode the payorgcode to set
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
	 * @param payorgname the payorgname to set
	 */
	public void setPayorgname(String payorgname) {
		this.payorgname = payorgname;
	}
	/**
	 * 123
	 */
	public String getPayaccountcode() {
		return payaccountcode;
	}
	/**
	 * @param payaccountcode the payaccountcode to set
	 */
	public void setPayaccountcode(String payaccountcode) {
		this.payaccountcode = payaccountcode;
	}
	/**
	 * 123
	 */
	public String getPayaccountname() {
		return payaccountname;
	}
	/**
	 * @param payaccountname the payaccountname to set
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
	 * @param paybankname the paybankname to set
	 */
	public void setPaybankname(String paybankname) {
		this.paybankname = paybankname;
	}
	/**
	 * 123
	 */
	public Integer getVendstatus() {
		return vendstatus;
	}
	/**
	 * @param vendstatus the vendstatus to set
	 */
	public void setVendstatus(Integer vendstatus) {
		this.vendstatus = vendstatus;
	}
	public Integer getVapptlytypeid() {
		return vapptlytypeid;
	}
	public void setVapptlytypeid(Integer vapptlytypeid) {
		this.vapptlytypeid = vapptlytypeid;
	}
	public String getVpayunit() {
		return vpayunit;
	}
	public void setVpayunit(String vpayunit) {
		this.vpayunit = vpayunit;
	}
	public String getPayunitid() {
		return payunitid;
	}
	public void setPayunitid(String payunitid) {
		this.payunitid = payunitid;
	}
	public String getVpayunitcode() {
		return vpayunitcode;
	}
	public void setVpayunitcode(String vpayunitcode) {
		this.vpayunitcode = vpayunitcode;
	}
	public String getVpaybank() {
		return vpaybank;
	}
	public void setVpaybank(String vpaybank) {
		this.vpaybank = vpaybank;
	}
	public String getVpaybanknum() {
		return vpaybanknum;
	}
	public void setVpaybanknum(String vpaybanknum) {
		this.vpaybanknum = vpaybanknum;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public Integer getFkbdtid() {
		return fkbdtid;
	}
	public void setFkbdtid(Integer fkbdtid) {
		this.fkbdtid = fkbdtid;
	}
	public String getVpaymethod() {
		return vpaymethod;
	}
	public void setVpaymethod(String vpaymethod) {
		this.vpaymethod = vpaymethod;
	}
	public String getVdbtype() {
		return vdbtype;
	}
	public void setVdbtype(String vdbtype) {
		this.vdbtype = vdbtype;
	}
	public String getVfkdxtype() {
		return vfkdxtype;
	}
	public void setVfkdxtype(String vfkdxtype) {
		this.vfkdxtype = vfkdxtype;
	}
	/**
	 * 123
	 */
	public String getProjectid() {
		return projectid;
	}
	/**
	 * @param projectid the projectid to set
	 */
	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}
	/**
	 * 123
	 */
	public String getVprojectname() {
		return vprojectname;
	}
	/**
	 * @param vprojectname the vprojectname to set
	 */
	public void setVprojectname(String vprojectname) {
		this.vprojectname = vprojectname;
	}
	/**
	 * 123
	 */
	public String getVcwdept() {
		return vcwdept;
	}
	/**
	 * @param vcwdept the vcwdept to set
	 */
	public void setVcwdept(String vcwdept) {
		this.vcwdept = vcwdept;
	}
	
	
}
