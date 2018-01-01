package com.wen.jun.mq.test;


import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.cglib.proxy.Enhancer;

//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TestInterceptor implements MethodInterceptor{

	
	public Object getProxy(){
		ProxyFactory factory = new ProxyFactory();
		
		Enhancer en = new Enhancer();
		
		
		
		return en;
	}


	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
