package com.wen.jun.mq.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.core.NamedThreadLocal;

import com.wen.jun.mq.core.MessageListener;

/**
 * @author swj
 *
 */
public class ConsumerMessageListenerManager0 {
	

	private static ConcurrentMap<String, List<MessageListener>> listeners = new ConcurrentHashMap<>();
	
	/**
	 * 添加监听器
	 * @param clientId
	 * @param listener
	 * @return
	 */
	public static boolean addListener0(String clientId,MessageListener listener){
		List<MessageListener> listenersByConsumer = listeners.get(clientId);

		if(listenersByConsumer == null){
			listenersByConsumer = new ArrayList<>();
			
			listeners.put(clientId, listenersByConsumer);
		}
		
		listenersByConsumer.add(listener);
		System.out.println("添加了监听器:"+listeners);
		return true;
	}
	
	/**
	 * 获取监听器
	 * @param clientId
	 * @return
	 */
	public static List<MessageListener> getListeners(String clientId){
		List<MessageListener> lis = listeners.get(clientId);
		
		return lis;
	}
	
	
}



















