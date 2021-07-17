package com.sr.aop.springaop;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class TestAdvisor implements MethodBeforeAdvice {


	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		System.out.println(method.getName());
		System.out.println("com.sr.aop.springaop.TestAdvisor.before invoke ...");
	}
}
