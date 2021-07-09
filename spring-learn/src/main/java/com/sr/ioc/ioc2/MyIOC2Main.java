package com.sr.ioc.ioc2;

import com.sr.ioc.ioc2.application.MyClassPathXmlApplicationContent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyIOC2Main {
	public static void main(String[] args) {
		ApplicationContext context = new MyClassPathXmlApplicationContent("springApplication.xml");

		System.out.println(context);
	}
}
