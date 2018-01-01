package com.wen.jun.mq;

import java.util.concurrent.TimeUnit;

public class TestBase {

	protected static void delayMinutes(int m){
		try {
			TimeUnit.MINUTES.sleep(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void delayMILLISECONDS(int m){
		try {
			TimeUnit.MILLISECONDS.sleep(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
