package com.sr.ioc.ioc4.testImport.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

@Configuration
@ComponentScan(basePackages = "com.sr.ioc.ioc4.testImport", excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class),
		@ComponentScan.Filter(type = FilterType.CUSTOM, classes = MyTypeFilter.class)
})
@Import(value = {
		MyBeanDefinitionRegister.class,
		ZooConfig.class,
		MyImportSelector.class
})
public class MyConfig {
}
