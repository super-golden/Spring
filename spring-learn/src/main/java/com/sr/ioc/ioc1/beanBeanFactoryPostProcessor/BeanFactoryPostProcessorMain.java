package com.sr.ioc.ioc1.beanBeanFactoryPostProcessor;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class BeanFactoryPostProcessorMain {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-beanFactoryPostProcessor.xml");

		//XmlBeanFactory context = new XmlBeanFactory(new ClassPathResource("spring-beanFactoryPostProcessor.xml"));
		HelloMessage message = (HelloMessage) context.getBean("message");
		System.out.println(message.getName());

//		String[] postProcessorNames = context.getBeanNamesForType(HelloMessage.class, true, false);
//		for (String name : postProcessorNames) {
//			System.out.println(name);
//		}

//		ExpressionParser parser = new SpelExpressionParser();
//		EvaluationContext context1 = new StandardEvaluationContext();
//		System.out.println(parser.parseExpression(message.getName()).getValue(context1,String.class));

	}
}
