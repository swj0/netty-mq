package com.wen.jun.mq.consumer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.domain.ResponseMessage;

public class ConsumerCallbackInvoker {
	
	public ConsumerCallbackInvoker(ResponseMessage ack,String msgId ,long timeout) {
		this(ack,msgId);
		this.timeout = timeout;
	}
	
	
	public ConsumerCallbackInvoker(ResponseMessage ack,String msgId) {
		this.ack = (ack == null ) ? new ResponseMessage() : ack;
		
		this.ack.setMsgContent("可能超时。。。。。。。。。。。。。。。");
		
		this.ack.setMsgContent(msgId);
		
		
		latch = new CountDownLatch(1);
	}
	
	private long timeout = -1;
	private CountDownLatch latch;
	
	
	private ResponseMessage ack ;
	

	public void setCause(Throwable ca){
		this.ack.setCause(ca);
	}
	
	public void setMsgContent(String content){
		this.ack.setMsgContent(content);
	}


	public ResponseMessage getAck() {
		try {
			////true if the count reached zero and false if the waiting time elapsed before the count reached zero
			boolean notTimeout = latch.await(timeout >= 0 ? this.timeout : MessageRequestConfig.requestTimeOut, TimeUnit.MILLISECONDS);
			if(!notTimeout){
				ack.setSuccess(false);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.ack.setSuccess(false);
		}
		return ack;
	}

	public void done() {
		latch.countDown();
	}
	
	
	

}
