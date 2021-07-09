package com.sr.testImport;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);

		for(String beanName :ctx.getBeanDefinitionNames()){
			System.out.println("beanName:"+ beanName);
		}
	}
}
