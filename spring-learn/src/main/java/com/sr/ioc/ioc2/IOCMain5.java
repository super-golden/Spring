package com.sr.ioc.ioc2;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class IOCMain5 {

	public static void main(String[] args) {
		FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext("/Users/sailinna/Documents/spring-framework-master/spring-learn/src/main/resources/springApplication-xml2.xml");
		System.out.println(fileSystemXmlApplicationContext);
	}
}
