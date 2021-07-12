package com.sr.ioc.ioc1.app;

import com.sr.ioc.ioc1.service.IndexService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.sr.ioc.ioc1")
//AOP支持
@EnableAspectJAutoProxy
public class AppConfig {


	@Bean(initMethod = "init")
	public IndexService indexService(){
		return new IndexService();
	}
}
