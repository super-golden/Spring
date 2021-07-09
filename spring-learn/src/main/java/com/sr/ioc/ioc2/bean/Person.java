package com.sr.ioc.ioc2.bean;

import java.util.List;

public class Person {

	private String name;

	private List<String> hobbies;

	private Cat cat;

	public Person() {
		System.out.println("Person 的构造方法");
	}

	public Person(String name, List<String> hobbies) {
		this.name = name;
		this.hobbies = hobbies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getHobbies() {
		return hobbies;
	}

	public void setHobbies(List<String> hobbies) {
		this.hobbies = hobbies;
	}

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				", hobbies=" + hobbies +
				'}';
	}

	public Cat getCat() {
		return cat;
	}

	public void setCat(Cat cat) {
		this.cat = cat;
	}
}
