package com.wen.jun.mq.demo1;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.constant.StatusCode;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.common.domain.ResponseMessage;
import com.wen.jun.mq.producer.MessageProducer;

public class MessageProduceTest {

	public static void main(String[] args) {
		
		
		
		MessageProducer producer = new MessageProducer("demo1-producer-client1");
		
		
		int counter = 1;
		while (counter <= 100){

			RequestMessage msg = new RequestMessage(MessageSource.MessageProducer,MessageType.Message2Queue);
			msg.setDestination(MessageRequestConfig.defaultQueue);
			
			msg.setMsgContent("any");
			msg.setMsgId(String.valueOf(counter));
			
			ResponseMessage resp = producer.send(msg);
			System.out.println("时间:"+new Date()+",counter="+counter+",resp="+resp);
			try {
				counter++;
				TimeUnit.SECONDS.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
