package com.wen.jun.mq.core;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;

import com.wen.jun.mq.common.constant.StatusCode;
import com.wen.jun.mq.common.domain.MessageBase;
import com.wen.jun.mq.common.domain.RequestMessage;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public abstract class AbstractProcessor extends ThreadFactoryProvider implements Processor{
	
	public String clientId;
	
	private volatile boolean inited = false;
	private volatile boolean isRunning = false;
	

	public AbstractProcessor(String client) {
		this.clientId = client;
	}
	//等待Netty服务端链路建立通知信号
	protected Lock lock = new ReentrantLock();
	protected Condition signal = lock.newCondition();
		
		
	private Channel channel ;
		
	
	public void setChannel(Channel ch){
		try{
			lock.lock();
			this.channel = ch ;
			signal.signalAll();
		}finally{
			lock.unlock();
		}
	}
	
	protected Channel channel() throws InterruptedException{
		try{
			lock.lock();
			if(this.channel == null){
				signal.await();
			}
			
			return this.channel;
		}finally{
			lock.unlock();
		}
	}
	
	
	protected int send(MessageBase msg,ChannelFutureListener listener){
		msg.setClientId(clientId);
		try {
			
			System.out.println("发送msg="+msg);
			ChannelFuture future = channel().writeAndFlush(msg);
			
			
			if(listener != null){
				future.addListener(listener);
			}
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
	/**
	 * 
	 * @return 0:可以使用
	 * @throws InterruptedException 
	 * 
	 */
	public StatusCode checkChannelStatus() throws InterruptedException{
		try{
			lock.lock();
			if(this.channel == null){
				signal.await(1000, TimeUnit.MILLISECONDS);
			}
			
			if(channel == null)return StatusCode.LinkNotEstablished;//链路还没有建立
			
			if(!channel.isActive())return StatusCode.LinkNotActived;//没有激活
			
			if(!channel.isOpen())return StatusCode.LinkNotOpened;//链路没有打开
			
			if(!channel.isWritable())return StatusCode.LinkNotWritabled;//不可写
			
			return StatusCode.LinkUsabled;
		}finally{
			lock.unlock();
		}
		
	}
	
	
	

	@Override
	public boolean isInited() {
		return this.inited;
	}
	public void inited(boolean flag){
		this.inited = flag;
	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}
	
	protected void running(boolean flag){
		this.isRunning = flag;
	}
	
	protected void checkMsgId(RequestMessage request){
		if(StringUtils.isBlank(request.getMsgId())){
			request.setMsgId(UUID.randomUUID().toString());
		}
	}

	public abstract boolean successReconnect(Channel ch);
	public abstract void handleResult(Object msg) ;
}
