package com.wen.jun.mq.test2;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.constant.NettyConfig;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.common.domain.ResponseMessage;
import com.wen.jun.mq.producer.MessageProducer;

public class TestProducer {

	public static void main(String[] args) {
		
		MessageProducer sender = new MessageProducer("producer2");
		
		RequestMessage msg = new RequestMessage(MessageSource.MessageProducer,MessageType.Message2Topic);
		msg.setDestination(MessageRequestConfig.defaultTopic);
		msg.setMsgContent("hi test2");
		msg.setMsgId(UUID.randomUUID().toString());
		ResponseMessage resp = sender.send(msg);
		
		
		
		
		
		System.out.println("11111111111111111resp="+resp);
		
		System.out.println("2222222222222222");
	}

}
