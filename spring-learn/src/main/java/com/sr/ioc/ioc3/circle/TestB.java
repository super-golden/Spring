package com.sr.ioc.ioc3.circle;

public class TestB {

	private TestC testC;

	public TestB() {
	}

	public TestB(TestC testC) {
		System.out.println("TestB 的构造方法");
		this.testC = testC;
	}

	public void b(){
		testC.c();
	}

	public TestC getTestC() {
		return testC;
	}

	public void setTestC(TestC testC) {
		this.testC = testC;
	}
}
