package com.sr.aop.springaop;

public class AServiceImpl implements AService {
	@Override
	public void barA() {
		System.out.println("AServiceImpl.barA()");
		this.barB();
	}
	@Override
	public void barB() {
		System.out.println("AServiceImpl.barB()");
	}
}
