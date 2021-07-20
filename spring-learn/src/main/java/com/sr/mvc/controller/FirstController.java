package com.sr.mvc.controller;

import com.sr.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/first")
public class FirstController {

	@Autowired
	private UserService userService;

	@RequestMapping("/test")
	@ResponseBody
	public String firstTest() {
		System.out.println(userService);
		return "hello tomcat!!";
	}
}
