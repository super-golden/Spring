package com.sr.mvc;

import com.sr.mvc.dao.UserDao;
import com.sr.mvc.pojo.User;
import com.sr.mvc.service.UserService;
import org.apache.catalina.startup.Tomcat;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppMain {

	private static int port = 8080;
	private static String contextPath = "/";

	public static void start() throws Exception {
		Tomcat tomcat = new Tomcat();
		String baseDir = System.getProperty("user.dir") + "/spring-learn/src/main/webapp/";
		tomcat.setPort(port);
		tomcat.getConnector().setPort(port);
		tomcat.setBaseDir(baseDir);
		tomcat.addWebapp(contextPath, baseDir);
		tomcat.enableNaming();
		tomcat.start();
		tomcat.getServer().await();
	}

	public static void main(String[] args) throws Exception {
		//start();

		//加载配置文件

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

		//获取jdbcTemplate实例
//		JdbcTemplate jdbcTemplate = applicationContext.getBean("jdbcTemplate", JdbcTemplate.class);
//		System.out.println(jdbcTemplate);
//
//		//使用execute()方法执行SQL语句，创建用户账户管理表account
//
//		jdbcTemplate.execute("create table account(" +
//				"id int primary key auto_increment," +
//				"usrname varchar(50)," +
//				"balance double)");
//		System.out.println("账户表创建成功!");
//		SqlSessionFactory sqlSessionFactory =  applicationContext.getBean("sqlSessionFactory", SqlSessionFactory.class);
//		System.out.println(sqlSessionFactory);
//
//		UserDao userDao = applicationContext.getBean("userMapper", UserDao.class);
//		User userByID = userDao.getUserByID(1);
//
//		System.out.println(userByID.getName());

		UserService userService = applicationContext.getBean("userServiceImpl", UserService.class);
		userService.insertUser("samson");


	}

}
