package com.sr.cache;

import org.springframework.stereotype.Component;

@Component
public class UserDao {

	@GuavaCache(spelKey = "#name")
	public String getUserName(String name) {

		System.out.println("==========走数据库=============");
		return "hello" + name;
	}
}
