package com.sr.aop.aop4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.sr.aop.aop4")
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = false)
public class MainConfig {
	@Bean
	public Calculate calculate() {
		return new Calculate();
	}
}
