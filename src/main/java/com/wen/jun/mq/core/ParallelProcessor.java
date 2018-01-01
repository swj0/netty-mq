package com.wen.jun.mq.core;

import io.netty.channel.ChannelHandlerContext;

public interface ParallelProcessor extends MQLifeCycle{
	
	/**
	 * 接收消息
	 * @param ctx
	 * @param msg
	 * @return
	 */
	boolean offer(ChannelHandlerContext ctx, Object msg);
}
