package com.sr.aop.aop3.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class EnhancerDemo {

	public static void main(String[] args) {

		Enhancer enhancer = new Enhancer();

		enhancer.setSuperclass(EnhancerDemo.class);
		enhancer.setCallback(new MethodInterceptorImpl());
		EnhancerDemo demo = (EnhancerDemo) enhancer.create();
		demo.test();
		System.out.println(demo);

	}

	public void test() {
		System.out.println("EnhancerDemo test()");
	}

	private static class MethodInterceptorImpl implements MethodInterceptor {
		@Override
		public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

			System.out.println("Before invoke " + method);
			Object reslut = methodProxy.invokeSuper(o, objects);
			System.out.println("After invoke " + method);

			return reslut;
		}
	}

}
