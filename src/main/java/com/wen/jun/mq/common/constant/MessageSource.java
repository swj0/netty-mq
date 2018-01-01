package com.wen.jun.mq.common.constant;

public enum MessageSource {
	
	MessageProducer(1),
	MessageBroker(2),
	MessageConsumer(3);
	
	private int source;
	
	MessageSource(int s){
		this.source = s;
	}

}
