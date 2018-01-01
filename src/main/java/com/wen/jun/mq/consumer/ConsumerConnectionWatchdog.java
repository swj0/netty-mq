package com.wen.jun.mq.consumer;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.core.AbstractProcessor;
import com.wen.jun.mq.core.ChannelHandlerHolder;
import com.wen.jun.mq.core.ConnectionWatchdog;
import com.wen.jun.mq.core.Processor;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public abstract class ConsumerConnectionWatchdog extends ConnectionWatchdog{

	
	public ConsumerConnectionWatchdog(AbstractProcessor p, Timer timer,boolean reconnect) {
		super(p, timer, reconnect);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("消费者链接Inactive...");
		//processor.stop();//这里不要stop，因为stop被调用，那么EventLoopGroup也会被停掉
		if(reconnect){
			
			System.out.println("消费者链接Inactive..   将进行重连");
			
			if(attempts < 10){
				attempts++;
			}
			int timeout = 2<<attempts;//timeout最大2048，大约2s
			timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS);
		}
		
		super.channelInactive(ctx);
	}
	
	@Override
	public void run(Timeout timeout) throws Exception {
		ChannelFuture future ;
		synchronized (bootstrap) {
			bootstrap.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(handlers());
				}
			});
			
			future = bootstrap.connect(remoteAddress);
		}
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future0) throws Exception {
				boolean succeed = future0.isSuccess();
				//如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试n次，如果失败则不再重连  
				if(!succeed){
					System.out.println("消费者重连失败...");
					future0.channel().pipeline().fireChannelInactive();
				}else{
					processor.successReconnect(future0.channel());
					System.out.println("消费者重连成功...");
				}
			}
		});
	}

}
