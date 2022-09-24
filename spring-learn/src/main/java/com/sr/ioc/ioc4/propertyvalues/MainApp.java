package com.sr.ioc.ioc4.propertyvalues;

import com.sr.ioc.ioc4.propertyvalues.bean.Person;
import com.sr.ioc.ioc4.propertyvalues.config.MainConfigOfPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfigOfPropertyValues.class);
		String[] beanDefinitionNames = context.getBeanDefinitionNames();
		for (String beanDefinitionName : beanDefinitionNames) {
			System.out.println(beanDefinitionName);
		}

		Person person = context.getBean("person", Person.class);
		System.out.println(person);
	}
}
