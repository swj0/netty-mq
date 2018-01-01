package com.wen.jun.mq.consumer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConsumerCallbackManager {
	
	private ConsumerCallbackManager(){}
	
	private static  ConcurrentMap<String, ConsumerCallbackInvoker> callMap = new ConcurrentHashMap<>();
	
	public static boolean addCallback(String msgId,ConsumerCallbackInvoker callback){
		callMap.put(msgId, callback);
		
		return true;
	}
	
	public static boolean removeCallback(String msgId){
		callMap.remove(msgId);
		
		return true;
	}

}
