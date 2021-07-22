package com.sr.mvc.dao;

import com.sr.mvc.pojo.User;

public interface UserDao extends BaseDao {
	User getUserByID (int id);

	int insertUser(String userName);

}
