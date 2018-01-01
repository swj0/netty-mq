package com.wen.jun.mq.core;

public interface MQLifeCycle {
	
	default void init(){throw new  UnsupportedOperationException("未实现!!");}
	
	void start();
	void stop();
	
	boolean isInited();
	
	boolean isRunning();
	

}
