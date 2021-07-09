package com.sr.ioc.ioc1.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

//@Component
//@Lazy(true)
public class IndexService implements InitializingBean, ApplicationContextAware {

	@Autowired
	private UserService userService;

	public IndexService() {

		System.out.println("Construct from IndexService");
	}

	public void getUserService() {
		System.out.println("Service logic");
		//System.out.println(userService);
	}

	/*
	Java中该注解的说明：@PostConstruct该注解被用来修饰一个非静态的void（）方法。
	被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
	PostConstruct在构造函数之后执行，init（）方法之前执行。
	通常我们会是在Spring框架中使用到@PostConstruct注解 该注解的方法在整个Bean初始化中的执行顺序：

	Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)

	 */
	@PostConstruct
	public void construct(){
		System.out.println("PostConstruct ::" + userService);
	}

	public void init(){
		System.out.println("init 方法");
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("InitializingBean  afterPropertiesSet");

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("IndexService Aware");
	}
}
