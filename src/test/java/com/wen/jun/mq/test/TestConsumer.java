package com.wen.jun.mq.test;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.consumer.MessageConsumer;

public class TestConsumer {

	public static void main(String[] args) {
		MessageConsumer consumer = new MessageConsumer("111",MessageRequestConfig.defaultGroupName, MessageRequestConfig.defaultTopic);
		
		consumer.init();
		consumer.start();
		
		
		 
	}

}
