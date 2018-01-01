package com.wen.jun.mq.producer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.domain.ResponseMessage;

public class ProducerCallbackInvoker {
	
	public ProducerCallbackInvoker(String msgId) {
		this.msgId = msgId;
		latch = new CountDownLatch(1);
	}
	private CountDownLatch latch;
	
	private ResponseMessage ack ;
	
	private String msgId;
	
	public void setCause(Throwable ca){
		this.ack.setCause(ca);
	}
	
	public void setMsgContent(String content){
		this.ack.setMsgContent(content);
	}


	public ResponseMessage getAck() {
		try {
			//true if the count reached zero and false if the waiting time elapsed before the count reached zero
			boolean fo = latch.await(MessageRequestConfig.requestTimeOut, TimeUnit.MILLISECONDS);//
			if(!fo){//超时
				this.ack = new ResponseMessage(false, null);
				this.ack.setReason("请求超时");
			}else{
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ack;
	}

	public void done(ResponseMessage resp) {
		this.ack = resp;
		this.ack.setMsgId(this.msgId);
		latch.countDown();
	}
	
	
	
	
	
	

}
