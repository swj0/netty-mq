package com.wen.jun.mq.test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.common.domain.ResponseMessage;
import com.wen.jun.mq.producer.MessageProducer;

public class TestProducer {

	public static void main(String[] args) {
		MessageProducer producer = new MessageProducer("producer1");
		
		
		
		RequestMessage request = new RequestMessage(null);
		request.setMsgId(UUID.randomUUID().toString());
		
		//request.setSource(MessageSource.MessageProducer);
		
		request.setMsgContent("这是发送来自生产者producer1");
		
		request.setDestination(MessageRequestConfig.defaultTopic);
		
		
		
		System.out.println("request="+request);
		ResponseMessage ack = producer.send(request);
		System.out.println("..ack="+ack);
		
		
		if(!ack.isSuccess()){
			try {
				System.out.println("等待4s再发");
				Thread.sleep(4000);
				ack = producer.send(request);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		try {
			for(int i=0;i<10;i++){
				long then = System.currentTimeMillis();
				request.setMsgId(String.valueOf(i));
				ResponseMessage ack0 = producer.send(request);
				System.out.println("i="+i+",ack0="+ack0);
				System.out.println("cost="+(System.currentTimeMillis() - then));
			}
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		producer.stop();
		//13026668677
		System.out.println("结束......");
	}

}
