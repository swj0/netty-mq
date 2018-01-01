package com.wen.jun.mq.test3;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.producer.MessageProducer;


/**
 * 
 * @author swj
 * 
 * 测试单个生产者的吞吐量
 *
 */
public class TestProducer {

	public static void main(String[] args) {
		MessageProducer sender = new MessageProducer("producer3");
		
		int counter=0;
		boolean f = false;
		for(int i=0;i<20000;i++){
			

			RequestMessage request = new RequestMessage(MessageSource.MessageProducer,MessageType.Message2Queue);
			request.setMsgContent("发送给对方的队列...");
		
			request.setDestination(MessageRequestConfig.defaultQueue);
			request.setMsgId(String.valueOf(i));
			f = sender.sendAsynMessage(request);
			if(f)counter++;
		}
		System.out.println("send counter="+counter);
		
	}

}
