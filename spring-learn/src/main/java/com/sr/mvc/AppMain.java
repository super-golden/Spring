package com.sr.mvc;

import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class AppMain {
	//protected  static final Log logger = LogFactory.getLog(getClass());
	private static int port = 8080;
	private static String contextPath = "/";

	public static void start() throws Exception {
		//logger.info("=================开始加载内嵌tomcat=====================");
		Tomcat tomcat = new Tomcat();

		String docBase = "/Users/sailinna/Documents/spring-framework-master/spring-learn/src/main/webapp/";
		tomcat.setPort(port);
		tomcat.getConnector().setPort(port);
		tomcat.addWebapp(contextPath, new File(docBase).getAbsolutePath());
		tomcat.enableNaming();
		tomcat.start();
//        logger.info("==================tomcat加载成功==========================");
//        logger.info("测试地址：http://localhost:8080/hello");
		tomcat.getServer().await();
	}

	public static void main(String[] args) throws Exception {
		start();
		//System.getProperty("user.dir");

	}

}
