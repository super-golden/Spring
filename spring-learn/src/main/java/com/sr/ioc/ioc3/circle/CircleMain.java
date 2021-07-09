package com.sr.ioc.ioc3.circle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class CircleMain {

	public static void main(String[] args) throws Throwable {

//		ClassPathResource resource = new ClassPathResource("springApplication-circle.xml");
//		XmlBeanFactory factory = new XmlBeanFactory(resource);
//		System.out.println(factory);
//		TestA testA = (TestA)factory.getBean("testA");
//		System.out.println(testA);
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("springApplication-circle.xml");
			System.out.println(context);
			//context.setAllowCircularReferences(false);
			System.out.println(context.getBean("testA"));
		} catch (BeansException e) {
			Throwable cause = e.getCause().getCause().getCause();
			throw cause;
		}

	}
}
