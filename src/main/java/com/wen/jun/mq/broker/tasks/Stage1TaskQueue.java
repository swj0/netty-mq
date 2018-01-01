package com.wen.jun.mq.broker.tasks;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.domain.BrokerReceiveDto;
import com.wen.jun.mq.common.domain.BrokerReplyDto;

public class Stage1TaskQueue {
	
	private Stage1TaskQueue(){}
	
	private static  LinkedBlockingQueue<BrokerReceiveDto> stage1Messages = new LinkedBlockingQueue<>();
	
	/**
	 * 先接收发送者发送来的任何消息
	 * 人后立马把ack消息入列，告诉发送者，你发送的消息已经收到，但是这个消息不包括处理结果
	 * @param dto
	 * @return
	 */
	public static boolean offer (BrokerReceiveDto dto){
		try {
			
			boolean f = stage1Messages.offer(dto, 100, TimeUnit.MILLISECONDS);
			System.out.println("fffff="+f);
			if(f){//
				AckTaskQueue.offer(new BrokerReplyDto(dto.getCtx(), dto.getMsg(),MessageType.BrokerNormalReply,true));
				
				return true;
			}
			return false;
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
	public static BrokerReceiveDto poll(long timeout, TimeUnit unit){
		
		try {
			return stage1Messages.poll(timeout, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	

}
