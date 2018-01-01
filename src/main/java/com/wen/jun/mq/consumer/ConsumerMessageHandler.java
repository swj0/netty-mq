package com.wen.jun.mq.consumer;

import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.core.Processor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ConsumerMessageHandler extends SimpleChannelInboundHandler{
	
	private Processor processor;
	
	int counter = 0 ;
	public ConsumerMessageHandler(Processor consumer) {
		this.processor = consumer;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("consumer active:"+System.currentTimeMillis());
		
	}
	
	/**
	 * 接收消息唯一入口
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		processor.receive(ctx.channel(), msg);
		
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		cause.printStackTrace();
		
		processor.unRegister();
		
		ctx.close();
	}

}
