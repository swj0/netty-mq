package com.wen.jun.mq.test3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.wen.jun.mq.common.constant.MessageRequestConfig;
import com.wen.jun.mq.common.constant.MessageSource;
import com.wen.jun.mq.common.constant.MessageType;
import com.wen.jun.mq.common.domain.RequestMessage;
import com.wen.jun.mq.producer.MessageProducer;


/**
 * 
 * @author swj
 *	测试broker在本台计算机上可以接受多少个连接（未经过jvm参数优化配置的情况下8100个左右）
 *	其实更准确的来说是测试broker可以处理的并发消息能力,不过这里的测试方式不准确，
 *	因为受到tcp发送窗口的影响
 */
public class TestProducer2 {

	//这个测试要注意broker的boss线程数以及SO_BACKLOG
	public static void main(String[] args) {
		AtomicInteger ecounter = new AtomicInteger(0) ;
		CountDownLatch latch = new CountDownLatch(1);
		for(int i=1;i<=8000;i++){
			new Thread(new SendTask(latch, i,ecounter)).start();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		latch.countDown();
		try {
			
			System.out.println("ecounter="+ecounter);//异常计数器
			Thread.sleep(100000);
			
			System.out.println("ecounter="+ecounter);//异常计数器
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
class SendTask implements Runnable{
	
	private CountDownLatch latch ;
	
	private int index;
	
	private AtomicInteger ecounter;
	
	public SendTask(CountDownLatch latch,int index,AtomicInteger ecounter) {
		this.index = index;
		this.latch = latch;
		this.ecounter = ecounter;
	}

	@Override
	public void run() {
		try {
			
			MessageProducer sender = new MessageProducer("producer3."+index);
			latch.await();
			
			for(int i=0;i<1;i++){

				RequestMessage request = new RequestMessage(MessageSource.MessageProducer,MessageType.Message2Queue);
				request.setMsgContent(""+sender.clientId+"发送 的消息");
			
				request.setDestination(MessageRequestConfig.defaultQueue);
				request.setMsgId(sender.clientId+"."+String.valueOf(i));
				sender.sendAsynMessage(request);
			}
			TimeUnit.SECONDS.sleep(3);
			sender.stop();
		} catch (Exception e) {
			ecounter.incrementAndGet();
			System.out.println("产生异常。。。。。。。。。。。");
			e.printStackTrace();
		}
		//2660 + 2720 = 4380;
	}
	
}

















