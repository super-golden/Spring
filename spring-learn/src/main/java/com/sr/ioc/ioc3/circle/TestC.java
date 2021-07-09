package com.sr.ioc.ioc3.circle;

public class TestC {

	private TestA testA;

	public TestC() {
	}

	public TestC(TestA testA) {
		System.out.println("TestC 的构造方法");
		this.testA = testA;
	}

	public void c(){
		testA.a();
	}

	public TestA getTestA() {
		return testA;
	}

	public void setTestA(TestA testA) {
		this.testA = testA;
	}
}
