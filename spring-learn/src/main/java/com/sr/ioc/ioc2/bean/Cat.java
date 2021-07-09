package com.sr.ioc.ioc2.bean;

public class Cat {

	private String name;

	private Person person;

	public Cat() {
		System.out.println("Cat 的构造方法");
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
