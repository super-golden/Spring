package com.sr.aop;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan("com.sr.aop")
@EnableAspectJAutoProxy
public class MainConfig {
	@Bean
	public Calculate calculate() {
		return new Calculate();
	}
}
