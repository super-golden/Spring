package com.sr.ioc.ioc3.aware;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AwareMain {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("springApplication-aware.xml");

		TestAware testAware = (TestAware)context.getBean("testAware");
		testAware.testAware();


	}
}
