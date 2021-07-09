package com.sr.ioc.ioc2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IOC2Main {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("springApplication.xml");

		System.out.println(context);

		Object person = context.getBean("person");
		System.out.println(person);
	}

}
