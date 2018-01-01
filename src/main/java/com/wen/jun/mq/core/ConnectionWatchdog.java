package com.wen.jun.mq.core;

import java.net.SocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

public abstract class ConnectionWatchdog  extends ChannelInboundHandlerAdapter implements TimerTask,ChannelHandlerHolder{
	
	protected AbstractProcessor processor;
	protected final Bootstrap bootstrap;
	protected final Timer timer;
	protected final SocketAddress remoteAddress;
	protected volatile boolean reconnect = true;
	
	
	protected int attempts;
	


	
	public ConnectionWatchdog( AbstractProcessor p, Timer timer, boolean reconnect) {  
		this.processor = p;
        this.bootstrap = processor.getBootstrap();  
        this.timer = timer;  
        this.remoteAddress = this.processor.getTargetSocketAddress();  
        this.reconnect = reconnect;  
    }  
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("当前链路已经激活，重连尝试次数重新 设置 0");
		attempts = 0 ;
		super.channelActive(ctx);
	}
	
	
}
