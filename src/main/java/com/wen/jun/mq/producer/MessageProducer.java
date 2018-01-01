package com.wen.jun.mq.producer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.wen.jun.mq.common.constant.NettyConfig;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class MessageProducer extends ProducerProcessor{
	
	public MessageProducer(String clientId) {
		super(clientId);
		
		init();
		start();
	}
	
	
	private EventLoopGroup worker = null;
	
	private Bootstrap b = null;
	
	private SocketAddress remoteAddress = null;
	

	@Override
	public synchronized void init() {
		if(isInited()){
			System.out.println("生产者已经启动了,不要重复启动!!");
			return ;
		}
		worker = new NioEventLoopGroup(1,getThreadFactory("ProducerWorker[Selector]"));
		
		remoteAddress = new InetSocketAddress(NettyConfig.host,NettyConfig.port);
		
		b = new Bootstrap();
		b.group(worker);
		b.channel(NioSocketChannel.class);
		b.remoteAddress(remoteAddress);
		b.option(ChannelOption.SO_SNDBUF, 20480);
		b.option(ChannelOption.TCP_NODELAY, true);
		b.handler(new ProducerChannelInitializer(this));
		inited(true);
	}

	@Override
	public synchronized void start() {
		if(isRunning()){
			System.out.println("生产者已经启动了,不要重复启动!!");
			return ;
		}
		
		try {
			ChannelFuture channelFuture = b.connect().sync();
			
			channelFuture.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()){
						successReconnect(future.channel());
					}else{
						running(false);
					}
					
				}
			});
			//channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		if(isRunning()){
			try {
				channel().close().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			worker.shutdownGracefully();
			inited(false);
			running(false);
		}
	}

	@Override
	public Bootstrap getBootstrap() {
		return this.b;
	}
	
	
	@Override
	public SocketAddress getTargetSocketAddress() {
		return this.remoteAddress;
	}

	@Override
	public boolean successReconnect(Channel ch) {
		setChannel(ch);
		running(true);
		return true;
	}
}
