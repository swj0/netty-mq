package com.wen.jun.mq.broker;

import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.core.AbstractParallelProcessor;
import com.wen.jun.mq.core.ParallelProcessor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class BrokerChannelInitializer extends ChannelInitializer<Channel> {

	private ParallelProcessor processor;
	
	
	BrokerChannelInitializer(ParallelProcessor p){
		this.processor = p;
	}
	
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		
		
		p.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
		
		p.addLast(new LengthFieldPrepender(4));
		
		
		ch.pipeline().addLast(new IdleStateHandler(115, 0, 0, TimeUnit.SECONDS));
		
		
		ch.pipeline().addLast(new BrokerIdleStateTrigger());
		
		
		p.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
		//p.addLast(new ObjectDecoder(Integer.MAX_VALUE,ClassResolvers.cacheDisabled(null)));
		p.addLast(new ObjectEncoder());
		
		p.addLast(new BrokerMessageHandler(this.processor));
	}

}
