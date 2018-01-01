package com.wen.jun.mq.core;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.domain.HeartbeatMessage;
import com.wen.jun.mq.common.domain.RequestMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;



//有可能会重复添加，所以要shareable
@Sharable  
public class ConnectorHeartbeatSender extends ChannelInboundHandlerAdapter{

	private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("alive", CharsetUtil.UTF_8));  
	
	
	@Override//如果间隔4s没有写事件发生，则会导致前端的
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
		if(evt instanceof IdleStateEvent){
			IdleState state = ((IdleStateEvent)evt).state();
			if(state == IdleState.WRITER_IDLE){
				//System.out.println("超时写.............");
				//注意这里要发送的心跳信息包，因为这个handler是比较靠前，ctx.writeAndFlush写的方式是不经过编码器的，只能发送ByteBuf，如果发送了
				//其他的对象信息包,那么下面的handler是不认的,但是这里目前产生一个问题，broker无法解析出消息对象了，所以暂时不使用这种方式了
				//ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());//注:如果发送的消息经过broker的handler不能解码成相应的消息对象，那么是不会到达业务handler的，不过这样的心跳包是有晓得
				
				
				
				// * 而如果使用ctx.channel().writeAndFlush来发送心跳包，则可以使用ByteBuf以及其他的对象，只要pipeline中有相应的编码包
				// *因为这种方式的写方式是从tail  Context写出去的
				
				ctx.channel().writeAndFlush("1");
				
			}
		}else{
			super.userEventTriggered(ctx, evt);
		}
		
	}
	
	
	
}
