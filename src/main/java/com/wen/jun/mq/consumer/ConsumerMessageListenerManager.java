package com.wen.jun.mq.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.NamedThreadLocal;

import com.wen.jun.mq.core.MessageListener;

/**
 * 极端情况：
 * 假设一个线程可以使用多个消费者
 * 而同一个应用下面可以有多个线程
 * @author swj
 *
 */
public class ConsumerMessageListenerManager {
	
	private static final ThreadLocal<Map<String, List<MessageListener>>> listeners =
			new NamedThreadLocal<Map<String, List<MessageListener>>>("ConsumerMessage listeners");
	
	
	/**
	 * 添加监听器
	 * @param clientId
	 * @param listener
	 * @return
	 */
	public static boolean addListener0(String clientId,MessageListener listener){
		Map<String,List<MessageListener>> map = listeners.get();
		
		if (map == null) {
			map = new HashMap<>();
			listeners.set(map);
		}
		
		List<MessageListener> listenersByConsumer = map.get(clientId);
		
		if(listenersByConsumer == null){
			listenersByConsumer = new ArrayList<>();
			
			map.put(clientId, listenersByConsumer);
		}
		
		listenersByConsumer.add(listener);
		
		return true;
	}
	
	/**
	 * 获取监听器
	 * @param clientId
	 * @return
	 */
	public static List<MessageListener> getListeners(String clientId){
		Map<String,List<MessageListener>> map = listeners.get();
		List<MessageListener > lis = map.get(clientId);
		
		return lis;
	}
	
	
}



















