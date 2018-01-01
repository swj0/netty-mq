package com.wen.jun.mq.broker;

import org.apache.commons.lang3.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class BrokerIdleStateTrigger extends ChannelInboundHandlerAdapter{

	private int idleReadTriggerCount = 0;//触发了几次读超时
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		//System.out.println("BrokerIdleStateTrigger msg="+msg);
		super.channelRead(ctx, msg);
	}
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
		if(evt instanceof IdleStateEvent){
			System.out.println("..............................");
			IdleStateEvent event = (IdleStateEvent) evt;
			
			if(event.state() == IdleState.READER_IDLE){
				idleReadTriggerCount++;
				
				System.out.println("5 秒没有接收到客户端的信息了...");
				
				if(idleReadTriggerCount > 2){//如果15秒之后也没有收到心跳报告，那么把订阅的相关信息删除
					QueuesManager.deSubscribe(ctx.channel());//取消监听队列
					//取消监听主题
					SubcribersManager.deSubscribe(ctx.channel());
					
					
					ctx.channel().close();
				}
			}
		}else{
			super.userEventTriggered(ctx, evt);
		}
		
		
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		if(cause != null && StringUtils.isNotBlank(cause.getMessage()) && cause.getMessage().indexOf("强迫关闭") > -1){
			QueuesManager.deSubscribe(ctx.channel());//取消监听队列
			//取消监听主题
			SubcribersManager.deSubscribe(ctx.channel());
			
			ctx.channel().close();
		}else{
			super.exceptionCaught(ctx, cause);
		}
		
	}
	
	
}
