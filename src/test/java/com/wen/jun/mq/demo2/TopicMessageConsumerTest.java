package com.wen.jun.mq.demo2;

import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.domain.Message;
import com.wen.jun.mq.consumer.MessageConsumer;
import com.wen.jun.mq.core.MessageListener;

public class TopicMessageConsumerTest {

	public static void main(String[] args) {
		MessageConsumer receiver1 = new MessageConsumer("demo2-consumer-client1", MessageRequestConfig.defaultGroupName, MessageRequestConfig.defaultTopic);
		MessageConsumer receiver2 = new MessageConsumer("demo2-consumer-client2", MessageRequestConfig.defaultGroupName, MessageRequestConfig.defaultTopic);
		
		MessageListener listener1 = new MessageListener() {
			@Override
			public void onMessage(Message msg) {
				System.out.println("receiver1收到了消息："+msg);
			}
		};
		
		MessageListener listener2 = new MessageListener() {
			@Override
			public void onMessage(Message msg) {
				System.out.println("receiver2收到了消息："+msg);
			}
		};
		
		
		
	
		receiver1.addListener(listener1);
		receiver2.addListener(listener2);
		
		
		int counter = 2;
		
		while (counter <= 10){
			counter++ ;
			try {
				TimeUnit.SECONDS.sleep(45);
				
				MessageConsumer  receiver = new MessageConsumer("demo2-consumer-client"+counter, MessageRequestConfig.defaultGroupName, MessageRequestConfig.defaultTopic);
				
				MessageListener listener = new MessageListener() {
					@Override
					public void onMessage(Message msg) {
						System.out.println("receiver"+receiver.clientId+"收到了消息："+msg);
					}
				};
				receiver.addListener(listener);
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
			
		}
		
		
		
	}

}
