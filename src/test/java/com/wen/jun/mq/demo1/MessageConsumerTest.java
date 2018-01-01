package com.wen.jun.mq.demo1;

import java.util.Date;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.domain.Message;
import com.wen.jun.mq.consumer.MessageConsumer;
import com.wen.jun.mq.core.MessageListener;

public class MessageConsumerTest {

	public static void main(String[] args) {
		
		
		//消费者,设置监听的队列
		MessageConsumer consumer  = new MessageConsumer("demo1-consumer-client1", MessageRequestConfig.defaultQueue);
		
		MessageListener listener = new MessageListener() {
			int counter = 0 ;
			@Override
			public void onMessage(Message msg) {
				System.out.println("收到消息:msg="+msg);
				System.out.println("3.2收到队列消息："+(++counter)+",时间："+new Date());
			}
		};
		consumer.addListener(listener);

	}

}
