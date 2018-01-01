package com.wen.jun.mq.broker.tasks;

import java.util.concurrent.Callable;

public interface BrokerPollerTask extends Callable<Void>{

	void stop();
	
	
	boolean isStop();
}
