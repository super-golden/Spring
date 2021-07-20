package com.sr.mvc;

import org.apache.catalina.startup.Tomcat;

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
		start();
	}

}
