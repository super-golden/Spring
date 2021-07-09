package com.sr.ioc.ioc3.annotation.service;

import com.sr.ioc.ioc3.annotation.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

	@Autowired()
	private UserDao userDao;

	public void show() {
		userDao.showUser();
	}

}
