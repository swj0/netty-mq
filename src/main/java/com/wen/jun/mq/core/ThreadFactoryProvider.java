package com.wen.jun.mq.core;

import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public abstract class ThreadFactoryProvider {
	
	/**
	 * 获取线程
	 * broker和producer都要使用
	 * @param isBoss
	 * @return
	 */
	protected ThreadFactory getThreadFactory(String threadName){
		ThreadFactory threadFactory = new ThreadFactoryBuilder()
				//.setNameFormat( "BrokerBoss[Selector]-%d" : "BrokerWorker[Selector]-%d")
				.setNameFormat(threadName+"-%d")
				.setDaemon(false)//true:当main函数退出的时候，所有的线程也会退出,开发的时候可以设置为false
				.build();
		
		
		return threadFactory;
	}

}
