package com.sr.ioc.ioc1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@Primary
public class UserService {

	@Autowired
	private IndexService indexService;

	public UserService() {
		System.out.println("Construct from UserService");
	}

	public IndexService getIndexService() {
		return indexService;
	}
}
