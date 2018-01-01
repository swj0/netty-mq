package com.wen.jun.mq.common.domain;

import com.wen.jun.mq.common.constant.MessageSource;

import io.netty.channel.ChannelHandlerContext;

public class BrokerReceiveDto {

	private ChannelHandlerContext ctx;
	private RequestMessage msg;
	
	
	public BrokerReceiveDto(ChannelHandlerContext ctx,RequestMessage req) {
		this.ctx = ctx;
		this.msg = req;
	}
	
	
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	public RequestMessage getMsg() {
		return msg;
	}
}
