package com.sr.cache;



import com.google.common.cache.AbstractCache;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.sr.cache")
public class Main {

	public static void main(String[] args) throws Exception{
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

		UserDao bean = context.getBean(UserDao.class);
		System.out.println(bean.getUserName("zmc"));
		System.out.println(bean.getUserName("zmc"));
		Thread.sleep(1000);
		System.out.println(bean.getUserName("zmc"));
		System.out.println(bean.getUserName("zmc"));

		//System.out.println(bean.getUserName("zmc"));


	}
}
