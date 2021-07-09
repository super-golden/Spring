package com.sr.aop.aop2;

import org.springframework.aop.framework.AopContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//@EnableAspectJAutoProxy
public class AopMain {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("/springApplication-aop.xml");
		TestBean test = (TestBean) context.getBean("test");
		//System.out.println(AopContext.currentProxy());
		test.test();
	}
}
