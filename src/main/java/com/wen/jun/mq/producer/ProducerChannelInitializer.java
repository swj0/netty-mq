package com.wen.jun.mq.producer;

import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.consumer.ConsumerConnectionWatchdog;
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

public class ProducerChannelInitializer extends ChannelInitializer<Channel> {

	private AbstractProcessor processor;
	protected final HashedWheelTimer timer = new HashedWheelTimer();
	private final ConnectorHeartbeatSender heartbeatSender = new ConnectorHeartbeatSender(); 
	
	final ProducerConnectionWatchdog watchdog ;
	
	
	ProducerChannelInitializer(AbstractProcessor p){
		processor = p;
		watchdog = new ProducerConnectionWatchdog(p,timer,true) {
			
			@Override
			public ChannelHandler[] handlers() {
				ChannelHandler[] hands = new ChannelHandler[]{
						this,
						new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,4,0,4) ,
						
						new LengthFieldPrepender(4),
						new IdleStateHandler(0, 4, 0,TimeUnit.SECONDS),
						heartbeatSender,
						new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)),
						new ObjectEncoder(),
						new ProducerMessageHandler(this.processor)
				};
				return hands;
			}
		};
	}
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		
		p.addLast(watchdog.handlers());
		
		
		
		
		/*
		p.addLast(new IdleStateHandler(0, 4, 0,TimeUnit.SECONDS));
		p.addLast(heartbeatSender);
		p.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
		p.addLast(new ObjectEncoder());
		p.addLast(new ProducerMessageHandler(this.processor));
		*/
	}

}
