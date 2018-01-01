package com.wen.jun.mq.consumer;

import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.core.AbstractProcessor;
import com.wen.jun.mq.core.ConnectorHeartbeatSender;
import com.wen.jun.mq.core.Processor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;

public class ConsumerChannelInitializer extends ChannelInitializer<Channel> {

	
	
	protected final HashedWheelTimer timer = new HashedWheelTimer();
	private final ConnectorHeartbeatSender heartbeatSender = new ConnectorHeartbeatSender(); 
	
	final ConsumerConnectionWatchdog watchdog ;
	
	ConsumerChannelInitializer(AbstractProcessor processor){
		
		watchdog = new ConsumerConnectionWatchdog(processor,timer,true) {
			
			@Override
			public ChannelHandler[] handlers() {
				return new ChannelHandler[]{
						this,
						new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,4,0,4) ,
						
						new LengthFieldPrepender(4),
						new IdleStateHandler(0, 4, 0,TimeUnit.SECONDS),
						heartbeatSender,
						new  ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)),
						new  ObjectEncoder(),
						new  ConsumerMessageHandler(processor)
				};
			}
		};
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast(watchdog.handlers());
		
		
		
		//p.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
		//p.addLast(new ObjectEncoder()	);
		//p.addLast(new ConsumerMessageHandler(processor));
	}

}
