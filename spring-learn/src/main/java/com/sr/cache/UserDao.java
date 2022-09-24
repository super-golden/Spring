package com.sr.cache;

import org.springframework.stereotype.Component;

@Component
public class UserDao {

	@GuavaCache(group = "user", spelKey = "#name",expire = 1)
	public String getUserName(String name) {

		System.out.println("==========走数据库=============");
		return "hello" + name;
	}
}
