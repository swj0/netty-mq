package com.wen.jun.mq.broker;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.wen.jun.mq.common.constant.NettyConfig;
import com.wen.jun.mq.core.AbstractParallelProcessor;
import com.wen.jun.mq.core.MQLifeCycle;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class MessageBroker extends AbstractParallelProcessor{
	
	private ServerBootstrap b = null;
	
	
	private EventLoopGroup boss = null;
	private EventLoopGroup worker = null;
	
	private SocketAddress localAddress = null;
	
	private volatile boolean inited = false;
	private volatile boolean isRunning = false;
	
	
	public MessageBroker() {
		this.start();
	}
	
	/**
	 * 
	 * 启动broker
	 */
	@Override
	public synchronized void start() {
		init0();
		if(isRunning())return ;
		
		try {
			
			ChannelFuture channelFuture = b.bind().sync();
			
			super.start();
			
			System.out.println("broker启动成功....................");
			running();
			//channelFuture.channel().closeFuture().sync();//在另外的main函数调用时注释掉，否则阻塞主函数
			//System.out.println("broker关闭...............");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * broker初始化
	 * 同步方法
	 */
	private  void init0(){
		if(isInited())return ;
		boss = new NioEventLoopGroup(2,getThreadFactory("BrokerBoss[Selector]"));
		worker = new NioEventLoopGroup(8,getThreadFactory("BrokerBoss[Selector]"));
		b = new ServerBootstrap();
		
		BrokerChannelInitializer bc = new BrokerChannelInitializer(this);
		localAddress = new InetSocketAddress(NettyConfig.host, NettyConfig.port);
		
		b.group(boss, worker);
		b.channel(NioServerSocketChannel.class);
		b.handler(new LoggingHandler(LogLevel.INFO));
		b.localAddress(localAddress);
		b.childOption(ChannelOption.SO_RCVBUF, 20480);
		b.childOption(ChannelOption.SO_KEEPALIVE, true);
		b.option(ChannelOption.SO_BACKLOG, 10240);
		b.childHandler(bc);
		
		super.init();
		inited = true;
	}
	
	
	
	
	
	@Override
	public void stop() {
		if (isRunning()) {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
			this.isRunning = false;
			
			super.stop();
		}
	}

	@Override
	public boolean isInited() {
		return inited;
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}
	
	private void running(){
		this.isRunning = true;
	}

}

























