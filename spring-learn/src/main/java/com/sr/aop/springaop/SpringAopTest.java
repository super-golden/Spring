package com.sr.aop.springaop;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringAopTest {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-aop.xml");

		AbcInterface proxyFactoryBean = (AbcInterface) context.getBean("proxyFactoryBean");
		proxyFactoryBean.add();
		System.out.println("*************************");
		ProxyFactory proxyFactory = context.getBean("proxyFactory", ProxyFactory.class);
		proxyFactory.addAdvice(new TestAdvisor());
		TestTarget proxy = (TestTarget) proxyFactory.getProxy();
		proxy.add();

	}
}
