package com.sr.ioc.ioc3.constructorarg;

public class HelloBean {

	private String name;

	private String msg;

	public HelloBean(String name, String msg) {
		this.name = name;
		this.msg = msg;
	}

	public void showInfo() {
		System.out.println("--" + msg + "---" + name);
	}
}
