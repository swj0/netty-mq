package com.wen.jun.mq.consumer;

import java.util.UUID;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.constant.StatusCode;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.common.domain.ResponseMessage;
import com.wen.jun.mq.core.AbstractProcessor;
import com.wen.jun.mq.core.MessageListener;
import com.wen.jun.mq.core.tasks.ConsumerReceiveTask;

import static com.wen.jun.mq.consumer.ConsumerCallbackManager.*;
import static com.wen.jun.mq.consumer.ConsumerMessageListenerManager0.*;

import io.netty.channel.Channel;

public abstract class ConsumerProcessor extends AbstractProcessor{
	
	
	
	public ConsumerProcessor(String clientId) {
		super(clientId);
	}

	
	/**
	 * 订阅主题
	 * @param groupName
	 * @param topic
	 * @return
	 */
	protected ResponseMessage register(String groupName,String topic){
		String msgId = UUID.randomUUID().toString();
		try {
			StatusCode status = checkChannelStatus();
			
			System.out.println("注册 status="+status);
			if(status != StatusCode.LinkUsabled){
				ResponseMessage ack = new ResponseMessage(false,null);
				ack.setStatus(status);
				return ack;
			}
			
			//1.
			RequestMessage request = new RequestMessage(MessageSource.MessageConsumer,MessageType.Subscribe);
			request.setMsgId(msgId);
			request.setGroupName(groupName);
			
			
			request.setMsgContent("我这是在订阅...");
			request.setDestination(topic);
			
			//2.
			
			ConsumerCallbackInvoker callback = new ConsumerCallbackInvoker(null, msgId,50);//超时50ms,因为这里的注册是被自己的eventloop调用，不能阻塞太久，否则有可能影响到io以及其他任务的执行
			
			
			addCallback(msgId, callback);
			//3.
			send(request,null);
			
			//4.
			return callback.getAck();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return new ResponseMessage(false,e);
		}finally{
			removeCallback(msgId);
		}
		
	}

	/**
	 * 
	 * @param groupName
	 * @param topic
	 * @return
	 */
	protected boolean unRegister(String groupName,String topic){
		try {
			StatusCode status = checkChannelStatus();
			
			System.out.println("取消注册 status="+status);
			if(status != StatusCode.LinkUsabled){
				return false;
			}
			RequestMessage request = new RequestMessage(MessageSource.MessageConsumer,MessageType.Unsubscribe);
			request.setMsgId(UUID.randomUUID().toString());
			
			
			request.setGroupName(groupName);
			request.setDestination(topic);
			request.setClientId(clientId);
			
			request.setMsgContent("我这是在取消订阅...");
			
			
			send(request,null);
			
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 监听队列
	 * @param queueName
	 * @return
	 */
	protected boolean listen2queue(String queueName){
		try {
			StatusCode status = checkChannelStatus();
			
			System.out.println("监听队列 status="+status);
			if(status != StatusCode.LinkUsabled){
				return false;
			}
			RequestMessage request = new RequestMessage(MessageSource.MessageConsumer,MessageType.Listen2Queue);
			request.setMsgId(UUID.randomUUID().toString());
			
			
			request.setClientId(clientId);
			request.setDestination(queueName);
			request.setMsgContent("我这是请求监听队列...");
			
			
			send(request,null);
			
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public boolean receive(Channel ch, Object msg) {
		ConsumerReceiveTask task = new ConsumerReceiveTask(clientId,msg);
		ch.eventLoop().submit(task);
		return true;
	}
	
	@Override
	public boolean addListener(MessageListener listener) {
		if(listener == null)return false;
		
		return addListener0(clientId, listener);
	}

}
