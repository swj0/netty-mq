package com.wen.jun.mq.test2;

import com.wen.jun.mq.TestBase;
import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.domain.Message;
import com.wen.jun.mq.consumer.MessageConsumer;
import com.wen.jun.mq.core.MessageListener;

public class TestConsumer extends TestBase{

	public static void main(String[] args) {
		MessageConsumer receiver = new MessageConsumer("consumer2", MessageRequestConfig.defaultGroupName, MessageRequestConfig.defaultTopic);
		
		System.out.println("consumer main over..");
		
		MessageListener listener = new MessageListener() {
			@Override
			public void onMessage(Message msg) {
				System.out.println("他嘛的收到了消息："+msg);
			}
		};
		
		receiver.addListener(listener);
		
		listener = new MessageListener() {
			@Override
			public void onMessage(Message msg) {
				System.out.println("他嘛的收到了消息11111："+msg);
			}
		};
		
		receiver.addListener(listener);
		delayMILLISECONDS(44);
		//receiver.stop();
	}

}
