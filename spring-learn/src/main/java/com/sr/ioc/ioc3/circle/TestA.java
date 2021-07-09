package com.sr.ioc.ioc3.circle;

public class TestA {

	private TestB testB;

	public TestA() {
	}

	public TestA(TestB testB) {
		System.out.println("TestA 的构造方法");
		this.testB = testB;
	}

	public void a(){
		testB.b();
	}

	public TestB getTestB() {
		return testB;
	}

	public void setTestB(TestB testB) {
		this.testB = testB;
	}
}
