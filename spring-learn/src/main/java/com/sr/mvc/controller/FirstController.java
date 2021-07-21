package com.sr.mvc.controller;

import com.sr.mvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
	@RequestMapping("/index")
	public ModelAndView firstIndex() {
		System.out.println(userService);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}
}
