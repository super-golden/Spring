package com.sr.aop.springaop;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringAopTest {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-aop.xml");

		AbcInterface proxyFactoryBean = (AbcInterface) context.getBean("proxyFactoryBean");
		proxyFactoryBean.add();
//		System.out.println("*************************");
//		TestTarget testTarget = context.getBean("testTarget", TestTarget.class);
//		ProxyFactory proxyFactory = context.getBean("proxyFactory", ProxyFactory.class);
//		proxyFactory.addAdvice(new TestAdvisor());
//		TargetSource singletonTargetSource = new SingletonTargetSource(testTarget);
//		TestTarget proxy = (TestTarget) proxyFactory.getProxy(singletonTargetSource);
//		System.out.println(proxyFactory.getProxy(singletonTargetSource));
//		proxy.add();

	}
}
