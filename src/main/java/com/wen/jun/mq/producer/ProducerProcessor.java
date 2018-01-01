package com.wen.jun.mq.producer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.set.SynchronizedSortedSet;
import org.apache.commons.lang3.StringUtils;

import com.wen.jun.mq.common.constant.MessageRequestStatus;
import com.wen.jun.mq.common.constant.StatusCode;
import com.wen.jun.mq.common.domain.MessageBase;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.common.domain.ResponseMessage;
import com.wen.jun.mq.core.AbstractProcessor;
import com.wen.jun.mq.core.Processor;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class ProducerProcessor extends AbstractProcessor{
	
	private ConcurrentHashMap<String, ProducerCallbackInvoker> callMap = new ConcurrentHashMap<>();
	
	public ProducerProcessor(String client) {
		super(client);
	}
	/**
	 * 生产者发送异步消息
	 * @param msg
	 * @return
	 */
	public  ResponseMessage send(RequestMessage msg){
		StatusCode status;
		checkMsgId(msg);
		try {
			status = checkChannelStatus();
			System.out.println(".............status="+status);
			if(status != StatusCode.LinkUsabled){
				ResponseMessage ack = new ResponseMessage();
				ack.setStatus(status);
				ack.setMsgId(msg.getMsgId());
				return ack;
			}
			
			
			ProducerCallbackInvoker invoker =  new ProducerCallbackInvoker(msg.getMsgId());
			
			callMap.put(msg.getMsgId(), invoker);
			
			
			ChannelFutureListener listener = new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(!future.isSuccess()){
						invoker.setCause(future.cause());
						invoker.done(new ResponseMessage(false,future.cause()));
					}
				}
			};
			long then = System.currentTimeMillis();
			send(msg,listener);
			System.out.println("send cost = "+(System.currentTimeMillis() - then));
			
			then = System.currentTimeMillis();
			ResponseMessage ack = invoker.getAck();
			System.out.println("ack cost = "+(System.currentTimeMillis() - then));
			return ack;
		} catch (InterruptedException e) {
			ResponseMessage ack = new ResponseMessage(false,e);
			ack.setCause(e);
			return ack;
		}finally{
			callMap.remove(msg.getMsgId());
		}
	}
	
	
	public  boolean sendAsynMessage(RequestMessage msg){
		StatusCode status;
		checkMsgId(msg);
		try {
			status = checkChannelStatus();
			
			if(status != StatusCode.LinkUsabled){
				System.out.println(".............status="+status);
				return false;
			}
			
			
			ProducerCallbackInvoker invoker =  new ProducerCallbackInvoker(msg.getMsgId());
			
			callMap.put(msg.getMsgId(), invoker);
			
			
			ChannelFutureListener listener = new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(!future.isSuccess()){
						invoker.setCause(future.cause());
						invoker.done(new ResponseMessage(false,future.cause()));
					}
				}
			};
			long then = System.currentTimeMillis();
			send(msg,listener);
			System.out.println("send cost = "+(System.currentTimeMillis() - then));
			
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}finally{
			callMap.remove(msg.getMsgId());
		}
	}

	public void handleResult(Object msg) {
		if(msg == null)return ;
		
		if(msg instanceof ResponseMessage){
			System.out.println("ResponseMessage="+msg);
			ResponseMessage resp = (ResponseMessage) msg;
			
			if(StringUtils.isBlank(resp.getMsgId()))return ;
			
			ProducerCallbackInvoker invoker = callMap.get(resp.getMsgId());
			if(invoker == null	)return ;
			System.out.println("invoker resp="+resp);
			invoker.done(resp);
			
			callMap.remove(resp.getMsgId());
		}
	}


	
}
