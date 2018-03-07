package com.xinleju.platform.flow.enumeration;

/**
 * 流程操作类型
 * 流程实例的相关操作时需要保存到
 * @author zhengjiajie
 *
 */
public enum InstanceOperateType {
	//以下10个操作是个人查询所需要的
	MY_START("我的发起", "MY_START"), 
	TO_DO("待办", "TO_DO"),
	TO_READ("待阅", "TO_READ"),
	HAVE_DONE("已办", "HAVE_DONE"),
	HAVE_READ("已阅读", "HAVE_READ"),
	
	AUTHEN_OTHERS("授权他人", "AUTHEN_OTHERS"), 
	AGENT_OTHERS("代理他人", "AGENT_OTHERS"),
	PASS_READ("传阅", "PASS_READ"),
	CANCEL("作废", "CANCEL"),
	SEND_BACK("打回", "SEND_BACK"),
	COLLECT("收藏", "COLLECT"),

	//以下几个是统计报表所需要的
    TRANSFER("转办", "TRANSFER"),
	COOPERATE("协办", "COOPERATE"),
	DRAW_BACK_INSTANCE("发起人撤回流程", "DRAW_BACK_INSTANCE"),
	DRAW_BACK_TASK("审批人撤回任务", "DRAW_BACK_TASK"),
	MODIFY_ADVICE("修改审批人意见", "MODIFY_ADVICE");

	private String name;
	private String value;

	private InstanceOperateType(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
