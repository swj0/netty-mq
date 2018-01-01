package com.wen.jun.mq.broker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.domain.Message;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.common.domain.SubscriberMetadata;

import io.netty.channel.Channel;

public class SubcribersManager {
	
	private static ConcurrentMap<String,Map<String,SubscriberMetadata>> subsribers = new ConcurrentHashMap<>();
	
	/**
	 * 用于链路断开删除监听
	 */
	private static ConcurrentMap<Channel, String> channels = new ConcurrentHashMap<>();
	
	
	
	/**
	 * 注册mou
	 * @param topic
	 * @param clientId
	 * @param ch
	 * @return
	 */
	public static boolean newSubcribe(String topic,String clientId,Channel ch){
		System.out.println("收到订阅请求。。。。。。");
		
		//1.
		Map<String,SubscriberMetadata> subscribersByTopic = subsribers.get(topic);
		
		if(subscribersByTopic == null){
			subscribersByTopic = new HashMap<>();
			subsribers.put(topic, subscribersByTopic);
		}
		
		//2.
		channels.put(ch, clientId);
		
		
		//3.
		SubscriberMetadata  sub = subscribersByTopic.get(clientId);
		
		if(sub == null){
			sub = new SubscriberMetadata();
			sub.setChannel(ch);
			sub.setClientId(clientId);
			sub.setTopic(topic);
			subscribersByTopic.put(clientId, sub);
			
			System.out.println("subscribersByTopic="+subscribersByTopic);
			
			return true;
		}
		
		return false;
	}
	
	
	public static boolean deSubscribe(Channel ch){
		if(ch == null)return false;
		
		String clientId = channels.get(ch);
		
		if(StringUtils.isBlank(clientId))return false;
		
		Set<String> keys = subsribers.keySet();
		
		for(String key : keys){
			deSubscribe(key, clientId);
		}
		
		return true;
	}
	/**
	 * 取消订阅
	 * @param topic
	 * @param clientId
	 */
	public static boolean deSubscribe(String topic,String clientId){
		System.out.println("收到取消订阅请求.....topic="+topic+",clientId="+clientId);
		Map<String,SubscriberMetadata> subscribersByTopic = subsribers.get(topic);
		
		if(subscribersByTopic == null )return false;
		
		if( subscribersByTopic.isEmpty()){
			subsribers.remove(topic);
			return false;
		}
		
		//
		subscribersByTopic.remove(clientId);
		
		if(subscribersByTopic.isEmpty())subsribers.remove(topic);
		
		return true;
		
	}
	
	
	/**
	 * 向订阅了某个主题的客户分发消息
	 * @param msg
	 * @param ch0
	 * @return
	 */
	public static boolean dispatch2Subcribers(RequestMessage msg,Channel ch0){
		System.out.println("dispatch2Subcribers..."+msg);
		if(msg == null)
		return false;
		
		if(StringUtils.isBlank(msg.getDestination()))return false;
		
		Map<String,SubscriberMetadata>  subscribersByTopic = subsribers.get(msg.getDestination());
		
		System.out.println("subscribersByTopic="+subscribersByTopic);
		
		
		if(subscribersByTopic == null || subscribersByTopic.isEmpty())return false;
		
		
		ch0.eventLoop().execute(new Runnable() {
			
			@Override
			public void run() {
				for(String key : subscribersByTopic.keySet()){
					SubscriberMetadata subscribe = subscribersByTopic.get(key);
					if(subscribe == null)continue;
					Channel sch = subscribe.getChannel();
					
					Message msgFromBroker = new Message(MessageSource.MessageBroker,MessageType.MessageFromTopic);
					
					msgFromBroker.setMsgContent(msg.getMsgContent());
					msgFromBroker.setMsgId(msg.getMsgId());
					
					
					
					sch.writeAndFlush(msgFromBroker);
				}
			}
		});
		
		
		
		return true;
	}
}







