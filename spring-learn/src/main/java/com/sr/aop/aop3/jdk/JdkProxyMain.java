package com.sr.aop.aop3.jdk;

public class JdkProxyMain {

	public static void main(String[] args) {
		UserService userService = new UserServiceImpl();

		MyInvocationHandler invocationHandler = new MyInvocationHandler(userService);

		UserService proxy = (UserService) invocationHandler.getProxy();

		//System.out.println(proxy);

		//调用被代理的方法

		proxy.add();
	}
}
