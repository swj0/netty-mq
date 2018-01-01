package com.wen.jun.mq.broker.tasks;

public abstract class AbstractBrokerPollerTask implements BrokerPollerTask{


	private volatile boolean stop = false;
	
	@Override
	public void stop(){
		this.stop = true;
	}
	
	public boolean isStop(){
		return this.stop;
	}
	
}
