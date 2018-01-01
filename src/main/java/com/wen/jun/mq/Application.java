package com.wen.jun.mq;

import java.util.concurrent.TimeUnit;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wen.jun.mq.broker.MessageBroker;

public class Application {

	public static void main(String[] args) {
		MessageBroker broker = new MessageBroker();
		
		System.out.println("main start..");
	
		//broker.stop();
	}

}
