package com.wen.jun.mq.broker.tasks;

import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.domain.BrokerReplyDto;
import com.wen.jun.mq.common.domain.MessageBase;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.common.domain.ResponseMessage;

import io.netty.channel.Channel;

import static com.wen.jun.mq.common.constant.NettyConfig.TAKE_ELEMENT_TIME_OUT;

public class BrokerReplyTask extends AbstractBrokerPollerTask{
	
	@Override
	public Void call() throws Exception {
		System.out.println("BrokerReplyTask..........开始执行"+isStop());
		
		while(!isStop()){
			try{
				BrokerReplyDto dto = AckTaskQueue.poll(TAKE_ELEMENT_TIME_OUT, TimeUnit.MILLISECONDS);
				if(dto != null){
					if (dto.getMsg().getSource() == MessageSource.MessageProducer) {
						
					
						ack2producer(dto);
					}else if(dto.getMsg().getSource() == MessageSource.MessageConsumer){
						
						ack2consumer(dto);
					}
				}
				dto = null;
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 回复消息给生产者,告诉生产者broker已经收到你的消息了(但是不确定消费者是否会收到消息)
	 * @param ch
	 * @param msg
	 */
	private  void ack2producer(BrokerReplyDto dto){
		
		RequestMessage msg = (RequestMessage) dto.getMsg();
		
		ResponseMessage response = new ResponseMessage(MessageSource.MessageBroker,dto.getMsgType());
		
		response.setMsgId(msg.getMsgId());
		response.setMsgContent("broker已经收到消息："+msg.getMsgId());
		response.setSuccess( dto.isHandleSuccess());
		
		dto.getCtx().writeAndFlush(response);
		
	}
	
	/**
	 * 暂时两个方法的代码一样，待以后扩展的需要
	 * @param ch
	 * @param msg
	 */
	private void ack2consumer(BrokerReplyDto dto){
		
		RequestMessage msg = (RequestMessage) dto.getMsg();
		
		
		ResponseMessage response = new ResponseMessage(MessageSource.MessageBroker,dto.getMsgType());
		response.setMsgId(msg.getMsgId());
		
		response.setMsgContent("broker已经收到消息："+msg.getMsgId());
		response.setSuccess(dto.isHandleSuccess());
		
		dto.getCtx().channel().writeAndFlush(response);
		
	}
}
