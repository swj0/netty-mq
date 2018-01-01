package com.wen.jun.mq.broker;

import com.wen.jun.mq.core.AbstractParallelProcessor;
import com.wen.jun.mq.core.ParallelProcessor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;


@Sharable
public class BrokerMessageHandler extends SimpleChannelInboundHandler{
	
	private ParallelProcessor processor;
	
	public BrokerMessageHandler(ParallelProcessor p){
		this.processor = p;
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("broker接到新的连接..."+System.currentTimeMillis());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		processor.offer(ctx,msg);
	}
}
