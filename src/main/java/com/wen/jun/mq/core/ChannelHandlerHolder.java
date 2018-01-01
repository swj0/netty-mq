package com.wen.jun.mq.core;

import io.netty.channel.ChannelHandler;

public interface ChannelHandlerHolder {

	ChannelHandler[] handlers();  
	
	
	
}
