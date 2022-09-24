package com.sr.aop.aop4;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);

		Calculate calculate =(Calculate) ctx.getBean("calculate");

		calculate.div(10,6);
	}
}
