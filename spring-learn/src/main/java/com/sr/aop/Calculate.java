package com.sr.aop;

public class Calculate {

	public int add(int numA, int numB) {
		System.out.println("执行目标方法：add");
		return numA + numB;
	}

	public int reduce(int numA, int numB) {
		System.out.println("执行目标方法：reduce");
		return numA - numB;
	}

	public int div(int numA, int numB) {
		System.out.println("执行目标方法：div");
		return numA / numB;
	}

	public int multi(int numA, int numB) {
		System.out.println("执行目标方法：multi");
		return numA * numB;
	}
}
