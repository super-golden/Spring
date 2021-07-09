package com.sr.ioc.ioc2.converter;

import com.sr.ioc.ioc2.bean.UserManger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConverterMain {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("springApplication-converter.xml");

		UserManger user = context.getBean("user",UserManger.class);
		System.out.println(user.getDataValue());
		context.stop();
	}
}
