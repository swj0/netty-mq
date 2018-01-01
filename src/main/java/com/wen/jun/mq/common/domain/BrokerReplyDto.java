package com.wen.jun.mq.common.domain;

import com.wen.jun.mq.common.constant.MessageType;

import io.netty.channel.ChannelHandlerContext;

public class BrokerReplyDto {

	private ChannelHandlerContext ctx;
	private RequestMessage msg;
	
	MessageType replyMsgType;
	
	private boolean isHandleSuccess ;
	
	
	
	public BrokerReplyDto(ChannelHandlerContext ctx,RequestMessage msg,MessageType mt) {
		this.ctx = ctx;
		this.msg = msg;
		this.replyMsgType = mt;
	}
	
	public BrokerReplyDto(ChannelHandlerContext ctx,RequestMessage obj,MessageType mt,boolean success) {
		this(ctx,obj,mt);
		this.isHandleSuccess = success;
	}
	
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	public RequestMessage getMsg() {
		return msg;
	}

	public boolean isHandleSuccess() {
		return isHandleSuccess;
	}
	public void setHandleSuccess(boolean isHandleSuccess) {
		this.isHandleSuccess = isHandleSuccess;
	}
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	public MessageType getMsgType() {
		return replyMsgType;
	}

	public void setMsgType(MessageType msgType) {
		this.replyMsgType = msgType;
	}
	
	
	
	
}
