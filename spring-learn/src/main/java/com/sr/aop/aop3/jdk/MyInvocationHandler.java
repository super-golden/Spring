package com.sr.aop.aop3.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 创建自定义的InvocationHandler,用于对接口提供的方法进行增强
 */
public class MyInvocationHandler implements InvocationHandler {

	//目标对象
	private Object target;

	public MyInvocationHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		System.out.println("-------------before---------------");

		//执行目标对象的方法
		Object result = method.invoke(target, args);

		//System.out.println(proxy);

		System.out.println("-------------after---------------");
		return result;
	}

	/**
	 * 获取目标对象的代理对象
	 *
	 * @return
	 */
	public Object getProxy() {
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				target.getClass().getInterfaces(), this);
	}
}
