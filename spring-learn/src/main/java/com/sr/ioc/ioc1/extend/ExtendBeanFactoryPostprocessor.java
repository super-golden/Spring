//package com.sr.extend;
//
//import com.sr.ioc.ioc1.app.service.UserService;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.GenericBeanDefinition;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ExtendBeanFactoryPostprocessor implements BeanFactoryPostProcessor {
//
//
//	@Override
//	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//
//		System.out.println("ExtendBeanFactoryPostprocessor ... postProcessBeanFactory start");
//
////		System.out.println(beanFactory);
////
////		GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanFactory.getBeanDefinition("indexService");
////
////		System.out.println(beanDefinition.getBeanClass());
////
////		beanDefinition.setBeanClass(UserService.class);
//
//		System.out.println("ExtendBeanFactoryPostprocessor ... postProcessBeanFactory end");
//	}
//}
