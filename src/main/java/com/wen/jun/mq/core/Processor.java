package com.wen.jun.mq.core;

import java.net.SocketAddress;

import com.wen.jun.mq.common.constant.StatusCode;
import com.wen.jun.mq.common.domain.MessageBase;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

public interface Processor extends MQLifeCycle{
	
	StatusCode checkChannelStatus() throws InterruptedException;

	//int send(MessageBase msg,ChannelFutureListener listener);
	
	default boolean addListener(MessageListener listener){return false;}
	
	
	default boolean receive(Channel ch,Object msg){return false;}
	
	default  boolean register(){return false;}
	
	default boolean listen2queue(){return false;}
	
	default boolean unRegister(){return false;}
	
	Bootstrap getBootstrap();
	
	SocketAddress getTargetSocketAddress();
	
}
