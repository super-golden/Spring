package com.sr.ioc.ioc4.testImport.config;

import com.sr.ioc.ioc4.testImport.beans.TiddyDog;
import com.sr.ioc.ioc4.testImport.factory.ColorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZooConfig {

	@Bean
	public TiddyDog tiddyDog() {
		return new TiddyDog();
	}


	@Bean
	public ColorFactoryBean colorFactoryBean(){
		return new ColorFactoryBean();
	}
}
