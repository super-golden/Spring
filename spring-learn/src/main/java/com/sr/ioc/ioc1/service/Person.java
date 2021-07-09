package com.sr.ioc.ioc1.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class Person implements FactoryBean<Person> {
	public Person() {
		System.out.println("Person 的构造方法");
	}

	@Override
	public Person getObject() throws Exception {
		return null;
	}

	@Override
	public Class<?> getObjectType() {
		return null;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
