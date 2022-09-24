package com.sr.ioc.ioc4.lifecycle;

import com.sr.ioc.ioc4.lifecycle.config.MainConfigOfLifeCycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LifeCycleMain {


	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
		System.out.println("容器创建完成....");
		context.start();
		String[] beanDefinitionNames = context.getBeanDefinitionNames();

		for (String beanDefinitionName : beanDefinitionNames) {
			System.out.println(beanDefinitionName);
		}

		context.close();

	}
}
