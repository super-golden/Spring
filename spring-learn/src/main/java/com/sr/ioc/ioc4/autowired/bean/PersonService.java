package com.sr.ioc.ioc4.autowired.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

	@Autowired(required = false)
	private PersonDao personDao;

	public void print(){
		System.out.println(personDao);
	}
}
