package com.sr.ioc.ioc2.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EventMain {

	public static void main(String[] args) {
		Class[] classes = new Class[]{TestListener.class};
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(classes);
//		System.out.println(context);
//		for (String beanDefinitionName : context.getBeanDefinitionNames()) {
//			System.out.println(beanDefinitionName);
//		}

		System.out.println("========================");
		TestEvent testEvent = new TestEvent("hello", "msg");
		context.publishEvent(testEvent);

	}
}
