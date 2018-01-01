package com.wen.jun.mq.test3;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.domain.Message;
import com.wen.jun.mq.consumer.MessageConsumer;
import com.wen.jun.mq.core.MessageListener;

public class TestConsumer {

	public static void main(String[] args) {
		MessageConsumer receiver = new MessageConsumer("consumer3", MessageRequestConfig.defaultQueue);
		
		MessageListener listener = new MessageListener() {
			int counter = 0 ;
			@Override
			public void onMessage(Message msg) {
				System.out.println("收到队列消息："+(++counter));
			}
		};
		receiver.addListener(listener);
	}

}
