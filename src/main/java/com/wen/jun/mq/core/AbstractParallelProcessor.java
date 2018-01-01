package com.wen.jun.mq.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang3.StringUtils;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wen.jun.mq.broker.tasks.BrokerPollerTask;
import com.wen.jun.mq.broker.tasks.BrokerReplyTask;
import com.wen.jun.mq.broker.tasks.Stage1Task;
import com.wen.jun.mq.broker.tasks.Stage1TaskQueue;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.domain.BrokerReceiveDto;
import com.wen.jun.mq.common.domain.RequestMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


import static com.wen.jun.mq.common.constant.NettyConfig.BROKER_REPLY_TASK_NUM;
import static com.wen.jun.mq.common.constant.NettyConfig.BROKER_STAGE1_TASK_NUM;



public abstract class AbstractParallelProcessor extends ThreadFactoryProvider implements ParallelProcessor{

	int parallel = BROKER_STAGE1_TASK_NUM + BROKER_REPLY_TASK_NUM;
	
	
	private  EventLoopGroup executorService = null;
	
	//ExecutorService executorService = null;
	
	//protected ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(parallel));
	//protected ExecutorCompletionService<Void> executorService;
	
	private List<BrokerPollerTask> tasks = null;
	
	
	@Override
	public void init() {
		executorService = new NioEventLoopGroup(parallel,getThreadFactory("BrokerWorker[Pooler]"));
		//executorService = Executors.newFixedThreadPool(parallel);
		//executorService = new ExecutorCompletionService<>(executor);
		
		initTasks ();
	}
	
	
	@Override
	public void start() {
		
		for(BrokerPollerTask task : tasks){
			
			executorService.submit(task);
		}
	}
	
	@Override
	public void stop() {
		stopTasks();
		
		executorService.shutdownGracefully();
		System.out.println("关闭 ll  l");
		//executorService.shutdown();
		
		//executor.shutdown();
	}
	
	/**
	 * 将阶段1的信息入队，后面的处理分派给各个Task
	 */
	public  boolean offer(ChannelHandlerContext ctx, Object msg) {
		
		if(msg instanceof RequestMessage){
			RequestMessage req = (RequestMessage) msg;
			
			
			BrokerReceiveDto sm = new BrokerReceiveDto(ctx,req);
			
				
			System.out.println("offer="+req.getMsgId()+",msg="+req);
			
			
			return Stage1TaskQueue.offer(sm);
		}else{
			//System.out.println("收到心跳消息或者非法信息，丢弃....."+msg.getClass());
		}
		
		return false;
		
	}
	
	/**
	 * 初始化任务
	 */
	private void initTasks(){
		
		if(tasks == null)tasks = new ArrayList<>();
		
		for(int i=0;i<BROKER_REPLY_TASK_NUM;i++){
			tasks.add(new BrokerReplyTask());
		}
		
		
		for(int i=0;i<BROKER_STAGE1_TASK_NUM;i++){
			tasks.add(new Stage1Task() );
		}
		
		
	}
	
	/**
	 * 停止任务
	 */
	private void stopTasks(){
		if(tasks == null || tasks.size()<1)return ;
		
		for(BrokerPollerTask task : tasks){
			task.stop();
		}
	}
	
	
}
