package com.xinleju.platform.univ.mq.comsumers;

import com.xinleju.platform.univ.mq.service.MessageConsumer;

public class DemoRabbitMsgConsumer implements MessageConsumer {

	@Override
	public Boolean doConsumer(String message) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean doValidateConsumed(String message) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
