package com.sr.ioc.ioc2;

import com.sr.ioc.ioc2.bean.SimpleBean;
import com.sr.ioc.ioc2.bean.UserManger;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class IOCMain4 {

	public static void main(String[] args) {

		ConfigurableListableBeanFactory bf = new XmlBeanFactory(new ClassPathResource("springApplication3.xml"));
		System.out.println(bf);
		SimpleBean simpleBean = bf.getBean("simpleBean", SimpleBean.class);
		System.out.println(simpleBean);
//		BeanFactoryPostProcessor bfpp = (BeanFactoryPostProcessor) bf.getBean("bfpp");
//		bfpp.postProcessBeanFactory(bf);
//		System.out.println(bf.getBean("simpleBean"));


	}
}
