package com.wen.jun.mq.core.tasks;

import java.util.concurrent.Callable;

import java.util.*;
import com.wen.jun.mq.common.domain.Message;
import com.wen.jun.mq.common.domain.ResponseMessage;
import com.wen.jun.mq.core.MessageListener;

import static com.wen.jun.mq.consumer.ConsumerMessageListenerManager0.*;

public class ConsumerReceiveTask implements Callable<Void>{
	
	private Object msg;
	private String clientId;
	
	
	public ConsumerReceiveTask(Object msg) {
		this.msg = msg;
	}

	public ConsumerReceiveTask(String clientId,Object msg) {
		this.clientId = clientId; 
		this.msg = msg;
	}
	
	
	
	
	/**
	 * 在生产环境中应该把Message的域设置成final
	 */
	@Override
	public Void call() throws Exception {
		
		if(msg instanceof Message	){
			System.out.println("收到了队列消息或者主题消息");
			
			
			
			List<MessageListener> listeners = getListeners(clientId);
			//System.out.println("获取到的监听器:"+listeners);
			if(listeners != null && listeners.size()>0){
				for(MessageListener listener : listeners){
					listener.onMessage((Message)msg);
				}
			}
		}else if(msg instanceof ResponseMessage){
			System.out.println("收到了回复消息msg:"+msg);
		}
		return null;
	}

}
