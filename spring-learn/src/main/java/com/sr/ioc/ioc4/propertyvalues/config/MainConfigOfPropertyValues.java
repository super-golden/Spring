package com.sr.ioc.ioc4.propertyvalues.config;

import com.sr.ioc.ioc4.propertyvalues.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:propertyvalues.properties",encoding = "UTF-8")
public class MainConfigOfPropertyValues {

	@Bean
	public Person person(){
		return new Person();
	}


}
