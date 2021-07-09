package com.sr.ioc.ioc3.annotation.dao;

import org.springframework.stereotype.Component;

@Component
public class UserDao {

	public void showUser(){
		System.out.println("UserDao showUser()");
	}
}
