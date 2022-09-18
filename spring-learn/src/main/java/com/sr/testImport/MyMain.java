package com.sr.testImport;

import com.sr.testImport.config.MyConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);

		for(String beanName :ctx.getBeanDefinitionNames()){
			System.out.println("beanName:"+ beanName);
		}

		Object colorFactoryBean = ctx.getBean("colorFactoryBean");
		System.out.println(colorFactoryBean.getClass());

		Object factory = ctx.getBean("&colorFactoryBean");
		System.out.println(factory.getClass());
	}
}
