package com.wen.jun.mq.common.constant;

public enum MessageType {
	
	Message2Topic(1),//发送给主题的消息，给生产者使用
	MessageFromTopic(2),//接收到的主题消息
	Message2Queue(3),//发送给队列的消息
	MessageFromQueue(4),//接收到的队列消息
	
	BrokerNormalReply(5),//broker的回复，告诉发送端，消息已经收到，至于处理结果并不知道
	
	Subscribe(6),//消费者订阅请求
	Unsubscribe(7),//消费者取消订阅请求
	SubscribeAck(8),//broker回复消费者的订阅请求
	UnSubscribeAck(9),//broker回复消费者的取消订阅的请求
	
	
	Listen2Queue(10),//消费者监听队列请求
	Listen2QueueAck(11),
	
	Reply2Topic(12),
	Reply2Queue(13),
	
	
	Heartbeat(14)
	
	
	;
	
	
	
	
	
	private int type;
	
	
	MessageType(int type){
		this.type = type;
	}

}
