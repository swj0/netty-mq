package com.wen.jun.mq.producer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.core.AbstractProcessor;
import com.wen.jun.mq.core.Processor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProducerMessageHandler extends SimpleChannelInboundHandler{
	
	private AbstractProcessor processor;
	
	public ProducerMessageHandler(AbstractProcessor p) {
		this.processor = p;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		/*
		RequestMessage request = new RequestMessage(MessageSource.MessageProducer,MessageType.Message2Queue);
		request.setMsgId("1111111111111111100");
		
		System.out.println("request="+request);
		
		
		ctx.channel().eventLoop().scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				ctx.channel().writeAndFlush(request);
			}
		}, 1, 4, TimeUnit.SECONDS);
	
		*/
		
		
	}

	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("生产者收到链路关闭事件.............");
		super.channelInactive(ctx);
	}
	
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		processor.handleResult(msg);
		
	}

}
