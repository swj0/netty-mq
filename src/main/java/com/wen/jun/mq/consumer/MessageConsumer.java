package com.wen.jun.mq.consumer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.commons.lang3.StringUtils;

import com.wen.jun.mq.common.constant.NettyConfig;
import com.wen.jun.mq.common.domain.ResponseMessage;

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

public class MessageConsumer extends ConsumerProcessor{
	
	private String groupName;
	private String topic;
	private String queue;
	
	
	private EventLoopGroup worker = null;
	private Bootstrap b = null;
	private SocketAddress remoteAddress = null;
	
	public MessageConsumer(String clientId,String queue) {
		super(clientId);
		
		this.queue = queue;
		this.init();
		this.start();
	}
	

	public MessageConsumer(String clientId,String groupName,String topic) {
		
		super(clientId);
		
		if(StringUtils.isBlank(groupName) && StringUtils.isBlank(topic)){
			throw new IllegalArgumentException("组和主题至少提供一个!");
		}
		
		this.groupName = groupName;
		this.topic = topic;
		this.init();
		this.start();
		
	}
	
	
	
	@Override
	public synchronized void init() {
		if(isInited())return ;
		worker = new NioEventLoopGroup(1,getThreadFactory("ConsumerWorker[Selector]"));
		b = new Bootstrap();
		
		remoteAddress = new InetSocketAddress(NettyConfig.host, NettyConfig.port);
		
		b.option(ChannelOption.TCP_NODELAY, true);
		b.group(worker);
		b.channel(NioSocketChannel.class);
		b.remoteAddress(remoteAddress);
		b.handler(new ConsumerChannelInitializer(this));
		inited(true);
	}

	@Override
	public synchronized void start() {
		if(isRunning())return ;
		
		try {
			ChannelFuture channelFuture = b.connect().sync();
			
			channelFuture.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()){
						successReconnect(future.channel());
					}else{
						stop();
					}
				}
			});
			System.out.println("连接成功。。");
			
			
			//channelFuture.channel().closeFuture().sync();
			//System.out.println("关闭channel.........");
			//this.stop();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		System.out.println("isRunning()="+isRunning());
		if(isRunning()){
			worker.shutdownGracefully();
			inited(false);
			running(false);
		}
	}
	
	
	@Override
	public boolean register(){
		if(StringUtils.isBlank(topic))return false;
		ResponseMessage ack = register(groupName, topic);
		return ack.isSuccess();
	}
	
	public boolean listen2queue(){
		if(StringUtils.isBlank(queue))return false;
		return listen2queue(queue);
	}
	
	
	@Override
	public boolean unRegister(){
		return unRegister(groupName,topic);
	}

	@Override
	public Bootstrap getBootstrap() {
		return this.b;
	}
	
	@Override
	public SocketAddress getTargetSocketAddress() {
		return this.remoteAddress;
	}
	
	public boolean successReconnect(Channel ch){
		if(ch == null)return false;
		
		setChannel(ch);
		
		running(true);
		
		register();//监听topic和group
		listen2queue();//监听queue
		
		System.out.println("连接成功..time:"+System.currentTimeMillis());
		return true;
	}
	
	@Override
	public void handleResult(Object msg) {
	}
}
