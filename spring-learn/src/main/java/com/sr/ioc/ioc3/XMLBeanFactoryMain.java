package com.sr.ioc.ioc3;

import com.sr.ioc.ioc2.bean.Person;
import com.sr.ioc.ioc3.constructorarg.HelloBean;
import com.sr.ioc.ioc3.factoryBean.Car;
import com.sr.ioc.ioc3.factoryBean.CarFactoryBean;
import com.sr.ioc.ioc3.lookupmethod.GetBeanTest;
import com.sr.ioc.ioc3.replacedmethod.TestChangeMethod;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class XMLBeanFactoryMain {

	public static void main(String[] args) {

		ClassPathResource resource = new ClassPathResource("springApplication-xml.xml");
		XmlBeanFactory factory = new XmlBeanFactory(resource);
//		GetBeanTest test = (GetBeanTest)factory.getBean("getBeanTest");
//		test.showMe();
//		TestChangeMethod testChangeMethod = (TestChangeMethod)factory.getBean("testChangeMethod");
//		testChangeMethod.changeMe();
//
//		HelloBean helloBean = (HelloBean)factory.getBean("helloBean");
//		helloBean.showInfo();
//
//		BeanDefinition beanDefinition = factory.getBeanDefinition("person");
//		Object testStr = beanDefinition.getAttribute("testStr");
//		System.out.println(testStr);

		Person person = (Person)factory.getBean("person");
		System.out.println(person);

//		Car car = (Car)factory.getBean("car");
//		System.out.println(car);
//		CarFactoryBean carFactory = (CarFactoryBean)factory.getBean("&car");
//		System.out.println(carFactory);
	}
}
