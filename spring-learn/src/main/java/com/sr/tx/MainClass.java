package com.sr.tx;

import com.sr.tx.config.MainConfig;
import com.sr.tx.service.PayService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainClass {

	public static void main(String[	] args) {


		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);

		PayService payService = (PayService)ctx.getBean("payServiceImpl");

		payService.pay("123456",10);

		System.out.printf("payService: "+ payService);

	}
}
