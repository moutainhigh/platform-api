package com.xinleju.platform.finance.dto;

import java.util.List;

public class VoucherData {
	private VoucherBillDto voucherBillDto;
	private List<VoucherBillEntryDto> entryBillList;
	private List<VoucherBillRelationDto> voucherBillRelationList;
	
	public VoucherBillDto getVoucherBillDto() {
		return voucherBillDto;
	}
	public void setVoucherBillDto(VoucherBillDto voucherBillDto) {
		this.voucherBillDto = voucherBillDto;
	}
	public List<VoucherBillEntryDto> getEntryBillList() {
		return entryBillList;
	}
	public void setEntryBillList(List<VoucherBillEntryDto> entryBillList) {
		this.entryBillList = entryBillList;
	}
	public List<VoucherBillRelationDto> getVoucherBillRelationList() {
		return voucherBillRelationList;
	}
	public void setVoucherBillRelationList(List<VoucherBillRelationDto> voucherBillRelationList) {
		this.voucherBillRelationList = voucherBillRelationList;
	}
}
