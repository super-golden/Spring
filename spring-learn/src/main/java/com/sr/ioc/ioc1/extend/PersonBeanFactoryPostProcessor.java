//package com.sr.extend;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.GenericBeanDefinition;
//import org.springframework.context.annotation.Bean;
//
//public class PersonBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
//	@Override
//	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//
//		 GenericBeanDefinition beanDefinition = (GenericBeanDefinition)beanFactory.getBeanDefinition("person");
//		 beanDefinition.setLazyInit(false);
//	}
//}
