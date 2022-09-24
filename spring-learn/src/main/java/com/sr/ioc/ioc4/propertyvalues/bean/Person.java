package com.sr.ioc.ioc4.propertyvalues.bean;

import org.springframework.beans.factory.annotation.Value;

public class Person {

	/**
	 * 使用@Value赋值
	 * 1、基本数值
	 * 2、可以写SpEL：#{}
	 * 3、可以写${}:取出配置文件中的值（在运行环境变量的值）
	 */
	@Value("zhangsan")
	private String name;

	@Value("#{20-2}")
	private Integer age;

	@Value("${person.nickName}")
	private String nickName;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				", age=" + age +
				", nickName='" + nickName + '\'' +
				'}';
	}
}
