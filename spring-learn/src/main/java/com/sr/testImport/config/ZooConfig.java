package com.sr.testImport.config;

import com.sr.testImport.beans.TiddyDog;
import com.sr.testImport.factory.ColorFactoryBean;
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
