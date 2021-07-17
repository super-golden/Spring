package com.sr.aop.springaop;

public class TestTarget implements AbcInterface {
	@Override
	public void add() {

		System.out.println("com.sr.aop.springaop.TestTarget.add invoke... ");
	}
}
