package com.sr.ioc.ioc2.application;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class MyClassPathXmlApplicationContent extends ClassPathXmlApplicationContext {

	public MyClassPathXmlApplicationContent(String configLocation) throws BeansException {
		super(configLocation);
	}

	@Override
	protected void initPropertySources() {
		//添加验证要求
		ConfigurableEnvironment environment = getEnvironment();
		System.out.println(environment);
		environment.setRequiredProperties("Var");
	}

	@Override
	protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
		super.setAllowBeanDefinitionOverriding(false);
		super.setAllowCircularReferences(false);
		super.customizeBeanFactory(beanFactory);
	}
}
