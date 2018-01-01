package com.wen.jun.mq.broker;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.domain.Message;
import com.wen.jun.mq.common.domain.QueueMetadata;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.common.domain.SubscriberMetadata;

import io.netty.channel.Channel;

public class QueuesManager {
	
	private static ConcurrentMap<String,Map<String,QueueMetadata>> queuesListeners = new ConcurrentHashMap<>();
	
	/**
	 * 用于链路断开删除监听
	 */
	private static ConcurrentMap<Channel, String> channels = new ConcurrentHashMap<>();
	
	/**
	 * 注册mou
	 * @param queueName
	 * @param clientId
	 * @param ch
	 * @return
	 */
	public static boolean newQueueListener(String queueName,String clientId,Channel ch){
		System.out.println("收到监听队列的请求。。。。。。");
		
		
		//1.
		Map<String,QueueMetadata> subscribersByQueue = queuesListeners.get(queueName);
		
		if(subscribersByQueue == null){
			subscribersByQueue = new HashMap<>();
			queuesListeners.put(queueName, subscribersByQueue);
		}
		
		
		//2.
		
		channels.put(ch, clientId);
		
		//3.
		QueueMetadata  sub = subscribersByQueue.get(clientId);
		
		if(sub == null){
			sub = new QueueMetadata();
			sub.setChannel(ch);
			sub.setClientId(clientId);
			sub.setQueue(queueName);
			subscribersByQueue.put(clientId, sub);
			
			System.out.println("subscribersByQueue="+subscribersByQueue);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * 取消订阅
	 * @param topic
	 * @param clientId
	 */
	public static boolean deSubscribe(String queneName,String clientId){
		System.out.println("收到取消监听队列的请求.....queneName="+queneName+",clientId="+clientId);
		Map<String,QueueMetadata> subscribersByQueue = queuesListeners.get(queneName);
		
		if(subscribersByQueue == null )return false;
		
		if( subscribersByQueue.isEmpty()){
			queuesListeners.remove(queneName);
			return false;
		}
		
		//
		subscribersByQueue.remove(clientId);
		
		if(subscribersByQueue.isEmpty())queuesListeners.remove(queneName);
		
		return true;
		
	}
	
	
	/**
	 * 删除监听
	 * @param ch
	 * @return
	 */
	public static boolean deSubscribe(Channel ch){
		if(ch == null)return false;
		
		String clientId = channels.get(ch);
		System.out.println("删除监听队列，clientID="+clientId);
		if(StringUtils.isBlank(clientId))return false;
		
		Set<String> queues = queuesListeners.keySet();
		
		for(String queneName : queues){
			deSubscribe(queneName,clientId);
		}
		
		return true;
	}
	/**
	 * 向订阅了某个主题的客户分发消息
	 * @param msg
	 * @param ch0
	 * @return
	 */
	public static boolean dispatch2queuer(RequestMessage msg,Channel ch0){
		//System.out.println("dispatch2queuer..."+msg);
		if(msg == null)
		return false;
		
		if(StringUtils.isBlank(msg.getDestination()))return false;
		
		Map<String,QueueMetadata>  subscribersByQueue = queuesListeners.get(msg.getDestination());
		
		//System.out.println("subscribersByQueue="+subscribersByQueue);
		
		
		if(subscribersByQueue == null || subscribersByQueue.isEmpty())return false;
		
		
		
		QueueMetadata qm = randomQueueMetadata(subscribersByQueue);
		
		if(qm == null)return false;
		
		Channel sch = qm.getChannel();
		
		Message msgFromBroker = new Message(MessageSource.MessageBroker,MessageType.MessageFromQueue);
		
		msgFromBroker.setMsgContent(msg.getMsgContent());
		msgFromBroker.setMsgId(msg.getMsgId());
		
		sch.writeAndFlush(msgFromBroker);
		
		
		return true;
	}

	/**
	 * 随机获取一个
	 * @param subscribersByQueue
	 * @return
	 */
	private static QueueMetadata randomQueueMetadata(Map<String, QueueMetadata> subscribersByQueue) {
		
		String[] clientIds = subscribersByQueue.keySet().toArray(new String[0]);
		
		Random random = new Random();
		
		String clientId = clientIds[random.nextInt(clientIds.length)];
		
		return subscribersByQueue.get(clientId);
	}
}







