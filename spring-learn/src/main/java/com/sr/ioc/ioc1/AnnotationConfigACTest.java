package com.sr.ioc.ioc1;

import com.sr.ioc.ioc1.app.AppConfig;
import com.sr.ioc.ioc1.service.IndexService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationConfigACTest {
	public static void main(String[] args) {

		/*
		1、new -- instanceWrapper = createBeanInstance(beanName, mbd, args);
		2、注入 -- populateBean(beanName, mbd, instanceWrapper);
		3、init生命周期初始化方法
		4、代理AOP
		5、put singletonObjects
		 */
		/**
		 * 初始化Spring容器
		 */
		AnnotationConfigApplicationContext acc=
				new AnnotationConfigApplicationContext();
		//false:禁止循环依赖
		acc.setAllowCircularReferences(true);
		acc.register(AppConfig.class);
		acc.refresh();
		//IndexService bean = acc.getBean(IndexService.class);
		//System.out.println(bean);
		//bean.getUserService();
		//UserService userService = acc.getBean(UserService.class);
		//System.out.println(userService);
		IndexService bean = acc.getBean(IndexService.class);
		System.out.println(bean);

		System.out.println(acc.containsBean("person"));


		//acc.getBean(UserService.class).getIndexService().getUserService();
        //关闭容器
        acc.close();






//		BeanDefinition beanDefinition = applicationContext.getBeanDefinition("userService");
//		System.out.println(beanDefinition);

	}


}
