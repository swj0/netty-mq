package com.wen.jun.mq.broker.tasks;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.domain.BrokerReplyDto;

public class AckTaskQueue {
	
	private AckTaskQueue(){}
	
	private static LinkedBlockingQueue<BrokerReplyDto> replayCaches = new LinkedBlockingQueue<>();
	
	public static boolean  offer (BrokerReplyDto dto){
		try {
			System.out.println("回复队列入队dto="+dto.getMsg().getClass());
			return replayCaches.offer(dto, 100, TimeUnit.MILLISECONDS);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			return false;
		}
	}
	
	/**
	 * 
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public static  BrokerReplyDto poll(long timeout, TimeUnit unit){
		try {
			BrokerReplyDto dto =  replayCaches.poll(timeout, unit);
			
			return dto;
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			
			return null;
		}
	}
	
	
	

}
