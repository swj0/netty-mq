package com.wen.jun.mq.broker.tasks;

import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.broker.QueuesManager;
import com.wen.jun.mq.broker.SubcribersManager;
import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.domain.BrokerReceiveDto;
import com.wen.jun.mq.common.domain.BrokerReplyDto;
import com.wen.jun.mq.common.domain.MessageBase;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.common.domain.ResponseMessage;

import io.netty.channel.Channel;

public class Stage1Task extends AbstractBrokerPollerTask{

	/**
	 * 处理消费之消息
	 * @param stage
	 */
	private  void handleConsumerMessage(BrokerReceiveDto stage){
		RequestMessage request = (RequestMessage) stage.getMsg();
		
		if(request.getMsgType() == MessageType.Subscribe){//处理订阅请求
			boolean isSuccess = SubcribersManager.newSubcribe(request.getDestination(), request.getClientId(),stage.getCtx().channel());
			
			BrokerReplyDto replyDto = new BrokerReplyDto(stage.getCtx(),request,MessageType.SubscribeAck,isSuccess);
			AckTaskQueue.offer(replyDto);
			
		}else if(request.getMsgType() == MessageType.Listen2Queue){
			boolean isSuccess = QueuesManager.newQueueListener(request.getDestination(), request.getClientId(), stage.getCtx().channel());
			
			BrokerReplyDto replyDto = new BrokerReplyDto(stage.getCtx(), request, MessageType.Listen2QueueAck ,isSuccess);
			AckTaskQueue.offer(replyDto);
			
		}else if(request.getMsgType() == MessageType.Reply2Topic){
			
		}else if(request.getMsgType() == MessageType.Unsubscribe){
			
			SubcribersManager.deSubscribe(request.getDestination(), request.getClientId());
		}
	}
	
	/**
	 * 处理生产者消息
	 * @param stage
	 */
	private  void handleProducerMessage(BrokerReceiveDto stage ){
		RequestMessage request = (RequestMessage) stage.getMsg();
		if(request.getMsgType() == MessageType.Message2Topic){
			SubcribersManager.dispatch2Subcribers(request,stage.getCtx().channel());
		}else if(request.getMsgType() == MessageType.Message2Queue){
			QueuesManager.dispatch2queuer(request, stage.getCtx().channel());
		}
		
		
	}
	
	
	@Override
	public Void call() throws Exception {
		while(!isStop()){
			
			BrokerReceiveDto stage1 = Stage1TaskQueue.poll(100, TimeUnit.MILLISECONDS);
			if(stage1 != null){
				if(stage1.getMsg().getSource() == MessageSource.MessageProducer){
					handleProducerMessage(stage1);
				}else if(stage1.getMsg().getSource() == MessageSource.MessageConsumer){
					handleConsumerMessage(stage1);
				}
			}
			
		}
		return null;
	}

}
