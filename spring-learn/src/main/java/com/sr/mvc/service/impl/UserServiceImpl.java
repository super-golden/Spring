package com.sr.mvc.service.impl;

import com.sr.mvc.dao.UserDao;
import com.sr.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertUser(String userName) {

		int i = userDao.insertUser(userName);
		System.out.println("com.sr.mvc.service.impl.UserServiceImpl.insertUser invoke....");
		int test = 1 / 0;
		return 0;
	}
}
