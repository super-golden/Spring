package com.sr.ioc.ioc2;

import com.sr.ioc.ioc2.bean.UserManger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IOCMain3 {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("springApplication2.xml");
		UserManger user =(UserManger) context.getBean("user");
		System.out.println(user.getMessage());
		System.out.println(context);
	}
}
